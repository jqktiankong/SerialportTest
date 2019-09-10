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
    private SerialPort serialPort;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    public boolean open(File device, int baudrate, int flags) {
        try {
            /* 打开串口 */
            serialPort = new SerialPort(device, baudrate, flags);
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

    public FileInputStream getInputStream() {
        return fileInputStream;
    }

    public FileOutputStream getOutputStream() {
        return fileOutputStream;
    }

    public void close() {
        serialPort.close();
    }
}
