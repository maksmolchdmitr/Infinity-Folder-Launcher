package maks.molch.dmitr.infinityfolderlauncher.ui.theme

import maks.molch.dmitr.infinityfolderlauncher.dao.FolderBackgroundStore

/** Resolved background for an open folder screen. */
sealed interface ResolvedFolderBackground {
    data class Preset(val id: String) : ResolvedFolderBackground
    data class Slideshow(
        val absolutePaths: List<String>,
        val rotateSeconds: Int,
    ) : ResolvedFolderBackground
}

fun resolveFolderBackground(
    folderPreset: String?,
    folderImages: List<String>,
    folderRotateSeconds: Int?,
    defaultPreset: String,
    defaultImages: List<String>,
    defaultRotateSeconds: Int,
    imageAbsolutePath: (String?) -> String?,
): ResolvedFolderBackground {
    resolveImages(folderImages, folderRotateSeconds, defaultRotateSeconds, imageAbsolutePath)
        ?.let { return it }
    folderPreset?.let {
        return ResolvedFolderBackground.Preset(FolderBackgrounds.normalizeId(it))
    }
    resolveImages(defaultImages, null, defaultRotateSeconds, imageAbsolutePath)
        ?.let { return it }
    return ResolvedFolderBackground.Preset(FolderBackgrounds.normalizeId(defaultPreset))
}

private fun resolveImages(
    fileNames: List<String>,
    rotateSeconds: Int?,
    defaultRotateSeconds: Int,
    imageAbsolutePath: (String?) -> String?,
): ResolvedFolderBackground.Slideshow? {
    val paths = fileNames.mapNotNull { imageAbsolutePath(it) }
    if (paths.isEmpty()) return null
    val seconds = (rotateSeconds ?: defaultRotateSeconds).coerceIn(
        FolderBackgroundStore.ROTATE_OPTIONS_SECONDS.first(),
        FolderBackgroundStore.ROTATE_OPTIONS_SECONDS.last(),
    )
    return ResolvedFolderBackground.Slideshow(
        absolutePaths = paths,
        rotateSeconds = seconds,
    )
}
