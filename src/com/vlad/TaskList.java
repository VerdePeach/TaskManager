package com.vlad;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

abstract class TaskList implements Iterable<Task>, Cloneable, Serializable {
    final static Logger logger = Logger.getLogger(TaskList.class);
    public abstract void add(Task task);
    abstract int size();
    public abstract Task getTask(int index);
    public abstract boolean remove(Task task);
    public abstract String listName();
    public abstract String getAllList();
   
    /**
    *Public method that forms list from tasks in certain interval of time.
    *@param from start time point of interval.
    *@param to end time point of interval.
    *@return list of tasks that will be start in certain interval of time.
    */
    public LinkedTaskList incoming(Date from, Date to) {
        if (from == null) {
            logger.error("Incorrect format of start executing");
            throw new IllegalArgumentException("The execution" 
            + " start can not be less than 0");        
        }
        if (to == null) {
            logger.error("Incorrect format of end executing");
            throw new IllegalArgumentException("The execution finish" 
            + " can not be less than 0");        
        }
        if (to.before(from)) {
            logger.error("Start time execution more than end time execution");
            throw new IllegalArgumentException("The execution finish" 
            + " can not be less or equal to execution start");        
        }

        LinkedTaskList subset = new LinkedTaskList();                   
        for (int i = 0; i < size(); i++) {
            if (getTask(i).nextTimeAfter(from).after(from)
            && getTask(i).nextTimeAfter(from).getTime() <= to.getTime()) {
                if (getTask(i).isActive()) {
                    subset.add(getTask(i));
                }                    
            }                
        }                
        return subset;
    }

    /**
     * Public method that return myListIterator
     * @return myListIterator
     */
    @Override
    public Iterator<Task> iterator() {
        return new myListIterator();
    }

    /**
     * Private class myListIterator that implements Iterator<com.vlad.Task> and override methods.
     */
    private class myListIterator implements Iterator<Task> {
        
        private int index = 0;
        private Task helpTask;

        /**
         * Method that checks presence of next element.
         * @return true or false
         */
        @Override
        public boolean hasNext() {
            if (index < size()) {
                return true;
            }          
            return false;
        }

        /**
         * Method returns next task in the list.
         * @return task
         */
        @Override 
        public Task next() {
            if (hasNext()) {
                helpTask = getTask(index);
                return getTask(index++);
            } else {
                logger.error("No such element exception");
                throw new NoSuchElementException();
            }
        }

        /**
         * Method that remove element from the list.
         */
        @Override 
        public void remove() {
            if (index > 0 && getTask(index - 1) == helpTask
                && index <= size()) {
                TaskList.this.remove(helpTask);
                index--;
            } else {
                logger.error("Impossible to remove task. Task with such index dose not exist");
                throw new IllegalStateException();
            }
            
        }
    }

    /**
     * Method compare two Lists.
     * @param obj List
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        TaskList helpList = (TaskList) obj;
        for (int i = 0; i < size(); i++) {
            if (!TaskList.this.getTask(i).equals(helpList.getTask(i)) 
            && !TaskList.this.getTask(i).getTime().equals(helpList.getTask(i).getTime())
            && !TaskList.this.getTask(i).getStartTime().equals(helpList.getTask(i).getStartTime())
            && !TaskList.this.getTask(i).getEndTime().equals(helpList.getTask(i).getEndTime()))
                return false;
        }
        return TaskList.this.size() == helpList.size(); 
    }

    /**
     * Method that makes a hash-code.
     * @return hashCode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result;
        result = prime * ((size() % 2 == 0) ? size() + 1 : size());
        return result;
    }

    /**
     * Method that return name and size of list.
     * @return String.
     */
    @Override
    public String toString() {
        return TaskList.this.listName() + ", size = " + size();
    }
    
}