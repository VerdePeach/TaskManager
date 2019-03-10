import org.apache.log4j.Logger;

/**
* Public class ArrayList for working with some tasks,
* forming list of task.
*/
public class ArrayTaskList extends TaskList implements  Cloneable {
    final static Logger logger = Logger.getLogger(ArrayTaskList.class);
    final int firstTime = 10;
    private int arrSize = firstTime;
    private Task[] myArrayTasks = new Task[arrSize];
    private int i = 0;

    /**
    * Method that add element to ArrayTaskList.
    * @param task that is adding to list.
    */
    @Override
    public void add(Task task) { 
        if (task == null) {
            logger.error("An attempt to add empty task.");
            throw new IllegalArgumentException("Empty task can not be at List");
        } else {
            myArrayTasks[i++] = task;
            if (i == arrSize - 1) {
                Task[] newMyArray = new Task[arrSize * 2];
                for (int k = 0; k < arrSize; k++) {
                    newMyArray[k] = myArrayTasks[k];
                }
                myArrayTasks = newMyArray;
                arrSize = arrSize * 2;
            }
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
        for (int j = 0; j < arrSize; j++) {
            if (task == myArrayTasks[j]) {
                for (int k = j; k < arrSize - 1; k++) {
                   myArrayTasks[k] = myArrayTasks[k + 1];   
                }
                i--;
                return true;
            }
        }
        
        logger.info("The task did not found at the list.");
        return false;
    }

    /**
    * Method that return size of ArrayList/
    * @return int size.
    */
    @Override
    public int size() { 
        int kol = 0;
        for (int j = 0; j < arrSize; j++) {
            if (myArrayTasks[j] != null) {
                kol++;
            } 
        }
        return kol;
    }

    /**
    * Method that return task from list by index.
    * @param index index of task that mast be returned.
    * @return task.
    */
    @Override
    public Task getTask(int index) { 
        if (index >= arrSize || index < 0) {
            logger.error("Incorrect index less than 0 or more than number of tasks in the list.");
            throw new IllegalArgumentException("Incorrect index");
        } else {
            return myArrayTasks[index];
        }
    }

    /**
     * Method return name of class
     * @return "Array Task List "
     */
    @Override
    public String listName() {
        return "Array Task List ";
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
        System.out.println("Tasks List: ");
            for (int i = 0; i < size(); i++) {
                System.out.println("Index is " + i + ". Name of the task is "
                + myArrayTasks[i].getTitle());
            }
        return "";
    }

    /**
     * Method makes a clone of the ArrayTaskList.
     * @throws CloneNotSupportedException
     * @return coned arrayTaskList
     */
    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList helpList = (ArrayTaskList) super.clone();
        
        Task [] helpArrList = new Task[arrSize];
        for (int i = 0; i < size(); i++) {
           helpArrList[i] = (Task)this.myArrayTasks[i].clone();
        }
        helpList.myArrayTasks = helpArrList;
        return helpList;
    }
}



