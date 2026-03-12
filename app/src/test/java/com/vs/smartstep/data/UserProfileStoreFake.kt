package com.vs.smartstep.data

import com.vs.smartstep.main.domain.smartStep.userProfileStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserProfileStore : userProfileStore {

    private var _gender: String = ""
    private var _heightUnit: Int = 0
    private var _height: Int = 0
    private var _weightUnit: Int = 0
    private var _weight: Int = 0
    private val _isMetric = MutableStateFlow(_heightUnit == 0 && _weightUnit == 0)
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
    override suspend fun getWeightWithUnit() = Pair(_weightUnit, _weight)
    override suspend fun getGender() = _gender
    override suspend fun getHeightWithUnit() = Pair(_heightUnit, _height)
    override suspend fun saveWeightWithUnit(unit: Int, weight: Int) {
        _weightUnit = unit
        _weight = weight
    }
    override suspend fun saveGender(gender: String) {
        _gender = gender
        _isProfileSetup.value = true
    }
    override suspend fun saveheightWithUnit(unit: Int, height: Int) {
        _heightUnit = unit
        _height = height
    }
}