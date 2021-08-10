package com.yieldx.data

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import mu.KotlinLogging
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class DataService(
    private val objectMapper: ObjectMapper,
    private val resourceLoader: ResourceLoader
) {

    private val logger = KotlinLogging.logger {}

    fun getDataFromCsvFile(fileName: String = "exampleData.csv"): List<Finance> {
        logger.info {
            """
            Retrieving file $fileName from resources folder
            """.trimIndent()
        }

        val resource = resourceLoader.getResource("classpath:data/$fileName")
        val inputStream = resource.inputStream
        val mapper = CsvMapper()
        val schema = mapper.schemaFor(Finance::class.java)

        val objectReader = mapper
            .readerFor(Finance::class.java)
            .with(schema)

        val iterator: MappingIterator<Finance> = objectReader.readValues(inputStream)

        inputStream.close()

        return iterator.readAll()
    }
}

// TODO: This is some dummy class
data class Finance(
    val name: String,
    val number: Int
)
