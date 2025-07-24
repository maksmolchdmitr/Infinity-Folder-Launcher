package maks.molch.dmitr.infinityfolderlauncher.dao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import maks.molch.dmitr.infinityfolderlauncher.dao.converter.converter
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_NAME

class FolderDao(context: Context) {
    private val folderSharedPreferences: SharedPreferences =
        context.getSharedPreferences("Folders", Context.MODE_PRIVATE)

    fun save(folder: Folder): Folder {
        folderSharedPreferences.edit {
            putString(folder.name, converter.toJson(folder))
        }
        return folder
    }

    fun put(folder: Folder): Folder {
        val actualFolder: Folder? = getByName(folder.name)
        return actualFolder?.let { actualFolder ->
            save(
                actualFolder.copy(
                    iconName = folder.iconName,
                )
            )
        } ?: run {
            save(folder)
        }
    }

    fun getOrSaveByName(folderName: String): Folder =
        getByName(folderName) ?: save(Folder(folderName))

    fun getAll(): List<Folder> {
        return folderSharedPreferences.all.map { folderNameWithJson ->
            converter.fromJson(folderNameWithJson.value as String, Folder::class.java)
        }
    }

    fun getAllByQuery(query: String): List<Folder> {
        return folderSharedPreferences.all
            .filter { it.key.lowercase().contains(query.lowercase()) }
            .map { folderNameWithJson ->
                converter.fromJson(folderNameWithJson.value as String, Folder::class.java)
            }
    }

    fun removeObjectsAndSave(folderName: String, objects: Set<LauncherObject>) {
        val objectNames: Set<String> = objects.map { it.name }.toSet()
        val folder: Folder = getOrSaveByName(folderName)
        val currentObjects: Set<LauncherObject> = folder.launcherObjects.toSet()
        val updatedObjects: List<LauncherObject> = currentObjects.filter { it.name !in objectNames }
        save(folder.copy(launcherObjects = updatedObjects))
    }

    fun addObjectsAndSave(folderName: String, objects: Set<LauncherObject>) {
        val folder: Folder = getOrSaveByName(folderName)
        val currentObjects: Set<LauncherObject> = folder.launcherObjects.toSet()
        val updatedObjects: Set<LauncherObject> = currentObjects + objects
        save(folder.copy(launcherObjects = updatedObjects.toList()))
    }

    private fun getByName(folderName: String): Folder? {
        return converter.fromJson(
            folderSharedPreferences.getString(folderName, null) ?: return null,
            Folder::class.java
        )
    }
}

fun Activity.getFolderName(): String =
    intent.getStringExtra("FOLDER_NAME_INTENT_KEY") ?: MAIN_FOLDER_NAME

fun Intent.putFolderName(folderName: String) =
    putExtra("FOLDER_NAME_INTENT_KEY", folderName)