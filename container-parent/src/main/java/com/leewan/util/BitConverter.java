package com.leewan.util;


import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 */
public class BitConverter {

    /**
     * 浠ュ瓧鑺傛暟缁勭殑褰㈠紡杩斿洖鎸囧畾鐨勫竷灏斿��
     * @param data 涓�涓竷灏斿��
     * @return 闀垮害涓� 1 鐨勫瓧鑺傛暟缁�
     */
    public static byte[] getBytes(boolean data) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (data ? 1 : 0);
        return bytes;
    }
    
    /**
     * 浠ュ瓧鑺傛暟缁勭殑褰㈠紡杩斿洖鎸囧畾鐨� 16 浣嶆湁绗﹀彿鏁存暟鍊�
     * @param data 瑕佽浆鎹㈢殑鏁板瓧
     * @return 闀垮害涓� 2 鐨勫瓧鑺傛暟缁�
     */
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data & 0xff);
            bytes[1] = (byte) ((data & 0xff00) >> 8);
        } else {
            bytes[1] = (byte) (data & 0xff);
            bytes[0] = (byte) ((data & 0xff00) >> 8);
        }
        return bytes;
    }

    /**
     * 浠ュ瓧鑺傛暟缁勭殑褰㈠紡杩斿洖鎸囧畾鐨� Unicode 瀛楃鍊�
     * @param data 瑕佽浆鎹㈢殑瀛楃
     * @return 闀垮害涓� 2 鐨勫瓧鑺傛暟缁�
     */
    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data);
            bytes[1] = (byte) (data >> 8);
        } else {
            bytes[1] = (byte) (data);
            bytes[0] = (byte) (data >> 8);
        }
        return bytes;
    }

    /**
     * 浠ュ瓧鑺傛暟缁勭殑褰㈠紡杩斿洖鎸囧畾鐨� 32 浣嶆湁绗﹀彿鏁存暟鍊�
     * @param data 瑕佽浆鎹㈢殑鏁板瓧
     * @return 闀垮害涓� 4 鐨勫瓧鑺傛暟缁�
     */
    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data & 0xff);
            bytes[1] = (byte) ((data & 0xff00) >> 8);
            bytes[2] = (byte) ((data & 0xff0000) >> 16);
            bytes[3] = (byte) ((data & 0xff000000) >> 24);
        } else {
            bytes[3] = (byte) (data & 0xff);
            bytes[2] = (byte) ((data & 0xff00) >> 8);
            bytes[1] = (byte) ((data & 0xff0000) >> 16);
            bytes[0] = (byte) ((data & 0xff000000) >> 24);
        }
        return bytes;
    }

    /**
     * 浠ュ瓧鑺傛暟缁勭殑褰㈠紡杩斿洖鎸囧畾鐨� 64 浣嶆湁绗﹀彿鏁存暟鍊�
     * @param data 瑕佽浆鎹㈢殑鏁板瓧
     * @return 闀垮害涓� 8 鐨勫瓧鑺傛暟缁�
     */
    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data & 0xff);
            bytes[1] = (byte) ((data >> 8) & 0xff);
            bytes[2] = (byte) ((data >> 16) & 0xff);
            bytes[3] = (byte) ((data >> 24) & 0xff);
            bytes[4] = (byte) ((data >> 32) & 0xff);
            bytes[5] = (byte) ((data >> 40) & 0xff);
            bytes[6] = (byte) ((data >> 48) & 0xff);
            bytes[7] = (byte) ((data >> 56) & 0xff);
        } else {
            bytes[7] = (byte) (data & 0xff);
            bytes[6] = (byte) ((data >> 8) & 0xff);
            bytes[5] = (byte) ((data >> 16) & 0xff);
            bytes[4] = (byte) ((data >> 24) & 0xff);
            bytes[3] = (byte) ((data >> 32) & 0xff);
            bytes[2] = (byte) ((data >> 40) & 0xff);
            bytes[1] = (byte) ((data >> 48) & 0xff);
            bytes[0] = (byte) ((data >> 56) & 0xff);
        }
        return bytes;
    }

    /**
     * 浠ュ瓧鑺傛暟缁勭殑褰㈠紡杩斿洖鎸囧畾鐨勫崟绮惧害娴偣鍊�
     * @param data 瑕佽浆鎹㈢殑鏁板瓧
     * @return 闀垮害涓� 4 鐨勫瓧鑺傛暟缁�
     */
    public static byte[] getBytes(float data) {
        return getBytes(Float.floatToIntBits(data));
    }

    /**
     * 浠ュ瓧鑺傛暟缁勭殑褰㈠紡杩斿洖鎸囧畾鐨勫弻绮惧害娴偣鍊�
     * @param data 瑕佽浆鎹㈢殑鏁板瓧
     * @return 闀垮害涓� 8 鐨勫瓧鑺傛暟缁�
     */
    public static byte[] getBytes(double data) {
        return getBytes(Double.doubleToLongBits(data));
    }
    
    /**
     * 灏嗘寚瀹氬瓧绗︿覆涓殑鎵�鏈夊瓧绗︾紪鐮佷负涓�涓瓧鑺傚簭鍒�
     * @param data 鍖呭惈瑕佺紪鐮佺殑瀛楃鐨勫瓧绗︿覆
     * @return 涓�涓瓧鑺傛暟缁勶紝鍖呭惈瀵规寚瀹氱殑瀛楃闆嗚繘琛岀紪鐮佺殑缁撴灉
     */
    public static byte[] getBytes(String data) {
        return data.getBytes(Charset.forName("UTF-8"));
    }
    
    /**
     * 灏嗘寚瀹氬瓧绗︿覆涓殑鎵�鏈夊瓧绗︾紪鐮佷负涓�涓瓧鑺傚簭鍒�
     * @param data 鍖呭惈瑕佺紪鐮佺殑瀛楃鐨勫瓧绗︿覆
     * @param charsetName 瀛楃闆嗙紪鐮�
     * @return 涓�涓瓧鑺傛暟缁勶紝鍖呭惈瀵规寚瀹氱殑瀛楃闆嗚繘琛岀紪鐮佺殑缁撴灉
     */
    public static byte[] getBytes(String data, String charsetName) {
        return data.getBytes(Charset.forName(charsetName));
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨勫竷灏斿��
     * @param bytes 瀛楄妭鏁扮粍
     * @return 甯冨皵鍊�
     */
    public static boolean toBoolean(byte[] bytes) {
        return bytes[0] == 0 ? false : true;
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勪腑鐨勬寚瀹氱殑涓�涓瓧鑺傝浆鎹㈡潵鐨勫竷灏斿��
     * @param bytes 瀛楄妭鏁扮粍
     * @param startIndex 璧峰涓嬫爣
     * @return 甯冨皵鍊�
     */
    public static boolean toBoolean(byte[] bytes, int startIndex) {
        return toBoolean(copyFrom(bytes, startIndex, 1));
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨� 16 浣嶆湁绗﹀彿鏁存暟
     * @param bytes 瀛楄妭鏁扮粍
     * @return 鐢变袱涓瓧鑺傛瀯鎴愮殑 16 浣嶆湁绗﹀彿鏁存暟
     */
    public static short toShort(byte[] bytes) {
        if (isLittleEndian()) {
            return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
        } else {
            return (short) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
        }
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勪腑鐨勬寚瀹氱殑涓や釜瀛楄妭杞崲鏉ョ殑 16 浣嶆湁绗﹀彿鏁存暟
     * @param bytes 瀛楄妭鏁扮粍
     * @param startIndex 璧峰涓嬫爣
     * @return 鐢变袱涓瓧鑺傛瀯鎴愮殑 16 浣嶆湁绗﹀彿鏁存暟
     */
    public static short toShort(byte[] bytes, int startIndex) {
        return toShort(copyFrom(bytes, startIndex, 2));
    }

    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨� Unicode 瀛楃
     * @param bytes 瀛楄妭鏁扮粍
     * @return 鐢变袱涓瓧鑺傛瀯鎴愮殑瀛楃
     */
    public static char toChar(byte[] bytes) {
        if (isLittleEndian()) {
            return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
        } else {
            return (char) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
        }
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勪腑鐨勬寚瀹氱殑涓や釜瀛楄妭杞崲鏉ョ殑 Unicode 瀛楃
     * @param bytes 瀛楄妭鏁扮粍
     * @param startIndex 璧峰涓嬫爣
     * @return 鐢变袱涓瓧鑺傛瀯鎴愮殑瀛楃
     */
    public static char toChar(byte[] bytes, int startIndex) {
        return toChar(copyFrom(bytes, startIndex, 2));
    }

    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨� 32 浣嶆湁绗﹀彿鏁存暟
     * @param bytes 瀛楄妭鏁扮粍
     * @return 鐢卞洓涓瓧鑺傛瀯鎴愮殑 32 浣嶆湁绗﹀彿鏁存暟
     */
    public static int toInt(byte[] bytes) {
        if (isLittleEndian()) {
            return (0xff & bytes[0]) 
                    | (0xff00 & (bytes[1] << 8)) 
                    | (0xff0000 & (bytes[2] << 16))
                    | (0xff000000 & (bytes[3] << 24));
        } else {
            return (0xff & bytes[3]) 
                    | (0xff00 & (bytes[2] << 8)) 
                    | (0xff0000 & (bytes[1] << 16))
                    | (0xff000000 & (bytes[0] << 24));
        }
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勪腑鐨勬寚瀹氱殑鍥涗釜瀛楄妭杞崲鏉ョ殑 32 浣嶆湁绗﹀彿鏁存暟
     * @param bytes 瀛楄妭鏁扮粍
     * @param startIndex 璧峰涓嬫爣
     * @return 鐢卞洓涓瓧鑺傛瀯鎴愮殑 32 浣嶆湁绗﹀彿鏁存暟
     */
    public static int toInt(byte[] bytes, int startIndex) {
        return toInt(copyFrom(bytes, startIndex, 4));
    }

    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨� 64 浣嶆湁绗﹀彿鏁存暟
     * @param bytes 瀛楄妭鏁扮粍
     * @return 鐢卞叓涓瓧鑺傛瀯鎴愮殑 64 浣嶆湁绗﹀彿鏁存暟
     */
    public static long toLong(byte[] bytes) {
        if (isLittleEndian()) {
            return (0xffL & (long) bytes[0]) 
                    | (0xff00L & ((long) bytes[1] << 8)) 
                    | (0xff0000L & ((long) bytes[2] << 16))
                    | (0xff000000L & ((long) bytes[3] << 24)) 
                    | (0xff00000000L & ((long) bytes[4] << 32))
                    | (0xff0000000000L & ((long) bytes[5] << 40)) 
                    | (0xff000000000000L & ((long) bytes[6] << 48))
                    | (0xff00000000000000L & ((long) bytes[7] << 56));
        } else {
            return (0xffL & (long) bytes[7]) 
                    | (0xff00L & ((long) bytes[6] << 8)) 
                    | (0xff0000L & ((long) bytes[5] << 16))
                    | (0xff000000L & ((long) bytes[4] << 24)) 
                    | (0xff00000000L & ((long) bytes[3] << 32))
                    | (0xff0000000000L & ((long) bytes[2] << 40)) 
                    | (0xff000000000000L & ((long) bytes[1] << 48))
                    | (0xff00000000000000L & ((long) bytes[0] << 56));
        }
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勪腑鐨勬寚瀹氱殑鍏釜瀛楄妭杞崲鏉ョ殑 64 浣嶆湁绗﹀彿鏁存暟
     * @param bytes 瀛楄妭鏁扮粍
     * @param startIndex 璧峰涓嬫爣
     * @return 鐢卞叓涓瓧鑺傛瀯鎴愮殑 64 浣嶆湁绗﹀彿鏁存暟
     */
    public static long toLong(byte[] bytes, int startIndex) {
        return toLong(copyFrom(bytes, startIndex, 8));
    }

    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨勫崟绮惧害娴偣鏁�
     * @param bytes 瀛楄妭鏁扮粍
     * @return 鐢卞洓涓瓧鑺傛瀯鎴愮殑鍗曠簿搴︽诞鐐规暟
     */
    public static float toFloat(byte[] bytes) {
        return Float.intBitsToFloat(toInt(bytes));
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勪腑鐨勬寚瀹氱殑鍥涗釜瀛楄妭杞崲鏉ョ殑鍗曠簿搴︽诞鐐规暟
     * @param bytes 瀛楄妭鏁扮粍
     * @param startIndex 璧峰涓嬫爣
     * @return 鐢卞洓涓瓧鑺傛瀯鎴愮殑鍗曠簿搴︽诞鐐规暟
     */
    public static float toFloat(byte[] bytes, int startIndex) {
        return Float.intBitsToFloat(toInt(copyFrom(bytes, startIndex, 4)));
    }

    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨勫弻绮惧害娴偣鏁�
     * @param bytes 瀛楄妭鏁扮粍
     * @return 鐢卞叓涓瓧鑺傛瀯鎴愮殑鍙岀簿搴︽诞鐐规暟
     */
    public static double toDouble(byte[] bytes) {
        return Double.longBitsToDouble(toLong(bytes));
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勪腑鐨勬寚瀹氱殑鍏釜瀛楄妭杞崲鏉ョ殑鍙岀簿搴︽诞鐐规暟
     * @param bytes 瀛楄妭鏁扮粍
     * @param startIndex 璧峰涓嬫爣
     * @return 鐢卞叓涓瓧鑺傛瀯鎴愮殑鍙岀簿搴︽诞鐐规暟
     */
    public static double toDouble(byte[] bytes, int startIndex) {
        return Double.longBitsToDouble(toLong(copyFrom(bytes, startIndex, 8)));
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨勫瓧绗︿覆
     * @param bytes 瀛楄妭鏁扮粍
     * @return 瀛楃涓�
     */
    public static String toString(byte[] bytes) {
        return new String(bytes, Charset.forName("UTF-8"));
    }
    
    /**
     * 杩斿洖鐢卞瓧鑺傛暟缁勮浆鎹㈡潵鐨勫瓧绗︿覆
     * @param bytes 瀛楄妭鏁扮粍
     * @param charsetName 瀛楃闆嗙紪鐮�
     * @return 瀛楃涓�
     */
    public static String toString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }
    
    /**
     * 浠ュ瓧绗︿覆琛ㄧず褰㈠紡杩斿洖瀛楄妭鏁扮粍鐨勫唴瀹�
     * @param bytes 瀛楄妭鏁扮粍
     * @return 瀛楃涓插舰寮忕殑 <tt>bytes</tt>
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null)
            return "null";
        int iMax = bytes.length - 1;
        if (iMax == -1)
            return "[]";
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.format("%02x", bytes[i] & 0xFF));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
    

    // --------------------------------------------------------------------------------------------

    
    /**
     * 鏁扮粍鎷疯礉銆�
     * @param src 瀛楄妭鏁扮粍銆�
     * @param off 璧峰涓嬫爣銆�
     * @param len 鎷疯礉闀垮害銆�
     * @return 鎸囧畾闀垮害鐨勫瓧鑺傛暟缁勩��
     */
    private static byte[] copyFrom(byte[] src, int off, int len) {
        // return Arrays.copyOfRange(src, off, off + len);
        byte[] bits = new byte[len];
        for (int i = off, j = 0; i < src.length && j < len; i++, j++) {
            bits[j] = src[i];
        }
        return bits;
    }
    
    /**
     * 鍒ゆ柇 CPU Endian 鏄惁涓� Little
     * @return 鍒ゆ柇缁撴灉
     */
    private static boolean isLittleEndian() {
        return ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
    }
}
