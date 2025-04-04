package com.librarymanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long userId;
    private Long bookId;
    private String reviewText;
}
