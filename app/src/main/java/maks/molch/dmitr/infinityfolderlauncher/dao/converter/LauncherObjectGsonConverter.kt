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
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import java.lang.reflect.Type
import java.util.UUID

val converter: Gson = GsonBuilder()
    .registerTypeAdapter(LauncherObject::class.java, LauncherObjectTypeAdapter())
    .registerTypeAdapter(Folder::class.java, FolderTypeAdapter())
    .registerTypeAdapter(Application::class.java, ApplicationTypeAdapter())
    .create()

class LauncherObjectTypeAdapter : JsonSerializer<LauncherObject>, JsonDeserializer<LauncherObject> {
    override fun serialize(
        launcherObject: LauncherObject,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = when (launcherObject) {
        is Application -> context.serialize(launcherObject, Application::class.java)
        is Folder -> context.serialize(launcherObject, Folder::class.java)
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

        val jsonArray = json.getAsJsonArray("launcherObjects") ?: JsonArray()
        val launcherObjects = ArrayList<LauncherObject>(jsonArray.size())
        for (jsonObj in jsonArray) {
            launcherObjects.add(context.deserialize(jsonObj, LauncherObject::class.java))
        }
        return Folder(id = id, name = name, launcherObjects = launcherObjects, iconName = iconName)
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
        return Application(id = id, name = name, packageName = packageName)
    }
}
