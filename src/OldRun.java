import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class OldRun implements Runnable {
    public static String pathforApp;
    public static Config con;
    public static String path;
    public static String pathforYoutube;
    public static String strOneDrive;
    public static String strGoogleDrive;


    //read String From File And Add It To Array
    public static String[] readStringFromFileAndAddItToArray(String path) {
        String[] arr = new String[0];
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file, "UTF-8");
            int i = 0;
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if (data.length() >= 2 && !data.substring(0, 2).equals("/*")) {
                    arr = Arrays.copyOf(arr, arr.length + 1);
                    arr[i++] = data;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred in func readStringFromFileAndAddItToArray.");
            e.printStackTrace();
        }
        return arr;
    }

    //get all name files from folder
    public static String[] getAllFileNamesFromFolder(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String[] newArr = new String[0];
        int j = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("desktop.ini")) {
                newArr = Arrays.copyOf(newArr, newArr.length + 1);
                newArr[j++] = listOfFiles[i].getName();
            } else if (listOfFiles[i].isDirectory()) {
                newArr = Arrays.copyOf(newArr, newArr.length + 1);
                newArr[j++] = listOfFiles[i].getName();
            }
        }
        return newArr;
    }

    //the app
    public static void runApp(String strOneDrive, String strGoogleDrive, String[] folders) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < folders.length; i++) {
            boolean checkIfFolderIsEmpty = checkIfFolderIsEmpty(strGoogleDrive + folders[i]);
            if (checkIfFolderIsEmpty) {
                String[] fileNames = getAllFileNamesFromFolder(strGoogleDrive + folders[i]);
                if (fileNames.length > 0) {
                    for (int k = 0; k < fileNames.length; k++) {
                        String oldLocation = strGoogleDrive + folders[i] + "\\" + fileNames[k];
                        String newLocation = strOneDrive + folders[i] + "\\" + fileNames[k];
                        String[] fileNames2 = getAllFileNamesFromFolder(strOneDrive + folders[i]);
                        String nameFile = fileNames[k];
                        for (int l = 0; l < fileNames2.length; l++) {
                            if (nameFile.equals(fileNames2[l])) {
                                nameFile = "New" + nameFile;
                                l = -1;
                            }
                        }
                        newLocation = strOneDrive + folders[i] + "\\" + nameFile;
                        System.out.println("oldLocation = " + oldLocation);
                        System.out.println("newLocation = " + newLocation);
                        System.out.println("nameFile = " + nameFile);
                        moveFileToFolder(oldLocation, newLocation, nameFile);
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);
    }

    //move file to other folder
    public static void moveFileToFolder(String oldLocation, String newLocation, String nameFile) {
        File file = new File(oldLocation);
        // renaming the file and moving it to a new location
        if (file.renameTo(new File(newLocation))) {
            // if file copied successfully then delete the original file
            file.delete();
            System.out.println("File moved successfully\n Name: " + nameFile + "\n" + newLocation);
        } else {
            System.out.println("Failed to move the file to" + newLocation);
        }
    }

    //check if the folder is empty
    public static boolean checkIfFolderIsEmpty(String path) {

        File file = new File(path);
        if (file.isDirectory()) {
            if (file.list().length > 0) {
                //System.out.println("Directory is not empty!");
                return true;
            } else {
                //System.out.println("Directory is empty!");
                return false;
            }
        } else {
            System.out.println("the path: " + path + "\n is not a directory");
            return false;
        }

    }

    //printFilesName
    public static void printFilesName(String[] arr, String strOneDrive) {
        for (int i = 0; i < arr.length; i++) {
            if (checkIfFolderIsEmpty(strOneDrive + arr[i])) {
                String[] newArr = getAllFileNamesFromFolder(strOneDrive + arr[i]);
                System.out.println();
                System.out.println(strOneDrive + arr[i]);
                for (int k = 0; k < newArr.length; k++) {
                    System.out.println(newArr[k]);
                }
            }
        }
    }

    //function to check if folder is empty and return arr with the path of the folder
    public static String[] getArrayByEmptyFolder(String strOneDrive, String[] folders) {
        String[] array = new String[0];
        int index = 0;
        for (int i = 0; i < folders.length; i++) {
            if (checkIfFolderIsEmpty2(strOneDrive + folders[i]) == 0) {
                array = Arrays.copyOf(array, array.length + 1);
                array[index++] = strOneDrive + folders[i];
            }
        }
        createNewFile(array, pathforApp, "emptyFolders.txt", "pathEmptyFolders.txt", 0);
        createNewFile(array, pathforApp, "emptyFolders.txt", "pathEmptyFolders.txt", 1);
        return array;
    }

    //Create a File
    public static boolean createNewFile2(String[] array, String path, String nameFile) {

        //System.out.println(pathforApp+nameFile);
        try {
            FileWriter myWriter = new FileWriter(pathforApp + nameFile);
            for (int i = 0; i < array.length; i++) {
                String str = array[i];
                myWriter.write(array[i] + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file." + nameFile);
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createNewFile(String[] array, String pathforApp, String nameFile, String nameFile2, int flag) {

        //System.out.println(pathforApp+nameFile);
        if (flag == 1) {
            try {
                FileWriter myWriter = new FileWriter(pathforApp + nameFile);
                for (int i = 0; i < array.length; i++) {
                    String str = array[i];
                    myWriter.write(array[i] + "\n");
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file." + nameFile);
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                FileWriter myWriter = new FileWriter(pathforApp + nameFile2);
                for (int i = 0; i < array.length; i++) {
                    String str = array[i];
                    String[] arrP = (str).split("\\\\");
                    String str2 = "";
                    for (int j = 3; j < arrP.length; j++) {
                        if (j + 1 == arrP.length) {
                            str2 += arrP[j];
                            break;
                        }
                        str2 += arrP[j] + " -> ";
                    }
                    myWriter.write(str2 + "\n");
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file." + nameFile2);
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return false;
            }
        }
    }

    //check if the folder is empty
    public static int checkIfFolderIsEmpty2(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            if (file.list().length > 0) {
                //System.out.println("Directory is not empty!");
                return 1;
            } else {
                //System.out.println("Directory is empty!");
                return 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * @name getYoutubetxt
     * run on the pattern and create a list in a text file
     * @params: folders -> file patterns
     * strOneDrive -> path fpr one drive
     * @return: youtubeList -> the list
     **/
    public static String[] getYoutubetxt(String strOneDrive, String[] folders, String nameFile) {

        //System.out.println("strOneDrive = " + strOneDrive + folders[2]);
        //System.out.println("folders[" + 2 + "] = " + folders[2]);
        String[] array = new String[0];
        try {
            FileWriter myWriter = new FileWriter(pathforApp + "youtubeList.txt");
            int index = 0;
            for (int i = 0; i < folders.length; i++) {
                if (checkIfFolderIsEmpty2(strOneDrive + folders[i]) == 1) {
                    array = Arrays.copyOf(array, array.length + 1);
                    String[] arrNames = getAllFileNamesFromFolder(strOneDrive + folders[i]);
                    String str = "";
                    for (int j = 0; j < arrNames.length; j++) {
                        str += arrNames[j] + "\n";
                    }
                    array[index++] = str;
                    String[] arrP = (strOneDrive + folders[i]).split("\\\\");
                    String str2 = "";
                    for (int j = 3; j < arrP.length; j++) {
                        if (j + 1 == arrP.length) {
                            str2 += arrP[j];
                            break;
                        }
                        str2 += arrP[j] + " -> ";
                    }
                    String result = strOneDrive + folders[i] + "\n" + str2 + "\n" + str;
                    //System.out.println(result);
                    myWriter.write(result + "\n");
                }
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file." + nameFile);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return array;
    }

    public void run() {
        //start of the folder on your pc
        con = new Config();
        //the path of the folders from text file
        path = con.pathOfTextFile;
        //the path of the folder that we need to upload to Youtube from text file
        pathforYoutube = con.pathforYoutube;
        //the path of strOneDrive
        strOneDrive = con.strOneDrive;
        //the path of strGoogleDrive
        strGoogleDrive = con.strGoogleDrive;
        //the path of App
        pathforApp = con.pathforApp;

        //run by the file and add it to array
        String[] foldersYoutube = readStringFromFileAndAddItToArray(pathforYoutube);

        //print all files in folders for upload to yputube
        printFilesName(foldersYoutube, strOneDrive);

        //run by the file and add it to array
        String[] folders = readStringFromFileAndAddItToArray(path);

        //System.out.println(Arrays.deepToString(folders));
        //print all files in folders
        printFilesName(folders, strOneDrive);

        /**
         * check for path
         * run by all paths and check the directory
         */
		/*for (int i = 0 ; i < folders.length; i++){
			System.out.println(strOneDrive + folders[i] );
			checkIfFolderIsEmpty(strOneDrive + folders[i]);
		}*/
        //function to check if folder is empty and return arr with the path of the folder
        String[] emptyFolders = getArrayByEmptyFolder(strOneDrive, folders);
        //create txt file of the youtube courses list
        String[] youtubeList = getYoutubetxt(strOneDrive, foldersYoutube, "youtubeList.txt");
        boolean allFiles = createNewFile2(folders, pathforApp, "allFiles.txt");
        //get time for stop the app
        runApp(strOneDrive, strGoogleDrive, folders);
    }
}
