# Research: javax.sound.sampled.Clip for Low-Latency Playback

## Overview
The `javax.sound.sampled` package provides the `Clip` interface, which is suitable for playing back short sound files with low latency. A `Clip` loads the entire audio data into memory before playback, allowing for immediate start.

## Key Classes
*   **AudioSystem**: The entry point for all audio resources.
*   **Clip**: A data line that loads audio data into memory.
*   **AudioInputStream**: A stream of audio data.
*   **LineListener**: Interface for receiving events (OPEN, CLOSE, START, STOP).

## Usage Pattern
1.  Obtain an `AudioInputStream` from a file or URL.
2.  Get a `Clip` instance from `AudioSystem`.
3.  Open the `Clip` with the stream.
4.  Call `start()` to play.
5.  Handle lifecycle events to close resources.

## Limitations
*   **Memory Usage**: Since `Clip` loads the entire file, it is not suitable for large audio files (e.g., background music). For those, `SourceDataLine` should be used for streaming.
*   **Format Support**: JVM support for audio formats (like MP3) might require SPI plugins. WAV is universally supported.
*   **Concurrency**: Playback is asynchronous by default, but resource management requires care to avoid leaks.

## Prototype Idea
A simple function `playEffect(file: File)` that:
1.  Checks if the file exists.
2.  Creates a `Clip`.
3.  Opens it.
4.  Starts it.
5.  Adds a listener to close the clip after playback stops.
