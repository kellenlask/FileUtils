//This program's purpose is to provide file manipulation tools: remove duplicate files, organize a file system.
//Creation Date: 12/8/14
//Author: Kellen Lask (& Joshua T. Pearson helped)
//Designed for JRE/JDK 8 or higher
//File Name: FileManipulationUtilities.java
//Last Edit: 12/15/2014 (MM/DD/YYYY) 22:15 (24HR)

import javafx.application.Application;
import javafx.stage.Stage;

public class FileManipulationUtilities extends Application {

//main
    public static void main(String[] args) {
	launch(args);
    } //End main
	
    @Override
    public void start(Stage primaryStage) throws Exception {
	new RemoveDuplicateFiles(primaryStage);
	
    } //End public void start(Stage)
	
} //End Class FileManipulationUtilities