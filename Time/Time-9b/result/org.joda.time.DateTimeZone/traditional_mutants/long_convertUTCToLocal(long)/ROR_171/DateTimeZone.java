// This is a mutant program.
// Author : ysma

package org.joda.time;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.FormatUtils;
import org.joda.time.tz.DefaultNameProvider;
import org.joda.time.tz.FixedDateTimeZone;
import org.joda.time.tz.NameProvider;
import org.joda.time.tz.Provider;
import org.joda.time.tz.UTCProvider;
import org.joda.time.tz.ZoneInfoProvider;


public abstract class DateTimeZone implements java.io.Serializable
{

    private static final long serialVersionUID = 5546345482340108586L;

    public static final org.joda.time.DateTimeZone UTC = new org.joda.time.tz.FixedDateTimeZone( "UTC", "UTC", 0, 0 );

    private static org.joda.time.tz.Provider cProvider;

    private static org.joda.time.tz.NameProvider cNameProvider;

    private static java.util.Set<String> cAvailableIDs;

    private static volatile org.joda.time.DateTimeZone cDefault;

    private static org.joda.time.format.DateTimeFormatter cOffsetFormatter;

    private static java.util.Map<String,SoftReference<DateTimeZone>> iFixedOffsetCache;

    private static java.util.Map<String,String> cZoneIdConversion;

    static {
        setProvider0( null );
        setNameProvider0( null );
    }

    public static  org.joda.time.DateTimeZone getDefault()
    {
        org.joda.time.DateTimeZone zone = cDefault;
        if (zone == null) {
            synchronized (org.joda.time.DateTimeZone.class)
{
                zone = cDefault;
                if (zone == null) {
                    org.joda.time.DateTimeZone temp = null;
                    try {
                        try {
                            java.lang.String id = System.getProperty( "user.timezone" );
                            if (id != null) {
                                temp = forID( id );
                            }
                        } catch ( java.lang.RuntimeException ex ) {
                        }
                        if (temp == null) {
                            temp = forTimeZone( TimeZone.getDefault() );
                        }
                    } catch ( java.lang.IllegalArgumentException ex ) {
                    }
                    if (temp == null) {
                        temp = UTC;
                    }
                    cDefault = zone = temp;
                }
            }
        }
        return zone;
    }

