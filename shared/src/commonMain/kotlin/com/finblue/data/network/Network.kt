package com.finblue.data.network

import com.finblue.BuildKonfig
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val jsonModule = module {
    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
            coerceInputValues = true
        }
    }
}

val ktorModule = module {
    single {
        HttpClient {
            expectSuccess = true
            defaultRequest {
                url("") // TODO: Set the base URL
                url {
                    parameters.append("api_key", BuildKonfig.API_KEY)
                }
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(message)
                    }
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(get())
            }
        }
    }
}