package com.valiukh.protonika.di

import com.valiukh.protonika.data.script.ScriptRepository
import com.valiukh.protonika.data.scripts.ScriptsRepository
import com.valiukh.protonika.domain.script.ScriptRepositoryImpl
import com.valiukh.protonika.domain.scripts.ScriptsRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides the repository for getting and saving scripts.
 */
val dataModule: Module = module {
    single<ScriptRepository> { ScriptRepositoryImpl() }

    single<ScriptsRepository> { ScriptsRepositoryImpl() }
}