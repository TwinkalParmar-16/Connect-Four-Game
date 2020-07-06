package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {



	//game rules
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;
	private static final String discColor1 ="#24303E";
	private static final String discColor2 = "#4CAA88";

	//variables
	private static String PLAYER_ONE ="Player One";
	private static String PLAYER_TWO ="Player Two";
	private static boolean isPlayerOneTurn = true;

	//creating 2d arrary of disc type structural changes
	private Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS];

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane;

	@FXML
	public Label playerNameLabel;

	@FXML
	public Button Set_name_Btn;

	@FXML
	public TextField Player_One;

	@FXML
	public TextField Player_Two;


	//this falg will be used to avoid the multipledisc insertion by samr user
	private boolean isAllowedToInsert = true;//by default we are allowing to insert

	@Override
	public void initialize(URL location, ResourceBundle resources) {



	}

	private void setName() {
		System.out.println("Set name method");

		if(!(  Player_One.getText().isEmpty()  || Player_One.getText().isEmpty() ) ) {
			String input1 = Player_One.getText();
			String input2 = Player_Two.getText();
			System.out.println(input1);
			System.out.println(input2);
			PLAYER_ONE = input1;
			PLAYER_TWO = input2;
		}else{
			return;
		}

	}


	public void createPlayGround(){


		//click event for set names buttons
		Set_name_Btn.setOnAction(event ->{
			System.out.println("hi hellow");
			setName();

		});

		//calling 2d circle cut grid
		Shape rectangleWithHoles = createGameStructuralGrid();

		//attaching with gridpane
		rootGridPane.add(rectangleWithHoles,0,1);
		//need to call this method within main.java

		List<Rectangle> rectangleList = createClickableColums();
		for(Rectangle rectangle : rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}
	}


	private Shape createGameStructuralGrid(){
		//col+1 r=1 to increase the rectangle area
		Shape rectangleWithHoles = new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);


		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLUMNS;c++){

				//creating circle;
				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER/2);
				circle.setCenterX(CIRCLE_DIAMETER/2);
				circle.setCenterY(CIRCLE_DIAMETER/2);

				circle.setSmooth(true);
				//subtract from rectangle


				//for 2d circle cut
				circle.setTranslateX(c*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
				circle.setTranslateY(r*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

				rectangleWithHoles = Shape.subtract(rectangleWithHoles,circle);

			}
		}

		rectangleWithHoles.setFill(Color.WHITE);
		return rectangleWithHoles;
	}

	private List<Rectangle> createClickableColums(){

		List<Rectangle> rectangleList = new ArrayList();
		for(int col =0;col <COLUMNS;col++) {
			//creating seven rectangle to implement hover over
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER / 4);
			rectangle.setOnMouseEntered(event-> rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event-> rectangle.setFill(Color.TRANSPARENT));

			final int column =col;
			rectangle.setOnMouseClicked(event->{

				//we will allow only one disc during the animation
				if(isAllowedToInsert) {
					isAllowedToInsert =false;//so that during the disc being inserted only one disc is inserted by one player
					//we nned to make it true after the animation of falling disc has compeleted
					insertDisc(new Disc(isPlayerOneTurn), column);
				}
			});

			rectangleList.add(rectangle);
		}
		return rectangleList;
	}

	private  void insertDisc(Disc disc ,int column){

		int row = ROWS - 1;
		while (row >= 0) {

			if (getDiscIfPresented(row,column)==null)
				break;

			row--;
		}

		if (row < 0)    // If it is full, we cannot insert anymore disc
			return;

		insertedDiscsArray[row][column] = disc;   // For structural Changes: For developers
		insertedDiscsPane.getChildren().add(disc);// For Visual Changes : For Players

		disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

		int currentRow = row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
		translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4+40);

		translateTransition.setOnFinished(event->{

			//so that after the successfully insertion of a disc user is allowed to insert another disc
			isAllowedToInsert = true;

			//we need to chk game over
			if(gameEnded(currentRow,column)){
				gameOver();
				return;
			}
			isPlayerOneTurn = !isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO);
		});
		translateTransition.play();
	}

	private void gameOver() {
		String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
		System.out.println("Winner :"+winner);

		//creating dailog box
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("COnnect Four Game");
		alert.setHeaderText("Winner is : "+winner);
		alert.setContentText("Want to play again ?");

		//creating two custom button
		ButtonType yesBtn = new ButtonType("Yes");
		ButtonType noBtn =new ButtonType("No, Exit");
		Platform.runLater( () ->{

			//so that this code will run only after the animation completed


			alert.getButtonTypes().setAll(yesBtn,noBtn);

			Optional<ButtonType> btnClicked = alert.showAndWait();
			if(btnClicked.isPresent() && btnClicked.get()==yesBtn){
				//reset the game
				resetGame();
			}else{
				//exit the game
				Platform.exit();
				System.exit(0);
			}

		});

	}

	//so that main.java can call this method
	public void resetGame() {
		//remove all inserted Disc
		insertedDiscsPane.getChildren().clear();
		//arrary should be null
		for(int r=0;r<insertedDiscsArray.length;r++){

			for(int c=0;c<insertedDiscsArray[r].length;c++)
			{
				insertedDiscsArray[r][c]=null;
			}
		}
		//make the playerone turn
		isPlayerOneTurn = true;
		//creat new playGround
		createPlayGround();
	}

	private boolean gameEnded(int row,int column){

		//vertical check
		IntStream.rangeClosed(0,5);
		List<Point2D> verticalPoint = IntStream.rangeClosed(row-3,row+3).
				mapToObj( r-> new Point2D(r,column)).
				collect(Collectors.toList());

		//horizontal
		List<Point2D> horizontalPoint = IntStream.rangeClosed(column-3,column+3).
				mapToObj( col-> new Point2D(row,col)).
				collect(Collectors.toList());

		//diagonal1
		Point2D startPoint1 = new Point2D(row-3,column+3);
		List<Point2D> daigonalPoints = IntStream.rangeClosed(0,6)
				.mapToObj( i -> startPoint1.add(i,-i))
				.collect(Collectors.toList());

		//diagona2
		Point2D startPoint2 =  new Point2D(row-3,column-3);
		List<Point2D> daigonal2Points = IntStream.rangeClosed(0,6)
				.mapToObj(i -> startPoint2.add(i,i))
				.collect(Collectors.toList());


		boolean isEnded = checkCombination(verticalPoint) || checkCombination(horizontalPoint)
				|| checkCombination(daigonalPoints) || checkCombination(daigonal2Points);

		return isEnded;
	}

	private boolean checkCombination(List<Point2D> verticalPoint) {
		int chain=0;
		for(Point2D point :verticalPoint){

			int rowIndexForArray =(int)point.getX();
			int columnIndexArray = (int)point.getY();

			Disc disc = getDiscIfPresented(rowIndexForArray,columnIndexArray);
			if(disc!=null && disc.isPlayerOneMove == isPlayerOneTurn){
				chain++;
				if(chain==4){
					return true;
				}
			}else{
				chain =0;
			}
		}
		return false;
	}

	private Disc getDiscIfPresented(int row, int column) {
		if(row>=ROWS || row<0 || column >=COLUMNS || column <0)
			return null;
		return insertedDiscsArray[row][column];
	}

	private static class Disc extends Circle{
		private final boolean isPlayerOneMove;


		private Disc(boolean isPlayerOneTurn) {
			this.isPlayerOneMove = isPlayerOneTurn;
			setFill(isPlayerOneTurn ? Color.valueOf(discColor1):Color.valueOf(discColor2));
			setRadius(CIRCLE_DIAMETER/2);
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterX(CIRCLE_DIAMETER/2);
		}
	}


}
