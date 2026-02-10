package xyz.alaniz.aaron.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle
import com.jakewharton.mosaic.ui.UnderlineStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlin.system.exitProcess
import kotlinx.coroutines.delay
import xyz.alaniz.aaron.ui.foundation.BorderedTitledContent
import xyz.alaniz.aaron.ui.foundation.Footer
import xyz.alaniz.aaron.ui.foundation.FooterOption
import xyz.alaniz.aaron.ui.foundation.KeyEvents.CtrlC
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Enter
import xyz.alaniz.aaron.ui.foundation.KeyEvents.Esc
import xyz.alaniz.aaron.ui.foundation.KeyEvents.R
import xyz.alaniz.aaron.ui.foundation.KeyEvents.r
import xyz.alaniz.aaron.ui.foundation.TextWrapper

@Composable
@CircuitInject(screen = GameScreen::class, scope = AppScope::class)
fun GameScreenUi(state: GameState, modifier: androidx.compose.ui.Modifier) {
  if (state is GameState.State) {
    if (state.isError) {
      LaunchedEffect(state.isError) {
        delay(150)
        state.eventSink(GameEvent.ClearError)
      }
    }

    Column(
        modifier =
            Modifier.onKeyEvent { keyEvent ->
              when (keyEvent) {
                CtrlC -> {
                  exitProcess(0)
                }
                Enter -> {
                  if (state.status == GameStatus.GAME_OVER) {
                    state.eventSink(GameEvent.NewGame)
                  } else if (state.status == GameStatus.PLAYING) {
                    // Allow typing newlines for code snippets or multi-line chunks
                    state.eventSink(GameEvent.LetterTyped('\n'))
                  }
                  true
                }
                Esc -> {
                  state.eventSink(GameEvent.ReturnToMenu)
                  true
                }
                else -> {
                  if (state.status == GameStatus.PLAYING) {
                    val key = keyEvent.key
                    if (key.length == 1) {
                      state.eventSink(GameEvent.LetterTyped(key[0]))
                      true
                    } else {
                      false
                    }
                  } else if (state.status == GameStatus.GAME_OVER) {
                    if (keyEvent == r || keyEvent == R) {
                      state.eventSink(GameEvent.RetryGame)
                      true
                    } else {
                      false
                    }
                  } else {
                    false
                  }
                }
              }
            }) {
          if (state.status == GameStatus.COUNTDOWN) {
            Text(GET_READY_ART, color = Color.Green)
            Row {
              for (i in 1..5) {
                if (i <= state.countdownStage) {
                  Text(" ● ", color = Color.Red)
                } else {
                  Text(" ○ ", textStyle = TextStyle.Dim)
                }
              }
            }
            Spacer(Modifier.height(1))
            Footer(options = countdownFooterOptions)
          } else if (state.status == GameStatus.PLAYING) {
            BorderedTitledContent(title = "Stats", width = 82, height = 1) {
              Row {
                Text("WPM: ", textStyle = TextStyle.Dim)
                Text("${state.wpm.toInt()}  ", textStyle = TextStyle.Bold)

                Text("Accuracy: ", textStyle = TextStyle.Dim)
                Text("${state.accuracy.toInt()}%  ", textStyle = TextStyle.Bold)

                Text("Progress: ", textStyle = TextStyle.Dim)
                Text(
                    "${state.currentLineIndex + 1}/${state.passage.size}  ",
                    textStyle = TextStyle.Bold)

                val lineProgress =
                    if (state.currentWord.isNotEmpty()) {
                      state.userInput.length.toFloat() / state.currentWord.length
                    } else {
                      0f
                    }
                val totalProgress =
                    (state.currentLineIndex + lineProgress) / state.passage.size.coerceAtLeast(1)
                val barWidth = 20
                val filledCount = (totalProgress * barWidth).toInt().coerceIn(0, barWidth)
                val emptyCount = barWidth - filledCount

                Text("▕", textStyle = TextStyle.Dim)
                Text(getFilledBar(filledCount), color = Color.Green)
                Text(getEmptyBar(emptyCount), color = Color.Green, textStyle = TextStyle.Dim)
                Text("▏", textStyle = TextStyle.Dim)
              }
            }
            Spacer(Modifier.height(1))

            val currentChunk = state.passage.getOrElse(state.currentLineIndex) { "" }
            // Wrap the current chunk for display
            val displayLines = remember(currentChunk) { TextWrapper.displayWrap(currentChunk, 80) }
            // Calculate height dynamically based on wrapped lines
            val contentHeight = displayLines.size.coerceAtLeast(1)

            BorderedTitledContent(title = "Passage", width = 82, height = contentHeight) {
              Column {
                var currentGlobalIndex = 0
                val userInputLen = state.userInput.length

                displayLines.forEach { line ->
                  Row {
                    val lineLen = line.length
                    val lineStart = currentGlobalIndex
                    val lineEnd = lineStart + lineLen

                    if (userInputLen >= lineEnd) {
                      // Entire line is typed correctly
                      Text(line.replace("\n", ""))
                    } else if (userInputLen >= lineStart) {
                      // Partial line typed (or cursor at start)
                      val localCursor = userInputLen - lineStart
                      val typedPart = line.substring(0, localCursor)
                      Text(typedPart.replace("\n", ""))

                      val remainingInLine = line.substring(localCursor)
                      if (remainingInLine.isNotEmpty()) {
                        val nextChar = remainingInLine.take(1)
                        val rest = remainingInLine.drop(1)

                        if (state.isError) {
                          // Error on this character
                          if (nextChar == " ") {
                            Text("_", color = Color.Red)
                          } else if (nextChar == "\n") {
                            Text("⏎", color = Color.Red)
                          } else {
                            Text(nextChar, color = Color.Red)
                          }
                        } else {
                          // Cursor is here
                          if (nextChar == " ") {
                            Text("·", textStyle = TextStyle.Bold)
                          } else if (nextChar == "\n") {
                            Text("⏎", textStyle = TextStyle.Dim)
                          } else {
                            Text(
                                nextChar,
                                textStyle = TextStyle.Bold,
                                underlineStyle = UnderlineStyle.Straight)
                          }
                        }
                        // Rest of the line (untyped)
                        Text(rest.replace("\n", ""), textStyle = TextStyle.Dim)
                      }
                    } else {
                      // Future line
                      Text(line.replace("\n", ""), textStyle = TextStyle.Dim)
                    }
                    currentGlobalIndex += lineLen
                  }
                }
              }
            }
            Spacer(Modifier.height(1))
            Footer(options = playingFooterOptions)
          } else if (state.status == GameStatus.GAME_OVER) {
            val totalSeconds = state.elapsedTime / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60

            Text(COMPLETE_ART, color = Color.Green)
            Spacer(Modifier.height(1))

            Row {
              Column {
                Text("Final WPM", textStyle = TextStyle.Dim)
                Text("${state.wpm.toInt()}", textStyle = TextStyle.Bold, color = Color.Green)
              }
              Spacer(Modifier.width(4))
              Column {
                Text("Accuracy", textStyle = TextStyle.Dim)
                Text("${state.accuracy.toInt()}%", textStyle = TextStyle.Bold, color = Color.Green)
              }
              Spacer(Modifier.width(4))
              Column {
                Text("Time", textStyle = TextStyle.Dim)
                Text("${minutes}m ${seconds}s", textStyle = TextStyle.Bold, color = Color.Green)
              }
            }
            Spacer(Modifier.height(1))
            Footer(options = gameOverFooterOptions)
          }
        }
  }
}

