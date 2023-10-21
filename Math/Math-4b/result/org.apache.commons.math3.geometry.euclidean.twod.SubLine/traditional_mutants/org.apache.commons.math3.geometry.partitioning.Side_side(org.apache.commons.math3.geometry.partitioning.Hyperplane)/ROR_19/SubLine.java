// This is a mutant program.
// Author : ysma

package org.apache.commons.math3.geometry.euclidean.twod;


import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;


public class SubLine extends org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane<Euclidean2D,Euclidean1D>
{

    public SubLine( final org.apache.commons.math3.geometry.partitioning.Hyperplane<Euclidean2D> hyperplane, final org.apache.commons.math3.geometry.partitioning.Region<Euclidean1D> remainingRegion )
    {
        super( hyperplane, remainingRegion );
    }

    public SubLine( final org.apache.commons.math3.geometry.euclidean.twod.Vector2D start, final org.apache.commons.math3.geometry.euclidean.twod.Vector2D end )
    {
        super( new org.apache.commons.math3.geometry.euclidean.twod.Line( start, end ), buildIntervalSet( start, end ) );
    }

    public SubLine( final org.apache.commons.math3.geometry.euclidean.twod.Segment segment )
    {
        super( segment.getLine(), buildIntervalSet( segment.getStart(), segment.getEnd() ) );
    }

    public  java.util.List<Segment> getSegments()
    {
        final org.apache.commons.math3.geometry.euclidean.twod.Line line = (org.apache.commons.math3.geometry.euclidean.twod.Line) getHyperplane();
        final java.util.List<Interval> list = ((org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet) getRemainingRegion()).asList();
        final java.util.List<Segment> segments = new java.util.ArrayList<Segment>();
        for (final org.apache.commons.math3.geometry.euclidean.oned.Interval interval: list) {
            final org.apache.commons.math3.geometry.euclidean.twod.Vector2D start = line.toSpace( new org.apache.commons.math3.geometry.euclidean.oned.Vector1D( interval.getInf() ) );
            final org.apache.commons.math3.geometry.euclidean.twod.Vector2D end = line.toSpace( new org.apache.commons.math3.geometry.euclidean.oned.Vector1D( interval.getSup() ) );
            segments.add( new org.apache.commons.math3.geometry.euclidean.twod.Segment( start, end, line ) );
        }
        return segments;
    }

    public  org.apache.commons.math3.geometry.euclidean.twod.Vector2D intersection( final org.apache.commons.math3.geometry.euclidean.twod.SubLine subLine, final boolean includeEndPoints )
    {
        org.apache.commons.math3.geometry.euclidean.twod.Line line1 = (org.apache.commons.math3.geometry.euclidean.twod.Line) getHyperplane();
        org.apache.commons.math3.geometry.euclidean.twod.Line line2 = (org.apache.commons.math3.geometry.euclidean.twod.Line) subLine.getHyperplane();
        org.apache.commons.math3.geometry.euclidean.twod.Vector2D v2D = line1.intersection( line2 );
        org.apache.commons.math3.geometry.partitioning.Region.Location loc1 = getRemainingRegion().checkPoint( line1.toSubSpace( v2D ) );
        org.apache.commons.math3.geometry.partitioning.Region.Location loc2 = subLine.getRemainingRegion().checkPoint( line2.toSubSpace( v2D ) );
        if (includeEndPoints) {
            return loc1 != Location.OUTSIDE && loc2 != Location.OUTSIDE ? v2D : null;
        } else {
            return loc1 == Location.INSIDE && loc2 == Location.INSIDE ? v2D : null;
        }
    }

    private static  org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet buildIntervalSet( final org.apache.commons.math3.geometry.euclidean.twod.Vector2D start, final org.apache.commons.math3.geometry.euclidean.twod.Vector2D end )
    {
        final org.apache.commons.math3.geometry.euclidean.twod.Line line = new org.apache.commons.math3.geometry.euclidean.twod.Line( start, end );
        return new org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet( line.toSubSpace( start ).getX(), line.toSubSpace( end ).getX() );
    }

