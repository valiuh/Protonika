package com.valiukh.protonika.di

import com.valiukh.virtualmachine.Mk61
import com.valiukh.microbasic.Lexer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides the MK-61 virtual machine dependencies.
 */
val commonModule: Module = module {
    single { Mk61() }
    single<CoroutineDispatcher> { Dispatchers.IO }
}