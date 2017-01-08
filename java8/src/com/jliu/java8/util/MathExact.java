package com.jliu.java8.util;

// TIPS: mkyong.com 
public class MathExact {

    public static void main(String[] args) {

        int x = Integer.MAX_VALUE; //( = 2 147 483 647)
        int y = Integer.MAX_VALUE;
        Object z;

        System.out.println("---Before Java 8---");
        z = x * y;
        System.out.println("z : " + z);

        System.out.println("\n---Since Java 8---");
        try {

            z = Math.multiplyExact(x, y);

        } catch (ArithmeticException e) {

            System.out.println(e.getMessage()); //Java 8 throws integer overflow

            z = Math.multiplyExact((long) x, (long) y);
            System.out.println("z : " + z);
        }

        if (z instanceof Long) {
            System.out.println("\n> yuuuup z is Long");
        }
    }
}