package com.mraleksmay.projects.download_manager.common.model.plugin;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.Restoreable;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.group.Group;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Holds plugin information
 */
public abstract class Plugin implements Restoreable {
    /**
     * Unique plugin identifier used during serialization.
     */
    @Expose
    private String serializableId = "null";
    /**
     * Plugin identifier
     */
    @Expose
    private String id;
    /**
     * Plugin name
     */
    @Expose
    private String name;
    /**
     * Contains categories
     */
    @Expose
    private List<Group> groups;
    /**
     * Contains information about the dialog window class used to add a new load.
     * Used when deserializing.
     */
    @Expose
    private String downloadDialogClassStr;
    /**
     * The class of a specific implementation of this plugin. Used when deserializing.
     */
    @Expose
    private String classStr;
    /**
     * Plugin can handle this downloads from specified websites.
     */
    private List<String> supportedWebSitesRegex;
    /**
     * Plugin priority.
     */
    private int priority;
    /**
     * Specify, if user can download this file later.
     */
    private boolean userCanDownloadLater = false;


    {
        setClassStr(this.getClass());
    }

    // Constructors
    public Plugin() {
    }

    public Plugin(@NotNull final String id,
                  @NotNull final String name) {
        this.id = id.toUpperCase();
        this.name = name;
    }

    // Methods
    public static Plugin createInstance(@NotNull final Class<?> pluginClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<?> pluginConstructor = pluginClass.getConstructor();
        final Plugin plugin = (Plugin) pluginConstructor.newInstance();

        return plugin;
    }

    public static Plugin createInstance(@NotNull final Class<? extends Plugin> pluginClass,
                                        final String id,
                                        final String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<?> pluginConstructor = pluginClass.getConstructor(new Class[]{String.class, String.class});
        final Plugin plugin = (Plugin) pluginConstructor.newInstance(new Object[]{id, name});

        return plugin;
    }

    public abstract void init() throws IOException;

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void deserialize(Object pobj) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Plugin plugin = this;
        Map<String, Object> pmap = (Map<String, Object>) pobj;
        List<Category> categoriesList = new ArrayList<>();
        List<Group> groupsList = new ArrayList<>();

        plugin.setSerializableId((String) pmap.get("serializableId"));
        plugin.setId((String) pmap.get("id"));
        plugin.setName((String) pmap.get("name"));
        plugin.setName((String) pmap.get("name"));


        for (Object gobj : (List<Object>) pmap.get("groups")) {
            Map<String, Object> gmap = (Map<String, Object>) gobj;

            for (Object cobj : (List<Object>) gmap.get("categories")) {
                Map<String, Object> cmap = (Map<String, Object>) cobj;
                String classStr = (String) cmap.get("classStr");
                Category category = (Category) Class.forName(classStr).newInstance();
                category.deserialize(cmap);
                category.setPlugin(plugin);

                categoriesList.add(category);
            }

            break;
        }

        for (Object gobj : (List<Object>) pmap.get("groups")) {
            Map<String, Object> gmap = (Map<String, Object>) gobj;
            String classStr = (String) gmap.get("classStr");
            Group group = (Group) Class.forName(classStr).newInstance();
            group.deserialize(gmap);
            group.setParent(plugin);
            group.setCategories(categoriesList);

            groupsList.add(group);
        }

        plugin.setGroups(groupsList);
    }


    public synchronized void add(@NotNull final Group group) {
        getGroupsList().add(group);
    }

    public synchronized void remove(@NotNull final Category category) {
        for (Group group : getGroups()) {
            group.remove(category);
        }
    }

    public Group getGroupBy(@NotNull String groupId) {
        groupId = groupId.toUpperCase();

        for (Group group : getGroups()) {
            if (group.getId().equals(groupId)) {
                return group;
            }
        }

        return null;
    }

    public void addSupportedWebSiteRegex(String supportedWebSiteRegex) {
        getSupportedWebSitesRegexList().add(supportedWebSiteRegex);
    }

    //(http|https)?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)
    public boolean canHandle(URL url) {
        for (String supportedWebSite : getSupportedWebSitesRegex()) {
            String supportedWebSiteRegex = "^(http|https)?:\\/\\/(www\\.)?" + supportedWebSite + "\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
            Pattern pattern = Pattern.compile(supportedWebSiteRegex);
            Matcher matcher = pattern.matcher(url.toString());

            if (matcher.find()) {
                return true;
            }
        }

        return false;
    }

    public abstract Category getDefaultCategory();


    // Getters and Setters
    protected synchronized List<Group> getGroupsList() {
        if (groups == null) {
            groups = new ArrayList<>();
        }

        return groups;
    }

    public synchronized List<Group> getGroups() {
        return Collections.unmodifiableList(getGroupsList());
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getSerializableId() {
        return serializableId;
    }

    public void setSerializableId(String serializableId) {
        this.serializableId = serializableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JDialog getDownloadDialog(Class<?>[] constructorArgsType, Object[] constructorParams) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Class<? extends JDialog> dialogClass = this.getDownloadDialogClassStr();
        final Constructor<? extends JDialog> dialogConstructor = dialogClass.getDeclaredConstructor(constructorArgsType);
        final JDialog downloadInformationDialog = dialogConstructor.newInstance(constructorParams);

        return downloadInformationDialog;
    }

    public void setDownloadDialog(JDialog downloadDialog) {
        setDownloadDialogClass(downloadDialog.getClass());
    }

    public Class<? extends JDialog> getDownloadDialogClassStr() throws ClassNotFoundException {
        Class<?> clazz = this.getClass().getClassLoader().loadClass(downloadDialogClassStr);

        return (Class<? extends JDialog>) clazz;
    }

    public void setDownloadDialogClass(Class<? extends JDialog> downloadDialogClass) {
        this.downloadDialogClassStr = downloadDialogClass.getCanonicalName();
    }

    protected Class<? extends Plugin> getClazz() throws ClassNotFoundException {
        Class<?> clazz = this.getClass().getClassLoader().loadClass(classStr);

        return (Class<? extends Plugin>) clazz;
    }

    protected void setClassStr(Class<? extends Plugin> clazz) {
        this.classStr = clazz.getCanonicalName();
    }

    private List<String> getSupportedWebSitesRegexList() {
        if (supportedWebSitesRegex == null) {
            supportedWebSitesRegex = new ArrayList<>();
        }

        return supportedWebSitesRegex;
    }

    public List<String> getSupportedWebSitesRegex() {
        return Collections.unmodifiableList(getSupportedWebSitesRegexList());
    }

    protected void setSupportedWebSitesRegex(List<String> supportedWebSitesRegex) {
        this.supportedWebSitesRegex = supportedWebSitesRegex;
    }

    public int getPluginPriority() {
        return priority;
    }

    public void setPluginPriority(int priority) {
        this.priority = priority;
    }

    public boolean isUserCanDownloadLater() {
        return userCanDownloadLater;
    }

    public void setUserCanDownloadLater(boolean userCanDownloadLater) {
        this.userCanDownloadLater = userCanDownloadLater;
    }

    public boolean equals(Object o) {
        if (o.getClass().equals(this.getClass())) {
            Plugin o1 = (Plugin) o;
            Plugin o2 = this;

            return o1.getId().equals(o2.getId());
        }

        return super.equals(o);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
