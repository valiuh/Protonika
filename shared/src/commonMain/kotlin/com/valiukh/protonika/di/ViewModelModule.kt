package com.valiukh.protonika.di

import com.valiukh.protonika.instrumentation.InstrumentationSheetViewModel
import com.valiukh.protonika.operational.OperationalConsoleViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Provides the view models used across the application.
 */
val viewModelModule: Module = module {
    viewModelOf(::OperationalConsoleViewModel)
    viewModelOf(::InstrumentationSheetViewModel)
}
