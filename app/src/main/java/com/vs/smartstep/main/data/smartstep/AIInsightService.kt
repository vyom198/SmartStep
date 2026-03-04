package com.vs.smartstep.main.data.smartstep

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.domain.smartStep.AIInsights
import com.vs.smartstep.main.presentation.util.getTimeContext
import com.vs.smartstep.main.presentation.util.getTodayDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AIInsightService(
    private val stepDao: DailyStepDao
) : AIInsights {
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-3-flash-preview")






    override fun getInsights(): Flow<String> = flow {
        try {
            val item = stepDao.getDailyStepByDate(getTodayDate())

            if (item != null) {
                val step = item.steps
                val dailyGoal = item.stepGoal

                val percentage = (step.toFloat() / dailyGoal.toFloat()) * 100
                val timeContext = getTimeContext()

                val prompt = """
                Context: It is currently $timeContext. 
                Data: The user has walked $step steps out of a $dailyGoal goal ($percentage% complete).
                Task: Provide a 10-11 word insight. 
                Tone: Analytical and motivational. 
                Constraint: Return only the insight text. No quotes.
            """.trimIndent()

                // 2. Generate Content
                val response = model.generateContent(prompt)

                // 3. Emit the text (handling potential nulls)
                emit(response.text ?: "Keep moving toward your daily goal today.")

            } else {
                emit("No data found for today. Start your journey now.")
            }
        } catch (e: Exception) {
            emit("AI limit exhaust.")
            e.printStackTrace()
        }
    }
}