package org.amadla.hery

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class HeryFileType private constructor() : LanguageFileType(HeryLanguage.INSTANCE) {
    companion object {
        @JvmField
        val INSTANCE = HeryFileType()
    }

    override fun getName(): String = "HERY"
    override fun getDescription(): String = "HERY (Hierarchical Entity Relational YAML) file"
    override fun getDefaultExtension(): String = "hery"
    override fun getIcon(): Icon = HeryIcons.FILE
}
