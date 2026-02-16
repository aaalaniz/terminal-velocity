import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import java.io.File
import java.util.concurrent.CountDownLatch

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: kotlinc -script PlaySound.kts <path-to-wav>")
        return
    }

    val file = File(args[0])
    if (!file.exists()) {
        println("File not found: ${file.absolutePath}")
        return
    }

    try {
        val audioInputStream = AudioSystem.getAudioInputStream(file)
        val clip = AudioSystem.getClip()
        val latch = CountDownLatch(1)

        clip.addLineListener { event ->
            if (event.type == LineEvent.Type.STOP) {
                clip.close()
                latch.countDown()
            }
        }

        clip.open(audioInputStream)
        clip.start()
        println("Playing ${file.name}...")

        // Keep the main thread alive until playback finishes
        latch.await()
        println("Playback finished.")

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
main(args)
