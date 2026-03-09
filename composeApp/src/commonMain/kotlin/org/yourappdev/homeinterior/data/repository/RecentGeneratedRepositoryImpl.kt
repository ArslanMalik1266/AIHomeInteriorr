package org.yourappdev.homeinterior.data.repository

import kotlinx.coroutines.flow.Flow
import org.yourappdev.homeinterior.data.local.dao.RecentGeneratedDao
import org.yourappdev.homeinterior.data.local.entities.RecentGeneratedEntity
import org.yourappdev.homeinterior.domain.repo.RecentGeneratedRepository

class RecentGeneratedRepositoryImpl(
    private val recentGeneratedDao: RecentGeneratedDao
) : RecentGeneratedRepository {

    override fun getRecentGenerated(): Flow<List<RecentGeneratedEntity>> {
        return recentGeneratedDao.getRecentGenerated()
    }

    override suspend fun saveGenerated(generated: RecentGeneratedEntity) {
        recentGeneratedDao.insertGenerated(generated)
    }

    override suspend fun deleteGeneratedById(id: Long) {
        recentGeneratedDao.deleteGeneratedById(id)
    }
}