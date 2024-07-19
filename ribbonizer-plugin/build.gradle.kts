/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Gradle plugin project to get you started.
 * For more details take a look at the Writing Custom Plugins chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.0.2/userguide/custom_plugins.html
 */

plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.9.25"

    // for publishing to Maven Central
    id("signing")
    id("maven-publish")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use Android Gradle plugin
    implementation("com.android.tools.build:gradle:8.2.2")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}


group = "com.shogo82148.ribbonizer"
version = "8.1.0"

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.compileJava {
    options.release.set(17)
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            artifactId = "ribbonizer-plugin"
            version = version
            pom.configureForRibbonizer("ribbonizer-plugin")
        }
        repositories {
            maven {
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                val v = version as String
                url = if (v.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                credentials {
                    if (hasProperty("sonatypeUsername")) {
                        username = findProperty("sonatypeUsername") as String
                    }
                    if (hasProperty("sonatypePassword")) {
                        password = findProperty("sonatypePassword") as String
                    }
                }
            }
        }
    }

    // sign Plugin Marker Artifacts
    // https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_markers
    publications {
        afterEvaluate {
            named<MavenPublication>("ribbonizer-pluginPluginMarkerMaven") {
                signing.sign(this)
                pom.configureForRibbonizer("ribbonizer-plugin")
            }
        }
    }
}

// comes from https://github.com/runningcode/gradle-doctor/blob/0e78bc8f304007bb0def37f72d6416947e58379a/doctor-plugin/build.gradle.kts#L115-L136
fun org.gradle.api.publish.maven.MavenPom.configureForRibbonizer(pluginName: String) {
    name.set(pluginName)
    description.set("Modifies launcher icons of Android apps on debug build")
    url.set("https://github.com/shogo82148/gradle-android-ribbonizer-plugin")
    licenses {
        license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }
    developers {
        developer {
            id.set("shogo82148")
            name.set("ICHINOSE Shogo")
            email.set("shogo82148@gmail.com")
        }
    }
    scm {
        connection.set("git@github.com:shogo82148/gradle-android-ribbonizer-plugin.git")
        developerConnection.set("git@github.com:shogo82148/gradle-android-ribbonizer-plugin.git")
        url.set("https://github.com/shogo82148/gradle-android-ribbonizer-plugin")
    }
}

signing {
    sign(publishing.publications["pluginMaven"])
}

gradlePlugin {
   plugins {
       // Define the plugin
       create("ribbonizer-plugin") {
           id = "com.shogo82148.ribbonizer"
           implementationClass = "com.shogo82148.ribbonizer.plugin.RibbonizerPlugin"
       }
   }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}
