package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import maks.molch.dmitr.infinityfolderlauncher.dao.converter.converter
import maks.molch.dmitr.infinityfolderlauncher.data.AppWidgetItem
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.data.StepsWidget
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_ID
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_NAME
import java.util.UUID

class FolderDao(
    context: Context,
    private val backgroundStore: FolderBackgroundStore = FolderBackgroundStore(context),
) {
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

    fun createChildFolder(
        parentId: String,
        name: String,
        iconName: String?,
        backgroundName: String? = null,
        backgroundImages: List<String> = emptyList(),
        backgroundRotateSeconds: Int? = null,
        requireDistinctObjects: Boolean = true,
        deleteContentsOnRemove: Boolean = true,
    ): Folder {
        val child = Folder(
            id = UUID.randomUUID().toString(),
            name = name,
            iconName = iconName,
            backgroundName = backgroundName,
            backgroundImages = backgroundImages,
            backgroundRotateSeconds = backgroundRotateSeconds,
            requireDistinctObjects = requireDistinctObjects,
            deleteContentsOnRemove = deleteContentsOnRemove,
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
        val candidates = if (folder.requireDistinctObjects) {
            objects.filter { it.id !in existingIds }
        } else {
            objects.toList()
        }
        val toAdd = candidates.mapNotNull { obj ->
            when (obj) {
                is Folder -> {
                    if (wouldCreateCycle(obj.id, folderId)) null else obj.asReference()
                }
                is Application -> obj
                is WebsiteShortcut -> obj
                is AppWidgetItem -> obj
                is StepsWidget -> obj
            }
        }.let { list ->
            if (folder.requireDistinctObjects) {
                list.distinctBy { it.id }.filter { it.id !in existingIds }
            } else {
                list
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
        for (obj in objects) {
            if (obj is Folder) {
                val full = getById(obj.id) ?: obj
                if (full.deleteContentsOnRemove) {
                    deleteFolderTree(obj.id)
                }
            }
        }
        val folder = getOrCreate(folderId)
        saveQuiet(
            folder.copy(launcherObjects = folder.launcherObjects.filter { it.id !in ids })
        )
        bump()
    }

    fun renameFolder(folderId: String, newName: String): RenameResult =
        updateFolder(
            folderId = folderId,
            newName = newName,
            iconName = null,
            keepIcon = true,
            backgroundName = null,
            backgroundImages = emptyList(),
            backgroundRotateSeconds = null,
            keepBackground = true,
        )

    fun updateFolder(
        folderId: String,
        newName: String,
        iconName: String?,
        keepIcon: Boolean = false,
        backgroundName: String? = null,
        backgroundImages: List<String> = emptyList(),
        backgroundRotateSeconds: Int? = null,
        keepBackground: Boolean = false,
        requireDistinctObjects: Boolean? = null,
        deleteContentsOnRemove: Boolean? = null,
    ): RenameResult {
        if (folderId == MAIN_FOLDER_ID) return RenameResult.Forbidden
        val trimmed = newName.trim()
        if (trimmed.isBlank()) return RenameResult.Blank
        if (trimmed == MAIN_FOLDER_NAME) return RenameResult.Forbidden

        val folder = getById(folderId) ?: return RenameResult.NotFound
        val nextIcon = if (keepIcon) folder.iconName else iconName
        val nextBackground = if (keepBackground) folder.backgroundName else backgroundName
        val nextBackgroundImages =
            if (keepBackground) folder.backgroundImages else backgroundImages
        val nextRotate =
            if (keepBackground) folder.backgroundRotateSeconds else backgroundRotateSeconds
        val nextDistinct = requireDistinctObjects ?: folder.requireDistinctObjects
        val nextDeleteContents = deleteContentsOnRemove ?: folder.deleteContentsOnRemove
        val nameUnchanged = folder.name == trimmed
        val iconUnchanged = folder.iconName == nextIcon
        val backgroundUnchanged =
            folder.backgroundName == nextBackground &&
                folder.backgroundImages == nextBackgroundImages &&
                folder.backgroundRotateSeconds == nextRotate
        val flagsUnchanged =
            folder.requireDistinctObjects == nextDistinct &&
                folder.deleteContentsOnRemove == nextDeleteContents
        if (nameUnchanged && iconUnchanged && backgroundUnchanged && flagsUnchanged) {
            return RenameResult.Ok
        }

        if (!nameUnchanged) {
            val siblingConflict = getAll().any { parent ->
                parent.launcherObjects.any { it.id == folderId } &&
                    parent.launcherObjects.any { child ->
                        child is Folder && child.id != folderId && child.name == trimmed
                    }
            }
            if (siblingConflict) return RenameResult.NameTaken
        }

        val oldImages = folder.backgroundImages
        saveQuiet(
            folder.copy(
                name = trimmed,
                iconName = nextIcon,
                backgroundName = nextBackground,
                backgroundImages = nextBackgroundImages,
                backgroundRotateSeconds = nextRotate,
                requireDistinctObjects = nextDistinct,
                deleteContentsOnRemove = nextDeleteContents,
            ),
        )
        for (parent in getAll()) {
            var changed = false
            val updated = parent.launcherObjects.map { child ->
                if (child is Folder && child.id == folderId) {
                    changed = true
                    child.copy(
                        name = trimmed,
                        iconName = nextIcon,
                        backgroundName = nextBackground,
                        backgroundImages = nextBackgroundImages,
                        backgroundRotateSeconds = nextRotate,
                        requireDistinctObjects = nextDistinct,
                        deleteContentsOnRemove = nextDeleteContents,
                    )
                } else {
                    child
                }
            }
            if (changed) {
                saveQuiet(parent.copy(launcherObjects = updated))
            }
        }
        backgroundStore.deleteAll(oldImages.filter { it !in nextBackgroundImages })
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
        moveObjectToIndex(folderId, index, newIndex)
    }

    fun moveObjectToIndex(folderId: String, fromIndex: Int, toIndex: Int) {
        val folder = getOrCreate(folderId)
        val list = folder.launcherObjects.toMutableList()
        if (fromIndex !in list.indices || fromIndex == toIndex) {
            return
        }
        val item = list.removeAt(fromIndex)
        // [toIndex] = desired index in the list *after* removal.
        list.add(toIndex.coerceIn(0, list.size), item)
        save(folder.copy(launcherObjects = list))
    }

    fun setLauncherObjects(folderId: String, objects: List<LauncherObject>) {
        val folder = getOrCreate(folderId)
        if (folder.launcherObjects == objects) return
        save(folder.copy(launcherObjects = objects))
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

    private fun deleteFolderTree(folderId: String) {
        val ids = collectDescendantFolderIds(folderId)
        for (id in ids) {
            if (id == MAIN_FOLDER_ID) continue
            backgroundStore.deleteAll(getById(id)?.backgroundImages.orEmpty())
        }
        for (parent in getAll()) {
            val filtered = parent.launcherObjects.filter { it.id !in ids }
            if (filtered.size != parent.launcherObjects.size) {
                saveQuiet(parent.copy(launcherObjects = filtered))
            }
        }
        folderPrefs.edit {
            for (id in ids) {
                if (id != MAIN_FOLDER_ID) remove(id)
            }
        }
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
                            backgroundName = child.backgroundName,
                            backgroundImages = child.backgroundImages,
                            backgroundRotateSeconds = child.backgroundRotateSeconds,
                        )
                    }

                    is Application -> Application(
                        id = child.packageName,
                        name = child.name,
                        packageName = child.packageName,
                    )

                    is WebsiteShortcut -> child
                    is AppWidgetItem -> child
                    is StepsWidget -> child
                }
            }
            migrated[id] = Folder(
                id = id,
                name = if (id == MAIN_FOLDER_ID) MAIN_FOLDER_NAME else folder.name,
                launcherObjects = children,
                iconName = folder.iconName,
                backgroundName = folder.backgroundName,
                backgroundImages = folder.backgroundImages,
                backgroundRotateSeconds = folder.backgroundRotateSeconds,
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
