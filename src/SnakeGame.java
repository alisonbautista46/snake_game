import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.events.Key;
import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.GraphicsGroup;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import edu.macalester.graphics.Point;

public class SnakeGame {
        public static final int CANVAS_WIDTH = 800;
        public static final int CANVAS_HEIGHT = 600;

        public static final Color YELLOW = new Color(238, 186, 76);
        public static final Color RED = new Color(227, 73, 59);
        public static final Color AQUA = new Color(35, 181, 175);
        public static final Color LIGHT_AQUA = new Color(169, 221, 217);
        public static final Color DARK_GRAY = new Color(58, 58, 60);

        private static CanvasWindow canvas;
        private Snake snake;

        private FoodManager food;

        private Collision collide;

        private Segments segments;

        private List<Segments> allSegments = new ArrayList<>();

        private WallManager wallManager;

        private boolean moveLeft;
        private boolean moveRight;
        private boolean moveUp;
        private boolean moveDown;

        private int numSegs;

        private List<Point> path;

        private GraphicsGroup group = new GraphicsGroup();
        private GraphicsGroup foodPieces = new GraphicsGroup();

        private List<Button> buttons = new ArrayList<>();
        private List<GraphicsText> screenText = new ArrayList<>();
        
    public static void main(String[] args) {   
        SnakeGame snakeGame = new SnakeGame(); 
        snakeGame.homeScreen();
    }
     
    /**
     * Main Snake game method that animates the canvas
     */
    public SnakeGame() {

        numSegs = 0;

        path = new ArrayList<>();

        moveLeft = false;
        moveRight = false;
        moveUp = false;
        moveDown = false;

        canvas = new CanvasWindow("Snake!", CANVAS_WIDTH, CANVAS_HEIGHT);

        food = new FoodManager(canvas, foodPieces);

        snake = new Snake(canvas);
        snake.setCenter(canvas.getWidth() * 0.5, canvas.getHeight() * 0.9);
        group.add(snake);

        wallManager = new WallManager(canvas);

        collide = new Collision(snake, group, foodPieces, wallManager, canvas);              
    }

    private void run() {

        canvas.add(group);
        canvas.add(foodPieces);

        canvas.onKeyDown(event-> {
            if (event.getKey() == Key.LEFT_ARROW) {
                moveRight = false;
                moveUp = false;
                moveDown = false;
                moveLeft = true;
            }
            if (event.getKey() == Key.RIGHT_ARROW) {
                moveLeft = false;
                moveUp = false;
                moveDown = false;
                moveRight = true;
            }
            if (event.getKey() == Key.UP_ARROW) {
                moveLeft = false;
                moveRight = false;
                moveDown = false;
                moveUp = true;
            }
            if (event.getKey() == Key.DOWN_ARROW) {
                moveLeft = false;
                moveRight = false;
                moveUp = false;
                moveDown = true;
            }
        });   

        canvas.animate(() -> {
            checkForCollision();

            if(moveLeft) { 
                addingSegments(path);
                food.foodEaten(collide.eatsFood());
                snake.addToPath(path);
                snake.moveLeft();
            }
            if(moveRight) {
                addingSegments(path);
                food.foodEaten(collide.eatsFood());
                snake.addToPath(path);
                snake.moveRight();
            }
            if(moveUp) {
                addingSegments(path);
                food.foodEaten(collide.eatsFood());
                snake.addToPath(path);
                snake.moveUp();
            }
            if(moveDown) {
                addingSegments(path);
                food.foodEaten(collide.eatsFood());
                snake.addToPath(path);
                snake.moveDown();
            }
        });    

    }

    private void addingSegments(List<Point> path) {
        if(collide.eatsFood2()) {
            numSegs++;
            segments = new Segments(snake, path, numSegs, group);
            segments.addToGroup();
            allSegments.add(segments);
        }
        for(Segments segs : allSegments) {
            segs.follow();
        }
    }

       


    private void checkForCollision() {
        if (collide.wallCollision()) {
            System.out.println("-----> THERE HAS BEEN A COLLISION");
            System.exit(0);
        }
        if (collide.snakeCollision()){
            segments.removeAll();
            canvas.remove(segments);
        }
    }

    // ---------------------------------------

    // >>>>> HOME SCREEN RELATED METHODS <<<<<

    // ---------------------------------------

    private void homeScreen() {
        canvas.removeAll();
        levelButtons();
        welcomeText();
        startButton();
    }

    private void welcomeText() {
        GraphicsText title = new GraphicsText();
        title.setText("SNAKE GAME");
        title.setFillColor(DARK_GRAY);
        title.setCenter(CANVAS_WIDTH / 5, CANVAS_HEIGHT / 3);
        title.setFont(FontStyle.BOLD, 80);
        canvas.add(title);
        screenText.add(title);

        GraphicsText chooseLevel = new GraphicsText();
        chooseLevel.setText("Choose your level");
        chooseLevel.setFillColor(DARK_GRAY);
        chooseLevel.setCenter(CANVAS_WIDTH / 5, 2 * CANVAS_HEIGHT / 3);
        chooseLevel.setFont(FontStyle.BOLD, 60);
        canvas.add(chooseLevel);
        screenText.add(chooseLevel);
    }

    private void startButton() {
        Button start = new Button("click to start game");
        start.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
        canvas.add(start);
        buttons.add(start);

        start.onClick(() -> {
            for (Button button : buttons) {
                canvas.remove(button);
            }
            for (GraphicsText text : screenText) {
                canvas.remove(text);
            }
            run();
        });
    }

    private void levelButtons() {
        Button basic = new Button("No obstacles");
        basic.setPosition(50, 3 * CANVAS_HEIGHT / 4);
        canvas.add(basic);
        buttons.add(basic);
        basic.onClick(() -> wallManager.removeWalls());

        Button borders = new Button("Borders");
        borders.setPosition(200, 3 * CANVAS_HEIGHT / 4);
        canvas.add(borders);
        buttons.add(borders);
        borders.onClick(() -> {
            wallManager.removeWalls();
            wallManager.generateBorders();
        });

        Button doors = new Button("Doors");
        doors.setPosition(300, 3 * CANVAS_HEIGHT / 4);
        canvas.add(doors);
        buttons.add(doors);
        doors.onClick(() -> {
            wallManager.removeWalls();
            wallManager.generateDoors();
        });

        Button simpleMaze = new Button("simple Maze");
        simpleMaze.setPosition(400, 3 * CANVAS_HEIGHT / 4);
        canvas.add(simpleMaze);
        buttons.add(simpleMaze);
        simpleMaze.onClick(() -> {
            wallManager.removeWalls();
            wallManager.generateSimpleMaze();
        });

        Button harderMaze = new Button("Not So Simple Maze");
        harderMaze.setPosition(550, 3 * CANVAS_HEIGHT / 4);
        canvas.add(harderMaze);
        buttons.add(harderMaze);
        harderMaze.onClick(() -> {
            wallManager.removeWalls();
            wallManager.generateHarderMaze();
        });
    }
}
