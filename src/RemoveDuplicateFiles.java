
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.swing.JOptionPane;

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
    private static final String IMAGE_FILE_TYPES = "gif, jpg, jpeg, png, ico";
    private static final String AUDIO_FILE_TYPES = "flac, oga, wma, mp3, acc";
    private static final String DOCUMENT_FILE_TYPES = "pdf, doc, docx, xls, xlsx, ppt, pptx";
    private static final String VIDEO_FILE_TYPES = "avi, wmv, mpeg, mpg, mkv, flv, ogv, mp4";
    private static final String BUTTON_STYLE = "-fx-background-color: #336699;";

    //Other Fields
    private static File selectedDirectory;

    //JavaFX Fields
    private static Button sortFilesButton;
    private static Button batchRenameButton;
    private static TextField fileTypes;
    private static Button all;
    private static Button images;
    private static Button music;
    private static Button documents;
    private static Button video;
    private static Button browseButton;
    private static Button rmvButton;
    private static Text actiontarget;
    private static TextField address;

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

	//Action Handler: get the directory from the user.	
	browseButton.setOnAction((ActionEvent event) -> { //Lambda expression: get the directory from the user.
	    DirectoryChooser directoryChooser = new DirectoryChooser();

	    directoryChooser.setTitle("Select Directory");
	    String currentDir = System.getProperty("user.dir") + File.separator;
	    directoryChooser.setInitialDirectory(new File(currentDir));

	    selectedDirectory = directoryChooser.showDialog(null);

	    if (selectedDirectory != null) {
		address.setText(selectedDirectory.getPath());
	    }
	});

	//Action Handler: remove the duplicate files in the directory.
	rmvButton.setOnAction((ActionEvent e) -> { //Lambda expression: remove duplicate files.
	    //TODO: set the selected directory from the textbox (the user could have manually changed it)
	    if (selectedDirectory != null) {
		//Grab the list of file types from the textbox
		String[] extensions = UtilFunctions.parseFileTypes(fileTypes.getText());

		try {
		    HashMap<String, String> map = UtilFunctions.getFiles(extensions, selectedDirectory);
		    ArrayList<String> duplicates = UtilFunctions.findDuplicateFiles(map);
		    int filesRemoved = UtilFunctions.removeFiles(duplicates);

		    actiontarget.setFill(Color.BLACK);
		    actiontarget.setText("Files Deleted: " + filesRemoved);
		} catch (Exception except) {
		    JOptionPane.showMessageDialog(null, "Error Occured: " + except.toString());
		}

	    } else {
		actiontarget.setFill(Color.FIREBRICK);
		actiontarget.setText("Invalid directory.");
	    }
	});

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

    } //End public static void addActionHandlers()

//******************************************************************************
//	GUI
//******************************************************************************    
    public static Scene makeScene() {
	//Setup the BorderPane
	BorderPane backPane = new BorderPane();

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

	rmvButton = new Button("Remove Duplicates");
	HBox hbBtn2 = new HBox(10);
	hbBtn2.getChildren().add(rmvButton);
	hbBtn2.setAlignment(Pos.CENTER_LEFT);

	actiontarget = new Text();
	hbBtn2.getChildren().add(actiontarget);

	grid.add(hbBtn2, 1, 3);

	backPane.setCenter(grid);

	Scene scene = new Scene(backPane, 500, 300);
	return scene;
    } //End public Scene makeScene()
}//End class RemoveDuplicateFiles
