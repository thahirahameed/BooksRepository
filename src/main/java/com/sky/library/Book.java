package com.sky.library;

/*
 * Copyright © 2015 Sky plc All Rights reserved.
 * Please do not make your solution publicly available in any way e.g. post in forums or commit to GitHub.
 */

public class Book {
    private String reference;
    private String title;
    private String review;

    public Book(String reference, String title, String description) {
        this.reference = reference;
        this.title = title;
        this.review = description;
    }

    public Book(Builder builder) {
    }

    public String getReview() {
        return review;
    }

    public String getReference() {
        return reference;
    }

    public String getTitle() {
        return title;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String reference;
        private String title;
        private String review;


        public Builder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder review(final String review) {
            this.review = review;
            return this;
        }


        public Book build() {
            return new Book(this);
        }
    }
}
