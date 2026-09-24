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
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
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
        val toAdd = objects.filter { it.id !in existingIds }.mapNotNull { obj ->
            when (obj) {
                is Folder -> {
                    if (wouldCreateCycle(obj.id, folderId)) null else obj.asReference()
                }
                is Application -> obj
                is WebsiteShortcut -> obj
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

    fun renameFolder(folderId: String, newName: String): RenameResult {
        if (folderId == MAIN_FOLDER_ID) return RenameResult.Forbidden
        val trimmed = newName.trim()
        if (trimmed.isBlank()) return RenameResult.Blank
        if (trimmed == MAIN_FOLDER_NAME) return RenameResult.Forbidden

        val folder = getById(folderId) ?: return RenameResult.NotFound
        if (folder.name == trimmed) return RenameResult.Ok

        val siblingConflict = getAll().any { parent ->
            parent.launcherObjects.any { it.id == folderId } &&
                parent.launcherObjects.any { child ->
                    child is Folder && child.id != folderId && child.name == trimmed
                }
        }
        if (siblingConflict) return RenameResult.NameTaken

        saveQuiet(folder.copy(name = trimmed))
        for (parent in getAll()) {
            var changed = false
            val updated = parent.launcherObjects.map { child ->
                if (child is Folder && child.id == folderId) {
                    changed = true
                    child.copy(name = trimmed)
                } else {
                    child
                }
            }
            if (changed) {
                saveQuiet(parent.copy(launcherObjects = updated))
            }
        }
        bump()
        return RenameResult.Ok
    }

    fun moveObject(folderId: String, objectId: String, delta: Int) {
        val folder = getOrCreate(folderId)
        val list = folder.launcherObjects.toMutableList()
        val index = list.indexOfFirst { it.id == objectId }
        if (index < 0) return
        val newIndex = (index + delta).coerceIn(0, list.lastIndex)
        if (newIndex == index) return
        val item = list.removeAt(index)
        list.add(newIndex, item)
        save(folder.copy(launcherObjects = list))
    }

    fun wouldCreateCycle(movingFolderId: String, targetFolderId: String): Boolean {
        if (movingFolderId == targetFolderId) return true
        var current: String? = targetFolderId
        val visited = mutableSetOf<String>()
        while (current != null && current !in visited) {
            if (current == movingFolderId) return true
            visited += current
            current = findParentId(current)
        }
        return false
    }

    fun findParentId(folderId: String): String? =
        getAll().firstOrNull { parent ->
            parent.launcherObjects.any { it is Folder && it.id == folderId }
        }?.id

    fun collectDescendantFolderIds(folderId: String): Set<String> {
        val result = mutableSetOf<String>()
        val queue = ArrayDeque<String>()
        queue.add(folderId)
        while (queue.isNotEmpty()) {
            val id = queue.removeFirst()
            if (!result.add(id)) continue
            getById(id)?.launcherObjects?.forEach { child ->
                if (child is Folder) queue.add(child.id)
            }
        }
        return result
    }

    fun searchInSubtree(rootId: String, query: String): List<LauncherObject> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        val results = mutableListOf<LauncherObject>()
        val seen = mutableSetOf<String>()
        for (folderId in collectDescendantFolderIds(rootId)) {
            val folder = getById(folderId) ?: continue
            for (obj in folder.launcherObjects) {
                if (obj.name.lowercase().contains(q) && seen.add(obj.id)) {
                    results += when (obj) {
                        is Folder -> getById(obj.id) ?: obj
                        else -> obj
                    }
                }
            }
        }
        return results
    }

    enum class RenameResult {
        Ok,
        Blank,
        NameTaken,
        NotFound,
        Forbidden,
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

                    is WebsiteShortcut -> child
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
