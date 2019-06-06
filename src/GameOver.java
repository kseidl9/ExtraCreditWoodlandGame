import processing.core.PImage;

import java.util.List;
import java.util.Set;

public class GameOver extends AbstractEntity {
    private static final String GAME_OVER_KEY = "gameOver";
    public GameOver(String id, Point position, List<PImage>images) {
        super(id, position, images);
    }

    public static void endGame(WorldModel world, Point endPos, ImageStore imageStore, EventScheduler scheduler ){


        Set<Entity> entities = world.getEntities();
        GameOver go = new GameOver(GAME_OVER_KEY, endPos, imageStore.getImageList(GAME_OVER_KEY));
        world.addEntity(go);

        for (Entity e : entities){
            System.out.print("GAME OVER");
            world.removeEntity(e);
            scheduler.unscheduleAllEvents(e);
        }
    }
}
