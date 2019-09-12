package com.jqk.serialporttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jqk.serialporttest.util.ThreadUtil;

public class MainActivity extends AppCompatActivity {
    private Button open;
    private Button close;
    private Button send;
    private Button destroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        send = findViewById(R.id.send);
        destroy = findViewById(R.id.destroy);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadUtil.startRead();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadUtil.stopRead();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadUtil.sendMsg();
            }
        });

        destroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadUtil.stopRead();
    }
}
