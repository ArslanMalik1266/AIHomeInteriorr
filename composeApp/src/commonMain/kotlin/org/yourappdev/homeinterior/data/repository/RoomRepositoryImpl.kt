package org.yourappdev.homeinterior.data.repository

import io.ktor.client.call.body
import org.yourappdev.homeinterior.data.local.dao.ProfileDao
import org.yourappdev.homeinterior.data.remote.service.AuthService
import org.yourappdev.homeinterior.data.remote.service.RoomService
import org.yourappdev.homeinterior.domain.model.RegisterResponse
import org.yourappdev.homeinterior.domain.model.Room
import org.yourappdev.homeinterior.domain.model.Rooms
import org.yourappdev.homeinterior.domain.repo.AuthRepository
import org.yourappdev.homeinterior.domain.repo.RoomsRepository

class RoomRepositoryImpl(val roomService: RoomService) : RoomsRepository {
    override suspend fun getRoomsList(): Rooms {
        val response = roomService.getRooms().body<Rooms>()

        return response
    }
}