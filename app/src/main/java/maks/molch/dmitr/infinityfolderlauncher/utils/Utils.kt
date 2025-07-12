package maks.molch.dmitr.infinityfolderlauncher.utils

import android.content.Context
import android.widget.Toast

fun Context.toastMakeTextAndShow(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}