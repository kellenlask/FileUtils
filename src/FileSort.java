//Creation Date: 12/8/14
//Author: Kellen Lask
//Designed for JRE/JDK 1.8 or higher
//File Name: FileSort.java
//Last Edit: 07/17/2015 (MM/DD/YYYY) 17:15 (24HR)

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import org.apache.commons.io.FilenameUtils;


/**
 *
 * @author Kellen
 */

/*
*   This is the class for the utility which takes all the files in a given 
*   directory and sorts them into folders by filetype. The program is recursive.
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
    private static Button sortButton;
    private static final TextField fileTypes = new TextField();
    private static final TextField address = new TextField();    
    private static Text actionTarget;    

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
	removeDuplicatesButton.setOnAction((ActionEvent event) -> {
	    new RemoveDuplicateFiles(primaryStage);
	});

	batchRenameButton.setOnAction((ActionEvent event) -> {
	    new BatchRename(primaryStage);

	});

	//Action Handler: sort the files in the given directory.
	sortButton.setOnAction((ActionEvent event) -> {
	    //Update the selected directory
	    selectedDirectory = new File(address.getText());

	    if (selectedDirectory != null && selectedDirectory.isDirectory()) {
		//Clear the actionTarget
		actionTarget.setFill(Color.BLACK);
		actionTarget.setText("");
		
		//Grab the entered filetypes
		String[] extensions = UtilFunctions.parseFileTypes(fileTypes.getText());
		
		//Make an iterator of all the files in the directory
		Iterator<File> fileItr = FileUtils.iterateFiles(selectedDirectory, extensions, true);
		File organizedDir = UtilFunctions.makeDirectory(selectedDirectory, "OrganizedFiles");
		int filesSorted = 0;

		//Sort the files
		while (fileItr.hasNext()) {
		    //Grab the next file from the list
		    File f = fileItr.next();
		    
		    //Pull the file's extension
		    String ext = FilenameUtils.getExtension(f.getName());

		    //If the file has no extension, put it in a reasonably
		    //  named directory
		    if(ext.equals("")) { ext = "No Extension"; }

		    //Determine the file's destination directory
		    File dest = new File(organizedDir.getAbsolutePath() + "/" + ext);

		    try {
			//Try moving the file the normal way
			FileUtils.moveFileToDirectory(f, dest, true);

		    } catch (IOException iOException) {
			//The target directory already has a file named f.getName()
			String testAddress = dest.getAbsolutePath() + "/" + f.getName();
			File test = new File(testAddress);

			//Come up with a filename that is available 
			int i = 0;
			while(test.exists()) {
			    i++;
			    test = new File(testAddress + i);
			}

			try {
			    //Move the file with its new name
			    Files.move(f.toPath(), test.toPath(), StandardCopyOption.REPLACE_EXISTING);
			    
			} catch (IOException ex) {
			    System.out.println(ex);
			} //End Inner Try-Catch
		    } //End Try-Catch

		    //Notify the user of our progress
		    actionTarget.setText("Sorted Files: " + ++filesSorted);

		} //End While

		//Notify the user that the sorting is done.
		actionTarget.setText("Total sorted: " + filesSorted);
		
	    } else {
		//Let the user know they're a twat
		actionTarget.setFill(Color.FIREBRICK);
		actionTarget.setText("Invalid directory.");
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
	actionTarget = new Text();

	GridPane grid = GUIFactories.getCenterPane(address, sortButton, actionTarget);
	backPane.setCenter(grid);

	//----------------------------------------------------------------------
	Scene scene = new Scene(backPane, WIDTH, HEIGHT);
	return scene;
	
    } //End public Scene makeScene()
} //End FileSort
