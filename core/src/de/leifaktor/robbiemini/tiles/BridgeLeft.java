package de.leifaktor.robbiemini.tiles;

import de.leifaktor.robbiemini.Room;
import de.leifaktor.robbiemini.actor.Actor;
import de.leifaktor.robbiemini.actor.Player;
import de.leifaktor.robbiemini.actor.Robot;

public class BridgeLeft extends Tile {

	@Override
	public boolean canBeEntered(Actor actor, int dir) {
		if (actor instanceof Player || actor instanceof Robot) {
			return dir == 1;
		}
		return false;
	}

	@Override
	public void onLeave(Room room, Actor actor, int dir) {
		if (dir == 1) room.liftActorOneLayer(actor);
	}	

}
