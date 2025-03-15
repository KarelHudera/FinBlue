package com.finblue.di

import com.finblue.cache.Database
import com.finblue.cache.IOSDatabaseDriverFactory
import org.koin.dsl.module

actual fun databaseModule() = module {
    single<Database> {
        Database(databaseDriverFactory = IOSDatabaseDriverFactory())
    }
}