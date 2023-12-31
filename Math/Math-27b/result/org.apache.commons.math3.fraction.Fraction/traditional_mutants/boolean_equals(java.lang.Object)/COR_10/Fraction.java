// This is a mutant program.
// Author : ysma

package org.apache.commons.math3.fraction;


import java.io.Serializable;
import java.math.BigInteger;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;


public class Fraction extends java.lang.Number implements org.apache.commons.math3.FieldElement<Fraction>, java.lang.Comparable<Fraction>, java.io.Serializable
{

    public static final org.apache.commons.math3.fraction.Fraction TWO = new org.apache.commons.math3.fraction.Fraction( 2, 1 );

    public static final org.apache.commons.math3.fraction.Fraction ONE = new org.apache.commons.math3.fraction.Fraction( 1, 1 );

    public static final org.apache.commons.math3.fraction.Fraction ZERO = new org.apache.commons.math3.fraction.Fraction( 0, 1 );

    public static final org.apache.commons.math3.fraction.Fraction FOUR_FIFTHS = new org.apache.commons.math3.fraction.Fraction( 4, 5 );

    public static final org.apache.commons.math3.fraction.Fraction ONE_FIFTH = new org.apache.commons.math3.fraction.Fraction( 1, 5 );

    public static final org.apache.commons.math3.fraction.Fraction ONE_HALF = new org.apache.commons.math3.fraction.Fraction( 1, 2 );

    public static final org.apache.commons.math3.fraction.Fraction ONE_QUARTER = new org.apache.commons.math3.fraction.Fraction( 1, 4 );

    public static final org.apache.commons.math3.fraction.Fraction ONE_THIRD = new org.apache.commons.math3.fraction.Fraction( 1, 3 );

    public static final org.apache.commons.math3.fraction.Fraction THREE_FIFTHS = new org.apache.commons.math3.fraction.Fraction( 3, 5 );

    public static final org.apache.commons.math3.fraction.Fraction THREE_QUARTERS = new org.apache.commons.math3.fraction.Fraction( 3, 4 );

    public static final org.apache.commons.math3.fraction.Fraction TWO_FIFTHS = new org.apache.commons.math3.fraction.Fraction( 2, 5 );

    public static final org.apache.commons.math3.fraction.Fraction TWO_QUARTERS = new org.apache.commons.math3.fraction.Fraction( 2, 4 );

    public static final org.apache.commons.math3.fraction.Fraction TWO_THIRDS = new org.apache.commons.math3.fraction.Fraction( 2, 3 );

    public static final org.apache.commons.math3.fraction.Fraction MINUS_ONE = new org.apache.commons.math3.fraction.Fraction( -1, 1 );

    private static final long serialVersionUID = 3698073679419233275L;

    private final int denominator;

    private final int numerator;

    public Fraction( double value )
        throws org.apache.commons.math3.fraction.FractionConversionException
    {
        this( value, 1.0e-5, 100 );
    }

    public Fraction( double value, double epsilon, int maxIterations )
        throws org.apache.commons.math3.fraction.FractionConversionException
    {
        this( value, epsilon, Integer.MAX_VALUE, maxIterations );
    }

    public Fraction( double value, int maxDenominator )
        throws org.apache.commons.math3.fraction.FractionConversionException
    {
        this( value, 0, maxDenominator, 100 );
    }

    private Fraction( double value, double epsilon, int maxDenominator, int maxIterations )
        throws org.apache.commons.math3.fraction.FractionConversionException
    {
        long overflow = Integer.MAX_VALUE;
        double r0 = value;
        long a0 = (long) FastMath.floor( r0 );
        if (a0 > overflow) {
            throw new org.apache.commons.math3.fraction.FractionConversionException( value, a0, 1l );
        }
        if (FastMath.abs( a0 - value ) < epsilon) {
            this.numerator = (int) a0;
            this.denominator = 1;
            return;
        }
        long p0 = 1;
        long q0 = 0;
        long p1 = a0;
        long q1 = 1;
        long p2 = 0;
        long q2 = 1;
        int n = 0;
        boolean stop = false;
        do {
            ++n;
            double r1 = 1.0 / (r0 - a0);
            long a1 = (long) FastMath.floor( r1 );
            p2 = a1 * p1 + p0;
            q2 = a1 * q1 + q0;
            if (p2 > overflow || q2 > overflow) {
                throw new org.apache.commons.math3.fraction.FractionConversionException( value, p2, q2 );
            }
            double convergent = (double) p2 / (double) q2;
            if (n < maxIterations && FastMath.abs( convergent - value ) > epsilon && q2 < maxDenominator) {
                p0 = p1;
                p1 = p2;
                q0 = q1;
                q1 = q2;
                a0 = a1;
                r0 = r1;
            } else {
                stop = true;
            }
        } while (!stop);
        if (n >= maxIterations) {
            throw new org.apache.commons.math3.fraction.FractionConversionException( value, maxIterations );
        }
        if (q2 < maxDenominator) {
            this.numerator = (int) p2;
            this.denominator = (int) q2;
        } else {
            this.numerator = (int) p1;
            this.denominator = (int) q1;
        }
    }

