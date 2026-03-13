# Smart Step

Smart Step is a modern Android fitness tracker that helps users monitor their daily steps, distance, calories, and activity trends. The app features an AI‑powered coach that provides contextual insights and recommendations based on the user’s progress. Built with Jetpack Compose and following Material 3 guidelines, Smart Step adapts to both mobile and wide‑screen layouts.

---

## Features
- **Onboarding Profile** – Collects gender (dropdown with Male/Female), height, and weight using custom picker dialogs.
  - Height picker supports centimeters or feet/inches with real‑time conversion.
  - Weight picker supports kilograms or pounds with conversion.
- **Personal Settings Screen** – Edit user profile data at any time (reuses the onboarding screen without the skip button).
- **Home Screen** – Displays today’s step count, daily goal, and a horizontal progress bar.
  - Edit steps directly from the step card (opens date and step input dialog).
  - Pause/resume step tracking – when paused, the step count is dimmed and shows “Paused”.
- **Activity Metrics** – Below the step counter, the app shows:
  - **Distance** – Calculated from steps and height (km or mi, rounded to one decimal).
  - **Calories** – Estimated using weight, gender, and steps (kcal, whole number).
  - **Time** – Total active minutes (rounded, based on periods with steps).
- **Daily Average & 7‑Day History** – Displays average steps over the last 7 days and a horizontal row of days.
  - Each day shows a circular progress indicator (goal completion), day label, and step count.
  - Progress is capped at 100% and uses the goal value at that day.
- **Manual Step Editing** – Edit steps for any date via the edit icon on the home screen.
  - Opens a dialog with date picker (year/month/day scroll columns) and numeric step input.
  - After editing, a new sensor baseline is stored so subsequent sensor steps are added without overwriting the manual value.
- **Reset Today’s Steps** – Confirmation dialog that resets the current day’s step count to zero by updating the sensor baseline.
- **Foreground Notification** – Persistent notification while background tracking is active.
  - Collapsed state shows step count, calories, and progress.
  - Expanded state adds app name, timestamp, and a decorative chevron.
  - Tapping the notification opens the app.
  - Updates silently when new steps are detected.
- **AI Insights Block** – On the home screen, a short AI‑generated message interprets the user’s current activity (e.g., “You’re slightly behind today’s pace”).
  - Refreshed on first launch of the session, returning from background, reaching the daily goal, or changing the goal.
  - Offline state shows a static message and a “Try Again” button.
- **AI Coach Chat Screen** – A lightweight, session‑based chat for deeper interaction.
  - Initial AI message greets the user and summarises today’s activity.
  - Three fixed quick suggestions: *Recommend workout*, *Explain today’s trend*, *How to reach today’s goal?*.
  - Message input expands up to 5 lines; disabled when offline with a “Online connection required” hint.
  - Chat history is **not** persisted – each visit starts a new session.
- **Adaptive Layouts** – Supports Wide Screens


## Tech Stack

- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose + Material 3
- **Architecture**: MVVM with Clean Architecture principles
- **State Management**: State hoisting, ViewModel, Compose State
- **Navigation**: Compose Navigation
- **Local Storage**: DataStore (user preferences), Room (step history and profile data)
- **Background Processing**: Foreground Service for step tracking
- **Sensors**: Android Activity Recognition API / Step Counter Sensor
- **AI Integration**: Gemini API (or any LLM with free tier)
- **Permissions**: Accompanist permissions / ActivityResultContracts
- **Adaptive Layout**: Custom breakpoint at 840dp
- **Dependency Injection**: (Optional) Dagger Hilt / Koin
- **Testing**: JUnit 5, Espresso, Compose UI Testing


---

