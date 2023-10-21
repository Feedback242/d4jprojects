// This is a mutant program.
// Author : ysma

package org.jfree.data.category;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import org.jfree.data.DataUtilities;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.general.AbstractSeriesDataset;


public class DefaultIntervalCategoryDataset extends org.jfree.data.general.AbstractSeriesDataset implements org.jfree.data.category.IntervalCategoryDataset
{

    private java.lang.Comparable[] seriesKeys;

    private java.lang.Comparable[] categoryKeys;

    private java.lang.Number[][] startData;

    private java.lang.Number[][] endData;

    public DefaultIntervalCategoryDataset( double[][] starts, double[][] ends )
    {
        this( DataUtilities.createNumberArray2D( starts ), DataUtilities.createNumberArray2D( ends ) );
    }

    public DefaultIntervalCategoryDataset( java.lang.Number[][] starts, java.lang.Number[][] ends )
    {
        this( null, null, starts, ends );
    }

    public DefaultIntervalCategoryDataset( java.lang.String[] seriesNames, java.lang.Number[][] starts, java.lang.Number[][] ends )
    {
        this( seriesNames, null, starts, ends );
    }

    public DefaultIntervalCategoryDataset( java.lang.Comparable[] seriesKeys, java.lang.Comparable[] categoryKeys, java.lang.Number[][] starts, java.lang.Number[][] ends )
    {
        this.startData = starts;
        this.endData = ends;
        if (starts != null && ends != null) {
            java.lang.String baseName = "org.jfree.data.resources.DataPackageResources";
            java.util.ResourceBundle resources = ResourceBundle.getBundle( baseName );
            int seriesCount = starts.length;
            if (seriesCount != ends.length) {
                java.lang.String errMsg = "DefaultIntervalCategoryDataset: the number " + "of series in the start value dataset does " + "not match the number of series in the end " + "value dataset.";
                throw new java.lang.IllegalArgumentException( errMsg );
            }
            if (seriesCount > 0) {
                if (seriesKeys != null) {
                    if (seriesKeys.length != seriesCount) {
                        throw new java.lang.IllegalArgumentException( "The number of series keys does not " + "match the number of series in the data." );
                    }
                    this.seriesKeys = seriesKeys;
                } else {
                    java.lang.String prefix = resources.getString( "series.default-prefix" ) + " ";
                    this.seriesKeys = generateKeys( seriesCount, prefix );
                }
                int categoryCount = starts[0].length;
                if (categoryCount != ends[0].length) {
                    java.lang.String errMsg = "DefaultIntervalCategoryDataset: the " + "number of categories in the start value " + "dataset does not match the number of " + "categories in the end value dataset.";
                    throw new java.lang.IllegalArgumentException( errMsg );
                }
                if (categoryKeys != null) {
                    if (categoryKeys.length != categoryCount) {
                        throw new java.lang.IllegalArgumentException( "The number of category keys does not match " + "the number of categories in the data." );
                    }
                    this.categoryKeys = categoryKeys;
                } else {
                    java.lang.String prefix = resources.getString( "categories.default-prefix" ) + " ";
                    this.categoryKeys = generateKeys( categoryCount, prefix );
                }
            } else {
                this.seriesKeys = null;
                this.categoryKeys = null;
            }
        }
    }

    public  int getSeriesCount()
    {
        int result = 0;
        if (this.startData != null) {
            result = this.startData.length;
        }
        return result;
    }

    public  int getSeriesIndex( java.lang.Comparable seriesKey )
    {
        int result = -1;
        for (int i = 0; i < this.seriesKeys.length; i++) {
            if (seriesKey.equals( this.seriesKeys[i] )) {
                result = i;
                break;
            }
        }
        return result;
    }

    public  java.lang.Comparable getSeriesKey( int series )
    {
        if (series >= getSeriesCount() || series < 0) {
            throw new java.lang.IllegalArgumentException( "No such series : " + series );
        }
        return this.seriesKeys[series];
    }

