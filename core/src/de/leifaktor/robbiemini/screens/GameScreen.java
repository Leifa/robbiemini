package de.leifaktor.robbiemini.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.leifaktor.robbiemini.Episode;
import de.leifaktor.robbiemini.GlobalVars;
import de.leifaktor.robbiemini.IO;
import de.leifaktor.robbiemini.InputManager;
import de.leifaktor.robbiemini.RobbieMini;
import de.leifaktor.robbiemini.Room;
import de.leifaktor.robbiemini.RoomManager;
import de.leifaktor.robbiemini.TestEpisode;
import de.leifaktor.robbiemini.XYZPos;
import de.leifaktor.robbiemini.actor.Player;
import de.leifaktor.robbiemini.render.InventoryRenderer;
import de.leifaktor.robbiemini.render.RoomRenderer;
import de.leifaktor.robbiemini.render.StatusBarRenderer;
import de.leifaktor.robbiemini.render.TextboxRenderer;

public class GameScreen implements Screen {
	
	RobbieMini game;
	
	Viewport viewport;
	Camera camera;
	
	Episode episode;
	
	RoomManager roomManager;
	Room currentRoom;
	XYZPos currentRoomPosition;
	XYZPos newRoomPosition;
	Player player;
	boolean startRoomTransitionAfterThisFrame;
	
	boolean startTeleportAfterThisFrame;
	XYZPos playerTeleportPosition;
	
	RoomRenderer renderer;
	StatusBarRenderer barRenderer;
	InventoryRenderer inventoryRenderer;
	TextboxRenderer textboxRenderer;
	
	int textboxWidth = 22;
	int textboxHeight = 1;

	String textboxText;
	boolean showTextboxFromNextFrame;
	
	GlobalVars globalVars;
	
	State state;
	
	enum State {
		GAME,
		INVENTORY,
		TEXTBOX
	}
	
	public GameScreen(RobbieMini game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.position.set(RobbieMini.getVirtualWidth()/2, RobbieMini.getVirtualHeight()/2, 1);
		viewport = new FitViewport(RobbieMini.getVirtualWidth(), RobbieMini.getVirtualHeight(), camera);
		camera.update();
		
		loadEpisode(TestEpisode.createTestEpisode());
		
		renderer = new RoomRenderer();
		renderer.setOffset(0, 0);
		barRenderer = new StatusBarRenderer();
		barRenderer.setOffset(0, RobbieMini.getVirtualHeight()-RobbieMini.TILESIZE);
		inventoryRenderer = new InventoryRenderer();
		inventoryRenderer.setOffset(0, RobbieMini.getVirtualHeight()-RobbieMini.TILESIZE);
		textboxRenderer = new TextboxRenderer();
		textboxRenderer.setSize(textboxWidth, textboxHeight);
		
		InputManager.initKeyMap();
	}
	
	private void save() {
		episode.startingPosition = new XYZPos(player.x, player.y, player.z);
		episode.startingRoom = currentRoomPosition;
		currentRoom.removePlayer();
		IO.save(episode, "episode.rob");
		currentRoom.putPlayer(player, player.x, player.y, player.z);
	}
	
	private void load() {
		loadEpisode(IO.load("episode.rob"));
	}
	
	public void loadEpisode(Episode epi) {
		episode = epi;
		roomManager = epi.roomManager;
		globalVars = epi.globalVars;
		currentRoomPosition = epi.startingRoom;
		player = epi.player;
		currentRoom = roomManager.getRoom(currentRoomPosition);
		player.setPosition(epi.startingPosition);
		currentRoom.putPlayer(player, player.x, player.y, player.z);
		currentRoom.setGameScreen(this);
	}
	
	@Override
	public void render(float delta) {
		InputManager.update();
		if (Gdx.input.isKeyJustPressed(Keys.S)) save();
		if (Gdx.input.isKeyJustPressed(Keys.L)) load();
		if (showTextboxFromNextFrame) {
			state = State.TEXTBOX;
			showTextboxFromNextFrame = false;
		}
		if (state == State.TEXTBOX) {
			if (InputManager.justPressed[InputManager.ENTER]) state = State.GAME;
		} else {
			processInventory();
			currentRoom.update();
		}
		renderTheGame();		
		if (startRoomTransitionAfterThisFrame) {
			startRoomTransition();
			startRoomTransitionAfterThisFrame = false;
		}
		if (startTeleportAfterThisFrame) {
			startTeleport();
			startTeleportAfterThisFrame = false;
		}
	}
	
