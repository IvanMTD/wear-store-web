package ru.wear.store.web.utils;

import java.util.Base64;

public class ImageUtil {
    public static String getImgData(byte[] byteData){
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}