    public  void setSeriesKeys( java.lang.Comparable[] seriesKeys )
    {
        if (seriesKeys == null) {
            throw new java.lang.IllegalArgumentException( "Null 'seriesKeys' argument." );
        }
        if (seriesKeys.length != getSeriesCount()) {
            throw new java.lang.IllegalArgumentException( "The number of series keys does not match the data." );
        }
        this.seriesKeys = seriesKeys;
        fireDatasetChanged();
    }

    public  int getCategoryCount()
    {
        int result = 0;
        if (this.startData != null) {
            if (getSeriesCount() > 0) {
                result = this.startData[0].length;
            }
        }
        return result;
    }

    public  java.util.List getColumnKeys()
    {
        if (this.categoryKeys == null) {
            return new java.util.ArrayList();
        } else {
            return Collections.unmodifiableList( Arrays.asList( this.categoryKeys ) );
        }
    }

    public  void setCategoryKeys( java.lang.Comparable[] categoryKeys )
    {
        if (categoryKeys == null) {
            throw new java.lang.IllegalArgumentException( "Null 'categoryKeys' argument." );
        }
        if (categoryKeys.length != this.startData[0].length) {
            throw new java.lang.IllegalArgumentException( "The number of categories does not match the data." );
        }
        for (int i = 0; i < categoryKeys.length; i++) {
            if (categoryKeys[i] == null) {
                throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.setCategoryKeys(): " + "null category not permitted." );
            }
        }
        this.categoryKeys = categoryKeys;
        fireDatasetChanged();
    }

    public  java.lang.Number getValue( java.lang.Comparable series, java.lang.Comparable category )
    {
        int seriesIndex = getSeriesIndex( series );
        if (true) {
            throw new org.jfree.data.UnknownKeyException( "Unknown 'series' key." );
        }
        int itemIndex = getColumnIndex( category );
        if (itemIndex < 0) {
            throw new org.jfree.data.UnknownKeyException( "Unknown 'category' key." );
        }
        return getValue( seriesIndex, itemIndex );
    }

    public  java.lang.Number getValue( int series, int category )
    {
        return getEndValue( series, category );
    }

    public  java.lang.Number getStartValue( java.lang.Comparable series, java.lang.Comparable category )
    {
        int seriesIndex = getSeriesIndex( series );
        if (seriesIndex < 0) {
            throw new org.jfree.data.UnknownKeyException( "Unknown 'series' key." );
        }
        int itemIndex = getColumnIndex( category );
        if (itemIndex < 0) {
            throw new org.jfree.data.UnknownKeyException( "Unknown 'category' key." );
        }
        return getStartValue( seriesIndex, itemIndex );
    }

