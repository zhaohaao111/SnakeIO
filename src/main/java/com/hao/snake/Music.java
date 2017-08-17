package com.hao.snake;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Music {
	public static void main(String[] args) {
		Music.play("/Sounds/bg.ogg", true);
	}
	
	
	public static void play(final String filePath, final boolean isLoop) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					final AudioInputStream in = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(filePath));
					final AudioFormat outFormat = getOutFormat(in.getFormat());
					final DataLine.Info info = new DataLine.Info(SourceDataLine.class,outFormat);
					
					final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
					
					if (line != null) {
						line.open(outFormat);
						line.start();
						stream(AudioSystem.getAudioInputStream(outFormat, in), line);
						line.drain();
						line.stop();
						if(isLoop){
							play(filePath, isLoop);
						}
					}
					
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
		});
		t.start();
	}

	private static AudioFormat getOutFormat(AudioFormat inFormat) {
		final int ch = inFormat.getChannels();
		final float rate = inFormat.getSampleRate();
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch,
				ch * 2, rate, false);
	}

	private static void stream(AudioInputStream in, SourceDataLine line)
			throws IOException {
		final byte[] buffer = new byte[65536];
		for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
			line.write(buffer, 0, n);
		}
	}
}
