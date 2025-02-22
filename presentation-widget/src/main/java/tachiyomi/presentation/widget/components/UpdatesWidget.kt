package tachiyomi.presentation.widget.components

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import tachiyomi.core.Constants
import tachiyomi.presentation.widget.ContainerModifier
import tachiyomi.presentation.widget.R
import tachiyomi.presentation.widget.util.calculateRowAndColumnCount
import tachiyomi.presentation.widget.util.stringResource

@Composable
fun UpdatesWidget(data: List<Pair<Long, Bitmap?>>?) {
    val (rowCount, columnCount) = LocalSize.current.calculateRowAndColumnCount()
    Column(
        modifier = ContainerModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (data == null) {
            CircularProgressIndicator()
        } else if (data.isEmpty()) {
            Text(text = stringResource(R.string.information_no_recent))
        } else {
            (0..<rowCount).forEach { i ->
                val coverRow = (0..<columnCount).mapNotNull { j ->
                    data.getOrNull(j + (i * columnCount))
                }
                if (coverRow.isNotEmpty()) {
                    Row(
                        modifier = GlanceModifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        coverRow.forEach { (mangaId, cover) ->
                            Box(
                                modifier = GlanceModifier
                                    .padding(horizontal = 3.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                val intent = Intent(LocalContext.current, Class.forName(Constants.MAIN_ACTIVITY)).apply {
                                    action = Constants.SHORTCUT_MANGA
                                    putExtra(Constants.MANGA_EXTRA, mangaId)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                                    // https://issuetracker.google.com/issues/238793260
                                    addCategory(mangaId.toString())
                                }
                                UpdatesMangaCover(
                                    modifier = GlanceModifier.clickable(actionStartActivity(intent)),
                                    cover = cover,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
