package com.mraleksmay.projects.download_manager.common.util.image;

import com.luciad.imageio.webp.WebPReadParam;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageWorker {

    public static BufferedImage getImageFromWebp(ByteArrayInputStream bais) throws IOException {
        // Obtain a WebP ImageReader instance
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

        // Configure decoding parameters
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);

        // Configure the input on the ImageReader
        reader.setInput(new MemoryCacheImageInputStream(bais));

        // Decode the image
        BufferedImage image = reader.read(0, readParam);
        return image;
    }
}
