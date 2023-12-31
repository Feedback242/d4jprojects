// This is a mutant program.
// Author : ysma

package org.apache.commons.math3.distribution;


import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;


public class FDistribution extends org.apache.commons.math3.distribution.AbstractRealDistribution
{

    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    private static final long serialVersionUID = -8516354193418641566L;

    private final double numeratorDegreesOfFreedom;

    private final double denominatorDegreesOfFreedom;

    private final double solverAbsoluteAccuracy;

    private double numericalVariance = Double.NaN;

    private boolean numericalVarianceIsCalculated = false;

    public FDistribution( double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom )
        throws org.apache.commons.math3.exception.NotStrictlyPositiveException
    {
        this( numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, DEFAULT_INVERSE_ABSOLUTE_ACCURACY );
    }

    public FDistribution( double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double inverseCumAccuracy )
        throws org.apache.commons.math3.exception.NotStrictlyPositiveException
    {
        this( new org.apache.commons.math3.random.Well19937c(), numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, inverseCumAccuracy );
    }

    public FDistribution( org.apache.commons.math3.random.RandomGenerator rng, double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double inverseCumAccuracy )
        throws org.apache.commons.math3.exception.NotStrictlyPositiveException
    {
        super( rng );
        if (numeratorDegreesOfFreedom <= 0) {
            throw new org.apache.commons.math3.exception.NotStrictlyPositiveException( LocalizedFormats.DEGREES_OF_FREEDOM, numeratorDegreesOfFreedom );
        }
        if (denominatorDegreesOfFreedom <= 0) {
            throw new org.apache.commons.math3.exception.NotStrictlyPositiveException( LocalizedFormats.DEGREES_OF_FREEDOM, denominatorDegreesOfFreedom );
        }
        this.numeratorDegreesOfFreedom = numeratorDegreesOfFreedom;
        this.denominatorDegreesOfFreedom = denominatorDegreesOfFreedom;
        solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public  double density( double x )
    {
        final double nhalf = numeratorDegreesOfFreedom / 2;
        final double mhalf = denominatorDegreesOfFreedom / 2;
        final double logx = FastMath.log( x );
        final double logn = FastMath.log( numeratorDegreesOfFreedom );
        final double logm = FastMath.log( denominatorDegreesOfFreedom );
        final double lognxm = FastMath.log( numeratorDegreesOfFreedom * x + denominatorDegreesOfFreedom );
        return FastMath.exp( nhalf * logn + nhalf * logx - logx + mhalf * logm - nhalf * lognxm - mhalf * lognxm - Beta.logBeta( nhalf, mhalf ) );
    }

    public  double cumulativeProbability( double x )
    {
        double ret;
        if (x <= 0) {
            ret = 0;
        } else {
            double n = numeratorDegreesOfFreedom;
            double m = denominatorDegreesOfFreedom;
            ret = Beta.regularizedBeta( n * x / (m + n * x), 0.5 * n, 0.5 * m );
        }
        return ret;
    }

    public  double getNumeratorDegreesOfFreedom()
    {
        return numeratorDegreesOfFreedom;
    }

    public  double getDenominatorDegreesOfFreedom()
    {
        return denominatorDegreesOfFreedom;
    }

    protected  double getSolverAbsoluteAccuracy()
    {
        return solverAbsoluteAccuracy;
    }

    public  double getNumericalMean()
    {
        final double denominatorDF = getDenominatorDegreesOfFreedom();
        if (denominatorDF > 2) {
            return denominatorDF / (denominatorDF - 2);
        }
        return Double.NaN;
    }

    public  double getNumericalVariance()
    {
        if (!numericalVarianceIsCalculated) {
            numericalVariance = calculateNumericalVariance();
            numericalVarianceIsCalculated = true;
        }
        return numericalVariance;
    }

    protected  double calculateNumericalVariance()
    {
        final double denominatorDF = getDenominatorDegreesOfFreedom();
        if (denominatorDF > 4) {
            final double numeratorDF = getNumeratorDegreesOfFreedom();
            final double denomDFMinusTwo = denominatorDF - 2;
            return 2 * (denominatorDF * denominatorDF) * (numeratorDF - denominatorDF - 2) / (numeratorDF * (denomDFMinusTwo * denomDFMinusTwo) * (denominatorDF - 4));
        }
        return Double.NaN;
    }

    public  double getSupportLowerBound()
    {
        return 0;
    }

    public  double getSupportUpperBound()
    {
        return Double.POSITIVE_INFINITY;
    }

    public  boolean isSupportLowerBoundInclusive()
    {
        return true;
    }

    public  boolean isSupportUpperBoundInclusive()
    {
        return false;
    }

    public  boolean isSupportConnected()
    {
        return true;
    }

}
