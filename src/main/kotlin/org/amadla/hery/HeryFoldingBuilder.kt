package org.amadla.hery

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

/**
 * Provides folding for root-level HERY blocks (_meta, _body, _requires)
 * and nested YAML mappings.
 */
class HeryFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        PsiTreeUtil.findChildrenOfType(root, YAMLKeyValue::class.java).forEach { keyValue ->
            val key = keyValue.keyText
            val valueNode = keyValue.value ?: return@forEach

            // Only fold multi-line values
            val startLine = document.getLineNumber(keyValue.textRange.startOffset)
            val endLine = document.getLineNumber(valueNode.textRange.endOffset)
            if (endLine > startLine) {
                descriptors.add(FoldingDescriptor(
                    keyValue.node,
                    TextRange(keyValue.textRange.startOffset, valueNode.textRange.endOffset)
                ))
            }
        }

        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        val element = node.psi
        if (element is YAMLKeyValue) {
            return "${element.keyText}: ..."
        }
        return "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
