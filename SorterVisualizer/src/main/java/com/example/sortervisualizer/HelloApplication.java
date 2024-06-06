package com.example.sortervisualizer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class HelloApplication extends Application {
    private static final int arrSize = 500;
    private double height;
    private double width;
    static StackPane pane = new StackPane();
    private int[] arr;
    static HBox hbox = new HBox(1);  // Moved HBox initialization to class level
    private Timeline timeline;
    private AudioClip beep;

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenbounds = Screen.getPrimary().getBounds();
        width = screenbounds.getWidth();
        height = screenbounds.getHeight() - 200;

        pane.setStyle("-fx-background-color: #111111");
        Scene scene = new Scene(pane, width, height);
        stage.setTitle("Sorting Visualizer");
        stage.setScene(scene);
        stage.show();

//        beep = new AudioClip(getClass().getResource("stop-13692.mp3").toString());

        arr = createArr(arrSize);
        System.out.println(Arrays.toString(arr));
        hbox.setTranslateY(arr[arrSize-1] / 10);

        displayArr(arr);

        //key handler
        scene.setOnKeyPressed(e -> {
            //shuffle array on 'r'
            if(e.getCode() == KeyCode.R) {
                shuffleArray(arr);
                displayArr(arr);
            }
            //begin sort array 'space'
            if(e.getCode() == KeyCode.SPACE){
                if (timeline != null) {
                    timeline.stop();
                }
                startBubbleSort();
            }
        });
    }

    private void shuffleArray(int[] arr) {
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static int[] createArr(int arrSize){
        int[] arr = new int[arrSize];
        for(int i = 0; i < arr.length; i++){
            arr[i] = (i + 1) * 1000/arrSize;
        }
        return arr;
    }

    public static void displayArr(int[] arr){
        hbox.getChildren().clear();  // Clear the HBox before adding new rectangles
        hbox.setAlignment(Pos.BASELINE_CENTER);
        for (int value : arr) {
            double hue = (double) value / arrSize * 360;
            Color color = Color.hsb(hue, 1 , 1);
            Rectangle rectangle = new Rectangle(1500/arrSize, value, color);
            hbox.getChildren().add(rectangle);
        }
        if (!pane.getChildren().contains(hbox)) {
            pane.getChildren().add(hbox);  // Add the HBox to the pane only if it's not already added
        }
    }

    public static void main(String[] args) {
        launch();
    }

    private void startBubbleSort() {
         timeline = new Timeline();
         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), e ->{
             boolean sorted = bubbleStep(arr);
             displayArr(arr);
             if(sorted)
                 timeline.stop();
             else
                 beep.play();
         }));
         timeline.play();
    }

    public static boolean bubbleStep(int[] arr){
        boolean sorted = true;
        for (int i = 0; i < arr.length - 1; i++){
            if(arr[i] > arr[i+1]) {
                int temp = arr[i];
                arr[i] = arr[i+1];
                arr[i + 1] = temp;
                sorted = false;
                break;
            }
        }
        return sorted;
    }
}
