package com.github.humb1t.cleverbotapi

import org.junit.Test
import java.io.IOException

const val TEST_API_KEY: String = "TEST_API_KEY"

/**
 * Test for CleverBotQuery.
 *
 * @author Nikita_Puzankov
 */
class CleverBotQueryTest {

    /**
     * Because there is no real API Key it would always fail with 401 response code.
     */
    @Test(expected = IOException::class)
    fun sendEmptyRequest() {
        val cleverBotQuery: CleverBotQuery = CleverBotQuery(apiKey = TEST_API_KEY, phrase = "")
        cleverBotQuery.sendRequest()
    }
}