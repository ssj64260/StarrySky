package com.cxb.starrysky.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cxb.starrysky.R;
import com.cxb.starrysky.app.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView tvFixed;
    private TextView tvNoFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setData();

    }

    private void initView() {
        tvFixed = (TextView) findViewById(R.id.tv_fixed);
        tvNoFixed = (TextView) findViewById(R.id.tv_no_fixed);
    }

    private void setData() {
        tvFixed.setOnClickListener(click);
        tvNoFixed.setOnClickListener(click);
    }

    //点击监听
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_fixed:
                    startActivity(new Intent(MainActivity.this, StarrySkyActivity.class));
                    break;
                case R.id.tv_no_fixed:

                    break;
            }
        }
    };
}
