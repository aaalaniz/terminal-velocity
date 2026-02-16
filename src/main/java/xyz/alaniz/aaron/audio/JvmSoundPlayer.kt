package xyz.alaniz.aaron.audio

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class JvmSoundPlayer : SoundPlayer {
  override fun playEffect(name: String) {
    val resource = javaClass.classLoader.getResource("audio/$name.wav")
    if (resource == null) {
      // Audio file not found, fail silently or log
      return
    }

    try {
      val audioInputStream = AudioSystem.getAudioInputStream(resource)
      val clip = AudioSystem.getClip()
      clip.addLineListener { event ->
        if (event.type == LineEvent.Type.STOP) {
          clip.close()
        }
      }
      clip.open(audioInputStream)
      clip.start()
    } catch (e: Exception) {
      // Error playing sound
      e.printStackTrace()
    }
  }
}
