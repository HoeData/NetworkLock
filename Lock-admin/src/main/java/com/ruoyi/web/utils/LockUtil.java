package com.ruoyi.web.utils;

import com.fazecast.jSerialComm.SerialPort;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class LockUtil {

    //    private static String dev="/dev/cu.usbserial-2140";
    private static String dev = "\\\\.\\COM5";


    public LockUtil(String dev) {
        this.dev = dev;
    }

    /**
     * 获取钥匙ID
     */
    public static byte[] readKeyId(byte[] data) throws Exception {
        SerialPort serialPort = getSerialPort(dev);
        serialPort.openPort();
        send(serialPort, data);
        byte[] buffer = new byte[21];
        receiveByte(serialPort, buffer);
        serialPort.closePort();
        return buffer;
    }

    /**
     * 设置钥匙ID
     */
    public static boolean setKeyId(byte[] data) throws Exception {
        SerialPort serialPort = getSerialPort(dev);
        serialPort.openPort();
        byte[] header = new byte[]{0x68, 0x64};
        byte[] len = CheckLen(data.length);
        byte[] bytes = mergeByteArrays(header, len, data);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte[] checksum = new byte[]{(byte) i};
        byte[] message = mergeByteArrays(header, len, data, checksum);//报文
        send(serialPort, message);
        byte[] buffer = new byte[1024];
        receiveByte(serialPort, buffer);
        serialPort.closePort();
        if (buffer.toString().contains("68 FF")) {
            return true;
        } else {
            return false;
        }
    }



    /**
     * 查询锁信息
     * 返回数据编码格式ASCII字符串
     */
    public static byte[]  selectLockInformation(byte[] data) throws IOException{
        SerialPort serialPort = getSerialPort(dev);
        serialPort.openPort();
        byte[] header=new byte[]{0x68,0x62};
        byte[] len=CheckLen(data.length);
        byte[] bytes = mergeByteArrays(header, len, data);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte[] checksum= new byte[]{(byte) i};
        byte[] message = mergeByteArrays(header, len, data, checksum);//报文
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(message);
        outputStream.flush();
        InputStream inputStream = serialPort.getInputStream();
        byte[] buffer = new byte[5+(data.length-1)*19];
        inputStream.read(buffer);
        serialPort.closePort();
        return buffer;
    }

    /**
     * 删除锁信息
     *        deleteLockInformation(byteArray);
     */
    public static Boolean  deleteLockInformation(byte[] data) throws IOException{
        SerialPort serialPort = getSerialPort(dev);
        serialPort.openPort();
        byte[] header=new byte[]{0x68,0x63};
        byte[] len=CheckLen(data.length);
        byte[] bytes = mergeByteArrays(header, len, data);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte[] checksum= new byte[]{(byte) i};
        byte[] message = mergeByteArrays(header, len, data, checksum);//报文
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(message);
        outputStream.flush();
        InputStream inputStream = serialPort.getInputStream();
        byte[] buffer = new byte[6];
        inputStream.read(buffer);
        serialPort.closePort();
        if (buffer.toString().contains("68 FF")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 添加锁信息
     *
     */
    public static void addLockInformation(List<LockInfo> lockInfos) throws IOException{
        if (!lockInfos.isEmpty() && lockInfos.size()>48){
            throw  new RuntimeException("数据长度超过48");
        }
        SerialPort serialPort = getSerialPort(dev);
        serialPort.openPort();
        byte[] header=new byte[]{0x68,0x61};
        int sized = lockInfos.size();
        //转换锁的数量
        // 获取最低有效字节  字符串
        byte[] data=new byte[lockInfos.size()*19+1];
        data[0]= (byte) (sized & 0xFF); // 获取最低有效字节  字符串
        for (int i = 0; i < lockInfos.size(); i++) {
            int i1 = i + 1;
            LockInfo lockInfo = lockInfos.get(i);
            data[i1]=lockInfo.getLockNumber();
            byte[] lockSerialNumber = lockInfo.getLockSerialNumber();
            for (int j = 0; j < lockSerialNumber.length; j++) {
                data[i1+1+j]=lockSerialNumber[j];
            }
            data[i1+17]=lockInfo.getLockEffective();
            data[i1+18]=lockInfo.getLockTime();
        }
        byte[] len=CheckLen(data.length);
        byte[] bytes = mergeByteArrays(header, len, data);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte lsb = (byte) (i & 0xFF); // 获取最低有效字节  字符串
        byte[] checksum= new byte[]{lsb};

        byte[] message = mergeByteArrays(header, len, data, checksum);//报文
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(message);
        outputStream.flush();
        serialPort.closePort();
    }


    /**
     * 串口连接
     * @param CommPort
     * @return
     */

    public static  SerialPort getSerialPort(String CommPort) {
//        SerialPort serialPort = SerialPort.getCommPort(CommPort);
        SerialPort serialPort = SerialPort.getCommPorts()[0];
        serialPort.setBaudRate(9600);
        serialPort.setNumDataBits(8);
        serialPort.setParity(SerialPort.EVEN_PARITY);//偶检验
        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT); // 1个停止位
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
//        serialPort.clearDTR();
        serialPort.clearRTS();
        return serialPort;
    }

    /**
     * 获取版本
     * 返回数据编码格式ASCII字符串
     */
    public static byte[] getVersion() throws IOException {
        SerialPort serialPort = getSerialPort(dev);
        serialPort.openPort();
        byte[] header=new byte[]{0x68,0x60};
        byte[] data={0x00};
        byte[] len=CheckLen(data.length);
        byte[] bytes = mergeByteArrays(header, len, data);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte lsb = (byte) (i & 0xFF); // 获取最低有效字节  字符串
        byte[] checksum= new byte[]{lsb};
        byte[] message = mergeByteArrays(header, len, data, checksum);//报文
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(message);
        outputStream.flush();
        InputStream inputStream = serialPort.getInputStream();
        byte[] buffer = new byte[21];
        inputStream.read(buffer);
        serialPort.closePort();
        return buffer;
    }
    /**
     * 合并
     */
    public static byte[] mergeByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }
    /**
     * 计算指定区域的8位累加和（不考虑进位）
     *
     * @param data   字节数据数组
     * @param offset 起始位索引
     * @param length 要计算累加和的数据长度
     * @return 8位累加和的结果
     */
    public static int calculateChecksum(byte[] data, int offset, int length) {
        int checksum = 0; // 初始化累加和

        // 检查输入参数的有效性
        if (data == null || offset < 0 || length < 0 || offset + length > data.length) {
            throw new IllegalArgumentException("Invalid parameters for calculateChecksum");
        }

        // 遍历指定区域的数据
        for (int i = offset; i < offset + length; i++) {
            checksum += data[i] & 0xFF; // 将byte值转换为int并加到累加和上，确保是正值
        }

        // 只保留累加和的低8位
        checksum &= 0xFF;

        return checksum;
    }
    /**
     * 数组转Ascii
     */
    public static String byteArrayToAscii(byte[] byteArray) {
        StringBuilder output = new StringBuilder("");
        for (byte b : byteArray) {
            output.append((char) b);
        }
        return output.toString();
    }
    /**
     * 计算校验和
     */
    public static byte CheckSumming(byte[] start, int len){
        byte crc_reg = 0;
        byte check;

        for (int i = 0; i < len; i++) {
            // Java中的byte是有符号的，但这里我们将其视为无符号
            // 0xAA在Java中可以直接这样写，因为byte会自动扩展为int进行计算
            crc_reg = (byte) (0xAA ^ (start[i] & 0xFF)); // 确保byte被当作无符号处理

            for (int j = 0; j < 8; j++) {
                check = (byte) (crc_reg & 0x01); // 取出最低位
                crc_reg >>= 1; // 右移一位

                if ((check & 0x01) != 0) { // 检查最低位是否为1
                    // 注意：0x55在Java中也是直接使用的，但同样会进行int计算
                    crc_reg ^= (byte) 0x55; // 异或操作
                }
            }
        }

        return crc_reg;
    }
    /**
     * 计算长度数值
     */
    public static byte[] CheckLen(int len){
        // 通过与0xFFFF进行按位与操作来获取低16位
        int low16Bits = len & 0xFFFF;
        // 使用String.format来确保结果是4个字符长的十六进制字符串
        String format = String.format("%04X", low16Bits);
        byte[] bytes = new byte[2];
        for (int i = 0; i < format.length(); i += 2) {
            // 每次取两位字符，转换为字节
            String hexPair = format.substring(i, i + 2);
            bytes[i / 2] = (byte) Integer.parseInt(hexPair, 16);
        }
        return bytes;
    }

    public static void checkASCIILength(String str, String msg) {
        if (StringUtils.isNotBlank(str)) {
            if (str.getBytes(StandardCharsets.US_ASCII).length > 16) {
                throw new ServiceException(msg);
            }
        }
    }

    public static byte[] getByteForAddLock(List<LockInfo> lockInfoList) {
        byte[] header = new byte[]{0x68, 0x61};
        int sized = lockInfoList.size();
        //转换锁的数量
        // 获取最低有效字节  字符串
        byte[] data = new byte[lockInfoList.size() * 19 + 1];
        data[0] = (byte) (sized & 0xFF); // 获取最低有效字节  字符串
        for (int i = 0; i < lockInfoList.size(); i++) {
            int i1 = i + 1;
            LockInfo lockInfo = lockInfoList.get(i);
            data[i1] = lockInfo.getLockNumber();
            byte[] lockSerialNumber = lockInfo.getLockSerialNumber();
            for (int j = 0; j < lockSerialNumber.length; j++) {
                data[i1 + 1 + j] = lockSerialNumber[j];
            }
            data[i1 + 17] = lockInfo.getLockEffective();
            data[i1 + 18] = lockInfo.getLockTime();
        }
        byte[] len = CheckLen(data.length);
        byte[] bytes = mergeByteArrays(header, len, data);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte lsb = (byte) (i & 0xFF); // 获取最低有效字节  字符串
        byte[] checksum = new byte[]{lsb};
        return mergeByteArrays(header, len, data, checksum);
    }

    public static byte[] getByteForDelLock(List<LockInfo> lockInfoList) {
        byte[] header = new byte[]{0x68, 0x63};
        int sized = lockInfoList.size();
        //转换锁的数量
        // 获取最低有效字节  字符串
        byte[] data = new byte[lockInfoList.size()  + 1];
        data[0] = (byte) (sized & 0xFF); // 获取最低有效字节  字符串
        for (int i = 0; i < lockInfoList.size(); i++) {
            int i1 = i + 1;
            data[i1] = lockInfoList.get(i).getLockNumber();
        }
        byte[] len = CheckLen(data.length);
        byte[] bytes = mergeByteArrays(header, len, data);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte lsb = (byte) (i & 0xFF); // 获取最低有效字节  字符串
        byte[] checksum = new byte[]{lsb};
        return mergeByteArrays(header, len, data, checksum);
    }

    public static String bytesToHexWithSpaces(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X ", b));
        }
        if (hexString.length() > 0) {
            hexString.setLength(hexString.length() - 1);
        }
        return hexString.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 3]; // 每个字节占三个字符（两个十六进制数字加上一个空格）
        for (int i = 0; i < len; i += 3) {
            int index = i / 3;
            int v = Integer.parseInt(s.substring(i, i + 2), 16);
            data[index] = (byte) v;
        }
        return data;
    }

    private static void send(SerialPort serialPort, byte[] data) throws Exception {
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
    }

    private static void receiveByte(SerialPort serialPort, byte[] buffer) throws Exception {
        int i = 0;
        int bytesRead = 0;
        while (i < 10 && bytesRead < buffer.length - 1) {
            if (buffer.length - 1 == bytesRead) {
                break;
            }
            bytesRead = serialPort.readBytes(buffer, buffer.length);
            Thread.sleep(1000);
            i++;
        }
    }

    public static void main(String[] args) throws Exception {
        //以下为测试开锁的秘钥测试，秘钥为ON，第一步必须要调用锁信息，然后在输入解锁秘钥
        //获取锁信息报文为68 80 00 01 00 E9，开锁报文为68 82 00 04 54 30 34 3D E3
        //如果想解析锁信息，需要base64解密报文，然后在转为ascii码来获取真实报文
        byte[] onData = "ON".getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(onData);
        String hexRepresentation = bytesToHexWithSpaces(encodedBytes);
        System.out.println(hexRepresentation);
        byte[] aa= hexStringToByteArray("54 30 34 3D ");
        byte[] bb = new byte[]{0x68, (byte) 0x82};
        byte[] len = CheckLen(aa.length);
        byte[] bytes = mergeByteArrays(bb, len, aa);
        int i = calculateChecksum(bytes, 0, bytes.length);
        byte[] checksum = new byte[]{(byte) i};
        byte[] message = mergeByteArrays(bytes, checksum);//报文
        System.out.println(bytesToHexWithSpaces(message));
        SerialPort serialPort = getSerialPort(dev);
        serialPort.openPort();
        send(serialPort, message);
        byte[] buffer = new byte[100];
        receiveByte(serialPort, buffer);
        serialPort.closePort();
        System.out.println(bytesToHexWithSpaces(buffer));
        byte[] decodedBytes = hexStringToByteArray(
            "57 46 6C 61 55 7A 49 30 4D 44 68 42 51 6A 41 77 4D 44 41 30 4D 77 3D 3D ");
        String decodedString = new String(decodedBytes);
        // 输出解码后的字符串
        System.out.println(decodedString);
        byte[] decodedByte = Base64.getDecoder().decode(decodedString);
        String decodedStringa = new String(decodedByte, StandardCharsets.US_ASCII);
        System.out.println(decodedStringa);
//        System.out.println(getStrForAscii("31 32 33 34 35 36 37 38 39 3A 3B 3C 3D 3E 3F 23 "));



    }


}
