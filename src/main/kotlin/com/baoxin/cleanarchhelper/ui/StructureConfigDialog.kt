package com.baoxin.cleanarchhelper.ui

import com.baoxin.cleanarchhelper.config.DefaultStructures
import com.baoxin.cleanarchhelper.model.DirectoryStructure
import com.baoxin.cleanarchhelper.services.StructureConfigService
import com.baoxin.cleanarchhelper.utils.JsonFormatter
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*

/**
 * 结构配置对话框
 */
class StructureConfigDialog(private val project: Project) : DialogWrapper(project) {
    
    private val configService = StructureConfigService.getInstance()
    private val baseStructureTextArea = JBTextArea()
    private val featureStructureTextArea = JBTextArea()
    private val tabbedPane = JTabbedPane()
    
    init {
        title = "配置 Clean Architecture 目录结构"
        init()
        
        // 加载当前配置
        loadCurrentConfiguration()
    }
    
    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        
        // 创建基础结构配置面板
        val baseStructurePanel = createStructurePanel(
            baseStructureTextArea,
            "配置基础目录结构（JSON 格式）：",
            "这里定义了创建基础结构时的目录布局"
        )
        
        // 创建 Feature 结构配置面板
        val featureStructurePanel = createStructurePanel(
            featureStructureTextArea,
            "配置 Feature 模块结构（JSON 格式）：",
            "这里定义了创建 Feature 模块时的目录布局"
        )
        
        // 添加到选项卡面板
        tabbedPane.addTab("基础结构", baseStructurePanel)
        tabbedPane.addTab("Feature 结构", featureStructurePanel)
        
        panel.add(tabbedPane, BorderLayout.CENTER)
        
        // 添加按钮面板
        val buttonPanel = createButtonPanel()
        panel.add(buttonPanel, BorderLayout.SOUTH)
        
        panel.preferredSize = Dimension(800, 600)
        
        return panel
    }
    
    private fun createStructurePanel(textArea: JBTextArea, labelText: String, tooltipText: String): JComponent {
        textArea.rows = 20
        textArea.columns = 60
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        textArea.toolTipText = tooltipText
        
        val scrollPane = JBScrollPane(textArea)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel(labelText), scrollPane, true)
            .panel
    }
    
    private fun createButtonPanel(): JComponent {
        val panel = JPanel()
        
        val resetBaseButton = JButton("重置基础结构")
        resetBaseButton.addActionListener {
            val result = Messages.showYesNoDialog(
                project,
                "确定要重置基础结构配置为默认值吗？",
                "确认重置",
                Messages.getQuestionIcon()
            )
            if (result == Messages.YES) {
                val defaultStructure = DefaultStructures.getDefaultBaseStructure()
                baseStructureTextArea.text = JsonFormatter.formatJson(defaultStructure.toJson())
            }
        }
        
        val resetFeatureButton = JButton("重置 Feature 结构")
        resetFeatureButton.addActionListener {
            val result = Messages.showYesNoDialog(
                project,
                "确定要重置 Feature 结构配置为默认值吗？",
                "确认重置",
                Messages.getQuestionIcon()
            )
            if (result == Messages.YES) {
                val defaultStructure = DefaultStructures.getDefaultFeatureStructure()
                featureStructureTextArea.text = JsonFormatter.formatJson(defaultStructure.toJson())
            }
        }
        
        val previewButton = JButton("预览结构")
        previewButton.addActionListener {
            previewCurrentStructure()
        }

        val formatButton = JButton("格式化 JSON")
        formatButton.addActionListener {
            formatCurrentJson()
        }

        panel.add(resetBaseButton)
        panel.add(resetFeatureButton)
        panel.add(previewButton)
        panel.add(formatButton)

        return panel
    }
    
    private fun loadCurrentConfiguration() {
        val baseStructure = configService.getBaseStructure()
        val featureStructure = configService.getFeatureStructure()

        baseStructureTextArea.text = JsonFormatter.formatJson(baseStructure.toJson())
        featureStructureTextArea.text = JsonFormatter.formatJson(featureStructure.toJson())
    }
    
    private fun previewCurrentStructure() {
        val currentTab = tabbedPane.selectedIndex
        val jsonText = if (currentTab == 0) {
            baseStructureTextArea.text
        } else {
            featureStructureTextArea.text
        }

        if (!JsonFormatter.isValidJson(jsonText)) {
            val error = JsonFormatter.getJsonError(jsonText)
            Messages.showErrorDialog(
                project,
                "JSON 格式错误：\n$error",
                "配置错误"
            )
            return
        }

        try {
            val structure = DirectoryStructure.fromJson(jsonText)
            val preview = structure.getAllPaths().joinToString("\n")
            val structureType = if (currentTab == 0) "基础结构" else "Feature 结构"

            Messages.showInfoMessage(
                project,
                "当前 $structureType 预览：\n\n$preview",
                "结构预览"
            )
        } catch (e: Exception) {
            Messages.showErrorDialog(
                project,
                "解析配置时发生错误：\n${e.message}",
                "配置错误"
            )
        }
    }
    
    override fun doOKAction() {
        val baseJsonText = baseStructureTextArea.text
        val featureJsonText = featureStructureTextArea.text

        // 验证 JSON 格式
        if (!JsonFormatter.isValidJson(baseJsonText)) {
            val error = JsonFormatter.getJsonError(baseJsonText)
            Messages.showErrorDialog(
                project,
                "基础结构配置 JSON 格式错误：\n$error",
                "保存失败"
            )
            return
        }

        if (!JsonFormatter.isValidJson(featureJsonText)) {
            val error = JsonFormatter.getJsonError(featureJsonText)
            Messages.showErrorDialog(
                project,
                "Feature 结构配置 JSON 格式错误：\n$error",
                "保存失败"
            )
            return
        }

        try {
            // 验证并保存基础结构配置
            val baseStructure = DirectoryStructure.fromJson(baseJsonText)
            configService.setBaseStructure(baseStructure)

            // 验证并保存 Feature 结构配置
            val featureStructure = DirectoryStructure.fromJson(featureJsonText)
            configService.setFeatureStructure(featureStructure)

            Messages.showInfoMessage(
                project,
                "配置已保存成功！",
                "保存成功"
            )

            super.doOKAction()
        } catch (e: Exception) {
            Messages.showErrorDialog(
                project,
                "保存配置时发生错误：\n${e.message}",
                "保存失败"
            )
        }
    }

    private fun formatCurrentJson() {
        val currentTab = tabbedPane.selectedIndex
        val textArea = if (currentTab == 0) {
            baseStructureTextArea
        } else {
            featureStructureTextArea
        }

        val jsonText = textArea.text
        if (JsonFormatter.isValidJson(jsonText)) {
            textArea.text = JsonFormatter.formatJson(jsonText)
            Messages.showInfoMessage(
                project,
                "JSON 格式化完成！",
                "格式化成功"
            )
        } else {
            val error = JsonFormatter.getJsonError(jsonText)
            Messages.showErrorDialog(
                project,
                "无法格式化，JSON 格式错误：\n$error",
                "格式化失败"
            )
        }
    }
}
