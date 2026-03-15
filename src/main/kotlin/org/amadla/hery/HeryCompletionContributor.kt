package org.amadla.hery

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

class HeryCompletionContributor : CompletionContributor() {

    init {
        // Complete reserved properties at root level
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(HeryLanguage.INSTANCE),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val lineStart = position.text.trimStart()

                    // Only suggest at the start of a line (root level)
                    val indent = parameters.editor.document
                        .getText(com.intellij.openapi.util.TextRange(
                            parameters.editor.document.getLineStartOffset(
                                parameters.editor.document.getLineNumber(parameters.offset)
                            ),
                            parameters.offset
                        ))

                    if (indent.isNotBlank() && !indent.startsWith("_")) return

                    // Reserved properties
                    result.addElement(
                        LookupElementBuilder.create("_type: ")
                            .withTypeText("Entity type URI")
                            .withBoldness(true)
                            .withInsertHandler { ctx, _ ->
                                ctx.editor.caretModel.moveToOffset(ctx.tailOffset)
                            }
                    )
                    result.addElement(
                        LookupElementBuilder.create("_extends: ")
                            .withTypeText("Inherit from entity")
                            .withBoldness(true)
                    )
                    result.addElement(
                        LookupElementBuilder.create("_meta:\n  ")
                            .withPresentableText("_meta:")
                            .withTypeText("Entity metadata")
                            .withBoldness(true)
                    )
                    result.addElement(
                        LookupElementBuilder.create("_body:\n  ")
                            .withPresentableText("_body:")
                            .withTypeText("Entity content")
                            .withBoldness(true)
                    )
                    result.addElement(
                        LookupElementBuilder.create("_requires:\n  - ")
                            .withPresentableText("_requires:")
                            .withTypeText("Dependency list")
                            .withBoldness(true)
                    )

                    // Common _meta keys (when inside _meta block)
                    if (indent.startsWith("  ")) {
                        val docText = parameters.editor.document.text
                        val lineNum = parameters.editor.document.getLineNumber(parameters.offset)
                        // Walk back to find parent key
                        for (i in lineNum - 1 downTo 0) {
                            val prevLine = parameters.editor.document.getText(
                                com.intellij.openapi.util.TextRange(
                                    parameters.editor.document.getLineStartOffset(i),
                                    parameters.editor.document.getLineEndOffset(i)
                                )
                            ).trim()
                            if (prevLine.startsWith("_meta:")) {
                                result.addElement(LookupElementBuilder.create("name: ").withTypeText("Entity name"))
                                result.addElement(LookupElementBuilder.create("description: ").withTypeText("Entity description"))
                                result.addElement(LookupElementBuilder.create("category: ").withTypeText("Entity category"))
                                result.addElement(LookupElementBuilder.create("tags:\n    - ").withPresentableText("tags:").withTypeText("Entity tags"))
                                break
                            }
                            if (!prevLine.startsWith("  ") && prevLine.isNotEmpty()) break
                        }
                    }

                    // Entity type URIs after _type: or _extends:
                    val lineText = parameters.editor.document.getText(
                        com.intellij.openapi.util.TextRange(
                            parameters.editor.document.getLineStartOffset(
                                parameters.editor.document.getLineNumber(parameters.offset)
                            ),
                            parameters.offset
                        )
                    )
                    if (lineText.contains("_type:") || lineText.contains("_extends:") || lineText.trimStart().startsWith("- ")) {
                        val entityTypes = listOf(
                            "amadla.org/entity/application",
                            "amadla.org/entity/application/webserver",
                            "amadla.org/entity/infrastructure",
                            "amadla.org/entity/infrastructure/vm",
                            "amadla.org/entity/package",
                            "amadla.org/entity/template",
                            "amadla.org/entity/container",
                            "amadla.org/entity/secret",
                            "amadla.org/entity/tools",
                            "amadla.org/entity/system",
                            "amadla.org/entity/system/net",
                            "amadla.org/entity/system/storage",
                            "amadla.org/entity/system/user",
                            "amadla.org/entity/system/service",
                        )
                        for (uri in entityTypes) {
                            result.addElement(
                                LookupElementBuilder.create("$uri@v1.0.0")
                                    .withTypeText("Entity type")
                            )
                        }
                    }
                }
            }
        )
    }
}
