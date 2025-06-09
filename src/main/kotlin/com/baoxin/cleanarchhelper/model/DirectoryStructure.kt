package com.baoxin.cleanarchhelper.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 表示目录结构的数据类
 * 支持嵌套的目录结构定义
 */
data class DirectoryStructure(
    val directories: Map<String, DirectoryStructure> = emptyMap()
) {
    
    /**
     * 检查是否为叶子节点（没有子目录）
     */
    fun isLeaf(): Boolean = directories.isEmpty()
    
    /**
     * 获取所有目录路径的扁平列表
     */
    fun getAllPaths(prefix: String = ""): List<String> {
        val paths = mutableListOf<String>()
        
        for ((name, subStructure) in directories) {
            val currentPath = if (prefix.isEmpty()) name else "$prefix/$name"
            paths.add(currentPath)
            
            // 递归添加子目录路径
            paths.addAll(subStructure.getAllPaths(currentPath))
        }
        
        return paths
    }
    
    /**
     * 将结构转换为 JSON 字符串
     */
    fun toJson(): String {
        return Gson().toJson(this)
    }
    
    companion object {
        /**
         * 从 JSON 字符串创建目录结构
         */
        fun fromJson(json: String): DirectoryStructure {
            return try {
                Gson().fromJson(json, DirectoryStructure::class.java)
            } catch (e: Exception) {
                DirectoryStructure()
            }
        }
        
        /**
         * 从 Map 创建目录结构
         */
        fun fromMap(map: Map<String, Any>): DirectoryStructure {
            val directories = mutableMapOf<String, DirectoryStructure>()
            
            for ((key, value) in map) {
                when (value) {
                    is Map<*, *> -> {
                        @Suppress("UNCHECKED_CAST")
                        directories[key] = fromMap(value as Map<String, Any>)
                    }
                    else -> {
                        // 如果值不是 Map，则创建一个空的目录结构
                        directories[key] = DirectoryStructure()
                    }
                }
            }
            
            return DirectoryStructure(directories)
        }
        
        /**
         * 转换为 Map 格式
         */
        fun toMap(structure: DirectoryStructure): Map<String, Any> {
            val result = mutableMapOf<String, Any>()
            
            for ((key, value) in structure.directories) {
                if (value.isLeaf()) {
                    result[key] = emptyMap<String, Any>()
                } else {
                    result[key] = toMap(value)
                }
            }
            
            return result
        }
    }
}

/**
 * 项目类型枚举
 */
enum class ProjectType(val displayName: String, val extensions: List<String>) {
    FLUTTER("Flutter", listOf("dart", "yaml")),
    REACT("React", listOf("js", "jsx", "ts", "tsx")),
    NODE_JS("Node.js", listOf("js", "ts")),
    GENERAL("通用", emptyList());
    
    companion object {
        /**
         * 根据项目文件检测项目类型
         */
        fun detectProjectType(projectFiles: List<String>): ProjectType {
            return when {
                projectFiles.any { it.contains("pubspec.yaml") } -> FLUTTER
                projectFiles.any { it.contains("package.json") && it.contains("react") } -> REACT
                projectFiles.any { it.contains("package.json") } -> NODE_JS
                else -> GENERAL
            }
        }
    }
}
