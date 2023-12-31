// This is a mutant program.
// Author : ysma

package org.joda.time;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseLocal;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.field.AbstractReadableInstantFieldProperty;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


public final class LocalDate extends org.joda.time.base.BaseLocal implements org.joda.time.ReadablePartial, java.io.Serializable
{

    private static final long serialVersionUID = -8775358157899L;

    private static final int YEAR = 0;

    private static final int MONTH_OF_YEAR = 1;

    private static final int DAY_OF_MONTH = 2;

    private static final java.util.Set<DurationFieldType> DATE_DURATION_TYPES = new java.util.HashSet<DurationFieldType>();

    static {
        DATE_DURATION_TYPES.add( DurationFieldType.days() );
        DATE_DURATION_TYPES.add( DurationFieldType.weeks() );
        DATE_DURATION_TYPES.add( DurationFieldType.months() );
        DATE_DURATION_TYPES.add( DurationFieldType.weekyears() );
        DATE_DURATION_TYPES.add( DurationFieldType.years() );
        DATE_DURATION_TYPES.add( DurationFieldType.centuries() );
        DATE_DURATION_TYPES.add( DurationFieldType.eras() );
    }

    private final long iLocalMillis;

    private final org.joda.time.Chronology iChronology;

    private transient volatile int iHash;

    public static  org.joda.time.LocalDate now()
    {
        return new org.joda.time.LocalDate();
    }

    public static  org.joda.time.LocalDate now( org.joda.time.DateTimeZone zone )
    {
        if (zone == null) {
            throw new java.lang.NullPointerException( "Zone must not be null" );
        }
        return new org.joda.time.LocalDate( zone );
    }

    public static  org.joda.time.LocalDate now( org.joda.time.Chronology chronology )
    {
        if (chronology == null) {
            throw new java.lang.NullPointerException( "Chronology must not be null" );
        }
        return new org.joda.time.LocalDate( chronology );
    }

    public static  org.joda.time.LocalDate parse( java.lang.String str )
    {
        return parse( str, ISODateTimeFormat.localDateParser() );
    }

    public static  org.joda.time.LocalDate parse( java.lang.String str, org.joda.time.format.DateTimeFormatter formatter )
    {
        return formatter.parseLocalDate( str );
    }

    public static  org.joda.time.LocalDate fromCalendarFields( java.util.Calendar calendar )
    {
        if (calendar == null) {
            throw new java.lang.IllegalArgumentException( "The calendar must not be null" );
        }
        int yearOfEra = calendar.get( Calendar.YEAR );
        return new org.joda.time.LocalDate( yearOfEra, calendar.get( Calendar.MONTH ) + 1, calendar.get( Calendar.DAY_OF_MONTH ) );
    }

    public static  org.joda.time.LocalDate fromDateFields( java.util.Date date )
    {
        if (date == null) {
            throw new java.lang.IllegalArgumentException( "The date must not be null" );
        }
        return new org.joda.time.LocalDate( date.getYear() + 1900, date.getMonth() + 1, date.getDate() );
    }

    public LocalDate()
    {
        this( DateTimeUtils.currentTimeMillis(), ISOChronology.getInstance() );
    }

    public LocalDate( org.joda.time.DateTimeZone zone )
    {
        this( DateTimeUtils.currentTimeMillis(), ISOChronology.getInstance( zone ) );
    }

    public LocalDate( org.joda.time.Chronology chronology )
    {
        this( DateTimeUtils.currentTimeMillis(), chronology );
    }

    public LocalDate( long instant )
    {
        this( instant, ISOChronology.getInstance() );
    }

    public LocalDate( long instant, org.joda.time.DateTimeZone zone )
    {
        this( instant, ISOChronology.getInstance( zone ) );
    }

