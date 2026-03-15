package org.amadla.hery

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class HeryFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, HeryLanguage.INSTANCE) {
    override fun getFileType(): FileType = HeryFileType.INSTANCE
    override fun toString(): String = "HERY File"
}
