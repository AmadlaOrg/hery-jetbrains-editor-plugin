package org.amadla.hery

import com.intellij.psi.tree.IElementType

class HeryTokenType(debugName: String) : IElementType(debugName, HeryLanguage.INSTANCE) {
    override fun toString(): String = "HeryTokenType.${super.toString()}"
}

object HeryTokenTypes {
    @JvmField val RESERVED_PROPERTY = HeryTokenType("RESERVED_PROPERTY")
    @JvmField val ENTITY_URI = HeryTokenType("ENTITY_URI")
    @JvmField val VERSION = HeryTokenType("VERSION")
}
