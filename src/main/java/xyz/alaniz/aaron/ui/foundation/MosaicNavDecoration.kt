package xyz.alaniz.aaron.ui.foundation

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Box
import com.slack.circuit.backstack.NavArgument
import com.slack.circuit.backstack.NavDecoration
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class MosaicNavDecoration : NavDecoration {
    @Composable
    override fun <T : NavArgument> DecoratedContent(
        args: List<T>,
        modifier: androidx.compose.ui.Modifier,
        content: @Composable (T) -> Unit,
    ) {
        Box(modifier = Modifier) { content(args.first()) }
    }
}