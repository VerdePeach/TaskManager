package com.vlad;

import org.apache.log4j.Logger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Сlass that searches for a task at a certain interval.
 */
public class Tasks {

    final static Logger logger = Logger.getLogger(Tasks.class);
    /**
     * Static method that searches tasks at a certain interval
     * @param tasks list of task for searching
     * @param from time when to start.
     * @param to time when to finish.
     * @return Iterable<com.vlad.Task>
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date from, Date to) {
        if (from.getTime() < 0) {
            logger.error("Data execution, execution start time can not be less than 0");
            throw new IllegalArgumentException("The execution"
                    + " start can not be less than 0");
        }
        if (to.getTime() < 0) {
            logger.error("Data execution, execution end time can not be less than 0");
            throw new IllegalArgumentException("The execution finish"
                    + " end can not be less than 0");
        }
        if (to.getTime() <= from.getTime()) {
            logger.error("Data execution, execution end time can not be less or equal to execution start time");
            throw new IllegalArgumentException("The execution finish"
                    + " can not be less or equal to execution start");
        }

        Date helpDate;
        LinkedTaskList subset = new LinkedTaskList();
        for (Task task: tasks) {
            helpDate = task.nextTimeAfter(from);
            if (helpDate == null) {

                continue;
            } else if (helpDate.getTime() > from.getTime() && helpDate.getTime() <= to.getTime()) {
                if (task.isActive()) {
                    subset.add(task);
                }
            }

        }
        return subset;
    }

    /**
     * Static method that searches tasks at a certain interval
     * @param tasks list of task for searching
     * @param from time when to start.
     * @param to time when to finish.
     * @return SortedMap<Date, Set<com.vlad.Task>>
     */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date from, Date to) {
        HashSet<Task> mySet = new HashSet<>();
        SortedMap<Date, Set<Task>> myMap = new TreeMap<>();
        Iterator<Task> myIter = tasks.iterator();
        while (myIter.hasNext()) {
            Task tmp = myIter.next();
            Date tmpDate = (Date) from.clone();
            while (tmp.nextTimeAfter(tmpDate) != null && !tmp.nextTimeAfter(tmpDate).after(to)) {
                if (myMap.get(tmp.nextTimeAfter(tmpDate)) == null) {
                    mySet = new HashSet<>();
                    mySet.add(tmp);
                    myMap.put(tmp.nextTimeAfter(tmpDate), mySet);
                } else {
                    mySet = (HashSet<Task>) myMap.get(tmp.nextTimeAfter(tmpDate));
                    mySet.add(tmp);
                    myMap.put(tmp.nextTimeAfter(tmpDate), mySet);
                }
                tmpDate.setTime(tmpDate.getTime() + tmp.getRepeatInterval());
            }   
        }
        return myMap;
    }
}