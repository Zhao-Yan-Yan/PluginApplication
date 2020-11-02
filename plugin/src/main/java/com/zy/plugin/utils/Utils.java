package com.zy.plugin.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @ProjectName: PluginApplication
 * @Author: 赵岩
 * @Email: 17635289240@163.com
 * @Description: TODO
 * @CreateDate: 2020/11/2 16:29
 */
class Utils {
    private Utils() {

    }

    private void hello(Context context) {
        Toast.makeText(context, "Plugin --> Utils --> hello", Toast.LENGTH_SHORT).show();
        System.out.println("Plugin --> Utils --> hello");
    }
}
