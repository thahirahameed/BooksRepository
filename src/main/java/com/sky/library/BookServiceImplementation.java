package com.sky.library;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BookServiceImplementation implements BookService {

        private static final String BOOK_REFERENCE_PREFIX = "BOOK-";
        private static final String BOOK_SUMMARY_REGEX = "\\W+";
        private static final int BOOK_SUMMARY_MAX_WORDS = 9;

        private final BookRepository bookRepository;

        public BookServiceImplementation(final BookRepository bookRepository) {
            this.bookRepository = bookRepository;
        }

        @Override
        public Book retrieveBook(final String bookReference) throws BookNotFoundException {
            if(!validateBookReference(bookReference)) {
                throw new BookNotFoundException(String.format("invalid book reference value: %s", bookReference));
            }

            Optional<Book> optionalBook = Optional.ofNullable(bookRepository.retrieveBook(bookReference));

            optionalBook.orElseThrow(() -> new BookNotFoundException(String.format(
                    "book with reference value: %s does not exist", bookReference)));

            Book book = optionalBook.get();

            return Book.builder().reference(book.getReference())
                    .title(book.getTitle())
                    .review(book.getReview())
                    .build();
        }

        @Override
        public String getBookSummary(final String bookReference) throws BookNotFoundException {
            Book book = retrieveBook(bookReference);

            return buildBookSummary(book);
        }

        private String buildBookSummary(final Book book) {
            StringBuffer sb = new StringBuffer("[");
            sb.append(book.getReference());
            sb.append("] ");
            sb.append(book.getTitle());
            sb.append(" - ");

            if(!book.getReview().isEmpty()) {
                String[] reviewWords = book.getReview().split(BOOK_SUMMARY_REGEX);

                List<String> wordsList = Arrays.asList(reviewWords).stream()
                        .filter(BookServiceImplementation::isNotBlank)
                        .collect(Collectors.toList());

                sb.append((wordsList.size() > BOOK_SUMMARY_MAX_WORDS) ?
                        (Joiner.on(" ").join(wordsList.subList(0, BOOK_SUMMARY_MAX_WORDS))) + "..." :
                        book.getReview());
            }

            return sb.toString();
        }

        private boolean validateBookReference(final String bookReference) {
            return isNotBlank(bookReference) &&
                    bookReference.trim().startsWith(BOOK_REFERENCE_PREFIX);
        }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
}
