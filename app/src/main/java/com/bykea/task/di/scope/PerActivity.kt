package com.bykea.task.di.scope

import dagger.hilt.android.scopes.ActivityRetainedScoped


@ActivityRetainedScoped
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity