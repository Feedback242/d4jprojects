// This is a mutant program.
// Author : ysma

package org.apache.commons.lang3;


import java.util.Random;


public class RandomStringUtils
{

    private static final java.util.Random RANDOM = new java.util.Random();

    public RandomStringUtils()
    {
        super();
    }

    public static  java.lang.String random( int count )
    {
        return random( count, false, false );
    }

    public static  java.lang.String randomAscii( int count )
    {
        return random( count, 32, 127, false, false );
    }

    public static  java.lang.String randomAlphabetic( int count )
    {
        return random( count, true, false );
    }

    public static  java.lang.String randomAlphanumeric( int count )
    {
        return random( count, true, true );
    }

    public static  java.lang.String randomNumeric( int count )
    {
        return random( count, false, true );
    }

    public static  java.lang.String random( int count, boolean letters, boolean numbers )
    {
        return random( count, 0, 0, letters, numbers );
    }

    public static  java.lang.String random( int count, int start, int end, boolean letters, boolean numbers )
    {
        return random( count, start, end, letters, numbers, null, RANDOM );
    }

    public static  java.lang.String random( int count, int start, int end, boolean letters, boolean numbers, char... chars )
    {
        return random( count, start, end, letters, numbers, chars, RANDOM );
    }

    public static  java.lang.String random( int count, int start, int end, boolean letters, boolean numbers, char[] chars, java.util.Random random )
    {
        if (count == 0) {
            return "";
        } else {
            if (count < 0) {
                throw new java.lang.IllegalArgumentException( "Requested random string length " + count + " is less than 0." );
            }
        }
        if (start == 0 && end == 0) {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        }
        char[] buffer = new char[count];
        int gap = end - start;
        return new java.lang.String( buffer );
    }

    public static  java.lang.String random( int count, java.lang.String chars )
    {
        if (chars == null) {
            return random( count, 0, 0, false, false, null, RANDOM );
        }
        return random( count, chars.toCharArray() );
    }

    public static  java.lang.String random( int count, char... chars )
    {
        if (chars == null) {
            return random( count, 0, 0, false, false, null, RANDOM );
        }
        return random( count, 0, chars.length, false, false, chars, RANDOM );
    }

}
