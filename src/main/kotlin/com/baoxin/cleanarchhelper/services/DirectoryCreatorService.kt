package com.baoxin.cleanarchhelper.services

import com.baoxin.cleanarchhelper.model.DirectoryStructure
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.command.WriteCommandAction
import java.io.IOException

/**
 * 目录创建服务
 * 负责根据配置创建目录结构
 */
class DirectoryCreatorService {
    
    /**
     * 在指定目录下创建目录结构
     */
    fun createDirectoryStructure(
        project: Project,
        baseDirectory: VirtualFile,
        structure: DirectoryStructure,
        onProgress: ((String) -> Unit)? = null,
        onComplete: ((Boolean, String?) -> Unit)? = null
    ) {
        ApplicationManager.getApplication().invokeLater {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    createDirectoriesRecursively(baseDirectory, structure, onProgress)
                    onComplete?.invoke(true, null)
                } catch (e: Exception) {
                    onComplete?.invoke(false, e.message)
                }
            }
        }
    }
    
    /**
     * 递归创建目录
     */
    private fun createDirectoriesRecursively(
        parentDir: VirtualFile,
        structure: DirectoryStructure,
        onProgress: ((String) -> Unit)? = null
    ) {
        for ((dirName, subStructure) in structure.directories) {
            onProgress?.invoke("创建目录: ${parentDir.path}/$dirName")
            
            val newDir = try {
                parentDir.findChild(dirName) ?: parentDir.createChildDirectory(this, dirName)
            } catch (e: IOException) {
                throw IOException("无法创建目录: ${parentDir.path}/$dirName - ${e.message}")
            }
            
            // 递归创建子目录
            if (!subStructure.isLeaf()) {
                createDirectoriesRecursively(newDir, subStructure, onProgress)
            }
        }
    }
    
    /**
     * 创建 Feature 模块
     */
    fun createFeatureModule(
        project: Project,
        baseDirectory: VirtualFile,
        featureName: String,
        structure: DirectoryStructure,
        onProgress: ((String) -> Unit)? = null,
        onComplete: ((Boolean, String?) -> Unit)? = null
    ) {
        ApplicationManager.getApplication().invokeLater {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    // 规范化 Feature 名称
                    val normalizedName = normalizeFeatureName(featureName)
                    onProgress?.invoke("创建 Feature 模块: $normalizedName")
                    
                    // 创建 Feature 根目录
                    val featureDir = baseDirectory.findChild(normalizedName) 
                        ?: baseDirectory.createChildDirectory(this, normalizedName)
                    
                    // 创建 Feature 内部结构
                    createDirectoriesRecursively(featureDir, structure, onProgress)
                    
                    onComplete?.invoke(true, null)
                } catch (e: Exception) {
                    onComplete?.invoke(false, e.message)
                }
            }
        }
    }
    
    /**
     * 规范化 Feature 名称
     * 转换为小写，用下划线分隔
     */
    private fun normalizeFeatureName(name: String): String {
        return name.trim()
            .replace(Regex("[\\s-]+"), "_")  // 空格和连字符转为下划线
            .replace(Regex("[^a-zA-Z0-9_]"), "")  // 移除特殊字符
            .lowercase()
    }
    
    /**
     * 检查目录是否已存在
     */
    fun checkDirectoryExists(baseDirectory: VirtualFile, dirName: String): Boolean {
        return baseDirectory.findChild(dirName) != null
    }
    
    /**
     * 获取目录结构预览
     */
    fun getStructurePreview(structure: DirectoryStructure, prefix: String = ""): List<String> {
        return structure.getAllPaths(prefix)
    }
    
    /**
     * 验证目录名称
     */
    fun validateDirectoryName(name: String): Pair<Boolean, String?> {
        if (name.isBlank()) {
            return false to "目录名称不能为空"
        }
        
        if (name.contains(Regex("[<>:\"/\\\\|?*]"))) {
            return false to "目录名称包含非法字符"
        }
        
        if (name.length > 255) {
            return false to "目录名称过长"
        }
        
        return true to null
    }
    
    companion object {
        /**
         * 获取服务实例
         */
        fun getInstance(): DirectoryCreatorService {
            return DirectoryCreatorService()
        }
    }
}
