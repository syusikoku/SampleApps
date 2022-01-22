package com.zy.jet.notebook.jetpack.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zy.jet.notebook.jetpack.InjectUtils
import com.zy.jet.notebook.jetpack.repository.GankRepository
import com.zy.jet.notebook.jetpack.repository.PlantDbRepository
import com.zy.jet.notebook.jetpack.viewmodel.*

/**
 * 代码重构的ViewModel创建工厂
 */
class KasaViewModelFactory(
    val plantDbRepository: PlantDbRepository,
    val gankRepository: GankRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel()
                }

                isAssignableFrom(HomeListViewModel::class.java) -> {
                    HomeListViewModel()
                }

                isAssignableFrom(PlantViewModel::class.java) -> {
                    PlantViewModel(plantDbRepository)
                }

                isAssignableFrom(GankViewModel::class.java) -> {
                    GankViewModel(gankRepository)
                }

                isAssignableFrom(SampleListViewModel::class.java) -> {
                    SampleListViewModel()
                }

                isAssignableFrom(NavFragmentViewModel::class.java) -> {
                    NavFragmentViewModel()
                }

                else -> {
                    throw IllegalArgumentException("无效参数${modelClass.simpleName}")
                }
            }
        } as T

    companion object {
        private var INSTANCE: KasaViewModelFactory? = null

        fun getInstance(context: Context): KasaViewModelFactory =
            INSTANCE ?: synchronized(KasaViewModelFactory::class.java) {
                INSTANCE ?: KasaViewModelFactory(
                    InjectUtils.getPlantsRepository(context),
                    InjectUtils.provedeGankRepository()
                )
            }

    }
}