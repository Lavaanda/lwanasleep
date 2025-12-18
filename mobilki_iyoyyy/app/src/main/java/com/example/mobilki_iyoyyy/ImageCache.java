package com.example.mobilki_iyoyyy;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {
    private static LruCache<String, Bitmap> cache;

    static {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8; // 1/8 от доступной памяти
        cache = new LruCache<>(cacheSize);
    }

    public static void put(String key, Bitmap bitmap) {
        if (get(key) == null && bitmap != null) {
            cache.put(key, bitmap);
        }
    }

    public static Bitmap get(String key) {
        return cache.get(key);
    }
}