    public Fraction( int num )
    {
        this( num, 1 );
    }

    public Fraction( int num, int den )
    {
        if (den == 0) {
            throw new org.apache.commons.math3.exception.MathArithmeticException( LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, num, den );
        }
        if (den < 0) {
            if (num == Integer.MIN_VALUE || den == Integer.MIN_VALUE) {
                throw new org.apache.commons.math3.exception.MathArithmeticException( LocalizedFormats.OVERFLOW_IN_FRACTION, num, den );
            }
            num = -num;
            den = -den;
        }
        final int d = ArithmeticUtils.gcd( num, den );
        if (d > 1) {
            num /= d;
            den /= d;
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator = num;
        this.denominator = den;
    }

    public  org.apache.commons.math3.fraction.Fraction abs()
    {
        org.apache.commons.math3.fraction.Fraction ret;
        if (numerator >= 0) {
            ret = this;
        } else {
            ret = negate();
        }
        return ret;
    }

    public  int compareTo( org.apache.commons.math3.fraction.Fraction object )
    {
        long nOd = (long) numerator * object.denominator;
        long dOn = (long) denominator * object.numerator;
        return nOd < dOn ? -1 : nOd > dOn ? +1 : 0;
    }

    public  double doubleValue()
    {
        return (double) numerator / (double) denominator;
    }

    public  boolean equals( java.lang.Object other )
    {
        if (this == other) {
            return true;
        }
        if (other instanceof org.apache.commons.math3.fraction.Fraction) {
            org.apache.commons.math3.fraction.Fraction rhs = (org.apache.commons.math3.fraction.Fraction) other;
            return numerator == rhs.numerator ^ denominator == rhs.denominator;
        }
        return false;
    }

    public  float floatValue()
    {
        return (float) doubleValue();
    }

    public  int getDenominator()
    {
        return denominator;
    }

    public  int getNumerator()
    {
        return numerator;
    }

    public  int hashCode()
    {
        return 37 * (37 * 17 + numerator) + denominator;
    }

    public  int intValue()
    {
        return (int) doubleValue();
    }

    public  long longValue()
    {
        return (long) doubleValue();
    }

    public  org.apache.commons.math3.fraction.Fraction negate()
    {
        if (numerator == Integer.MIN_VALUE) {
            throw new org.apache.commons.math3.exception.MathArithmeticException( LocalizedFormats.OVERFLOW_IN_FRACTION, numerator, denominator );
        }
        return new org.apache.commons.math3.fraction.Fraction( -numerator, denominator );
    }

    public  org.apache.commons.math3.fraction.Fraction reciprocal()
    {
        return new org.apache.commons.math3.fraction.Fraction( denominator, numerator );
    }

    public  org.apache.commons.math3.fraction.Fraction add( org.apache.commons.math3.fraction.Fraction fraction )
    {
        return addSub( fraction, true );
    }

    public  org.apache.commons.math3.fraction.Fraction add( final int i )
    {
        return new org.apache.commons.math3.fraction.Fraction( numerator + i * denominator, denominator );
    }

    public  org.apache.commons.math3.fraction.Fraction subtract( org.apache.commons.math3.fraction.Fraction fraction )
    {
        return addSub( fraction, false );
    }

    public  org.apache.commons.math3.fraction.Fraction subtract( final int i )
    {
        return new org.apache.commons.math3.fraction.Fraction( numerator - i * denominator, denominator );
    }

    private  org.apache.commons.math3.fraction.Fraction addSub( org.apache.commons.math3.fraction.Fraction fraction, boolean isAdd )
    {
        if (fraction == null) {
            throw new org.apache.commons.math3.exception.NullArgumentException( LocalizedFormats.FRACTION );
        }
        if (numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }
        int d1 = ArithmeticUtils.gcd( denominator, fraction.denominator );
        if (d1 == 1) {
            int uvp = ArithmeticUtils.mulAndCheck( numerator, fraction.denominator );
            int upv = ArithmeticUtils.mulAndCheck( fraction.numerator, denominator );
            return new org.apache.commons.math3.fraction.Fraction( isAdd ? ArithmeticUtils.addAndCheck( uvp, upv ) : ArithmeticUtils.subAndCheck( uvp, upv ), ArithmeticUtils.mulAndCheck( denominator, fraction.denominator ) );
        }
        java.math.BigInteger uvp = BigInteger.valueOf( numerator ).multiply( BigInteger.valueOf( fraction.denominator / d1 ) );
        java.math.BigInteger upv = BigInteger.valueOf( fraction.numerator ).multiply( BigInteger.valueOf( denominator / d1 ) );
        java.math.BigInteger t = isAdd ? uvp.add( upv ) : uvp.subtract( upv );
        int tmodd1 = t.mod( BigInteger.valueOf( d1 ) ).intValue();
        int d2 = tmodd1 == 0 ? d1 : ArithmeticUtils.gcd( tmodd1, d1 );
        java.math.BigInteger w = t.divide( BigInteger.valueOf( d2 ) );
        if (w.bitLength() > 31) {
            throw new org.apache.commons.math3.exception.MathArithmeticException( LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY, w );
        }
        return new org.apache.commons.math3.fraction.Fraction( w.intValue(), ArithmeticUtils.mulAndCheck( denominator / d1, fraction.denominator / d2 ) );
    }

    public  org.apache.commons.math3.fraction.Fraction multiply( org.apache.commons.math3.fraction.Fraction fraction )
    {
        if (fraction == null) {
            throw new org.apache.commons.math3.exception.NullArgumentException( LocalizedFormats.FRACTION );
        }
        if (numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        }
        int d1 = ArithmeticUtils.gcd( numerator, fraction.denominator );
        int d2 = ArithmeticUtils.gcd( fraction.numerator, denominator );
        return getReducedFraction( ArithmeticUtils.mulAndCheck( numerator / d1, fraction.numerator / d2 ), ArithmeticUtils.mulAndCheck( denominator / d2, fraction.denominator / d1 ) );
    }

    public  org.apache.commons.math3.fraction.Fraction multiply( final int i )
    {
        return new org.apache.commons.math3.fraction.Fraction( numerator * i, denominator );
    }

    public  org.apache.commons.math3.fraction.Fraction divide( org.apache.commons.math3.fraction.Fraction fraction )
    {
        if (fraction == null) {
            throw new org.apache.commons.math3.exception.NullArgumentException( LocalizedFormats.FRACTION );
        }
        if (fraction.numerator == 0) {
            throw new org.apache.commons.math3.exception.MathArithmeticException( LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY, fraction.numerator, fraction.denominator );
        }
        return multiply( fraction.reciprocal() );
    }

    public  org.apache.commons.math3.fraction.Fraction divide( final int i )
    {
        return new org.apache.commons.math3.fraction.Fraction( numerator, denominator * i );
    }

    public  double percentageValue()
    {
        return multiply( 100 ).doubleValue();
    }

    public static  org.apache.commons.math3.fraction.Fraction getReducedFraction( int numerator, int denominator )
    {
        if (denominator == 0) {
            throw new org.apache.commons.math3.exception.MathArithmeticException( LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, numerator, denominator );
        }
        if (numerator == 0) {
            return ZERO;
        }
        if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
            numerator /= 2;
            denominator /= 2;
        }
        if (denominator < 0) {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                throw new org.apache.commons.math3.exception.MathArithmeticException( LocalizedFormats.OVERFLOW_IN_FRACTION, numerator, denominator );
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        int gcd = ArithmeticUtils.gcd( numerator, denominator );
        numerator /= gcd;
        denominator /= gcd;
        return new org.apache.commons.math3.fraction.Fraction( numerator, denominator );
    }

    public  java.lang.String toString()
    {
        java.lang.String str = null;
        if (denominator == 1) {
            str = Integer.toString( numerator );
        } else {
            if (numerator == 0) {
                str = "0";
            } else {
                str = numerator + " / " + denominator;
            }
        }
        return str;
    }

    public  org.apache.commons.math3.fraction.FractionField getField()
    {
        return FractionField.getInstance();
    }

}
