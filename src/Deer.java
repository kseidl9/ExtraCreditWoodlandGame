import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

class Deer extends AbstractMobileEntity {

    private static final String DEER_KEY = "deer";
    private String direction;
    private static LinkedList<Bunny> followers = new LinkedList<>();


    public Deer(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        if (world.isOccupied(destPos)){
            return getPosition();
        }
        return destPos;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        //Optional<Entity> bunnyTarget = world.findNearest(this.getPosition(), Bunny.class);
        long nextPeriod = this.getActionPeriod();

        List<Bunny> notFollowing = PathingStrategy.CARDINAL_NEIGHBORS.apply(getPosition())
                .filter(p ->world.isOccupied(p) &&world.getOccupant(p).isPresent() &&world.getOccupant(p).get()instanceof Bunny)
                .map(p -> (Bunny)world.getOccupant(p).get())
                .filter(b -> !followers.contains(b))
                .collect(Collectors.toList());

        if (!notFollowing.isEmpty()) {
            Bunny bun = notFollowing.get(0);

            if (!followers.contains(bun) && touchesBun(bun)) {
                if (followers.size() == 0){
                    bun.setTarget(this);
                    followers.add(bun);
                }
                else{
                    bun.setTarget(followers.getLast());
                    followers.add(bun);
                }
                nextPeriod += this.getActionPeriod();
            }
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public static Deer createDeer(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        //for when game starts
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

    public List<Bunny> getFollowers() {
        return followers;
    }
}

