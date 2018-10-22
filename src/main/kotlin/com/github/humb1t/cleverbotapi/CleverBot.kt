package com.github.humb1t.cleverbotapi

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL


/**
 * A CleverBot Query
 *
 * A `CleverBotQuery` object contains all of the necessary tools to query CleverBot API's servers
 * Inspired by https://github.com/Headline/CleverBotAPI-Java.
 *
 * @author Nikita Puzankov
 * @version 1.0.0
 */
class CleverBotQuery

/**
 * CleverBotQuery constructor
 *
 * Conversation identifer is set empty, thus calling sendRequest immediately after
 * instantiation will create a new conversation
 *
 * @param key API key (cleverbot.com/api)
 * @param phrase input phrase
 */
(val apiKey: String, var phrase: String?) {
    /**
     * This is a unique string used to identify conversation context to the CleverBot API servers.
     * This string is updated after every `sendRequest()` call.
     *
     * Set to empty ("") in order to start a new conversation with the CleverBot.
     */
    var conversationID: String? = null
    /**
     * Response from a request. Get must be used after `sendRequest()`
     *
     * Set only used internally
     */
    var response: String? = null
        private set
    /**
     * CleverBot API queries provide a random number greater than zero and less than one thousand.
     *
     * Set only used internally
     */
    var randomNumber: Int = 0
        private set

    init {
        this.conversationID = ""
    }

    /**
     * Sends request to CleverBot servers. API key and phrase should be set prior to this call
     *
     * @throws IOException exception upon query failure
     */
    @Throws(IOException::class)
    fun sendRequest() {
        val url = URL(formatRequest(URL_STRING, this.apiKey, this.phrase!!, this.conversationID!!))

        val urlConnection = url.openConnection()

        val `in` = BufferedReader(InputStreamReader(urlConnection.getInputStream()))
        val inputLine = `in`.readLine()

        val jsonObject = ObjectMapper().readTree(inputLine)!!

        this.conversationID = jsonObject.get("cs").textValue()
        this.response = jsonObject.get("output").textValue()
        this.randomNumber = jsonObject.get("random_number").intValue()
        `in`.close()
    }


    /**
     * URL String used for CleverBot connection. **Used internally**
     */
    val URL_STRING = "https://www.cleverbot.com/getreply?key="

    /**
     * URL request formatter
     *
     * @param url starting url to connect to
     * @param key API key (cleverbot.com/api)
     * @param phrase input to be sent to CleverBot servers
     * @param conversationID unique conversation identifer
     * @return String object containing properly formatted URL
     */
    private fun formatRequest(url: String, key: String, phrase: String, conversationID: String): String {
        val formattedPhrase = phrase.replace("\\s+".toRegex(), "+")
        return String.format(
                "%s%s&input=%s&wrapper=humb1t22kotlinapi%s",
                url,
                key,
                formattedPhrase,
                if (conversationID == "") "" else "&cs=$conversationID"
        )
    }
}

