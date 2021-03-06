package com.jqk.serialporttest.util;

import android.os.SystemClock;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class ThreadUtil {
    public static boolean open = true;

    public static void startRead() {
        open = true;
        File file = new File("/dev/" + "ttyS2");
        int baudrate = 38400;
        int parity = 1;
        int dataBits = 8;
        int stopBit = 1;
        int floats = 0;

        if (SerialPortUtil.open(file, baudrate, parity, dataBits, stopBit, floats)) {
            L.d("jiqingke", "打开四方位串口成功");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        //  获取串口的输入流对象
                        FileInputStream inputStream = SerialPortUtil.getInputStream();
                        //记录已经到达串口且未被读取的数据的字节（Byte）数。
                        int availableBytes = 0;
                        int cacheSize = 0;
                        //无限循环，每隔20毫秒对串口进行一次扫描，检查是否有数据到达
                        while (open && inputStream != null) {
                            // 可用字节数不再增加时才开始读取
                            do {
                                //获取串口收到的可用字节数
                                availableBytes = inputStream.available();
                                Thread.sleep(20);
                            } while (availableBytes != inputStream.available());
                            // 如果可用字节数大于零则开始循环并获取数据
                            while (availableBytes > 0) {
                                //定义用于缓存读入数据的数组，缓存最大为1024
                                cacheSize = availableBytes > 1024 ? 1024 : availableBytes;
                                byte[] cache = new byte[cacheSize];
                                //从串口的输入流对象中读入数据并将数据存放到缓存数组中
                                inputStream.read(cache);

                                availableBytes = inputStream.available();

                                L.d("jiqingke", "接收 = " + FormatUtil.bytesToHexString(cache));
                            }
                            //让线程睡眠20毫秒
                            Thread.sleep(20);
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
                    byte[] b = {0x01, 0x02, 0x03, 0x04};

                    os.write(b);

                    L.d("jiqingke", "发送成功 = " + FormatUtil.bytesToHexString(b));
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
