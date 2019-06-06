import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Fox extends AbstractMobileEntity {

        private static final String CARROT_KEY = "carrot";
        private static final String FOX_KEY = "fox";
        //private boolean letThereBeBunnies = false;


        public Fox(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
            super(id, position, images, actionPeriod, animationPeriod);
        }

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
        public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            Optional<Entity> bunnyTarget = world.findNearest(this.getPosition(), Bunny.class);
            long nextPeriod = this.getActionPeriod();

            if (bunnyTarget.isPresent())
            {
                Point tgtPos = bunnyTarget.get().getPosition();

                if (moveTo(world, (Bunny)bunnyTarget.get(), scheduler))
                {
                    Carrot carrot = new Carrot("carrot", tgtPos, imageStore.getImageList(CARROT_KEY));
                    world.addEntity(carrot);
                    nextPeriod += this.getActionPeriod();
                }
            }

            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world, imageStore),
                    nextPeriod);
        }

        public static void spawnFoxes(WorldModel world, Point p, ImageStore imageStore, EventScheduler scheduler){

            Random rand = new Random();
            int foxies = rand.nextInt(3)+1;
            for (int i = 0; i < foxies; i++ ){
                Point close = new Point(p.x + rand.nextInt(7), p.y + rand.nextInt(7));

                Fox fox = new Fox(FOX_KEY,close, imageStore.getImageList(FOX_KEY), 6,5 );


                world.addEntity(fox);
                fox.scheduleActions(scheduler, world, imageStore);

            }

        }
        private boolean moveTo(WorldModel world, Bunny target, EventScheduler scheduler)
        {
            if (this.getPosition().adjacent(target.getPosition()))
            {
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
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
    }

