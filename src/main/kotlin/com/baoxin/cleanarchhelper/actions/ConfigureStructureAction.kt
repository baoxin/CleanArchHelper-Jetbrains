package com.baoxin.cleanarchhelper.actions

import com.baoxin.cleanarchhelper.ui.StructureConfigDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * 配置目录结构的 Action
 */
class ConfigureStructureAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        // 显示结构配置对话框
        val dialog = StructureConfigDialog(project)
        dialog.show()
    }
    
    override fun update(e: AnActionEvent) {
        // 只有在项目存在时才启用和显示
        val project = e.project
        e.presentation.isVisible = project != null
        e.presentation.isEnabled = project != null
    }
}
