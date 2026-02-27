package org.yourappdev.homeinterior.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.*
import io.ktor.client.request.get
import io.ktor.http.*
import org.yourappdev.homeinterior.domain.model.GenerateRoomResponse

class RoomService(val client: HttpClient) {

    suspend fun getRooms() = client.get("rooms/")

    // CommonMain-safe: send image bytes directly
    suspend fun generateRoom(
        imageBytes: ByteArray,
        prompt: String,
        strength: Float
    ): GenerateRoomResponse {
        return client.submitFormWithBinaryData(
            url = "generate-room-image",
            formData = formData {
                append("prompt", prompt)
                append("strength", strength.toString())
                // Key 'image' wahi hai jo Postman mein File ke liye use ki
                append("image", imageBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"room_image.jpg\"")
                })
            }
        ).body()
    }
}