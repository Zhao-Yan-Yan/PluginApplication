# PluginApplication
Android 插件化原理实现雏形

# 原理

利用 `DexClassLoader` `loadClass` 加载 `Apk dex`中的 `class`文件 拿到目标`Class`,再通过反射执行目标`Class`的方法

#简单实现

`plugin` App
```java
package com.zy.plugin.utils
class Utils {
    private Utils() {

    }

    private void hello(Context context) {
        Toast.makeText(context, "Plugin --> Utils --> hello", Toast.LENGTH_SHORT).show();
        System.out.println("Plugin --> Utils --> hello");
    }
}
```
宿主 App
```java
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
        DexClassLoader classLoader = new DexClassLoader(pluginApk.getAbsolutePath(), pluginApk.getAbsolutePath(), null, null);
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
```

1. 将插件apk保存到手机存储中
2. 通过`DexClassLoader.loadClass()`加载目标`class`文件,得到目标`Class`对象
3. 反射执行具体操作


[Demo](https://github.com/Zhao-Yan-Yan/PluginApplication)
