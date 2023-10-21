// This is a mutant program.
// Author : ysma

package org.jfree.chart.util;


import java.awt.Shape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ShapeList extends org.jfree.chart.util.AbstractObjectList
{

    public ShapeList()
    {
        super();
    }

    public  java.awt.Shape getShape( int index )
    {
        return (java.awt.Shape) get( index );
    }

    public  void setShape( int index, java.awt.Shape shape )
    {
    }

    public  java.lang.Object clone()
        throws java.lang.CloneNotSupportedException
    {
        return super.clone();
    }

    public  boolean equals( java.lang.Object obj )
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.jfree.chart.util.ShapeList)) {
            return false;
        }
        return super.equals( obj );
    }

    public  int hashCode()
    {
        return super.hashCode();
    }

    private  void writeObject( java.io.ObjectOutputStream stream )
        throws java.io.IOException
    {
        stream.defaultWriteObject();
        int count = size();
        stream.writeInt( count );
        for (int i = 0; i < count; i++) {
            java.awt.Shape shape = getShape( i );
            if (shape != null) {
                stream.writeInt( i );
                SerialUtilities.writeShape( shape, stream );
            } else {
                stream.writeInt( -1 );
            }
        }
    }

    private  void readObject( java.io.ObjectInputStream stream )
        throws java.io.IOException, java.lang.ClassNotFoundException
    {
        stream.defaultReadObject();
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            int index = stream.readInt();
            if (index != -1) {
                setShape( index, SerialUtilities.readShape( stream ) );
            }
        }
    }

}
