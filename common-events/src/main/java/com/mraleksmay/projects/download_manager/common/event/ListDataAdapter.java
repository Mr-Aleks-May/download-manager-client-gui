package com.mraleksmay.projects.download_manager.common.event;

import javax.swing.event.ListDataEvent;

/**
 * ListDataAdapter
 */
public abstract class ListDataAdapter implements javax.swing.event.ListDataListener {
    /**
     * Sent after the indices in the index0,index1
     * interval have been inserted in the data model.
     * The new interval includes both index0 and index1.
     *
     * @param listDataEvent a <code>ListDataEvent</code> encapsulating the
     *                      event information
     */
    @Override
    public void intervalAdded(ListDataEvent listDataEvent) {
    }

    /**
     * Sent after the indices in the index0,index1 interval
     * have been removed from the data model.  The interval
     * includes both index0 and index1.
     *
     * @param listDataEvent a <code>ListDataEvent</code> encapsulating the
     *                      event information
     */
    @Override
    public void intervalRemoved(ListDataEvent listDataEvent) {
    }

    /**
     * Sent when the contents of the list has changed in a way
     * that's too complex to characterize with the previous
     * methods. For example, this is sent when an item has been
     * replaced. Index0 and index1 bracket the change.

     * @param listDataEvent a <code>ListDataEvent</code> encapsulating
     *                      the event information
     */
    @Override
    public void contentsChanged(ListDataEvent listDataEvent) {
    }
}
