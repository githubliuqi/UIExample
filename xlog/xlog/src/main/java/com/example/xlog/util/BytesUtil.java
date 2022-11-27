package com.example.xlog.util;

/**
 * 十六进制字符串和字节数组转换工具
 *
 * @author zzz
 */
public class BytesUtil {
    /**
     * 字节数组转化为十六字节字符串
     *
     * @param src byte[] 字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHexString(byte[] src) {
        return bytesToHexString(src, 0, src.length);
    }

    /**
     * 字节数组转化为十六字节字符串
     *
     * @param src byte[] 字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHexString(byte[] src, int offset, int length) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            // 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
            int v = src[offset + i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /**
     * 字节数组转化为十六字节字符串
     *
     * @param src short[] 字节数组
     * @return 十六进制字符串
     */
    public static String shortsToHexString(short[] src, int offset, int length) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            // 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
            int v = src[offset + i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 把十六进制字符串转化为字节数组
     *
     * @param hexString 十六进制字节字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * int到byte[]
     *
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        //由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value |= (bytes[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }


    /**
     * 字节转为Int值, 大端序
     * 即 0xab,0xcd ＝> 0xabcd
     *
     * @param ab
     * @param cd
     * @return
     */
    public static int bytesToInt(byte ab, byte cd) {
        return ((ab & 0xFF) << 8) | (cd & 0xFF);
    }

    /**
     * 从Buffer中取出来特定的字节, 并转换成整数，大端。
     * </p>
     *
     * @param buffer
     * @param start  0基的Bit位
     * @param count  bit数
     * @return
     */
    public static int getInt(byte[] buffer, int start, int count) {
        if (count == 0 || count > 32) {
            return 0;
        }

        // bits索引
        // [start, start + count - 1]
        int startIndexInBytes = start / 8;
        int endBitIndexInBytes = (start + count - 1) / 8;

        // [startIndexInBytes, endBitIndexInBytes] 切片出来
        long cutter = 0;
        for (int i = startIndexInBytes; i < (endBitIndexInBytes + 1); ++i) {
            cutter <<= 8;
            cutter |= buffer[i] & 0xFF;
        }

        // 右边多余的Bits的个数
        int end = start + count;
        // [start, end)
        int rightZeroCount = (endBitIndexInBytes + 1) * 8 - end;

        cutter >>= rightZeroCount;

        // 生成mask
        long mask = 0;
        for (int i = 0; i < count; i++) {
            mask <<= 1;
            mask |= 0x01;
        }

        // getValue
        return (int) (cutter & mask);
    }

    /**
     * 从Buffer中取出来特定的字节, 并转换成整数，大端。
     * <p/>
     * 等价于 getInt(buffer, offset*8+start, count);
     *
     * @param buffer
     * @param offset 字节数便宜
     * @param start  0基的Bit位
     * @param count  bit数
     * @return
     */
    public static int getInt(byte[] buffer, int offset, int start, int count) {
        return getInt(buffer, offset * 8 + start, count);
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}