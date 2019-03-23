package com.vlad;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

/**
 * Public class contains window that display information about task for next 7 days.
 * class com.vlad.ViewWeek extends JFrame
 */
public class ViewWeek extends JFrame {

    /**
     * Constructor creates window that display tasks nearest 7 days.
     * @param linkedTaskList
     */
    ViewWeek(LinkedTaskList linkedTaskList){
        Date curentDate = new Date();
        LinkedTaskList weekTasks = (LinkedTaskList)Tasks.incoming(linkedTaskList, curentDate, new Date(curentDate.getTime() + 24 * 60 * 7 * 60000));
        if (weekTasks.size() == 0) {
            JOptionPane.showMessageDialog(null,
                    "No tasks to next week",
                    "Notification",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            String [] weekList = getMas(weekTasks);
            setBounds(100,50,1000,800);
            setTitle("Plan to week - Task Manager");
            setResizable(false);

            JList weekJList = new JList(weekList);

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.setBackground(Color.darkGray);
            JScrollPane scrollPane = new JScrollPane(weekJList);
            scrollPane.setPreferredSize(new Dimension(950,730));
            panel.add(scrollPane);
            setContentPane(panel);

            setVisible(true);
        }
    }

    /**
     * Method creates string array for displaying in view week window.
     * @param list
     * @return masTasks
     */
    private String []getMas(LinkedTaskList list){
        String []masTasks = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            masTasks[i] = list.getTask(i).toString2();
        }
        return masTasks;
    }
}
