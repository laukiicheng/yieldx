package com.yieldx.controllers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.yieldx.exceptions.MultiFileNullException
import com.yieldx.models.Finance
import com.yieldx.services.CsvService
import com.yieldx.services.XmlService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("finance")
class FinanceController(
    private val objectMapper: ObjectMapper,
    private val csvService: CsvService,
    private val xmlService: XmlService
) {

    @GetMapping
    fun getFinanceFromFile(): ResponseEntity<List<Finance>> {
        val finance = csvService.getFinancesFromResourceFile()
        return ResponseEntity.ok(finance)
    }

    @PostMapping("file")
    fun multiPartFileExample(@RequestParam(required = false) file: MultipartFile?): ResponseEntity<String> {
        if (file == null) {
            throw MultiFileNullException("file must not be null")
        }

        val fileContent = String(file.bytes)
        return ResponseEntity.ok(fileContent)
    }

    @PostMapping("multiPartFile")
    fun getFinanceFromCsvFile(@RequestParam(required = false) file: MultipartFile?): ResponseEntity<List<Finance>> {
        if (file == null) {
            throw MultiFileNullException("file must not be null")
        }

        val fileContent = csvService.getFinanceFromMultiPartFile(file)
        return ResponseEntity.ok(fileContent)
    }

    @PostMapping("xml")
    fun getFinanceFromXmlFile(): ResponseEntity<List<Finance>> {
        val fileContent = xmlService.getFinancesFromResourceFile()

        return ResponseEntity.ok(fileContent)
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultiFileNullException::class)
    fun handleException(multiFileNullException: MultiFileNullException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(multiFileNullException.message)
    }

    // TODO: Remove if not needed
    private inline fun <reified T> getObjectsFromJson(json: String): List<T> {
        return objectMapper.readValue(json, object : TypeReference<List<T>>() {})
    }
}