// Pre-computed progress bar strings to avoid repeated allocation during rendering loop (hot path)
private val FILLED_BARS = Array(21) { "█".repeat(it) }
private val EMPTY_BARS = Array(21) { "░".repeat(it) }

private fun getFilledBar(count: Int): String {
  return if (count in FILLED_BARS.indices) FILLED_BARS[count] else "█".repeat(count)
}

private fun getEmptyBar(count: Int): String {
  return if (count in EMPTY_BARS.indices) EMPTY_BARS[count] else "░".repeat(count)
}

// Extracted to avoid reallocation on every recomposition/keystroke
private val countdownFooterOptions =
    listOf(
        FooterOption("Esc", "Back"),
    )

private val playingFooterOptions =
    listOf(
        FooterOption("Esc", "Menu"),
        FooterOption("Ctrl-C", "Quit"),
    )

private val gameOverFooterOptions =
    listOf(
        FooterOption("Enter", "New Passage"),
        FooterOption("R", "Retry"),
        FooterOption("Esc", "Menu"),
    )

private const val GET_READY_ART =
    """
   ______     __     ____                 __      __
  / ____/__  / /_   / __ \___  ____ _____/ /_  __/ /
 / / __/ _ \/ __/  / /_/ / _ \/ __ `/ __  / / / / /
/ /_/ /  __/ /_   / _, _/  __/ /_/ / /_/ / /_/ /_/
\____/\___/\__/  /_/ |_|\___/\__,_/\__,_/\__, (_)
                                        /____/
"""

private const val COMPLETE_ART =
    """
   ______                      __     __
  / ____/___  ____ ___  ____  / /__  / /____
 / /   / __ \/ __ `__ \/ __ \/ / _ \/ __/ _ \
/ /___/ /_/ / / / / / / /_/ / /  __/ /_/  __/
\____/\____/_/ /_/ /_/ .___/_/\___/\__/\___/
                    /_/
"""
