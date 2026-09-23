package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import maks.molch.dmitr.infinityfolderlauncher.dao.converter.converter
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_ID
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_NAME
import java.util.UUID

class FolderDao(context: Context) {
    private val folderPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FOLDERS, Context.MODE_PRIVATE)
    private val metaPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_META, Context.MODE_PRIVATE)

    private val _epoch = MutableStateFlow(0)
    val epoch: StateFlow<Int> = _epoch.asStateFlow()

    init {
        migrateIfNeeded()
        ensureRoot()
    }

    fun getById(folderId: String): Folder? {
        val json = folderPrefs.getString(folderId, null) ?: return null
        return converter.fromJson(json, Folder::class.java)
    }

    fun getOrCreate(folderId: String): Folder =
        getById(folderId) ?: if (folderId == MAIN_FOLDER_ID) {
            save(Folder(id = MAIN_FOLDER_ID, name = MAIN_FOLDER_NAME))
        } else {
            error("Unknown folder id: $folderId")
        }

    fun getAll(): List<Folder> =
        folderPrefs.all.mapNotNull { (_, value) ->
            (value as? String)?.let { converter.fromJson(it, Folder::class.java) }
        }

    fun getAllByQuery(query: String): List<Folder> {
        val q = query.lowercase()
        return getAll().filter { it.name.lowercase().contains(q) }
    }

    fun save(folder: Folder): Folder {
        folderPrefs.edit {
            putString(folder.id, converter.toJson(folder))
        }
        bump()
        return folder
    }

    fun createChildFolder(parentId: String, name: String, iconName: String?): Folder {
        val child = Folder(
            id = UUID.randomUUID().toString(),
            name = name,
            iconName = iconName,
        )
        saveQuiet(child)
        val parent = getOrCreate(parentId)
        saveQuiet(
            parent.copy(launcherObjects = parent.launcherObjects + child.asReference())
        )
        bump()
        return child
    }

    fun addObjectsAndSave(folderId: String, objects: Set<LauncherObject>) {
        val folder = getOrCreate(folderId)
        val existingIds = folder.launcherObjects.map { it.id }.toSet()
        val toAdd = objects.filter { it.id !in existingIds }.map { obj ->
            when (obj) {
                is Folder -> obj.asReference()
                is Application -> obj
            }
        }
        if (toAdd.isEmpty()) return
        for (obj in toAdd) {
            if (obj is Folder && getById(obj.id) == null) {
                saveQuiet(obj)
            }
        }
        saveQuiet(folder.copy(launcherObjects = folder.launcherObjects + toAdd))
        bump()
    }

    fun removeObjectsAndSave(folderId: String, objects: Set<LauncherObject>) {
        val ids = objects.map { it.id }.toSet()
        val folder = getOrCreate(folderId)
        saveQuiet(
            folder.copy(launcherObjects = folder.launcherObjects.filter { it.id !in ids })
        )
        bump()
    }

    private fun saveQuiet(folder: Folder) {
        folderPrefs.edit {
            putString(folder.id, converter.toJson(folder))
        }
    }

    private fun bump() {
        _epoch.value = _epoch.value + 1
    }

    private fun ensureRoot() {
        if (getById(MAIN_FOLDER_ID) == null) {
            saveQuiet(Folder(id = MAIN_FOLDER_ID, name = MAIN_FOLDER_NAME))
            bump()
        }
    }

    private fun migrateIfNeeded() {
        val version = metaPrefs.getInt(KEY_SCHEMA_VERSION, 0)
        if (version >= SCHEMA_VERSION) return

        val raw = folderPrefs.all
        if (raw.isEmpty()) {
            metaPrefs.edit { putInt(KEY_SCHEMA_VERSION, SCHEMA_VERSION) }
            return
        }

        val looksLikeV1 = raw.keys.all { key ->
            key == MAIN_FOLDER_ID || key.length >= 32
        } && raw.values.any { value ->
            (value as? String)?.contains("\"id\"") == true
        }

        if (version == 0 && looksLikeV1) {
            metaPrefs.edit { putInt(KEY_SCHEMA_VERSION, SCHEMA_VERSION) }
            return
        }

        val parsedByName = linkedMapOf<String, Folder>()
        for ((key, value) in raw) {
            val json = value as? String ?: continue
            val folder = runCatching {
                converter.fromJson(json, Folder::class.java)
            }.getOrNull() ?: continue
            parsedByName[key] = folder
        }

        val nameToId = linkedMapOf<String, String>()
        nameToId[MAIN_FOLDER_NAME] = MAIN_FOLDER_ID
        for ((key, folder) in parsedByName) {
            if (key == MAIN_FOLDER_NAME || folder.name == MAIN_FOLDER_NAME) {
                nameToId[folder.name] = MAIN_FOLDER_ID
                nameToId[key] = MAIN_FOLDER_ID
            } else {
                val id = folder.id.takeIf { it.isNotBlank() && it != key }
                    ?: UUID.randomUUID().toString()
                nameToId.putIfAbsent(folder.name, id)
                nameToId[key] = nameToId[folder.name]!!
            }
        }

        val migrated = linkedMapOf<String, Folder>()
        for ((key, folder) in parsedByName) {
            val id = nameToId[key] ?: nameToId[folder.name] ?: UUID.randomUUID().toString()
            val children = folder.launcherObjects.map { child ->
                when (child) {
                    is Folder -> {
                        val childId = nameToId[child.name]
                            ?: child.id.takeIf { it.isNotBlank() }
                            ?: UUID.randomUUID().toString()
                        nameToId.putIfAbsent(child.name, childId)
                        Folder(
                            id = childId,
                            name = child.name,
                            iconName = child.iconName,
                        )
                    }

                    is Application -> Application(
                        id = child.packageName,
                        name = child.name,
                        packageName = child.packageName,
                    )
                }
            }
            migrated[id] = Folder(
                id = id,
                name = if (id == MAIN_FOLDER_ID) MAIN_FOLDER_NAME else folder.name,
                launcherObjects = children,
                iconName = folder.iconName,
            )
        }

        if (MAIN_FOLDER_ID !in migrated) {
            migrated[MAIN_FOLDER_ID] = Folder(id = MAIN_FOLDER_ID, name = MAIN_FOLDER_NAME)
        }

        folderPrefs.edit {
            clear()
            for ((id, folder) in migrated) {
                putString(id, converter.toJson(folder))
            }
        }
        metaPrefs.edit { putInt(KEY_SCHEMA_VERSION, SCHEMA_VERSION) }
    }

    companion object {
        const val SCHEMA_VERSION = 1
        private const val PREFS_FOLDERS = "Folders"
        private const val PREFS_META = "FolderMeta"
        private const val KEY_SCHEMA_VERSION = "schema_version"
    }
}
