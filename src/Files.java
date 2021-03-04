import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class Files {
    private double kilobytes;
    private double megabytes;
    private double gigabytes;
    private String type, name, path, newName, lecturerName;

    public Files(String path, String name) {
        this.path = path;
        this.newName = name;
        this.name = name;
        this.lecturerName = getLecturerName(path + "\\" + name);;
        getSizeFile(path + "\\" + name);
    }

    private String getLecturerName(String s) {
        String[] split = s.split("\\\\");
        return split[split.length-1];
    }

    public void getSizeFile(String s) {//
        File file = new File(s);
        if (file.exists()) {
            Path path;
            try {
                path = Paths.get(s);
                this.type = "" + java.nio.file.Files.probeContentType(path);
            } catch (Exception x) {
                this.type = "null";
            }
            // size of a file (in bytes)
            long bytes = file.length();
            this.kilobytes = (double) ((int) ((bytes / 1024) * 1000)) / 1000;
            this.megabytes = (double) ((int) ((kilobytes / 1024) * 1000)) / 1000;
            this.gigabytes = (double) ((int) ((megabytes / 1024) * 1000)) / 1000;
        }
    }

    public double getKilobytes() {
        return kilobytes;
    }

    public void setKilobytes(double kilobytes) {
        this.kilobytes = kilobytes;
    }

    public double getMegabytes() {
        return megabytes;
    }

    public void setMegabytes(double megabytes) {
        this.megabytes = megabytes;
    }

    public double getGigabytes() {
        return gigabytes;
    }

    public void setGigabytes(double gigabytes) {
        this.gigabytes = gigabytes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
//        this.name = newName;
        this.newName = newName;
    }
    public String getLecturer() {
        return lecturerName;
    }

    public void setLecturer(String lecturer) {
        this.lecturerName = lecturer;
    }
    @Override
    public String toString() {
        return "\nfile{" +
                "name = '" + name + '\'' +
                "| type = '" + type + '\'' +
                "| kilobytes = " + kilobytes +
                "| megabytes = " + megabytes +
                "| gigabytes = " + gigabytes +
                "| path = '" + path + '\'' +
                '}';
    }

}


