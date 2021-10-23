package com.shogo82148.ribbonizer

import com.android.build.api.variant.ApplicationVariantBuilder
import com.shogo82148.ribbonizer.filter.ColorRibbonFilter
import com.shogo82148.ribbonizer.resource.Resource
import java.awt.Color
import java.io.File
import java.util.function.Consumer

class YellowRibbonBuilder : FilterBuilder {
    companion object {
        val COLOR: Color = Color(0xff, 0x76, 0, 0x99)
    }

    override fun apply(variant: ApplicationVariantBuilder, iconFile: File): Consumer<Resource> {
        return ColorRibbonFilter(variant.buildType ?: "", COLOR)
    }
}