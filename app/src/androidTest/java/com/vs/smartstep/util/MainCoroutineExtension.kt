package com.vs.smartstep.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain


//@OptIn(ExperimentalCoroutinesApi::class)
//class MainCoroutineExtension(
//    val testDispatcher: TestDispatcher = StandardTestDispatcher()
//): BeforeEachCallback, AfterEachCallback {
//
//    override fun beforeEach(context: ExtensionContext?) {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    override fun afterEach(context: ExtensionContext?) {
//        Dispatchers.resetMain()
//    }
//}