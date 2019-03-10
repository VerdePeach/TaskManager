import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Public class that responsible for removing the task from the sheet.
 * class ViewRemove extends JFrame.
 */
public class ViewRemove extends JFrame {

    /**
     * Constructor that out put window that asks a customer delete task or no.
     * @param index
     * @param linkedTaskList
     */
    ViewRemove(int index, LinkedTaskList linkedTaskList){

        JButton yes = new JButton("YES");
        JButton no = new JButton("NO");
        JLabel question = new JLabel("Delete task?");
        JPanel panel = new JPanel();

        setBounds(900,400,300,100);
        setTitle("Remove Task");
        setResizable(false);

        question.setForeground(Color.white);
        yes.setBackground(Color.ORANGE);
        no.setBackground(Color.ORANGE);

        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.darkGray);
        panel.add(question);
        panel.add(yes);
        panel.add(no);

        add(panel);
        setVisible(true);

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Task delTask = linkedTaskList.getTask(index);
                linkedTaskList.remove(linkedTaskList.getTask(index));

                TaskIO taskIO = new TaskIO();
                taskIO.writeText(linkedTaskList, new File("file.txt"));
                JOptionPane.showMessageDialog(null,"Task removed successfully");
                dispose();
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}
