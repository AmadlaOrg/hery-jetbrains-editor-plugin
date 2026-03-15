package org.amadla.hery

import com.intellij.ide.structureView.*
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import javax.swing.Icon

class HeryStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return HeryStructureViewModel(psiFile)
            }
        }
    }
}

class HeryStructureViewModel(psiFile: PsiFile) :
    StructureViewModelBase(psiFile, HeryStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = false
    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean = false
}

class HeryStructureViewElement(private val element: com.intellij.psi.NavigatablePsiElement) :
    StructureViewTreeElement {

    override fun getValue(): Any = element
    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)
    override fun canNavigate(): Boolean = element.canNavigate()
    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String {
                return when (element) {
                    is YAMLKeyValue -> {
                        val key = element.keyText
                        val value = element.valueText
                        if (key in setOf("_type", "_extends") && value.isNotEmpty()) {
                            "$key: $value"
                        } else {
                            key
                        }
                    }
                    is PsiFile -> element.name
                    else -> element.text?.take(30) ?: ""
                }
            }

            override fun getLocationString(): String? = null

            override fun getIcon(unused: Boolean): Icon? {
                return when (element) {
                    is PsiFile -> HeryIcons.FILE
                    else -> null
                }
            }
        }
    }

    override fun getChildren(): Array<TreeElement> {
        val children = mutableListOf<TreeElement>()

        when (element) {
            is PsiFile -> {
                // Find all YAML documents, then their root key-value pairs
                PsiTreeUtil.findChildrenOfType(element, YAMLDocument::class.java).forEach { doc ->
                    val mapping = PsiTreeUtil.findChildOfType(doc, YAMLMapping::class.java)
                    mapping?.keyValues?.forEach { kv ->
                        children.add(HeryStructureViewElement(kv))
                    }
                }
            }
            is YAMLKeyValue -> {
                val mapping = PsiTreeUtil.findChildOfType(element.value, YAMLMapping::class.java)
                mapping?.keyValues?.forEach { kv ->
                    children.add(HeryStructureViewElement(kv))
                }
            }
        }

        return children.toTypedArray()
    }
}
