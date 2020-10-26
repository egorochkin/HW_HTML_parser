import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.io.File;

public class Writer {

    private String fileName;

    public Writer(String fileName){
        this.fileName = fileName;
    }

    private FileWriter prepareFile() throws IOException {
        File destFile = new File(fileName);
        if (destFile.exists()){
            FileWriter writeToDest = new FileWriter(destFile, true);
            return writeToDest;
        }
        else {
            destFile.createNewFile();
            FileWriter writeToDest = new FileWriter(destFile);
            return writeToDest;
        }

    }

    public void writeToFile(Set<Vacancy> vacancies) throws IOException {
        FileWriter writeToDest = prepareFile();
        for (Vacancy currVacancy : vacancies) {
            writeToDest.write(currVacancy.getLinkToSource() + "\n");
            writeToDest.write(currVacancy.getCreationDate() + "\n");
            writeToDest.write(currVacancy.getVacancyHead() + "\n");
            writeToDest.write(currVacancy.getVacancyText() + "\n");
        }
        writeToDest.flush();
        writeToDest.close();
    }
}
