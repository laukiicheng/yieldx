package com.yieldx.services

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.yieldx.models.Finance
import mu.KotlinLogging
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Component
class DataService(private val resourceLoader: ResourceLoader) {

    private val logger = KotlinLogging.logger {}

    fun getDataFromCsvFile(fileName: String = "exampleData.csv"): List<Finance> {
        logger.info { "Retrieving file $fileName from resources folder" }

        val resource = resourceLoader.getResource("classpath:data/$fileName")
        return getObjectsFromCsv(resource.inputStream)
    }

    fun getFinanceFromMultiPartFile(file: MultipartFile): List<Finance> {
        logger.info { "Retrieving list of ${Finance::class.simpleName} from ${MultipartFile::class.simpleName} ${file.name}" }

        return getObjectsFromCsv(file.inputStream)
    }

    fun getDataFromMultiPartFile(file: MultipartFile): String {
        logger.info { "Retrieving content as string from ${MultipartFile::class.simpleName} from ${file.name}" }

        return String(file.bytes)
    }

    private inline fun <reified T> getObjectsFromCsv(inputStream: InputStream): List<T> {
        inputStream.use {
            val mapper = CsvMapper()
            mapper.registerKotlinModule()

            val schema = mapper
                .schemaFor(T::class.java)
                .withHeader()

            val objectReader = mapper
                .readerFor(T::class.java)
                .with(schema)

            val iterator: MappingIterator<T> = objectReader.readValues(it)

            return iterator.readAll()
        }
    }
}

