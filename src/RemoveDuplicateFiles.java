//Creation Date: 12/8/14
//Author: Kellen Lask
//Designed for JRE/JDK 1.8 or higher
//File Name: RemoveDuplicateFiles.java
//Last Edit: 07/17/2015 (MM/DD/YYYY) 17:15 (24HR)

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kellen
 */
public class RemoveDuplicateFiles {
//******************************************************************************
//	Fields
//******************************************************************************
    //Constants

    private static final String BUTTON_STYLE = "-fx-background-color: #336699;";
    private static final int WIDTH = 550;
    private static final int HEIGHT = 350;

    //Other Fields
    private static File selectedDirectory;

    //JavaFX Fields
    private static Button sortFilesButton;
    private static Button batchRenameButton;
    private static Button rmvButton;    
    private static final TextField fileTypes = new TextField();
    private static final TextField address = new TextField();
    private static Text actionTarget;    

//******************************************************************************
//	Constructor
//******************************************************************************
    public RemoveDuplicateFiles(Stage primaryStage) {
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
	//Sort Files Button Pressed.
	sortFilesButton.setOnAction((ActionEvent event) -> {
	    //Move to the SortFiles window
	    new FileSort(primaryStage);
	});

	//Batch Rename Button Pressed
	batchRenameButton.setOnAction((ActionEvent event) -> {
	    //Move to the BatchRename window
	    new BatchRename(primaryStage);
	});

	//Action Handler: remove the duplicate files in the directory.
	rmvButton.setOnAction((ActionEvent event) -> {
	    //Clear the actionTarget
	    actionTarget.setFill(Color.BLACK);
	    actionTarget.setText("");
	    
	    //Make sure we have the right path
	    selectedDirectory = new File(address.getText());

	    if (selectedDirectory != null && selectedDirectory.isDirectory()) {
		//Grab the list of file types from the textbox
		String[] extensions = UtilFunctions.parseFileTypes(fileTypes.getText());

		//Grab the list of files in the selectedDirectory
		List<File> files = (List<File>) FileUtils.listFiles(selectedDirectory, extensions, true);
		HashSet<String> hashCodes = new HashSet<>();
		ArrayList<File> duplicates = new ArrayList<>();

		//Progress reporting values
		actionTarget.setFill(Color.BLACK);
		int totalFileCount = files.size();
		int filesProcessed = 0;

		//Find the duplicate files
		for (File f : files) {
		    try {
			//Update the status
			filesProcessed++;
			actionTarget.setText("Processing file " + filesProcessed + " of " + totalFileCount);

			//Grab the file's hash code
			String hash = UtilFunctions.makeHash(f);

			//If we already have a file matching that hash code
			if (hashCodes.contains(hash)) {
			    //Add the file to the list of files to be deleted
			    duplicates.add(f);
			} else {
			    hashCodes.add(hash);
			}

		    } catch (Exception except) {
		    }
		} //End for

		//Progress reporting
		filesProcessed = 0;
		totalFileCount = duplicates.size();
		Iterator<File> itr = duplicates.iterator();

		//Remove the duplicate files
		while (itr.hasNext()) {
		    try {
			//Update the status
			filesProcessed++;
			actionTarget.setText("Deleting file " + filesProcessed + " of " + totalFileCount);

			//Grab the file
			File file = itr.next();

			if (!file.delete()) {
			    JOptionPane.showMessageDialog(null, file.getPath() + " not deleted.");
			}

		    } catch (Exception except) {
		    }
		} //End while

		actionTarget.setText("Deleted: " + filesProcessed);

	    } else {
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
	sortFilesButton = new Button("Sort Files");
	batchRenameButton = new Button("Batch Rename Files");

	HBox mainButtons = new HBox();
	mainButtons.setPadding(new Insets(15, 12, 15, 12));
	mainButtons.setSpacing(10);
	mainButtons.setStyle(BUTTON_STYLE);

	mainButtons.getChildren().add(sortFilesButton);
	mainButtons.getChildren().add(batchRenameButton);

	backPane.setTop(mainButtons);

	//----------------------------------------------------------------------
	//Setup the filetypes box
	HBox fileTypePane = GUIFactories.getFileTypeBox(fileTypes);
	backPane.setBottom(fileTypePane);

	//----------------------------------------------------------------------
	//Setup the FileType Pre-set Buttons
	VBox preSets = GUIFactories.getPresets(fileTypes);
	backPane.setRight(preSets);

	//----------------------------------------------------------------------
	//Setup the center GridPane
	rmvButton = new Button("Remove Duplicates");
	actionTarget = new Text();

	GridPane grid = GUIFactories.getCenterPane(address, rmvButton, actionTarget);
	backPane.setCenter(grid);

	//----------------------------------------------------------------------
	Scene scene = new Scene(backPane, WIDTH, HEIGHT);
	return scene;
	
    } //End public Scene makeScene()
}//End class RemoveDuplicateFiles
