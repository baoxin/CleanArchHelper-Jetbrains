package com.baoxin.cleanarchhelper.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

/**
 * JSON 格式化工具类
 */
object JsonFormatter {
    
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    
    /**
     * 格式化 JSON 字符串，使其更易读
     */
    fun formatJson(json: String): String {
        return try {
            val jsonElement = JsonParser.parseString(json)
            gson.toJson(jsonElement)
        } catch (e: Exception) {
            // 如果解析失败，返回原始字符串
            json
        }
    }
    
    /**
     * 压缩 JSON 字符串，移除多余的空白字符
     */
    fun compactJson(json: String): String {
        return try {
            val jsonElement = JsonParser.parseString(json)
            Gson().toJson(jsonElement)
        } catch (e: Exception) {
            // 如果解析失败，返回原始字符串
            json
        }
    }
    
    /**
     * 验证 JSON 字符串是否有效
     */
    fun isValidJson(json: String): Boolean {
        return try {
            JsonParser.parseString(json)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 获取 JSON 解析错误信息
     */
    fun getJsonError(json: String): String? {
        return try {
            JsonParser.parseString(json)
            null
        } catch (e: Exception) {
            e.message
        }
    }
}
