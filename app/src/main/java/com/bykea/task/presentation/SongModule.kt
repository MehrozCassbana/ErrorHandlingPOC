package com.bykea.task.presentation

import com.bykea.task.core.networking.CoreHelper
import com.bykea.task.data.remote.ApiService
import com.bykea.task.data.repository.SongsRepositoryImpl
import com.bykea.task.di.scope.PerActivity
import com.bykea.task.domain.SongsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
object SongModule {

    @Provides
    @PerActivity
    fun provideSongRepository(
        apiService: ApiService, coreHelper: CoreHelper
    ): SongsRepository {
        return SongsRepositoryImpl(apiService, coreHelper)
    }
}