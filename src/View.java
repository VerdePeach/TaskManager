
import org.apache.log4j.Logger;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Public class that contains main window of the programme.
 * class View extends JFrame
 */
public class View extends JFrame {

    final static Logger logger = Logger.getLogger(View.class);
    private JButton weekPlan = new JButton("Week tasks");
    private JButton addTask = new JButton("Add task");
    private JButton editTask = new JButton("Edit task");
    private JButton detailView = new JButton("Detail view");
    private JButton removeTask = new JButton("Remove task");
    private JButton calendar = new JButton("Calendar");
    private JButton update = new JButton("Update");

    private LinkedTaskList myNewList;
    private String masTasks[];
    private JList taskList;

    /**
     * Constructor that create main window and its content.
     * @param myList
     */
    View(LinkedTaskList myList){
        myNewList = myList;
        setJList(myNewList);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(50, 25, 1200 ,700);
        setResizable(false);
        setTitle("Task Manager");


//Buttons
        weekPlan.setBackground(Color.ORANGE);
        addTask.setBackground(Color.ORANGE);
        editTask.setBackground(Color.ORANGE);
        detailView.setBackground(Color.ORANGE);
        removeTask.setBackground(Color.ORANGE);
        calendar.setBackground(Color.ORANGE);
        update.setBackground(Color.ORANGE);

        weekPlan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewWeek(myNewList);
            }
        });

         addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new View(1, myNewList);
            }
        });

        editTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ViewEdit(taskList.getSelectedIndex(), myNewList);
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Task didn`t choose");
                }
            }
        });

        calendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingCalendar sc = new SwingCalendar(myNewList);
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(myNewList);
            }
        });


//Panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.darkGray);

        panel.add(weekPlan);
        panel.add(addTask);
        panel.add(editTask);
        panel.add(detailView);
        panel.add(removeTask);
        panel.add(calendar);
        panel.add(update);



        removeTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (taskList.getSelectedIndex() == -1) {
                        throw new IOException();
                    }
                    new ViewRemove(taskList.getSelectedIndex(), myNewList);
                } catch (Exception ex) {
                    logger.error("A task for removing did not choose.");
                    JOptionPane.showMessageDialog(null, "Task didn`t choose");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(700,500));
        panel.add(scrollPane);
        setContentPane(panel);

        detailView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DetailView(myNewList);
            }
        });

        setVisible(true);
    }

    /**
     * A constructor that creates an auxiliary window when a client wants to add a task.
     * @param key
     * @param myNewList
     */
    View(int key, LinkedTaskList myNewList) {
        if(key == 1) { // add task
            JLabel repeatLabel = new JLabel("Is Your task repeated?");
            JButton repeated = new JButton("Yes");
            JButton notRepeated = new JButton("No");

            repeated.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new ViewAdd("yes", myNewList);
                }
            });

            notRepeated.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new ViewAdd("no", myNewList);
                }
            });

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());

            panel.add(repeatLabel);
            panel.add(repeated);
            panel.add(notRepeated);

            setContentPane(panel);
            setBounds(900,300,300,100);
            setTitle("Add Task");
            setResizable(false);
            setVisible(true);
        }
    }

    /**
     * Method that sets content of JList that will be displayed in main window.
     * @param myList
     */
    private void setJList(LinkedTaskList myList) {
        masTasks = new String[myList.size()+5];
        for (int i = 0; i < myList.size(); i++) {
           masTasks[i] = myList.getTask(i).toString2();
        }
        taskList = new JList(masTasks);
    }

    /**
     * Method closes main window and after that creates new one with updated information.
     * @param linkedTaskList
     */
    private void update(LinkedTaskList linkedTaskList){
        dispose();
        new View(linkedTaskList);
    }
}
