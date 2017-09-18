/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 * <p>
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.xsdk.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by xuqiqiang on 2016/05/17.
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static String filePathToCloudPath(String filePath) {
        if (filePath.startsWith(Cache.rootName)) {
            return filePath.substring(Cache.rootName.length());
        } else {
            Log.e(TAG, "filePathToCloudPath error!");
            return null;
        }
    }

    public static String cloudPathToFilePath(String cloudPath) {
        return Cache.rootName + cloudPath;
    }

    public static int getLevel(File file) {
        if (TextUtils.equals(file.getPath(), Cache.rootName))
            return 0;
        return filePathToCloudPath(file.getParent()).split("/").length;
    }

    public static boolean deleteCache(Context context, String[] cacheName,
            String fileName) {
        File f;
        boolean isHaveSDCard = false;
        if (Environment.getExternalStorageState() != null
                && !Environment.getExternalStorageState().equals("removed")) {
            isHaveSDCard = true;
        }

        if (isHaveSDCard) {
            File root = new File(Cache.rootName);

            if (!root.exists()) {
                return true;
            }

            String pathName = Cache.rootName;

            if (cacheName != null) {
                int i, length = cacheName.length;
                for (i = 0; i < length; i++) {
                    pathName += File.separator + cacheName[i];
                    File path = new File(pathName);

                    if (!path.exists()) {
                        return true;
                    }
                }
            }

            f = new File(pathName + File.separator + fileName);
            if (!f.exists()) {
                return true;
            }

            return f.delete();

        } else {
            return context.deleteFile(fileName);
        }
    }

}