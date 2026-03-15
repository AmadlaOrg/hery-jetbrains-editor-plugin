package org.amadla.hery

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.jetbrains.yaml.YAMLHighlighter
import org.jetbrains.yaml.YAMLTokenTypes

class HerySyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        // HERY-specific text attributes
        val RESERVED_PROPERTY = createTextAttributesKey(
            "HERY_RESERVED_PROPERTY",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        val ENTITY_URI = createTextAttributesKey(
            "HERY_ENTITY_URI",
            DefaultLanguageHighlighterColors.STRING
        )
        val VERSION = createTextAttributesKey(
            "HERY_VERSION",
            DefaultLanguageHighlighterColors.NUMBER
        )

        // Re-export YAML attributes for non-HERY tokens
        val KEY = createTextAttributesKey("HERY_KEY", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val VALUE = createTextAttributesKey("HERY_VALUE", DefaultLanguageHighlighterColors.STRING)
        val COMMENT = createTextAttributesKey("HERY_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val SCALAR = createTextAttributesKey("HERY_SCALAR", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("HERY_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val BOOLEAN = createTextAttributesKey("HERY_BOOLEAN", DefaultLanguageHighlighterColors.KEYWORD)
        val ANCHOR = createTextAttributesKey("HERY_ANCHOR", DefaultLanguageHighlighterColors.LABEL)
        val SEPARATOR = createTextAttributesKey("HERY_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)

        private val RESERVED_PROPERTY_KEYS = arrayOf(RESERVED_PROPERTY)
        private val ENTITY_URI_KEYS = arrayOf(ENTITY_URI)
        private val KEY_KEYS = arrayOf(KEY)
        private val VALUE_KEYS = arrayOf(VALUE)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val SCALAR_KEYS = arrayOf(SCALAR)
        private val ANCHOR_KEYS = arrayOf(ANCHOR)
        private val SEPARATOR_KEYS = arrayOf(SEPARATOR)
        private val EMPTY_KEYS = arrayOf<TextAttributesKey>()
    }

    override fun getHighlightingLexer(): Lexer = HeryLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            HeryTokenTypes.RESERVED_PROPERTY -> RESERVED_PROPERTY_KEYS
            HeryTokenTypes.ENTITY_URI -> ENTITY_URI_KEYS
            HeryTokenTypes.VERSION -> arrayOf(VERSION)
            YAMLTokenTypes.SCALAR_KEY -> KEY_KEYS
            YAMLTokenTypes.COMMENT -> COMMENT_KEYS
            YAMLTokenTypes.SCALAR_STRING,
            YAMLTokenTypes.SCALAR_DSTRING,
            YAMLTokenTypes.SCALAR_LIST,
            YAMLTokenTypes.TEXT -> SCALAR_KEYS
            YAMLTokenTypes.TAG -> ANCHOR_KEYS
            YAMLTokenTypes.DOCUMENT_MARKER -> SEPARATOR_KEYS
            YAMLTokenTypes.SEQUENCE_MARKER -> SEPARATOR_KEYS
            YAMLTokenTypes.COLON -> SEPARATOR_KEYS
            else -> EMPTY_KEYS
        }
    }
}
