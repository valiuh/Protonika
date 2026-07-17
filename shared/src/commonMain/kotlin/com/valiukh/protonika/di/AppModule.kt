package com.valiukh.protonika.di

import org.koin.core.module.Module

/**
 * Aggregated list of Koin modules that make up the application graph.
 */
val appModules: List<Module> = listOf(
    commonModule,
    viewModelModule,
    dataModule,
    domainModule,
)
