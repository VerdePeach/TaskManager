
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;
import java.util.Locale;

/**
 * Class Task, where tasks is created.
 */
public class Task implements Cloneable, Serializable {

    final static Logger logger = Logger.getLogger(Task.class);
    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;
    private boolean repeat;

    public Task(){}
    /**
     * First class constructor.
     * Constructs an inactive task that runs at a given time
     * without repeating and has a given name.
     * @param title some String name of task.
     * @param time time when task will be started(integer value).
     */
    public Task(String title, Date time) {
        this.title = title;
        if (time.getTime() < 0) {
            logger.error("Incorrect time value, time less than 0.");
            throw new IllegalArgumentException("Incorrect time");
        } else {
            this.time = time;
        }

        active = false;
        repeat = false;
    }

    /**
     * Second class constructor.
     * Constructs an inactive task that runs at a given time
     * with repeating(beginning and end inclusive)
     * with a given interval and has a given name.
     * @param title some String name of task.
     * @param start time when task will be started(integer value) and > 0.
     * @param end time when task will be stopped(integer value) and > 0.
     * @param interval interval-time between tasks(integer value) and > 0.
     */
    public Task(String title, Date start, Date end, int interval) {
        this.title = title;
        if (start.getTime() < 0) {
            logger.error("Incorrect start time value, start time less than 0.");
            throw new IllegalArgumentException("Incorrect start time");
        } else {
            this.start = start;
        }

        if (end.before(start) || end.getTime() < 0) {
            logger.error("Incorrect end time value, end time less than 0 or less than start time.");
            throw new IllegalArgumentException("Incorrect end time");
        } else {
            this.end = end;
        }

        if (interval  <= 0 || end.getTime() <=  start.getTime() + (long) (interval) * 1000) {
            logger.error("Incorrect interval. It is less or equals 0 or start time + interval more than end time");
            throw new IllegalArgumentException("Incorrect interval");
        } else {
            this.interval = interval;
        }
        active = false;
        repeat = true;
    }

    /**
     * Method for reading the task title.
     * @return task title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method for change the task title.
     * @param title some String name of task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method for reading the task status .
     * Is the status active or passive(true or false).
     * @return false or true.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method for writing the task status .
     * It can be active or passive(true or false).
     * @param active boolean value. Contains information about the task status
     * (active of passive).
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Method for reading time for task that not repeating.
     * If task repeating, method return start time of repeating.
     * @return int value > 0 that shows when one-time task will be started.
     */
    public Date getTime() {
        if (repeat) {
            return start;
        } else {
            return time;
        }
    }

    /**
     * Method for changing time for task that not repeating.
     * If task repeating, method makes task not repeating.
     * @param time time for a one-time task.
     */
    public void setTime(Date time) {
        if (repeat) {
            repeat = false;
            start.setTime(0);
            end.setTime(0);
            interval = 0;
        }
        if (time.getTime() < 0) {
            logger.error("Incorrect execution time, less than 0.");
            throw new IllegalArgumentException("Incorrect time");
        } else {
            this.time = time;
        }
    }

    /**
     * Method for reading start time for tasks that repeating.
     * If task not repeating, method return doing task time.
     * @return time int value > 0 shows when not one-time task will be started.
     */

    public Date getStartTime() {
        if (!repeat) {
            return time;
        } else {
            return start;
        }
    }

    /**
     * Method for reading end time for tasks that is repeating.
     * If task not repeating, method return doing task time.
     * @return int value > 0 that show when task will be finished.
     */
    public Date getEndTime() {
        if (!repeat) {
            return time;
        } else {
            return end;
        }
    }

    /**
     * Method for reading time-interval for tasks that repeating.
     * If task not repeating, method return 0.
     * @return int value > 0 that shows interval between tasks for repeating task.
     */
    public int getRepeatInterval() {
        if (!repeat) {
            return 0;
        } else {
            return interval;
        }
    }

    /**
     *Method for writing time(start, end, interval) for tasks that repeating
     * If task not repeating, method change this task to repeating.
     * @param start (int value > 0 and < end) start-time for task.
     * @param end (int value > 0 and > start) end-time for task.
     * @param interval (int value > 0) interval between tasks.
     */
    public void setTime(Date start, Date end, int interval) {
        if (start.getTime() < 0) {
            logger.error("Incorrect execution start time less than 0.");
            throw new IllegalArgumentException("Incorrect start time");
        } else {
            this.start = start;
        }

        if (end.getTime() < start.getTime() || end.getTime() < 0) {
            logger.error("Incorrect end time value, end time less than 0 or less than start time.");
            throw new IllegalArgumentException("Incorrect end time");
        } else {
            this.end = end;
        }

        if (interval  <= 0 || end.getTime() <= start.getTime() + interval * 1000) {
            logger.error("Incorrect interval. It is less or equals 0 or start time + interval more than end time");
            throw new IllegalArgumentException("Incorrect interval");
        } else {
            this.interval = interval;
        }
        repeat = true;
    }

