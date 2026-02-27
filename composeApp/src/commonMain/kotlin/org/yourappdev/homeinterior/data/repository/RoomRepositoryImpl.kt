package org.yourappdev.homeinterior.data.repository

import io.ktor.client.call.body
import org.yourappdev.homeinterior.data.remote.service.RoomService
import org.yourappdev.homeinterior.domain.model.GenerateRoomResponse
import org.yourappdev.homeinterior.domain.model.Rooms
import org.yourappdev.homeinterior.domain.repo.RoomsRepository

class RoomRepositoryImpl(val roomService: RoomService) : RoomsRepository {
    override suspend fun getRoomsList(): Rooms {
        val response = roomService.getRooms().body<Rooms>()
        return response
    }

    override suspend fun generateRoom(
        imageBytes: ByteArray, // Nullable hata diya kyunki file lazmi hai
        fileName: String,
        prompt: String,
        strength: Float
    ): GenerateRoomResponse {
        return roomService.generateRoom(imageBytes, prompt, strength)

    }
}