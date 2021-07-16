package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import model.Settings;
import model.card.*;
import util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FrameController class, handles each frame's update
 * @author Adibov & Armin Gh
 * @version 1.0
 */
public class FrameController extends AnimationTimer {
  private MapViewController mapViewController;
  private List<Troop> activeTroops = new ArrayList<>();
  private List<Spell> activeSpells = new ArrayList<>();
  private List<Building> activeBuildings = new ArrayList<>();
  private Map<Card, ImageView> cardsImage = new HashMap<>();

  private KingTower friendlyKingTower;
  private PrinceTower friendlyPrinceTowerL, friendlyPrinceTowerR;
  private KingTower enemyKingTower;
  private PrinceTower enemyPrinceTowerL, enemyPrinceTowerR;

  /**
   * class constructor
   * @param mapViewController mapViewController field value
   */
  public FrameController(MapViewController mapViewController) {
    this.mapViewController = mapViewController;
  }

  public void initialize() {
    addTowersToMap();
  }

  /**
   * add tower images to map
   */
  private void addTowersToMap() {
    friendlyKingTower = new KingTower();
    friendlyPrinceTowerL = new PrinceTower();
    friendlyPrinceTowerR = new PrinceTower();
    enemyKingTower = new KingTower();
    enemyPrinceTowerL = new PrinceTower();
    enemyPrinceTowerR = new PrinceTower();

    friendlyKingTower.setTeamNumber(0);
    friendlyPrinceTowerL.setTeamNumber(0);
    friendlyPrinceTowerR.setTeamNumber(0);
    enemyKingTower.setTeamNumber(1);
    enemyPrinceTowerL.setTeamNumber(1);
    enemyPrinceTowerR.setTeamNumber(1);

    activeBuildings.add(friendlyKingTower);
    activeBuildings.add(friendlyPrinceTowerL);
    activeBuildings.add(friendlyPrinceTowerR);
    activeBuildings.add(enemyKingTower);
    activeBuildings.add(enemyPrinceTowerL);
    activeBuildings.add(enemyPrinceTowerR);

    ImageView friendlyKingTowerImage = new ImageView(Config.retrieveProperty("KING_TOWER_FRIENDLY"));
    ImageView friendlyPrinceTowerLImage = new ImageView(Config.retrieveProperty("PRINCE_TOWER_FRIENDLY"));
    ImageView friendlyPrinceTowerRImage = new ImageView(Config.retrieveProperty("PRINCE_TOWER_FRIENDLY"));
    ImageView enemyKingTowerImage = new ImageView(Config.retrieveProperty("KING_TOWER_ENEMY"));
    ImageView enemyPrinceTowerLImage = new ImageView(Config.retrieveProperty("PRINCE_TOWER_ENEMY"));
    ImageView enemyPrinceTowerRImage = new ImageView(Config.retrieveProperty("PRINCE_TOWER_ENEMY"));

    cardsImage.put(friendlyKingTower, friendlyKingTowerImage);
    cardsImage.put(friendlyPrinceTowerL, friendlyPrinceTowerLImage);
    cardsImage.put(friendlyPrinceTowerR, friendlyPrinceTowerRImage);
    cardsImage.put(enemyKingTower, enemyKingTowerImage);
    cardsImage.put(enemyPrinceTowerL, enemyPrinceTowerLImage);
    cardsImage.put(enemyPrinceTowerR, enemyPrinceTowerRImage);

    int middleColumn = Settings.MAP_COLUMN_COUNT / 2;
    mapViewController.addMapGrid(friendlyKingTowerImage, middleColumn, Settings.MAP_ROW_COUNT - 1);
    mapViewController.addMapGrid(friendlyPrinceTowerLImage, 1, Settings.MAP_ROW_COUNT - 3);
    mapViewController.addMapGrid(friendlyPrinceTowerRImage, Settings.MAP_COLUMN_COUNT - 2, Settings.MAP_ROW_COUNT - 3);
    mapViewController.addMapGrid(enemyKingTowerImage, middleColumn, 0);
    mapViewController.addMapGrid(enemyPrinceTowerLImage, 1, 1);
    mapViewController.addMapGrid(enemyPrinceTowerRImage, Settings.MAP_COLUMN_COUNT - 2, 1);
  }