    public LocalDate( long instant, org.joda.time.Chronology chronology )
    {
        chronology = DateTimeUtils.getChronology( chronology );
        long localMillis = chronology.getZone().getMillisKeepLocal( DateTimeZone.UTC, instant );
        chronology = chronology.withUTC();
        iLocalMillis = chronology.dayOfMonth().roundFloor( localMillis );
        iChronology = chronology;
    }

    public LocalDate( java.lang.Object instant )
    {
        this( instant, (org.joda.time.Chronology) null );
    }

    public LocalDate( java.lang.Object instant, org.joda.time.DateTimeZone zone )
    {
        org.joda.time.convert.PartialConverter converter = ConverterManager.getInstance().getPartialConverter( instant );
        org.joda.time.Chronology chronology = converter.getChronology( instant, zone );
        chronology = DateTimeUtils.getChronology( chronology );
        iChronology = chronology.withUTC();
        int[] values = converter.getPartialValues( this, instant, chronology, ISODateTimeFormat.localDateParser() );
        iLocalMillis = iChronology.getDateTimeMillis( values[0], values[1], values[2], 0 );
    }

    public LocalDate( java.lang.Object instant, org.joda.time.Chronology chronology )
    {
        org.joda.time.convert.PartialConverter converter = ConverterManager.getInstance().getPartialConverter( instant );
        chronology = converter.getChronology( instant, chronology );
        chronology = DateTimeUtils.getChronology( chronology );
        iChronology = chronology.withUTC();
        int[] values = converter.getPartialValues( this, instant, chronology, ISODateTimeFormat.localDateParser() );
        iLocalMillis = iChronology.getDateTimeMillis( values[0], values[1], values[2], 0 );
    }

    public LocalDate( int year, int monthOfYear, int dayOfMonth )
    {
        this( year, monthOfYear, dayOfMonth, ISOChronology.getInstanceUTC() );
    }

    public LocalDate( int year, int monthOfYear, int dayOfMonth, org.joda.time.Chronology chronology )
    {
        super();
        chronology = DateTimeUtils.getChronology( chronology ).withUTC();
        long instant = chronology.getDateTimeMillis( year, monthOfYear, dayOfMonth, 0 );
        iChronology = chronology;
        iLocalMillis = instant;
    }

    private  java.lang.Object readResolve()
    {
        if (iChronology == null) {
            return new org.joda.time.LocalDate( iLocalMillis, ISOChronology.getInstanceUTC() );
        }
        if (DateTimeZone.UTC.equals( iChronology.getZone() ) == false) {
            return new org.joda.time.LocalDate( iLocalMillis, iChronology.withUTC() );
        }
        return this;
    }

    public  int size()
    {
        return 3;
    }

    protected  org.joda.time.DateTimeField getField( int index, org.joda.time.Chronology chrono )
    {
        switch (index) {
        case YEAR :
            return chrono.year();

        case MONTH_OF_YEAR :
            return chrono.monthOfYear();

        case DAY_OF_MONTH :
            return chrono.dayOfMonth();

        default  :
            throw new java.lang.IndexOutOfBoundsException( "Invalid index: " + index );

        }
    }

    public  int getValue( int index )
    {
        switch (index) {
        case YEAR :
            return getChronology().year().get( getLocalMillis() );

        case MONTH_OF_YEAR :
            return getChronology().monthOfYear().get( getLocalMillis() );

        case DAY_OF_MONTH :
            return getChronology().dayOfMonth().get( getLocalMillis() );

        default  :
            throw new java.lang.IndexOutOfBoundsException( "Invalid index: " + index );

        }
    }

    public  int get( org.joda.time.DateTimeFieldType fieldType )
    {
        if (fieldType == null) {
            throw new java.lang.IllegalArgumentException( "The DateTimeFieldType must not be null" );
        }
        if (isSupported( fieldType ) == false) {
            throw new java.lang.IllegalArgumentException( "Field '" + fieldType + "' is not supported" );
        }
        return fieldType.getField( getChronology() ).get( getLocalMillis() );
    }

