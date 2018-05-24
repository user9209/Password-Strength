package de.gs_sys.lib.crypto.passwords;

import java.math.BigInteger;
import java.util.Arrays;

import static de.gs_sys.lib.crypto.passwords.Complexity.SPECIALCHARS;
import static de.gs_sys.lib.crypto.passwords.Complexity.SPECIALCHARS_REGEX;

/*
 * Copyright (c) 2018 Georg Schmidt
 * All rights reserved
*/

public class PasswordStrength {

	/**
	 * Demo Main
	 */
    public static void main(String[] args) {

        String pw = "AZaz09!#446d4gd56fgd";

        Complexity c = complexity(pw);

        System.out.println(c);

        System.out.println(Arrays.toString(passwordToBytes(pw, c)));


        /*
            // ~128 bit
            // (A-Za-z0-9 + 10 special chars)
            System.out.println(bitStrength(72,21));
            // (A-Za-z0-9)
            System.out.println(bitStrength(62,22));

            // ~ 256 bit (A-Za-z0-9)
            System.out.println(bitStrength(62,43));
        */
    }

    /**
     * Charset and length of a password
     * @param password
     */
    public static Complexity complexity(String password) {
        Complexity complexity = new Complexity();
        int chars = 0;
        if(password.matches(".*[0-9].*"))
        {
            chars = 10;
            complexity.setNumbers();
        }

        if(password.matches(".*[A-Z].*"))
        {
            chars += 26;
            complexity.setUppercase();
        }

        if(password.matches(".*[a-z].*"))
        {
            chars += 26;
            complexity.setLowercase();
        }

        if(password.matches(".*[" + SPECIALCHARS_REGEX + "].*"))
        {
            chars += 17;
            complexity.setSpecialcahrs();
        }

        if(chars == 0)
        {
            System.out.println("Can not analyse " + password);
            return new Complexity();
        }

        System.out.print(chars + " chars ^ " + password.length() + " length \u2248 ");
        System.out.println(bitStrength(chars,password.length()) + " bit");

        return complexity;
    }

    /**
     * Not working yet, currently printing the position of pos^x of 79^x
     * @param password
     * @return
     */
    private static byte[] passwordToBytes(String password, Complexity complexity) {
        char[] pw = password.toCharArray();
        int[] out = new int[pw.length]; // todo: size

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < pw.length; i++) {
            out[i] = (int) pw[i];

            if(out[i] > 47 && out[i] < 58)
            {
                out[i] -= 48 - complexity.offsetNumbers();
            } else if(out[i] > 64 && out[i] < 91)
            {
                out[i] -= 65 - complexity.offsetUppercase();
            }
            else if(out[i] > 96 && out[i] < 123)
            {
                out[i] -= 97 - complexity.offsetLowercase();
            }
            else {
                out[i] = lookup(pw[i]);
                if(out[i] == -1)
                {
                    throw new RuntimeException("Unknown " + pw[i]);//Todo: handle
                }
                out[i] += complexity.offsetSpecialchars();
            }

            String t = BigInteger.valueOf(out[i]).toString(2);
            for (int j = 0; j < 7 - t.length(); j++) { // need 7 bits
                sb.append(0);
            }
            sb.append(t);
        }



        int l = sb.length();

        int fill = Math.floorMod(l,8);

        // leading one that is trimmed
        StringBuilder fillSB = new StringBuilder("00000001");

        // fill to byte
        for (int j = 0; j < fill; j++) {
            fillSB.append(0);
        }

        // System.out.println("sb=" + fillSB.toString() + sb.toString());

        // would trim all leading zeros so see leading one
        byte[] num = new BigInteger(fillSB.toString() + sb.toString(),2).toByteArray();
        
        // trim the one
        byte[] outB = new byte[num.length - 1];
        System.arraycopy(num,1,outB,0,outB.length);

        return outB;
    }

    /**
     * Look up position of a special char
     * @param c
     * @return
     */
    private static int lookup(char c) {

        return SPECIALCHARS.indexOf(c);
    }


    /**
     * Returns the corresponding bits of a password complexity
     * @param base
     * @param exponent
     * @return
     */
    public static int bitStrength(int base, int exponent) {
        BigInteger b = BigInteger.valueOf(base);

        BigInteger r = b.pow(exponent);

        return (int) (Math.log(r.doubleValue()) / Math.log(2));
    }
}
