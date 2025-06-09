package com.baoxin.cleanarchhelper.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Feature 名称输入对话框
 */
class FeatureNameDialog(project: Project) : DialogWrapper(project) {
    
    private val featureNameField = JBTextField()
    
    init {
        title = "创建 Feature 模块"
        init()
        
        // 设置默认焦点
        featureNameField.requestFocusInWindow()
    }
    
    override fun createCenterPanel(): JComponent {
        val panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("Feature 名称:"),
                featureNameField,
                1,
                false
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel
        
        panel.preferredSize = Dimension(400, 100)
        
        // 添加输入提示
        featureNameField.toolTipText = "请输入 Feature 名称，支持字母、数字、下划线和连字符"
        
        return panel
    }
    
    override fun getPreferredFocusedComponent(): JComponent {
        return featureNameField
    }
    
    /**
     * 获取输入的 Feature 名称
     */
    fun getFeatureName(): String {
        return featureNameField.text.trim()
    }
    
    override fun doValidate(): ValidationInfo? {
        val featureName = getFeatureName()
        
        if (featureName.isBlank()) {
            return ValidationInfo("Feature 名称不能为空", featureNameField)
        }
        
        if (featureName.length < 2) {
            return ValidationInfo("Feature 名称至少需要2个字符", featureNameField)
        }
        
        if (featureName.length > 50) {
            return ValidationInfo("Feature 名称不能超过50个字符", featureNameField)
        }
        
        // 检查是否包含非法字符
        if (!featureName.matches(Regex("^[a-zA-Z0-9_\\-\\s]+$"))) {
            return ValidationInfo("Feature 名称只能包含字母、数字、下划线、连字符和空格", featureNameField)
        }
        
        // 检查是否以字母开头
        if (!featureName.first().isLetter()) {
            return ValidationInfo("Feature 名称必须以字母开头", featureNameField)
        }
        
        return null
    }
}
