// This is a mutant program.
// Author : ysma

package org.joda.time;


import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.base.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;


public final class Period extends org.joda.time.base.BasePeriod implements org.joda.time.ReadablePeriod, java.io.Serializable
{

    public static final org.joda.time.Period ZERO = new org.joda.time.Period();

    private static final long serialVersionUID = 741052353876488155L;

    public static  org.joda.time.Period parse( java.lang.String str )
    {
        return parse( str, ISOPeriodFormat.standard() );
    }

    public static  org.joda.time.Period parse( java.lang.String str, org.joda.time.format.PeriodFormatter formatter )
    {
        return formatter.parsePeriod( str );
    }

    public static  org.joda.time.Period years( int years )
    {
        return new org.joda.time.Period( new int[]{ years, 0, 0, 0, 0, 0, 0, 0, 0 }, PeriodType.standard() );
    }

    public static  org.joda.time.Period months( int months )
    {
        return new org.joda.time.Period( new int[]{ 0, months, 0, 0, 0, 0, 0, 0 }, PeriodType.standard() );
    }

    public static  org.joda.time.Period weeks( int weeks )
    {
        return new org.joda.time.Period( new int[]{ 0, 0, weeks, 0, 0, 0, 0, 0 }, PeriodType.standard() );
    }

    public static  org.joda.time.Period days( int days )
    {
        return new org.joda.time.Period( new int[]{ 0, 0, 0, days, 0, 0, 0, 0 }, PeriodType.standard() );
    }

    public static  org.joda.time.Period hours( int hours )
    {
        return new org.joda.time.Period( new int[]{ 0, 0, 0, 0, hours, 0, 0, 0 }, PeriodType.standard() );
    }

    public static  org.joda.time.Period minutes( int minutes )
    {
        return new org.joda.time.Period( new int[]{ 0, 0, 0, 0, 0, minutes, 0, 0 }, PeriodType.standard() );
    }

    public static  org.joda.time.Period seconds( int seconds )
    {
        return new org.joda.time.Period( new int[]{ 0, 0, 0, 0, 0, 0, seconds, 0 }, PeriodType.standard() );
    }

    public static  org.joda.time.Period millis( int millis )
    {
        return new org.joda.time.Period( new int[]{ 0, 0, 0, 0, 0, 0, 0, millis }, PeriodType.standard() );
    }

    public static  org.joda.time.Period fieldDifference( org.joda.time.ReadablePartial start, org.joda.time.ReadablePartial end )
    {
        if (start == null || end == null) {
            throw new java.lang.IllegalArgumentException( "ReadablePartial objects must not be null" );
        }
        if (start.size() != end.size()) {
            throw new java.lang.IllegalArgumentException( "ReadablePartial objects must have the same set of fields" );
        }
        org.joda.time.DurationFieldType[] types = new org.joda.time.DurationFieldType[start.size()];
        int[] values = new int[start.size()];
        for (int i = 0, isize = start.size(); i < isize; i++) {
            if (start.getFieldType( i ) != end.getFieldType( i )) {
                throw new java.lang.IllegalArgumentException( "ReadablePartial objects must have the same set of fields" );
            }
            types[i] = start.getFieldType( i ).getDurationType();
            if (i > 0 && types[i - 1] == types[i]) {
                throw new java.lang.IllegalArgumentException( "ReadablePartial objects must not have overlapping fields" );
            }
            values[i] = end.getValue( i ) - start.getValue( i );
        }
        return new org.joda.time.Period( values, PeriodType.forFields( types ) );
    }

    public Period()
    {
        super( 0L, null, null );
    }

    public Period( int hours, int minutes, int seconds, int millis )
    {
        super( 0, 0, 0, 0, hours, minutes, seconds, millis, PeriodType.standard() );
    }

    public Period( int years, int months, int weeks, int days, int hours, int minutes, int seconds, int millis )
    {
        super( years, months, weeks, days, hours, minutes, seconds, millis, PeriodType.standard() );
    }

    public Period( int years, int months, int weeks, int days, int hours, int minutes, int seconds, int millis, org.joda.time.PeriodType type )
    {
        super( years, months, weeks, days, hours, minutes, seconds, millis, type );
    }

