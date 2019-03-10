import org.apache.log4j.Logger;

/**
* Public class that extends abstract class TaskList.
* In this class created List that contains tasks.
*/
public class LinkedTaskList extends TaskList implements Cloneable {

    final static Logger logger = Logger.getLogger(LinkedTaskList.class);
    private LinkNode first = new LinkNode();
    private LinkNode last = first;
    private int counter = 0;
    
    /**
    * Method that adds element to LinkedTaskList.
    * @param task that is adding to list.
    */
    @Override
    public void add(Task task) {
        if (task == null) {
            logger.error("An attempt to add empty task.");
            throw new NullPointerException("Task is empty");
        } else {
            LinkNode help = new LinkNode(task, first, last); 
            first.prev = help;
            last.next = help;
            last = help;
            counter++;
        }
    }

    /**
    * Method that delete element(task) from list and if task was at the list
    * method returns true. If in list more then one task that we are looking for
    * method deletes only 1 of n.
    * @param task that task that must be deleted.
    * @return true or false.
    */
    @Override
    public boolean remove(Task task) {
        if (task == null) {
            logger.error("An attempt to remove empty task.");
            throw new IllegalArgumentException("Removing task is empty!");
        }
        LinkNode help = first.next;
        while (/*help.task.getTitle() != null ||*/ !help.task.equals(task)) { //corrected
            help = help.next;
        }
        if (help.task != null) {
            if (help.next.task == null) {
                last = help.prev;
            }
            help.prev.next = help.next;
            help.next.prev = help.prev;
            counter--;
            return true;
        }
        return false;
    }

    /**
    * Method that return size of LinkedList.
    * @return int size.
    */
    @Override
    public int size() {
        return counter;
    }

    /**
    * Method that return task from list by index.
    * @param index index of task that mast be returned.
    * @return task.
    */
    @Override
    public Task getTask(int index) {
        if (index >= counter || index < 0) {
            logger.error("Incorrect index less than 0 or more than number of tasks in the list.");
            throw new IllegalArgumentException("Incorrect index");
        }
        LinkNode help = first.next;
        int i = 0;
        while (help != null && index != i) {
            i++;
            help = help.next;
        }
        if (help.task != null) {
            return help.task;
        } else {
            return null;
        }
    }
    
    
    /**
    * Method that create clone of list.
    * @throws CloneNotSupportedException
    * @return List that was cloned.
    */
    public LinkedTaskList clone() throws CloneNotSupportedException {
        LinkedTaskList helpList = (LinkedTaskList) super.clone(); 
        helpList.first = null;
        helpList.last = null;
        helpList.counter = 0;
        
        helpList.first = new LinkNode();
        helpList.last = helpList.first;
        for (LinkNode x = first.next; x.task != null; x = x.next) {
            LinkNode help = new LinkNode(x.task, helpList.first,
            helpList.last); 
            
            helpList.first.prev = help;
            helpList.last.next = help;
            helpList.last = help;
            helpList.counter++;
        }
        return helpList;
    }

    /**
     * Method return name of class
     * @return "Linked Task List "
     */
    @Override
    public String listName() {
        return "Linked Task List ";
    }

    /**
     * Method output Tasks list.
     * That included index and name
     * @return ""
     */
    @Override
    public String getAllList() {
        if (size() == 0)
            return "";
        LinkNode help = first.next;
        int i = 0;
        System.out.println("Tasks of List: ");
        while (help.task != null) {
        System.out.println("Index is " + i + ". Name is "
        + help.task.getTitle());
            i++;
            help = help.next;
        }
        return "";
    }
}

