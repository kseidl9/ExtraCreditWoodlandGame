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

    private static final int OBSTACLE_ROW = 3;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final String OBSTACLE_KEY = "obstacle";

    private static final int ORE_ACTION_PERIOD = 4;
    private static final int ORE_ROW = 3;
    private static final int ORE_COL = 2;
    private static final int ORE_ID = 1;
    private static final int ORE_NUM_PROPERTIES = 5;
    private static final String ORE_KEY = "ore";

    private static final int SMITH_ROW = 3;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ID = 1;
    private static final int SMITH_NUM_PROPERTIES = 4;
    private static final String SMITH_KEY = "blacksmith";

    private static final int VEIN_ACTION_PERIOD = 4;
    private static final int VEIN_ROW = 3;
    private static final int VEIN_COL = 2;
    private static final int VEIN_ID = 1;
    private static final int VEIN_NUM_PROPERTIES = 5;
    private static final String VEIN_KEY = "vein";


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



}
