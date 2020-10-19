import java.util.Random;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

public class FoodManager {

    private CanvasWindow canvas;

    public Food food;

    private Point location;

    private GraphicsGroup group;

    private int x;
    private int y;

    FoodManager(CanvasWindow canvas, GraphicsGroup group) {
        x = new Random().nextInt(canvas.getWidth());
        y = new Random().nextInt(canvas.getHeight());
        location = new Point(x, y);
        food = new Food(location);
        this.canvas = canvas;
        this.group = group;
        addFood();
    }

    /**
     * If the piece of food is eaten (isEaten), then the food dissapears and is added
     * to a new spot on the canvas.
     */
     public void foodEaten(boolean isEaten) {
        if (isEaten) {
            newLocation();
            addFood();
        }
     }

    /**
     * Removes piece of food from canvas
     */

    /**
     * Adds piece of food to the canvas
     */
    public void addFood() {
        food = new Food(location);
        group.add(food);
    }

    /** 
     * Sets the piece of food to a new random spot on the canvas. Will have to add
     * ability to see where snake is and not place food there.
     */
    public void newLocation() {
        x = new Random().nextInt(canvas.getWidth());
        y = new Random().nextInt(canvas.getHeight());
        location = new Point(x, y);
        food.setCenter(location);
    }

}
