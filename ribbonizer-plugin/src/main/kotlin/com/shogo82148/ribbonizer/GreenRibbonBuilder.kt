package com.shogo82148.ribbonizer

import com.android.build.api.variant.Variant
import com.shogo82148.ribbonizer.filter.ColorRibbonFilter
import com.shogo82148.ribbonizer.resource.Resource
import java.awt.Color
import java.io.File
import java.util.function.Consumer

class GreenRibbonBuilder: FilterBuilder {
    companion object {
        val COLOR: Color = Color(0, 0x72, 0, 0x99)
    }
    override fun apply(variant: Variant, iconFile: File): Consumer<Resource> {
        return ColorRibbonFilter(variant.buildType!!, COLOR)
    }
}