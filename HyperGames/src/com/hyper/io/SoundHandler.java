package com.hyper.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.*;

public final class SoundHandler {
	public static final Mixer mix = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
	
	public static void test() {
		DataLine.Info info = new DataLine.Info(Clip.class, null);
		Clip clip = null;
		try {
			clip = (Clip) mix.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		Sequencer midi = null;
		try {
			midi = MidiSystem.getSequencer();
			midi.setSequence(new ByteArrayInputStream(new byte[] {(byte) 0x90, (byte) 0x90, (byte) 0x80,
					(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80}));
			midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			midi.open();
		} catch (MidiUnavailableException | IOException | InvalidMidiDataException e1) {
			e1.printStackTrace();
		}
		
//		ResourceLocation loc = new ResourceLocation("sound/pistol_shot.ogg");
//		try {
//			AudioInputStream stream = AudioSystem.getAudioInputStream(loc.getAsStream());
//			clip.open(stream);
//		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//			e.printStackTrace();
//		}
		midi.start();
		do {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(clip.isActive());
	}
}
