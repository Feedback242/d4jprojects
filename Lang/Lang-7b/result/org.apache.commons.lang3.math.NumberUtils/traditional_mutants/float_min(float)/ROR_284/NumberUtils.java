// This is a mutant program.
// Author : ysma

package org.apache.commons.lang3.math;


import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;


public class NumberUtils
{

    public static final java.lang.Long LONG_ZERO = Long.valueOf( 0L );

    public static final java.lang.Long LONG_ONE = Long.valueOf( 1L );

    public static final java.lang.Long LONG_MINUS_ONE = Long.valueOf( -1L );

    public static final java.lang.Integer INTEGER_ZERO = Integer.valueOf( 0 );

    public static final java.lang.Integer INTEGER_ONE = Integer.valueOf( 1 );

    public static final java.lang.Integer INTEGER_MINUS_ONE = Integer.valueOf( -1 );

    public static final java.lang.Short SHORT_ZERO = Short.valueOf( (short) 0 );

    public static final java.lang.Short SHORT_ONE = Short.valueOf( (short) 1 );

    public static final java.lang.Short SHORT_MINUS_ONE = Short.valueOf( (short) (-1) );

    public static final java.lang.Byte BYTE_ZERO = Byte.valueOf( (byte) 0 );

    public static final java.lang.Byte BYTE_ONE = Byte.valueOf( (byte) 1 );

    public static final java.lang.Byte BYTE_MINUS_ONE = Byte.valueOf( (byte) (-1) );

    public static final java.lang.Double DOUBLE_ZERO = Double.valueOf( 0.0d );

    public static final java.lang.Double DOUBLE_ONE = Double.valueOf( 1.0d );

    public static final java.lang.Double DOUBLE_MINUS_ONE = Double.valueOf( -1.0d );

    public static final java.lang.Float FLOAT_ZERO = Float.valueOf( 0.0f );

    public static final java.lang.Float FLOAT_ONE = Float.valueOf( 1.0f );

    public static final java.lang.Float FLOAT_MINUS_ONE = Float.valueOf( -1.0f );

    public NumberUtils()
    {
        super();
    }

    public static  int toInt( java.lang.String str )
    {
        return toInt( str, 0 );
    }

