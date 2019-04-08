package com.github.gfx.ribbonizer.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.github.gfx.ribbonizer.FilterBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.stream.Stream

class RibbonizerTask extends DefaultTask {

    static final String NAME = "ribbonize"

    ApplicationVariant variant

    //@OutputDirectory
    File outputDir

    // `iconNames` includes: "@drawable/icon", "@mipmap/ic_launcher", etc.
    Set<String> iconNames

    List<FilterBuilder> filterBuilders = []

    @TaskAction
    public void run() {
        if (filterBuilders.size() == 0) {
            return
        }

        def t0 = System.currentTimeMillis()

        def names = new HashSet<String>(iconNames)
        names.addAll(launcherIconNames)

        def adaptiveIcons = getAdaptiveIconNames(names)
        names.addAll(adaptiveIcons)

        variant.sourceSets.stream()
                .flatMap { sourceProvider -> sourceProvider.resDirectories.stream() }
                .filter { resDir -> resDir != outputDir }
                .forEach { File resDir ->

            names.forEach { String name ->
                project.fileTree(
                        dir: resDir,
                        include: Resources.resourceFilePattern(name),
                        exclude: "**/*.xml",
                ).forEach { File inputFile ->
                    info "process $inputFile"

                    def basename = inputFile.name
                    def resType = inputFile.parentFile.name
                    def outputFile = new File(outputDir, "${resType}/${basename}")
                    outputFile.parentFile.mkdirs()

                    def ribbonizer = new Ribbonizer(inputFile, outputFile)
                    ribbonizer.process filterBuilders.stream().map { filterBuilder -> filterBuilder.apply(variant, inputFile) }
                    ribbonizer.save()
                }
            }
        }

        info("task finished in ${System.currentTimeMillis() - t0}ms")
    }

    void info(String message) {
        //System.out.println("[$name] $message")
        project.logger.info("[$name] $message")
    }

    Set<String> getLauncherIconNames() {
        def names = new HashSet<String>()
        androidManifestFiles.forEach { File manifestFile ->
            names.addAll(Resources.getLauncherIcons(manifestFile))
        }
        return names
    }

    Stream<File> getAndroidManifestFiles() {
        AppExtension android = project.extensions.findByType(AppExtension)

        return ["main", variant.name, variant.buildType.name, variant.flavorName].stream()
                .filter({ name -> !name.empty })
                .distinct()
                .map({ name -> project.file(android.sourceSets[name].manifest.srcFile) })
                .filter({ manifestFile -> manifestFile.exists() })
    }

    // finds adaptive icons and returns foreground resource names.
    Set<String> getAdaptiveIconNames(HashSet<String> icons) {
        def names = new HashSet<String>()
        variant.sourceSets.stream()
                .flatMap { sourceProvider -> sourceProvider.resDirectories.stream() }
                .filter { resDir -> resDir != outputDir }
                .forEach { resDir ->

            icons.forEach { String name ->
                project.fileTree(
                        dir: resDir,
                        include: Resources.resourceFilePattern(name),
                ).filter { inputFile -> inputFile.name.endsWith(".xml") }
                .forEach { File inputFile ->
                    names.addAll(Resources.getAdaptiveIcons(inputFile))
                }
            }
        }
        return names
    }
}