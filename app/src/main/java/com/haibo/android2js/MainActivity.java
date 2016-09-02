package com.haibo.android2js;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 * android 和JS的交互调用
 *
 * @author yuhaibo
 * @time 2016年9月2日 16:02:25
 * SuppressLint一定要加上去！！！
 */

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private WebView webview;
    private Button android2js;
    private android.widget.TextView js2parameter;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        registerView();
    }


    /**
     * 初始化界面的view
     */
    private void initview() {
        webview = (WebView) findViewById(R.id.webview);
        js2parameter = (TextView) findViewById(R.id.js2parameter);
        android2js = (Button) findViewById(R.id.android2js);

        webview.getSettings().setDefaultTextEncodingName("utf-8");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/yuhaibo.html");
    }

    /**
     * 注册view事件
     */
    private void registerView() {
        //Android调用js
        android2js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ms = "Android传给js的参数yuhaibo";
                //js里面的方法名
                webview.loadUrl("javascript:androidGetJs('" + ms + "')");
            }
        });
        //js调用Android  第二个字符串参数是js方法名前面的name
        webview.addJavascriptInterface(new JavaScriptInterface(this), "androidjs");
    }

    public class JavaScriptInterface {
        Context context;

        JavaScriptInterface(Context context) {
            this.context = context;
        }

        //在js中调用 android.showInfoFromJs("传给Android的参数")就会出发此方法
        @JavascriptInterface
        public String showInfoFromJs(final String name) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    js2parameter.setText(name);
                }
            });
            String a = "ghjijh";
            return "返回给js的参数" + name;
        }
    }
     /*
     JAVA和JS交互注意事项
     1、尽量用js调用Android方法：
        Android调用js里面的函数的效率远远低于js调用Android
        Android调用js的函数，没有返回值，调用了就控制不到了
     2、Js调用Android的方法，返回值如果是native字符串，必须使用toLocaleString()转成locale的才能正常使用，但是有的耗时
     3、网页中尽量使用原生的js写业务脚本、以提升加载速度。
     */
}