    public Period( long duration )
    {
        super( duration );
    }

    public Period( long duration, org.joda.time.PeriodType type )
    {
        super( duration, type, null );
    }

    public Period( long duration, org.joda.time.Chronology chronology )
    {
        super( duration, null, chronology );
    }

    public Period( long duration, org.joda.time.PeriodType type, org.joda.time.Chronology chronology )
    {
        super( duration, type, chronology );
    }

    public Period( long startInstant, long endInstant )
    {
        super( startInstant, endInstant, null, null );
    }

    public Period( long startInstant, long endInstant, org.joda.time.PeriodType type )
    {
        super( startInstant, endInstant, type, null );
    }

    public Period( long startInstant, long endInstant, org.joda.time.Chronology chrono )
    {
        super( startInstant, endInstant, null, chrono );
    }

    public Period( long startInstant, long endInstant, org.joda.time.PeriodType type, org.joda.time.Chronology chrono )
    {
        super( startInstant, endInstant, type, chrono );
    }

    public Period( org.joda.time.ReadableInstant startInstant, org.joda.time.ReadableInstant endInstant )
    {
        super( startInstant, endInstant, null );
    }

    public Period( org.joda.time.ReadableInstant startInstant, org.joda.time.ReadableInstant endInstant, org.joda.time.PeriodType type )
    {
        super( startInstant, endInstant, type );
    }

    public Period( org.joda.time.ReadablePartial start, org.joda.time.ReadablePartial end )
    {
        super( start, end, null );
    }

    public Period( org.joda.time.ReadablePartial start, org.joda.time.ReadablePartial end, org.joda.time.PeriodType type )
    {
        super( start, end, type );
    }

    public Period( org.joda.time.ReadableInstant startInstant, org.joda.time.ReadableDuration duration )
    {
        super( startInstant, duration, null );
    }

    public Period( org.joda.time.ReadableInstant startInstant, org.joda.time.ReadableDuration duration, org.joda.time.PeriodType type )
    {
        super( startInstant, duration, type );
    }

    public Period( org.joda.time.ReadableDuration duration, org.joda.time.ReadableInstant endInstant )
    {
        super( duration, endInstant, null );
    }

    public Period( org.joda.time.ReadableDuration duration, org.joda.time.ReadableInstant endInstant, org.joda.time.PeriodType type )
    {
        super( duration, endInstant, type );
    }

    public Period( java.lang.Object period )
    {
        super( period, null, null );
    }

    public Period( java.lang.Object period, org.joda.time.PeriodType type )
    {
        super( period, type, null );
    }

    public Period( java.lang.Object period, org.joda.time.Chronology chrono )
    {
        super( period, null, chrono );
    }

    public Period( java.lang.Object period, org.joda.time.PeriodType type, org.joda.time.Chronology chrono )
    {
        super( period, type, chrono );
    }

    private Period( int[] values, org.joda.time.PeriodType type )
    {
        super( values, type );
    }

    public  org.joda.time.Period toPeriod()
    {
        return this;
    }

    public  int getYears()
    {
        return getPeriodType().getIndexedField( this, PeriodType.YEAR_INDEX );
    }

    public  int getMonths()
    {
        return getPeriodType().getIndexedField( this, PeriodType.MONTH_INDEX );
    }

    public  int getWeeks()
    {
        return getPeriodType().getIndexedField( this, PeriodType.WEEK_INDEX );
    }

    public  int getDays()
    {
        return getPeriodType().getIndexedField( this, PeriodType.DAY_INDEX );
    }

    public  int getHours()
    {
        return getPeriodType().getIndexedField( this, PeriodType.HOUR_INDEX );
    }

    public  int getMinutes()
    {
        return getPeriodType().getIndexedField( this, PeriodType.MINUTE_INDEX );
    }

    public  int getSeconds()
    {
        return getPeriodType().getIndexedField( this, PeriodType.SECOND_INDEX );
    }

    public  int getMillis()
    {
        return getPeriodType().getIndexedField( this, PeriodType.MILLI_INDEX );
    }

