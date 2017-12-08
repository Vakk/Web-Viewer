package com.github.vakk.webviewer;

import android.os.Build;

/**
 * Created by Valery Kotsulym on 12/8/17.
 */

public class Utils {
    public static boolean isMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
