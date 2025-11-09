package io.github.kei_1111.kmp_sample_library.di

import io.github.kei_1111.kmp_sample_library.core.network.di.networkModule
import io.github.kei_1111.kmp_sample_library.data.di.dataModule
import io.github.kei_1111.kmp_sample_library.feature.home.di.homeModule
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

actual fun initKoin(appContext: Any?) {
    startKoin {
        modules(networkModule, dataModule, homeModule)
    }.koin
}

@OptIn(BetaInteropApi::class)
@Suppress("unused")
object KoinResolver : KoinComponent {
    fun resolve(objCObject: Any): Any = when (objCObject) {
        is ObjCProtocol -> getKoin().get(getOriginalKotlinClass(objCProtocol = objCObject)!!)

        is ObjCClass -> getKoin().get(getOriginalKotlinClass(objCClass = objCObject)!!)

        else -> throw Exception(message = "Unknown Object Type.")
    }
}