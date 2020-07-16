package com.zy.sunflower.viewmodel

import androidx.lifecycle.*
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.repository.local.PlantsRepository
import com.zy.sunflower.repository.local.room.entry.Plant

/**
 * 植物列表viewmodel
 */
class PlantsListViewModel internal constructor(
    plantsRepository: PlantsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }

    val plants: LiveData<List<Plant>> = getSavedGrowZoneNumber().switchMap {
        LogUtil.loge("getSavedGrowZoneNumber-> switchMap")
        if (it == NO_GROW_ZONE) {
            LogUtil.loge("getSavedGrowZoneNumber-> getPlants")
            plantsRepository.getPlants()
        } else {
            LogUtil.loge("getSavedGrowZoneNumber-> getPlantsWidthGrowZoneNumber")
            plantsRepository.getPlantsWithGrowZoneNumber(it)
        }
    }

    /**
     * 获取保存的ZoneNumber
     */
    private fun getSavedGrowZoneNumber(): MutableLiveData<Int> {
        LogUtil.loge("getSavedGrowZoneNumber ... ")
        return savedStateHandle.getLiveData(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
    }


    /**
     * 是否过滤过
     */
    fun isFiltered() = getSavedGrowZoneNumber().value != NO_GROW_ZONE

    /**
     * 清除ZoneNumber
     */
    fun clearGrowZoneNumber() {
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
    }

    /**
     * 设置ZoneNumber
     */
    fun setGrowZoneNumber(num: Int) {
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, num)
    }
}