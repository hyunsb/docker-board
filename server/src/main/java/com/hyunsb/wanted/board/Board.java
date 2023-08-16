package com.hyunsb.wanted.board;

import com.hyunsb.wanted.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "board_tb")
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isCreatedBy(Long userId) {
        return Objects.equals(user.getId(), userId);
    }

    public void updateBy(String contentToUpdate, String titleToUpdate) {
        title = titleToUpdate;
        content = contentToUpdate;
    }
}
