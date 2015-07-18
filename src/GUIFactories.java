//Creation Date: 12/8/14
//Author: Kellen Lask
//Designed for JRE/JDK 1.8 or higher
//File Name: GUIFactories.java
//Last Edit: 07/17/2015 (MM/DD/YYYY) 17:15 (24HR)

import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;


/**
 *
 * @author Kellen
 */

/*
*   The purpose of this class is to contain the GUI generation code for the 
*   common GUI elements used in this application. This way, the same GUI code is
*   maintained in one central place and the related constants are defined only
*   once. Keep it DRY. 
*/
public class GUIFactories {
//******************************************************************************
//	Constants
//******************************************************************************    
    private static final String IMAGE_FILE_TYPES = "gif, jpg, jpeg, png, ico";
    private static final String AUDIO_FILE_TYPES = "flac, oga, wma, mp3, acc";
    private static final String DOCUMENT_FILE_TYPES = "pdf, doc, docx, xls, xlsx, ppt, pptx";
    private static final String VIDEO_FILE_TYPES = "avi, wmv, mpeg, mpg, mkv, flv, ogv, mp4";	
    public static final Insets PADDING_INSETS = new Insets(15, 12, 15, 12);
    
//******************************************************************************
//	Filetype Buttons
//******************************************************************************    
    public static VBox getPresets(TextField fileTypes) {
	//Setup the FileType Pre-set Buttons
	Button all = new Button("All");
	all.setMaxWidth(Double.MAX_VALUE);

	Button images = new Button("Images");
	images.setMaxWidth(Double.MAX_VALUE);

	Button music = new Button("Music");
	music.setMaxWidth(Double.MAX_VALUE);

	Button documents = new Button("Documents");
	documents.setMaxWidth(Double.MAX_VALUE);

	Button video = new Button("Video");
	video.setMaxWidth(Double.MAX_VALUE);

	//Add the buttons to a container 
	VBox preSets = new VBox();
	preSets.setPadding(PADDING_INSETS);
	preSets.setSpacing(10);
	preSets.getChildren().add(all);
	preSets.getChildren().add(images);
	preSets.getChildren().add(music);
	preSets.getChildren().add(documents);
	preSets.getChildren().add(video);
	
	//Set the button action handlers to populate the text field
	//Action Handler: set file types to [all]
	all.setOnAction((ActionEvent event) -> {
	    fileTypes.setText("");
	});

	//Action Handler: set file types to image types
	images.setOnAction((ActionEvent event) -> {
	    fileTypes.setText(IMAGE_FILE_TYPES);
	});

	//Action Handler: set file types to music types
	music.setOnAction((ActionEvent event) -> {
	    fileTypes.setText(AUDIO_FILE_TYPES);
	});

	//Action Handler: set file types to document types
	documents.setOnAction((ActionEvent event) -> {
	    fileTypes.setText(DOCUMENT_FILE_TYPES);
	});

	//Action Handler: set file types to video types
	video.setOnAction((ActionEvent event) -> {
	    fileTypes.setText(VIDEO_FILE_TYPES);
	});
	
	//Return the container
	return preSets;
	
    } //End public static VBox getPresets()
    
//******************************************************************************
//	Filetypes TextBox
//******************************************************************************    
    public static HBox getFileTypeBox(TextField fileTypes) {
	//Setup the Bottom TextField
	fileTypes.setPrefWidth(300);

	//Setup its container
	HBox fileTypePane = new HBox();
	fileTypePane.setPadding(PADDING_INSETS);
	fileTypePane.setSpacing(10);

	//Add it to the container
	fileTypePane.getChildren().add(new Text("File Types:"));
	fileTypePane.getChildren().add(fileTypes);
	
	//Return the container
	return fileTypePane;
	
    } //End public static HBox getFileTypeBox()

//******************************************************************************
//	Filetypes TextBox
//******************************************************************************
    public static GridPane getCenterPane(TextField address, Button button, Text actionTarget) {
	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(25, 25, 25, 25));

	Text scenetitle = new Text("Select Working Directory:");
	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	grid.add(scenetitle, 0, 0, 2, 1);
	
	address.setPrefWidth(300);
	grid.add(address, 1, 1);
	
	Button browseButton = new Button("...");
	browseButton.setMaxWidth(Double.MAX_VALUE);
	HBox hbBtn = new HBox(20);
	hbBtn.setAlignment(Pos.CENTER);
	hbBtn.getChildren().add(browseButton);
	grid.add(hbBtn, 2, 1);
	
	HBox hbBtn2 = new HBox(10);
	hbBtn2.getChildren().add(button);
	hbBtn2.setAlignment(Pos.CENTER_LEFT);
	
	hbBtn2.getChildren().add(actionTarget);
	
	grid.add(hbBtn2, 1, 3);
	
	//Action Handler: get the directory from the user.	
	browseButton.setOnAction((ActionEvent event) -> {
	    DirectoryChooser directoryChooser = new DirectoryChooser();

	    directoryChooser.setTitle("Select Directory");
	    String currentDir = System.getProperty("user.dir") + File.separator;
	    directoryChooser.setInitialDirectory(new File(currentDir));

	   File selectedDirectory = directoryChooser.showDialog(null);

	    if (selectedDirectory != null) {
		address.setText(selectedDirectory.getPath());
	    }
	});
	
	return grid;
	
    } //End public static GridPane getCenterPane(
    
    
    
} //End public class GUIFactories
