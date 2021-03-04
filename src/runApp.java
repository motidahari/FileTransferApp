import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class runApp implements Runnable {
    private String pathforApp;
    private Config con;
    private String path;
    private String pathforYoutube;
    private String strOneDrive;
    private String strGoogleDrive;
    private File file;

    private HashMap<String, Folder> FilesBylecturerInGoogleDrive = new HashMap<String, Folder>();
    private HashMap<String, Folder> FilesBylecturerInOneDrive = new HashMap<String, Folder>();

    public void run() {
        //start of the folder on your pc
        con = new Config();
        //the path of the Folder from text file
        path = con.pathOfTextFile;
        //the path of the folder that we need to upload to Youtube from text file
        pathforYoutube = con.pathforYoutube;
        //the path of strOneDrive
        strOneDrive = con.strOneDrive;
        //the path of strGoogleDrive
        strGoogleDrive = con.strGoogleDrive;
        //the path of App
        pathforApp = con.pathforApp;

        long startTime = System.currentTimeMillis();
        readStringFromFile(path);
        //System.out.println("FilesBylecturerInGoogleDrive = " + FilesBylecturerInGoogleDrive);
        //System.out.println("FilesBylecturerInOneDrive = " + FilesBylecturerInOneDrive);
        //System.out.println("FilesBylecturerInGoogleDrive.size() = " + FilesBylecturerInGoogleDrive.size());
        //System.out.println("FilesBylecturerInOneDrive.size() = " + FilesBylecturerInOneDrive.size());

        for (Folder f : FilesBylecturerInGoogleDrive.values()) {
            if (f.isProperLocation() && f.getNumFiles() >= 1) {
                for (Files file : f.getFilesBylecturer().values()) {
                    if (FilesBylecturerInOneDrive.get(f.getPath()).contains(file.getName())) {
                        String name = changeNameFile(f, file, FilesBylecturerInOneDrive);
                        String oldName = f.getPathGoogleDrive() + f.getPath() + "\\" + file.getName();
                        String newName = f.getPathOneDrive() + f.getPath();
                        moveFileToFolder(oldName, newName, file.getNewName());
                    } else {
                        String oldName = f.getPathGoogleDrive() + f.getPath() + "\\" + file.getName();
                        String newName = f.getPathOneDrive() + f.getPath();
                        moveFileToFolder(oldName, newName, file.getNewName());
                    }
                }
            }
        }
        int[] flags = {0, 1, 2, 3};
        String[] flagsS = {"allFiles.txt", "emptyFolders.txt", "AllFilesWithAllDetails.txt", "bigFiles.txt"};
        boolean exportResult = createNewFile2(FilesBylecturerInOneDrive, pathforApp, flagsS, flags);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time in milliseconds: " + (endTime - startTime));
    }

    public static boolean createNewFile(HashMap<String, Folder> list, String path, String nameFile, int flag) {
        int count = 1;
        if (flag == 1) {
            try {
                FileWriter myWriter = new FileWriter(path + "\\" + nameFile);
                for (Folder f : list.values()) {
                    myWriter.write(count++ + ". " + f.getPathOneDrive() + f.getPath() + "\n");
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file :" + nameFile);
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return false;
            }
        } else if (flag == 0) {
            try {
                FileWriter myWriter = new FileWriter(path + "\\" + nameFile);
                for (Folder f : list.values()) {
                    if (f.isProperLocation() && f.isEmpty()) {
                        String str = count++ + ". ";
                        String[] arrP = (f.getPathOneDrive() + f.getPath()).split("\\\\");
                        for (int j = 3; j < arrP.length; j++) {
                            if (j + 1 == arrP.length) {
                                str += arrP[j];
                                break;
                            }
                            str += arrP[j] + " -> ";
                        }
                        str += "\n" + f.getPathOneDrive() + f.getPath() + "\n";
                        myWriter.write(str + "\n");
                    }
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file :" + nameFile);
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                FileWriter myWriter = new FileWriter(path + "\\" + nameFile);
                for (Folder f : list.values()) {
                    if (f.isProperLocation() && !f.isEmpty()) {
                        String str = count++ + ". " + f.getPathOneDrive() + f.getPath() + "\n";
                        for (Files file : f.getFilesBylecturer().values()) {
                            str += file.getNewName() + "\n";
                        }
                        myWriter.write(str + "\n");
                    }
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file :" + nameFile);
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return false;
            }
        }
    }

    public static boolean createNewFile2(HashMap<String, Folder> list, String path, String[] nameFiles, int[] flag) {

        try {
            for (int i = 0; i < flag.length; i++) {
                int count = 1;
                FileWriter myWriter = new FileWriter(path + "\\" + nameFiles[i]);
                String str = "";
                for (Folder f : list.values()) {
                    if (flag[i] == 0 && f.isProperLocation() && !f.isEmpty()) {
                        str += getPathByFolder(f, count) + "\n";
                        str += getAllFiles(f.getFilesBylecturer().values());
                    } else if (flag[i] == 1 && f.isProperLocation() && f.isEmpty()) {
                        str += getPathByFolder(f, count) + "\n";
                    } else if (flag[i] == 2 && f.isProperLocation() && !f.isEmpty()) {
                        str += getPathByFolder(f, count) + "\n";
                        str += getDetailsByFilesInFolder(f.getFilesBylecturer().values()) + "\n";
                    } else if (flag[i] == 3 && f.isProperLocation() && !f.isEmpty()) {
                        str += getPathByFolder(f, count) + "\n";
                        str += getBigFilesInFolder(f.getFilesBylecturer().values()) + "\n";
                    }
                    count++;

                }
//                System.out.println(str);
                myWriter.write(str + "\n");
                System.out.println("Successfully wrote to the file :" + nameFiles[i]);
                myWriter.close();
            }
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }



    private static String getAllFiles(Collection<Files> files) {
        String str = "";
        for (Files file : files) {
            str += "\t" + file.getName() + "\n";
        }
        return str + "\n";
    }

    private static String getBigFilesInFolder(Collection<Files> files) {
        String str = "";
        for (Files file : files) {
            if (file.getMegabytes() > 500) {
                str += "\t{" +
                        "\n\t\t name = " + file.getNewName() + "," +
                        "\n\t\t size = {" +
                        "\n\t\t\t " + file.getKilobytes() + "KB," +
                        "\n\t\t\t " + file.getMegabytes() + "MB," +
                        "\n\t\t\t " + file.getGigabytes() + "GB" +
                        "\n\t\t }" +
                        "\n\t\t type = " + file.getType() + "," +
                        "\n\t\t path = " + file.getPath() + "\n\t}\n";
            }
        }
        return str;
    }


    private static String getDetailsByFilesInFolder(Collection<Files> files) {
        String str = "";
        for (Files file : files) {
            str += "\tFiles:{" +
                    "\n\t\t name = " + file.getNewName() + "," +
                    "\n\t\t size = {" +
                    "\n\t\t\t " + file.getKilobytes() + "KB," +
                    "\n\t\t\t " + file.getMegabytes() + "MB," +
                    "\n\t\t\t " + file.getGigabytes() + "GB" +
                    "\n\t\t }" +

//                    "\n\t\t\t size = " +
//                    "}" + file.getKilobytes() + "KB,\n" + file.getMegabytes() + "MB, " + file.getGigabytes() + "GB]" + "," +
                    "\n\t\t type = " + file.getType() + "," +
                    "\n\t\t path = " + file.getPath() + "\n\t}\n";
        }
        return str;
    }

    private static String getPathByFolder(Folder f, int rowNum) {
        String[] arrP = (f.getPathOneDrive() + f.getPath()).split("\\\\");
        String str = rowNum + ". ";
        for (int j = 3; j < arrP.length - 1; j++) {
            str += arrP[j] + " -> ";
        }
        str += arrP[arrP.length - 1];
        str += "\n" + f.getPathOneDrive() + f.getPath() + "\n";
        if(f.getNumFiles() > 0){
            str += "\nFolder Size = " +(double)((int)(f.getSizeFolder()*100))/100 + "GB\t || \tNumber Files = " + f.getNumFiles() +  "\t || \t Lecturer Name = " + f.getLecturer() + "\n\n";
        }

        return str;
    }


    /**
     * move file from google drive to one drive
     *
     * @param: oldLocation - the path of the file in google drive
     * @param: newLocation - the path of the file in one drive
     * @param: nameFile - the file name
     * @return: true if Succeeded, false is isn't
     **/
    private boolean moveFileToFolder(String oldLocation, String newLocation, String nameFile) {
        File file = new File(oldLocation);
        System.out.println(newLocation + "\\" + nameFile);
        // renaming the file and moving it to a new location
        if (file.renameTo(new File(newLocation + "\\" + nameFile))) {
            // if file copied successfully then delete the original file
            file.delete();
            System.out.println("File moved successfully\n Name: " + nameFile + "\n" + newLocation + "\\" + nameFile);
            return true;
        } else {
            System.out.println("Failed to move the file to" + newLocation);
        }
        return false;
    }

    /**
     * check if there file with the same name in the one drive/
     * if exist create new name for the fole and change it in the object of the file.
     *
     * @params: path - the path of the Folder
     * @return: arr -> Strings that representing the paths of the Lectures
     **/
    private String changeNameFile(Folder folder, Files file, HashMap<String, Folder> OneDrive) {
        if (!OneDrive.get(folder.getPath()).contains(file.getNewName())) {
            return file.getNewName();
        }
        file.setNewName("New" + file.getName());
//        file.setName("New" + file.getName());
        return changeNameFile(folder, file, OneDrive);
    }


    /**
     * read String From File And Add It To Array
     *
     * @params: path - the path of the Folder
     * @return: arr -> Strings that representing the paths of the Lectures
     **/
    public void readStringFromFile(String path) {
        try {
            file = new File(path);
            Scanner scanner = new Scanner(file, "UTF-8");
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
//                System.out.println(data);
                if (data.length() >= 2 && !(data.substring(0, 2).equals("/*"))) {
//                    System.out.println(data);
                    FilesBylecturerInGoogleDrive.putIfAbsent(data, new Folder(data, strGoogleDrive + data, strOneDrive, strGoogleDrive));
                    FilesBylecturerInOneDrive.putIfAbsent(data, new Folder(data, strOneDrive + data, strOneDrive, strGoogleDrive));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred in func readStringFromFileAndAddItToArray.");
            e.printStackTrace();
        }
    }
}
