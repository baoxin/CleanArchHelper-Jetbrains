package com.baoxin.cleanarchhelper.settings

import com.baoxin.cleanarchhelper.config.DefaultStructures
import com.baoxin.cleanarchhelper.model.DirectoryStructure
import com.baoxin.cleanarchhelper.services.StructureConfigService
import com.baoxin.cleanarchhelper.utils.JsonFormatter
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*

/**
 * Clean Architecture Helper 设置页面
 */
class CleanArchConfigurable : Configurable {
    
    private val configService = StructureConfigService.getInstance()
    private var settingsPanel: JPanel? = null
    private var baseStructureTextArea: JBTextArea? = null
    private var featureStructureTextArea: JBTextArea? = null
    
    override fun getDisplayName(): String = "Clean Architecture Helper"
    
    override fun createComponent(): JComponent? {
        if (settingsPanel == null) {
            settingsPanel = createSettingsPanel()
        }
        return settingsPanel
    }
    
    private fun createSettingsPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        
        // 创建选项卡面板
        val tabbedPane = JTabbedPane()
        
        // 基础结构配置选项卡
        baseStructureTextArea = JBTextArea()
        baseStructureTextArea!!.rows = 15
        baseStructureTextArea!!.columns = 50
        baseStructureTextArea!!.lineWrap = true
        baseStructureTextArea!!.wrapStyleWord = true
        
