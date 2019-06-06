import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Bunny extends AbstractMobileEntity{

    private static final String CARROT_KEY = "carrot";
    private static final String BUNNY_KEY = "bunny";
    private static final int TILE = 32;
    private boolean shouldFollow = false;
    //private boolean letThereBeBunnies = false;


    public Bunny(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    /*public void letThereBeBunnies(){
        letThereBeBunnies = true;
    }
    */

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        Point position = getPosition();
        PathingStrategy strategy = getStrategy();
        List<Point> pts = strategy.computePath(position, destPos,
                p -> !world.isOccupied(p),
                (s,d) -> s.adjacent(d),
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
        if (pts.isEmpty())
            return position;
        else
            return pts.get(pts.size() -2);
    }

    @Override
    public Point getPointBehind(WorldModel world) {
        return null;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        long nextPeriod = this.getActionPeriod();
        if(shouldFollow) {
            Optional<Entity> deerTarget = world.findNearest(this.getPosition(), Deer.class);

            if (deerTarget.isPresent()) {
                //Point tgtPos = deerTarget.get().getPosition();

                if (moveTo(world, (Deer)deerTarget.get(), scheduler)) {
                    //Carrot carrot = new Carrot("carrot", tgtPos, imageStore.getImageList(CARROT_KEY));
                    //world.addEntity(carrot);
                    nextPeriod += this.getActionPeriod();
                }
            }
        } else {
            moveRandom(world);
            nextPeriod += this.getActionPeriod();
       }
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public static void spawnBunnies(WorldModel world, Point p, ImageStore imageStore, EventScheduler scheduler){
        //spawn bunnies
        System.out.print("here");
        Random rand = new Random();
        int nbuns = rand.nextInt(20)+1;

        for (int i = 0; i < nbuns; i++ ){
            Point close = new Point(p.x + rand.nextInt(7), p.y + rand.nextInt(7));
            System.out.println(close);
            Bunny bun = new Bunny("bunny",close, imageStore.getImageList(BUNNY_KEY), 6,5 );
            world.addEntity(bun);
            bun.scheduleActions(scheduler, world, imageStore);

        }

    }

    private void moveRandom(WorldModel world) {
        Random rand = new Random();
        int x = this.getPosition().x;
        int y = this.getPosition().y;
        int negX = rand.nextInt(3);
        int negY = rand.nextInt(3);
        if(negX == 1) {
            x = x + TILE;
        }
        else if(negX == 2) {
            x = x - TILE;
        }
        if(negY == 1) {
            y = y + TILE;
        }
        else if(negY == 2) {
            y = y - TILE;
        }
        if(!world.isOccupied(new Point(x,y)) && world.withinBounds(new Point(x,y))) {
            world.moveEntity(this, new Point(x, y));
        }
    }

    private boolean moveTo(WorldModel world, Deer target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void setShouldFollow(boolean shouldFollow) {
        this.shouldFollow = shouldFollow;
    }
}


