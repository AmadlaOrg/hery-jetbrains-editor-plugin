package org.amadla.hery

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class HeryLiveTemplateContext : TemplateContextType("HERY", "HERY") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        return templateActionContext.file.name.endsWith(".hery")
    }
}
