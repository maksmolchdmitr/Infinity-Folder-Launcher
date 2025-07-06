package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val objectNumberOnTheRow = 4

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Green50),
        contentAlignment = Alignment.Center,
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(16.dp),
            columns = GridCells.Fixed(objectNumberOnTheRow),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            items(100) { index ->
                ObjectCell(index)
            }
        }
    }

}

@Composable
fun ObjectCell(index: Int) {
    Box(
        modifier = Modifier
            .shadow(
                shape = RoundedCornerShape(16.dp),
                elevation = 16.dp,
            )
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.clickable {
                println("Press from $index!")
            },
            painter = painterResource(R.drawable.infinity_folder_logo),
            contentDescription = null,
        )
        Text("$index")
    }
}
