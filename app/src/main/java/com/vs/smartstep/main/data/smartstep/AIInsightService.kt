package com.vs.smartstep.main.data.smartstep

import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.vs.smartstep.R
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.domain.smartStep.AIInsights
import com.vs.smartstep.main.presentation.util.getTimeContext
import com.vs.smartstep.main.presentation.util.getTodayDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AIInsightService(
    private val stepDao: DailyStepDao ,
    private val context : Context
) : AIInsights {
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash-lite")

   val default = context.getString(R.string.connect_to_the_internet_to_get_ai_insights)


    override suspend  fun getInitialMessage (): String {
        try {
            val timeContext = getTimeContext()
            val item = stepDao.getDailyStepByDate(getTodayDate())
            val percentage = item!!.steps.toFloat() / item.stepGoal.toFloat() * 100
            val prompt = """
            You are an AI fitness coach. Generate a 40-50 word welcome message.
            
            Context: User has completed ${"%.1f".format(percentage)}% of daily step goal in the $timeContext.
            
            Message must:
            - Start with greeting and "I'm your AI fitness coach"
            - Acknowledge their progress naturally (without using exact percentages)
            - End with "How can I help you today?"
            - Be warm and motivational
            - No quotes or formatting
        """.trimIndent()
            return model.generateContent(prompt).text?.trim() ?: ""
        }catch (e: Exception) {
            e.printStackTrace()
        }
       return  ""
    }


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
                Task: Provide a 6-8 word insight. 
                Tone: Analytical and motivational. 
                Constraint: Return only the insight text. No quotes.
            """.trimIndent()

                // 2. Generate Content
                val response = model.generateContent(prompt)
                Log.d("AIInsightService", "Response: ${response.text}")

                // 3. Emit the text (handling potential nulls)
                emit(response.text ?: default )

            } else {
                emit("No data found for today. Start your journey now.")
            }
        } catch (e: Exception) {
            emit("Loading...")
            e.printStackTrace()
        }
    }


    override suspend fun chatWithAI(query: String): String {
        try {
            val item = stepDao.getDailyStepByDate(getTodayDate())!!

                val step = item.steps
                val dailyGoal = item.stepGoal
                val percentage = (step.toFloat() / dailyGoal.toFloat()) * 100
                val timeContext = getTimeContext()

            val prompt = """
            You are an AI fitness coach for a step-tracking app. Answer the user's question based on their current activity context.
            
            Context:
            - Current steps: $step out of $dailyGoal goal
            - Progress: ${"%.1f".format(percentage)}% complete
            - Time of day: $timeContext
            
            User Question: $query
            
            Rules:
            - Use the context to give personalized answers
            - Don't repeat raw numbers unless necessary
            - Be motivational and encouraging
            - Don't give medical advice
            - Answer naturally as a fitness coach
            
            Generate a helpful response to the user's question.
        """.trimIndent()

            val response = model.generateContent(prompt)
            Log.d("AIInsightService", "Response: ${response.text}")
           return response.text?.trim() ?: "I'm here to help with your fitness questions. Could you please try again?"
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}