package com.yieldx.data

import com.yieldx.models.Finance
import com.yieldx.services.CsvService
import io.kotest.matchers.shouldBe
import io.kotest.provided.BaseStringSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader

internal class CsvServiceTest : BaseStringSpec() {
    init {
        val fileContents =
            """
            name,number
            laukii,3
            kahili,7
            """.trimIndent()

        val resource = mockk<Resource> {
            every { inputStream } returns fileContents.byteInputStream()
        }
        val resourceLoader = mockk<ResourceLoader> {
            every { getResource(any()) } returns resource
        }
        val service = CsvService(resourceLoader)

        "getDataFromCsvFile should return list of ${Finance::class.simpleName}" {
            val financeList = service.getFinancesFromResourceFile("")

            financeList.size shouldBe 2
        }
    }
}
