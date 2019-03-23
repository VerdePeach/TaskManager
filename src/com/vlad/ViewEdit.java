package com.vlad;

import org.apache.log4j.Logger;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class contains window that display full information about tasks.
 * class com.vlad.ViewEdit extends JFrame.
 */
public class ViewEdit extends JFrame {

    final static Logger logger = Logger.getLogger(ViewEdit.class);

    /**
     * Constructor that create window for editing task.
     * @param index
     * @param linkedTaskList
     */
    ViewEdit(int index, LinkedTaskList linkedTaskList){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        setBounds(900,400,300,300);
        setTitle("Edit Task");
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.darkGray);

        JCheckBox activeCheck = new JCheckBox("Active");
        JLabel titleLabel = new JLabel("Title");

        JTextField titleTextField = new JTextField("title1", 20);
        JButton setButton = new JButton("Set");
        JButton setButton1 = new JButton("Set");

        titleLabel.setForeground(Color.orange);

        panel.add(titleLabel);
        titleTextField.setText(linkedTaskList.getTask(index).getTitle());
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

        if (linkedTaskList.getTask(index).isRepeated()) {
                fromLable.setForeground(Color.orange);
                panel.add(fromLable);
                fromTextField.setText(simpleDateFormat.format(linkedTaskList.getTask(index).getStartTime()));
                panel.add(fromTextField);

                toLabel.setForeground(Color.orange);
                panel.add(toLabel);
                toTextField.setText(simpleDateFormat.format(linkedTaskList.getTask(index).getEndTime()));
                panel.add(toTextField);

                intervalLable.setForeground(Color.orange);
                panel.add(intervalLable);
                intervalTextField.setText("" + linkedTaskList.getTask(index).getRepeatInterval()/60);
                panel.add(intervalTextField);
                panel.add(setButton);
        } else {
            timeLable.setForeground(Color.orange);
            panel.add(timeLable);


            timetextField.setText(simpleDateFormat.format(linkedTaskList.getTask(index).getTime()));
            panel.add(timetextField);
            panel.add(setButton1);
        }
        if (linkedTaskList.getTask(index).isActive())
            activeCheck.setSelected(true);
        panel.add(activeCheck);
        add(panel);
        setVisible(true);


        setButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = "";
                boolean active = false;
                Date time = new Date(0);

                if (titleTextField.getText().length() > 0) {
                    title = titleTextField.getText();
                    linkedTaskList.getTask(index).setTitle(title);
                    System.out.println(titleTextField.getText());
                }

                if (timetextField.getText().length() == 16) {
                    String string = timetextField.getText();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try {
                        time = format.parse(string);
                        linkedTaskList.getTask(index).setTime(time);
                    } catch (ParseException ex){
                        logger.error(ex);
                        JOptionPane.showMessageDialog(null, "incorrect date format");
                    }
                }

                active = activeCheck.isSelected();
                linkedTaskList.getTask(index).setActive(active);
                System.out.println(activeCheck.isSelected());

                if (title.length() > 0 && time.after(new Date())) {
                    TaskIO.writeText(linkedTaskList, new File("file.txt"));
                    JOptionPane.showMessageDialog(null, "Successfully edited");
                } else {
                    logger.error("Incorrect data for the task");
                    JOptionPane.showMessageDialog(null, "Incorrect data");
                }
            }
        });

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int interval = 0;
                String title = "";
                boolean active = false;
                Date start = new Date(0);
                Date end = new Date(0);
                if (titleTextField.getText().length() > 0) {
                    title = titleTextField.getText();
                    linkedTaskList.getTask(index).setTitle(title);
                    System.out.println(titleTextField.getText());
                }

                active = activeCheck.isSelected();
                linkedTaskList.getTask(index).setActive(active);
                System.out.println(activeCheck.isSelected());

                if (intervalTextField.getText().length() > 0) {
                    char[] arrInterval = intervalTextField.getText().toCharArray();
                    for(int i = 0; i < arrInterval.length; i++){
                        if(arrInterval[i] == '1' || (int)arrInterval[i] == '2' || (int)arrInterval[i] == '3'
                                || arrInterval[i] == '4' || (int)arrInterval[i] == '5' || (int)arrInterval[i] == '6'
                                || arrInterval[i] == '7' || (int)arrInterval[i] == '8' || (int)arrInterval[i] == '9'
                                || arrInterval[i] == '0'){
                            interval = Integer.parseInt(intervalTextField.getText());
                            System.out.println(interval);
                        } else {
                            logger.error("Incorrect interval format.");
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

                    linkedTaskList.getTask(index).setTime(start, end, interval*60);
                    TaskIO.writeText(linkedTaskList, new File("file.txt"));
                    JOptionPane.showMessageDialog(null, "Successfully edited");
                } else {
                    logger.error("Incorrect data for the task.");
                    JOptionPane.showMessageDialog(null, "Incorrect data");
                }
            }
        });
    }
}
