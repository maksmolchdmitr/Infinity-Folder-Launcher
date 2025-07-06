package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import androidx.core.content.edit

class OnboardingUtil(private val context: Context) {
    companion object {
        const val ONBAORDING_KEY = "ONBAORDING_KEY"
        const val IS_COMPLETED_KEY = "IS_COMPLETED_KEY"
    }

    fun onboardingIsCompleted(): Boolean {
        return context.getSharedPreferences(ONBAORDING_KEY, Context.MODE_PRIVATE)
            .getBoolean(IS_COMPLETED_KEY, false)
    }

    fun setOnboardingCompleted() {
        context.getSharedPreferences(ONBAORDING_KEY, Context.MODE_PRIVATE).edit {
            putBoolean(IS_COMPLETED_KEY, true)
        }
    }
}