package de.leifaktor.robbiemini.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.leifaktor.robbiemini.items.Acid;
import de.leifaktor.robbiemini.items.Blaumann;
import de.leifaktor.robbiemini.items.Item;
import de.leifaktor.robbiemini.items.Key;
import de.leifaktor.robbiemini.items.Life;
import de.leifaktor.robbiemini.items.Magnet;
import de.leifaktor.robbiemini.items.Notiz;
import de.leifaktor.robbiemini.items.Schleuder;

public class ItemRenderer {
	
	public static void render(SpriteBatch batch, Item item, float x, float y) {
		if (item instanceof Acid) {
			batch.draw(Graphics.textures.get("acid"), x, y);
		} else if (item instanceof Key) {
			Key key = (Key) item;
			batch.draw(Graphics.textures.get("key_" + key.getNumber()), x, y);
		} else if (item instanceof Life) {
			batch.draw(Graphics.textures.get("life"), x, y);
		} else if (item instanceof Magnet) {
			if (((Magnet)item).isPositive()) {
				batch.draw(Graphics.textures.get("magnet_positive"), x, y);
			} else {
				batch.draw(Graphics.textures.get("magnet_negative"), x, y);
			}
		} else if (item instanceof Blaumann) {
			batch.draw(Graphics.textures.get("blaumann"), x, y);
		} else if (item instanceof Notiz) {
			batch.draw(Graphics.textures.get("notiz"), x, y);
		} else if (item instanceof Schleuder) {
			batch.draw(Graphics.textures.get("schleuder"), x, y);
		} else {
			batch.draw(Graphics.textures.get("inventory_outer_background"), x, y);
		}
	}

}