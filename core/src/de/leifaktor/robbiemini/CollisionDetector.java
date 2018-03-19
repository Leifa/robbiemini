package de.leifaktor.robbiemini;

import java.util.ArrayList;

import de.leifaktor.robbiemini.actor.Actor;
import de.leifaktor.robbiemini.actor.Robot;
import de.leifaktor.robbiemini.actor.Player;

public class CollisionDetector {

	public static boolean canMoveTo(Actor actor, Room room, int dir) {
		if (dir == -1) return true;
		int newx = actor.x + Direction.DIR_X[dir];
		int newy = actor.y + Direction.DIR_Y[dir];
		int width = room.width;

		// Ausnahmeregel: Roboter können IMMER das Feld mit dem Spieler betreten!
		if (room.player != null && room.player.x == newx && room.player.y == newy && actor instanceof Robot) return true; 

		// Man kann nicht out of bounds gehen. Ausnahme: Der Spieler kann, falls dort ein Raum existiert.
		if (!room.isInBounds(newx, newy)) {
			if (!(actor instanceof Player)) return false;
			if (dir > 3 || dir < 0) return false;
			return room.hasNeighborRoomAt(newx, newy);
		}
		
		if (!room.tiles[newy*width + newx].canBeEntered(actor)) return false;

		for (Actor other: room.actors) {
			if (other.x == newx && other.y == newy) {
				if (!other.canBeEntered(actor)) return false;
			}
		}
		return true;
	}
	
	public static boolean canShiftTo(Actor actor, Room room, int dir) {
		if (dir < 0 || dir > 3) return false;
		
		int newx = actor.x + Direction.DIR_X[dir];
		int newy = actor.y + Direction.DIR_Y[dir];
		
		if (!room.isInBounds(newx, newy)) return false;
		
		boolean somethingToShift = false;
		
		for (Actor other: room.actors) {
			if (other.x == newx && other.y == newy) {
				somethingToShift = true;
				if (!other.canBeShifted(actor))	return false;
				if (!(canMoveTo(other, room, dir) || canShiftTo(other, room, dir))) {
					return false;
				}
				if (! other.isOnTile()) return false;
			}
		}
		
		return somethingToShift;
	}

	public static int[] getPossibleDirections(Actor actor, Room room) {
		ArrayList<Integer> dirs = new ArrayList<Integer>();
		for (int dir=0; dir < 8; dir++) {			
			if (canMoveTo(actor, room, dir)) dirs.add(dir);
		}
		int[] dirArray = new int[dirs.size()];
		for (int i = 0; i < dirs.size(); i++) {
			dirArray[i] = dirs.get(i);
		}
		return dirArray;
	}

}