    public  boolean isSupported( org.joda.time.DateTimeFieldType type )
    {
        if (type == null) {
            return false;
        }
        org.joda.time.DurationFieldType durType = type.getDurationType();
        if (DATE_DURATION_TYPES.contains( durType ) || durType.getField( getChronology() ).getUnitMillis() >= getChronology().days().getUnitMillis()) {
            return type.getField( getChronology() ).isSupported();
        }
        return false;
    }

    public  boolean isSupported( org.joda.time.DurationFieldType type )
    {
        if (type == null) {
            return false;
        }
        org.joda.time.DurationField field = type.getField( getChronology() );
        if (DATE_DURATION_TYPES.contains( type ) || field.getUnitMillis() >= getChronology().days().getUnitMillis()) {
            return field.isSupported();
        }
        return false;
    }

    protected  long getLocalMillis()
    {
        return iLocalMillis;
    }

    public  org.joda.time.Chronology getChronology()
    {
        return iChronology;
    }

    public  boolean equals( java.lang.Object partial )
    {
        if (this == partial) {
            return true;
        }
        if (partial instanceof org.joda.time.LocalDate) {
            org.joda.time.LocalDate other = (org.joda.time.LocalDate) partial;
            if (iChronology.equals( other.iChronology )) {
                return iLocalMillis == other.iLocalMillis;
            }
        }
        return super.equals( partial );
    }

    public  int hashCode()
    {
        int hash = iHash;
        if (hash < 0) {
            hash = iHash = super.hashCode();
        }
        return hash;
    }

    public  int compareTo( org.joda.time.ReadablePartial partial )
    {
        if (this == partial) {
            return 0;
        }
        if (partial instanceof org.joda.time.LocalDate) {
            org.joda.time.LocalDate other = (org.joda.time.LocalDate) partial;
            if (iChronology.equals( other.iChronology )) {
                return iLocalMillis < other.iLocalMillis ? -1 : iLocalMillis == other.iLocalMillis ? 0 : 1;
            }
        }
        return super.compareTo( partial );
    }

    public  org.joda.time.DateTime toDateTimeAtStartOfDay()
    {
        return toDateTimeAtStartOfDay( null );
    }

    public  org.joda.time.DateTime toDateTimeAtStartOfDay( org.joda.time.DateTimeZone zone )
    {
        zone = DateTimeUtils.getZone( zone );
        org.joda.time.Chronology chrono = getChronology().withZone( zone );
        long localMillis = getLocalMillis() + 6L * DateTimeConstants.MILLIS_PER_HOUR;
        long instant = zone.convertLocalToUTC( localMillis, false );
        instant = chrono.dayOfMonth().roundFloor( instant );
        return new org.joda.time.DateTime( instant, chrono );
    }

    public  org.joda.time.DateTime toDateTimeAtMidnight()
    {
        return toDateTimeAtMidnight( null );
    }

    public  org.joda.time.DateTime toDateTimeAtMidnight( org.joda.time.DateTimeZone zone )
    {
        zone = DateTimeUtils.getZone( zone );
        org.joda.time.Chronology chrono = getChronology().withZone( zone );
        return new org.joda.time.DateTime( getYear(), getMonthOfYear(), getDayOfMonth(), 0, 0, 0, 0, chrono );
    }

    public  org.joda.time.DateTime toDateTimeAtCurrentTime()
    {
        return toDateTimeAtCurrentTime( null );
    }

    public  org.joda.time.DateTime toDateTimeAtCurrentTime( org.joda.time.DateTimeZone zone )
    {
        zone = DateTimeUtils.getZone( zone );
        org.joda.time.Chronology chrono = getChronology().withZone( zone );
        long instantMillis = DateTimeUtils.currentTimeMillis();
        long resolved = chrono.set( this, instantMillis );
        return new org.joda.time.DateTime( resolved, chrono );
    }

