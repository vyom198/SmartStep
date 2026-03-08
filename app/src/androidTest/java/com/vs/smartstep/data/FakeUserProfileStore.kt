package com.vs.smartstep.data

import com.vs.smartstep.main.domain.smartStep.userProfileStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserProfileStore : userProfileStore {
    // 1. Create StateFlows to hold the "In-Memory" data
    private val _isMetric = MutableStateFlow(true)
    private val _permissionCount = MutableStateFlow(0)
    private val _steps = MutableStateFlow(0)
    private val _isProfileSetup = MutableStateFlow(false)
    private var _backgroundPermissionAsked = false

    // 2. Implement Flow getters
    override fun isMetric(): Flow<Boolean> = _isMetric
    override fun getPermissionCount(): Flow<Int> = _permissionCount
    override fun getStep(): Flow<Int> = _steps
    override fun isProfileSetup(): Flow<Boolean> = _isProfileSetup

    // 3. Implement Suspend Setters (Update the StateFlows)
    override suspend fun saveStep(step: Int) {
        _steps.value = step
    }

    override suspend fun addPermissionCount() {
        _permissionCount.value += 1
    }

    override suspend fun askedBackgroundPermission(isAsked: Boolean) {
        _backgroundPermissionAsked = isAsked
    }

    override suspend fun getIsbackgroundAsked(): Boolean = _backgroundPermissionAsked

    // 4. Implement simple values (Add defaults as needed for your tests)
    override suspend fun getWeightWithUnit() = Pair(0, 0)
    override suspend fun getGender() = "Male"
    override suspend fun getHeightWithUnit() = Pair(0, 0)
    override suspend fun saveWeightWithUnit(unit: Int, weight: Int) {}
    override suspend fun saveGender(gender: String) {}
    override suspend fun saveheightWithUnit(unit: Int, height: Int) {}
}