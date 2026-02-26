package org.yourappdev.homeinterior.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import org.yourappdev.homeinterior.domain.model.GenerateRoomRequest
import org.yourappdev.homeinterior.domain.model.GenerateRoomResponse
import org.yourappdev.homeinterior.domain.model.RegisterRequest


class RoomService(val client: HttpClient) {
    suspend fun getRooms() = client.get("rooms/")
    suspend fun generateRoom(request: GenerateRoomRequest): GenerateRoomResponse {
        return client.post("generate-room-image") {
            setBody(request)
        }.body()
    }}