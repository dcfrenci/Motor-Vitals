package com.motorvitals.adapter;

public interface RecyclerViewInterface {
    /**
     * Perform action when a Card of RecyclerView is clicked.
     * @param position Specified the list
     * @param positionElement Specified the element in the list
     */
    void onCardClick(int position, int positionElement);

    /**
     * Delete the Card of RecyclerView that was clicked.
     * @param listIndex Specified the list
     * @param index Specified the element in the list
     */
    void onCardDelete(int listIndex, int index);
}
