package com.example.auracle.api

import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiTester {

    private val LOG = "ApiTester"
    private val client = OkHttpClient()

    fun testApi() {
        val request = Request.Builder()
            .url("https://api.publicapis.org/entries")
            .build()

        Log.d(LOG, "Inside makeACall()")

        try {

            Log.d(LOG, "Before Response")
            val response = client.newCall(request).execute()
            Log.d(LOG, "After Response")

            println(response.body?.string())
        } catch (e: Exception) {
            Log.e(LOG, "Exception")
            Log.e(LOG, e.toString())
        }

    }

    suspend fun justDelay() {
        delay(6000)
        Log.d(LOG, "After delay")
    }

}