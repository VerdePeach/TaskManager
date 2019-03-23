package com.vlad;

/**
*public class that makes nodes for com.vlad.LinkedTaskList
*/
public class LinkNode implements  Cloneable {

    LinkNode next;
    LinkNode prev;
    Task task;

    /**
     * Constructor that creates node with next parameters.
     * @param task task to set
     * @param next point on next node
     * @param prev point on previous node
     */
    LinkNode(Task task, LinkNode next, LinkNode prev) {
        this.task = task;
        this.next = next; 
        this.prev = prev;
    }

    /**
     *Constructor that creates node that makes all elements of node null.
     */
    LinkNode() {
        this.task = null;
        this.next = null;
        this.prev = null;
    }

    /**
     * Method that makes a clone of Node
     * @return cloned node.
     * @throws CloneNotSupportedException
     */
    @Override
    public LinkNode clone() throws CloneNotSupportedException {
            return (LinkNode) super.clone();
    }
}