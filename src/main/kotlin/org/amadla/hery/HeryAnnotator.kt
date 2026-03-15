package org.amadla.hery

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLScalar

/**
 * Annotates HERY files with:
 * - Warnings for unknown root-level _-prefixed properties
 * - Warnings for reserved properties used inside _body or _meta
 * - Highlighting for entity URIs in values
 */
class HeryAnnotator : Annotator {

    companion object {
        private val RESERVED_PROPERTIES = setOf("_type", "_extends", "_meta", "_body", "_requires")

        private val ENTITY_URI_REGEX = Regex(
            """[a-zA-Z0-9._-]+\.[a-zA-Z]{2,}/[a-zA-Z0-9_./#-]+(@[\^~]?v?[0-9][0-9a-zA-Z.*-]*|@latest)?"""
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is YAMLKeyValue) return

        val key = element.keyText

        // Check if this is a root-level key (direct child of document mapping)
        val parentMapping = element.parent as? YAMLMapping ?: return
        val isRootLevel = parentMapping.parent is YAMLDocument

        if (isRootLevel) {
            // Warn about unknown _-prefixed root properties
            if (key.startsWith("_") && key !in RESERVED_PROPERTIES) {
                holder.newAnnotation(
                    HighlightSeverity.WARNING,
                    "Unknown reserved property '$key'. HERY reserved properties are: ${RESERVED_PROPERTIES.joinToString(", ")}"
                ).range(element.key!!.textRange).create()
            }
        } else {
            // Warn about reserved properties used inside _body or _meta
            if (key in RESERVED_PROPERTIES) {
                val ancestorKey = findAncestorReservedKey(element)
                if (ancestorKey != null) {
                    holder.newAnnotation(
                        HighlightSeverity.WARNING,
                        "Reserved property '$key' should not appear inside '$ancestorKey'"
                    ).range(element.key!!.textRange).create()
                }
            }
        }

        // Highlight entity URIs in scalar values
        val value = element.value
        if (value is YAMLScalar) {
            val text = value.textValue
            ENTITY_URI_REGEX.findAll(text).forEach { match ->
                val valueOffset = value.textRange.startOffset
                // Account for quote characters
                val textStart = value.text.indexOf(text)
                if (textStart >= 0) {
                    val matchStart = valueOffset + textStart + match.range.first
                    val matchEnd = valueOffset + textStart + match.range.last + 1
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(com.intellij.openapi.util.TextRange(matchStart, matchEnd))
                        .textAttributes(HerySyntaxHighlighter.ENTITY_URI)
                        .create()
                }
            }
        }
    }

    private fun findAncestorReservedKey(element: PsiElement): String? {
        var current = element.parent
        while (current != null) {
            if (current is YAMLKeyValue && current.keyText in setOf("_body", "_meta")) {
                return current.keyText
            }
            current = current.parent
        }
        return null
    }
}
