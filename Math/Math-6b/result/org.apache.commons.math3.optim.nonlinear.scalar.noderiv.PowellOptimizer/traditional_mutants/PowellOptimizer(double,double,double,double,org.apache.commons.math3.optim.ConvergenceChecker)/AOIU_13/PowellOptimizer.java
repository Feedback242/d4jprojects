// This is a mutant program.
// Author : ysma

package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;


import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.SearchInterval;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;


public class PowellOptimizer extends org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer
{

    private static final double MIN_RELATIVE_TOLERANCE = 2 * FastMath.ulp( 1d );

    private final double relativeThreshold;

    private final double absoluteThreshold;

    private final org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PowellOptimizer.LineSearch line;

    public PowellOptimizer( double rel, double abs, org.apache.commons.math3.optim.ConvergenceChecker<PointValuePair> checker )
    {
        this( rel, abs, FastMath.sqrt( rel ), FastMath.sqrt( abs ), checker );
    }

    public PowellOptimizer( double rel, double abs, double lineRel, double lineAbs, org.apache.commons.math3.optim.ConvergenceChecker<PointValuePair> checker )
    {
        super( checker );
        if (rel < MIN_RELATIVE_TOLERANCE) {
            throw new org.apache.commons.math3.exception.NumberIsTooSmallException( rel, MIN_RELATIVE_TOLERANCE, true );
        }
        if (abs <= 0) {
            throw new org.apache.commons.math3.exception.NotStrictlyPositiveException( abs );
        }
        relativeThreshold = rel;
        absoluteThreshold = abs;
        line = new org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PowellOptimizer.LineSearch( -lineRel, lineAbs );
    }

    public PowellOptimizer( double rel, double abs )
    {
        this( rel, abs, null );
    }

    public PowellOptimizer( double rel, double abs, double lineRel, double lineAbs )
    {
        this( rel, abs, lineRel, lineAbs, null );
    }

    protected  org.apache.commons.math3.optim.PointValuePair doOptimize()
    {
        checkParameters();
        final org.apache.commons.math3.optim.nonlinear.scalar.GoalType goal = getGoalType();
        final double[] guess = getStartPoint();
        final int n = guess.length;
        final double[][] direc = new double[n][n];
        for (int i = 0; i < n; i++) {
            direc[i][i] = 1;
        }
        final org.apache.commons.math3.optim.ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        double[] x = guess;
        double fVal = computeObjectiveValue( x );
        double[] x1 = x.clone();
        int iter = 0;
        while (true) {
            ++iter;
            double fX = fVal;
            double fX2 = 0;
            double delta = 0;
            int bigInd = 0;
            double alphaMin = 0;
            for (int i = 0; i < n; i++) {
                final double[] d = MathArrays.copyOf( direc[i] );
                fX2 = fVal;
                final org.apache.commons.math3.optim.univariate.UnivariatePointValuePair optimum = line.search( x, d );
                fVal = optimum.getValue();
                alphaMin = optimum.getPoint();
                final double[][] result = newPointAndDirection( x, d, alphaMin );
                x = result[0];
                if (fX2 - fVal > delta) {
                    delta = fX2 - fVal;
                    bigInd = i;
                }
            }
            boolean stop = 2 * (fX - fVal) <= relativeThreshold * (FastMath.abs( fX ) + FastMath.abs( fVal )) + absoluteThreshold;
            final org.apache.commons.math3.optim.PointValuePair previous = new org.apache.commons.math3.optim.PointValuePair( x1, fX );
            final org.apache.commons.math3.optim.PointValuePair current = new org.apache.commons.math3.optim.PointValuePair( x, fVal );
            if (!stop) {
                if (checker != null) {
                    stop = checker.converged( iter, previous, current );
                }
            }
            if (stop) {
                if (goal == GoalType.MINIMIZE) {
                    return fVal < fX ? current : previous;
                } else {
                    return fVal > fX ? current : previous;
                }
            }
            final double[] d = new double[n];
            final double[] x2 = new double[n];
            for (int i = 0; i < n; i++) {
                d[i] = x[i] - x1[i];
                x2[i] = 2 * x[i] - x1[i];
            }
            x1 = x.clone();
            fX2 = computeObjectiveValue( x2 );
            if (fX > fX2) {
                double t = 2 * (fX + fX2 - 2 * fVal);
                double temp = fX - fVal - delta;
                t *= temp * temp;
                temp = fX - fX2;
                t -= delta * temp * temp;
                if (t < 0.0) {
                    final org.apache.commons.math3.optim.univariate.UnivariatePointValuePair optimum = line.search( x, d );
                    fVal = optimum.getValue();
                    alphaMin = optimum.getPoint();
                    final double[][] result = newPointAndDirection( x, d, alphaMin );
                    x = result[0];
                    final int lastInd = n - 1;
                    direc[bigInd] = direc[lastInd];
                    direc[lastInd] = result[1];
                }
            }
        }
    }

    private  double[][] newPointAndDirection( double[] p, double[] d, double optimum )
    {
        final int n = p.length;
        final double[] nP = new double[n];
        final double[] nD = new double[n];
        for (int i = 0; i < n; i++) {
            nD[i] = d[i] * optimum;
            nP[i] = p[i] + nD[i];
        }
        final double[][] result = new double[2][];
        result[0] = nP;
        result[1] = nD;
        return result;
    }

    private class LineSearch extends org.apache.commons.math3.optim.univariate.BrentOptimizer
    {

        private static final double REL_TOL_UNUSED = 1e-15;

        private static final double ABS_TOL_UNUSED = Double.MIN_VALUE;

        private final org.apache.commons.math3.optim.univariate.BracketFinder bracket = new org.apache.commons.math3.optim.univariate.BracketFinder();

        LineSearch( double rel, double abs )
        {
            super( REL_TOL_UNUSED, ABS_TOL_UNUSED, new org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker( rel, abs ) );
        }

        public  org.apache.commons.math3.optim.univariate.UnivariatePointValuePair search( final double[] p, final double[] d )
        {
            final int n = p.length;
            final org.apache.commons.math3.analysis.UnivariateFunction f = new org.apache.commons.math3.analysis.UnivariateFunction(){
                public  double value( double alpha )
                {
                    final double[] x = new double[n];
                    for (int i = 0; i < n; i++) {
                        x[i] = p[i] + alpha * d[i];
                    }
                    final double obj = PowellOptimizer.this.computeObjectiveValue( x );
                    return obj;
                }
            };
            final org.apache.commons.math3.optim.nonlinear.scalar.GoalType goal = PowellOptimizer.this.getGoalType();
            bracket.search( f, goal, 0, 1 );
            return optimize( new org.apache.commons.math3.optim.MaxEval( Integer.MAX_VALUE ), new org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction( f ), goal, new org.apache.commons.math3.optim.univariate.SearchInterval( bracket.getLo(), bracket.getHi(), bracket.getMid() ) );
        }

    }

    private  void checkParameters()
    {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new org.apache.commons.math3.exception.MathUnsupportedOperationException( LocalizedFormats.CONSTRAINT );
        }
    }

}
