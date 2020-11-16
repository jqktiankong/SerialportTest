package com.jqk.serialporttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jqk.serialporttest.util.ThreadUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button open;
    private Button close;
    private Button send;
    private Button destroy;

    private File device;

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
                device = new File("/dev/" + "ttyS4");
                if (!device.canRead() || !device.canWrite()) {
                    try {
                        // Missing read/write permission, trying to chmod the file
                        Process su;
                        su = Runtime.getRuntime().exec("/system/bin/su");
                        String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
                                + "exit\n";
                        su.getOutputStream().write(cmd.getBytes());
                        if ((su.waitFor() != 0) || !device.canRead()
                                || !device.canWrite()) {
                            throw new SecurityException();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new SecurityException();
                    }
                }

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
