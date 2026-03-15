package org.amadla.hery

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.jetbrains.yaml.YAMLTokenTypes

class HeryBraceMatcher : PairedBraceMatcher {

    companion object {
        private val PAIRS = arrayOf(
            BracePair(YAMLTokenTypes.LBRACE, YAMLTokenTypes.RBRACE, true),
            BracePair(YAMLTokenTypes.LBRACKET, YAMLTokenTypes.RBRACKET, true),
        )
    }

    override fun getPairs(): Array<BracePair> = PAIRS
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}
