package com.liqaa.shared.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageConverter
{
    public static byte[] convertImageFileToBytes(String path)
    {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ByteArrayOutputStream byteStream = new ByteArrayOutputStream())
        {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1)
                byteStream.write(buffer, 0, bytesRead);

            return byteStream.toByteArray();

        } catch (IOException e)
        {
            throw new RuntimeException("Error reading image file", e);
        }
    }
}
