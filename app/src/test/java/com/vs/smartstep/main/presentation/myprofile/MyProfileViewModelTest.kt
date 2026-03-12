package com.vs.smartstep.main.presentation.myprofile

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.vs.smartstep.MainDispatcherRule
import com.vs.smartstep.data.FakeUserProfileStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MyProfileViewModelTest {

    @get:Rule
    val testRule = MainDispatcherRule()

     private lateinit var myProfileViewModel: MyProfileViewModel
     private lateinit var userProfileStore  : FakeUserProfileStore




  @BeforeEach
  fun setUp(){
      userProfileStore = FakeUserProfileStore()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `load data if profile data is not null` () = runTest{
      userProfileStore.saveGender("Male")
      userProfileStore.saveWeightWithUnit(0,17)
      userProfileStore.saveheightWithUnit(0,170)
      println("Debug - Gender: ${userProfileStore.isProfileSetup().first()}")
      myProfileViewModel = MyProfileViewModel(userProfileStore)
      //advanceUntilIdle()
      val state = myProfileViewModel.state.first { it.dataNotNull }
      assertThat(state.dataNotNull).isTrue()
      assertThat(state.currentGender).isEqualTo("Male")
      assertThat(state.selectedUnitforHeight).isEqualTo(0)
      assertThat(state.selectedUnitforWeight).isEqualTo(0)
      assertThat(state.selectedWeight).isEqualTo(17)
      assertThat(state.selectedHeightInCm).isEqualTo(170)
  }
}