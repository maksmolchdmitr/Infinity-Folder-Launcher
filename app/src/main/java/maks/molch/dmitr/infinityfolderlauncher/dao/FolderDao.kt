package maks.molch.dmitr.infinityfolderlauncher.dao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import maks.molch.dmitr.infinityfolderlauncher.converter.converter
import maks.molch.dmitr.infinityfolderlauncher.data.Folder

class FolderDao(context: Context) {
    private val folderSharedPreferences: SharedPreferences =
        context.getSharedPreferences("Folders", Context.MODE_PRIVATE)

    fun saveFolder(folder: Folder) {
        folderSharedPreferences.edit {
            putString(folder.name, converter.toJson(folder))
        }
    }

    fun getFolderByName(folderName: String): Folder? {
        return converter.fromJson(
            folderSharedPreferences.getString(folderName, null) ?: return null,
            Folder::class.java
        )
    }

    fun getAllFolders(): List<Folder> {
        return folderSharedPreferences.all.map { folderNameWithJson ->
            converter.fromJson(folderNameWithJson.value as String, Folder::class.java)
        }
    }
}

fun Activity.getFolderName(): String =
    intent.getStringExtra("FOLDER_NAME_INTENT_KEY") ?: "MAIN_FOLDER"

fun Intent.putFolderName(folderName: String) =
    putExtra("FOLDER_NAME_INTENT_KEY", folderName)