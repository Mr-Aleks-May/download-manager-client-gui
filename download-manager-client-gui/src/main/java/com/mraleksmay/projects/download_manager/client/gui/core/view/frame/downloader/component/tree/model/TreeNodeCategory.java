package com.mraleksmay.projects.download_manager.client.gui.core.view.frame.downloader.component.tree.model;


import com.mraleksmay.projects.download_manager.common.model.category.Category;

public class TreeNodeCategory {
    private final Category category;
    private int childesCount;


    public TreeNodeCategory(Category category) {
        this.category = category;
    }


    public int getChildesCount() {
        return childesCount;
    }

    public void setChildesCount(int childesCount) {
        this.childesCount = childesCount;
    }

    public int increaseDC() {
        return ++this.childesCount;
    }

    public void resetChildesCount() {
        this.childesCount = 0;
    }

    public Category getCategory() {
        return category;
    }


    @Override
    public String toString() {
        // if (!category.getName().equalsIgnoreCase("All"))
        return String.format("%s (%d)", category.getName(), childesCount);
        //else
        //   return category.getName();
    }
}
