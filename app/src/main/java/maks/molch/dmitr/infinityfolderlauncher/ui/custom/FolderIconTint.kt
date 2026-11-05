package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path

val FolderIconPlateDefault = Color(0xFF393939)
val FolderIconMarkDefault = Color(0xFF1C9961)

val FolderIconPlates = listOf(
    FolderIconPlateDefault,
    Color(0xFF1C1C1C),
    Color(0xFFFFFFFF),
    Color(0xFF163A4A),
    Color(0xFF3A2A22),
    Color(0xFF2C2140),
    Color(0xFF1C3D32),
    Color(0xFF4A2C2A),
    Color(0xFF243044),
)

val FolderIconMarks = listOf(
    FolderIconMarkDefault,
    Color(0xFFFFFFFF),
    Color(0xFF161616),
    Color(0xFFF4C430),
    Color(0xFFE85D4C),
    Color(0xFF3D8BFF),
    Color(0xFFB06BFF),
    Color(0xFFFF8A3D),
    Color(0xFF2EC4B6),
)

fun Icons.recolorableFolderIcon(name: String): Boolean =
    name != Icons.FOLDER_ICON_DEFAULT && name != Icons.FOLDER_ICON_APPS_PREVIEW &&
        folderIconByName(name) != null

fun Icons.coloredFolderIcon(
    name: String,
    plateArgb: Int?,
    markArgb: Int?,
): ImageVector? {
    val source = folderIconByName(name) ?: return null
    if (!recolorableFolderIcon(name)) return source
    val plate = plateArgb?.let { Color(it) } ?: FolderIconPlateDefault
    val mark = markArgb?.let { Color(it) } ?: FolderIconMarkDefault
    if (plate == FolderIconPlateDefault && mark == FolderIconMarkDefault) return source
    return source.withFolderColors(plate, mark)
}

fun storedIconColor(color: Color, default: Color): Int? =
    if (color == default) null else color.toArgb()

private fun ImageVector.withFolderColors(plate: Color, mark: Color): ImageVector {
    val builder = ImageVector.Builder(
        name = name,
        defaultWidth = defaultWidth,
        defaultHeight = defaultHeight,
        viewportWidth = viewportWidth,
        viewportHeight = viewportHeight,
        tintColor = tintColor,
        autoMirror = autoMirror,
    )
    builder.addNodes(root, plate, mark)
    return builder.build()
}

private fun ImageVector.Builder.addNodes(group: VectorGroup, plate: Color, mark: Color) {
    group(
        name = group.name,
        rotate = group.rotation,
        pivotX = group.pivotX,
        pivotY = group.pivotY,
        scaleX = group.scaleX,
        scaleY = group.scaleY,
        translationX = group.translationX,
        translationY = group.translationY,
        clipPathData = group.clipPathData,
    ) {
        for (index in 0 until group.size) {
            when (val node = group[index]) {
                is VectorGroup -> addNodes(node, plate, mark)
                is VectorPath -> addPath(node, plate, mark)
            }
        }
    }
}

private fun ImageVector.Builder.addPath(node: VectorPath, plate: Color, mark: Color) {
    val fill = (node.fill as? SolidColor)?.value
    val nextFill = when (fill?.toArgb()) {
        FolderIconPlateDefault.toArgb() -> SolidColor(plate)
        FolderIconMarkDefault.toArgb() -> SolidColor(mark)
        else -> node.fill
    }
    path(
        fill = nextFill,
        fillAlpha = node.fillAlpha,
        stroke = node.stroke,
        strokeAlpha = node.strokeAlpha,
        strokeLineWidth = node.strokeLineWidth,
        strokeLineCap = node.strokeLineCap,
        strokeLineJoin = node.strokeLineJoin,
        strokeLineMiter = node.strokeLineMiter,
        pathFillType = node.pathFillType,
    ) {
        replay(node.pathData)
    }
}

private fun PathBuilder.replay(nodes: List<PathNode>) {
    for (node in nodes) {
        when (node) {
            is PathNode.Close -> close()
            is PathNode.MoveTo -> moveTo(node.x, node.y)
            is PathNode.RelativeMoveTo -> moveToRelative(node.dx, node.dy)
            is PathNode.LineTo -> lineTo(node.x, node.y)
            is PathNode.RelativeLineTo -> lineToRelative(node.dx, node.dy)
            is PathNode.HorizontalTo -> horizontalLineTo(node.x)
            is PathNode.RelativeHorizontalTo -> horizontalLineToRelative(node.dx)
            is PathNode.VerticalTo -> verticalLineTo(node.y)
            is PathNode.RelativeVerticalTo -> verticalLineToRelative(node.dy)
            is PathNode.CurveTo -> curveTo(node.x1, node.y1, node.x2, node.y2, node.x3, node.y3)
            is PathNode.RelativeCurveTo -> curveToRelative(
                node.dx1, node.dy1, node.dx2, node.dy2, node.dx3, node.dy3,
            )
            is PathNode.ReflectiveCurveTo -> reflectiveCurveTo(node.x1, node.y1, node.x2, node.y2)
            is PathNode.RelativeReflectiveCurveTo -> reflectiveCurveToRelative(
                node.dx1, node.dy1, node.dx2, node.dy2,
            )
            is PathNode.QuadTo -> quadTo(node.x1, node.y1, node.x2, node.y2)
            is PathNode.RelativeQuadTo -> quadToRelative(node.dx1, node.dy1, node.dx2, node.dy2)
            is PathNode.ReflectiveQuadTo -> reflectiveQuadTo(node.x, node.y)
            is PathNode.RelativeReflectiveQuadTo -> reflectiveQuadToRelative(node.dx, node.dy)
            is PathNode.ArcTo -> arcTo(
                node.horizontalEllipseRadius,
                node.verticalEllipseRadius,
                node.theta,
                node.isMoreThanHalf,
                node.isPositiveArc,
                node.arcStartX,
                node.arcStartY,
            )
            is PathNode.RelativeArcTo -> arcToRelative(
                node.horizontalEllipseRadius,
                node.verticalEllipseRadius,
                node.theta,
                node.isMoreThanHalf,
                node.isPositiveArc,
                node.arcStartDx,
                node.arcStartDy,
            )
        }
    }
}
