package com.yieldx.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.yieldx.models.Finance
import mu.KotlinLogging
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class XmlService(private val resourceLoader: ResourceLoader) {

    private val logger = KotlinLogging.logger {}

    fun getFinancesFromResourceFile(fileName: String = "xmlExampleData.xml"): List<Finance> {
        logger.info { "Retrieving file $fileName from resources folder" }

        val resource = resourceLoader.getResource("classpath:data/$fileName")

        resource.inputStream.use {
            val xmlMapper = XmlMapper()
            xmlMapper.registerKotlinModule()

            return xmlMapper.readValue(it, object : TypeReference<List<Finance>>() {})
        }
    }
}
