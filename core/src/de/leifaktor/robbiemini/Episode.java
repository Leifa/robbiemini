package de.leifaktor.robbiemini;

import de.leifaktor.robbiemini.actor.Player;

public class Episode {
	
	public RoomManager roomManager;
	public GlobalVars globalVars;
	public XYZPos startingRoom;
	public Player player;
	
	public Episode() {
		roomManager = new RoomManager();
		globalVars = new GlobalVars();
		startingRoom = new XYZPos(0,0,0);
		player = new Player();
	}

}
