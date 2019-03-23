package com.vlad;

import java.io.File;

/**
* Class com.vlad.TestTask, public class where created main method.
* Class created for testing class com.vlad.Task.
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
        TaskIO.readText(myLinklist, new File("file.txt"));
        new Notification(myLinklist);
        new View(myLinklist);
    }
}