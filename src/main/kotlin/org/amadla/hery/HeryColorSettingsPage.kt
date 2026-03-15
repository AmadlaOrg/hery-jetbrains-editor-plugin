package org.amadla.hery

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class HeryColorSettingsPage : ColorSettingsPage {

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Reserved property", HerySyntaxHighlighter.RESERVED_PROPERTY),
            AttributesDescriptor("Entity URI", HerySyntaxHighlighter.ENTITY_URI),
            AttributesDescriptor("Version", HerySyntaxHighlighter.VERSION),
            AttributesDescriptor("Key", HerySyntaxHighlighter.KEY),
            AttributesDescriptor("Value/Scalar", HerySyntaxHighlighter.SCALAR),
            AttributesDescriptor("Comment", HerySyntaxHighlighter.COMMENT),
            AttributesDescriptor("Anchor", HerySyntaxHighlighter.ANCHOR),
            AttributesDescriptor("Separator", HerySyntaxHighlighter.SEPARATOR),
        )
    }

    override fun getIcon(): Icon = HeryIcons.FILE
    override fun getHighlighter(): SyntaxHighlighter = HerySyntaxHighlighter()
    override fun getDemoText(): String = """
---
_type: amadla.org/entity/application@v1.0.0
_extends: github.com/AmadlaOrg/EntityApplication@v1.0.0
_meta:
  name: MyApp
  description: An example application
  tags:
    - web
    - backend
_requires:
  - amadla.org/entity/package@v1.0.0
  - amadla.org/entity/template@^v1.0.0
_body:
  server_name: localhost
  listen:
    - 8080
    - 443
  root: /var/www/html
  debug: false
  workers: 4
  # This is a comment
  locations:
    - path: /
      try_files: "${'$'}uri ${'$'}uri/ =404"
""".trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "HERY"
}
