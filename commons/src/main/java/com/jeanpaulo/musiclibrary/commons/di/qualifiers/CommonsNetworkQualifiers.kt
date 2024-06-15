package com.jeanpaulo.musiclibrary.commons.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOScheduler

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainScheduler
