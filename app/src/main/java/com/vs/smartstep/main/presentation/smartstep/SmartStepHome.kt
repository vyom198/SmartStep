package com.vs.smartstep.main.presentation.smartstep

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vs.smartstep.core.theme.SmartStepTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SmartStepHomeRoot(
    viewModel: SmartStepHomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SmartStepHomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SmartStepHomeScreen(
    state: SmartStepHomeState,
    onAction: (SmartStepHomeAction) -> Unit,
) {

}

//@Preview
//@Composable
//private fun Preview() {
//    SmartStepTheme {
//        SmartStepHomeScreen(
//            state = SmartStepHomeState(),
//            onAction = {}
//        )
//    }
//}