    public  org.joda.time.Period withPeriodType( org.joda.time.PeriodType type )
    {
        type = DateTimeUtils.getPeriodType( type );
        if (type.equals( getPeriodType() )) {
            return this;
        }
        return new org.joda.time.Period( this, type );
    }

    public  org.joda.time.Period withFields( org.joda.time.ReadablePeriod period )
    {
        if (period == null) {
            return this;
        }
        int[] newValues = getValues();
        newValues = super.mergePeriodInto( newValues, period );
        return new org.joda.time.Period( newValues, getPeriodType() );
    }

    public  org.joda.time.Period withField( org.joda.time.DurationFieldType field, int value )
    {
        if (field == null) {
            throw new java.lang.IllegalArgumentException( "Field must not be null" );
        }
        int[] newValues = getValues();
        super.setFieldInto( newValues, field, value );
        return new org.joda.time.Period( newValues, getPeriodType() );
    }

    public  org.joda.time.Period withFieldAdded( org.joda.time.DurationFieldType field, int value )
    {
        if (field == null) {
            throw new java.lang.IllegalArgumentException( "Field must not be null" );
        }
        if (value == 0) {
            return this;
        }
        int[] newValues = getValues();
        super.addFieldInto( newValues, field, value );
        return new org.joda.time.Period( newValues, getPeriodType() );
    }

