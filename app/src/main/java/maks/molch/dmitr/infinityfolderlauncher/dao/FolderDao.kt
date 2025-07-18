package maks.molch.dmitr.infinityfolderlauncher.dao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import maks.molch.dmitr.infinityfolderlauncher.converter.converter
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject

class FolderDao(context: Context) {
    private val folderSharedPreferences: SharedPreferences =
        context.getSharedPreferences("Folders", Context.MODE_PRIVATE)

    fun saveFolder(folder: Folder): Folder {
        folderSharedPreferences.edit {
            putString(folder.name, converter.toJson(folder))
        }
        return folder
    }

    fun getFolderOrSaveByName(folderName: String): Folder =
        getFolderByName(folderName) ?: saveFolder(Folder(folderName))

    fun getAllFolders(): List<Folder> {
        return folderSharedPreferences.all.map { folderNameWithJson ->
            converter.fromJson(folderNameWithJson.value as String, Folder::class.java)
        }
    }

    fun getFoldersByQuery(query: String): List<Folder> {
        return folderSharedPreferences.all
            .filter { it.key.lowercase().matches(Regex(".*${query.lowercase()}.*")) }
            .map { folderNameWithJson ->
                converter.fromJson(folderNameWithJson.value as String, Folder::class.java)
            }
    }

    fun removeObjectsAndSave(folderName: String, objects: Set<LauncherObject>) {
        val objectNames: Set<String> = objects.map { it.name }.toSet()
        val folder: Folder = getFolderOrSaveByName(folderName)
        val currentObjects: Set<LauncherObject> = folder.launcherObjects.toSet()
        val updatedObjects: List<LauncherObject> = currentObjects.filter { it.name !in objectNames }
        saveFolder(folder.copy(launcherObjects = updatedObjects))
    }

    fun addObjectsAndSave(folderName: String, objects: Set<LauncherObject>) {
        val folder: Folder = getFolderOrSaveByName(folderName)
        val currentObjects: Set<LauncherObject> = folder.launcherObjects.toSet()
        val updatedObjects: Set<LauncherObject> = currentObjects + objects
        saveFolder(folder.copy(launcherObjects = updatedObjects.toList()))
    }

    private fun getFolderByName(folderName: String): Folder? {
        return converter.fromJson(
            folderSharedPreferences.getString(folderName, null) ?: return null,
            Folder::class.java
        )
    }
}

fun Activity.getFolderName(): String =
    intent.getStringExtra("FOLDER_NAME_INTENT_KEY") ?: "MAIN_FOLDER"

fun Intent.putFolderName(folderName: String) =
    putExtra("FOLDER_NAME_INTENT_KEY", folderName)