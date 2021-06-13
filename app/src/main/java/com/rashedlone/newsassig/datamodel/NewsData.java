package com.rashedlone.newsassig.datamodel;


import java.io.Serializable;

public class NewsData implements Serializable {


        private final String pid;
        private final String newsTitle;
        private final String newsImage;
        private final String newsDate;
        private final String newsSite;
        private final String newsSummary;
        private final String newsUrl;

        public NewsData(String pid, String newsTitle, String newsImage, String newsSite, String newsDate, String newsSummary, String newsUrl) {


            this.pid = pid;
            this.newsTitle = newsTitle;
            this.newsImage = newsImage;
            this.newsDate = newsDate;
            this.newsSite = newsSite;
            this.newsSummary = newsSummary;
            this.newsUrl = newsUrl;
        }

        public String getPid() {
            return pid;
        }

        public String getNewsTitle() {
            return newsTitle;
        }


        public String getNewsImage() {
            return newsImage;
        }

        public String getNewsDate() {
            return newsDate;
        }

        public String getNewsSite() {
            return newsSite;
        }

        public String getNewsSummary() {
            return newsSummary;
        }

        public String getNewsUrl() {
            return newsUrl;
        }

    }
