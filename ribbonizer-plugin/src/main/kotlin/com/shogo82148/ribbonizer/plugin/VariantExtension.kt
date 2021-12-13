package com.shogo82148.ribbonizer.plugin

import org.gradle.api.provider.Property

// the result of collecting information beforeVariants
interface VariantExtension {
    val debuggable: Property<Boolean>
}