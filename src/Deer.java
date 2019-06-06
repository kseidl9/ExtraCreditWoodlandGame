import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

class Deer extends AbstractMobileEntity {

    private static final String DEER_KEY = "deer";
    private String direction;
    private static final int TILE = 32;
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    //private boolean letThereBeBunnies = false;


    public Deer(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public Point setDirectionNextPosition(WorldModel world, Point destPos, String direction){
        this.direction = direction;
        return nextPosition(world, destPos);
    }
    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        if (world.isOccupied(destPos)){
            return getPosition();
        }
        return destPos;
    }

    @Override
    public Point getPointBehind(WorldModel world) {
        Point deerPoint = getPosition();
        switch(direction){
            case UP:
                return new Point(deerPoint.x, deerPoint.y + TILE);

            case DOWN:
                return new Point(deerPoint.x, deerPoint.y - TILE);

            case RIGHT:
                return new Point(deerPoint.x - TILE, deerPoint.y);

            case LEFT:
                return new Point(deerPoint.x + TILE, deerPoint.y);

        }
        return deerPoint; //shouldn't happen

    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> bunnyTarget = world.findNearest(this.getPosition(), Bunny.class);
        long nextPeriod = this.getActionPeriod();

        if (bunnyTarget.isPresent()) {

            if (touchesBun((Bunny) bunnyTarget.get())) {
                //bunny follows deer
                ((Bunny) bunnyTarget.get()).setShouldFollow(true);
                nextPeriod += this.getActionPeriod();
            }
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public static Deer createDeer(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        //for when game starts
        System.out.print("here");
        Random rand = new Random();
        Point start = new Point(rand.nextInt(32 / 2), rand.nextInt(32 / 2));

        Deer deer = new Deer(DEER_KEY, start, imageStore.getImageList(DEER_KEY), 6, 5);


        world.addEntity(deer);
        deer.scheduleActions(scheduler, world, imageStore);
        System.out.print("deer");
        return deer;
    }


    private boolean touchesBun(Bunny target) {
        return this.getPosition().adjacent(target.getPosition());
    }
}

