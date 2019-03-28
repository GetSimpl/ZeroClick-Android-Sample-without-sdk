package com.simpl.pay.sample.zc_s2s.utils;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class Decompression {

    public static String run(String text) throws IOException {
        byte[] decodedBytes = Base64.decode(text, Base64.NO_WRAP);
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(unobfuscate(decodedBytes)));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String line = bf.readLine();
        bf.close();
        gis.close();
        return line;
    }

    private static byte[] unobfuscate(byte[] bytes) {
        byte[] result = new byte[bytes.length - 1];
        int shifts = bytes[getMedianPosition(result.length) - 1];
        int j=0;
        for(int i=0; i<bytes.length; i++) {
            if(isMedianPosition(result.length, i + 1)) {
                continue;
            }
            result[j] = rotateLeft(bytes[i], shifts);
            j++;
        }

        return result;
    }

    private static int getMedianPosition(int length) {
        return length % 2 == 0 ? length / 2 : (length + 1) / 2 ;
    }

    private static boolean isMedianPosition(int length, int currentPosition) {
        return getMedianPosition(length) == currentPosition;
    }

    private static byte rotateLeft(byte b, int shifts) {
        int BYTE_LENGTH = 8;
        return (byte)((( b & 0xff) << shifts) | ((b & 0xff) >>> BYTE_LENGTH - shifts));
    }
}