    /**
     * Method for verifying the repetition of the task.
     * @return boolean value shows is task repeat or not repeat.
     */
    public boolean isRepeated() {
        return repeat;
    }

    /**
     * The method that returns the time of the next task execution
     * after the specified time "current".
     * If the task is not executed after the specified time,
     * then the method has to return -1.
     * @param current point of time that inputs to know next
     * start-time after current.
     * @return start-time after current time that was inputted.
     */
    public Date nextTimeAfter(Date current) {
        if (!active || current == null) {
            return null;
        } else if (time != null) {
            if (current.getTime() < time.getTime() && start == null && end == null) {
                return time;
            }
        } else if (start != null && end != null) {
            if (current.getTime() < start.getTime()) {
                return start;
            } else if (current.getTime() >= start.getTime() && !current.equals(end)) {
                Date flag = (Date) start.clone();
                while (flag.getTime() < end.getTime()) {
                    if (flag.getTime() <= current.getTime()) {
                        flag.setTime(flag.getTime() + interval * 1000);
                    } else {
                        break;
                    }
                }
                if (flag.after(end)) {
                    flag = null;
                }
                return flag;
            }
        }
        return null;
    }

    /**
     * Method that compares equality of tasks.
     * @param obj
     * @return true or false. If task are equals it returns true.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Task helpTask = (Task) obj;
        return (this.getTitle().equals(helpTask.getTitle())
                && this.getTitle() == helpTask.getTitle()
                && this.getTime().equals(helpTask.getTime())
                && this.isActive() == helpTask.isActive()
                && this.isRepeated() == helpTask.isRepeated()
                && this.getStartTime().equals(helpTask.getStartTime())
                && this.getEndTime().equals(helpTask.getEndTime()));
    }

    /**
     * Method that make a hashCode by task information.
     * @return hashCode
     */
    @Override
    public int hashCode() {

        final int one = 1;
        final int two = 1;
        final int three = 1;
        final int five = 5;
        final int seven = 7;
        final int eleven = 11;
        final int prime = 31;
        int result = 1;

        result = prime * ((getTitle().length() / 2 == 0) ? getTitle().length()
                : getTitle().length() + one);

        result += prime * ((getTime().getTime() % 2 == 0) ? getTime().getTime() : getTime().getTime() + one);

        result += prime * ((isActive()) ? 2 : three);

        result += prime + ((isRepeated()) ? eleven : seven);

        result += prime + ((getStartTime().getTime() % five == 0) ? getStartTime().getTime() + one
                : getStartTime().getTime() + two);

        result += prime + ((getEndTime().getTime() % 2 == 0) ? getEndTime().getTime() + one
                : getEndTime().getTime() + two);

        return result;
    }

    /**
     * Method that creates the string representation of the task.
     * @return string representation of the task
     */
    @Override
    public String toString() {
        if(isRepeated()){
            return "Task title is " + getTitle() + ". Start time is " + getTime()
                    +  ". End time is " + getEndTime() + ". Interval is "
                    + getRepeatInterval()/60  + " minutes. Task activity is " + isActive() + ".";
        } else {
            return "Task title is " + getTitle() + ". Start time is " + getTime()
                    + ". Task activity is " + isActive() + ".";
        }
    }

    /**
     * Method that creates clone of task.
     * @return cloned task
     * @throws CloneNotSupportedException
     */
    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }

    /**
     * Method that creates the string representation of the task with special Date "yyyy-MM-dd HH:mm".
     * @return string representation of the task
     */
    public String toString2() {
        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (isRepeated()) {
            return getTitle() + ". Start time: " + newDate.format(getTime())
                    +  ". End time: " + newDate.format(getEndTime()) + ". Interval: "
                    + getRepeatInterval()/60 + " minutes.";
        } else {
            return getTitle() + ". Time: " + newDate.format(getTime()) + ".";
        }
    }

    /**
     * Method that return year, when task must start.
     * @return year.
     */
    public int getYear() {
        SimpleDateFormat newDate = new SimpleDateFormat("yyyy");
        return Integer.valueOf(newDate.format(getTime()));
    }

    /**
     * Method that return month, when task must start.
     * @return month.
     */
    public String getMonth() {
        SimpleDateFormat newDate = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        return newDate.format(getTime());
    }

    /**
     * Method that return day, when task must start.
     * @return day.
     */
    public int getDay() {
        SimpleDateFormat newDate = new SimpleDateFormat("dd");
        return Integer.valueOf(newDate.format(getTime()));
    }
/*
    public Date getStepTime(Date stepDate){
        stepDate = new Date(stepDate.getTime() + interval);
      return stepDate;
    }
*/
}


