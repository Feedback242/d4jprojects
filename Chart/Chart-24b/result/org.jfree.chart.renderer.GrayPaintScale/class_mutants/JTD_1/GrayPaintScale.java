// This is a mutant program.
// Author : ysma

package org.jfree.chart.renderer;


import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;


public class GrayPaintScale implements org.jfree.chart.renderer.PaintScale, org.jfree.chart.util.PublicCloneable, java.io.Serializable
{

    private double lowerBound;

    private double upperBound;

    public GrayPaintScale()
    {
        this( 0.0, 1.0 );
    }

    public GrayPaintScale( double lowerBound, double upperBound )
    {
        if (lowerBound >= upperBound) {
            throw new java.lang.IllegalArgumentException( "Requires lowerBound < upperBound." );
        }
        lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public  double getLowerBound()
    {
        return this.lowerBound;
    }

    public  double getUpperBound()
    {
        return this.upperBound;
    }

    public  java.awt.Paint getPaint( double value )
    {
        double v = Math.max( value, this.lowerBound );
        v = Math.min( v, this.upperBound );
        int g = (int) ((value - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0);
        return new java.awt.Color( g, g, g );
    }

    public  boolean equals( java.lang.Object obj )
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.jfree.chart.renderer.GrayPaintScale)) {
            return false;
        }
        org.jfree.chart.renderer.GrayPaintScale that = (org.jfree.chart.renderer.GrayPaintScale) obj;
        if (this.lowerBound != that.lowerBound) {
            return false;
        }
        if (this.upperBound != that.upperBound) {
            return false;
        }
        return true;
    }

    public  java.lang.Object clone()
        throws java.lang.CloneNotSupportedException
    {
        return super.clone();
    }

}
