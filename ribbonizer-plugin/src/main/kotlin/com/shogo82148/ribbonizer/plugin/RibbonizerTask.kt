package com.shogo82148.ribbonizer.plugin

import com.android.build.api.variant.ApplicationVariantBuilder
import com.android.build.gradle.AppExtension
import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.resource.AdaptiveIcon
import com.shogo82148.ribbonizer.resource.ImageIcon
import java.io.File
import java.util.stream.Stream
import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.*

abstract class RibbonizerTask : DefaultTask() {
    @ExperimentalStdlibApi
    @TaskAction
    fun run() {
        info("\uD83D\uDC7A\uD83D\uDC7A\uD83D\uDC7A\uD83D\uDC7A\uD83D\uDC7A")
        info(outputDir.get().toString())
//        if (filterBuilders.get().isEmpty()) {
//            return
//        }
//        val t0 = System.currentTimeMillis()
//        val names = HashSet(iconNames.get())
//        names.addAll(launcherIconNames)
//        val ribbonizer = Ribbonizer(name, project, variant.get(), outputDir.get().asFile, filterBuilders.get())
//        names.forEach { name: String ->
//            ribbonizer.findResourceFiles(name).forEach {
//                when (it.extension) {
//                    "xml" -> {
//                        val icon = AdaptiveIcon(ribbonizer, it)
//                        ribbonizer.process(icon)
//                    }
//                    "png" -> {
//                        val icon = ImageIcon(ribbonizer, it)
//                        ribbonizer.process(icon)
//                    }
//                }
//            }
//        }
//        info("task finished in " + (System.currentTimeMillis() - t0) + "ms")
    }

    private fun info(message: String) {
        project.logger.info("[$name] $message")
    }

    private val launcherIconNames: Set<String>
        get() {
            val names = HashSet<String>()
            try {
                names.addAll(Resources.launcherIcons(manifest.get().asFile))
            } catch (e: Exception) {
                info("Exception: $e")
            }
            return names
        }

    @get:Internal
    abstract val variant: Property<Variant>

    @get:InputFile
    abstract val manifest: RegularFileProperty

    @get:InputFiles
    abstract val assets: ListProperty<Collection<Directory>>

    @get:OutputFile
    abstract val outputDir: DirectoryProperty

    @get:Internal
    abstract val iconNames: SetProperty<String>

    @get:Internal
    abstract val filterBuilders: ListProperty<FilterBuilder>

    companion object {
        const val NAME = "ribbonize"
    }
}
