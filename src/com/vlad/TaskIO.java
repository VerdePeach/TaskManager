package com.vlad;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.Writer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Class that provides input output information about tasks to the file.
 */
public class TaskIO {

    final static Logger logger = Logger.getLogger(TaskIO.class);
    /**
     * Static method that write information about tasks to the file in binary form.
     * @param tasks list of task for writing.
     * @param out stream of output.
     */
    public static void write(TaskList tasks, OutputStream out) {
        DataOutputStream outputStream = null;
        try {
            outputStream = new DataOutputStream(out);
            try {
                outputStream.writeInt(tasks.size()); // amount of tasks
                for (int i = 0; i < tasks.size(); i++) {
                    outputStream.write(tasks.getTask(i).getTitle().length()); // length of title
                    outputStream.writeBytes(tasks.getTask(i).getTitle());
                    if (tasks.getTask(i).isActive()) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(1);
                    }
                    outputStream.writeInt(tasks.getTask(i).getRepeatInterval());
                    if (tasks.getTask(i).isRepeated()) {
                        outputStream.writeLong(tasks.getTask(i).getStartTime().getTime());
                        outputStream.writeLong(tasks.getTask(i).getEndTime().getTime());
                    } else {
                        outputStream.writeLong(tasks.getTask(i).getTime().getTime());
                    }
                }
            } catch (IOException ex) {
                logger.error(ex);
            }
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     * Static method that read information about tasks from the file in binary form.
     * @param tasks list of task for writing.
     * @param in stream of input.
     */
    public static void read(TaskList tasks, InputStream in) {
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(in);
            try {
                int sizeList = inputStream.readInt(); // amount of tasks

                for (int i = 0; i < sizeList ;i++) {
                    int lenName = inputStream.read(); // name length
                    byte[] bf = new byte[lenName];
                    int nameStap = 0;
                    while (nameStap < lenName) {
                        bf[nameStap] = inputStream.readByte();
                        nameStap++;
                    }
                    char [] mas = new char[lenName];
                    for(int k = 0; k < lenName; k++) {
                        mas[k] = (char) bf[k];
                    }
                    String str = new String(mas);

                    int k = inputStream.read(); // activity
                    boolean act;
                    if (k == 1) {
                        act = true;
                    } else {
                        act = false;
                    }

                    int inter = inputStream.readInt();
                    if (inter != 0) {
                        Date a = new Date(inputStream.readLong());
                        Date b = new Date(inputStream.readLong());
                        tasks.add(new Task(str, a, b, inter));
                    } else {
                        Date a = new Date(inputStream.readLong());
                        tasks.add(new Task(str, a));
                    }
                    tasks.getTask(i).setActive(act);
                }
            } catch (IOException ex) {
                logger.error(ex);
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }

    }

    /**
     * Static method that creates FileOutputStream for next writing.
     * @param tasks list of task for writing
     * @param file file for writing.
     * @throws IOException
     */
    public static void writeBinary(TaskList tasks, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        write(tasks, fileOutputStream);
    }

    /**
     * Static method that creates FileOutputStream for next reading.
     * @param tasks list of task for  reading
     * @param file file for reading
     */
    public static void readBinary(TaskList tasks, File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            read(tasks, fileInputStream);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
//********************************************************************************************

    /**
     * Static method that write information about tasks to the file in text form.
     * @param tasks list of task for writing
     * @param out Writer
     * @throws IOException
     */
    public static void write(TaskList tasks, Writer out) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(out);
        for (int i = 0; i < tasks.size(); i++) {
            if(tasks.getTask(i).getTitle() != null)
            try {
                StringBuilder a = new StringBuilder(tasks.getTask(i).getTitle());
                int g = 0;
                while (a.length() > g) {
                    if (a.charAt(g) == '\"') {
                         a = a.insert(g, '\"');
                        g++;
                    }
                    g++;
                }
                String newTitle = a.toString();
                bufferedWriter.write("\"" + newTitle + "\" ");

                String strDateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
                SimpleDateFormat newDate = new SimpleDateFormat(strDateFormat);

                if(tasks.getTask(i).isRepeated()) {
                    bufferedWriter.write("from ");
                    bufferedWriter.write("[" + newDate.format(tasks.getTask(i).getStartTime().getTime()) + "]");
                    bufferedWriter.write(" to ");
                    bufferedWriter.write("[" + newDate.format(tasks.getTask(i).getEndTime().getTime()) + "]");
                    if (tasks.getTask(i).getRepeatInterval() > 0) {
                        int days = 0;
                        int hours = 0;
                        int minutes = 0;
                        int seconds = 0;
                        int myInterval = tasks.getTask(i).getRepeatInterval();

                        bufferedWriter.write(" every [");
                        if ((days = myInterval / 86400) >= 0) {
                            if (days > 1)
                                bufferedWriter.write(days + " days");
                            else if (days == 1)
                                bufferedWriter.write(days + " day");
                            myInterval %= 86400;
                            if (myInterval > 0 && days > 0)
                                bufferedWriter.write(" ");
                        }
                        if ((hours = myInterval / 3600) >= 0) {
                            myInterval = myInterval % 3600;
                        }
                        if ((minutes = myInterval / 60) >= 0) {
                            myInterval %= 60;
                        }
                        seconds = myInterval;

                        if (hours > 1)
                            bufferedWriter.write(hours + " hours");
                        else if (hours == 1)
                            bufferedWriter.write(hours + " hour");
                        if (minutes > 0 && hours > 0)
                            bufferedWriter.write(" ");

                        if (minutes > 1)
                            bufferedWriter.write(minutes + " minutes");
                        else if (minutes == 1)
                            bufferedWriter.write(minutes + " minute");
                        if (seconds > 0  && minutes > 0)
                            bufferedWriter.write(" ");

                        if (seconds > 1)
                            bufferedWriter.write(seconds + " seconds");
                        else if (seconds == 1)
                            bufferedWriter.write(seconds + " seconds");

                        bufferedWriter.write("]");

                    }

                    if (tasks.getTask(i).isActive() == false) {
                        bufferedWriter.write(" inactive");
                    } else bufferedWriter.write(" active");// corrected
                } else {
                    bufferedWriter.write("at ");
                    bufferedWriter.write("[" + newDate.format(tasks.getTask(i).getTime()) + "]");
                    if (tasks.getTask(i).isActive() == false) {
                        bufferedWriter.write(" inactive");
                    } else bufferedWriter.write(" active");// corrected
                }
            } catch (IOException ex){
                logger.error("Output error", ex);
            }

            if (i + 1 < tasks.size())
                bufferedWriter.write(";");
            else
                bufferedWriter.write(".");
            bufferedWriter.newLine();
        }
        bufferedWriter.write("\n");
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    /**
     * Static method that read information about tasks from the file in text form.
     * @param tasks list of task for writing
     * @param in Reader
     */
    public static void read(TaskList tasks, Reader in) {
        BufferedReader bufferedReader = null;
        Pattern patternNonRepeated = Pattern.compile("^\"(.*)\" at \\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})](.*)[.;]$");
        Pattern patternRepeated = Pattern.compile("^\"(.*)\" from \\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})] to \\[(\\d{4}-" +
                "\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})] every \\[(.*)](.*)[.;]$");

        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            bufferedReader = new BufferedReader(in);
            String newStr = bufferedReader.readLine();
            while (bufferedReader.ready()) {
                Matcher matcher = patternNonRepeated.matcher(newStr);
                Matcher matcher1 = patternRepeated.matcher(newStr);

                Task t = new Task();
                Date start;
                Date end;
                if (matcher.find()) {
                    String name = new String(matcher.group(1));
                    char mas [] = name.toCharArray();
                    for (int i = 0; i < mas.length; i++) {
                        if (mas[i] == '\"') {
                            char mas1 [] = new char[mas.length - 1];
                            for (int k = 0; k < mas1.length; k++){
                                if (k < i) {
                                    mas1[k] = mas[k];
                                } else {
                                    mas1[k] = mas[k + 1];
                                }
                            }
                            mas = mas1;
                        }
                    }
                    name = new String(mas);
                    t.setTitle(name);
                    t.setTime((newDate.parse(matcher.group(2))));
                    if(matcher.group(3).equals(" inactive")) {
                        t.setActive(false);
                    } else {
                        t.setActive(true);
                    }

                } else if (matcher1.find()) {
                    String name = new String(matcher1.group(1));
                    char mas [] = name.toCharArray();
                    for(int i = 0; i < mas.length; i++){
                        if(mas[i] == '\"') {
                            char mas1 [] = new char[mas.length - 1];
                            for (int k = 0; k < mas1.length; k++){
                                if(k < i) {
                                    mas1[k] = mas[k];
                                } else {
                                    mas1[k] = mas[k + 1];
                                }
                            }
                            mas = mas1;
                        }
                    }
                    name = new String(mas);
                    t.setTitle(name);
                    start = newDate.parse(matcher1.group(2));
                    end = newDate.parse(matcher1.group(3));
                    String inter = new String(matcher1.group(4));
                    if(matcher1.group(5).equals(" inactive")) { //space is necessary
                        t.setActive(false);
                    } else
                        t.setActive(true);

                    int allIn = 0;
                    int val = 0;
                    for (int i = 0; i < inter.length() - 1; i++) {
                        if((int)inter.charAt(i) >= 48 && (int)inter.charAt(i) - 0 <= 57) {
                            val = inter.charAt(i) - '0';
                            i++;
                            while ((int)inter.charAt(i) >= 48 && (int)inter.charAt(i) - 0 <= 57){
                                val = val * 10 + inter.charAt(i) - '0';
                                i++;
                            }
                        }
                        if(inter.charAt(i) == 'd' && inter.charAt(i + 1) == 'a') {
                            allIn += val * 24 * 60 * 60;
                            val = 0;
                        } else if (inter.charAt(i) == 'h' && inter.charAt(i + 1) == 'o') {
                            allIn += val * 60 * 60;
                            val = 0;
                        } else if (inter.charAt(i) == 'm' && inter.charAt(i + 1) == 'i') {
                            allIn += val * 60;
                            val = 0;
                        } else if (inter.charAt(i) == 's' && inter.charAt(i + 1) == 'e') {
                            allIn += val;
                            val = 0;
                        }
                    }
                    t.setTime(start, end, allIn);
                }
                if (t != null)
                    tasks.add(t);

                newStr = bufferedReader.readLine();
                if (newStr == null) {
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            logger.error(e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    /**
     * Static method that creates FileWriter for next writing.
     * @param tasks list of task for writing.
     * @param file file for writing.
     */
    public static void writeText(TaskList tasks, File file){
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        try (FileWriter fileWriter = new FileWriter(file)){
            write(tasks, fileWriter);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /**
     * Static method that creates FileReader for next reading.
     * @param tasks list of task for writing.
     * @param file file for reading.
     */
    public static void readText(TaskList tasks, File file){
        try {
            FileReader fileReader = new FileReader(file);
            read(tasks, fileReader);
        } catch (FileNotFoundException e) {
            logger.error(e);
        }
    }

    /**
     * Static method that creates File if it is not exists.
     */
    public static void createFile() {
        File newFile = new File("file.txt");
        if(!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }
}