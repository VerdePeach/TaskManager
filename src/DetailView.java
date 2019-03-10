import org.apache.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Public class DetailView that extends class JFrame.
 * It creates the window with full, detail information about Tasks.
 */
public class DetailView extends JFrame {

    final static Logger logger = Logger.getLogger(DetailView.class);
    private String masTasks[];
    private JList taskJList;

    /**
     * Class constructor.
     * It creates a window that contains information about tasks.
     * @param linkedTaskList list of tasks that will be displayed.
     */
    DetailView(LinkedTaskList linkedTaskList) {
        setJList(linkedTaskList);
        setBounds(100, 50, 900 ,700);
        setResizable(false);
        setTitle("Detail View");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.darkGray);

        JScrollPane scrollPane = new JScrollPane(taskJList);
        scrollPane.setPreferredSize(new Dimension(700,500));
        panel.add(scrollPane);
        setContentPane(panel);

        setVisible(true);
        logger.info("Window DetailView successfully created");
    }

    /**
     * Method set String information about tasks to the JList.
     * @param myList list of tasks that used to create JList.
     */
    private void setJList(LinkedTaskList myList) {
        masTasks = new String[myList.size()+5];
        for (int i = 0; i < myList.size(); i++) {
            masTasks[i] = myList.getTask(i).toString();
        }
        taskJList = new JList(masTasks);
    }
}
