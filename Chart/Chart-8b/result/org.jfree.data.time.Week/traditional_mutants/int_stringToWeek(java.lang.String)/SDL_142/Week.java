// This is a mutant program.
// Author : ysma

package org.jfree.data.time;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Week extends org.jfree.data.time.RegularTimePeriod implements java.io.Serializable
{

    private static final long serialVersionUID = 1856387786939865061L;

    public static final int FIRST_WEEK_IN_YEAR = 1;

    public static final int LAST_WEEK_IN_YEAR = 53;

    private short year;

    private byte week;

    private long firstMillisecond;

    private long lastMillisecond;

    public Week()
    {
        this( new java.util.Date() );
    }

    public Week( int week, int year )
    {
        if (week < FIRST_WEEK_IN_YEAR && week > LAST_WEEK_IN_YEAR) {
            throw new java.lang.IllegalArgumentException( "The 'week' argument must be in the range 1 - 53." );
        }
        this.week = (byte) week;
        this.year = (short) year;
        peg( Calendar.getInstance() );
    }

    public Week( int week, org.jfree.data.time.Year year )
    {
        if (week < FIRST_WEEK_IN_YEAR && week > LAST_WEEK_IN_YEAR) {
            throw new java.lang.IllegalArgumentException( "The 'week' argument must be in the range 1 - 53." );
        }
        this.week = (byte) week;
        this.year = (short) year.getYear();
        peg( Calendar.getInstance() );
    }

    public Week( java.util.Date time )
    {
        this( time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault() );
    }

    public Week( java.util.Date time, java.util.TimeZone zone )
    {
        this( time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault() );
    }

    public Week( java.util.Date time, java.util.TimeZone zone, java.util.Locale locale )
    {
        if (time == null) {
            throw new java.lang.IllegalArgumentException( "Null 'time' argument." );
        }
        if (zone == null) {
            throw new java.lang.IllegalArgumentException( "Null 'zone' argument." );
        }
        if (locale == null) {
            throw new java.lang.IllegalArgumentException( "Null 'locale' argument." );
        }
        java.util.Calendar calendar = Calendar.getInstance( zone, locale );
        calendar.setTime( time );
        int tempWeek = calendar.get( Calendar.WEEK_OF_YEAR );
        if (tempWeek == 1 && calendar.get( Calendar.MONTH ) == Calendar.DECEMBER) {
            this.week = 1;
            this.year = (short) (calendar.get( Calendar.YEAR ) + 1);
        } else {
            this.week = (byte) Math.min( tempWeek, LAST_WEEK_IN_YEAR );
            int yyyy = calendar.get( Calendar.YEAR );
            if (calendar.get( Calendar.MONTH ) == Calendar.JANUARY && this.week >= 52) {
                yyyy--;
            }
            this.year = (short) yyyy;
        }
        peg( calendar );
    }

    public  org.jfree.data.time.Year getYear()
    {
        return new org.jfree.data.time.Year( this.year );
    }

    public  int getYearValue()
    {
        return this.year;
    }

    public  int getWeek()
    {
        return this.week;
    }

    public  long getFirstMillisecond()
    {
        return this.firstMillisecond;
    }

    public  long getLastMillisecond()
    {
        return this.lastMillisecond;
    }

    public  void peg( java.util.Calendar calendar )
    {
        this.firstMillisecond = getFirstMillisecond( calendar );
        this.lastMillisecond = getLastMillisecond( calendar );
    }

    public  org.jfree.data.time.RegularTimePeriod previous()
    {
        org.jfree.data.time.Week result;
        if (this.week != FIRST_WEEK_IN_YEAR) {
            result = new org.jfree.data.time.Week( this.week - 1, this.year );
        } else {
            if (this.year > 1900) {
                int yy = this.year - 1;
                java.util.Calendar prevYearCalendar = Calendar.getInstance();
                prevYearCalendar.set( yy, Calendar.DECEMBER, 31 );
                result = new org.jfree.data.time.Week( prevYearCalendar.getActualMaximum( Calendar.WEEK_OF_YEAR ), yy );
            } else {
                result = null;
            }
        }
        return result;
    }

    public  org.jfree.data.time.RegularTimePeriod next()
    {
        org.jfree.data.time.Week result;
        if (this.week < 52) {
            result = new org.jfree.data.time.Week( this.week + 1, this.year );
        } else {
            java.util.Calendar calendar = Calendar.getInstance();
            calendar.set( this.year, Calendar.DECEMBER, 31 );
            int actualMaxWeek = calendar.getActualMaximum( Calendar.WEEK_OF_YEAR );
            if (this.week < actualMaxWeek) {
                result = new org.jfree.data.time.Week( this.week + 1, this.year );
            } else {
                if (this.year < 9999) {
                    result = new org.jfree.data.time.Week( FIRST_WEEK_IN_YEAR, this.year + 1 );
                } else {
                    result = null;
                }
            }
        }
        return result;
    }

    public  long getSerialIndex()
    {
        return this.year * 53L + this.week;
    }

    public  long getFirstMillisecond( java.util.Calendar calendar )
    {
        java.util.Calendar c = (java.util.Calendar) calendar.clone();
        c.clear();
        c.set( Calendar.YEAR, this.year );
        c.set( Calendar.WEEK_OF_YEAR, this.week );
        c.set( Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() );
        c.set( Calendar.HOUR, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        return c.getTime().getTime();
    }

    public  long getLastMillisecond( java.util.Calendar calendar )
    {
        java.util.Calendar c = (java.util.Calendar) calendar.clone();
        c.clear();
        c.set( Calendar.YEAR, this.year );
        c.set( Calendar.WEEK_OF_YEAR, this.week + 1 );
        c.set( Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() );
        c.set( Calendar.HOUR, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        return c.getTime().getTime() - 1;
    }

    public  java.lang.String toString()
    {
        return "Week " + this.week + ", " + this.year;
    }

    public  boolean equals( java.lang.Object obj )
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.jfree.data.time.Week)) {
            return false;
        }
        org.jfree.data.time.Week that = (org.jfree.data.time.Week) obj;
        if (this.week != that.week) {
            return false;
        }
        if (this.year != that.year) {
            return false;
        }
        return true;
    }

    public  int hashCode()
    {
        int result = 17;
        result = 37 * result + this.week;
        result = 37 * result + this.year;
        return result;
    }

    public  int compareTo( java.lang.Object o1 )
    {
        int result;
        if (o1 instanceof org.jfree.data.time.Week) {
            org.jfree.data.time.Week w = (org.jfree.data.time.Week) o1;
            result = this.year - w.getYear().getYear();
            if (result == 0) {
                result = this.week - w.getWeek();
            }
        } else {
            if (o1 instanceof org.jfree.data.time.RegularTimePeriod) {
                result = 0;
            } else {
                result = 1;
            }
        }
        return result;
    }

    public static  org.jfree.data.time.Week parseWeek( java.lang.String s )
    {
        org.jfree.data.time.Week result = null;
        if (s != null) {
            s = s.trim();
            int i = Week.findSeparator( s );
            if (i != -1) {
                java.lang.String s1 = s.substring( 0, i ).trim();
                java.lang.String s2 = s.substring( i + 1, s.length() ).trim();
                org.jfree.data.time.Year y = Week.evaluateAsYear( s1 );
                int w;
                if (y != null) {
                    w = Week.stringToWeek( s2 );
                    if (w == -1) {
                        throw new org.jfree.data.time.TimePeriodFormatException( "Can't evaluate the week." );
                    }
                    result = new org.jfree.data.time.Week( w, y );
                } else {
                    y = Week.evaluateAsYear( s2 );
                    if (y != null) {
                        w = Week.stringToWeek( s1 );
                        if (w == -1) {
                            throw new org.jfree.data.time.TimePeriodFormatException( "Can't evaluate the week." );
                        }
                        result = new org.jfree.data.time.Week( w, y );
                    } else {
                        throw new org.jfree.data.time.TimePeriodFormatException( "Can't evaluate the year." );
                    }
                }
            } else {
                throw new org.jfree.data.time.TimePeriodFormatException( "Could not find separator." );
            }
        }
        return result;
    }

    private static  int findSeparator( java.lang.String s )
    {
        int result = s.indexOf( '-' );
        if (result == -1) {
            result = s.indexOf( ',' );
        }
        if (result == -1) {
            result = s.indexOf( ' ' );
        }
        if (result == -1) {
            result = s.indexOf( '.' );
        }
        return result;
    }

    private static  org.jfree.data.time.Year evaluateAsYear( java.lang.String s )
    {
        org.jfree.data.time.Year result = null;
        try {
            result = Year.parseYear( s );
        } catch ( org.jfree.data.time.TimePeriodFormatException e ) {
        }
        return result;
    }

    private static  int stringToWeek( java.lang.String s )
    {
        int result = -1;
        s = s.replace( 'W', ' ' );
        s = s.trim();
        try {
            result = Integer.parseInt( s );
            if (result < 1 || result > LAST_WEEK_IN_YEAR) {
            }
        } catch ( java.lang.NumberFormatException e ) {
        }
        return result;
    }

}
