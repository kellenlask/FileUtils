
import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Kellen
 */
public class BatchRename {
//Fields

    private static File selectedDirectory;

//JavaFX Fields
    private static Button removeDuplicatesButton;
    private static Button sortButton;
    private static Button browseButton;
    private static Button renameButton;
    private static Text actiontarget;
    private static TextField address;
    private static TextField prefix;
    private static TextField postfix;
    private static TextField renameFromThis;
    private static TextField renameToThis;
    private static ProgressIndicator pin;

//Constructor
    public BatchRename(Stage primaryStage) {
	//Setup the PrimaryStage and make it visible. 	
	Scene scene = makeScene();

	primaryStage.setTitle("File Utilities");
	primaryStage.setScene(scene);

	addActionHandlers(primaryStage);

	primaryStage.show();

    } //End public BatchRename()

//Logistical Methods
    public static void addActionHandlers(Stage primaryStage) {
	//Action Handler: Sort Files Button Pressed.
	removeDuplicatesButton.setOnAction((ActionEvent event) -> {
	    new RemoveDuplicateFiles(primaryStage);
	});

	sortButton.setOnAction((ActionEvent event) -> {
	    new FileSort(primaryStage);
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

	renameButton.setOnAction((ActionEvent event) -> { //Lambda expression to perform the batch renaming

	    try {
		//Find all the files that match the "where" field
		File[] files = selectedDirectory.listFiles();

		pin.setProgress(-1);

		int filesChanged = UtilFunctions.batchRename(files, prefix.getText(), postfix.getText(), renameFromThis.getText(), renameToThis.getText());

		pin.setProgress(100);

		if (filesChanged >= 0) {
		    actiontarget.setFill(Color.BLACK);
		    actiontarget.setText("Files changed: " + filesChanged);

		} else {
		    actiontarget.setFill(Color.FIREBRICK);
		    actiontarget.setText("Some file names may not have been changed.");

		}

	    } catch (Exception e) {
		actiontarget.setFill(Color.FIREBRICK);
		actiontarget.setText("Please select a directory.");
	    }
	});

    } //End public static void addActionHandlers()

    public static Scene makeScene() {
	//Setup the BorderPane
	BorderPane backPane = new BorderPane();

	//Setup the Main Menu Buttons & Pane
	removeDuplicatesButton = new Button("Remove Duplicates");
	sortButton = new Button("Sort Files");

	HBox mainButtons = new HBox();
	mainButtons.setPadding(new Insets(15, 12, 15, 12));
	mainButtons.setSpacing(10);
	mainButtons.setStyle("-fx-background-color: #336699;");

	mainButtons.getChildren().add(removeDuplicatesButton);
	mainButtons.getChildren().add(sortButton);
	mainButtons.getChildren().add(pin);

	backPane.setTop(mainButtons);

	//Setup the Rows (There'll be 5 rows) 
	VBox vbox = new VBox(20);
	vbox.setPadding(new Insets(25, 25, 25, 25));

	//Add Title
	Text title = new Text("Fill in Parameters:");
	title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	vbox.getChildren().add(title);

	//(1) Setup the prefix/postfix row
	Text preLabel = new Text("Prefix: ");
	Text postLabel = new Text("Postfix: ");

	prefix = new TextField();
	prefix.setPrefWidth(200);

	postfix = new TextField();
	postfix.setPrefWidth(200);

	HBox afixRow = new HBox(10);
	afixRow.getChildren().add(preLabel);
	afixRow.getChildren().add(prefix);
	afixRow.getChildren().add(postLabel);
	afixRow.getChildren().add(postfix);

	vbox.getChildren().add(afixRow);

	//(2) Setup the rename row row
	Text from = new Text("Rename: ");
	Text to = new Text(" to ");

	renameFromThis = new TextField();
	renameFromThis.setPrefWidth(200);

	renameToThis = new TextField();
	renameToThis.setPrefWidth(200);

	HBox renameRow = new HBox(10);
	renameRow.getChildren().add(from);
	renameRow.getChildren().add(renameFromThis);
	renameRow.getChildren().add(to);
	renameRow.getChildren().add(renameToThis);

	vbox.getChildren().add(renameRow);

	//(4) Setup the working directoy row
	Text addressLabel = new Text("In: ");

	address = new TextField();
	address.setPrefWidth(400);

	browseButton = new Button("...");
	browseButton.setMaxWidth(Double.MAX_VALUE);

	HBox addressBox = new HBox(10);
	addressBox.getChildren().add(addressLabel);
	addressBox.getChildren().add(address);
	addressBox.getChildren().add(browseButton);

	vbox.getChildren().add(addressBox);

	//(5) Setup the Go button & label row
	renameButton = new Button("Batch Rename");
	actiontarget = new Text();

	HBox hbBtn2 = new HBox(10);
	hbBtn2.getChildren().add(renameButton);
	hbBtn2.getChildren().add(actiontarget);

	vbox.getChildren().add(hbBtn2);

	backPane.setCenter(vbox);

	Scene scene = new Scene(backPane, 500, 300);
	return scene;
    } //End public Scene makeScene()

    public static void setProgressIndicator() {
	pin = new ProgressIndicator(0);

//		pin.progressProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> ov, Number t, Number newValue) {
//				// If progress is 100% then show Text
//				if (newValue.doubleValue() >= 1) {
//					// Apply CSS so you can lookup the text
//					pin.applyCss();
//					Text text = (Text) pin.lookup(".text.percentage");
//					// This text replaces "Done"
//					text.setText("");
//				}
//			}
//		});
    }

}//End public class BatchRename