    public static  void setDefault( org.joda.time.DateTimeZone zone )
        throws java.lang.SecurityException
    {
        java.lang.SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission( new org.joda.time.JodaTimePermission( "DateTimeZone.setDefault" ) );
        }
        if (zone == null) {
            throw new java.lang.IllegalArgumentException( "The datetime zone must not be null" );
        }
        synchronized (org.joda.time.DateTimeZone.class)
{
            cDefault = zone;
        }
    }

    public static  org.joda.time.DateTimeZone forID( java.lang.String id )
    {
        if (id == null) {
            return getDefault();
        }
        if (id.equals( "UTC" )) {
            return DateTimeZone.UTC;
        }
        org.joda.time.DateTimeZone zone = cProvider.getZone( id );
        if (zone != null) {
            return zone;
        }
        if (id.startsWith( "+" ) || id.startsWith( "-" )) {
            int offset = parseOffset( id );
            if (offset == 0L) {
                return DateTimeZone.UTC;
            } else {
                id = printOffset( offset );
                return fixedOffsetZone( id, offset );
            }
        }
        throw new java.lang.IllegalArgumentException( "The datetime zone id '" + id + "' is not recognised" );
    }

    public static  org.joda.time.DateTimeZone forOffsetHours( int hoursOffset )
        throws java.lang.IllegalArgumentException
    {
        return forOffsetHoursMinutes( hoursOffset, 0 );
    }

    public static  org.joda.time.DateTimeZone forOffsetHoursMinutes( int hoursOffset, int minutesOffset )
        throws java.lang.IllegalArgumentException
    {
        if (hoursOffset == 0 && minutesOffset == 0) {
            return DateTimeZone.UTC;
        }
        if (minutesOffset < 0 || minutesOffset > 59) {
            throw new java.lang.IllegalArgumentException( "Minutes out of range: " + minutesOffset );
        }
        int offset = 0;
        try {
            int hoursInMinutes = FieldUtils.safeMultiply( hoursOffset, 60 );
            if (hoursInMinutes < 0) {
                minutesOffset = FieldUtils.safeAdd( hoursInMinutes, -minutesOffset );
            } else {
                minutesOffset = FieldUtils.safeAdd( hoursInMinutes, minutesOffset );
            }
            offset = FieldUtils.safeMultiply( minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE );
        } catch ( java.lang.ArithmeticException ex ) {
            throw new java.lang.IllegalArgumentException( "Offset is too large" );
        }
        return forOffsetMillis( offset );
    }

    public static  org.joda.time.DateTimeZone forOffsetMillis( int millisOffset )
    {
        java.lang.String id = printOffset( millisOffset );
        return fixedOffsetZone( id, millisOffset );
    }

    public static  org.joda.time.DateTimeZone forTimeZone( java.util.TimeZone zone )
    {
        if (zone == null) {
            return getDefault();
        }
        final java.lang.String id = zone.getID();
        if (id.equals( "UTC" )) {
            return DateTimeZone.UTC;
        }
        org.joda.time.DateTimeZone dtz = null;
        java.lang.String convId = getConvertedId( id );
        if (convId != null) {
            dtz = cProvider.getZone( convId );
        }
        if (dtz == null) {
            dtz = cProvider.getZone( id );
        }
        if (dtz != null) {
            return dtz;
        }
        if (convId == null) {
            convId = zone.getID();
            if (convId.startsWith( "GMT+" ) || convId.startsWith( "GMT-" )) {
                convId = convId.substring( 3 );
                int offset = parseOffset( convId );
                if (offset == 0L) {
                    return DateTimeZone.UTC;
                } else {
                    convId = printOffset( offset );
                    return fixedOffsetZone( convId, offset );
                }
            }
        }
        throw new java.lang.IllegalArgumentException( "The datetime zone id '" + id + "' is not recognised" );
    }

    private static synchronized  org.joda.time.DateTimeZone fixedOffsetZone( java.lang.String id, int offset )
    {
        if (offset == 0) {
            return DateTimeZone.UTC;
        }
        if (iFixedOffsetCache == null) {
            iFixedOffsetCache = new java.util.HashMap<String,SoftReference<DateTimeZone>>();
        }
        org.joda.time.DateTimeZone zone;
        java.lang.ref.Reference<DateTimeZone> ref = iFixedOffsetCache.get( id );
        if (ref != null) {
            zone = ref.get();
            if (zone != null) {
                return zone;
            }
        }
        zone = new org.joda.time.tz.FixedDateTimeZone( id, null, offset, offset );
        iFixedOffsetCache.put( id, new java.lang.ref.SoftReference<DateTimeZone>( zone ) );
        return zone;
    }

    public static  java.util.Set<String> getAvailableIDs()
    {
        return cAvailableIDs;
    }

    public static  org.joda.time.tz.Provider getProvider()
    {
        return cProvider;
    }

    public static  void setProvider( org.joda.time.tz.Provider provider )
        throws java.lang.SecurityException
    {
        java.lang.SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission( new org.joda.time.JodaTimePermission( "DateTimeZone.setProvider" ) );
        }
        setProvider0( provider );
    }

    private static  void setProvider0( org.joda.time.tz.Provider provider )
    {
        if (provider == null) {
            provider = getDefaultProvider();
        }
        java.util.Set<String> ids = provider.getAvailableIDs();
        if (ids == null || ids.size() == 0) {
            throw new java.lang.IllegalArgumentException( "The provider doesn't have any available ids" );
        }
        if (!ids.contains( "UTC" )) {
            throw new java.lang.IllegalArgumentException( "The provider doesn't support UTC" );
        }
        if (!UTC.equals( provider.getZone( "UTC" ) )) {
            throw new java.lang.IllegalArgumentException( "Invalid UTC zone provided" );
        }
        cProvider = provider;
        cAvailableIDs = ids;
    }

    private static  org.joda.time.tz.Provider getDefaultProvider()
    {
        org.joda.time.tz.Provider provider = null;
        try {
            java.lang.String providerClass = System.getProperty( "org.joda.time.DateTimeZone.Provider" );
            if (providerClass != null) {
                try {
                    provider = (org.joda.time.tz.Provider) Class.forName( providerClass ).newInstance();
                } catch ( java.lang.Exception ex ) {
                    java.lang.Thread thread = Thread.currentThread();
                    thread.getThreadGroup().uncaughtException( thread, ex );
                }
            }
        } catch ( java.lang.SecurityException ex ) {
        }
        if (provider == null) {
            try {
                provider = new org.joda.time.tz.ZoneInfoProvider( "org/joda/time/tz/data" );
            } catch ( java.lang.Exception ex ) {
                java.lang.Thread thread = Thread.currentThread();
                thread.getThreadGroup().uncaughtException( thread, ex );
            }
        }
        if (provider == null) {
            provider = new org.joda.time.tz.UTCProvider();
        }
        return provider;
    }

    public static  org.joda.time.tz.NameProvider getNameProvider()
    {
        return cNameProvider;
    }

    public static  void setNameProvider( org.joda.time.tz.NameProvider nameProvider )
        throws java.lang.SecurityException
    {
        java.lang.SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission( new org.joda.time.JodaTimePermission( "DateTimeZone.setNameProvider" ) );
        }
        setNameProvider0( nameProvider );
    }

    private static  void setNameProvider0( org.joda.time.tz.NameProvider nameProvider )
    {
        if (nameProvider == null) {
            nameProvider = getDefaultNameProvider();
        }
        cNameProvider = nameProvider;
    }

    private static  org.joda.time.tz.NameProvider getDefaultNameProvider()
    {
        org.joda.time.tz.NameProvider nameProvider = null;
        try {
            java.lang.String providerClass = System.getProperty( "org.joda.time.DateTimeZone.NameProvider" );
            if (providerClass != null) {
                try {
                    nameProvider = (org.joda.time.tz.NameProvider) Class.forName( providerClass ).newInstance();
                } catch ( java.lang.Exception ex ) {
                    java.lang.Thread thread = Thread.currentThread();
                    thread.getThreadGroup().uncaughtException( thread, ex );
                }
            }
        } catch ( java.lang.SecurityException ex ) {
        }
        if (nameProvider == null) {
            nameProvider = new org.joda.time.tz.DefaultNameProvider();
        }
        return nameProvider;
    }

    private static synchronized  java.lang.String getConvertedId( java.lang.String id )
    {
        java.util.Map<String,String> map = cZoneIdConversion;
        if (map == null) {
            map = new java.util.HashMap<String,String>();
            map.put( "GMT", "UTC" );
            map.put( "WET", "WET" );
            map.put( "CET", "CET" );
            map.put( "MET", "CET" );
            map.put( "ECT", "CET" );
            map.put( "EET", "EET" );
            map.put( "MIT", "Pacific/Apia" );
            map.put( "HST", "Pacific/Honolulu" );
            map.put( "AST", "America/Anchorage" );
            map.put( "PST", "America/Los_Angeles" );
            map.put( "MST", "America/Denver" );
            map.put( "PNT", "America/Phoenix" );
            map.put( "CST", "America/Chicago" );
            map.put( "EST", "America/New_York" );
            map.put( "IET", "America/Indiana/Indianapolis" );
            map.put( "PRT", "America/Puerto_Rico" );
            map.put( "CNT", "America/St_Johns" );
            map.put( "AGT", "America/Argentina/Buenos_Aires" );
            map.put( "BET", "America/Sao_Paulo" );
            map.put( "ART", "Africa/Cairo" );
            map.put( "CAT", "Africa/Harare" );
            map.put( "EAT", "Africa/Addis_Ababa" );
            map.put( "NET", "Asia/Yerevan" );
            map.put( "PLT", "Asia/Karachi" );
            map.put( "IST", "Asia/Kolkata" );
            map.put( "BST", "Asia/Dhaka" );
            map.put( "VST", "Asia/Ho_Chi_Minh" );
            map.put( "CTT", "Asia/Shanghai" );
            map.put( "JST", "Asia/Tokyo" );
            map.put( "ACT", "Australia/Darwin" );
            map.put( "AET", "Australia/Sydney" );
            map.put( "SST", "Pacific/Guadalcanal" );
            map.put( "NST", "Pacific/Auckland" );
            cZoneIdConversion = map;
        }
        return map.get( id );
    }

    private static  int parseOffset( java.lang.String str )
    {
        org.joda.time.Chronology chrono = new org.joda.time.chrono.BaseChronology(){
            public  org.joda.time.DateTimeZone getZone()
            {
                return null;
            }

            public  org.joda.time.Chronology withUTC()
            {
                return this;
            }

            public  org.joda.time.Chronology withZone( org.joda.time.DateTimeZone zone )
            {
                return this;
            }

            public  java.lang.String toString()
            {
                return getClass().getName();
            }
        };
        return -((int) offsetFormatter().withChronology( chrono ).parseMillis( str ));
    }

    private static  java.lang.String printOffset( int offset )
    {
        java.lang.StringBuffer buf = new java.lang.StringBuffer();
        if (offset >= 0) {
            buf.append( '+' );
        } else {
            buf.append( '-' );
            offset = -offset;
        }
        int hours = offset / DateTimeConstants.MILLIS_PER_HOUR;
        FormatUtils.appendPaddedInteger( buf, hours, 2 );
        offset -= hours * (int) DateTimeConstants.MILLIS_PER_HOUR;
        int minutes = offset / DateTimeConstants.MILLIS_PER_MINUTE;
        buf.append( ':' );
        FormatUtils.appendPaddedInteger( buf, minutes, 2 );
        offset -= minutes * DateTimeConstants.MILLIS_PER_MINUTE;
        if (offset == 0) {
            return buf.toString();
        }
        int seconds = offset / DateTimeConstants.MILLIS_PER_SECOND;
        buf.append( ':' );
        FormatUtils.appendPaddedInteger( buf, seconds, 2 );
        offset -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
        if (offset == 0) {
            return buf.toString();
        }
        buf.append( '.' );
        FormatUtils.appendPaddedInteger( buf, offset, 3 );
        return buf.toString();
    }

    private static synchronized  org.joda.time.format.DateTimeFormatter offsetFormatter()
    {
        if (cOffsetFormatter == null) {
            cOffsetFormatter = (new org.joda.time.format.DateTimeFormatterBuilder()).appendTimeZoneOffset( null, true, 2, 4 ).toFormatter();
        }
        return cOffsetFormatter;
    }

    private final java.lang.String iID;

    protected DateTimeZone( java.lang.String id )
    {
        if (id == null) {
            throw new java.lang.IllegalArgumentException( "Id must not be null" );
        }
        iID = id;
    }

    public final  java.lang.String getID()
    {
        return iID;
    }

    public abstract  java.lang.String getNameKey( long instant );

    public final  java.lang.String getShortName( long instant )
    {
        return getShortName( instant, null );
    }

    public  java.lang.String getShortName( long instant, java.util.Locale locale )
    {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        java.lang.String nameKey = getNameKey( instant );
        if (nameKey == null) {
            return iID;
        }
        java.lang.String name = cNameProvider.getShortName( locale, iID, nameKey );
        if (name != null) {
            return name;
        }
        return printOffset( getOffset( instant ) );
    }

    public final  java.lang.String getName( long instant )
    {
        return getName( instant, null );
    }

    public  java.lang.String getName( long instant, java.util.Locale locale )
    {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        java.lang.String nameKey = getNameKey( instant );
        if (nameKey == null) {
            return iID;
        }
        java.lang.String name = cNameProvider.getName( locale, iID, nameKey );
        if (name != null) {
            return name;
        }
        return printOffset( getOffset( instant ) );
    }

    public abstract  int getOffset( long instant );

    public final  int getOffset( org.joda.time.ReadableInstant instant )
    {
        if (instant == null) {
            return getOffset( DateTimeUtils.currentTimeMillis() );
        }
        return getOffset( instant.getMillis() );
    }

    public abstract  int getStandardOffset( long instant );

    public  boolean isStandardOffset( long instant )
    {
        return getOffset( instant ) == getStandardOffset( instant );
    }

    public  int getOffsetFromLocal( long instantLocal )
    {
        final int offsetLocal = getOffset( instantLocal );
        final long instantAdjusted = instantLocal - offsetLocal;
        final int offsetAdjusted = getOffset( instantAdjusted );
        if (offsetLocal != offsetAdjusted) {
            if (offsetLocal - offsetAdjusted < 0) {
                long nextLocal = nextTransition( instantAdjusted );
                long nextAdjusted = nextTransition( instantLocal - offsetAdjusted );
                if (nextLocal != nextAdjusted) {
                    return offsetLocal;
                }
            }
        } else {
            if (offsetLocal >= 0) {
                long prev = previousTransition( instantAdjusted );
                if (prev < instantAdjusted) {
                    int offsetPrev = getOffset( prev );
                    int diff = offsetPrev - offsetLocal;
                    if (instantAdjusted - prev <= diff) {
                        return offsetPrev;
                    }
                }
            }
        }
        return offsetAdjusted;
    }

    public  long convertUTCToLocal( long instantUTC )
    {
        int offset = getOffset( instantUTC );
        long instantLocal = instantUTC + offset;
        if ((instantUTC ^ instantLocal) > 0 && (instantUTC ^ offset) >= 0) {
            throw new java.lang.ArithmeticException( "Adding time zone offset caused overflow" );
        }
        return instantLocal;
    }

    public  long convertLocalToUTC( long instantLocal, boolean strict, long originalInstantUTC )
    {
        int offsetOriginal = getOffset( originalInstantUTC );
        long instantUTC = instantLocal - offsetOriginal;
        int offsetLocalFromOriginal = getOffset( instantUTC );
        if (offsetLocalFromOriginal == offsetOriginal) {
            return instantUTC;
        }
        return convertLocalToUTC( instantLocal, strict );
    }

    public  long convertLocalToUTC( long instantLocal, boolean strict )
    {
        int offsetLocal = getOffset( instantLocal );
        int offset = getOffset( instantLocal - offsetLocal );
        if (offsetLocal != offset) {
            if (strict || offsetLocal < 0) {
                long nextLocal = nextTransition( instantLocal - offsetLocal );
                if (nextLocal == instantLocal - offsetLocal) {
                    nextLocal = Long.MAX_VALUE;
                }
                long nextAdjusted = nextTransition( instantLocal - offset );
                if (nextAdjusted == instantLocal - offset) {
                    nextAdjusted = Long.MAX_VALUE;
                }
                if (nextLocal != nextAdjusted) {
                    if (strict) {
                        throw new org.joda.time.IllegalInstantException( instantLocal, getID() );
                    } else {
                        offset = offsetLocal;
                    }
                }
            }
        }
        long instantUTC = instantLocal - offset;
        if ((instantLocal ^ instantUTC) < 0 && (instantLocal ^ offset) < 0) {
            throw new java.lang.ArithmeticException( "Subtracting time zone offset caused overflow" );
        }
        return instantUTC;
    }

    public  long getMillisKeepLocal( org.joda.time.DateTimeZone newZone, long oldInstant )
    {
        if (newZone == null) {
            newZone = DateTimeZone.getDefault();
        }
        if (newZone == this) {
            return oldInstant;
        }
        long instantLocal = convertUTCToLocal( oldInstant );
        return newZone.convertLocalToUTC( instantLocal, false, oldInstant );
    }

    public  boolean isLocalDateTimeGap( org.joda.time.LocalDateTime localDateTime )
    {
        if (isFixed()) {
            return false;
        }
        try {
            localDateTime.toDateTime( this );
            return false;
        } catch ( org.joda.time.IllegalInstantException ex ) {
            return true;
        }
    }

    public  long adjustOffset( long instant, boolean earlierOrLater )
    {
        long instantBefore = instant - 3 * DateTimeConstants.MILLIS_PER_HOUR;
        long instantAfter = instant + 3 * DateTimeConstants.MILLIS_PER_HOUR;
        long offsetBefore = getOffset( instantBefore );
        long offsetAfter = getOffset( instantAfter );
        if (offsetBefore <= offsetAfter) {
            return instant;
        }
        long diff = offsetBefore - offsetAfter;
        long transition = nextTransition( instantBefore );
        long overlapStart = transition - diff;
        long overlapEnd = transition + diff;
        if (instant < overlapStart || instant >= overlapEnd) {
            return instant;
        }
        long afterStart = instant - overlapStart;
        if (afterStart >= diff) {
            return earlierOrLater ? instant : instant - diff;
        } else {
            return earlierOrLater ? instant + diff : instant;
        }
    }

    public abstract  boolean isFixed();

    public abstract  long nextTransition( long instant );

    public abstract  long previousTransition( long instant );

    public  java.util.TimeZone toTimeZone()
    {
        return java.util.TimeZone.getTimeZone( iID );
    }

    public abstract  boolean equals( java.lang.Object object );

    public  int hashCode()
    {
        return 57 + getID().hashCode();
    }

    public  java.lang.String toString()
    {
        return getID();
    }

    protected  java.lang.Object writeReplace()
        throws java.io.ObjectStreamException
    {
        return new org.joda.time.DateTimeZone.Stub( iID );
    }

    private static final class Stub implements java.io.Serializable
    {

        private static final long serialVersionUID = -6471952376487863581L;

        private transient java.lang.String iID;

        Stub( java.lang.String id )
        {
            iID = id;
        }

        private  void writeObject( java.io.ObjectOutputStream out )
                throws java.io.IOException
        {
            out.writeUTF( iID );
        }

        private  void readObject( java.io.ObjectInputStream in )
                throws java.io.IOException
        {
            iID = in.readUTF();
        }

        private  java.lang.Object readResolve()
                throws java.io.ObjectStreamException
        {
            return forID( iID );
        }

    }

}
