package com.hyunsb.wanted.board;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class ListDTO {

        private Long id;
        private String title;
        private String content;
        private Long userId;

        public static ListDTO from(Board board) {
            return ListDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .userId(board.getUser().getId())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class DetailDTO {

        private Long id;
        private String title;
        private String content;
        private Long userId;
        private String userEmail;

        public static DetailDTO from(Board board) {
            return DetailDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .userId(board.getUser().getId())
                    .userEmail(board.getUser().getEmail())
                    .build();
        }
    }
}