  /**
   * add the given card to the list of active cards
   * @param card the given card
   * @param x x position of the new card
   * @param y y position of the new card
   */
  public void addCardToMap(Card card, double x, double y) {
    if (card.getCardGroup().equals(CardGroups.TROOP))
      activeTroops.add((Troop) card);
    else if (card.getCardGroup().equals(CardGroups.SPELL))
      activeSpells.add((Spell) card);
    else
      activeBuildings.add((Building) card);
    ImageView newImageView = new ImageView(Config.retrieveProperty(card.getImageKey()));
    newImageView.setX(x);
    newImageView.setY(y);
    cardsImage.put(card, newImageView);
    mapViewController.addImageView(newImageView);
  }

  /**
   * update hp of the active troops and building
   */
  private void updateHp() {

  }

  /**
   * update spells' state
   */
  private void updateSpells() {

  }

  /**
   * update troops and buildings' target
   */
  private void updateTargets() {
    for (Troop troop : activeTroops)
      updateTarget(troop);
    for (Building building : activeBuildings)
      updateTarget(building);
  }

  /**
   * update troops' velocity
   */
  private void updateVelocities() {

  }

  /**
   * update target for the given attackingCard
   * @param attackingCard the given attackingCard
   */
  private void updateTarget(Attacker attackingCard) {
    double minimumDistance = 1000;
    for (Card card : activeTroops) {
      double distance = getDistance(attackingCard, card);
      if (distance < minimumDistance) {
        minimumDistance = distance;
        attackingCard.setCurrentTarget(card);
      }
    }

    for (Card card : activeBuildings) {
      double distance = getDistance(attackingCard, card);
      if (distance < minimumDistance) {
        minimumDistance = distance;
        attackingCard.setCurrentTarget(card);
      }
    }

    if (getDistance(attackingCard, attackingCard.getCurrentTarget()) <= attackingCard.getRangeDistance())
      attackingCard.setAttacking(true);
    else
      attackingCard.setAttacking(false);
  }

  /**
   * get minimum distance between two given card
   * @param source source card (attacker)
   * @param destination destination card
   * @return distance value
   */
  private double getDistance(Card source, Card destination) {
    int sourceRegion = getRegionNumber(source);
    int destinationRegion = getRegionNumber(destination);

    ImageView sourceImage = cardsImage.get(source);
    ImageView destinationImage = cardsImage.get(destination);
    if (sourceImage == null || destinationImage == null)
      return 100;

    boolean euclideanDistance = false;
    if (source.getCardGroup().equals(CardGroups.TROOP)) {
      Troop tempTroop = (Troop) source;
      euclideanDistance |= tempTroop.getTarget().equals(Target.AIR);
      euclideanDistance |= tempTroop.getTarget().equals(Target.ALL);
    }
    if (sourceRegion == destinationRegion || sourceRegion == 0 || destinationRegion == 0)
      euclideanDistance = true;
    if (euclideanDistance)
      return getEuclideanDistance(sourceImage.getX(), sourceImage.getY(), destinationImage.getX(), destinationImage.getY());

    double firstPath = getEuclideanDistance(sourceImage.getX(), sourceImage.getY(), Settings.LEFT_BRIDGE_X, Settings.LEFT_BRIDGE_Y) +
            getEuclideanDistance(Settings.LEFT_BRIDGE_X, Settings.LEFT_BRIDGE_Y, destinationImage.getX(), destinationImage.getY());
    double secondPath = getEuclideanDistance(sourceImage.getX(), sourceImage.getY(), Settings.RIGHT_BRIDGE_X, Settings.RIGHT_BRIDGE_Y) +
            getEuclideanDistance(Settings.RIGHT_BRIDGE_X, Settings.RIGHT_BRIDGE_Y, destinationImage.getX(), destinationImage.getY());
    return Math.min(firstPath, secondPath);
  }

  /**
   * calculate the euclidean distance between two given points
   * @param x1 x position of the first point
   * @param y1 y position of the first point
   * @param x2 x position of the second point
   * @param y2 y position of the second point
   * @return euclidean distance
   */
  private double getEuclideanDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

  /**
   * calculate region number of the given card
   * @param card the given card
   * @return 0 for bridges and 1 for friendly half and 2 for enemy's half
   */
  private int getRegionNumber(Card card) {
    ImageView cardImage = cardsImage.get(card);
    if (cardImage == null)
      return 3;

    int middleRow = Settings.MAP_ROW_COUNT / 2;
    int cellRow = (int) (cardImage.getY() / Settings.CELL_HEIGHT);
    if (cellRow == middleRow)
      return 0;
    else if (cellRow > middleRow)
      return 1;
    else
      return 2;
  }

  /**
   * handle method will be called every frame
   * @param currentNanoTime current time in nanoseconds
   */
  @Override
  public void handle(long currentNanoTime) {
    updateHp();
    updateSpells();
    updateTargets();
  }
}
