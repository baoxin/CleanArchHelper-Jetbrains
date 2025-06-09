package com.baoxin.cleanarchhelper.services

import com.baoxin.cleanarchhelper.config.DefaultStructures
import com.baoxin.cleanarchhelper.model.DirectoryStructure
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * 结构配置服务
 * 负责管理和持久化目录结构配置
 */
@Service(Service.Level.APP)
@State(
    name = "CleanArchHelperSettings",
    storages = [Storage("cleanArchHelper.xml")]
)
class StructureConfigService : PersistentStateComponent<StructureConfigService.State> {
    
    data class State(
        var baseStructureJson: String = "",
        var featureStructureJson: String = "",
        var isFirstTime: Boolean = true
    )
    
    private var myState = State()
    
    override fun getState(): State = myState
    
    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, myState)
    }
    
    /**
     * 获取基础结构配置
     */
    fun getBaseStructure(): DirectoryStructure {
        return if (myState.baseStructureJson.isNotEmpty()) {
            DirectoryStructure.fromJson(myState.baseStructureJson)
        } else {
            DefaultStructures.getDefaultBaseStructure()
        }
    }
    
    /**
     * 设置基础结构配置
     */
    fun setBaseStructure(structure: DirectoryStructure) {
        myState.baseStructureJson = structure.toJson()
    }
    
    /**
     * 获取 Feature 结构配置
     */
    fun getFeatureStructure(): DirectoryStructure {
        return if (myState.featureStructureJson.isNotEmpty()) {
            DirectoryStructure.fromJson(myState.featureStructureJson)
        } else {
            DefaultStructures.getDefaultFeatureStructure()
        }
    }
    
    /**
     * 设置 Feature 结构配置
     */
    fun setFeatureStructure(structure: DirectoryStructure) {
        myState.featureStructureJson = structure.toJson()
    }
    
    /**
     * 重置为默认配置
     */
    fun resetToDefaults() {
        myState.baseStructureJson = ""
        myState.featureStructureJson = ""
    }
    
    /**
     * 检查是否为首次使用
     */
    fun isFirstTime(): Boolean = myState.isFirstTime
    
    /**
     * 标记已不是首次使用
     */
    fun markNotFirstTime() {
        myState.isFirstTime = false
    }
    
    /**
     * 根据项目类型获取推荐的基础结构
     */
    fun getRecommendedBaseStructure(projectType: String): DirectoryStructure {
        return DefaultStructures.getRecommendedBaseStructure(projectType)
    }
    
    companion object {
        /**
         * 获取服务实例
         */
        fun getInstance(): StructureConfigService {
            return ApplicationManager.getApplication().getService(StructureConfigService::class.java)
        }
    }
}
