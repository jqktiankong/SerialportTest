package com.jqk.serialporttest.util;

import android.util.Log;

import com.jqk.serialporttest.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

public class SerialPortUtil {
    private static SerialPort serialPort;
    private static FileInputStream fileInputStream;
    private static FileOutputStream fileOutputStream;

    /**
     * @param device   串口地址
     * @param baudrate 波特率
     * @param parity   效验方式，0无，1奇效验，2偶效验
     * @param dataBits 数据位
     * @param stopBit  停止位
     * @param flags
     * @return
     */
    public static boolean open(File device, int baudrate, int parity, int dataBits,
                               int stopBit, int flags) {
        try {
            /* 打开串口 */
            serialPort = new SerialPort(device, baudrate, parity, dataBits,
                    stopBit, flags);
            fileOutputStream = (FileOutputStream) serialPort.getOutputStream();
            fileInputStream = (FileInputStream) serialPort.getInputStream();

            return true;
        } catch (SecurityException e) {
            Log.d("jiqingke", "e = " + e.toString());
        } catch (IOException e) {
            Log.d("jiqingke", "e = " + e.toString());
        } catch (InvalidParameterException e) {
            Log.d("jiqingke", "e = " + e.toString());
        }

        return false;
    }

    public static FileInputStream getInputStream() {
        return fileInputStream;
    }

    public static FileOutputStream getOutputStream() {
        return fileOutputStream;
    }

    public static void close() {
        if (serialPort != null) {
            serialPort.close();
        }
    }
}
