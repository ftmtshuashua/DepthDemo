package com.depth.demo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.library.utils.UnicodeHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        WebView mV_WebView = findViewById(R.id.view_WebView);
        mV_WebView.loadUrl("http://player.cntv.cn/flashplayer/players/htmls/smallwindow.html?pid=305b8b302b44426dbe657c675817bd47&time=0");


        TextView viewById = findViewById(R.id.view_TextView);

        viewById.setText(UnicodeHelper.parser("今日天气:\uD83D\uDE3A"));
    }

    protected void onCreate2(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView viewById = findViewById(R.id.view_Image);

//        viewById.setText("显示图标："+"\uD83D\uDC0B");
//        EmojiUtils.string2Emoji("U+2601、U+FE0F");

//        viewById.setText("显示图标："+"⛹\uD83C\uDFFB");
//        viewById.setText("显示图标：" + EmojiUtils.decode("\uD83D\uDE00"));


        //Unicode编码：U+1F600 -> \uD83D\uDE00 = 😀 -> ALT+128512
        //  \uD83D:1101100000111101
        //  \uDE00:1101111000000000
        // U+1F600:00011111011000000000
        // 11110XXX 10XXXXXX 10XXXXXX 10XXXXXX
        //       00   011111   011000   000000
        // 11110000 10011111 10011000 10000000


//        viewById.setText(UnicodeHelper.parser("显示图标：U+26C8|U+26C8 FE0F|U+2600|U+2600 FE02|U+1F600|U+1F600|U+1F33A |  U+26F9 1F3FB  |  U+1F93E 200D 2640"));
//        viewById.setText(UnicodeHelper.parser("显示图标：U+26C8|U+26C8 FE0E|U+26C8 FE0F :\u26c8\ufe0f \n U+26C8 FE10|U+26C8 FE11|U+26C8 FE12|U+26C8 FE13|U+26C8 FE14|U+26C8 FE15"));

        //时间格式转化
        try {
            viewById.setText("时间解析");
            String data = "2020-08-20T18:00+08:00";
            data = data.substring(0, 16) + ":00" + data.substring(16);
            DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
            DateTime parse = dateTimeFormatter.parseDateTime(data);

//            SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMANY);
//            Date parse = ISO8601DATEFORMAT.parse(time.replaceAll("\\+0([0-9]){1}\\:00", "+0$100"));
            System.err.println("解析后的时间:" + parse.toString());

            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(parse.toDate().getTime()));
            viewById.setText("时间 ：" + format);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}