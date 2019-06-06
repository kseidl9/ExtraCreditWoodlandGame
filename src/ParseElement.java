public class ParseElement {
    private static final int BGND_ROW = 3;
    private static final int BGND_COL = 2;
    private static final int BGND_ID = 1;
    private static final int BGND_NUM_PROPERTIES = 4;

    private static final int DEER_ANIMATION_PERIOD = 6;
    private static final int DEER_ACTION_PERIOD = 5;
    private static final int DEER_ROW = 3;
    private static final int DEER_COL = 2;
    private static final int DEER_ID = 1;
    private static final int DEER_NUM_PROPERTIES = 5;
    private static final String DEER_KEY = "deer";

    private static final int BUNNY_ANIMATION_PERIOD = 6;
    private static final int BUNNY_ACTION_PERIOD = 5;
    private static final int BUNNY_ROW = 3;
    private static final int BUNNY_COL = 2;
    private static final int BUNNY_ID = 1;
    private static final int BUNNY_NUM_PROPERTIES = 5;
    private static final String BUNNY_KEY = "deer";

    private static final int FOX_ANIMATION_PERIOD = 6;
    private static final int FOX_ACTION_PERIOD = 5;
    private static final int FOX_ROW = 3;
    private static final int FOX_COL = 2;
    private static final int FOX_ID = 1;
    private static final int FOX_NUM_PROPERTIES = 5;
    private static final String FOX_KEY = "deer";


    private static final int GAME_OVER_ROW = 3;
    private static final int GAME_OVER_COL = 2;
    private static final int GAME_OVER_ID = 1;
    private static final int GAME_OVER_PROPERTIES = 3;
    private static final String GAME_OVER_KEY = "gameOver";


    public static boolean parseBackground(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt, new Background(imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public static boolean parseBunny(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == BUNNY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BUNNY_COL]),
                    Integer.parseInt(properties[BUNNY_ROW]));
            Entity entity = new Bunny(
                    properties[BUNNY_ID], pt, imageStore.getImageList(BUNNY_KEY),
                    Integer.parseInt(properties[BUNNY_ACTION_PERIOD]),
                    Integer.parseInt(properties[BUNNY_ANIMATION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == BUNNY_NUM_PROPERTIES;
    }

    public static boolean parseFox(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == FOX_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FOX_COL]),
                    Integer.parseInt(properties[FOX_ROW]));
            Entity entity = new Fox(
                    properties[FOX_ID], pt, imageStore.getImageList(FOX_KEY),
                    Integer.parseInt(properties[FOX_ACTION_PERIOD]),
                    Integer.parseInt(properties[FOX_ANIMATION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == FOX_NUM_PROPERTIES;
    }

    public static boolean parseDeer(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == DEER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DEER_COL]),
                    Integer.parseInt(properties[DEER_ROW]));
            Entity entity = new Deer(
                    properties[DEER_ID], pt, imageStore.getImageList(DEER_KEY),
                    Integer.parseInt(properties[DEER_ACTION_PERIOD]),
                    Integer.parseInt(properties[DEER_ANIMATION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == DEER_NUM_PROPERTIES;
    }

    public static boolean parseGameOver(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == GAME_OVER_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[GAME_OVER_COL]), Integer.parseInt(properties[GAME_OVER_ROW]));
            Entity entity = new GameOver(
                    properties[GAME_OVER_ID], pt, imageStore.getImageList(GAME_OVER_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == GAME_OVER_PROPERTIES;
    }



}
