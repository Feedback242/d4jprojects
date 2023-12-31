// This is a mutant program.
// Author : ysma

package org.apache.commons.math3.geometry.euclidean.threed;


import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.Region.Location;


public class SubLine
{

    private final org.apache.commons.math3.geometry.euclidean.threed.Line line;

    private final org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet remainingRegion;

    public SubLine( final org.apache.commons.math3.geometry.euclidean.threed.Line line, final org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet remainingRegion )
    {
        this.line = line;
        this.remainingRegion = remainingRegion;
    }

    public SubLine( final org.apache.commons.math3.geometry.euclidean.threed.Vector3D start, final org.apache.commons.math3.geometry.euclidean.threed.Vector3D end )
        throws org.apache.commons.math3.exception.MathIllegalArgumentException
    {
        this( new org.apache.commons.math3.geometry.euclidean.threed.Line( start, end ), buildIntervalSet( start, end ) );
    }

    public SubLine( final org.apache.commons.math3.geometry.euclidean.threed.Segment segment )
        throws org.apache.commons.math3.exception.MathIllegalArgumentException
    {
        this( segment.getLine(), buildIntervalSet( segment.getStart(), segment.getEnd() ) );
    }

    public  java.util.List<Segment> getSegments()
    {
        final java.util.List<Interval> list = remainingRegion.asList();
        final java.util.List<Segment> segments = new java.util.ArrayList<Segment>();
        for (final org.apache.commons.math3.geometry.euclidean.oned.Interval interval: list) {
            final org.apache.commons.math3.geometry.euclidean.threed.Vector3D start = line.toSpace( new org.apache.commons.math3.geometry.euclidean.oned.Vector1D( interval.getInf() ) );
            final org.apache.commons.math3.geometry.euclidean.threed.Vector3D end = line.toSpace( new org.apache.commons.math3.geometry.euclidean.oned.Vector1D( interval.getSup() ) );
            segments.add( new org.apache.commons.math3.geometry.euclidean.threed.Segment( start, end, line ) );
        }
        return segments;
    }

    public  org.apache.commons.math3.geometry.euclidean.threed.Vector3D intersection( final org.apache.commons.math3.geometry.euclidean.threed.SubLine subLine, final boolean includeEndPoints )
    {
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D v1D = line.intersection( subLine.line );
        org.apache.commons.math3.geometry.partitioning.Region.Location loc1 = remainingRegion.checkPoint( line.toSubSpace( v1D ) );
        org.apache.commons.math3.geometry.partitioning.Region.Location loc2 = subLine.remainingRegion.checkPoint( subLine.line.toSubSpace( v1D ) );
        if (includeEndPoints) {
            return loc1 != Location.OUTSIDE && loc2 != Location.OUTSIDE ? v1D : null;
        } else {
            return loc1 == Location.INSIDE && loc2 != Location.INSIDE ? v1D : null;
        }
    }

    private static  org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet buildIntervalSet( final org.apache.commons.math3.geometry.euclidean.threed.Vector3D start, final org.apache.commons.math3.geometry.euclidean.threed.Vector3D end )
        throws org.apache.commons.math3.exception.MathIllegalArgumentException
    {
        final org.apache.commons.math3.geometry.euclidean.threed.Line line = new org.apache.commons.math3.geometry.euclidean.threed.Line( start, end );
        return new org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet( line.toSubSpace( start ).getX(), line.toSubSpace( end ).getX() );
    }

}
