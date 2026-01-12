import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
    private final File savesFile = new File("src/progress_data.txt");
    public int readSavedLevel() {
        int unlockedLevel = 0 ;
        try (Scanner myReader = new Scanner(savesFile)) {
            if (myReader.hasNextLine()) {
                String readLine = myReader.nextLine();
                System.out.println(readLine);
                unlockedLevel = Integer.parseInt(readLine);
            }
        } catch (FileNotFoundException e) {
            System.out.println("could not find progress_data.txt.");
            e.printStackTrace();
            return 1;
        }
        return unlockedLevel;
    }
    public void writeSavedLevel(int unlockedLevel) {
        try {
            FileWriter myWriter = new FileWriter(savesFile);
            //I need to clear this file before writing new info
            myWriter.write(unlockedLevel + "\n");
            myWriter.close();  // must close manually
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
