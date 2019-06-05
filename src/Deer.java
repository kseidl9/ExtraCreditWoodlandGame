import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

class Deer extends AbstractMobileEntity {

    private static final String DEER_KEY = "deer";
    //private boolean letThereBeBunnies = false;


    public Deer(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        Point position = getPosition();
        return destPos;
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

