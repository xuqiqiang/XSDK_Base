/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 * <p>
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.xsdk.utils;

/**
 * Created by xuqiqiang on 2016/05/17.
 */
public class DataUtils {

    public static boolean equals(float a, float b) {
        if (a <= b + 0.000001f && a >= b - 0.000001f)
            return true;
        return false;
    }
}