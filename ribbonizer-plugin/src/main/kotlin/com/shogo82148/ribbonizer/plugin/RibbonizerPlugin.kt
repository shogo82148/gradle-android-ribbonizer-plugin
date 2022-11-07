/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.shogo82148.ribbonizer.plugin

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.GreenRibbonBuilder
import com.shogo82148.ribbonizer.resource.Variant
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.TaskProvider
import java.io.File
import java.util.*

class RibbonizerPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        // add RibbonizerExtension
        project.extensions.add(RibbonizerExtension.NAME, RibbonizerExtension::class.java)

        val androidComponents = project.extensions.findByType(ApplicationAndroidComponentsExtension::class.java)
            ?: throw Exception("Not an Android application; you forget `apply plugin: 'com.android.application`?")
        val extension = project.extensions.findByType(RibbonizerExtension::class.java)!!

        val tasks = mutableListOf<TaskProvider<RibbonizerTask>>()
        androidComponents.onVariants { variant ->
            // parse AndroidManifest
            val manifest = File(project.projectDir, "src/main/AndroidManifest.xml")
            val icons = Resources.launcherIcons(manifest)

            // find icon files
            val iconFiles = Resources.findResourceFiles(project.projectDir, icons)

            var filterBuilders = extension.filterBuilders
            if (filterBuilders.isEmpty()) {
                filterBuilders = listOf(GreenRibbonBuilder() as FilterBuilder)
            }

            val myVariant = Variant(
                debuggable = true, // TODO: fix me
                name = variant.name,
                buildType = variant.buildType ?: "",
                versionCode = 0, // TODO: fix me
                versionName = "", // TODO: fix me
                flavorName = variant.flavorName ?: ""
            )

            // create a new task
            val capitalizedName = capitalize(variant.name)
            val name = "${RibbonizerTask.NAME}${capitalizedName}"
            val task = project.tasks.register(name, RibbonizerTask::class.java) {
                it.iconFiles.set(iconFiles)
                it.filterBuilders.set(filterBuilders)
                it.variant.set(myVariant)
            }
            variant.sources.res?.addGeneratedSourceDirectory(task, RibbonizerTask::outputDir)
            tasks.add(task)
        }
        project.task(mapOf("dependsOn" to tasks), RibbonizerTask.NAME)
    }
}

fun capitalize(string: String): String {
    return string.substring(0, 1).uppercase(Locale.ROOT) + string.substring(1)
}
