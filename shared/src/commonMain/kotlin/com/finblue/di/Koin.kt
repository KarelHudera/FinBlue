package com.finblue.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import com.finblue.data.network.jsonModule
import com.finblue.data.network.ktorModule
import com.finblue.data.repository.PortfolioRepository
import com.finblue.viewmodel.PortfolioViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            jsonModule,
            ktorModule,
            dispatcherModule,
            repositoryModule,
            viewModelModule,
            databaseModule()
        )
    }
}

val repositoryModule = module {
    single<PortfolioRepository> { PortfolioRepository(get(), get(named("io"))) }
}

val viewModelModule = module {
    viewModelOf(::PortfolioViewModel)
}

val dispatcherModule = module {
    single(named("io")) { Dispatchers.IO }
}

expect fun databaseModule(): Module

fun getPortfolioViewModel(): PortfolioViewModel = KoinPlatform.getKoin().get()