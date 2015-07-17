//This class's purpose is to organize the specified files in a specified directory into subdirectories by file type.
//Creation Date: 12/8/14
//Author: Kellen Lask
//Designed for JRE/JDK 8 or higher
//File Name: FileSort.java
//Last Edit: 12/15/2014 (MM/DD/YYYY) 22:15 (24HR)

import java.io.File;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

//Make a main class that extends application, then instantiates the remove duplicates class with the primaryStage, then each class can simply pass the
//stage back and forth ad infinitum
/**
 *
 * @author Kellen
 */
public class FileSort {
//******************************************************************************
//	Fields
//******************************************************************************
    //Constants

    private static final int WIDTH = 550;
    private static final int HEIGHT = 350;
    private static final String BUTTON_STYLE = "-fx-background-color: #336699;";

    //Other
    private static File selectedDirectory;

    //JavaFX Fields
    private static Button removeDuplicatesButton;
    private static Button batchRenameButton;
    private static TextField fileTypes = new TextField();
    private static Button sortButton;
    private static Text actiontarget;
    private static TextField address = new TextField();

//******************************************************************************
//	Constructor
//******************************************************************************
    public FileSort(Stage primaryStage) {
	//Setup the PrimaryStage and make it visible. 	
	Scene scene = makeScene();

	primaryStage.setTitle("File Utilities");
	primaryStage.setScene(scene);

	addActionHandlers(primaryStage);

	primaryStage.show();

    } //End public FileSort()

//******************************************************************************
//	Action Handlers
//******************************************************************************
    public static void addActionHandlers(Stage primaryStage) {
	//Action Handler: Sort Files Button Pressed.
	removeDuplicatesButton.setOnAction((ActionEvent event) -> {
	    new RemoveDuplicateFiles(primaryStage);
	});

	batchRenameButton.setOnAction((ActionEvent event) -> {
	    new BatchRename(primaryStage);

	});

	//Action Handler: sort the files in the directory.
	sortButton.setOnAction((ActionEvent e) -> {
	    selectedDirectory = new File(address.getText());

	    if (selectedDirectory != null) {
		String[] extensions = null;

		if (!fileTypes.getText().equals("")) {
		    extensions = UtilFunctions.parseFileTypes(fileTypes.getText());
		}

		Iterator<File> fileItr = FileUtils.iterateFiles(selectedDirectory, extensions, true);

		int filesSorted = UtilFunctions.sortFiles(fileItr, selectedDirectory);

		if (filesSorted == -1) {
		    actiontarget.setFill(Color.FIREBRICK);
		    actiontarget.setText("There was a problem, sorry.");
		} else {
		    actiontarget.setFill(Color.BLACK);
		    actiontarget.setText("Sorted Files: " + filesSorted);
		}

	    } else {
		actiontarget.setFill(Color.FIREBRICK);
		actiontarget.setText("Invalid selection.");
	    }
	});

    } //End public static void addActionHandlers()

//******************************************************************************
//	GUI
//******************************************************************************    
    public static Scene makeScene() {
	//Setup the main BorderPane (Holds all the things)
	BorderPane backPane = new BorderPane();

	//----------------------------------------------------------------------
	//Setup the Main Menu Buttons & Pane
	//Make the buttons
	removeDuplicatesButton = new Button("Remove Duplicates");
	batchRenameButton = new Button("Batch Rename Files");

	//Make the buttons' container
	HBox mainButtons = new HBox();
	mainButtons.setPadding(new Insets(15, 12, 15, 12));
	mainButtons.setSpacing(10);
	mainButtons.setStyle(BUTTON_STYLE);

	//Put the buttons in the container
	mainButtons.getChildren().add(removeDuplicatesButton);
	mainButtons.getChildren().add(batchRenameButton);

	//Attach the container to the main pane
	backPane.setTop(mainButtons);

	//----------------------------------------------------------------------
	//Setup the filetypes box
	HBox fileTypePane = GUIFactories.getFileTypeBox(fileTypes);
	backPane.setBottom(fileTypePane);

	//----------------------------------------------------------------------
	//Set filetype presets menu
	VBox preSets = GUIFactories.getPresets(fileTypes);
	backPane.setRight(preSets);

	//----------------------------------------------------------------------
	//Setup the center pane
	sortButton = new Button("Sort Files");
	actiontarget = new Text();

	GridPane grid = GUIFactories.getCenterPane(address, sortButton, actiontarget);
	backPane.setCenter(grid);

	//----------------------------------------------------------------------
	Scene scene = new Scene(backPane, WIDTH, HEIGHT);
	return scene;
	
    } //End public Scene makeScene()    

} //End FileSort
