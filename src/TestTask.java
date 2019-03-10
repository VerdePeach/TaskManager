
import java.io.File;

/**
* Class TestTask, public class where created main method.
* Class created for testing class Task. 
*/
public class TestTask {

    /**
    * Main method where program is started.
    * @param args comand line values
    * @throws CloneNotSupportedException
    */
    public static void main(String[] args) {
        TaskIO.createFile();
        LinkedTaskList myLinklist = new  LinkedTaskList();
        TaskIO inputTasks = new TaskIO();
        inputTasks.readText(myLinklist, new File("file.txt"));
        new Notification(myLinklist);
        new View(myLinklist);
    }
}