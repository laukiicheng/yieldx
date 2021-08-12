package com.yieldx.controllers

import com.yieldx.exceptions.MultiFileNullException
import com.yieldx.models.Finance
import com.yieldx.services.DataService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("finance")
class FinanceController(private val dataService: DataService) {

    @GetMapping
    fun getFinanceFromFile(): ResponseEntity<List<Finance>> {
        val finance = dataService.getDataFromCsvFile()
        return ResponseEntity.ok(finance)
    }

    @PostMapping("multiPartFile")
    fun multiPartFileExample(@RequestParam(required = false) file: MultipartFile?): ResponseEntity<String> {
        if (file == null) {
            throw MultiFileNullException("file must not be null")
        }

        val fileContent = dataService.getDataFromMultiPartFile(file)
        return ResponseEntity.ok(fileContent)
    }

    @PostMapping("finance/multiPartFile")
    fun getFinanceFromFile(@RequestParam(required = false) file: MultipartFile?): ResponseEntity<List<Finance>> {
        if (file == null) {
            throw MultiFileNullException("file must not be null")
        }

        val fileContent = dataService.getFinanceFromMultiPartFile(file)
        return ResponseEntity.ok(fileContent)
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultiFileNullException::class)
    fun handleException(multiFileNullException: MultiFileNullException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(multiFileNullException.message)
    }
}
