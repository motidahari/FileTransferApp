import java.io.File;
import java.util.*;

class MyComp implements Comparator<Files> {


    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p>
     * The implementor must ensure that {@code sgn(compare(x, y)) ==
     * -sgn(compare(y, x))} for all {@code x} and {@code y}.  (This
     * implies that {@code compare(x, y)} must throw an exception if and only
     * if {@code compare(y, x)} throws an exception.)<p>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
     * {@code compare(x, z)>0}.<p>
     * <p>
     * Finally, the implementor must ensure that {@code compare(x, y)==0}
     * implies that {@code sgn(compare(x, z))==sgn(compare(y, z))} for all
     * {@code z}.<p>
     * <p>
     * It is generally the case, but <i>not</i> strictly required that
     * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."<p>
     * <p>
     * In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     */
    @Override
    public int compare(Files o1, Files o2) {
        return 0;
    }
}
class Folder {
    private boolean isEmpty, properLocation;
    private int numFiles;
    private String path,lecturerName,pathOneDrive,fullPath,pathGoogleDrive;
    double sizeFolder;
    private HashMap<String, Files> FilesBylecturer = new HashMap<String, Files>();


    public Folder(String path, String fullPath, String pathOneDrive, String pathGoogleDrive) {
        this.path = path;
        this.fullPath = fullPath;
        this.pathOneDrive = pathOneDrive;
        this.pathGoogleDrive = pathGoogleDrive;
        lecturerName = getLecturerName(path);
        checkIfFolderIsEmpty(fullPath);
        initSizeFolder(FilesBylecturer.values());

    }

    private String getLecturerName(String s) {
        String[] split = s.split("\\\\");
        return split[split.length-1];
    }
    public Folder(String path, String pathOneDrive, String pathGoogleDrive) {
        this.path = path;
        this.pathOneDrive = pathOneDrive;
        this.pathGoogleDrive = pathGoogleDrive;
        this.lecturerName = getLecturerName(path);
        checkIfFolderIsEmpty(this.pathOneDrive);
        initSizeFolder(FilesBylecturer.values());

    }
    public Folder(String path, String pathOneDrive, String pathGoogleDrive, boolean isEmpty, boolean properLocation, int numFiles) {
        this.path = path;
        this.pathOneDrive = pathOneDrive;
        this.pathGoogleDrive = pathGoogleDrive;
        this.isEmpty = isEmpty;
        this.properLocation = properLocation;
        this.numFiles = numFiles;
    }
    public Folder(Folder f) {
        this.path = f.path;
        this.pathOneDrive = f.pathOneDrive;
        this.pathGoogleDrive = f.pathGoogleDrive;
        this.isEmpty = f.isEmpty;
        this.properLocation = f.properLocation;
        this.numFiles = f.numFiles;
    }




