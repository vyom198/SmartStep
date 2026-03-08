package com.vs.smartstep.main.presentation.smartstep

import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.vs.smartstep.core.room.DailyStep
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.core.room.DailyStepDatabase
import com.vs.smartstep.data.FakeAIInsights
import com.vs.smartstep.data.FakeConnectionProvider
import com.vs.smartstep.data.FakeStepProvider
import com.vs.smartstep.data.FakeUserProfileStore
import com.vs.smartstep.main.presentation.util.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject


class SmartStepHomeViewModelTest : KoinTest{

    private lateinit var viewModel: SmartStepHomeViewModel
    private val database: DailyStepDatabase by inject()
    private val fakeDao: DailyStepDao by inject()



    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        database.clearAllTables()
        Dispatchers.setMain(testDispatcher)
        viewModel = SmartStepHomeViewModel(
            context = ApplicationProvider.getApplicationContext(),
            userProfileStore = FakeUserProfileStore(),
            stepProvider = FakeStepProvider(),
            dao = fakeDao,
            insights = FakeAIInsights(),
            connectionProvider = FakeConnectionProvider()
        )
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

//    @Test
//    fun InsertUpdate() = runTest {
//        val today = getTodayDate()
//        val initialSteps = 500
//
//        // ✅ Collect from state flow and wait for emissions
//        viewModel.state.test {
//            // ✅ Collecting triggers onStart
//            // Get initial state (stepCount = 0)
//            val initialState = awaitItem()
//            assertThat(initialState.stepCount).isEqualTo(0)
//
//            // Advance to ensure all initial loading is done
//            testDispatcher.scheduler.advanceUntilIdle()
//
//            // NOW insert data
//            fakeDao.insertDailyStep(DailyStep(date = today, steps = initialSteps))
//
//            // Advance to let database Flow emit
//            testDispatcher.scheduler.advanceUntilIdle()
//
//            // Wait for updated state
//            val updatedState = awaitItem()
//            assertThat(updatedState.stepCount).isEqualTo(initialSteps)
//        }
//    }



}