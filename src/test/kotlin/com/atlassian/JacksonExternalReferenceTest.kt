package com.atlassian

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JacksonExternalReferenceTest {
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
    }

    @Test
    internal fun `deserialize with type field at the end fails`() {
        objectMapper.readValue(loadJson("sample-fail.json"), Pet::class.java)
    }

    @Test
    internal fun `deserialize with type field at the top succeeds`() {
        objectMapper.readValue(loadJson("sample-success.json"), Pet::class.java)
    }

    private fun loadJson(jsonFile: String) = this.javaClass.classLoader.getResourceAsStream(jsonFile)
}