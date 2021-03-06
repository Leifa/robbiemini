package de.leifaktor.robbiemini.items;

import de.leifaktor.robbiemini.Room;
import de.leifaktor.robbiemini.SoundPlayer;
import de.leifaktor.robbiemini.actor.Actor;
import de.leifaktor.robbiemini.actor.DissolvingWall;
import de.leifaktor.robbiemini.actor.ItemActor;
import de.leifaktor.robbiemini.actor.Player;
import de.leifaktor.robbiemini.commands.AddActorCommand;
import de.leifaktor.robbiemini.commands.ChangeTileCommand;
import de.leifaktor.robbiemini.commands.PlaySoundCommand;
import de.leifaktor.robbiemini.tiles.EmptyTile;
import de.leifaktor.robbiemini.tiles.Wall;

public class Acid extends Item {
	
	@Override
	public void onUse(Room room, int x, int y, int z) {
		boolean useAcid = false;
		int px = room.getPlayer().x;
		int py = room.getPlayer().y;					
		for (int i = px - 1; i <= px+1; i++) {
			for (int j = py - 1; j <= py+1; j++) {
				if (room.isInBounds(i,j)) {							
					if (room.getTile(i, j, z) instanceof Wall) {
						useAcid = true;
						room.commands.add(new ChangeTileCommand(i, j, z, new EmptyTile()));
						room.commands.add(new AddActorCommand(new DissolvingWall(i, j, z)));
					}
				}
			}
		}
		if (useAcid) {
			room.commands.add(new PlaySoundCommand(SoundPlayer.SOUND_ACID));
		} else {
			room.commands.add(new AddActorCommand(new ItemActor(x, y, z, this)));
		}
	}
	
	@Override
	public Item clone() {
		return new Acid();
	}	

	@Override
	public void onHitBy(Room room, ItemActor itemActor, Actor actor, int x, int y, int z) {		
		if (actor instanceof Player) {
			itemActor.collect(room, (Player) actor);
			room.commands.add(new PlaySoundCommand(SoundPlayer.SOUND_COLLECT));
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Acid;
	}
	
	@Override
	public boolean isStackable() {
		return true;		
	}
	
	

}