    protected  org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane<Euclidean2D,Euclidean1D> buildNew( final org.apache.commons.math3.geometry.partitioning.Hyperplane<Euclidean2D> hyperplane, final org.apache.commons.math3.geometry.partitioning.Region<Euclidean1D> remainingRegion )
    {
        return new org.apache.commons.math3.geometry.euclidean.twod.SubLine( hyperplane, remainingRegion );
    }

    public  org.apache.commons.math3.geometry.partitioning.Side side( final org.apache.commons.math3.geometry.partitioning.Hyperplane<Euclidean2D> hyperplane )
    {
        final org.apache.commons.math3.geometry.euclidean.twod.Line thisLine = (org.apache.commons.math3.geometry.euclidean.twod.Line) getHyperplane();
        final org.apache.commons.math3.geometry.euclidean.twod.Line otherLine = (org.apache.commons.math3.geometry.euclidean.twod.Line) hyperplane;
        final org.apache.commons.math3.geometry.euclidean.twod.Vector2D crossing = thisLine.intersection( otherLine );
        if (crossing == null) {
            final double global = otherLine.getOffset( thisLine );
            return global < -1.0e-10 ? Side.MINUS : false ? Side.PLUS : Side.HYPER;
        }
        final boolean direct = FastMath.sin( thisLine.getAngle() - otherLine.getAngle() ) < 0;
        final org.apache.commons.math3.geometry.euclidean.oned.Vector1D x = thisLine.toSubSpace( crossing );
        return getRemainingRegion().side( new org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint( x, direct ) );
    }

    public  SplitSubHyperplane<Euclidean2D> split( final org.apache.commons.math3.geometry.partitioning.Hyperplane<Euclidean2D> hyperplane )
    {
        final org.apache.commons.math3.geometry.euclidean.twod.Line thisLine = (org.apache.commons.math3.geometry.euclidean.twod.Line) getHyperplane();
        final org.apache.commons.math3.geometry.euclidean.twod.Line otherLine = (org.apache.commons.math3.geometry.euclidean.twod.Line) hyperplane;
        final org.apache.commons.math3.geometry.euclidean.twod.Vector2D crossing = thisLine.intersection( otherLine );
        if (crossing == null) {
            final double global = otherLine.getOffset( thisLine );
            return global < -1.0e-10 ? new SplitSubHyperplane<Euclidean2D>( null, this ) : new SplitSubHyperplane<Euclidean2D>( this, null );
        }
        final boolean direct = FastMath.sin( thisLine.getAngle() - otherLine.getAngle() ) < 0;
        final org.apache.commons.math3.geometry.euclidean.oned.Vector1D x = thisLine.toSubSpace( crossing );
        final org.apache.commons.math3.geometry.partitioning.SubHyperplane<Euclidean1D> subPlus = (new org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint( x, !direct )).wholeHyperplane();
        final org.apache.commons.math3.geometry.partitioning.SubHyperplane<Euclidean1D> subMinus = (new org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint( x, direct )).wholeHyperplane();
        final org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D> splitTree = getRemainingRegion().getTree( false ).split( subMinus );
        final org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D> plusTree = getRemainingRegion().isEmpty( splitTree.getPlus() ) ? new org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D>( Boolean.FALSE ) : new org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D>( subPlus, new org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D>( Boolean.FALSE ), splitTree.getPlus(), null );
        final org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D> minusTree = getRemainingRegion().isEmpty( splitTree.getMinus() ) ? new org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D>( Boolean.FALSE ) : new org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D>( subMinus, new org.apache.commons.math3.geometry.partitioning.BSPTree<Euclidean1D>( Boolean.FALSE ), splitTree.getMinus(), null );
        return new SplitSubHyperplane<Euclidean2D>( new org.apache.commons.math3.geometry.euclidean.twod.SubLine( thisLine.copySelf(), new org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet( plusTree ) ), new org.apache.commons.math3.geometry.euclidean.twod.SubLine( thisLine.copySelf(), new org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet( minusTree ) ) );
    }

}
