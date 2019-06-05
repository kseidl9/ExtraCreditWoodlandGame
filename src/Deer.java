import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.awt.*;

import static java.awt.Event.DOWN;
import static java.awt.Event.UP;
import static javax.swing.JSplitPane.LEFT;
import static processing.core.PConstants.CODED;

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

            if (bunnyTarget.isPresent())
            {
                Point tgtPos = bunnyTarget.get().getPosition();

                if (moveTo(world, (Bunny)bunnyTarget.get(), scheduler))
                {
                    //bunny follows deer
                    nextPeriod += this.getActionPeriod();
                }
            }

            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world, imageStore),
                    nextPeriod);
        }

        public static Deer createDeer(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
            //for when game starts
            System.out.print("here");
            Random rand = new Random();
            Point start = new Point(rand.nextInt(32), rand.nextInt(32));

            Deer deer = new Deer(DEER_KEY,start, imageStore.getImageList(DEER_KEY), 6,5 );


            world.addEntity(deer);
            deer.scheduleActions(scheduler, world, imageStore);
            System.out.print("deer");
            return deer;
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
