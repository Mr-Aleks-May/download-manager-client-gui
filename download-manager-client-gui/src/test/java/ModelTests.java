import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import org.junit.jupiter.api.Test;
import test.TestCategory;
import test.TestGroup;
import test.TestPlugin;

import java.io.File;
import java.io.IOException;

public class ModelTests {
    @Test
    public void testCreation() throws IOException {
        Plugin plugin = new TestPlugin()
                .setPSID("TEST")
                .setName("test")
                .setVersion("0.1")
                .setAuthor("test author")
                .setDescription("test description");

        Group group = new TestGroup()
                .setGSID("test")
                .setName("test name")
                .setCategories(null)
                .setParent(plugin);

        Category category = new TestCategory()
                .setCSID("test")
                .setName("test name")
                .setOutputDir(new File("test"))
                .setPlugin(plugin);

    }
}
