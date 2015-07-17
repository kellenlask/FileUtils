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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;


//Make a main class that extends application, then instantiates the remove duplicates class with the primaryStage, then each class can simply pass the
//stage back and forth ad infinitum

/**
 *
 * @author Kellen
 */

public class FileSort {
//Fields
    private static File selectedDirectory;
    
//JavaFX Fields
    private static Button removeDuplicatesButton;
    private static Button batchRenameButton;
    private static TextField fileTypes;
    private static Button all;
    private static Button images;
    private static Button music;
    private static Button documents;
    private static Button video;
    private static Button browseButton;
    private static Button sortButton;
    private static Text actiontarget;
    private static TextField address;    
    
//Constructor
    public FileSort(Stage primaryStage) {
		//Setup the PrimaryStage and make it visible. 	
		Scene scene = makeScene();

		primaryStage.setTitle("File Utilities");
		primaryStage.setScene(scene);	

		addActionHandlers(primaryStage);

		primaryStage.show();
	
    } //End public FileSort()
    
//Logistical Methods
    public static void addActionHandlers(Stage primaryStage) {
	//Action Handler: Sort Files Button Pressed.
	removeDuplicatesButton.setOnAction((ActionEvent event) -> {
	    new RemoveDuplicateFiles(primaryStage);
	});
	
	batchRenameButton.setOnAction((ActionEvent event) -> {
		new BatchRename(primaryStage);
	
	});

	//Action Handler: get the directory from the user.	
	browseButton.setOnAction((ActionEvent event) -> { //Lambda expression: get the directory from the user.
	    DirectoryChooser directoryChooser = new DirectoryChooser();

	    directoryChooser.setTitle("Select Directory");
	    String currentDir = System.getProperty("user.dir") + File.separator;
	    directoryChooser.setInitialDirectory(new File(currentDir));

	    selectedDirectory = directoryChooser.showDialog(null);

	    if(selectedDirectory != null) {
		    address.setText(selectedDirectory.getPath());
	    }
	});

	//Action Handler: sort the files in the directory.
	sortButton.setOnAction((ActionEvent e) -> { //Lambda expression: remove duplicate files.
	    if(selectedDirectory != null) {
		String[] extensions = null;

		if(!fileTypes.getText().equals("")) {
		    extensions = UtilFunctions.parseArray(fileTypes.getText());
		}
		
		Iterator<File> fileItr = FileUtils.iterateFiles(selectedDirectory, extensions, true);
		
		int filesSorted = UtilFunctions.sortFiles(fileItr, selectedDirectory);
		
		if(filesSorted == -1) {
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

	//Action Handler: set file types to [all]
	all.setOnAction((ActionEvent event) -> {
	    fileTypes.setText("");
	});

	//Action Handler: set file types to image types
	images.setOnAction((ActionEvent event) -> {
	    fileTypes.setText("gif, jpg, jpeg, png, ico");		
	});

	//Action Handler: set file types to music types
	music.setOnAction((ActionEvent event) -> {
	    fileTypes.setText("flac, oga, wma, mp3, acc");		
	});

	//Action Handler: set file types to document types
	documents.setOnAction((ActionEvent event) -> {
	    fileTypes.setText("pdf, doc, docx, xls, xlsx, ppt, pptx");		
	});

	//Action Handler: set file types to video types
	video.setOnAction((ActionEvent event) -> {
	    fileTypes.setText("avi, wmv, mpeg, mpg, mkv, flv, ogv, mp4");		
	});
	
    } //End public static void addActionHandlers()
    
    public static Scene makeScene() {
		//Setup the BorderPane
		BorderPane backPane = new BorderPane();	

		//Setup the Main Menu Buttons & Pane
		removeDuplicatesButton = new Button("Remove Duplicates");
		batchRenameButton = new Button("Batch Rename Files");

		HBox mainButtons = new HBox();
		mainButtons.setPadding(new Insets(15, 12, 15, 12));
		mainButtons.setSpacing(10);
		mainButtons.setStyle("-fx-background-color: #336699;");

		mainButtons.getChildren().add(removeDuplicatesButton);
		mainButtons.getChildren().add(batchRenameButton);

		backPane.setTop(mainButtons);


		//Setup the Bottom TextField
		fileTypes = new TextField();
		fileTypes.setPrefWidth(300);		

		HBox fileTypePane = new HBox();
		fileTypePane.setPadding(new Insets(15, 12, 15, 12));
		fileTypePane.setSpacing(10);

		fileTypePane.getChildren().add(new Text("File Types:"));
		fileTypePane.getChildren().add(fileTypes);		

		backPane.setBottom(fileTypePane);


		//Setup the FileType Pre-set Buttons
		all = new Button("All");
		all.setMaxWidth(Double.MAX_VALUE);

		images = new Button("Images");
		images.setMaxWidth(Double.MAX_VALUE);

		music = new Button("Music");
		music.setMaxWidth(Double.MAX_VALUE);

		documents = new Button("Documents");
		documents.setMaxWidth(Double.MAX_VALUE);

		video = new Button("Video");
		video.setMaxWidth(Double.MAX_VALUE);


		VBox preSets = new VBox();
		preSets.setPadding(new Insets(15, 12, 15, 12));
		preSets.setSpacing(10);
		preSets.getChildren().add(all);
		preSets.getChildren().add(images);
		preSets.getChildren().add(music);
		preSets.getChildren().add(documents);
		preSets.getChildren().add(video);

		backPane.setRight(preSets);

		//Setup the GridPane	
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Select Working Directory:");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		address = new TextField();
		address.setPrefWidth(300);
		grid.add(address, 1, 1);

		browseButton = new Button("...");
		browseButton.setMaxWidth(Double.MAX_VALUE);
		HBox hbBtn = new HBox(20);
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(browseButton);
		grid.add(hbBtn, 2, 1);

		sortButton = new Button("Sort Files");
		HBox hbBtn2 = new HBox(10);
		hbBtn2.getChildren().add(sortButton);
		hbBtn2.setAlignment(Pos.CENTER_LEFT);

		actiontarget = new Text();
		hbBtn2.getChildren().add(actiontarget);

		grid.add(hbBtn2, 1, 3);

		backPane.setCenter(grid);



		Scene scene = new Scene(backPane, 500, 300);
		return scene;
    } //End public Scene makeScene()    
    
} //End FileSort
