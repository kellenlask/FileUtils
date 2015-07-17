//This class's purpose is to store all of the static classes used by the project
//Creation Date: 12/8/14
//Author: Kellen Lask
//Designed for JRE/JDK 8 or higher
//File Name: UtilFunctions.java
//Last Edit: 12/15/2014 (MM/DD/YYYY) 22:15 (24HR)

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
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
public class UtilFunctions {

    //Parse the user's string of filetypes into a String[] of filetypes
    public static String[] parseFileTypes(String inputString) {
	
	//ArrayList to store each parsed filetype
	ArrayList<String> fileTypes = new ArrayList<>();

	//Scanner to iterate over the string filetype by filetype
	Scanner in = new Scanner(inputString);
	in.useDelimiter("[,] | [, ]");

	//Iterate over the string
	while (in.hasNext()) {
	    fileTypes.add(in.next());
	}
	    
	if (fileTypes.size() > 0) {
	    //Generate an array from the ArrayList
	    String[] returnArray = new String[fileTypes.size()];
	    returnArray = fileTypes.toArray(returnArray);

	    //Return that magnificent array
	    return returnArray;
	    
	} else {
	    return null;
	}
    } //End public static String[] parseFileTypes(String)

    //Takes a file and makes a hash code in the form of a String.
    public static String makeHash(File file) throws FileNotFoundException, IOException {
	FileInputStream fis = new FileInputStream(file);
	String hash = DigestUtils.md5Hex(fis);
	fis.close();
	
	return hash;
	
    } //End public static String makeHash(File)

    public static String getFileExtension(File f) throws IOException {
	String path = f.getCanonicalPath();
	String extension = null;

	for (int i = path.length() - 1; i >= 0; i--) {
	    extension = path.substring(i);

	    if (extension.contains(".")) {
		break;
	    }
	}

	return extension.substring(1).toLowerCase();
    } //End public static String getFileExtension(File)

    public static File makeDirectory(File parentDirectory, String newDirectoryName) {
	try {
	    File newDirectory = new File(parentDirectory.getCanonicalPath() + "/" + newDirectoryName + "/");
	    int i = 0;

	    while (newDirectory.exists()) {
		newDirectory = new File(parentDirectory.getCanonicalPath() + "/" + newDirectoryName + i + "/");
		i++;
	    }

	    boolean success = newDirectory.mkdir();
	    if (!success) {
		throw new Exception();
	    }

	    return newDirectory;

	} catch (Exception e) {
	    JOptionPane.showMessageDialog(null, "Could not create new directory.");
	    return null;
	}

    } //End public static boolean makeSortDirectory()

    public static int sortFiles(Iterator<File> fileItr, File parentDirectory) {
	try {
	    File organizedDir = makeDirectory(parentDirectory, "OrganizedFiles");
	    if (organizedDir == null) {
		throw new Exception();
	    }

	    int filesSorted = 0;

	    while (fileItr.hasNext()) {
		File tmp = fileItr.next();
		File dest = new File(organizedDir.getAbsolutePath() + "/" + getFileExtension(tmp));

		FileUtils.moveFileToDirectory(tmp, dest, true);

		filesSorted++;
	    }

	    return filesSorted;
	} catch (Exception e) {
	    return -1;
	}
    } //End  public static boolean sortFiles(Iterator<File>)

    public static int batchRename(File[] files, String prefix, String postfix, String replaceThis, String withThis) {
	try {
	    int filesChanged = 0;

	    for (File f : files) {

		//This bit should have better duplicate name support
		if (f.getName().contains(replaceThis)) {
		    String name = f.getName();
		    name = name.replace(replaceThis, withThis);

		    f.renameTo(new File(f.getParentFile().getCanonicalPath() + "/" + name));

		}

		String name = prefix + f.getName() + postfix;

		f.renameTo(new File(f.getParentFile().getCanonicalPath() + "/" + name));

		filesChanged++;
	    }

	    return filesChanged;

	} catch (Exception e) {
	    return -1;
	}

    }

} //End UtilFunctions