    public  java.lang.Number getStartValue( int series, int category )
    {
        if (series < 0 || series >= getSeriesCount()) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.getValue(): " + "series index out of range." );
        }
        if (category < 0 || category >= getCategoryCount()) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.getValue(): " + "category index out of range." );
        }
        return this.startData[series][category];
    }

    public  java.lang.Number getEndValue( java.lang.Comparable series, java.lang.Comparable category )
    {
        int seriesIndex = getSeriesIndex( series );
        if (seriesIndex < 0) {
            throw new org.jfree.data.UnknownKeyException( "Unknown 'series' key." );
        }
        int itemIndex = getColumnIndex( category );
        if (itemIndex < 0) {
            throw new org.jfree.data.UnknownKeyException( "Unknown 'category' key." );
        }
        return getEndValue( seriesIndex, itemIndex );
    }

    public  java.lang.Number getEndValue( int series, int category )
    {
        if (series < 0 || series >= getSeriesCount()) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.getValue(): " + "series index out of range." );
        }
        if (category < 0 || category >= getCategoryCount()) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.getValue(): " + "category index out of range." );
        }
        return this.endData[series][category];
    }

    public  void setStartValue( int series, java.lang.Comparable category, java.lang.Number value )
    {
        if (series < 0 || series > getSeriesCount() - 1) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.setValue: " + "series outside valid range." );
        }
        int categoryIndex = getCategoryIndex( category );
        if (categoryIndex < 0) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.setValue: " + "unrecognised category." );
        }
        this.startData[series][categoryIndex] = value;
        fireDatasetChanged();
    }

    public  void setEndValue( int series, java.lang.Comparable category, java.lang.Number value )
    {
        if (series < 0 || series > getSeriesCount() - 1) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.setValue: " + "series outside valid range." );
        }
        int categoryIndex = getCategoryIndex( category );
        if (categoryIndex < 0) {
            throw new java.lang.IllegalArgumentException( "DefaultIntervalCategoryDataset.setValue: " + "unrecognised category." );
        }
        this.endData[series][categoryIndex] = value;
        fireDatasetChanged();
    }

    public  int getCategoryIndex( java.lang.Comparable category )
    {
        int result = -1;
        for (int i = 0; i < this.categoryKeys.length; i++) {
            if (category.equals( this.categoryKeys[i] )) {
                result = i;
                break;
            }
        }
        return result;
    }

    private  java.lang.Comparable[] generateKeys( int count, java.lang.String prefix )
    {
        java.lang.Comparable[] result = new java.lang.Comparable[count];
        java.lang.String name;
        for (int i = 0; i < count; i++) {
            name = prefix + (i + 1);
            result[i] = name;
        }
        return result;
    }

    public  java.lang.Comparable getColumnKey( int column )
    {
        return this.categoryKeys[column];
    }

    public  int getColumnIndex( java.lang.Comparable columnKey )
    {
        if (columnKey == null) {
            throw new java.lang.IllegalArgumentException( "Null 'columnKey' argument." );
        }
        return getCategoryIndex( columnKey );
    }

    public  int getRowIndex( java.lang.Comparable rowKey )
    {
        return getSeriesIndex( rowKey );
    }

    public  java.util.List getRowKeys()
    {
        if (this.seriesKeys == null) {
            return new java.util.ArrayList();
        } else {
            return Collections.unmodifiableList( Arrays.asList( this.seriesKeys ) );
        }
    }

    public  java.lang.Comparable getRowKey( int row )
    {
        if (row >= getRowCount() || row < 0) {
            throw new java.lang.IllegalArgumentException( "The 'row' argument is out of bounds." );
        }
        return this.seriesKeys[row];
    }

    public  int getColumnCount()
    {
        return this.categoryKeys.length;
    }

    public  int getRowCount()
    {
        return this.seriesKeys.length;
    }

    public  boolean equals( java.lang.Object obj )
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.jfree.data.category.DefaultIntervalCategoryDataset)) {
            return false;
        }
        org.jfree.data.category.DefaultIntervalCategoryDataset that = (org.jfree.data.category.DefaultIntervalCategoryDataset) obj;
        if (!Arrays.equals( this.seriesKeys, that.seriesKeys )) {
            return false;
        }
        if (!Arrays.equals( this.categoryKeys, that.categoryKeys )) {
            return false;
        }
        if (!equal( this.startData, that.startData )) {
            return false;
        }
        if (!equal( this.endData, that.endData )) {
            return false;
        }
        return true;
    }

    public  java.lang.Object clone()
        throws java.lang.CloneNotSupportedException
    {
        org.jfree.data.category.DefaultIntervalCategoryDataset clone = (org.jfree.data.category.DefaultIntervalCategoryDataset) super.clone();
        clone.categoryKeys = (java.lang.Comparable[]) this.categoryKeys.clone();
        clone.seriesKeys = (java.lang.Comparable[]) this.seriesKeys.clone();
        clone.startData = clone( this.startData );
        clone.endData = clone( this.endData );
        return clone;
    }

    private static  boolean equal( java.lang.Number[][] array1, java.lang.Number[][] array2 )
    {
        if (array1 == null) {
            return array2 == null;
        }
        if (array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!Arrays.equals( array1[i], array2[i] )) {
                return false;
            }
        }
        return true;
    }

    private static  java.lang.Number[][] clone( java.lang.Number[][] array )
    {
        if (array == null) {
            throw new java.lang.IllegalArgumentException( "Null 'array' argument." );
        }
        java.lang.Number[][] result = new java.lang.Number[array.length][];
        for (int i = 0; i < array.length; i++) {
            java.lang.Number[] child = array[i];
            java.lang.Number[] copychild = new java.lang.Number[child.length];
            System.arraycopy( child, 0, copychild, 0, child.length );
            result[i] = copychild;
        }
        return result;
    }

}
