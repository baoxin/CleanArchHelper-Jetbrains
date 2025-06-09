package com.baoxin.cleanarchhelper

import com.baoxin.cleanarchhelper.model.DirectoryStructure
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DirectoryStructureTest {
    
    @Test
    fun testEmptyStructureIsLeaf() {
        val structure = DirectoryStructure()
        assertTrue(structure.isLeaf())
    }
    
    @Test
    fun testStructureWithDirectoriesIsNotLeaf() {
        val structure = DirectoryStructure(
            mapOf("data" to DirectoryStructure())
        )
        assertFalse(structure.isLeaf())
    }
    
    @Test
    fun testGetAllPaths() {
        val structure = DirectoryStructure(
            mapOf(
                "data" to DirectoryStructure(
                    mapOf(
                        "models" to DirectoryStructure(),
                        "repositories" to DirectoryStructure()
                    )
                ),
                "domain" to DirectoryStructure(
                    mapOf(
                        "entities" to DirectoryStructure()
                    )
                )
            )
        )
        
        val paths = structure.getAllPaths()
        val expectedPaths = listOf(
            "data",
            "data/models",
            "data/repositories",
            "domain",
            "domain/entities"
        )
        
        assertEquals(expectedPaths.sorted(), paths.sorted())
    }
    
    @Test
    fun testFromMapAndToMap() {
        val originalMap = mapOf(
            "data" to mapOf(
                "models" to emptyMap<String, Any>(),
                "repositories" to emptyMap<String, Any>()
            ),
            "domain" to mapOf(
                "entities" to emptyMap<String, Any>()
            )
        )
        
        val structure = DirectoryStructure.fromMap(originalMap)
        val resultMap = DirectoryStructure.toMap(structure)
        
        assertEquals(originalMap, resultMap)
    }
    
    @Test
    fun testJsonSerialization() {
        val structure = DirectoryStructure(
            mapOf(
                "data" to DirectoryStructure(
                    mapOf("models" to DirectoryStructure())
                )
            )
        )
        
        val json = structure.toJson()
        val deserializedStructure = DirectoryStructure.fromJson(json)
        
        assertEquals(structure.getAllPaths(), deserializedStructure.getAllPaths())
    }
}