	private void startTeleport() {
		currentRoom.removePlayer();
		currentRoomPosition = newRoomPosition;
		currentRoom = roomManager.getRoom(newRoomPosition);
		currentRoom.putPlayer(player, playerTeleportPosition.x, playerTeleportPosition.y, playerTeleportPosition.z);
		player.stopMoving();
		currentRoom.setGameScreen(this);		
	}

	private void processInventory() {		
		if (state == State.INVENTORY) {
			player.inventory.update();
			if (InputManager.justPressed[InputManager.ENTER]) {
				if (player.inventory.getSelectedItem() != null) {
					currentRoom.useItem(player.inventory.getSelectedItem(), player.x, player.y, player.z);
					player.inventory.removeSelectedItem();
					player.inventory.decreasePointer();
				}
				state = State.GAME;
			} else if (InputManager.justPressed[InputManager.ESCAPE]) {
				state = State.GAME;
			}
		} else {
			if (InputManager.justPressed[InputManager.ENTER]) state = State.INVENTORY;
		}
	}
	
	private void renderTheGame() {
		game.batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		renderer.render(game.batch, currentRoom);
		if (state == State.INVENTORY) {
			inventoryRenderer.render(game.batch, currentRoom, player.inventory);
		} else {
			barRenderer.render(game.batch, currentRoom);
		}
		if (state == State.TEXTBOX) {
			textboxRenderer.render(game.batch, textboxText);
		}		
		game.batch.end();
	}
	
	private void startRoomTransition() {
		int dx = newRoomPosition.x - currentRoomPosition.x;
		int dy = newRoomPosition.y - currentRoomPosition.y;
		
		int direction = -1;
		if (dx == 0 && dy == 1) direction = 0;
		else if (dx == 1 && dy == 0) direction = 1;
		else if (dx == 0 && dy == -1) direction = 2;
		else if (dx == -1 && dy == 0) direction = 3;
		
		Room newRoom = roomManager.getRoom(newRoomPosition);
		game.setScreen(new RoomTransitionScreen(game, this, currentRoom, newRoom, direction));
		
		currentRoom.removePlayer();
		currentRoomPosition = newRoomPosition;
		currentRoom = newRoom;
		currentRoom.putPlayer(player, player.x, player.y, player.z);
		player.stopMoving();
		currentRoom.setGameScreen(this);
	}

	public void setRoom(XYZPos newRoomPosition) {
		this.newRoomPosition = newRoomPosition;
		startRoomTransitionAfterThisFrame = true;
	}
	
	@Override
	public void show() {
				
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width,  height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {

	}

	public RoomManager getRoomManager() {
		return roomManager;		
	}
	
	public XYZPos getCurrentRoomPosition() {
		return currentRoomPosition;
	}
	
	public void gameOver() {
		game.setScreen(new MainMenuScreen(game));
	}
	
	public boolean isInventoryOpen() {
		return state == State.INVENTORY;
	}
	
	public void showTextbox(String text) {
		this.textboxText = text;
		textboxHeight = (int) textboxRenderer.getHeight(text) / RobbieMini.TILESIZE + 3;
		textboxRenderer.setOffset((RobbieMini.getVirtualWidth()-textboxWidth*RobbieMini.TILESIZE)/2, (RobbieMini.getVirtualHeight()-textboxHeight*RobbieMini.TILESIZE)/2);
		textboxRenderer.setSize(textboxWidth, textboxHeight);
		showTextboxFromNextFrame = true;
	}
	
	public void teleportTo(XYZPos roomPosition, XYZPos playerPosition) {
		this.newRoomPosition = roomPosition;
		this.playerTeleportPosition = playerPosition;
		startTeleportAfterThisFrame = true;
	}
	
	public boolean getGlobalBoolean(String key) {
		return globalVars.getBoolean(key);
	}
	
	public void setGlobalBoolean(String key, boolean value) {
		globalVars.setBoolean(key, value);
	}

}