        val baseScrollPane = JBScrollPane(baseStructureTextArea!!)
        val basePanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("基础结构配置 (JSON 格式):"),
                baseScrollPane,
                true
            )
            .addComponent(createBaseStructureButtonPanel())
            .panel
        
        tabbedPane.addTab("基础结构", basePanel)
        
        // Feature 结构配置选项卡
        featureStructureTextArea = JBTextArea()
        featureStructureTextArea!!.rows = 15
        featureStructureTextArea!!.columns = 50
        featureStructureTextArea!!.lineWrap = true
        featureStructureTextArea!!.wrapStyleWord = true
        
        val featureScrollPane = JBScrollPane(featureStructureTextArea!!)
        val featurePanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("Feature 结构配置 (JSON 格式):"),
                featureScrollPane,
                true
            )
            .addComponent(createFeatureStructureButtonPanel())
            .panel
        
        tabbedPane.addTab("Feature 结构", featurePanel)
        
        // 帮助选项卡
        val helpPanel = createHelpPanel()
        tabbedPane.addTab("帮助", helpPanel)
        
        panel.add(tabbedPane, BorderLayout.CENTER)
        panel.preferredSize = Dimension(800, 600)
        
        return panel
    }
    
    private fun createBaseStructureButtonPanel(): JComponent {
        val panel = JPanel()
        
        val resetButton = JButton("重置为默认")
        resetButton.addActionListener {
            val result = Messages.showYesNoDialog(
                "确定要重置基础结构配置为默认值吗？",
                "确认重置",
                Messages.getQuestionIcon()
            )
            if (result == Messages.YES) {
                val defaultStructure = DefaultStructures.getDefaultBaseStructure()
                baseStructureTextArea?.text = JsonFormatter.formatJson(defaultStructure.toJson())
            }
        }
        
        val previewButton = JButton("预览结构")
        previewButton.addActionListener {
            val jsonText = baseStructureTextArea?.text ?: ""
            if (!JsonFormatter.isValidJson(jsonText)) {
                val error = JsonFormatter.getJsonError(jsonText)
                Messages.showErrorDialog(
                    "JSON 格式错误：\n$error",
                    "配置错误"
                )
                return@addActionListener
            }

            try {
                val structure = DirectoryStructure.fromJson(jsonText)
                val preview = structure.getAllPaths().joinToString("\n")
                Messages.showInfoMessage(
                    "基础结构预览：\n\n$preview",
                    "结构预览"
                )
            } catch (e: Exception) {
                Messages.showErrorDialog(
                    "解析配置时发生错误：\n${e.message}",
                    "配置错误"
                )
            }
        }

        val formatButton = JButton("格式化 JSON")
        formatButton.addActionListener {
            val jsonText = baseStructureTextArea?.text ?: ""
            if (JsonFormatter.isValidJson(jsonText)) {
                baseStructureTextArea?.text = JsonFormatter.formatJson(jsonText)
            } else {
                val error = JsonFormatter.getJsonError(jsonText)
                Messages.showErrorDialog(
                    "无法格式化，JSON 格式错误：\n$error",
                    "格式化失败"
                )
            }
        }
        
        panel.add(resetButton)
        panel.add(previewButton)
        panel.add(formatButton)

        return panel
    }
    
    private fun createFeatureStructureButtonPanel(): JComponent {
        val panel = JPanel()
        
        val resetButton = JButton("重置为默认")
        resetButton.addActionListener {
            val result = Messages.showYesNoDialog(
                "确定要重置 Feature 结构配置为默认值吗？",
                "确认重置",
                Messages.getQuestionIcon()
            )
            if (result == Messages.YES) {
                val defaultStructure = DefaultStructures.getDefaultFeatureStructure()
                featureStructureTextArea?.text = JsonFormatter.formatJson(defaultStructure.toJson())
            }
        }
        
        val previewButton = JButton("预览结构")
        previewButton.addActionListener {
            val jsonText = featureStructureTextArea?.text ?: ""
            if (!JsonFormatter.isValidJson(jsonText)) {
                val error = JsonFormatter.getJsonError(jsonText)
                Messages.showErrorDialog(
                    "JSON 格式错误：\n$error",
                    "配置错误"
                )
                return@addActionListener
            }

            try {
                val structure = DirectoryStructure.fromJson(jsonText)
                val preview = structure.getAllPaths().joinToString("\n")
                Messages.showInfoMessage(
                    "Feature 结构预览：\n\n$preview",
                    "结构预览"
                )
            } catch (e: Exception) {
                Messages.showErrorDialog(
                    "解析配置时发生错误：\n${e.message}",
                    "配置错误"
                )
            }
        }

        val formatFeatureButton = JButton("格式化 JSON")
        formatFeatureButton.addActionListener {
            val jsonText = featureStructureTextArea?.text ?: ""
            if (JsonFormatter.isValidJson(jsonText)) {
                featureStructureTextArea?.text = JsonFormatter.formatJson(jsonText)
            } else {
                val error = JsonFormatter.getJsonError(jsonText)
                Messages.showErrorDialog(
                    "无法格式化，JSON 格式错误：\n$error",
                    "格式化失败"
                )
            }
        }

        panel.add(resetButton)
        panel.add(previewButton)
        panel.add(formatFeatureButton)

        return panel
    }
    
    private fun createHelpPanel(): JComponent {
        val helpText = """
            Clean Architecture Helper 使用说明
            
            功能特性：
            • 🏗️ 快速创建完整的 Clean Architecture 基础目录结构
            • 🎯 为每个功能模块快速创建标准的三层架构目录
            • ⚙️ 完全可自定义的目录结构配置
            • 📁 支持多层级嵌套目录
            • 🔧 可以保存和重置配置
            
            使用方法：
            1. 创建基础结构：
               - 在项目视图中右键点击目录
               - 选择 "创建 Clean Architecture 基础结构"
               - 或使用快捷键 Ctrl+Alt+B
            
            2. 创建 Feature 模块：
               - 在项目视图中右键点击目录（推荐在 features 目录中）
               - 选择 "创建 Feature 模块"
               - 输入 Feature 名称
               - 或使用快捷键 Ctrl+Alt+F
            
            3. 配置目录结构：
               - 在此设置页面中编辑 JSON 配置
               - 或通过 Tools 菜单选择 "配置目录结构"
            
            JSON 配置格式：
            目录结构使用嵌套的 JSON 对象表示，每个键代表一个目录名，
            值为该目录的子目录结构。空对象 {} 表示叶子目录。
            
            示例：
            {
              "directories": {
                "data": {
                  "directories": {
                    "datasources": {"directories": {}},
                    "models": {"directories": {}},
                    "repositories": {"directories": {}}
                  }
                }
              }
            }
        """.trimIndent()
        
        val helpTextArea = JBTextArea(helpText)
        helpTextArea.isEditable = false
        helpTextArea.lineWrap = true
        helpTextArea.wrapStyleWord = true
        
        return JBScrollPane(helpTextArea)
    }
    
    override fun isModified(): Boolean {
        val currentBaseStructure = configService.getBaseStructure()
        val currentFeatureStructure = configService.getFeatureStructure()
        
        val baseModified = baseStructureTextArea?.text != JsonFormatter.formatJson(currentBaseStructure.toJson())
        val featureModified = featureStructureTextArea?.text != JsonFormatter.formatJson(currentFeatureStructure.toJson())
        
        return baseModified || featureModified
    }
    
    override fun apply() {
        val baseJsonText = baseStructureTextArea?.text ?: ""
        val featureJsonText = featureStructureTextArea?.text ?: ""

        // 验证 JSON 格式
        if (!JsonFormatter.isValidJson(baseJsonText)) {
            val error = JsonFormatter.getJsonError(baseJsonText)
            Messages.showErrorDialog(
                "基础结构配置 JSON 格式错误：\n$error",
                "保存失败"
            )
            throw IllegalArgumentException("Invalid base structure JSON")
        }

        if (!JsonFormatter.isValidJson(featureJsonText)) {
            val error = JsonFormatter.getJsonError(featureJsonText)
            Messages.showErrorDialog(
                "Feature 结构配置 JSON 格式错误：\n$error",
                "保存失败"
            )
            throw IllegalArgumentException("Invalid feature structure JSON")
        }

        try {
            // 保存基础结构配置
            val baseStructure = DirectoryStructure.fromJson(baseJsonText)
            configService.setBaseStructure(baseStructure)

            // 保存 Feature 结构配置
            val featureStructure = DirectoryStructure.fromJson(featureJsonText)
            configService.setFeatureStructure(featureStructure)

        } catch (e: Exception) {
            Messages.showErrorDialog(
                "保存配置时发生错误：\n${e.message}",
                "保存失败"
            )
            throw e
        }
    }
    
    override fun reset() {
        val baseStructure = configService.getBaseStructure()
        val featureStructure = configService.getFeatureStructure()

        baseStructureTextArea?.text = JsonFormatter.formatJson(baseStructure.toJson())
        featureStructureTextArea?.text = JsonFormatter.formatJson(featureStructure.toJson())
    }
}
