package com.sky.library;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class BookServiceImplementationTest {

    private static final String BOOK_REFERENCE_PREFIX = "BOOK-";
    private static final String BOOK_REFERENCE_PREFIX_INVALID = "INVALID-";
    private static final String THE_GRUFFALO_REFERENCE = BOOK_REFERENCE_PREFIX + "GRUFF472";
    private static final String THE_GRUFFALO_REFERENCE_UNKNOWN = BOOK_REFERENCE_PREFIX + "999";
    private static final String THE_GRUFFALO_REFERENCE_INVALID = BOOK_REFERENCE_PREFIX_INVALID + "GRUFF472";
    private static final String THE_GRUFFALO_TITLE = "The Gruffalo";
    private static final String THE_GRUFFALO_REVIEW = "A mouse taking a walk in the woods";

    private BookService bookService;
    private BookRepository bookRepository;

    @Before
    public void setup() {
        this.bookRepository = new BookRepositoryStub();
        this.bookService = new BookServiceImplementation(bookRepository);
    }

    @Test
    public void shouldRetrieveBook() throws BookNotFoundException {
        final Book result = bookService.retrieveBook(THE_GRUFFALO_REFERENCE);

        assertThat(result).isNotNull();
        assertThat(result.getReference()).isEqualTo(THE_GRUFFALO_REFERENCE);
        assertThat(result.getTitle()).isEqualTo(THE_GRUFFALO_TITLE);
        assertThat(result.getReview()).isEqualTo(THE_GRUFFALO_REVIEW);
    }

    @Test
    public void shouldValidateBookReference() {
        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> bookService.retrieveBook(THE_GRUFFALO_REFERENCE_INVALID))
                .withMessage(String.format("invalid book reference value: %s", THE_GRUFFALO_REFERENCE_INVALID))
                .withStackTraceContaining("BookNotFoundException")
                .withNoCause();
    }

    @Test
    public void shouldThrowExceptionIfBookReferenceDoesNotExist() {
        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> bookService.retrieveBook(THE_GRUFFALO_REFERENCE_UNKNOWN))
                .withMessage(String.format("book with reference value: %s does not exist", THE_GRUFFALO_REFERENCE_UNKNOWN))
                .withStackTraceContaining("BookNotFoundException")
                .withNoCause();
    }

}

