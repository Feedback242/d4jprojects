// This is a mutant program.
// Author : ysma

package org.apache.commons.math3.util;


import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;


public abstract class ContinuedFraction
{

    private static final double DEFAULT_EPSILON = 10e-9;

    protected ContinuedFraction()
    {
        super();
    }

    protected abstract  double getA( int n, double x );

    protected abstract  double getB( int n, double x );

    public  double evaluate( double x )
    {
        return evaluate( x, DEFAULT_EPSILON, Integer.MAX_VALUE );
    }

    public  double evaluate( double x, double epsilon )
    {
        return evaluate( x, epsilon, Integer.MAX_VALUE );
    }

    public  double evaluate( double x, int maxIterations )
    {
        return evaluate( x, DEFAULT_EPSILON, maxIterations );
    }

    public  double evaluate( double x, double epsilon, int maxIterations )
    {
        final double small = 1e-50;
        double hPrev = getA( 0, x );
        if (Precision.equals( hPrev, 0.0, small )) {
            hPrev = small;
        }
        int n = 1;
        double dPrev = 0.0;
        double p0 = 1.0;
        double q1 = 1.0;
        double cPrev = hPrev;
        double hN = hPrev;
        if (n >= maxIterations) {
            throw new org.apache.commons.math3.exception.MaxCountExceededException( LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x );
        }
        return hN;
    }

}
