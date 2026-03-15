package org.amadla.hery

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.yaml.YAMLParserDefinition
import org.jetbrains.yaml.psi.impl.YAMLFileImpl

/**
 * Reuses the YAML parser for HERY files since HERY is a superset of YAML
 * with reserved root-level properties.
 */
class HeryParserDefinition : ParserDefinition {

    private val yamlParserDef = YAMLParserDefinition()

    companion object {
        val FILE = IFileElementType(HeryLanguage.INSTANCE)
    }

    override fun createLexer(project: Project?): Lexer = HeryLexer()

    override fun createParser(project: Project?): PsiParser = yamlParserDef.createParser(project)

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = yamlParserDef.commentTokens

    override fun getStringLiteralElements(): TokenSet = yamlParserDef.stringLiteralElements

    override fun createElement(node: ASTNode?): PsiElement = yamlParserDef.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = HeryFile(viewProvider)
}
