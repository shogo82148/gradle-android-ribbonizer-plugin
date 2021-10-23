package com.shogo82148.ribbonizer

import com.android.build.api.variant.Variant
import com.shogo82148.ribbonizer.resource.Resource
import java.io.File
import java.util.function.BiFunction
import java.util.function.Consumer

interface FilterBuilder: BiFunction<Variant, File, Consumer<Resource>?> {
}