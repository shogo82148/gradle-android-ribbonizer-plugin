package com.shogo82148.ribbonizer

import com.shogo82148.ribbonizer.plugin.Variant
import com.shogo82148.ribbonizer.resource.Resource
import java.io.File
import java.util.function.BiFunction
import java.util.function.Consumer

interface FilterBuilder: BiFunction<Variant, File, Consumer<Resource>?> {
}