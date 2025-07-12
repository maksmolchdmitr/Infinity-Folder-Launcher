package maks.molch.dmitr.infinityfolderlauncher.converter

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

val converter: Gson = GsonBuilder()
    .registerTypeAdapter(LauncherObject::class.java, LauncherObjectTypeAdapter())
    .registerTypeAdapter(Folder::class.java, FolderTypeAdapter())
    .registerTypeAdapter(Application::class.java, ApplicationTypeAdapter())
    .create()

class LauncherObjectTypeAdapter : JsonSerializer<LauncherObject>, JsonDeserializer<LauncherObject> {
    override fun serialize(
        launcherObject: LauncherObject,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (launcherObject) {
            is Application -> context.serialize(launcherObject, Application::class.java)
            is Folder -> context.serialize(launcherObject, Folder::class.java)
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LauncherObject {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        return when (json.getAsJsonPrimitive("type").asString) {
            Application::class.java.canonicalName ->
                context.deserialize<Application>(json, Application::class.java)

            Folder::class.java.canonicalName ->
                context.deserialize<Folder>(json, Folder::class.java)

            else -> throw JsonParseException("Unsupported type")
        }
    }
}

class FolderTypeAdapter : JsonSerializer<Folder>, JsonDeserializer<Folder> {
    override fun serialize(
        folder: Folder,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject().apply {
            addProperty("name", folder.name)
            addProperty("type", Folder::class.java.canonicalName)
        }

        val launcherObjects = folder.launcherObjects
        val jsonArray = JsonArray(launcherObjects.size).apply {
            for (launcherObject in launcherObjects) {
                val serializedObject = if (launcherObject is Folder) {
                    // Create a shallow copy of the folder without its children
                    // to avoid overly deep recursion or circular dependencies.
                    context.serialize(launcherObject.copy(launcherObjects = emptyList()))
                } else {
                    context.serialize(launcherObject)
                }
                add(serializedObject)
            }
        }
        jsonObject.add("launcherObjects", jsonArray)
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Folder {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        val name = json.getAsJsonPrimitive("name").asString

        val jsonArray = json.getAsJsonArray("launcherObjects")
        val launcherObjects = ArrayList<LauncherObject>(jsonArray.size())
        for (jsonObj in jsonArray) {
            launcherObjects.add(context.deserialize(jsonObj, LauncherObject::class.java))
        }
        return Folder(name, launcherObjects)
    }
}

class ApplicationTypeAdapter : JsonSerializer<Application>, JsonDeserializer<Application> {
    override fun serialize(
        application: Application,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", application.name)
        jsonObject.addProperty("type", Application::class.java.canonicalName)
        jsonObject.addProperty("package_name", application.packageName)
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Application {
        if (json !is JsonObject) throw JsonParseException("Unsupported type")
        val name = json.getAsJsonPrimitive("name").asString
        val packageName = json.getAsJsonPrimitive("package_name").asString
        return Application(name, packageName)
    }
}