    public  org.joda.time.Period withYears( int years )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.YEAR_INDEX, values, years );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period withMonths( int months )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.MONTH_INDEX, values, months );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period withWeeks( int weeks )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.WEEK_INDEX, values, weeks );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period withDays( int days )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.DAY_INDEX, values, days );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period withHours( int hours )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.HOUR_INDEX, values, hours );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period withMinutes( int minutes )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.MINUTE_INDEX, values, minutes );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period withSeconds( int seconds )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.SECOND_INDEX, values, seconds );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period withMillis( int millis )
    {
        int[] values = getValues();
        getPeriodType().setIndexedField( this, PeriodType.MILLI_INDEX, values, millis );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plus( org.joda.time.ReadablePeriod period )
    {
        if (period == null) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.YEAR_INDEX, values, period.get( DurationFieldType.YEARS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.MONTH_INDEX, values, period.get( DurationFieldType.MONTHS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.WEEK_INDEX, values, period.get( DurationFieldType.WEEKS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.DAY_INDEX, values, period.get( DurationFieldType.DAYS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.HOUR_INDEX, values, period.get( DurationFieldType.HOURS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.MINUTE_INDEX, values, period.get( DurationFieldType.MINUTES_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.SECOND_INDEX, values, period.get( DurationFieldType.SECONDS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.MILLI_INDEX, values, period.get( DurationFieldType.MILLIS_TYPE ) );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusYears( int years )
    {
        if (years == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.YEAR_INDEX, values, years );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusMonths( int months )
    {
        if (months == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.MONTH_INDEX, values, months );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusWeeks( int weeks )
    {
        if (weeks == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.WEEK_INDEX, values, weeks );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusDays( int days )
    {
        if (days == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.DAY_INDEX, values, days );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusHours( int hours )
    {
        if (hours == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.HOUR_INDEX, values, hours );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusMinutes( int minutes )
    {
        if (minutes == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.MINUTE_INDEX, values, minutes );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusSeconds( int seconds )
    {
        if (seconds == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.SECOND_INDEX, values, seconds );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period plusMillis( int millis )
    {
        if (millis == 0) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.MILLI_INDEX, values, millis );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period minus( org.joda.time.ReadablePeriod period )
    {
        if (period == null) {
            return this;
        }
        int[] values = getValues();
        getPeriodType().addIndexedField( this, PeriodType.YEAR_INDEX, values, -period.get( DurationFieldType.YEARS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.MONTH_INDEX, values, -period.get( DurationFieldType.MONTHS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.WEEK_INDEX, values, -period.get( DurationFieldType.WEEKS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.DAY_INDEX, values, -period.get( DurationFieldType.DAYS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.HOUR_INDEX, values, -period.get( DurationFieldType.HOURS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.MINUTE_INDEX, values, -period.get( DurationFieldType.MINUTES_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.SECOND_INDEX, values, -period.get( DurationFieldType.SECONDS_TYPE ) );
        getPeriodType().addIndexedField( this, PeriodType.MILLI_INDEX, values, -period.get( DurationFieldType.MILLIS_TYPE ) );
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period minusYears( int years )
    {
        return plusYears( -years );
    }

    public  org.joda.time.Period minusMonths( int months )
    {
        return plusMonths( -months );
    }

    public  org.joda.time.Period minusWeeks( int weeks )
    {
        return plusWeeks( -weeks );
    }

    public  org.joda.time.Period minusDays( int days )
    {
        return plusDays( -days );
    }

    public  org.joda.time.Period minusHours( int hours )
    {
        return plusHours( -hours );
    }

    public  org.joda.time.Period minusMinutes( int minutes )
    {
        return plusMinutes( -minutes );
    }

    public  org.joda.time.Period minusSeconds( int seconds )
    {
        return plusSeconds( -seconds );
    }

    public  org.joda.time.Period minusMillis( int millis )
    {
        return plusMillis( -millis );
    }

    public  org.joda.time.Period multipliedBy( int scalar )
    {
        if (this == ZERO || scalar == 1) {
            return this;
        }
        int[] values = getValues();
        for (int i = 0; i < values.length; i++) {
            values[i] = FieldUtils.safeMultiply( values[i], scalar );
        }
        return new org.joda.time.Period( values, getPeriodType() );
    }

    public  org.joda.time.Period negated()
    {
        return multipliedBy( -1 );
    }

    public  org.joda.time.Weeks toStandardWeeks()
    {
        checkYearsAndMonths( "Weeks" );
        long millis = getMillis();
        millis += (long) getSeconds() * DateTimeConstants.MILLIS_PER_SECOND;
        millis += (long) getMinutes() * DateTimeConstants.MILLIS_PER_MINUTE;
        millis += (long) getHours() * DateTimeConstants.MILLIS_PER_HOUR;
        millis += (long) getDays() * DateTimeConstants.MILLIS_PER_DAY;
        long weeks = (long) getWeeks() + millis / DateTimeConstants.MILLIS_PER_WEEK;
        return Weeks.weeks( FieldUtils.safeToInt( weeks ) );
    }

    public  org.joda.time.Days toStandardDays()
    {
        checkYearsAndMonths( "Days" );
        long millis = getMillis();
        millis += (long) getSeconds() * DateTimeConstants.MILLIS_PER_SECOND;
        millis += (long) getMinutes() * DateTimeConstants.MILLIS_PER_MINUTE;
        millis += (long) getHours() * DateTimeConstants.MILLIS_PER_HOUR;
        long days = millis / DateTimeConstants.MILLIS_PER_DAY;
        days = FieldUtils.safeAdd( days, getDays() );
        days = FieldUtils.safeAdd( days, (long) getWeeks() * (long) DateTimeConstants.DAYS_PER_WEEK );
        return Days.days( FieldUtils.safeToInt( days ) );
    }

    public  org.joda.time.Hours toStandardHours()
    {
        checkYearsAndMonths( "Hours" );
        long millis = getMillis();
        millis += (long) getSeconds() * DateTimeConstants.MILLIS_PER_SECOND;
        millis += (long) getMinutes() * DateTimeConstants.MILLIS_PER_MINUTE;
        long hours = millis / DateTimeConstants.MILLIS_PER_HOUR;
        hours = FieldUtils.safeAdd( hours, getHours() );
        hours = FieldUtils.safeAdd( hours, (long) getDays() * (long) DateTimeConstants.HOURS_PER_DAY );
        hours = FieldUtils.safeAdd( hours, (long) getWeeks() * (long) DateTimeConstants.HOURS_PER_WEEK );
        return Hours.hours( FieldUtils.safeToInt( hours ) );
    }

    public  org.joda.time.Minutes toStandardMinutes()
    {
        checkYearsAndMonths( "Minutes" );
        long millis = getMillis();
        millis += (long) getSeconds() * DateTimeConstants.MILLIS_PER_SECOND;
        long minutes = millis / DateTimeConstants.MILLIS_PER_MINUTE;
        minutes = FieldUtils.safeAdd( minutes, getMinutes() );
        minutes = FieldUtils.safeAdd( minutes, (long) getHours() * (long) DateTimeConstants.MINUTES_PER_HOUR );
        minutes = FieldUtils.safeAdd( minutes, (long) getDays() * (long) DateTimeConstants.MINUTES_PER_DAY );
        minutes = FieldUtils.safeAdd( minutes, (long) getWeeks() * (long) DateTimeConstants.MINUTES_PER_WEEK );
        return Minutes.minutes( FieldUtils.safeToInt( minutes ) );
    }

    public  org.joda.time.Seconds toStandardSeconds()
    {
        checkYearsAndMonths( "Seconds" );
        long seconds = getMillis() / DateTimeConstants.MILLIS_PER_SECOND;
        seconds = FieldUtils.safeAdd( seconds, getSeconds() );
        seconds = FieldUtils.safeAdd( seconds, (long) getMinutes() * (long) DateTimeConstants.SECONDS_PER_MINUTE );
        seconds = FieldUtils.safeAdd( seconds, (long) getHours() * (long) DateTimeConstants.SECONDS_PER_HOUR );
        seconds = FieldUtils.safeAdd( seconds, (long) getDays() * (long) DateTimeConstants.SECONDS_PER_DAY );
        seconds = FieldUtils.safeAdd( seconds, (long) getWeeks() * (long) DateTimeConstants.SECONDS_PER_WEEK );
        return Seconds.seconds( FieldUtils.safeToInt( seconds ) );
    }

    public  org.joda.time.Duration toStandardDuration()
    {
        checkYearsAndMonths( "Duration" );
        long millis = getMillis();
        millis += (long) getSeconds() * (long) DateTimeConstants.MILLIS_PER_SECOND;
        millis += (long) getMinutes() * (long) DateTimeConstants.MILLIS_PER_MINUTE;
        millis += (long) getHours() * (long) DateTimeConstants.MILLIS_PER_HOUR;
        millis += (long) getDays() * (long) DateTimeConstants.MILLIS_PER_DAY;
        millis += (long) getWeeks() * (long) DateTimeConstants.MILLIS_PER_WEEK;
        return new org.joda.time.Duration( millis );
    }

    private  void checkYearsAndMonths( java.lang.String destintionType )
    {
        if (getMonths() != 0) {
            throw new java.lang.UnsupportedOperationException( "Cannot convert to " + destintionType + " as this period contains months and months vary in length" );
        }
        if (getYears() != 0) {
            throw new java.lang.UnsupportedOperationException( "Cannot convert to " + destintionType + " as this period contains years and years vary in length" );
        }
    }

    public  org.joda.time.Period normalizedStandard()
    {
        return normalizedStandard( PeriodType.standard() );
    }

    public  org.joda.time.Period normalizedStandard( org.joda.time.PeriodType type )
    {
        type = DateTimeUtils.getPeriodType( type );
        long millis = getMillis();
        millis += (long) getSeconds() * (long) DateTimeConstants.MILLIS_PER_SECOND;
        millis += (long) getMinutes() * (long) DateTimeConstants.MILLIS_PER_MINUTE;
        millis += (long) getHours() * (long) DateTimeConstants.MILLIS_PER_HOUR;
        millis += (long) getDays() * (long) DateTimeConstants.MILLIS_PER_DAY;
        millis += (long) getWeeks() * (long) DateTimeConstants.MILLIS_PER_WEEK;
        org.joda.time.Period result = new org.joda.time.Period( millis, type, ISOChronology.getInstanceUTC() );
        int years = getYears();
        int months = getMonths();
        if (years != 0 || months != 0) {
            years = FieldUtils.safeAdd( years, months / 12 );
            months = months % 12;
            if (years != 0) {
                result = result.withYears( years );
            }
            if (months != 0) {
                result = result.withMonths( months );
            }
        }
        return result;
    }

}
