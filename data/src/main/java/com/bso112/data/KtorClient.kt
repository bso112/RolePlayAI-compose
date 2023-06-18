package com.bso112.data

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    //http 클라이언트
    val httpClient : HttpClient = HttpClient(CIO) {
        // json 설정
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
            })
        }

        // 로깅 설정
        install(Logging){
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("test", "api log: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout){
            requestTimeoutMillis = 10000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 10000
        }

        // 기본적인 api 호출시 넣는 것들 즉, 기본 세팅
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}