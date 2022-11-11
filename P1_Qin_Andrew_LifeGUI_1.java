import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Name: Andrew Qin
 * Date: Mar 7, 2022
 * Period 1
 * 
 * Does this lab work? 
 * Any other comments?
 */

public class P1_Qin_Andrew_LifeGUI_1 extends Application implements GenerationListener{
	
	MenuItem loadButton;
	Button clearButton;
	MenuItem saveButton;
	Slider slider;
	BooleanGridPane view;
	FileChooser chooser;
	P1_Qin_Andrew_LifeModel model;
	Button nextGeneration;
	
	Text gen;
	Text genCount;
	VBox generationHolder;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		//stage init
		stage.setTitle("GridViewer");
		stage.setMinHeight(600);
		stage.setMinWidth(600);
		
		//root
		BorderPane root = new BorderPane();
		
		//menu
		MenuBar bar = new MenuBar();
		Menu menu = new Menu("File");
		bar.getMenus().add(menu);
		
		
		//load button
		loadButton = new MenuItem("Open");
		loadButton.setOnAction(e->{
			chooser = new FileChooser();
			File f = chooser.showOpenDialog(stage);

			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				StringTokenizer st = new StringTokenizer(br.readLine());
				int rows = Integer.parseInt(st.nextToken());
				int cols = Integer.parseInt(st.nextToken());

				Boolean[][] arr = new Boolean[rows][cols];

				for (int i = 0; i < rows; i++) {
					String s = br.readLine();
					StringTokenizer in = new StringTokenizer(s);
					for (int j = 0; j < cols; j++) {
						arr[i][j] = (in.nextToken().equals("X"));
					}
				}

				model.setGrid(arr);
				model.setGeneration(0);

			} catch (IOException exception) {
				exception.printStackTrace();
			}
		});
		
		//save button
		saveButton = new MenuItem("Save");
		saveButton.setOnAction(e->{
			chooser = new FileChooser();
			File f = chooser.showSaveDialog(stage);
			
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.println(model.getNumRows()+" "+model.getNumCols());
				for(int i=0; i<model.getNumRows(); i++) {
					String print = "";
					for(int j=0; j<model.getNumCols(); j++) {
						boolean curr = model.getValueAt(i, j);
						if(curr) {
							print+="X ";
						}else {
							print+="O ";
						}
					}
					pw.println(print);
				}
				pw.close();
								
			}catch(IOException exception) {
				exception.printStackTrace();
			}
		});
		
		menu.getItems().addAll(loadButton, saveButton);
		
		//bottom
		HBox bottom = new HBox();
		bottom.setBackground(new Background(new BackgroundFill(Paint.valueOf("#D3D3D3"), null, null)));
		bottom.setAlignment(Pos.CENTER);
		stage.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue <? extends Number>observable, Number oldValue, Number newValue){
	            bottom.setPrefHeight(stage.getHeight()*0.2);
	        }
		});
		
		//next generation button
		nextGeneration = new Button("Next Generation");
		nextGeneration.setOnAction(e->{
			model.setGeneration(model.getGeneration() + 1);			
		});
		
		//clear button
		clearButton = new Button("Clear");
		clearButton.setOnAction(e->{
			model.clear();
		});
		
		//slider 
		slider = new Slider(5, 120, 40);
		slider.setShowTickMarks(true);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
	         public void changed(ObservableValue <?extends Number>observable, Number oldValue, Number newValue){
	            view.setTileSize((double) newValue);
	         }
	      });
		
		//generation text
		generationHolder = new VBox();
		gen = new Text("Generation");
		genCount = new Text("0");
		generationHolder.getChildren().addAll(gen, genCount);
		generationHolder.setAlignment(Pos.CENTER);
		
		bottom.getChildren().addAll(clearButton, generationHolder, nextGeneration, slider);
		for (Node n : bottom.getChildren()) {
			HBox.setMargin(n, new Insets(5));
		}
		
		
		//grid view
		view = new BooleanGridPane();
		
		Boolean[][] data =  {{true, true, true},
							{false, true, false},
							{false, true, false}};
		
		model = new P1_Qin_Andrew_LifeModel(data);
		model.addGenerationListener(this);
		view.setModel(model);
		view.setTileSize(40);
		
		myMouseEvent handler = new myMouseEvent();
		view.setOnMouseClicked(handler);
		
		
		ScrollPane scrollThing = new ScrollPane();
		
		StackPane viewHolder = new StackPane();
		viewHolder.getChildren().add(view);
		viewHolder.setAlignment(view, Pos.CENTER);
		scrollThing.setFitToHeight(true);
        scrollThing.setFitToWidth(true);
		
		scrollThing.setContent(viewHolder);
		
		root.setCenter(scrollThing);
		root.setBottom(bottom);
		root.setTop(bar);
		
//		root.getChildren().addAll(view, bottom);
		
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		stage.show();
		
        bottom.setPrefHeight(stage.getHeight()*0.2);
	
	}
	
	class MyAnimationTimer extends AnimationTimer {
		long old = 0;

		@Override
		public void handle(long now) {
			if(now-old > 1e9) {
			
				old = now;
			}
		}
		
	}
	
	class myMouseEvent implements EventHandler<MouseEvent> {
		
		private double xPos;
		private double yPos;

		@Override
		public void handle(MouseEvent event) {
			xPos = event.getX();
			yPos = event.getY();
			
			int col = view.colForXPos(xPos);
			int row = view.rowForYPos(yPos);
			
			Rectangle curr = view.cellAtGridCoords(row, col);
			if(curr!=null) {
				boolean falseColor = curr.getFill()==view.getFalseColor();
				if(falseColor) {
					model.setValueAt(row, col, true);
				}else {
					model.setValueAt(row, col, true);
				}
			}
			
//			for(int i=row-1; i<=row+1; i++) {
//				for(int j=col-1; j<=col+1; j++) {
//					if(i==row && j==col) continue;
//					if(i<0 || i>=view.getLength()) continue;
//					if(j<0 || j>=view.getLength()) continue;
//					
//					Rectangle curr = view.cellAtGridCoords(i, j);
//					boolean falseColor = curr.getFill()==view.getFalseColor();
//					if(falseColor) {
//						view.cellChanged(i, j, false, true);
//					}else {
//						view.cellChanged(i, j, true, false);
//					}
//				}
//			}			
		}
	}

	@Override
	public void generationChanged(int oldVal, int newVal) {
		genCount.setText(""+newVal);
	}
}


