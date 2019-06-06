import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Fox extends AbstractMobileEntity {

        private static final String CARROT_KEY = "carrot";
        private static final String FOX_KEY = "fox";
        private Entity target = null;


        public Fox(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
            super(id, position, images, actionPeriod, animationPeriod);
        }

        public void kill(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
                    world.removeEntity(this);
                    scheduler.unscheduleAllEvents(this);


        }
        @Override
        public Point nextPosition(WorldModel world, Point destPos) {
            List<Point> pts = new LinkedList<>();
            if (target != null) {
                Point pos = target.getPosition();
                PathingStrategy strategy = getStrategy();
                pts = strategy.computePath(getPosition(), pos, p -> !world.isOccupied(p),
                        (s, d) -> s.adjacent(d),
                        PathingStrategy.CARDINAL_NEIGHBORS);
            }
            if (pts.isEmpty())
                return getPosition();
            else
                return pts.get(0);
        }

        @Override
        public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            //Optional<Entity> bunnyTarget = world.findNearest(this.getPosition(), Bunny.class);
            long nextPeriod = this.getActionPeriod();
            if (target!= null)
            {
                Entity nextTarget = target;
                Point tgtPos = target.getPosition();
                if (target instanceof Bunny){
                    nextTarget = ((Bunny)target).getTarget();
                }
                else if (target instanceof Deer){
                    GameOver.endGame(world, this.getPosition(), imageStore, scheduler);
                }


                if (moveTo(world, target, scheduler))
                {

                    Carrot carrot = new Carrot("carrot", tgtPos, imageStore.getImageList(CARROT_KEY));
                    world.addEntity(carrot);
                    target = nextTarget;
                    nextPeriod += this.getActionPeriod();
                }
            }

            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world, imageStore),
                    nextPeriod);
        }


        private boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
        {
            if (this.getPosition().adjacent(target.getPosition()))
            {
                Deer.killedBun((Bunny)target);
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
    public static void setTarget(Bunny bun, ImageStore imageStore, WorldModel world, EventScheduler scheduler){
        Random rand = new Random();
        Point start = new Point(rand.nextInt(32), rand.nextInt(32));
        Fox fox = new Fox(FOX_KEY, start, imageStore.getImageList(FOX_KEY), 6,5);
        world.addEntity(fox);
        fox.target = bun;
        fox.scheduleActions(scheduler, world, imageStore);

    }
    }

