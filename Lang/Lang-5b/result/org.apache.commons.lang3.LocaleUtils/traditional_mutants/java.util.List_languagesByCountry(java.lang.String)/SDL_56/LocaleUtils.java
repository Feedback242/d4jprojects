// This is a mutant program.
// Author : ysma

package org.apache.commons.lang3;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LocaleUtils
{

    private static final java.util.concurrent.ConcurrentMap<String,List<Locale>> cLanguagesByCountry = new java.util.concurrent.ConcurrentHashMap<String,List<Locale>>();

    private static final java.util.concurrent.ConcurrentMap<String,List<Locale>> cCountriesByLanguage = new java.util.concurrent.ConcurrentHashMap<String,List<Locale>>();

    public LocaleUtils()
    {
        super();
    }

    public static  java.util.Locale toLocale( final java.lang.String str )
    {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len < 2) {
            throw new java.lang.IllegalArgumentException( "Invalid locale format: " + str );
        }
        final char ch0 = str.charAt( 0 );
        final char ch1 = str.charAt( 1 );
        if (!Character.isLowerCase( ch0 ) || !Character.isLowerCase( ch1 )) {
            throw new java.lang.IllegalArgumentException( "Invalid locale format: " + str );
        }
        if (len == 2) {
            return new java.util.Locale( str );
        }
        if (len < 5) {
            throw new java.lang.IllegalArgumentException( "Invalid locale format: " + str );
        }
        if (str.charAt( 2 ) != '_') {
            throw new java.lang.IllegalArgumentException( "Invalid locale format: " + str );
        }
        final char ch3 = str.charAt( 3 );
        if (ch3 == '_') {
            return new java.util.Locale( str.substring( 0, 2 ), "", str.substring( 4 ) );
        }
        final char ch4 = str.charAt( 4 );
        if (!Character.isUpperCase( ch3 ) || !Character.isUpperCase( ch4 )) {
            throw new java.lang.IllegalArgumentException( "Invalid locale format: " + str );
        }
        if (len == 5) {
            return new java.util.Locale( str.substring( 0, 2 ), str.substring( 3, 5 ) );
        }
        if (len < 7) {
            throw new java.lang.IllegalArgumentException( "Invalid locale format: " + str );
        }
        if (str.charAt( 5 ) != '_') {
            throw new java.lang.IllegalArgumentException( "Invalid locale format: " + str );
        }
        return new java.util.Locale( str.substring( 0, 2 ), str.substring( 3, 5 ), str.substring( 6 ) );
    }

    public static  java.util.List<Locale> localeLookupList( java.util.Locale locale )
    {
        return localeLookupList( locale, locale );
    }

    public static  java.util.List<Locale> localeLookupList( java.util.Locale locale, java.util.Locale defaultLocale )
    {
        java.util.List<Locale> list = new java.util.ArrayList<Locale>( 4 );
        if (locale != null) {
            list.add( locale );
            if (locale.getVariant().length() > 0) {
                list.add( new java.util.Locale( locale.getLanguage(), locale.getCountry() ) );
            }
            if (locale.getCountry().length() > 0) {
                list.add( new java.util.Locale( locale.getLanguage(), "" ) );
            }
            if (list.contains( defaultLocale ) == false) {
                list.add( defaultLocale );
            }
        }
        return Collections.unmodifiableList( list );
    }

    public static  java.util.List<Locale> availableLocaleList()
    {
        return SyncAvoid.AVAILABLE_LOCALE_LIST;
    }

    public static  java.util.Set<Locale> availableLocaleSet()
    {
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    public static  boolean isAvailableLocale( java.util.Locale locale )
    {
        return availableLocaleList().contains( locale );
    }

    public static  java.util.List<Locale> languagesByCountry( java.lang.String countryCode )
    {
        if (countryCode == null) {
            return Collections.emptyList();
        }
        java.util.List<Locale> langs = cLanguagesByCountry.get( countryCode );
        if (langs == null) {
            langs = new java.util.ArrayList<Locale>();
            java.util.List<Locale> locales = availableLocaleList();
            for (int i = 0; i < locales.size(); i++) {
                java.util.Locale locale = locales.get( i );
                if (countryCode.equals( locale.getCountry() ) && locale.getVariant().isEmpty()) {
                }
            }
            langs = Collections.unmodifiableList( langs );
            cLanguagesByCountry.putIfAbsent( countryCode, langs );
            langs = cLanguagesByCountry.get( countryCode );
        }
        return langs;
    }

    public static  java.util.List<Locale> countriesByLanguage( java.lang.String languageCode )
    {
        if (languageCode == null) {
            return Collections.emptyList();
        }
        java.util.List<Locale> countries = cCountriesByLanguage.get( languageCode );
        if (countries == null) {
            countries = new java.util.ArrayList<Locale>();
            java.util.List<Locale> locales = availableLocaleList();
            for (int i = 0; i < locales.size(); i++) {
                java.util.Locale locale = locales.get( i );
                if (languageCode.equals( locale.getLanguage() ) && locale.getCountry().length() != 0 && locale.getVariant().isEmpty()) {
                    countries.add( locale );
                }
            }
            countries = Collections.unmodifiableList( countries );
            cCountriesByLanguage.putIfAbsent( languageCode, countries );
            countries = cCountriesByLanguage.get( languageCode );
        }
        return countries;
    }

    static class SyncAvoid
    {

        private static final java.util.List<Locale> AVAILABLE_LOCALE_LIST;

        private static final java.util.Set<Locale> AVAILABLE_LOCALE_SET;

        static {
            java.util.List<Locale> list = new java.util.ArrayList<Locale>( Arrays.asList( Locale.getAvailableLocales() ) );
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList( list );
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet( new java.util.HashSet<Locale>( list ) );
        }

    }

}
