import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
    public int readSavedLevel() {
        int unlockedLevel = 0 ;
        File savesFile = new File("src/progress_data.txt");
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
}
