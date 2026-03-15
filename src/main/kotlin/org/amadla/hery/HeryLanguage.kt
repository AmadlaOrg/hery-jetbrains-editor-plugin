package org.amadla.hery

import com.intellij.lang.Language

class HeryLanguage private constructor() : Language("hery", "text/x-hery") {
    companion object {
        @JvmField
        val INSTANCE = HeryLanguage()
    }

    override fun getDisplayName(): String = "HERY"
    override fun isCaseSensitive(): Boolean = true
}
