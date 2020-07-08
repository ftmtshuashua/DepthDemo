package com.depth.ap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.depth.ap_annoation.BindView;
import com.depth.ap_core.Butterknife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.view_TextView)
    TextView mV_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Butterknife.bind(this);

        if (mV_TextView != null) mV_TextView.setText("这是通过注解引入的View");

    }
}
