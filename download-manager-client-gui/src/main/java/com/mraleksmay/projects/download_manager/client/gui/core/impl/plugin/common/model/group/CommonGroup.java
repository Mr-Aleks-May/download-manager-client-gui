package com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.group;

import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;

public class CommonGroup extends Group {
    public CommonGroup() {
    }

    public CommonGroup(String id, String name, Plugin parent) {
        super(id, name, parent);
        setSerializableId("PLUGIN-COMMON-GROUP-COMMON-" + id);
    }
}
