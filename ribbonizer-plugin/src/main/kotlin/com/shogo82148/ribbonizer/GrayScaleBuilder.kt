package com.shogo82148.ribbonizer

import com.shogo82148.ribbonizer.filter.GrayScaleFilter
import com.shogo82148.ribbonizer.plugin.Variant
import com.shogo82148.ribbonizer.resource.Resource
import java.io.File
import java.util.function.Consumer

class GrayScaleBuilder : FilterBuilder {
    override fun apply(applicationVariant: Variant, iconFile: File): Consumer<Resource> {
        return GrayScaleFilter()
    }
}