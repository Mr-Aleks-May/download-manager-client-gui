package test;

import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.CategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.SCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.DownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.SDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.group.GroupMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.plugin.PluginMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class TestPlugin extends Plugin {
    {
        setPSID("TEST");
        setName("test name");
        setVersion("test version");
        setAuthor("test author");

        setDefPluginClass(TestPlugin.class);
        setDefGroupClass(TestGroup.class);
        setDefCategoryClass(TestCategory.class);
    }


    @Override
    public Plugin init() throws IOException {
        return null;
    }

    @Override
    public boolean canDeserialize(String psid, String version) {
        return false;
    }

    @Override
    public boolean canDeserializeIgnoreVersion(String psid) {
        return false;
    }

    @Override
    public ADialog getAddDownloadDialogWindow(Class<?>[] constructorArgsType, Object[] constructorParams) throws Exception {
        final Class<? extends ADialog> dialogClass = getAddDownloadDialogWindowClass();
        final Constructor<? extends ADialog> dialogConstructor = dialogClass.getDeclaredConstructor(constructorArgsType);
        final ADialog dialogWindow = dialogConstructor.newInstance(constructorParams);

        return dialogWindow;
    }

    @Override
    public Class<? extends ADialog> getAddDownloadDialogWindowClass() {
        return TestDialog.class;
    }

    @Override
    public Category getDefaultCategory() {
        return null;
    }

    @Override
    public Class<? extends Plugin> getPluginClass() {
        return null;
    }

    @Override
    public PluginMapper getPluginMapper() {
        return null;
    }

    @Override
    public GroupMapper getGroupMapper() {
        return null;
    }

    @Override
    public CategoryMapper getCategoryMapper() {
        return null;
    }

    @Override
    public DownloadMapper getDownloadMapper() {
        return null;
    }

    @Override
    public SDownloadMapper getSDownloadMapper() {
        return null;
    }

    @Override
    public SCategoryMapper getSCategoryMapper() {
        return null;
    }

    @Override
    public Group getDefGroup() {
        return new TestGroup();
    }

    @Override
    public Category getDefCategory() {
        return new TestCategory();
    }
}
