package model.card;

import javafx.scene.image.Image;
import util.Config;

public class InfernoTower extends Building {
  public static Image getDeckImage() {
    return new Image(Config.retrieveProperty("INFERNO_TOWER_DECK_IMAGE"));
  }
}