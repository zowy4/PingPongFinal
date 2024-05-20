package singleton.pingpongfinal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PongController {
    @FXML
    private Circle ball;

    @FXML
    private Rectangle paddleLeft;

    @FXML
    private Rectangle paddleRight;

    @FXML
    private Label scoreLeft;

    @FXML
    private Label scoreRight;

    @FXML
    private Pane gamePane;

    private double ballX = 2;
    private double ballY = 2;
    private double paddleSpeed = 5;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;

    private int pointsLeft = 0;
    private int pointsRight = 0;

    private static final double PANE_WIDTH = 618.0;
    private static final double PANE_HEIGHT = 338.0;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            moveBall();
            movePaddles();
            checkCollisions();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void setScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
    }

    private void moveBall() {
        ball.setCenterX(ball.getCenterX() + ballX);
        ball.setCenterY(ball.getCenterY() + ballY);
    }

    private void movePaddles() {
        if (wPressed && paddleLeft.getY() > 0) {
            paddleLeft.setY(paddleLeft.getY() - paddleSpeed);
            PongClient.sendMessage("LEFT_UP");
        }
        if (sPressed && paddleLeft.getY() < PANE_HEIGHT - paddleLeft.getHeight()) {
            paddleLeft.setY(paddleLeft.getY() + paddleSpeed);
            PongClient.sendMessage("LEFT_DOWN");
        }
        if (upPressed && paddleRight.getY() > 0) {
            paddleRight.setY(paddleRight.getY() - paddleSpeed);
            PongClient.sendMessage("RIGHT_UP");
        }
        if (downPressed && paddleRight.getY() < PANE_HEIGHT - paddleRight.getHeight()) {
            paddleRight.setY(paddleRight.getY() + paddleSpeed);
            PongClient.sendMessage("RIGHT_DOWN");
        }
    }

    private void checkCollisions() {
        if (ball.getBoundsInParent().intersects(paddleLeft.getBoundsInParent()) ||
                ball.getBoundsInParent().intersects(paddleRight.getBoundsInParent())) {
            ballX *= -1;
        }

        if (ball.getCenterX() <= ball.getRadius()) {
            pointsRight++;
            updateScore();
            resetBall();
        }

        if (ball.getCenterX() >= PANE_WIDTH - ball.getRadius()) {
            pointsLeft++;
            updateScore();
            resetBall();
        }

        if (ball.getCenterY() <= ball.getRadius() || ball.getCenterY() >= PANE_HEIGHT - ball.getRadius()) {
            ballY *= -1;
        }
    }

    private void updateScore() {
        scoreLeft.setText(String.valueOf(pointsLeft));
        scoreRight.setText(String.valueOf(pointsRight));
    }

    private void resetBall() {
        ball.setCenterX(PANE_WIDTH / 2);
        ball.setCenterY(PANE_HEIGHT / 2);
        ballX *= -1;
    }

    public void handleServerMessage(String message) {
        switch (message) {
            case "LEFT_UP":
                paddleLeft.setY(paddleLeft.getY() - paddleSpeed);
                break;
            case "LEFT_DOWN":
                paddleLeft.setY(paddleLeft.getY() + paddleSpeed);
                break;
            case "RIGHT_UP":
                paddleRight.setY(paddleRight.getY() - paddleSpeed);
                break;
            case "RIGHT_DOWN":
                paddleRight.setY(paddleRight.getY() + paddleSpeed);
                break;
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.W) {
            wPressed = true;
        }
        if (event.getCode() == KeyCode.S) {
            sPressed = true;
        }
        if (event.getCode() == KeyCode.UP) {
            upPressed = true;
        }
        if (event.getCode() == KeyCode.DOWN) {
            downPressed = true;
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.W) {
            wPressed = false;
        }
        if (event.getCode() == KeyCode.S) {
            sPressed = false;
        }
        if (event.getCode() == KeyCode.UP) {
            upPressed = false;
        }
        if (event.getCode() == KeyCode.DOWN) {
            downPressed = false;
        }
    }
}

