package model;

public class Settings {
  public static final int GAME_DURATION_IN_SECONDS = 3 * 60; // seconds
  public static final int FIRST_PERIOD_TO_INCREMENT_ELIXIR = 2000; // milliseconds
  public static final int SECOND_PERIOD_TO_INCREMENT_ELIXIR = 1000; // milliseconds
  public static final int INITIAL_ELIXIR = 4;
  public static final int ELIXIR_INCREASE = 1;
  public static final int DECK_SIZE = 8;

  public static final int MAP_ROW_COUNT = 13;
  public static final int MAP_COLUMN_COUNT = 7;
  public static final int CELL_HEIGHT = 64;
  public static final double LEFT_BRIDGE_X = 246;
  public static final double LEFT_BRIDGE_Y = 352;
  public static final double RIGHT_BRIDGE_X = 502;
  public static final double RIGHT_BRIDGE_Y = 352;

  public static final double MELEE_ATTACK_RANGE = 1.0; // FIXME check how much range will be appropriate
  public static final double ARCHER_ATTACK_RANGE = 5.0;
  public static final double BABY_DRAGON_ATTACK_RANGE = 3.0;
  public static final double WIZARD_ATTACK_RANGE = 5.0;
  public static final double CANNON_ATTACK_RANGE = 5.5;
  public static final double INFERNO_TOWER_ATTACK_RANGE = 6.0;
  public static final double KING_TOWER_ATTACK_RANGE = 7.0;
  public static final double PRINCE_TOWER_ATTACK_RANGE = 7.5;
}
