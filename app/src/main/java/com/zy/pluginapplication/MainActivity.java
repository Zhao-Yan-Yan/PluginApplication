package com.zy.pluginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File pluginApk = new File(getExternalFilesDir("pluginApks"), "plugin-debug.apk");
        try (Source source = Okio.source(getAssets().open("plugin-debug.apk"));
             BufferedSink bufferedSink = Okio.buffer(Okio.sink(pluginApk))
        ) {
            bufferedSink.writeAll(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DexClassLoader classLoader = new DexClassLoader(pluginApk.getAbsolutePath(), getExternalFilesDir("pluginApks").getAbsolutePath(), null, null);
        try {
            Class<?> utilsClass = classLoader.loadClass("com.zy.plugin.utils.Utils");
            Constructor<?> constructor = utilsClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            Object utils = constructor.newInstance();
            Method helloMethod = utilsClass.getDeclaredMethod("hello", Context.class);
            helloMethod.setAccessible(true);
            helloMethod.invoke(utils, this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}