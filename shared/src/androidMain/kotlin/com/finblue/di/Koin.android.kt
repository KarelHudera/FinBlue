package com.finblue.di

import com.finblue.cache.AndroidDatabaseDriverFactory
import com.finblue.cache.Database
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun databaseModule() = module {
    single<Database> {
        Database(databaseDriverFactory = AndroidDatabaseDriverFactory(androidContext()))
    }
}