package com.yieldx.controllers

import com.yieldx.exceptions.InvalidExampleNameException
import com.yieldx.models.Example
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("example")
class ExampleController {

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ResponseEntity<Example> {
        logger.info {
            """
            GET
            $id
            """.trimIndent()
        }

        return ResponseEntity.ok(Example("example string text"))
    }

    @GetMapping
    fun getWithQueryStringParams(
        @RequestParam queryStringOne: String,
        @RequestParam queryStringTwo: String
    ): ResponseEntity<Map<String, String>> {
        logger.info {
            """
            GET
            $queryStringOne
            $queryStringTwo
            """.trimIndent()
        }

        return ResponseEntity.ok(
            mapOf("queryStringOne" to queryStringOne, "queryStringTwo" to queryStringTwo)
        )
    }

    @PostMapping
    fun post(@RequestBody example: Example): ResponseEntity<Example> {
        logger.info {
            """
            Create example
            ${example.name}
            """.trimIndent()
        }

        if (example.name.isEmpty()) {
            throw InvalidExampleNameException("Example name cannot be empty")
        }

        return ResponseEntity.ok(example)
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidExampleNameException::class)
    fun handleException(invalidExampleNameException: InvalidExampleNameException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(invalidExampleNameException.message)
    }
}