    public  org.joda.time.DateMidnight toDateMidnight()
    {
        return toDateMidnight( null );
    }

    public  org.joda.time.DateMidnight toDateMidnight( org.joda.time.DateTimeZone zone )
    {
        zone = DateTimeUtils.getZone( zone );
        org.joda.time.Chronology chrono = getChronology().withZone( zone );
        return new org.joda.time.DateMidnight( getYear(), getMonthOfYear(), getDayOfMonth(), chrono );
    }

    public  org.joda.time.LocalDateTime toLocalDateTime( org.joda.time.LocalTime time )
    {
        if (time == null) {
            throw new java.lang.IllegalArgumentException( "The time must not be null" );
        }
        if (getChronology() != time.getChronology()) {
            throw new java.lang.IllegalArgumentException( "The chronology of the time does not match" );
        }
        long localMillis = getLocalMillis() + time.getLocalMillis();
        return new org.joda.time.LocalDateTime( localMillis, getChronology() );
    }

    public  org.joda.time.DateTime toDateTime( org.joda.time.LocalTime time )
    {
        return toDateTime( time, null );
    }

    public  org.joda.time.DateTime toDateTime( org.joda.time.LocalTime time, org.joda.time.DateTimeZone zone )
    {
        if (time != null && getChronology() != time.getChronology()) {
            throw new java.lang.IllegalArgumentException( "The chronology of the time does not match" );
        }
        org.joda.time.Chronology chrono = getChronology().withZone( zone );
        long instant = DateTimeUtils.currentTimeMillis();
        instant = chrono.set( this, instant );
        if (time != null) {
            instant = chrono.set( time, instant );
        }
        return new org.joda.time.DateTime( instant, chrono );
    }

    public  org.joda.time.Interval toInterval()
    {
        return toInterval( null );
    }

    public  org.joda.time.Interval toInterval( org.joda.time.DateTimeZone zone )
    {
        zone = DateTimeUtils.getZone( zone );
        org.joda.time.DateTime start = toDateTimeAtStartOfDay( zone );
        org.joda.time.DateTime end = plusDays( 1 ).toDateTimeAtStartOfDay( zone );
        return new org.joda.time.Interval( start, end );
    }

    public  java.util.Date toDate()
    {
        int dom = getDayOfMonth();
        java.util.Date date = new java.util.Date( getYear() - 1900, getMonthOfYear() - 1, dom );
        org.joda.time.LocalDate check = LocalDate.fromDateFields( date );
        if (check.isBefore( this )) {
            while (check.equals( this ) == false) {
                date.setTime( date.getTime() + 3600000 );
                check = LocalDate.fromDateFields( date );
            }
            while (date.getDate() == dom) {
                date.setTime( date.getTime() - 1000 );
            }
            date.setTime( date.getTime() + 1000 );
        } else {
            if (check.equals( this )) {
                java.util.Date earlier = new java.util.Date( date.getTime() - TimeZone.getDefault().getDSTSavings() );
                if (earlier.getDate() == dom) {
                    date = earlier;
                }
            }
        }
        return date;
    }

     org.joda.time.LocalDate withLocalMillis( long newMillis )
    {
        newMillis = iChronology.dayOfMonth().roundFloor( newMillis );
        return newMillis == getLocalMillis() ? this : new org.joda.time.LocalDate( newMillis, getChronology() );
    }

    public  org.joda.time.LocalDate withFields( org.joda.time.ReadablePartial partial )
    {
        if (partial == null) {
            return this;
        }
        return withLocalMillis( getChronology().set( partial, getLocalMillis() ) );
    }

