package com.jqk.serialporttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

public class MainActivity extends AppCompatActivity {
    private SerialPort serialPort;
    private InputStream inputStream;

    private ReadThread mReadThread;

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (inputStream == null) return;
                    size = inputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.d("jiqingke", (new String(buffer, 0, size)));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            /* 打开串口 */
            serialPort = new SerialPort(new File("/dev/" + "ttyS2"), 38400, 0);
            //  mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
            inputStream = serialPort.getInputStream();

            /* Create a receiving thread */
            mReadThread = new ReadThread();/* 创建串口处理线程 */
            mReadThread.start();

        } catch (SecurityException e) {
            Log.d("jiqingke", "e = " + e.toString());
        } catch (IOException e) {
            Log.d("jiqingke", "e = " + e.toString());
        } catch (InvalidParameterException e) {
            Log.d("jiqingke", "e = " + e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPort.close();
    }
}
