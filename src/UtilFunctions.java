//This class's purpose is to store all of the static classes used by the project
//Creation Date: 12/8/14
//Author: Kellen Lask
//Designed for JRE/JDK 8 or higher
//File Name: UtilFunctions.java
//Last Edit: 12/15/2014 (MM/DD/YYYY) 22:15 (24HR)

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
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
public class UtilFunctions {
    //Parse the user's string into a String[]
    public static String[] parseArray(String inputString) {
		System.out.println("You made it: " + inputString);
		ArrayList<String> fileTypes = new ArrayList<>();
		Scanner in = new Scanner(inputString);
		in.useDelimiter("[,] | [, ]");

		while(in.hasNext()) {
			fileTypes.add(in.next());
		}

		String[] returnArray = new String[fileTypes.size()];
		returnArray = fileTypes.toArray(returnArray);

		fileTypes.forEach(System.out::println);

		return returnArray;

    } //End public static String[] parseArray(String)

    //Takes a file and makes a hash code in the form of a String.
    public static String makeHash(File file) throws Exception {
		byte[] data = new byte[(int) file.length()];

		MessageDigest md = MessageDigest.getInstance("SHA"); //Or MD5
		try (FileInputStream fis = new FileInputStream(file)) {
			fis.read(data);
		}

		md.update(data);
		byte[] digest = md.digest();

		String hash = "";

		for(int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(digest[i]);
			if(hex.length() == 1) {
				hex = "0" + hex;
			}

			hex = hex.substring(hex.length() - 2);
			hash += hex;

		}

		return hash;
    } //End public static String makeHash(File)

    //Recursively gets all the files in a given directory with the given file
    //extensions and returns a Map<String, String> of the file paths and the
    //file hash codes.
    public static HashMap<String, String> getFiles(String[] extensions, File selectedDirectory) throws Exception {
		List<File> files = (List<File>) FileUtils.listFiles(selectedDirectory, extensions, true);
		HashMap<String, String> map = new HashMap<>();

		for(File f : files) {
			map.put(f.getAbsolutePath(), makeHash(f));
		}

		return map;

    } //End public HashMap<String, String> getImageFiles()

    public static ArrayList<String> findDuplicateFiles(HashMap<String, String> map) {
		Set<String> keys = map.keySet();
		HashSet<String> noDuplicates = new HashSet<>();
		ArrayList<String> deleteTheseFiles = new ArrayList<>();

		//If a hash code already exists in the set unique of hash strings, then add
		//the associated file address to the ArrayList of deletable files.
		//Otherwise, add the hash code to the set.
		for(String s : keys) {
			if(!noDuplicates.contains(map.get(s))) {
				noDuplicates.add(map.get(s));

			} else {
				deleteTheseFiles.add(s);
			}
		}

		return deleteTheseFiles;

    } //End public int removeDuplicateFiles(HashMap<String, String>)

    public static int removeFiles(ArrayList<String> files) throws java.io.FileNotFoundException {
        int filesRemoved = 0;

        for(String s : files) {
            File file = new File(s); //Minimal Scope

            if(!file.delete()) {
                    JOptionPane.showMessageDialog(null, s + " not deleted.");
            } else {
                    filesRemoved++;
            }
        }

        return filesRemoved;

    } //End private int removeFiles(ArrayList<String>)

    public static String getFileExtension(File f) throws IOException {
		String path = f.getCanonicalPath();
		String extension = null;

		for(int i = path.length() - 1; i >= 0; i--) {
			extension = path.substring(i);

			if(extension.contains(".")) {break;}
		}

		return extension.substring(1).toLowerCase();
    } //End public static String getFileExtension(File)

    public static File makeDirectory(File parentDirectory, String newDirectoryName) {
		try {
			File newDirectory = new File(parentDirectory.getCanonicalPath() + "/" + newDirectoryName + "/");
			int i = 0;

			while(newDirectory.exists()) {
				newDirectory = new File(parentDirectory.getCanonicalPath() + "/" + newDirectoryName + i + "/");
				i++;
			}

			boolean success = newDirectory.mkdir();
			if(!success) {throw new Exception();}

			return newDirectory;

		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Could not create new directory.");
			return null;
		}

    } //End public static boolean makeSortDirectory()

    public static int sortFiles(Iterator<File> fileItr, File parentDirectory) {
		try {
			File organizedDir = makeDirectory(parentDirectory, "OrganizedFiles");
			if(organizedDir == null) {throw new Exception();}

			int filesSorted = 0;

			while(fileItr.hasNext()) {
				File tmp = fileItr.next();
				File dest = new File(organizedDir.getAbsolutePath() + "/" + getFileExtension(tmp));

				FileUtils.moveFileToDirectory(tmp, dest, true);

				filesSorted++;
			}

			return filesSorted;
		} catch(Exception e) {
			return -1;
		}
	} //End  public static boolean sortFiles(Iterator<File>)
    
    public static int batchRename(File[] files, String prefix, String postfix, String replaceThis, String withThis) {
        try {
            int filesChanged = 0;
            
            for(File f : files) {
                
                //This bit should have better duplicate name support
                if(f.getName().contains(replaceThis)) {
                    String name = f.getName();
                    name = name.replace(replaceThis, withThis);
                    
                    f.renameTo(new File(f.getParentFile().getCanonicalPath() + "/" + name));
		    
                }
                
                String name = prefix + f.getName() + postfix;

                f.renameTo(new File(f.getParentFile().getCanonicalPath() +  "/" + name));
		
		filesChanged++;
            }
            
            return filesChanged;
            
        } catch(Exception e) {
            return -1;
        }
        
    }

} //End UtilFunctions