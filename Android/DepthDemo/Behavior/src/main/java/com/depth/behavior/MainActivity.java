package com.depth.behavior;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.depth.behavior.adapter.HomeAdapter;
import com.depth.behavior.behavior.HeaderViewPager;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init_3();
    }

    void init_0() {
        setContentView(R.layout.activity_main);

        ViewPager mV_ViewPager = findViewById(R.id.view_ViewPager);
        mV_ViewPager.setAdapter(new HomeAdapter(getSupportFragmentManager()));
    }

    void init_2() {
        setContentView(R.layout.activity_main2);

        ViewPager mV_ViewPager = findViewById(R.id.view_ViewPager);
        mV_ViewPager.setAdapter(new HomeAdapter(getSupportFragmentManager()));
    }

    void init_3() {
        setContentView(R.layout.activity_main3);

        HeaderViewPager mV_ViewPager = findViewById(R.id.view_ViewPager);

        mV_ViewPager.setKeepHeight(50);
        mV_ViewPager.setAdapter(new HomeAdapter(getSupportFragmentManager()));

        findViewById(R.id.view_BackTop).setOnClickListener(v -> mV_ViewPager.scrollToTop());
        findViewById(R.id.view_GoToDown).setOnClickListener(v -> mV_ViewPager.scrollToBottom());


    }
}
