package com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.group;

import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

public class BaseGroup extends Group {
        public BaseGroup() {
    }

    public BaseGroup(String id, String name, Plugin parent) {
        super(id, name, parent);
        setGSID("PLUGIN-COMMON-GROUP-COMMON-" + id);
    }
}
