package com.vlad;

import org.apache.log4j.Logger;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Public class contains window where customer can add new task.
 * class com.vlad.ViewAdd extends JFrame
 */
public class ViewAdd extends JFrame {

    final static Logger logger = Logger.getLogger(ViewAdd.class);

    /**
     * Constructor creates window for adding task.
     * Depending on the parameter "str", a window will be displayed for the task that is repeated or not repeated.
     * @param str
     * @param linkedTaskList
     */
    ViewAdd(String str, LinkedTaskList linkedTaskList) {
        setBounds(900,400,300,300);
        setTitle("Add Task");
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.darkGray);

        JCheckBox activeCheck = new JCheckBox("Active");
        JLabel titleLabel = new JLabel("Title");

        JTextField titleTextField = new JTextField("title1", 20);
        JButton addButton = new JButton("Add");
        JButton addButton1 = new JButton("Add");

        titleLabel.setForeground(Color.orange);

        panel.add(titleLabel);
        panel.add(titleTextField);


        //repeated task
        JTextField fromTextField = new JTextField( 20);
        JTextField toTextField = new JTextField( 20);
        JTextField intervalTextField = new JTextField( 20);
        JLabel fromLable = new JLabel("Start date: yyyy-MM-dd HH:mm");
        JLabel toLabel = new JLabel("End date: yyyy-MM-dd HH:mm");
        JLabel intervalLable = new JLabel("Interval in minutes");
        //repeated task

        //not repeated task
        JTextField timetextField = new JTextField( 20);
        JLabel timeLable = new JLabel("Date: yyyy-MM-dd HH:mm");
        //not repeated task
        if(str.equals("yes")){

            fromLable.setForeground(Color.orange);
            panel.add(fromLable);
            panel.add(fromTextField);

            toLabel.setForeground(Color.orange);
            panel.add(toLabel);
            panel.add(toTextField);

            intervalLable.setForeground(Color.orange);
            panel.add(intervalLable);
            panel.add(intervalTextField);
            panel.add(addButton);
        } else {
            timeLable.setForeground(Color.orange);
            panel.add(timeLable);
            panel.add(timetextField);
            panel.add(addButton1);
        }
        panel.add(activeCheck);
        setContentPane(panel);
        setVisible(true);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int interval = 0;
                String title = "";
                boolean active = false;
                Date start = new Date(0);
                Date end = new Date(0);
                if (titleTextField.getText().length() > 0) {
                    title = titleTextField.getText();
                    System.out.println(titleTextField.getText());
                }
                if (activeCheck.isSelected() == true) {
                    active = true;
                    System.out.println(activeCheck.isSelected());
                }
                if (intervalTextField.getText().length() > 0) {
                    char[] arrInterval = intervalTextField.getText().toCharArray();
                    for (int i = 0; i < arrInterval.length; i++) {
                        if(arrInterval[i] == '1' || (int)arrInterval[i] == '2' || (int)arrInterval[i] == '3'
                                || (int)arrInterval[i] == '4' || (int)arrInterval[i] == '5' || (int)arrInterval[i] == '6'
                                || (int)arrInterval[i] == '7' || (int)arrInterval[i] == '8' || (int)arrInterval[i] == '9'
                                || (int)arrInterval[i] == '0'){
                            interval = Integer.parseInt(intervalTextField.getText());
                            System.out.println(interval);
                        } else {
                            logger.error("Incorrect format of interval: " + interval + ".");
                            interval = 0;
                            break;
                        }
                    }
                }
                if (fromTextField.getText().length() == 16) {
                    String string = fromTextField.getText();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try {
                        start = format.parse(string);
                    } catch (ParseException ex){
                        logger.error(ex);
                        JOptionPane.showMessageDialog(null, "incorrect start date format");
                    }
                } else {
                    logger.error("Incorrect start date format.");
                    JOptionPane.showMessageDialog(null, "incorrect start date format");
                }

                if (toTextField.getText().length() == 16) {
                    String string = toTextField.getText();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try {
                        end = format.parse(string);
                    } catch (ParseException ex){
                        logger.error(ex);
                        JOptionPane.showMessageDialog(null, "incorrect end date format");
                    }
                } else {
                    logger.error("Incorrect end date format.");
                    JOptionPane.showMessageDialog(null, "incorrect end date format");
                }

                if (end.before(start)) {
                    JOptionPane.showMessageDialog(null, "Error end date more than start date or equals");
                } else if (title.length() > 0 && ((!start.equals(new Date(0)) && !end.equals(new Date(0))))){
                    System.out.println("interval before adding " + interval);
                    Task newTask = new Task(title, start, end, interval * 60);
                    newTask.setActive(active);

                    linkedTaskList.add(newTask);
                    TaskIO.writeText(linkedTaskList, new File("file.txt"));
                    JOptionPane.showMessageDialog(null, "Successfully added");
                    dispose();
                } else {
                    logger.error("Incorrect data for the task.");
                    JOptionPane.showMessageDialog(null, "Incorrect data");
                }
            }
        });

        addButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = "";
                boolean active = false;
                Date time = new Date(0);

                if (titleTextField.getText().length() > 0) {
                    title = titleTextField.getText();
                    System.out.println(titleTextField.getText());
                }

                if (timetextField.getText().length() == 16) {
                    String string = timetextField.getText();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try {
                        time = format.parse(string);
                    } catch (ParseException ex){
                        logger.error(ex);
                        JOptionPane.showMessageDialog(null, "incorrect date format");
                    }
                }

                active = activeCheck.isSelected();
                System.out.println(activeCheck.isSelected());

                if (title.length() > 0 && time.after(new Date())) {
                    Task newTask = new Task(title, time);
                    newTask.setActive(active);


                    LinkedTaskList helpList = linkedTaskList;
                    helpList.add(newTask);
                    TaskIO.writeText(helpList, new File("file.txt"));
                    JOptionPane.showMessageDialog(null, "Successfully added");
                    dispose();
                } else {
                    logger.error("Incorrect data for the task.");
                    JOptionPane.showMessageDialog(null, "Incorrect data");
                }
            }
        });
    }
}
