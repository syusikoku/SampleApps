package com.zy.sunflower.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.repository.local.GardenPlantsRepository
import com.zy.sunflower.repository.local.PlantsRepository
import com.zy.sunflower.repository.local.room.db.AppDatabase
import com.zy.sunflower.viewmodel.factory.GardenPlantViewModelFactory
import com.zy.sunflower.viewmodel.factory.PlantDetailViewModelFactory
import com.zy.sunflower.viewmodel.factory.PlantsViewModelFactory

/**
 * 静态方法用于注入class需要的Activity和Fragment
 */
object InjectorUtils {
    /**
     * 构建PlantsRepository
     */
    fun getPlantsRepository(ctx: Context): PlantsRepository {
        LogUtil.loge("getPlantsRepository")
        val plantDao = AppDatabase.getInstance(ctx.applicationContext).plantDao()
        return PlantsRepository.getInstance(plantDao)
    }

    /**
     * 构建GradenPlantsRepository
     */
    fun getGardenPlantsRepository(ctx: Context): GardenPlantsRepository {
        return GardenPlantsRepository.getInstance(AppDatabase.getInstance(ctx.applicationContext).gardenPlantDao())
    }

    /**
     * 构建PlantsViewModelFactory
     */
    fun providePlantListViewModelFactory(fragment: Fragment): PlantsViewModelFactory {
        LogUtil.loge("providePlantListViewModelFactory")
        val plantsRepository = getPlantsRepository(fragment.requireContext())
        return PlantsViewModelFactory(
            plantsRepository,
            fragment
        )
    }

    /**
     * 构建PlantDetailViewModelFactory
     */
    fun providePlantDetailViewModelFactory(
        act: Activity,
        plantId: String
    ): PlantDetailViewModelFactory {
        LogUtil.loge("providePlantDetailViewModelFactory")
        val plantsRepository = getPlantsRepository(act)
        val gardenPlantsRepository = getGardenPlantsRepository(act)
        return PlantDetailViewModelFactory(
            plantsRepository,
            gardenPlantsRepository,
            plantId
        )
    }

    /**
     * 构建GardenPlantViewModelFactory
     */
    fun provideGardenPlantViewModelFactory(fragment: Fragment): GardenPlantViewModelFactory {
        val gardenPlantsRepository = getGardenPlantsRepository(fragment.requireContext())
        return GardenPlantViewModelFactory(gardenPlantsRepository)
    }

}