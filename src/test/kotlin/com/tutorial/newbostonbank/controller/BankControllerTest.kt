package com.tutorial.newbostonbank.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.tutorial.newbostonbank.model.Bank
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should returns all banks`() {
            // when/then
            mockMvc.get(baseUrl)
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].account_number") { value("1234") }
                }
        }
    }

    @Nested
    @DisplayName("GET api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {
            // given
            val accountNumber = "1234"

            // when
            val result = mockMvc.get("$baseUrl/$accountNumber")

            // then
            result.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.account_number") { value(accountNumber) }
                jsonPath("$.trust") { value(3.14) }
                jsonPath("$.default_transaction_fee") { value(17) }
            }
        }

        @Test
        fun `should return NOT FOUND when the account doesn't exists`() {
            // given
            val notExistentAccountNumber = "notExists"

            // when
            val result = mockMvc.get("$baseUrl/$notExistentAccountNumber")

            // then
            result.andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostBanks {
        @Test
        fun `should add the new bank with the received parameters`() {
            // given
            val newBank = Bank("acc123", 31.415, 2)

            // when
            val postResult = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            // then
            postResult.andExpect {
                status { isCreated() }
                content { MediaType.APPLICATION_JSON }
                jsonPath("$.account_number") { value("acc123") }
                jsonPath("$.trust") { value(31.415) }
                jsonPath("$.default_transaction_fee") { value(2) }
            }

            mockMvc.get("$baseUrl/${newBank.accountNumber}").andExpect {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(newBank)) }
            }
        }

        @Test
        fun `should return UNPROCESSABLE ENTITY when trying to create an existent bank` () {
            // given
            val anExistentBank = Bank("1234", 3.14, 17)

            // when
            val postResult = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(anExistentBank)
            }

            // then
            postResult.andExpect {
                status { isUnprocessableEntity() }
            }
        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun `should update an existing bank` () {
            // given
            val updatedBank = Bank("1234", 1.0, 1)

            // when
            val result = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }

            // then
            result.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
                content { json(objectMapper.writeValueAsString(updatedBank)) }
            }

            mockMvc.get("$baseUrl/${updatedBank.accountNumber}").andExpect {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(updatedBank)) }
            }
        }

        @Test
        fun `should return a UNPROCESSABLE ENTITY if there is no bank with the given account number` () {
            // given
            val aNonExistentBank = Bank("not_exits", 0.0, 1)

            // when
            val result = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(aNonExistentBank)
            }

            // then
            result.andExpect {
                status { isUnprocessableEntity() }
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBanks {

        @Test
        @DirtiesContext
        fun `should delete an existent bank` () {
            // given
            val anExistingAccountNumber = "1234"

            // when
            val result = mockMvc.delete("$baseUrl/$anExistingAccountNumber")

            // then
            result.andExpect {
                status { isNoContent() }
            }

            mockMvc.get("$baseUrl/$anExistingAccountNumber").andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `should return UNPROCESSABLE ENTITY when trying to delete a non existing bank` () {
            // given
            val aNonExistingAccountNumber = "not_exists"

            // when
            val results = mockMvc.delete("$baseUrl/$aNonExistingAccountNumber")

            // then
            results.andExpect {
                status { isUnprocessableEntity() }
            }

        }
    }

}