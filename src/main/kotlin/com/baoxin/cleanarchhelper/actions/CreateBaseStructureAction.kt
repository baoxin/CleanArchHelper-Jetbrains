package com.baoxin.cleanarchhelper.actions

import com.baoxin.cleanarchhelper.services.DirectoryCreatorService
import com.baoxin.cleanarchhelper.services.StructureConfigService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.roots.ProjectRootManager

/**
 * 创建 Clean Architecture 基础结构的 Action
 */
class CreateBaseStructureAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)

        // 如果没有选择文件，使用项目根目录
        val targetDirectory = when {
            selectedFile == null -> {
                // 尝试多种方式获取项目根目录
                val projectRootManager = ProjectRootManager.getInstance(project)
                val contentRoots = projectRootManager.contentRoots

                project.projectFile?.parent
                    ?: project.workspaceFile?.parent
                    ?: contentRoots.firstOrNull()
                    ?: run {
                        Messages.showErrorDialog(project, "无法获取项目根目录", "错误")
                        return
                    }
            }
            selectedFile.isDirectory -> selectedFile
            else -> selectedFile.parent ?: run {
                Messages.showErrorDialog(project, "无法确定目标目录", "错误")
                return
            }
        }
        
        // 获取配置服务
        val configService = StructureConfigService.getInstance()
        val creatorService = DirectoryCreatorService.getInstance()
        
        // 获取基础结构配置
        val baseStructure = configService.getBaseStructure()
        
        // 显示确认对话框
        val structurePreview = creatorService.getStructurePreview(baseStructure)
        val previewText = structurePreview.take(10).joinToString("\n") + 
            if (structurePreview.size > 10) "\n... 还有 ${structurePreview.size - 10} 个目录" else ""
        
        val result = Messages.showYesNoDialog(
            project,
            "将在以下位置创建 Clean Architecture 基础结构：\n\n" +
            "目标目录: ${targetDirectory.path}\n\n" +
            "将创建的目录结构：\n$previewText\n\n" +
            "是否继续？",
            "创建 Clean Architecture 基础结构",
            Messages.getQuestionIcon()
        )
        
        if (result != Messages.YES) {
            return
        }
        
        // 在后台任务中创建目录结构
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "创建 Clean Architecture 基础结构", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = false
                indicator.fraction = 0.0
                
                val totalSteps = structurePreview.size
                var currentStep = 0
                
                creatorService.createDirectoryStructure(
                    project = project,
                    baseDirectory = targetDirectory,
                    structure = baseStructure,
                    onProgress = { message ->
                        currentStep++
                        indicator.fraction = currentStep.toDouble() / totalSteps
                        indicator.text = message
                    },
                    onComplete = { success, errorMessage ->
                        if (success) {
                            Messages.showInfoMessage(
                                project,
                                "Clean Architecture 基础结构创建成功！\n\n" +
                                "位置: ${targetDirectory.path}\n" +
                                "共创建了 ${structurePreview.size} 个目录。",
                                "创建成功"
                            )
                        } else {
                            Messages.showErrorDialog(
                                project,
                                "创建 Clean Architecture 基础结构时发生错误：\n\n$errorMessage",
                                "创建失败"
                            )
                        }
                    }
                )
            }
        })
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)

        // 只要项目存在就显示和启用菜单项
        e.presentation.isVisible = project != null
        e.presentation.isEnabled = project != null

        // 更新显示文本
        if (selectedFile?.isDirectory == true) {
            e.presentation.text = "创建 Clean Architecture 基础结构"
        } else if (selectedFile != null) {
            e.presentation.text = "在此处创建 Clean Architecture 基础结构"
        } else {
            e.presentation.text = "创建 Clean Architecture 基础结构"
        }
    }
}
