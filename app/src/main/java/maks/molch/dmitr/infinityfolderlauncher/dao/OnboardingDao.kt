package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import androidx.core.content.edit

class OnboardingDao(private val context: Context) {
    companion object {
        const val ONBAORDING_KEY = "ONBAORDING_KEY"
        const val IS_COMPLETED_KEY = "IS_COMPLETED_KEY"
        const val LOGIN_COMPLETED_KEY = "LOGIN_COMPLETED_KEY"
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

    fun loginIsCompleted(): Boolean {
        return context.getSharedPreferences(ONBAORDING_KEY, Context.MODE_PRIVATE)
            .getBoolean(LOGIN_COMPLETED_KEY, false)
    }

    fun setLoginCompleted() {
        context.getSharedPreferences(ONBAORDING_KEY, Context.MODE_PRIVATE).edit {
            putBoolean(LOGIN_COMPLETED_KEY, true)
        }
    }
}
