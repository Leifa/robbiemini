package de.leifaktor.robbiemini.actor;

import com.badlogic.gdx.Gdx;

import de.leifaktor.robbiemini.Room;
import de.leifaktor.robbiemini.condition.Condition;

public class Sperre extends Actor {
	
	boolean isOpen;
	Condition openCondition;
	boolean leftRight;
	
	float timeUntilOpen;

	public Sperre() {} // no-arg constructor for JSON
	
	public Sperre(int x, int y, int z, Condition openCondition, boolean leftRight, boolean isOpen) {
		super(x, y, z);
		this.openCondition = openCondition;
		this.leftRight = leftRight;
		isOpen = true;
	}	

	@Override
	public boolean canBeEntered(Actor other) {		
		return isOpen;
	}

	@Override
	public void update(Room room) {
		super.update(room);
		if (timeUntilOpen > 0) {
			timeUntilOpen -= Gdx.graphics.getDeltaTime(); 
			if (timeUntilOpen < 0) {
				timeUntilOpen = 0;
				isOpen = true;
			}
		} else {
			boolean shouldOpen = openCondition.evaluate(room);
			if (shouldOpen) {
				timeUntilOpen = (x+y)*0.05f;
			}
		}
		
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public boolean isLeftRight() {
		return leftRight;
	}

	@Override
	public Actor clone() {
		return new Sperre(x, y, z, openCondition, leftRight, isOpen);
	}

}
