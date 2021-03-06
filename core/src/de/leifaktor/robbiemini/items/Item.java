package de.leifaktor.robbiemini.items;

import de.leifaktor.robbiemini.Room;
import de.leifaktor.robbiemini.actor.Actor;
import de.leifaktor.robbiemini.actor.ItemActor;

public abstract class Item {
	
	public abstract void onUse(Room room, int x, int y, int z);
	
	public void onHitBy(Room room, ItemActor itemActor, Actor actor, int x, int y, int z) {};
	
	public abstract Item clone();

	public boolean isStackable() {return false;}
	
}
