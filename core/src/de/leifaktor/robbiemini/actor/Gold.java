package de.leifaktor.robbiemini.actor;

import de.leifaktor.robbiemini.Room;
import de.leifaktor.robbiemini.SoundPlayer;
import de.leifaktor.robbiemini.commands.PlaySoundCommand;
import de.leifaktor.robbiemini.commands.RemoveActorCommand;

public class Gold extends Actor {
	
	public Gold() {} // no-arg constructor for JSON

	public Gold(int x, int y, int z) {
		super(x, y , z);
	}
	
	@Override
	public void update(Room room) {
		
	}

	@Override
	public void hitBy(Room room, Actor actor) {
		if (actor instanceof Player) {
			((Player)actor).collectGold();
			room.commands.add(new RemoveActorCommand(this));
			room.commands.add(new PlaySoundCommand(SoundPlayer.SOUND_GOLD));
		}
	}

	@Override
	public Actor clone() {
		return new Gold(x, y, z);
	}

	@Override
	public boolean canBeEntered(Actor other) {
		return other instanceof Player;
	}	

}
