package com.shogo82148.ribbonizer.plugin

import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.resource.Resource
import org.gradle.api.Project
import org.gradle.api.file.Directory
import java.io.File

class Ribbonizer (
    private val name: String,
    private val project: Project,
    private val variant: Variant,
    private val res: List<Collection<Directory>>,
    private val outputDir: File,
    private val filterBuilders: List<FilterBuilder>
) {
    fun process(resource: Resource) {
        try {
            val file = resource.file
            info("process $file")

            val basename = file.name
            val resType = file.parentFile.name
            val outputFile = File(outputDir, "$resType/$basename")
            outputFile.parentFile.mkdirs()
            filterBuilders.forEach { filterBuilder: FilterBuilder ->
                val filter = filterBuilder.apply(variant, file)
                filter?.accept(resource)
            }
            info("saving to $outputFile")
            resource.save(outputFile)
        } catch (e: Exception) {
            info("Exception: $e")
        }
    }

    fun findResourceFiles(name: String): List<File> {
        info("finding $name...")
        val files = ArrayList<File>()

        if (name.startsWith("@")) {
            val pair = name.substring(1).split("/", limit = 2)
            val baseResType = pair[0]
            val filename = pair[1]
            findResType(res, baseResType).forEach { dir ->
                dir.listFiles()?.forEach { file ->
                    if (file.nameWithoutExtension == filename) {
                        files.add(file)
                    }
                }
            }
        }
        return files
    }

    private fun findResType(baseDir: List<Collection<Directory>>, baseResType: String): List<File> {
        val dirs = ArrayList<File>()
        baseDir.flatten().forEach { dir ->
            val resDir = dir.asFile
            dirs.addAll(findResType(resDir, baseResType))
        }
        return dirs
    }

    private fun findResType(baseDir: File, baseResType: String): List<File> {
        val dirs = ArrayList<File>()
        if (baseDir == outputDir) {
            return dirs
        }
        if (!baseDir.exists()) {
            return dirs
        }
        baseDir.listFiles()?.forEach {
            if (!it.isDirectory) {
                return@forEach
            }
            if (matchResType(it, baseResType)) {
                dirs.add(it)
            } else {
                dirs.addAll(findResType(it, baseResType))
            }
        }
        return dirs
    }

    private fun matchResType(file: File, baseResType: String): Boolean {
        if (!file.isDirectory) {
            return false
        }
        val pair = file.name.split("-", limit=2)
        return pair[0] == baseResType
    }

    private fun info(message: String) {
        project.logger.info("[$name] $message")
    }
}