package com.mraleksmay.projects.download_manager.plugin.model.plugin;

import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.annotation.Serialize;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.CategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.SCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.DownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.SDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.group.GroupMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.plugin.PluginMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Plugin {
    @Serialize
    private String psid;
    private String id;
    @Serialize
    private String name;
    private String version;
    private String author;
    @Serialize
    private String description;
    @Serialize
    private List<Group> groups = new ArrayList<>();
    private List<String> supportedWebSitesRegex = new ArrayList<>();
    private int priority;
    private boolean userCanDownloadLater = false;
    private String addDownloadHandlerClass;

    protected String defPluginClass;
    protected String defGroupClass;
    protected String defCategoryClass;

    protected Group defGroup;
    protected Category defCategory;


    public Plugin() {
    }

    public Plugin(String id, String name) {
        this.id = id.toUpperCase();
        this.name = name;
    }

    public Plugin(String psid, String name, String version, String author, String description, List<Group> groups, List<String> supportedWebSitesRegex, int priority, boolean userCanDownloadLater, String defPluginClass, String defGroupClass, String defCategoryClass) {
        this.psid = psid;
        this.name = name;
        this.version = version;
        this.author = author;
        this.description = description;
        this.groups = groups;
        this.supportedWebSitesRegex = supportedWebSitesRegex;
        this.priority = priority;
        this.userCanDownloadLater = userCanDownloadLater;
        this.defPluginClass = defPluginClass;
        this.defGroupClass = defGroupClass;
        this.defCategoryClass = defCategoryClass;
    }


    public boolean add(Group group) {
        return groups.add(group);
    }

    public boolean add(String supportedWebSiteRegex) {
        return supportedWebSitesRegex.add(supportedWebSiteRegex);
    }

    public synchronized void remove(@NotNull final Category category) {
        for (Group group : getGroups()) {
            group.remove(category);
        }
    }

    public Group getGroupBy(@NotNull String groupId) {
        groupId = groupId.toUpperCase();

        for (Group group : getGroups()) {
            if (groupId.equals(group.getId())) {
                return group;
            }
        }

        return null;
    }

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


    public abstract Plugin init() throws IOException;

    public abstract boolean canDeserialize(String psid, String version);

    public abstract boolean canDeserializeIgnoreVersion(String psid);

    public abstract JDialog getAddDownloadDialogWindow(Class<?>[] constructorArgsType, Object[] constructorParams) throws Exception;

    public abstract Class<? extends JDialog> getAddDownloadDialogWindowClass();

    public abstract Category getDefaultCategory();

    public abstract Class<? extends Plugin> getPluginClass();

    public abstract PluginMapper getPluginMapper();

    public abstract GroupMapper getGroupMapper();

    public abstract CategoryMapper getCategoryMapper();

    public abstract DownloadMapper getDownloadMapper();

    public abstract SDownloadMapper getSDownloadMapper();

    public abstract SCategoryMapper getSCategoryMapper();

    public abstract Group getDefGroup();

    public abstract Category getDefCategory();


    public String getPSID() {
        return psid;
    }

    public Plugin setPSID(String psid) {
        this.psid = psid;
        return this;
    }

    public String getId() {
        return id;
    }

    public Plugin setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Plugin setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Plugin setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Plugin setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Plugin setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Plugin setGroups(List<Group> groups) {
        this.groups = groups;
        return this;
    }

    public List<String> getSupportedWebSitesRegex() {
        return supportedWebSitesRegex;
    }

    public Plugin setSupportedWebSitesRegex(List<String> supportedWebSitesRegex) {
        this.supportedWebSitesRegex = supportedWebSitesRegex;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public Plugin setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public boolean isUserCanDownloadLater() {
        return userCanDownloadLater;
    }

    public Plugin setUserCanDownloadLater(boolean userCanDownloadLater) {
        this.userCanDownloadLater = userCanDownloadLater;
        return this;
    }


    public Class<? extends Plugin> getDefPluginClass() throws Exception {
        return (Class<? extends Plugin>) Class.forName(defPluginClass);
    }

    public Plugin setDefPluginClass(Class<? extends Plugin> defPluginClass) {
        this.defPluginClass = defPluginClass.getName();
        return this;
    }

    public Class<? extends Group> getDefGroupClass() throws Exception {
        return (Class<? extends Group>) Class.forName(defGroupClass);
    }

    public Plugin setDefGroupClass(Class<? extends Group> defGroupClass) {
        this.defGroupClass = defGroupClass.getName();
        return this;
    }

    public Class<? extends Category> getDefCategoryClass() throws Exception {
        return (Class<? extends Category>) Class.forName(defCategoryClass);
    }

    public Plugin setDefCategoryClass(Class<? extends Category> defCategoryClass) {
        this.defCategoryClass = defCategoryClass.getName();
        return this;
    }


    @Override
    public String toString() {
        return "Plugin{" +
                "psid='" + psid + '\'' +
                '}';
    }
}
