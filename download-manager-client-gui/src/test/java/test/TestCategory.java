package test;

import com.mraleksmay.projects.download_manager.plugin.model.category.Category;

import java.io.File;
import java.io.IOException;

public class TestCategory extends Category {
    @Override
    public File getTempDir() throws IOException {
        return null;
    }
}
