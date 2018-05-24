package de.gs_sys.lib.crypto.passwords;

/*
 * Copyright (c) 2018 Georg Schmidt
 * All rights reserved
 */

public class Complexity {

    public static final String SPECIALCHARS = "!$%&/()=?-_@+*.:#";
    public static final String SPECIALCHARS_REGEX = "!$%&/()=?\\-_@+*.:#";

    private boolean uppercase = false;
    private boolean lowercase = false;
    private boolean numbers = false;
    private boolean specialchars = false;

    public void setUppercase() {
        this.uppercase = true;
    }

    public void setLowercase() {
        this.lowercase = true;
    }

    public void setNumbers() {
        this.numbers = true;
    }

    public void setSpecialcahrs()  {
        this.specialchars = true;
    }

    public int offsetUppercase() {
        return 0;
    }

    public int offsetLowercase() {
       return offsetUppercase() + (uppercase ? 26 : 0) ;
    }

    public int offsetNumbers() {
        return offsetLowercase() + (lowercase ? 26 : 0);
    }

    public int offsetSpecialchars() {
        return offsetNumbers() + (numbers? 10 : 0); //+ (specialchars ? SPECIALCHARS.length() : 0);
    }

    @Override
    public String toString() {
        return "Complexity{" +
                "uppercase=" + uppercase +
                ", lowercase=" + lowercase +
                ", numbers=" + numbers +
                ", specialchars=" + specialchars +
                '}';
    }

    public String toString2() {
        return "Complexity{" +
                "uppercase=" + offsetUppercase() +
                ", lowercase=" + offsetLowercase() +
                ", numbers=" + offsetNumbers() +
                ", specialchars=" + offsetSpecialchars() +
                '}';
    }
}
