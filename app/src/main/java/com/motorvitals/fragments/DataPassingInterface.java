package com.motorvitals.fragments;

public interface DataPassingInterface {
    /**
     * Use this method to get data from child fragment into the parent fragment.
     *
     * @param object Data that we want to transmit.
     * @param listIndex Index of the list that contains the element.
     * @param index Index of the element in the list.
     */
    void passingObject(Object object, int listIndex, int index);
}
