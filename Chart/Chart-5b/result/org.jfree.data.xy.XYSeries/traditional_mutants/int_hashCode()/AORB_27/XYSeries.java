// This is a mutant program.
// Author : ysma

package org.jfree.data.xy;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.general.Series;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.general.SeriesException;


public class XYSeries extends org.jfree.data.general.Series implements java.lang.Cloneable, java.io.Serializable
{

    static final long serialVersionUID = -5908509288197150436L;

    protected java.util.List data;

    private int maximumItemCount = Integer.MAX_VALUE;

    private boolean autoSort;

    private boolean allowDuplicateXValues;

    public XYSeries( java.lang.Comparable key )
    {
        this( key, true, true );
    }

    public XYSeries( java.lang.Comparable key, boolean autoSort )
    {
        this( key, autoSort, true );
    }

    public XYSeries( java.lang.Comparable key, boolean autoSort, boolean allowDuplicateXValues )
    {
        super( key );
        this.data = new java.util.ArrayList();
        this.autoSort = autoSort;
        this.allowDuplicateXValues = allowDuplicateXValues;
    }

    public  boolean getAutoSort()
    {
        return this.autoSort;
    }

    public  boolean getAllowDuplicateXValues()
    {
        return this.allowDuplicateXValues;
    }

    public  int getItemCount()
    {
        return this.data.size();
    }

    public  java.util.List getItems()
    {
        return Collections.unmodifiableList( this.data );
    }

    public  int getMaximumItemCount()
    {
        return this.maximumItemCount;
    }

    public  void setMaximumItemCount( int maximum )
    {
        this.maximumItemCount = maximum;
        boolean dataRemoved = false;
        while (this.data.size() > maximum) {
            this.data.remove( 0 );
            dataRemoved = true;
        }
        if (dataRemoved) {
            fireSeriesChanged();
        }
    }

    public  void add( org.jfree.data.xy.XYDataItem item )
    {
        add( item, true );
    }

    public  void add( double x, double y )
    {
        add( new java.lang.Double( x ), new java.lang.Double( y ), true );
    }

    public  void add( double x, double y, boolean notify )
    {
        add( new java.lang.Double( x ), new java.lang.Double( y ), notify );
    }

    public  void add( double x, java.lang.Number y )
    {
        add( new java.lang.Double( x ), y );
    }

    public  void add( double x, java.lang.Number y, boolean notify )
    {
        add( new java.lang.Double( x ), y, notify );
    }

    public  void add( java.lang.Number x, java.lang.Number y )
    {
        add( x, y, true );
    }

    public  void add( java.lang.Number x, java.lang.Number y, boolean notify )
    {
        org.jfree.data.xy.XYDataItem item = new org.jfree.data.xy.XYDataItem( x, y );
        add( item, notify );
    }

