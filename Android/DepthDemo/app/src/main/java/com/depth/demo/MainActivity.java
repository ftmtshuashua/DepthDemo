package com.depth.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.library.utils.UnicodeHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        viewById.setText(UnicodeHelper.parser("显示图标：U+26C8|U+26C8 FE0E|U+26C8 FE0F :\u26c8\ufe0f \n U+26C8 FE10|U+26C8 FE11|U+26C8 FE12|U+26C8 FE13|U+26C8 FE14|U+26C8 FE15"));


    }

}