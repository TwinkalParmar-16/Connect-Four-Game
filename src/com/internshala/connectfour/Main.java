package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {


    private com.internshala.connectfour.Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.FXML"));

        GridPane rootGridPane = loader.load();

        controller = loader.getController();
        controller.createPlayGround();
        //calling menu
        MenuBar menuBar = createMenu();

        //so that menu bar can cover whole the space
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        Pane menuPane = (Pane)rootGridPane.getChildren().get(0);//getting first child of the gridpane
        //we need to add menu in pane
        menuPane.getChildren().add(menuBar);

        Scene scene = new Scene(rootGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.show();
    }


    private MenuBar createMenu(){

        Menu fileMenu = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event ->{
            resetGame();
        });
        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(event ->{
            resetGame();
        });
        //providing seprator
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        MenuItem exitGame = new MenuItem("Exit Game");
        exitGame.setOnAction(event ->{
            exitGame();
        });

        //adding to the file menu
        fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);



        //help menu bar
        Menu helpMenu = new Menu("Help");
        MenuItem aboutGame = new MenuItem("About Game");
        aboutGame.setOnAction(event ->{
            aboutConnect4();
        });
        //providing seprator
        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event ->{
            aboutMe();
        });
        //adding to the file menu
        helpMenu.getItems().addAll(aboutGame,separatorMenuItem1,aboutMe);


        //adding to the menubar

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;
    }

    private void aboutMe() {
        //creating pop up dialog box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Me");
        alert.setHeaderText("Its me");
        alert.setContentText("Ho i am twinkal and i don't "+"know god anf i m the unlucky girl in the world."+"Nobody can have worst life then "+"me so please be happy that you are not twinkal");
        alert.show();

    }

    private void aboutConnect4() {
        //creating pop up dialog box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four Game");
        alert.setHeaderText("How to play?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column,"+" six-row vertically suspended grid."+" The pieces fall straight down,"+" occupying the next available space within the column."+" The objective of the game is to be the first to form a horizontal, vertical,"+" or diagonal line of four of one's own discs."+" Connect Four is a solved game."+" The first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        controller.resetGame();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
