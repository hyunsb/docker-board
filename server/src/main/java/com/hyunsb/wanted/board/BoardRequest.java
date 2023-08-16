package com.hyunsb.wanted.board;

import com.hyunsb.wanted.user.User;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardRequest {

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class SaveDTO {

        private String title;
        private String content;

        public Board toEntityWith(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class updateDTO {

        private String title;
        private String content;

        public Board toEntityWith(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }
    }
}
