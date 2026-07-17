package com.valiukh.protonika.di

import com.valiukh.protonika.instrumentation.usecases.ApplySelectedScriptUseCase
import com.valiukh.protonika.instrumentation.usecases.GetAvailableScriptsUseCases
import com.valiukh.protonika.instrumentation.usecases.GetSelectedScriptUseCase
import com.valiukh.protonika.operational.usecases.GetActiveScriptUseCase
import com.valiukh.protonika.operational.usecases.ReadScriptUseCase
import com.valiukh.protonika.operational.usecases.SaveScriptUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val domainModule: Module = module {

    single {
        ReadScriptUseCase(
            coroutineDispatcher = get(),
            scriptRepository = get(),
            scriptsRepository = get()
        )
    }

    single {
        SaveScriptUseCase(
            coroutineDispatcher = get(),
            scriptRepository = get()
        )
    }

    single {
        GetAvailableScriptsUseCases(
            scriptsRepository = get()
        )
    }

    single {
        GetSelectedScriptUseCase(
            scriptsRepository = get()
        )
    }

    single {
        ApplySelectedScriptUseCase(
            scriptsRepository = get()
        )
    }

    single {
        ApplySelectedScriptUseCase(
            scriptsRepository = get()
        )
    }

    single {
        GetActiveScriptUseCase(
            scriptsRepository = get()
        )
    }
}