    private void initSizeFolder(Collection<Files> files) {
        String str = "";
        double result = 0;
        for (Files file : files) {
            result += file.getGigabytes();
        }
        sizeFolder = result;
    }
    private double getSizeFolder(Collection<Files> files) {
        return (double)((int)(sizeFolder*100))/100;
    }
    public String getLecturer() {
        return lecturerName;
    }
    /**
     * check if the folder is empty
     *
     * @param path
     * @params: path - the path of the Folder
     * @return: -1 -> if is not Directory, 1 if is Directory and the size big then zero -> ,if is Directory and the size is zero
     */
    public int checkIfFolderIsEmpty(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            if ((this.numFiles = file.list().length) > 0) {
                if (this.numFiles == 1 && !file.listFiles()[0].getName().equals("desktop.ini")) {
                    getDetailsByFiles(file, FilesBylecturer);
                    this.isEmpty = false;
                    this.properLocation = true;
                    return 1;
                } else if (this.numFiles > 1) {
                    getDetailsByFiles(file, FilesBylecturer);
                    this.isEmpty = false;
                    this.properLocation = true;
                    return 1;
                } else {
                    this.isEmpty = true;
                    this.properLocation = true;
                    this.numFiles = 0;
                    return 0;
                }
//                    System.out.println("Directory is not empty");
            } else {
                this.isEmpty = true;
                this.properLocation = true;
//                    System.out.println("Directory is empty!");
                return 0;
            }
        } else {
            this.isEmpty = true;
            this.properLocation = false;
//                System.out.println("the path is not Directory!");
            return -1;
        }
    }

    public List<String> getDetailsByFiles2(File path, HashMap<String, Files> list) {
        File[] listOfFiles = path.listFiles();
        if (listOfFiles.length <= 0) return null;
        ArrayList<String> sortedKeys = new ArrayList<>();
        sortedKeys.add(listOfFiles[0].getName()); // add first element

        int curr = 0;
        int res;
        for (File s: listOfFiles) {
            res = s.getName().compareToIgnoreCase(sortedKeys.get(curr));
            if (!list.containsKey(s)) {
                if (res == 0) {// If equals ignore-case
                    sortedKeys.add(++curr, s.getName());
                }
                else if(res < 0) {// If s comes before the other
                    String tmp = sortedKeys.get(curr);
                    sortedKeys.add(curr++, s.getName()); // Because s precedes the other string
                    sortedKeys.add(curr++, tmp);
                } else {// If the other comes before s
                    sortedKeys.add(++curr, s.getName()); // Because s precedes the other string
                }
            }
        }
        return sortedKeys;
    }


    public void getDetailsByFiles(File path, HashMap<String, Files> list) {
        File[] listOfFiles = path.listFiles();
        this.numFiles = listOfFiles.length;
            for (File f : path.listFiles()) {
                if (f.isFile() && !f.getName().equals("desktop.ini")) {
                    list.putIfAbsent(f.getName(),new Files(path.getPath() , f.getName()));
                }else if (f.isDirectory()) {
                    list.putIfAbsent(f.getName(),new Files(path.getPath() , f.getName()));
                }
            }
    }
    public void getDetailsByFiles(File path, Queue<Files> list) {
        File[] listOfFiles = path.listFiles();
        this.numFiles = listOfFiles.length;
        Queue<Files> q = new PriorityQueue<Files>(new compFiles());
        for (File f : path.listFiles()) {
            if ((f.isFile() || f.isDirectory()) && !f.getName().equals("desktop.ini")) {
                q.add(new Files(path.getPath(), f.getName()));
//                list.putIfAbsent(f.getName(), new Files(path.getPath(), f.getName()));
            }
        }
        while (!q.isEmpty()) {
            Files f = q.poll();
            this.FilesBylecturer.putIfAbsent(f.getName(), new Files(path.getPath(), f.getName()));
        }
    }
    private String getAllFiles(HashMap<String, Files> list) {
        String str = "";
        for (Files f : list.values()) {
            str += f.toString();
        }
        return str;
    }
    /**
     * Comparator - return 1 if the string of o1 big then string of o2,
     * return 1 if the string of o1 small then string of o2
     * return 0 if the string of o1 equal to string of o2
     */
    class compFiles implements Comparator<Files> {

        @Override
        public int compare(Files o1, Files o2) {
            int indexO1 = 0;
            int indexO2 = 0;
            int result = 0;
            for (int i = 0; i < Math.max(o1.getLecturer().length(),o2.getLecturer().length()); i++) {
                if(indexO1 < o1.getLecturer().length() && indexO2 < o2.getLecturer().length()){
                    if(indexO1 == 0 && o1.getLecturer().charAt(indexO1) != 'N' && indexO2 == 0 && o2.getLecturer().charAt(indexO2) != 'N'){
                        if((result = check(o1,indexO1,o2,indexO2)) != 0){
                            return result;
                        }
                    }else if(indexO1 == 1 && o1.getLecturer().charAt(indexO1) != 'e' && indexO2 == 1 && o2.getLecturer().charAt(indexO2) != 'e'){
                        if((result = check(o1,indexO1,o2,indexO2)) != 0){
                            return result;
                        }
                    }else if(indexO1 == 2 && o1.getLecturer().charAt(indexO1) != 'w' && indexO2 == 2 && o2.getLecturer().charAt(indexO2) != 'w'){
                        if((result = check(o1,indexO1,o2,indexO2)) != 0){
                            return result;
                        }
                    }else{
                        if((result = check(o1,indexO1,o2,indexO2)) != 0){
                            return result;
                        }
                    }
                }
            }
            return result;
        }

        private int check(Files o1,int indexO1 ,Files o2,int indexO2) {
            if(o1.getLecturer().charAt(indexO1++) >  o2.getLecturer().charAt(indexO2)){
                return 1;
            }else if(o1.getLecturer().charAt(indexO1++) <  o2.getLecturer().charAt(indexO2)){
                return -1;
            }else{
                return 0;
            }
        }

    }
    /**
     * Comparator - return 1 if the string of o1 big then string of o2,
     * return 1 if the string of o1 small then string of o2
     * return 0 if the string of o1 equal to string of o2
     */
    class comp implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int indexO1 = 0;
            int indexO2 = 0;
            int result = 0;
            for (int i = 0; i < Math.max(o1.length(),o2.length()); i++) {
                if(indexO1 < o1.length() && indexO2 < o2.length()){
                    if(indexO1 == 0 && o1.charAt(indexO1) != 'N' && indexO2 == 0 && o2.charAt(indexO2) != 'N'){
                        if((result = check(o1,indexO1,o2,indexO2)) != 0){
                            return result;
                        }
                    }else if(indexO1 == 1 && o1.charAt(indexO1) != 'e' && indexO2 == 1 && o2.charAt(indexO2) != 'e'){
                        if((result = check(o1,indexO1,o2,indexO2)) != 0){
                            return result;
                        }
                    }else if(indexO1 == 2 && o1.charAt(indexO1) != 'w' && indexO2 == 2 && o2.charAt(indexO2) != 'w'){
                        if((result = check(o1,indexO1,o2,indexO2)) != 0){
                            return result;
                        }
                    }
                }
            }
            return 0;
        }
        private int check(String o1,int indexO1 ,String o2,int indexO2) {
            if(o1.charAt(indexO1++) >  o2.charAt(indexO2)){
                return 1;
            }else if(o1.charAt(indexO1++) <  o2.charAt(indexO2)){
                return -1;
            }else{
                return 0;
            }
        }
    }
    public boolean isEmpty() {
        return isEmpty;
    }
    public boolean contains(String fileName) {
        return FilesBylecturer.containsKey(fileName);
    }
    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
    public boolean isProperLocation() {
        return properLocation;
    }
    public void setProperLocation(boolean properLocation) {
        this.properLocation = properLocation;
    }
    public int getNumFiles() {
        return numFiles;
    }
    public void setNumFiles(int numFiles) {
        this.numFiles = numFiles;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getPathOneDrive() {
        return pathOneDrive;
    }
    public void setPathOneDrive(String pathOneDrive) {
        this.pathOneDrive = pathOneDrive;
    }
    public String getPathGoogleDrive() {
        return pathGoogleDrive;
    }
    public void setPathGoogleDrive(String pathGoogleDrive) {
        this.pathGoogleDrive = pathGoogleDrive;
    }
    public double getSizeFolder() {
        return sizeFolder;
    }
    public void setSizeFolder(long sizeFolder) {
        this.sizeFolder = sizeFolder;
    }
    public HashMap<String, Files> getFilesBylecturer() {
        return FilesBylecturer;
    }
    public void setFilesBylecturer(HashMap<String, Files> filesBylecturer) {
        FilesBylecturer = filesBylecturer;
    }
    @Override
    public String toString() {
        return "folder{" +
                "\nisEmpty = " + isEmpty +
                ", \nexistLocation = " + properLocation +
                ", \nnumFiles = " + numFiles +
                ", \npath = '" + path + '\'' +
                ", \npathOneDrive = '" + pathOneDrive + '\'' +
                ", \npathGoogleDrive = '" + pathGoogleDrive + '\'' +
//                    ", sizeFolder=" + sizeFolder +
                "}, \nFilesBylecturer = {" + getAllFiles(FilesBylecturer) +
                "}\n}\n";
    }
}
