// This is a mutant program.
// Author : ysma

package org.jfree.data;


import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;


public class DefaultKeyedValues2D implements org.jfree.data.KeyedValues2D, org.jfree.chart.util.PublicCloneable, java.lang.Cloneable, java.io.Serializable
{

    private static final long serialVersionUID = -5514169970951994748L;

    private java.util.List rowKeys;

    private java.util.List columnKeys;

    private java.util.List rows;

    private boolean sortRowKeys;

    public DefaultKeyedValues2D()
    {
        this( false );
    }

    public DefaultKeyedValues2D( boolean sortRowKeys )
    {
        this.rowKeys = new java.util.ArrayList();
        this.columnKeys = new java.util.ArrayList();
        this.rows = new java.util.ArrayList();
        this.sortRowKeys = sortRowKeys;
    }

    public  int getRowCount()
    {
        return this.rowKeys.size();
    }

    public  int getColumnCount()
    {
        return this.columnKeys.size();
    }

    public  java.lang.Number getValue( int row, int column )
    {
        java.lang.Number result = null;
        org.jfree.data.DefaultKeyedValues rowData = (org.jfree.data.DefaultKeyedValues) this.rows.get( row );
        if (rowData != null) {
            java.lang.Comparable columnKey = (java.lang.Comparable) this.columnKeys.get( column );
            int index = rowData.getIndex( columnKey );
            if (index >= 0) {
                result = rowData.getValue( index );
            }
        }
        return result;
    }

    public  java.lang.Comparable getRowKey( int row )
    {
        return (java.lang.Comparable) this.rowKeys.get( row );
    }

    public  int getRowIndex( java.lang.Comparable key )
    {
        if (key == null) {
            throw new java.lang.IllegalArgumentException( "Null 'key' argument." );
        }
        if (this.sortRowKeys) {
            return Collections.binarySearch( this.rowKeys, key );
        } else {
            return this.rowKeys.indexOf( key );
        }
    }

    public  java.util.List getRowKeys()
    {
        return Collections.unmodifiableList( this.rowKeys );
    }

    public  java.lang.Comparable getColumnKey( int column )
    {
        return (java.lang.Comparable) this.columnKeys.get( column );
    }

    public  int getColumnIndex( java.lang.Comparable key )
    {
        if (key == null) {
            throw new java.lang.IllegalArgumentException( "Null 'key' argument." );
        }
        return this.columnKeys.indexOf( key );
    }

    public  java.util.List getColumnKeys()
    {
        return Collections.unmodifiableList( this.columnKeys );
    }

    public  java.lang.Number getValue( java.lang.Comparable rowKey, java.lang.Comparable columnKey )
    {
        if (rowKey == null) {
            throw new java.lang.IllegalArgumentException( "Null 'rowKey' argument." );
        }
        if (columnKey == null) {
            throw new java.lang.IllegalArgumentException( "Null 'columnKey' argument." );
        }
        if (!this.columnKeys.contains( columnKey )) {
            throw new org.jfree.data.UnknownKeyException( "Unrecognised columnKey: " + columnKey );
        }
        int row = getRowIndex( rowKey );
        if (row >= 0) {
            org.jfree.data.DefaultKeyedValues rowData = (org.jfree.data.DefaultKeyedValues) this.rows.get( row );
            int col = rowData.getIndex( columnKey );
            return col >= 0 ? rowData.getValue( col ) : null;
        } else {
            throw new org.jfree.data.UnknownKeyException( "Unrecognised rowKey: " + rowKey );
        }
    }

    public  void addValue( java.lang.Number value, java.lang.Comparable rowKey, java.lang.Comparable columnKey )
    {
        setValue( value, rowKey, columnKey );
    }

    public  void setValue( java.lang.Number value, java.lang.Comparable rowKey, java.lang.Comparable columnKey )
    {
        org.jfree.data.DefaultKeyedValues row;
        int rowIndex = getRowIndex( rowKey );
        if (rowIndex >= 0) {
            row = (org.jfree.data.DefaultKeyedValues) this.rows.get( rowIndex );
        } else {
            row = new org.jfree.data.DefaultKeyedValues();
            if (this.sortRowKeys) {
                rowIndex = -rowIndex - 1;
                this.rows.add( rowIndex, row );
            } else {
                this.rowKeys.add( rowKey );
                this.rows.add( row );
            }
        }
        row.setValue( columnKey, value );
        int columnIndex = this.columnKeys.indexOf( columnKey );
        if (columnIndex < 0) {
            this.columnKeys.add( columnKey );
        }
    }