    public  org.joda.time.LocalDate withField( org.joda.time.DateTimeFieldType fieldType, int value )
    {
        if (fieldType == null) {
            throw new java.lang.IllegalArgumentException( "Field must not be null" );
        }
        if (isSupported( fieldType ) == false) {
            throw new java.lang.IllegalArgumentException( "Field '" + fieldType + "' is not supported" );
        }
        long instant = fieldType.getField( getChronology() ).set( getLocalMillis(), value );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate withFieldAdded( org.joda.time.DurationFieldType fieldType, int amount )
    {
        if (fieldType == null) {
            throw new java.lang.IllegalArgumentException( "Field must not be null" );
        }
        if (isSupported( fieldType ) == false) {
            throw new java.lang.IllegalArgumentException( "Field '" + fieldType + "' is not supported" );
        }
        if (amount == 0) {
            return this;
        }
        long instant = fieldType.getField( getChronology() ).add( getLocalMillis(), amount );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate withPeriodAdded( org.joda.time.ReadablePeriod period, int scalar )
    {
        if (period == null || scalar == 0) {
            return this;
        }
        long instant = getLocalMillis();
        org.joda.time.Chronology chrono = getChronology();
        for (int i = 0; i < period.size(); i++) {
            long value = FieldUtils.safeMultiply( period.getValue( i ), scalar );
            org.joda.time.DurationFieldType type = period.getFieldType( i );
            if (isSupported( type )) {
                instant = type.getField( chrono ).add( instant, value );
            }
        }
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate plus( org.joda.time.ReadablePeriod period )
    {
        return withPeriodAdded( period, 1 );
    }

    public  org.joda.time.LocalDate plusYears( int years )
    {
        if (years == 0) {
            return this;
        }
        long instant = getChronology().years().add( getLocalMillis(), years );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate plusMonths( int months )
    {
        if (months == 0) {
            return this;
        }
        long instant = getChronology().months().add( getLocalMillis(), months );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate plusWeeks( int weeks )
    {
        if (weeks == 0) {
            return this;
        }
        long instant = getChronology().weeks().add( getLocalMillis(), weeks );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate plusDays( int days )
    {
        if (days == 0) {
            return this;
        }
        long instant = getChronology().days().add( getLocalMillis(), days );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate minus( org.joda.time.ReadablePeriod period )
    {
        return withPeriodAdded( period, -1 );
    }

    public  org.joda.time.LocalDate minusYears( int years )
    {
        if (years == 0) {
            return this;
        }
        long instant = getChronology().years().subtract( getLocalMillis(), years );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate minusMonths( int months )
    {
        if (months == 0) {
            return this;
        }
        long instant = getChronology().months().subtract( getLocalMillis(), months );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate minusWeeks( int weeks )
    {
        if (weeks == 0) {
            return this;
        }
        long instant = getChronology().weeks().subtract( getLocalMillis(), weeks );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate minusDays( int days )
    {
        if (days == 0) {
            return this;
        }
        long instant = getChronology().days().subtract( getLocalMillis(), days );
        return withLocalMillis( instant );
    }

    public  org.joda.time.LocalDate.Property property( org.joda.time.DateTimeFieldType fieldType )
    {
        if (fieldType == null) {
            throw new java.lang.IllegalArgumentException( "The DateTimeFieldType must not be null" );
        }
        if (isSupported( fieldType ) == false) {
            throw new java.lang.IllegalArgumentException( "Field '" + fieldType + "' is not supported" );
        }
        return new org.joda.time.LocalDate.Property( this, fieldType.getField( getChronology() ) );
    }

    public  int getEra()
    {
        return getChronology().era().get( getLocalMillis() );
    }

    public  int getCenturyOfEra()
    {
        return getChronology().centuryOfEra().get( getLocalMillis() );
    }

    public  int getYearOfEra()
    {
        return getChronology().yearOfEra().get( getLocalMillis() );
    }

    public  int getYearOfCentury()
    {
        return getChronology().yearOfCentury().get( getLocalMillis() );
    }

    public  int getYear()
    {
        return getChronology().year().get( getLocalMillis() );
    }

    public  int getWeekyear()
    {
        return getChronology().weekyear().get( getLocalMillis() );
    }

    public  int getMonthOfYear()
    {
        return getChronology().monthOfYear().get( getLocalMillis() );
    }

    public  int getWeekOfWeekyear()
    {
        return getChronology().weekOfWeekyear().get( getLocalMillis() );
    }

    public  int getDayOfYear()
    {
        return getChronology().dayOfYear().get( getLocalMillis() );
    }

    public  int getDayOfMonth()
    {
        return getChronology().dayOfMonth().get( getLocalMillis() );
    }

    public  int getDayOfWeek()
    {
        return getChronology().dayOfWeek().get( getLocalMillis() );
    }

    public  org.joda.time.LocalDate withEra( int era )
    {
        return withLocalMillis( getChronology().era().set( getLocalMillis(), era ) );
    }

    public  org.joda.time.LocalDate withCenturyOfEra( int centuryOfEra )
    {
        return withLocalMillis( getChronology().centuryOfEra().set( getLocalMillis(), centuryOfEra ) );
    }

    public  org.joda.time.LocalDate withYearOfEra( int yearOfEra )
    {
        return withLocalMillis( getChronology().yearOfEra().set( getLocalMillis(), yearOfEra ) );
    }

    public  org.joda.time.LocalDate withYearOfCentury( int yearOfCentury )
    {
        return withLocalMillis( getChronology().yearOfCentury().set( getLocalMillis(), yearOfCentury ) );
    }

    public  org.joda.time.LocalDate withYear( int year )
    {
        return withLocalMillis( getChronology().year().set( getLocalMillis(), year ) );
    }

    public  org.joda.time.LocalDate withWeekyear( int weekyear )
    {
        return withLocalMillis( getChronology().weekyear().set( getLocalMillis(), weekyear ) );
    }

    public  org.joda.time.LocalDate withMonthOfYear( int monthOfYear )
    {
        return withLocalMillis( getChronology().monthOfYear().set( getLocalMillis(), monthOfYear ) );
    }

    public  org.joda.time.LocalDate withWeekOfWeekyear( int weekOfWeekyear )
    {
        return withLocalMillis( getChronology().weekOfWeekyear().set( getLocalMillis(), weekOfWeekyear ) );
    }

    public  org.joda.time.LocalDate withDayOfYear( int dayOfYear )
    {
        return withLocalMillis( getChronology().dayOfYear().set( getLocalMillis(), dayOfYear ) );
    }

    public  org.joda.time.LocalDate withDayOfMonth( int dayOfMonth )
    {
        return withLocalMillis( getChronology().dayOfMonth().set( getLocalMillis(), dayOfMonth ) );
    }

    public  org.joda.time.LocalDate withDayOfWeek( int dayOfWeek )
    {
        return withLocalMillis( getChronology().dayOfWeek().set( getLocalMillis(), dayOfWeek ) );
    }

    public  org.joda.time.LocalDate.Property era()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().era() );
    }

    public  org.joda.time.LocalDate.Property centuryOfEra()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().centuryOfEra() );
    }

    public  org.joda.time.LocalDate.Property yearOfCentury()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().yearOfCentury() );
    }

    public  org.joda.time.LocalDate.Property yearOfEra()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().yearOfEra() );
    }

    public  org.joda.time.LocalDate.Property year()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().year() );
    }

