package maks.molch.dmitr.infinityfolderlauncher.dao.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import maks.molch.dmitr.infinityfolderlauncher.data.AppWidgetItem
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.data.StepsWidget
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
import java.lang.reflect.Type
import java.util.UUID

val converter: Gson = GsonBuilder()
    .registerTypeAdapter(LauncherObject::class.java, LauncherObjectTypeAdapter())
    .registerTypeAdapter(Folder::class.java, FolderTypeAdapter())
    .registerTypeAdapter(Application::class.java, ApplicationTypeAdapter())
    .registerTypeAdapter(WebsiteShortcut::class.java, WebsiteShortcutTypeAdapter())
    .registerTypeAdapter(AppWidgetItem::class.java, AppWidgetItemTypeAdapter())
    .registerTypeAdapter(StepsWidget::class.java, StepsWidgetTypeAdapter())
    .create()

class LauncherObjectTypeAdapter : JsonSerializer<LauncherObject>, JsonDeserializer<LauncherObject> {
    override fun serialize(
        launcherObject: LauncherObject,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = when (launcherObject) {
        is Application -> context.serialize(launcherObject, Application::class.java)
        is Folder -> context.serialize(launcherObject, Folder::class.java)
        is WebsiteShortcut -> context.serialize(launcherObject, WebsiteShortcut::class.java)
        is AppWidgetItem -> context.serialize(launcherObject, AppWidgetItem::class.java)
        is StepsWidget -> context.serialize(launcherObject, StepsWidget::class.java)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): LauncherObject {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        return when (json.getAsJsonPrimitive("type").asString) {
            Application::class.java.canonicalName ->
                context.deserialize(json, Application::class.java)

            Folder::class.java.canonicalName ->
                context.deserialize(json, Folder::class.java)

            WebsiteShortcut::class.java.canonicalName,
            "website",
            -> context.deserialize(json, WebsiteShortcut::class.java)

            AppWidgetItem::class.java.canonicalName,
            "appwidget",
            -> context.deserialize(json, AppWidgetItem::class.java)

            StepsWidget::class.java.canonicalName,
            "steps",
            -> context.deserialize(json, StepsWidget::class.java)

            else -> throw JsonParseException("Unsupported type")
        }
    }
}

class FolderTypeAdapter : JsonSerializer<Folder>, JsonDeserializer<Folder> {
    override fun serialize(
        folder: Folder,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        val jsonObject = JsonObject().apply {
            addProperty("id", folder.id)
            addProperty("name", folder.name)
            folder.iconName?.let { addProperty("iconName", it) }
            folder.backgroundName?.let { addProperty("backgroundName", it) }
            if (folder.backgroundImages.isNotEmpty()) {
                val images = JsonArray(folder.backgroundImages.size)
                folder.backgroundImages.forEach { images.add(it) }
                add("backgroundImages", images)
            }
            folder.backgroundRotateSeconds?.let { addProperty("backgroundRotateSeconds", it) }
            addProperty("requireDistinctObjects", folder.requireDistinctObjects)
            addProperty("deleteContentsOnRemove", folder.deleteContentsOnRemove)
            addProperty("type", Folder::class.java.canonicalName)
        }

        val jsonArray = JsonArray(folder.launcherObjects.size).apply {
            for (launcherObject in folder.launcherObjects) {
                val serialized = if (launcherObject is Folder) {
                    context.serialize(launcherObject.asReference())
                } else {
                    context.serialize(launcherObject)
                }
                add(serialized)
            }
        }
        jsonObject.add("launcherObjects", jsonArray)
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): Folder {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        val name = json.getAsJsonPrimitive("name").asString
        val id = json.get("id")?.takeUnless { it.isJsonNull }?.asString
            ?: UUID.randomUUID().toString()
        val iconName = json.get("iconName")?.takeUnless { it.isJsonNull }?.asString
        val backgroundName = json.get("backgroundName")?.takeUnless { it.isJsonNull }?.asString
        val backgroundImages = buildList {
            json.getAsJsonArray("backgroundImages")?.forEach { el ->
                el.takeUnless { it.isJsonNull }?.asString?.takeIf { it.isNotBlank() }?.let(::add)
            }
            // Legacy single-image field.
            if (isEmpty()) {
                json.get("backgroundImage")?.takeUnless { it.isJsonNull }?.asString
                    ?.takeIf { it.isNotBlank() }
                    ?.let(::add)
            }
        }
        val backgroundRotateSeconds = json.get("backgroundRotateSeconds")
            ?.takeUnless { it.isJsonNull }?.asInt
        val requireDistinct = json.get("requireDistinctObjects")
            ?.takeUnless { it.isJsonNull }?.asBoolean ?: true
        val deleteContents = json.get("deleteContentsOnRemove")
            ?.takeUnless { it.isJsonNull }?.asBoolean ?: true

        val jsonArray = json.getAsJsonArray("launcherObjects") ?: JsonArray()
        val launcherObjects = ArrayList<LauncherObject>(jsonArray.size())
        for (jsonObj in jsonArray) {
            launcherObjects.add(context.deserialize(jsonObj, LauncherObject::class.java))
        }
        return Folder(
            id = id,
            name = name,
            launcherObjects = launcherObjects,
            iconName = iconName,
            backgroundName = backgroundName,
            backgroundImages = backgroundImages,
            backgroundRotateSeconds = backgroundRotateSeconds,
            requireDistinctObjects = requireDistinct,
            deleteContentsOnRemove = deleteContents,
        )
    }
}

class ApplicationTypeAdapter : JsonSerializer<Application>, JsonDeserializer<Application> {
    override fun serialize(
        application: Application,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = JsonObject().apply {
        addProperty("id", application.id)
        addProperty("name", application.name)
        addProperty("type", Application::class.java.canonicalName)
        addProperty("package_name", application.packageName)
        if (application.activityName != null) {
            addProperty("activity_name", application.activityName)
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): Application {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        val name = json.getAsJsonPrimitive("name").asString
        val packageName = json.getAsJsonPrimitive("package_name").asString
        val id = json.get("id")?.takeUnless { it.isJsonNull }?.asString ?: packageName
        val activityName = json.get("activity_name")?.takeUnless { it.isJsonNull }?.asString
        return Application(
            id = id,
            name = name,
            packageName = packageName,
            activityName = activityName,
        )
    }
}

class WebsiteShortcutTypeAdapter : JsonSerializer<WebsiteShortcut>, JsonDeserializer<WebsiteShortcut> {
    override fun serialize(
        shortcut: WebsiteShortcut,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = JsonObject().apply {
        addProperty("id", shortcut.id)
        addProperty("name", shortcut.name)
        addProperty("url", shortcut.url)
        addProperty("type", WebsiteShortcut::class.java.canonicalName)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): WebsiteShortcut {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        val name = json.getAsJsonPrimitive("name").asString
        val url = json.getAsJsonPrimitive("url").asString
        val id = json.get("id")?.takeUnless { it.isJsonNull }?.asString
            ?: UUID.randomUUID().toString()
        return WebsiteShortcut(id = id, name = name, url = url)
    }
}

class AppWidgetItemTypeAdapter : JsonSerializer<AppWidgetItem>, JsonDeserializer<AppWidgetItem> {
    override fun serialize(
        item: AppWidgetItem,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = JsonObject().apply {
        addProperty("id", item.id)
        addProperty("name", item.name)
        addProperty("appWidgetId", item.appWidgetId)
        addProperty("provider", item.provider)
        addProperty("spanCols", item.spanCols)
        addProperty("spanRows", item.spanRows)
        addProperty("type", AppWidgetItem::class.java.canonicalName)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): AppWidgetItem {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        val name = json.getAsJsonPrimitive("name").asString
        val appWidgetId = json.getAsJsonPrimitive("appWidgetId").asInt
        val provider = json.getAsJsonPrimitive("provider").asString
        val id = json.get("id")?.takeUnless { it.isJsonNull }?.asString
            ?: UUID.randomUUID().toString()
        val spanCols = json.get("spanCols")?.takeUnless { it.isJsonNull }?.asInt ?: 2
        val spanRows = json.get("spanRows")?.takeUnless { it.isJsonNull }?.asInt ?: 2
        return AppWidgetItem(
            id = id,
            name = name,
            appWidgetId = appWidgetId,
            provider = provider,
            spanCols = spanCols,
            spanRows = spanRows,
        )
    }
}

class StepsWidgetTypeAdapter : JsonSerializer<StepsWidget>, JsonDeserializer<StepsWidget> {
    override fun serialize(
        item: StepsWidget,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = JsonObject().apply {
        addProperty("id", item.id)
        addProperty("name", item.name)
        addProperty("dailyGoal", item.dailyGoal)
        addProperty("spanCols", item.spanCols)
        addProperty("spanRows", item.spanRows)
        addProperty("type", StepsWidget::class.java.canonicalName)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): StepsWidget {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        val name = json.get("name")?.takeUnless { it.isJsonNull }?.asString ?: "Шаги"
        val id = json.get("id")?.takeUnless { it.isJsonNull }?.asString
            ?: UUID.randomUUID().toString()
        val dailyGoal = json.get("dailyGoal")?.takeUnless { it.isJsonNull }?.asInt ?: 10_000
        val spanCols = json.get("spanCols")?.takeUnless { it.isJsonNull }?.asInt ?: 2
        val spanRows = json.get("spanRows")?.takeUnless { it.isJsonNull }?.asInt ?: 1
        return StepsWidget(
            id = id,
            name = name,
            dailyGoal = dailyGoal,
            spanCols = spanCols,
            spanRows = spanRows,
        )
    }
}
