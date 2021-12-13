package com.shogo82148.ribbonizer.plugin

// Variant is used from Gradle DSL.
@Suppress("unused")
class Variant(
    val name: String,
    val debuggable: Boolean,
    val buildType: String,
    val versionCode: Int,
    val versionName: String,
    val flavorName: String
    ) {
}