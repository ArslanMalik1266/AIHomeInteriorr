package org.yourappdev.homeinterior.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.*
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import org.yourappdev.homeinterior.domain.model.CreditResponse
import org.yourappdev.homeinterior.domain.model.GenerateRoomResponse

class RoomService(val client: HttpClient,
                  private val baseUrl: String) {

    private fun fullUrl(path: String) = "$baseUrl/$path"

    suspend fun getRooms(): HttpResponse {
        val url = fullUrl("rooms/")
        println("DEBUG_SERVICE: Calling Rooms with Token...")

        return client.get(url) {
            header("Authorization", "Bearer 13|gd4J0SpTwI53hTzn9z6nmOtYSVhu6AdBfB2CY7qw2e1c3419")
        }
    }
    suspend fun generateRoom(
        imageBytes: ByteArray,
        prompt: String,
        strength: Float
    ): GenerateRoomResponse {
        println("DEBUG_SERVICE: 1. Starting generateRoom call")
        println("DEBUG_SERVICE: 2. URL -> ${fullUrl("generate-room-image")}")
        println("DEBUG_SERVICE: 3. Image Size -> ${imageBytes.size} bytes")

        return try {
            val response = client.submitFormWithBinaryData(
                url = fullUrl("generate-room-image"),
                formData = formData {
                    append("prompt", prompt)
                    append("strength", strength.toString())
                    append("image", imageBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"room_image.jpg\"")
                    })
                }
            ) {
                header("Authorization", "Bearer 13|gd4J0SpTwI53hTzn9z6nmOtYSVhu6AdBfB2CY7qw2e1c3419")
                println("DEBUG_SERVICE: 4. Request sent with Header.")
            }

            // Response check karne ke liye
            println("DEBUG_SERVICE: 5. Response Status -> ${response.status}")
            val errorBody = response.bodyAsText()
            println("DEBUG_SERVICE: RAW_RESPONSE_BODY -> $errorBody")
            val body = response.body<GenerateRoomResponse>()
            println("DEBUG_SERVICE: 6. Parse Success! Server Response -> ${body.success}")

            body
        } catch (e: Exception) {
            println("DEBUG_SERVICE: ERROR! -> ${e.message}")
            println("DEBUG_SERVICE: ERROR CAUSE! -> ${e.cause}")
            throw e
        }
    }


}