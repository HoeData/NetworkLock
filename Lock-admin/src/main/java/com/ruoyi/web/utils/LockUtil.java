package com.ruoyi.web.utils;

import com.fazecast.jSerialComm.SerialPort;

public class LockUtil {

    private static byte[] reverseBytes(byte[] bytes) {
        int length = bytes.length;
        byte[] reversedBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            reversedBytes[i] = bytes[length - 1 - i];
        }
        return reversedBytes;
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
//        String str = "6862000200";
//        str += Integer.toHexString(255).toUpperCase();
//        byte[] data = hexStringToByteArray(str);
//        byte crc8 = crc8(data, data.length);
//        System.out.println(crc8);
//        System.out.println(String.format("%02x",crc8));
//        System.out.println("===="+String.format("%02x",crc8));
//        str+=String.format("%02x",crc8);
//        byte[] result = hexStringToByteArray(str);
//        for (byte b : result) {
//            System.out.println(b);
//        }
//        System.out.println("====");
//        for (byte b : result) {
//            System.out.printf("0x%X", b);
//            System.out.println("-");
//        }
        unLock(11, "COM7");

    }

    public static boolean unLock(int serialNumber, String portName) {
        boolean result = false;
        String hexString = "6862000200";
        hexString += String.format("%02X", serialNumber);
        byte[] data = hexStringToByteArray(hexString);
        byte crc8 = crc8(data, data.length);
        hexString += String.format("%02x", crc8);
        byte[] sendMessage = hexStringToByteArray(hexString);
        SerialPort serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(115200, 8, 1, 0); // 设置串口参数：波特率9600, 数据位8, 停止位1, 校验位无
        serialPort.setParity(0);
        byte[] newData;
        try {

            if (serialPort.openPort()) {
                serialPort.writeBytes(sendMessage, sendMessage.length);
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    while (serialPort.bytesAvailable() > 0) {//循环读取所有的返回数据。如果可读取数据长度为0或-1，则停止读取
                        newData = new byte[serialPort.bytesAvailable()];//创建一个字节数组，长度为可读取的字节长度
                        int bytesRead = serialPort.readBytes(newData, newData.length); // 读取数据
                        if (bytesRead > 0) {
                            byte[] receivedData = new byte[bytesRead];
                            System.arraycopy(newData, 0, receivedData, 0, bytesRead);
                            for (byte b : newData) {
                                System.out.println(String.format("%02X", b));
                            }
                            String unLockStr = "6861001300";
                            unLockStr += String.format("%02X", serialNumber);
                            unLockStr += "01";
                            for (int j = 5; j < 21; j++) {
                                unLockStr += String.format("%02X", newData[j]);
                            }
                            byte[] unLockByte = hexStringToByteArray(unLockStr);
                            byte unLockCrc8 = crc8(unLockByte, unLockByte.length);
                            unLockStr += String.format("%02x", unLockCrc8);
                            unLockByte=hexStringToByteArray(unLockStr);
                            serialPort.writeBytes(unLockByte, unLockByte.length);
                            for (int k = 0; k < 10; k++) {
                                while (serialPort.bytesAvailable() > 0) {
                                    result = true;
                                    break;
                                }
                            }

                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serialPort.closePort();
        }
        return result;
    }

    public static byte crc8(byte[] start, int len) {
        int j;
        int crc_reg = 0;
        int check;
        for (int i = 0; i < len; i++) {
            crc_reg = 0xAA ^ (start[i] & 0xFF); // 将byte转换为无符号值
            for (j = 0; j < 8; j++) {
                check = crc_reg & 0x01;
                crc_reg >>= 1;
                if (check == 0x01) {
                    crc_reg ^= 0x55;
                }
            }
        }
        return (byte) crc_reg; // 返回时转换为byte
    }
}
