package com.tvstack.tvinput.entity;

import java.util.List;

public class IPTVEntity {


    /**
     * name : &privé HD
     * logo : null
     * url : https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/index.m3u8
     * category : Movies
     * languages : [{"code":"eng","name":"English"}]
     * countries : [{"code":"af","name":"Afghanistan"},{"code":"bd","name":"Bangladesh"},{"code":"bt","name":"Bhutan"},{"code":"in","name":"India"},{"code":"lk","name":"Sri Lanka"},{"code":"mv","name":"Maldives"},{"code":"np","name":"Nepal"},{"code":"pk","name":"Pakistan"}]
     * tvg : {"id":null,"name":null,"url":null}
     */

    public String name;
    public String logo;
    public String url;
    public String category;
    public List<LanguagesBean> languages;
    public List<CountriesBean> countries;
    public TvgBean tvg;

    public static class TvgBean {
        /**
         * id : null
         * name : null
         * url : null
         */

        public String id;
        public String name;
        public String url;
    }

    public static class LanguagesBean {
        /**
         * code : eng
         * name : English
         */

        public String code;
        public String name;
    }

    public static class CountriesBean {
        /**
         * code : af
         * name : Afghanistan
         */

        public String code;
        public String name;
    }
}