    public static  int toInt( java.lang.String str, int defaultValue )
    {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt( str );
        } catch ( java.lang.NumberFormatException nfe ) {
            return defaultValue;
        }
    }

    public static  long toLong( java.lang.String str )
    {
        return toLong( str, 0L );
    }

    public static  long toLong( java.lang.String str, long defaultValue )
    {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong( str );
        } catch ( java.lang.NumberFormatException nfe ) {
            return defaultValue;
        }
    }

    public static  float toFloat( java.lang.String str )
    {
        return toFloat( str, 0.0f );
    }

    public static  float toFloat( java.lang.String str, float defaultValue )
    {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat( str );
        } catch ( java.lang.NumberFormatException nfe ) {
            return defaultValue;
        }
    }

    public static  double toDouble( java.lang.String str )
    {
        return toDouble( str, 0.0d );
    }

    public static  double toDouble( java.lang.String str, double defaultValue )
    {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble( str );
        } catch ( java.lang.NumberFormatException nfe ) {
            return defaultValue;
        }
    }

    public static  byte toByte( java.lang.String str )
    {
        return toByte( str, (byte) 0 );
    }

    public static  byte toByte( java.lang.String str, byte defaultValue )
    {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte( str );
        } catch ( java.lang.NumberFormatException nfe ) {
            return defaultValue;
        }
    }

    public static  short toShort( java.lang.String str )
    {
        return toShort( str, (short) 0 );
    }

    public static  short toShort( java.lang.String str, short defaultValue )
    {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort( str );
        } catch ( java.lang.NumberFormatException nfe ) {
            return defaultValue;
        }
    }

    public static  java.lang.Number createNumber( java.lang.String str )
        throws java.lang.NumberFormatException
    {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank( str )) {
            throw new java.lang.NumberFormatException( "A blank string is not a valid number" );
        }
        if (str.startsWith( "--" )) {
            return null;
        }
        if (str.startsWith( "0x" ) || str.startsWith( "-0x" ) || str.startsWith( "0X" ) || str.startsWith( "-0X" )) {
            int hexDigits = str.length() - 2;
            if (str.startsWith( "-" )) {
                hexDigits--;
            }
            if (hexDigits > 8) {
                return createLong( str );
            }
            return createInteger( str );
        }
        char lastChar = str.charAt( str.length() - 1 );
        java.lang.String mant;
        java.lang.String dec;
        java.lang.String exp;
        int decPos = str.indexOf( '.' );
        int expPos = str.indexOf( 'e' ) + str.indexOf( 'E' ) + 1;
        if (decPos > -1) {
            if (expPos > -1) {
                if (expPos < decPos || expPos > str.length()) {
                    throw new java.lang.NumberFormatException( str + " is not a valid number." );
                }
                dec = str.substring( decPos + 1, expPos );
            } else {
                dec = str.substring( decPos + 1 );
            }
            mant = str.substring( 0, decPos );
        } else {
            if (expPos > -1) {
                if (expPos > str.length()) {
                    throw new java.lang.NumberFormatException( str + " is not a valid number." );
                }
                mant = str.substring( 0, expPos );
            } else {
                mant = str;
            }
            dec = null;
        }
        if (!Character.isDigit( lastChar ) && lastChar != '.') {
            if (expPos > -1 && expPos < str.length() - 1) {
                exp = str.substring( expPos + 1, str.length() - 1 );
            } else {
                exp = null;
            }
            java.lang.String numeric = str.substring( 0, str.length() - 1 );
            boolean allZeros = isAllZeros( mant ) && isAllZeros( exp );
            switch (lastChar) {
            case 'l' :
            case 'L' :
                if (dec == null && exp == null && (numeric.charAt( 0 ) == '-' && isDigits( numeric.substring( 1 ) ) || isDigits( numeric ))) {
                    try {
                        return createLong( numeric );
                    } catch ( java.lang.NumberFormatException nfe ) {
                    }
                    return createBigInteger( numeric );
                }
                throw new java.lang.NumberFormatException( str + " is not a valid number." );

            case 'f' :
            case 'F' :
                try {
                    java.lang.Float f = NumberUtils.createFloat( numeric );
                    if (!(f.isInfinite() || f.floatValue() == 0.0F && !allZeros)) {
                        return f;
                    }
                } catch ( java.lang.NumberFormatException nfe ) {
                }

            case 'd' :
            case 'D' :
                try {
                    java.lang.Double d = NumberUtils.createDouble( numeric );
                    if (!(d.isInfinite() || d.floatValue() == 0.0D && !allZeros)) {
                        return d;
                    }
                } catch ( java.lang.NumberFormatException nfe ) {
                }
                try {
                    return createBigDecimal( numeric );
                } catch ( java.lang.NumberFormatException e ) {
                }

            default  :
                throw new java.lang.NumberFormatException( str + " is not a valid number." );

            }
        } else {
            if (expPos > -1 && expPos < str.length() - 1) {
                exp = str.substring( expPos + 1, str.length() );
            } else {
                exp = null;
            }
            if (dec == null && exp == null) {
                try {
                    return createInteger( str );
                } catch ( java.lang.NumberFormatException nfe ) {
                }
                try {
                    return createLong( str );
                } catch ( java.lang.NumberFormatException nfe ) {
                }
                return createBigInteger( str );
            } else {
                boolean allZeros = isAllZeros( mant ) && isAllZeros( exp );
                try {
                    java.lang.Float f = createFloat( str );
                    if (!(f.isInfinite() || f.floatValue() == 0.0F && !allZeros)) {
                        return f;
                    }
                } catch ( java.lang.NumberFormatException nfe ) {
                }
                try {
                    java.lang.Double d = createDouble( str );
                    if (!(d.isInfinite() || d.doubleValue() == 0.0D && !allZeros)) {
                        return d;
                    }
                } catch ( java.lang.NumberFormatException nfe ) {
                }
                return createBigDecimal( str );
            }
        }
    }

    private static  boolean isAllZeros( java.lang.String str )
    {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt( i ) != '0') {
                return false;
            }
        }
        return str.length() > 0;
    }

    public static  java.lang.Float createFloat( java.lang.String str )
    {
        if (str == null) {
            return null;
        }
        return Float.valueOf( str );
    }

    public static  java.lang.Double createDouble( java.lang.String str )
    {
        if (str == null) {
            return null;
        }
        return Double.valueOf( str );
    }

    public static  java.lang.Integer createInteger( java.lang.String str )
    {
        if (str == null) {
            return null;
        }
        return Integer.decode( str );
    }

    public static  java.lang.Long createLong( java.lang.String str )
    {
        if (str == null) {
            return null;
        }
        return Long.decode( str );
    }

    public static  java.math.BigInteger createBigInteger( java.lang.String str )
    {
        if (str == null) {
            return null;
        }
        return new java.math.BigInteger( str );
    }

    public static  java.math.BigDecimal createBigDecimal( java.lang.String str )
    {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank( str )) {
            throw new java.lang.NumberFormatException( "A blank string is not a valid number" );
        }
        return new java.math.BigDecimal( str );
    }

    public static  long min( long[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static  int min( int[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        int min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] < min) {
                min = array[j];
            }
        }
        return min;
    }

    public static  short min( short[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static  byte min( byte[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static  double min( double[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Double.isNaN( array[i] )) {
                return Double.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static  float min( float[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Float.isNaN( array[i] )) {
                return Float.NaN;
            }
            if (array[i] > min) {
                min = array[i];
            }
        }
        return min;
    }

    public static  long max( long[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        long max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static  int max( int[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        int max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static  short max( short[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static  byte max( byte[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static  double max( double[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Double.isNaN( array[j] )) {
                return Double.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static  float max( float[] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        } else {
            if (array.length == 0) {
                throw new java.lang.IllegalArgumentException( "Array cannot be empty." );
            }
        }
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN( array[j] )) {
                return Float.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static  long min( long a, long b, long c )
    {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    public static  int min( int a, int b, int c )
    {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    public static  short min( short a, short b, short c )
    {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    public static  byte min( byte a, byte b, byte c )
    {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    public static  double min( double a, double b, double c )
    {
        return Math.min( Math.min( a, b ), c );
    }

    public static  float min( float a, float b, float c )
    {
        return Math.min( Math.min( a, b ), c );
    }

    public static  long max( long a, long b, long c )
    {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    public static  int max( int a, int b, int c )
    {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    public static  short max( short a, short b, short c )
    {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    public static  byte max( byte a, byte b, byte c )
    {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    public static  double max( double a, double b, double c )
    {
        return Math.max( Math.max( a, b ), c );
    }

    public static  float max( float a, float b, float c )
    {
        return Math.max( Math.max( a, b ), c );
    }

    public static  boolean isDigits( java.lang.String str )
    {
        if (StringUtils.isEmpty( str )) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit( str.charAt( i ) )) {
                return false;
            }
        }
        return true;
    }

    public static  boolean isNumber( java.lang.String str )
    {
        if (StringUtils.isEmpty( str )) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        int start = chars[0] == '-' ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false;
            }
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true;
        }
        sz--;
        int i = start;
        while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;
            } else {
                if (chars[i] == '.') {
                    if (hasDecPoint || hasExp) {
                        return false;
                    }
                    hasDecPoint = true;
                } else {
                    if (chars[i] == 'e' || chars[i] == 'E') {
                        if (hasExp) {
                            return false;
                        }
                        if (!foundDigit) {
                            return false;
                        }
                        hasExp = true;
                        allowSigns = true;
                    } else {
                        if (chars[i] == '+' || chars[i] == '-') {
                            if (!allowSigns) {
                                return false;
                            }
                            allowSigns = false;
                            foundDigit = false;
                        } else {
                            return false;
                        }
                    }
                }
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    return false;
                }
                return foundDigit;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                return foundDigit && !hasExp && !hasDecPoint;
            }
            return false;
        }
        return !allowSigns && foundDigit;
    }

}
