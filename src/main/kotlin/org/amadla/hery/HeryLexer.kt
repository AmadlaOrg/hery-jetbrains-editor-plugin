package org.amadla.hery

import com.intellij.lexer.DelegateLexer
import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType
import org.jetbrains.yaml.YAMLTokenTypes
import org.jetbrains.yaml.lexer.YAMLFlexLexer

/**
 * Wraps the YAML lexer and re-maps tokens for HERY reserved properties
 * and entity URIs to custom token types for distinct highlighting.
 */
class HeryLexer : DelegateLexer(YAMLFlexLexer()) {

    companion object {
        private val RESERVED_PROPERTIES = setOf("_type", "_extends", "_meta", "_body", "_requires")

        // Pattern: host.tld/path with optional @version
        private val ENTITY_URI_REGEX = Regex(
            """[a-zA-Z0-9._-]+\.[a-zA-Z]{2,}/[a-zA-Z0-9_./#-]+(@[\^~]?v?[0-9][0-9a-zA-Z.*-]*|@latest)?"""
        )
    }

    override fun getTokenType(): IElementType? {
        val baseType = super.getTokenType() ?: return null
        val tokenText = delegate.tokenText

        // Detect reserved property keys at the start of a line
        if (baseType == YAMLTokenTypes.SCALAR_KEY && tokenText in RESERVED_PROPERTIES) {
            return HeryTokenTypes.RESERVED_PROPERTY
        }

        // Detect entity URIs in scalar values
        if ((baseType == YAMLTokenTypes.SCALAR_STRING ||
             baseType == YAMLTokenTypes.TEXT ||
             baseType == YAMLTokenTypes.SCALAR_DSTRING ||
             baseType == YAMLTokenTypes.SCALAR_LIST)
            && ENTITY_URI_REGEX.containsMatchIn(tokenText)
        ) {
            return HeryTokenTypes.ENTITY_URI
        }

        return baseType
    }
}
