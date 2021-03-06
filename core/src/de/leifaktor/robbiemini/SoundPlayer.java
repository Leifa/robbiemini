package de.leifaktor.robbiemini;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {

	static SoundPlayer instance;	
	Sound[] sounds;
	
	public static final int SOUND_COLLECT = 0;
	public static final int SOUND_STEPS = 1;
	public static final int SOUND_ACID = 2;
	public static final int SOUND_EXPLOSION = 3;
	public static final int SOUND_DOOR = 4;
	public static final int SOUND_GOLD = 5;
	public static final int SOUND_LIFE = 6;
	public static final int SOUND_COLLECT_CLICK = 7;
	public static final int SOUND_COLLECT_SONG_1 = 8;
	public static final int SOUND_COLLECT_SONG_2 = 9;
	
	public static SoundPlayer getInstance() {
		if (instance == null) {
			instance = new SoundPlayer();
		}
		return instance;
	}
	
	private SoundPlayer() {
		sounds = new Sound[16];
		sounds[SOUND_COLLECT] = Gdx.audio.newSound(Gdx.files.internal("collect.wav"));
		sounds[SOUND_STEPS] = Gdx.audio.newSound(Gdx.files.internal("steps.wav"));
		sounds[SOUND_ACID] = Gdx.audio.newSound(Gdx.files.internal("acid.wav"));
		sounds[SOUND_EXPLOSION] = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
		sounds[SOUND_DOOR] = Gdx.audio.newSound(Gdx.files.internal("door.wav"));
		sounds[SOUND_GOLD] = Gdx.audio.newSound(Gdx.files.internal("gold.wav"));
		sounds[SOUND_LIFE] = Gdx.audio.newSound(Gdx.files.internal("life.wav"));
		sounds[SOUND_COLLECT_CLICK] = Gdx.audio.newSound(Gdx.files.internal("collect_click.wav"));
		sounds[SOUND_COLLECT_SONG_1] = Gdx.audio.newSound(Gdx.files.internal("collect_song_1.wav"));
		sounds[SOUND_COLLECT_SONG_2] = Gdx.audio.newSound(Gdx.files.internal("collect_song_2.wav"));
	}
	
	public void play(int id) {
		if (id == SOUND_STEPS) {
			if (Util.random.nextBoolean()) {
				sounds[id].play(0.6f);
			} else {
				sounds[id].play(0.6f, 1.1f, 1);
			}
			
		} else if (id >= 0 && id < sounds.length && sounds[id] != null) sounds[id].play(0.6f);
	}
	
}