    public  org.joda.time.LocalDate.Property weekyear()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().weekyear() );
    }

    public  org.joda.time.LocalDate.Property monthOfYear()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().monthOfYear() );
    }

    public  org.joda.time.LocalDate.Property weekOfWeekyear()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().weekOfWeekyear() );
    }

    public  org.joda.time.LocalDate.Property dayOfYear()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().dayOfYear() );
    }

    public  org.joda.time.LocalDate.Property dayOfMonth()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().dayOfMonth() );
    }

    public  org.joda.time.LocalDate.Property dayOfWeek()
    {
        return new org.joda.time.LocalDate.Property( this, getChronology().dayOfWeek() );
    }

    public  java.lang.String toString()
    {
        return ISODateTimeFormat.date().print( this );
    }

    public  java.lang.String toString( java.lang.String pattern )
    {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern( pattern ).print( this );
    }

    public  java.lang.String toString( java.lang.String pattern, java.util.Locale locale )
        throws java.lang.IllegalArgumentException
    {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern( pattern ).withLocale( locale ).print( this );
    }

    public static final class Property extends org.joda.time.field.AbstractReadableInstantFieldProperty
    {

        private static final long serialVersionUID = -3193829732634L;

        private transient org.joda.time.LocalDate iInstant;

        private transient org.joda.time.DateTimeField iField;

        Property( org.joda.time.LocalDate instant, org.joda.time.DateTimeField field )
        {
            super();
            iInstant = instant;
            iField = field;
        }

        private  void writeObject( java.io.ObjectOutputStream oos )
                throws java.io.IOException
        {
            oos.writeObject( iInstant );
            oos.writeObject( iField.getType() );
        }

        private  void readObject( java.io.ObjectInputStream oos )
                throws java.io.IOException, java.lang.ClassNotFoundException
        {
            iInstant = (org.joda.time.LocalDate) oos.readObject();
            org.joda.time.DateTimeFieldType type = (org.joda.time.DateTimeFieldType) oos.readObject();
            iField = type.getField( iInstant.getChronology() );
        }

        public  org.joda.time.DateTimeField getField()
        {
            return iField;
        }

        protected  long getMillis()
        {
            return iInstant.getLocalMillis();
        }

        protected  org.joda.time.Chronology getChronology()
        {
            return iInstant.getChronology();
        }

        public  org.joda.time.LocalDate getLocalDate()
        {
            return iInstant;
        }

        public  org.joda.time.LocalDate addToCopy( int value )
        {
            return iInstant.withLocalMillis( iField.add( iInstant.getLocalMillis(), value ) );
        }

        public  org.joda.time.LocalDate addWrapFieldToCopy( int value )
        {
            return iInstant.withLocalMillis( iField.addWrapField( iInstant.getLocalMillis(), value ) );
        }

        public  org.joda.time.LocalDate setCopy( int value )
        {
            return iInstant.withLocalMillis( iField.set( iInstant.getLocalMillis(), value ) );
        }

        public  org.joda.time.LocalDate setCopy( java.lang.String text, java.util.Locale locale )
        {
            return iInstant.withLocalMillis( iField.set( iInstant.getLocalMillis(), text, locale ) );
        }

        public  org.joda.time.LocalDate setCopy( java.lang.String text )
        {
            return setCopy( text, null );
        }

        public  org.joda.time.LocalDate withMaximumValue()
        {
            return setCopy( getMaximumValue() );
        }

        public  org.joda.time.LocalDate withMinimumValue()
        {
            return setCopy( getMinimumValue() );
        }

        public  org.joda.time.LocalDate roundFloorCopy()
        {
            return iInstant.withLocalMillis( iField.roundFloor( iInstant.getLocalMillis() ) );
        }

        public  org.joda.time.LocalDate roundCeilingCopy()
        {
            return iInstant.withLocalMillis( iField.roundCeiling( iInstant.getLocalMillis() ) );
        }

        public  org.joda.time.LocalDate roundHalfFloorCopy()
        {
            return iInstant.withLocalMillis( iField.roundHalfFloor( iInstant.getLocalMillis() ) );
        }

        public  org.joda.time.LocalDate roundHalfCeilingCopy()
        {
            return iInstant.withLocalMillis( iField.roundHalfCeiling( iInstant.getLocalMillis() ) );
        }

        public  org.joda.time.LocalDate roundHalfEvenCopy()
        {
            return iInstant.withLocalMillis( iField.roundHalfEven( iInstant.getLocalMillis() ) );
        }

    }

}