    public  void removeValue( java.lang.Comparable rowKey, java.lang.Comparable columnKey )
    {
        setValue( null, rowKey, columnKey );
        boolean allNull = true;
        int rowIndex = getRowIndex( rowKey );
        org.jfree.data.DefaultKeyedValues row = (org.jfree.data.DefaultKeyedValues) this.rows.get( rowIndex );
        for (int item = 0, itemCount = row.getItemCount(); item < itemCount; item++) {
            if (row.getValue( item ) != null) {
                allNull = false;
                break;
            }
        }
        if (allNull) {
            this.rowKeys.remove( rowIndex );
            this.rows.remove( rowIndex );
        }
        allNull = true;
        for (int item = 0, itemCount = this.rows.size(); item < itemCount; item++) {
            row = (org.jfree.data.DefaultKeyedValues) this.rows.get( item );
            int columnIndex = row.getIndex( columnKey );
            if (columnIndex >= 0 && row.getValue( columnIndex ) != null) {
                allNull = false;
                break;
            }
        }
        if (allNull) {
            for (int item = 0, itemCount = this.rows.size(); item < itemCount; item++) {
                row = (org.jfree.data.DefaultKeyedValues) this.rows.get( item );
                int columnIndex = row.getIndex( columnKey );
                if (columnIndex >= 0) {
                    row.removeValue( columnIndex );
                }
            }
            this.columnKeys.remove( columnKey );
        }
    }

    public  void removeRow( int rowIndex )
    {
        this.rowKeys.remove( rowIndex );
        this.rows.remove( rowIndex );
    }

    public  void removeRow( java.lang.Comparable rowKey )
    {
        removeRow( getRowIndex( rowKey ) );
    }

    public  void removeColumn( int columnIndex )
    {
        java.lang.Comparable columnKey = getColumnKey( columnIndex );
        removeColumn( columnKey );
    }

    public  void removeColumn( java.lang.Comparable columnKey )
    {
        java.util.Iterator iterator = this.rows.iterator();
        while (iterator.hasNext()) {
            org.jfree.data.DefaultKeyedValues rowData = (org.jfree.data.DefaultKeyedValues) iterator.next();
            rowData.removeValue( columnKey );
        }
        this.columnKeys.remove( columnKey );
    }

    public  void clear()
    {
        this.rowKeys.clear();
        this.columnKeys.clear();
        this.rows.clear();
    }

    public  boolean equals( java.lang.Object o )
    {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof org.jfree.data.KeyedValues2D)) {
            return false;
        }
        org.jfree.data.KeyedValues2D kv2D = (org.jfree.data.KeyedValues2D) o;
        if (!getRowKeys().equals( kv2D.getRowKeys() )) {
            return false;
        }
        if (!getColumnKeys().equals( kv2D.getColumnKeys() )) {
            return false;
        }
        int rowCount = getRowCount();
        if (rowCount != kv2D.getRowCount()) {
            return false;
        }
        int colCount = getColumnCount();
        if (colCount != kv2D.getColumnCount()) {
            return false;
        }
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                java.lang.Number v1 = getValue( r, c );
                java.lang.Number v2 = kv2D.getValue( r, c );
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
        }
        return true;
    }

    public  int hashCode()
    {
        int result;
        result = this.rowKeys.hashCode();
        result = 29 * result + this.columnKeys.hashCode();
        result = 29 * result + this.rows.hashCode();
        return result;
    }

    public  java.lang.Object clone()
        throws java.lang.CloneNotSupportedException
    {
        org.jfree.data.DefaultKeyedValues2D clone = (org.jfree.data.DefaultKeyedValues2D) super.clone();
        clone.columnKeys = new java.util.ArrayList( this.columnKeys );
        clone.rowKeys = new java.util.ArrayList( this.rowKeys );
        clone.rows = (java.util.List) ObjectUtilities.deepClone( this.rows );
        return clone;
    }

}
