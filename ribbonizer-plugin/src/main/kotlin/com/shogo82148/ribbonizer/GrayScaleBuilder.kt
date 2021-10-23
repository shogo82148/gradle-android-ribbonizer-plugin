package com.shogo82148.ribbonizer

import com.android.build.api.variant.ApplicationVariantBuilder
import com.shogo82148.ribbonizer.filter.GrayScaleFilter
import com.shogo82148.ribbonizer.resource.Resource
import java.io.File
import java.util.function.Consumer

class GrayScaleBuilder : FilterBuilder {
    override fun apply(applicationVariant: ApplicationVariantBuilder, iconFile: File): Consumer<Resource> {
        return GrayScaleFilter()
    }
}