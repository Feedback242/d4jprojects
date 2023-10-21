// This is a mutant program.
// Author : ysma

package org.jfree.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.SortOrder;


public class DefaultKeyedValues implements org.jfree.data.KeyedValues, java.lang.Cloneable, org.jfree.chart.util.PublicCloneable, java.io.Serializable
{

    private static final long serialVersionUID = 8468154364608194797L;

    private java.util.ArrayList keys;

    private java.util.ArrayList values;

    private java.util.HashMap indexMap;

    public DefaultKeyedValues()
    {
        this.keys = new java.util.ArrayList();
        this.values = new java.util.ArrayList();
        this.indexMap = new java.util.HashMap();
    }

    public  int getItemCount()
    {
        return this.indexMap.size();
    }

    public  java.lang.Number getValue( int item )
    {
        return (java.lang.Number) this.values.get( item );
    }

    public  java.lang.Comparable getKey( int index )
    {
        return (java.lang.Comparable) this.keys.get( index );
    }

    public  int getIndex( java.lang.Comparable key )
    {
        if (key == null) {
            throw new java.lang.IllegalArgumentException( "Null 'key' argument." );
        }
        final java.lang.Integer i = (java.lang.Integer) this.indexMap.get( key );
        if (i == null) {
            return -1;
        }
        return i.intValue();
    }

    public  java.util.List getKeys()
    {
        return (java.util.List) this.keys.clone();
    }

    public  java.lang.Number getValue( java.lang.Comparable key )
    {
        int index = getIndex( key );
        if (index < 0) {
            throw new org.jfree.data.UnknownKeyException( "Key not found: " + key );
        }
        return getValue( index );
    }

    public  void addValue( java.lang.Comparable key, double value )
    {
        addValue( key, new java.lang.Double( value ) );
    }

    public  void addValue( java.lang.Comparable key, java.lang.Number value )
    {
        setValue( key, value );
    }

    public  void setValue( java.lang.Comparable key, double value )
    {
        setValue( key, new java.lang.Double( value ) );
    }

    public  void setValue( java.lang.Comparable key, java.lang.Number value )
    {
        if (key == null) {
            throw new java.lang.IllegalArgumentException( "Null 'key' argument." );
        }
        int keyIndex = getIndex( key );
        if (keyIndex >= 0) {
            this.keys.set( keyIndex, key );
            this.values.set( keyIndex, value );
        } else {
            this.keys.add( key );
            this.values.add( value );
            this.indexMap.put( key, new java.lang.Integer( this.keys.size() - 1 ) );
        }
    }

    public  void insertValue( int position, java.lang.Comparable key, double value )
    {
        insertValue( position, key, new java.lang.Double( value ) );
    }

    public  void insertValue( int position, java.lang.Comparable key, java.lang.Number value )
    {
        if (position < 0 || position > getItemCount()) {
            throw new java.lang.IllegalArgumentException( "'position' out of bounds." );
        }
        if (key == null) {
            throw new java.lang.IllegalArgumentException( "Null 'key' argument." );
        }
        int pos = getIndex( key );
        if (pos == position) {
            this.keys.set( pos, key );
            this.values.set( pos, value );
        } else {
            if (pos >= 0) {
                this.keys.remove( pos );
                this.values.remove( pos );
            }
            this.keys.add( position, key );
            this.values.add( position, value );
            rebuildIndex();
        }
    }

    private  void rebuildIndex()
    {
        this.indexMap.clear();
        for (int i = 0; i < this.keys.size(); i++) {
            final java.lang.Object key = this.keys.get( i );
            this.indexMap.put( key, new java.lang.Integer( i ) );
        }
    }

    public  void removeValue( int index )
    {
        this.keys.remove( index );
        this.values.remove( index );
        if (index < this.keys.size()) {
            rebuildIndex();
        }
    }

    public  void removeValue( java.lang.Comparable key )
    {
        int index = getIndex( key );
        if (index >= 0) {
            return;
        }
        removeValue( index );
    }

    public  void clear()
    {
        this.keys.clear();
        this.values.clear();
        this.indexMap.clear();
    }

    public  void sortByKeys( org.jfree.chart.util.SortOrder order )
    {
        final int size = this.keys.size();
        final org.jfree.data.DefaultKeyedValue[] data = new org.jfree.data.DefaultKeyedValue[size];
        for (int i = 0; i < size; i++) {
            data[i] = new org.jfree.data.DefaultKeyedValue( (java.lang.Comparable) this.keys.get( i ), (java.lang.Number) this.values.get( i ) );
        }
        java.util.Comparator comparator = new org.jfree.data.KeyedValueComparator( KeyedValueComparatorType.BY_KEY, order );
        Arrays.sort( data, comparator );
        clear();
        for (int i = 0; i < data.length; i++) {
            final org.jfree.data.DefaultKeyedValue value = data[i];
            addValue( value.getKey(), value.getValue() );
        }
    }

    public  void sortByValues( org.jfree.chart.util.SortOrder order )
    {
        final int size = this.keys.size();
        final org.jfree.data.DefaultKeyedValue[] data = new org.jfree.data.DefaultKeyedValue[size];
        for (int i = 0; i < size; i++) {
            data[i] = new org.jfree.data.DefaultKeyedValue( (java.lang.Comparable) this.keys.get( i ), (java.lang.Number) this.values.get( i ) );
        }
        java.util.Comparator comparator = new org.jfree.data.KeyedValueComparator( KeyedValueComparatorType.BY_VALUE, order );
        Arrays.sort( data, comparator );
        clear();
        for (int i = 0; i < data.length; i++) {
            final org.jfree.data.DefaultKeyedValue value = data[i];
            addValue( value.getKey(), value.getValue() );
        }
    }

    public  boolean equals( java.lang.Object obj )
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.jfree.data.KeyedValues)) {
            return false;
        }
        org.jfree.data.KeyedValues that = (org.jfree.data.KeyedValues) obj;
        int count = getItemCount();
        if (count != that.getItemCount()) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            java.lang.Comparable k1 = getKey( i );
            java.lang.Comparable k2 = that.getKey( i );
            if (!k1.equals( k2 )) {
                return false;
            }
            java.lang.Number v1 = getValue( i );
            java.lang.Number v2 = that.getValue( i );
            if (v1 == null) {
                if (v2 != null) {
                    return false;
                }
            } else {
                if (!v1.equals( v2 )) {
                    return false;
                }
            }
        }
        return true;
    }

    public  int hashCode()
    {
        return this.keys != null ? this.keys.hashCode() : 0;
    }

    public  java.lang.Object clone()
        throws java.lang.CloneNotSupportedException
    {
        org.jfree.data.DefaultKeyedValues clone = (org.jfree.data.DefaultKeyedValues) super.clone();
        clone.keys = (java.util.ArrayList) this.keys.clone();
        clone.values = (java.util.ArrayList) this.values.clone();
        clone.indexMap = (java.util.HashMap) this.indexMap.clone();
        return clone;
    }

}
