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

        //String password = "AZaz09!#446d4gd56fgd";
        String password = "AZaz0946ddf4gd56fgd";

        if (args.length == 1)
            password = args[0];
        else
            System.out.println("Using demo mode '" + password + "'!\n" +
                               "The first parameter will taken as password in the cli.\n");

        Complexity c = complexity(password);

        System.out.println(c);

        byte[] pw = passwordToBytes(password, c);
        System.out.println("Password as array: " + Arrays.toString(pw));

        System.out.println();

        if (c.hasSpecialchars()) {
            System.out.println("Filled " + (int) Math.ceil((pw.length * 8) - password.length() * 6.3f) + " bit.");
            System.out.println("As special chars involved, this number can be very high as each char waste 0.7 bit.");
            System.out.println("Here ~ " + (int) (c.getLength() * 0.7d) + " bits");
            System.out.println("Without special chars only 0.05 bit wasted.");
        } else {
            System.out.println("Filled " + (int) Math.ceil((pw.length * 8) - password.length() * 5.95f) + " bit.");
            System.out.println("Without special chars only 0.05 bit wasted for each char.");
        }
        System.out.println("May be filled up to 7 bits one time.");

        // test remove chars

//        String demo = "§~09";
//        System.out.println(Arrays.toString(passwordToBytes(demo)));

        /*
            // ~128 bit
            // (A-Za-z0-9)
            System.out.println(bitStrength(62,22));
            // (A-Za-z0-9 + 17 special chars)
            System.out.println(bitStrength(79,21));

            // ~ 256 bit (A-Za-z0-9)
            System.out.println(bitStrength(62,43));
            // (A-Za-z0-9 + 17 special chars)
            System.out.println(bitStrength(79,41));
        */
    }

    /**
     * Charset and length of a password
     *
     * @param password
     */
    public static Complexity complexity(String password) {
        Complexity complexity = new Complexity();
        int chars = 0;
        if (password.matches(".*[0-9].*")) {
            chars = 10;
            complexity.setNumbers();
        }

        if (password.matches(".*[A-Z].*")) {
            chars += 26;
            complexity.setUppercase();
        }

        if (password.matches(".*[a-z].*")) {
            chars += 26;
            complexity.setLowercase();
        }

        if (password.matches(".*[" + SPECIALCHARS_REGEX + "].*")) {
            chars += 17;
            complexity.setSpecialcahrs();
        }

        if (chars == 0) {
            System.out.println("Can not analyse " + password);
            return new Complexity();
        }

        complexity.setCharsetSize(chars);
        complexity.setLength(password.length());
        complexity.setBit(bitStrength(chars, complexity.getLength()));

        System.out.print(chars + " chars ^ " + complexity.getLength() + " length \u2248 ");
        System.out.println(complexity.getBit() + " bit");

        return complexity;
    }

    public static byte[] passwordToBytes(String password) {
        return passwordToBytes(password, null);
    }

    /**
     * Not working yet, currently printing the position of pos^x of 79^x
     *
     * @param password
     * @return
     */
    public static byte[] passwordToBytes(String password, Complexity complexity) {

        if (password == null || password.isEmpty())
            return new byte[0];

        password = password.replaceAll("[^A-Za-z0-9" + SPECIALCHARS_REGEX + "]", "");

        if (complexity == null) {
            complexity = complexity(password);
        }

        char[] pw = password.toCharArray();
        int[] out = new int[pw.length]; // todo: size

        int neededBits = complexity.hasSpecialchars() ? 7 : 6;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < pw.length; i++) {
            out[i] = (int) pw[i];

            // Numbers
            if (out[i] > 47 && out[i] < 58) {
                out[i] -= 48 - complexity.offsetNumbers();
            }
            // A-Z
            else if (out[i] > 64 && out[i] < 91) {
                out[i] -= 65 - complexity.offsetUppercase();
            }
            // a-z
            else if (out[i] > 96 && out[i] < 123) {
                out[i] -= 97 - complexity.offsetLowercase();
            }
            // special chars
            else {
                out[i] = lookup(pw[i]);
                if (out[i] == -1) {
                    throw new RuntimeException("Unknown " + pw[i]); //Todo: handle
                }
                out[i] += complexity.offsetSpecialchars();
            }

            String t = BigInteger.valueOf(out[i]).toString(2);
            for (int j = 0; j < neededBits - t.length(); j++) { // need 7 bits
                sb.append(0);
            }
            sb.append(t);
        }

        int l = sb.length();

        int fill = Math.floorMod(l, 8);

        // leading one that is trimmed
        StringBuilder fillSB = new StringBuilder("00000001");

        // fill to byte
        for (int j = 0; j < fill; j++) {
            fillSB.append(0);
        }

        // System.out.println("sb=" + fillSB.toString() + sb.toString());

        // would trim all leading zeros so see leading one
        byte[] num = new BigInteger(fillSB.toString() + sb.toString(), 2).toByteArray();

        // trim the one
        byte[] outB = new byte[num.length - 1];
        System.arraycopy(num, 1, outB, 0, outB.length);

        return outB;
    }

    /**
     * Look up position of a special char
     *
     * @param c
     * @return
     */
    private static int lookup(char c) {

        return SPECIALCHARS.indexOf(c);
    }


    /**
     * Returns the corresponding bits of a password complexity
     *
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