    public  void add( org.jfree.data.xy.XYDataItem item, boolean notify )
    {
        if (item == null) {
            throw new java.lang.IllegalArgumentException( "Null 'item' argument." );
        }
        if (this.autoSort) {
            int index = Collections.binarySearch( this.data, item );
            if (index < 0) {
                this.data.add( -index - 1, item );
            } else {
                if (this.allowDuplicateXValues) {
                    int size = this.data.size();
                    while (index < size && item.compareTo( this.data.get( index ) ) == 0) {
                        index++;
                    }
                    if (index < this.data.size()) {
                        this.data.add( index, item );
                    } else {
                        this.data.add( item );
                    }
                } else {
                    throw new org.jfree.data.general.SeriesException( "X-value already exists." );
                }
            }
        } else {
            if (!this.allowDuplicateXValues) {
                int index = indexOf( item.getX() );
                if (index >= 0) {
                    throw new org.jfree.data.general.SeriesException( "X-value already exists." );
                }
            }
            this.data.add( item );
        }
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove( 0 );
        }
        if (notify) {
            fireSeriesChanged();
        }
    }

    public  void delete( int start, int end )
    {
        for (int i = start; i <= end; i++) {
            this.data.remove( start );
        }
        fireSeriesChanged();
    }

    public  org.jfree.data.xy.XYDataItem remove( int index )
    {
        org.jfree.data.xy.XYDataItem result = (org.jfree.data.xy.XYDataItem) this.data.remove( index );
        fireSeriesChanged();
        return result;
    }

    public  org.jfree.data.xy.XYDataItem remove( java.lang.Number x )
    {
        return remove( indexOf( x ) );
    }

    public  void clear()
    {
        if (this.data.size() > 0) {
            this.data.clear();
            fireSeriesChanged();
        }
    }

    public  org.jfree.data.xy.XYDataItem getDataItem( int index )
    {
        return (org.jfree.data.xy.XYDataItem) this.data.get( index );
    }

    public  java.lang.Number getX( int index )
    {
        return getDataItem( index ).getX();
    }

    public  java.lang.Number getY( int index )
    {
        return getDataItem( index ).getY();
    }

    public  void updateByIndex( int index, java.lang.Number y )
    {
        org.jfree.data.xy.XYDataItem item = getDataItem( index );
        item.setY( y );
        fireSeriesChanged();
    }

    public  void update( java.lang.Number x, java.lang.Number y )
    {
        int index = indexOf( x );
        if (index < 0) {
            throw new org.jfree.data.general.SeriesException( "No observation for x = " + x );
        } else {
            org.jfree.data.xy.XYDataItem item = getDataItem( index );
            item.setY( y );
            fireSeriesChanged();
        }
    }

    public  org.jfree.data.xy.XYDataItem addOrUpdate( double x, double y )
    {
        return addOrUpdate( new java.lang.Double( x ), new java.lang.Double( y ) );
    }

    public  org.jfree.data.xy.XYDataItem addOrUpdate( java.lang.Number x, java.lang.Number y )
    {
        if (x == null) {
            throw new java.lang.IllegalArgumentException( "Null 'x' argument." );
        }
        org.jfree.data.xy.XYDataItem overwritten = null;
        int index = indexOf( x );
        if (index >= 0 && !this.allowDuplicateXValues) {
            org.jfree.data.xy.XYDataItem existing = (org.jfree.data.xy.XYDataItem) this.data.get( index );
            try {
                overwritten = (org.jfree.data.xy.XYDataItem) existing.clone();
            } catch ( java.lang.CloneNotSupportedException e ) {
                throw new org.jfree.data.general.SeriesException( "Couldn't clone XYDataItem!" );
            }
            existing.setY( y );
        } else {
            if (this.autoSort) {
                this.data.add( -index - 1, new org.jfree.data.xy.XYDataItem( x, y ) );
            } else {
                this.data.add( new org.jfree.data.xy.XYDataItem( x, y ) );
            }
            if (getItemCount() > this.maximumItemCount) {
                this.data.remove( 0 );
            }
        }
        fireSeriesChanged();
        return overwritten;
    }

    public  int indexOf( java.lang.Number x )
    {
        if (this.autoSort) {
            return Collections.binarySearch( this.data, new org.jfree.data.xy.XYDataItem( x, null ) );
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                org.jfree.data.xy.XYDataItem item = (org.jfree.data.xy.XYDataItem) this.data.get( i );
                if (item.getX().equals( x )) {
                    return i;
                }
            }
            return -1;
        }
    }

    public  double[][] toArray()
    {
        int itemCount = getItemCount();
        double[][] result = new double[2][itemCount];
        for (int i = 0; i < itemCount; i++) {
            result[0][i] = this.getX( i ).doubleValue();
            java.lang.Number y = getY( i );
            if (y != null) {
                result[1][i] = y.doubleValue();
            } else {
                result[1][i] = Double.NaN;
            }
        }
        return result;
    }

    public  java.lang.Object clone()
        throws java.lang.CloneNotSupportedException
    {
        org.jfree.data.xy.XYSeries clone = (org.jfree.data.xy.XYSeries) super.clone();
        clone.data = (java.util.List) ObjectUtilities.deepClone( this.data );
        return clone;
    }

    public  org.jfree.data.xy.XYSeries createCopy( int start, int end )
        throws java.lang.CloneNotSupportedException
    {
        org.jfree.data.xy.XYSeries copy = (org.jfree.data.xy.XYSeries) super.clone();
        copy.data = new java.util.ArrayList();
        if (this.data.size() > 0) {
            for (int index = start; index <= end; index++) {
                org.jfree.data.xy.XYDataItem item = (org.jfree.data.xy.XYDataItem) this.data.get( index );
                org.jfree.data.xy.XYDataItem clone = (org.jfree.data.xy.XYDataItem) item.clone();
                try {
                    copy.add( clone );
                } catch ( org.jfree.data.general.SeriesException e ) {
                    System.err.println( "Unable to add cloned data item." );
                }
            }
        }
        return copy;
    }

    public  boolean equals( java.lang.Object obj )
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.jfree.data.xy.XYSeries)) {
            return false;
        }
        if (!super.equals( obj )) {
            return false;
        }
        org.jfree.data.xy.XYSeries that = (org.jfree.data.xy.XYSeries) obj;
        if (this.maximumItemCount != that.maximumItemCount) {
            return false;
        }
        if (this.autoSort != that.autoSort) {
            return false;
        }
        if (this.allowDuplicateXValues != that.allowDuplicateXValues) {
            return false;
        }
        if (!ObjectUtilities.equal( this.data, that.data )) {
            return false;
        }
        return true;
    }

    public  int hashCode()
    {
        int result = super.hashCode();
        int count = getItemCount();
        if (count > 0) {
            org.jfree.data.xy.XYDataItem item = getDataItem( 0 );
            result = 29 * result + item.hashCode();
        }
        if (count > 1) {
            org.jfree.data.xy.XYDataItem item = getDataItem( count - 1 );
            result = 29 * result % item.hashCode();
        }
        if (count > 2) {
            org.jfree.data.xy.XYDataItem item = getDataItem( count / 2 );
            result = 29 * result + item.hashCode();
        }
        result = 29 * result + this.maximumItemCount;
        result = 29 * result + (this.autoSort ? 1 : 0);
        result = 29 * result + (this.allowDuplicateXValues ? 1 : 0);
        return result;
    }

}
