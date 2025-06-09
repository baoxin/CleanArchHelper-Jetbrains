package com.baoxin.cleanarchhelper.config

import com.baoxin.cleanarchhelper.model.DirectoryStructure

/**
 * 默认的目录结构配置
 */
object DefaultStructures {
    
    /**
     * 默认的基础结构配置
     */
    fun getDefaultBaseStructure(): DirectoryStructure {
        val baseStructureMap = mapOf(
            "lib" to mapOf(
                "core" to mapOf(
                    "constants" to emptyMap<String, Any>(),
                    "errors" to emptyMap<String, Any>(),
                    "network" to emptyMap<String, Any>(),
                    "usecases" to emptyMap<String, Any>(),
                    "utils" to emptyMap<String, Any>()
                ),
                "features" to emptyMap<String, Any>(),
                "shared" to mapOf(
                    "data" to mapOf(
                        "datasources" to emptyMap<String, Any>(),
                        "models" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>()
                    ),
                    "domain" to mapOf(
                        "entities" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>(),
                        "usecases" to emptyMap<String, Any>()
                    ),
                    "presentation" to mapOf(
                        "controllers" to emptyMap<String, Any>(),
                        "pages" to emptyMap<String, Any>(),
                        "widgets" to emptyMap<String, Any>()
                    )
                )
            )
        )
        
        return DirectoryStructure.fromMap(baseStructureMap)
    }
    
    /**
     * 默认的 Feature 结构配置
     */
    fun getDefaultFeatureStructure(): DirectoryStructure {
        val featureStructureMap = mapOf(
            "data" to mapOf(
                "datasources" to emptyMap<String, Any>(),
                "models" to emptyMap<String, Any>(),
                "repositories" to emptyMap<String, Any>()
            ),
            "domain" to mapOf(
                "entities" to emptyMap<String, Any>(),
                "repositories" to emptyMap<String, Any>(),
                "usecases" to emptyMap<String, Any>()
            ),
            "presentation" to mapOf(
                "bindings" to emptyMap<String, Any>(),
                "controllers" to emptyMap<String, Any>(),
                "pages" to emptyMap<String, Any>(),
                "widgets" to emptyMap<String, Any>()
            )
        )
        
        return DirectoryStructure.fromMap(featureStructureMap)
    }
    
    /**
     * 根据项目类型获取推荐的基础结构
     */
    fun getRecommendedBaseStructure(projectType: String): DirectoryStructure {
        return when (projectType.lowercase()) {
            "flutter" -> getFlutterBaseStructure()
            "react" -> getReactBaseStructure()
            "nodejs", "node.js" -> getNodeJsBaseStructure()
            else -> getDefaultBaseStructure()
        }
    }
    
    /**
     * Flutter 项目的推荐基础结构
     */
    private fun getFlutterBaseStructure(): DirectoryStructure {
        val flutterStructureMap = mapOf(
            "lib" to mapOf(
                "core" to mapOf(
                    "constants" to emptyMap<String, Any>(),
                    "errors" to emptyMap<String, Any>(),
                    "network" to emptyMap<String, Any>(),
                    "usecases" to emptyMap<String, Any>(),
                    "utils" to emptyMap<String, Any>(),
                    "theme" to emptyMap<String, Any>()
                ),
                "features" to emptyMap<String, Any>(),
                "shared" to mapOf(
                    "data" to mapOf(
                        "datasources" to emptyMap<String, Any>(),
                        "models" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>()
                    ),
                    "domain" to mapOf(
                        "entities" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>(),
                        "usecases" to emptyMap<String, Any>()
                    ),
                    "presentation" to mapOf(
                        "bloc" to emptyMap<String, Any>(),
                        "pages" to emptyMap<String, Any>(),
                        "widgets" to emptyMap<String, Any>()
                    )
                )
            )
        )
        
        return DirectoryStructure.fromMap(flutterStructureMap)
    }
    
    /**
     * React 项目的推荐基础结构
     */
    private fun getReactBaseStructure(): DirectoryStructure {
        val reactStructureMap = mapOf(
            "src" to mapOf(
                "core" to mapOf(
                    "constants" to emptyMap<String, Any>(),
                    "errors" to emptyMap<String, Any>(),
                    "api" to emptyMap<String, Any>(),
                    "hooks" to emptyMap<String, Any>(),
                    "utils" to emptyMap<String, Any>()
                ),
                "features" to emptyMap<String, Any>(),
                "shared" to mapOf(
                    "data" to mapOf(
                        "services" to emptyMap<String, Any>(),
                        "models" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>()
                    ),
                    "domain" to mapOf(
                        "entities" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>(),
                        "usecases" to emptyMap<String, Any>()
                    ),
                    "presentation" to mapOf(
                        "components" to emptyMap<String, Any>(),
                        "pages" to emptyMap<String, Any>(),
                        "hooks" to emptyMap<String, Any>()
                    )
                )
            )
        )
        
        return DirectoryStructure.fromMap(reactStructureMap)
    }
    
    /**
     * Node.js 项目的推荐基础结构
     */
    private fun getNodeJsBaseStructure(): DirectoryStructure {
        val nodeJsStructureMap = mapOf(
            "src" to mapOf(
                "core" to mapOf(
                    "constants" to emptyMap<String, Any>(),
                    "errors" to emptyMap<String, Any>(),
                    "middleware" to emptyMap<String, Any>(),
                    "database" to emptyMap<String, Any>(),
                    "utils" to emptyMap<String, Any>()
                ),
                "features" to emptyMap<String, Any>(),
                "shared" to mapOf(
                    "data" to mapOf(
                        "datasources" to emptyMap<String, Any>(),
                        "models" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>()
                    ),
                    "domain" to mapOf(
                        "entities" to emptyMap<String, Any>(),
                        "repositories" to emptyMap<String, Any>(),
                        "usecases" to emptyMap<String, Any>()
                    ),
                    "presentation" to mapOf(
                        "controllers" to emptyMap<String, Any>(),
                        "routes" to emptyMap<String, Any>(),
                        "middleware" to emptyMap<String, Any>()
                    )
                )
            )
        )
        
        return DirectoryStructure.fromMap(nodeJsStructureMap)
    }
}
