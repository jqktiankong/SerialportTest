package com.jqk.serialporttest.util;

import android.os.SystemClock;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class ThreadUtil {
    public static boolean open = true;

    public static void startRead() {
        open = true;
        File file = new File("/dev/" + "ttyS4");
        int baudrate = 115200;
        int parity = 1;
        int dataBits = 8;
        int stopBit = 1;
        int floats = 0;

        if (SerialPortUtil.open(file, baudrate, 0, 8, 1, 0)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = SerialPortUtil.getInputStream();
                        int available;
                        int first;
                        while (open
                                && is != null
                                && (first = is.read()) != -1) {
                            do {
                                available = is.available();
                                SystemClock.sleep(1);
                            } while (available != is.available());

                            available = is.available();
                            byte[] bytes = new byte[available + 1];
                            is.read(bytes, 1, available);
                            bytes[0] = (byte) (first & 0xFF);
                            L.d("jiqingke", "接收 = " + FormatUtil.bytesToHexString(bytes));
                        }
                    } catch (Exception e) {
                        L.d("e = " + e.toString());
                    }
                }
            }).start();
        }


    }

    public static void sendMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream os = SerialPortUtil.getOutputStream();
                    byte[] b = {(byte) 0x5A, (byte) 0xFF, (byte) 0x00, (byte) 0x02, (byte) 0xA0, (byte) 0x07};
                    os.write(b);
                } catch (Exception e) {
                    L.d("e = " + e.toString());
                }
            }
        }).start();
    }

    public static void stopRead() {
        open = false;
        SerialPortUtil.close();
    }
}
