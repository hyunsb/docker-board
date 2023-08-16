package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.*;
import com.hyunsb.wanted.user.User;
import com.hyunsb.wanted.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class BoardIntegrationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        boardRepository.deleteAll();

        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();
        User persistenceUser = userRepository.save(user);

        List<Board> mockList = List.of(
                Board.builder().id(1L).title("title").content("content").user(persistenceUser).build(),
                Board.builder().id(2L).title("title").content("content").user(persistenceUser).build(),
                Board.builder().id(3L).title("title").content("content").user(persistenceUser).build(),
                Board.builder().id(4L).title("title").content("content").user(persistenceUser).build(),
                Board.builder().id(5L).title("title").content("content").user(persistenceUser).build()
        );
        boardRepository.saveAll(mockList);
    }

    @Transactional
    @Nested
    @DisplayName("게시글 생성 통합 테스트")
    class Save {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            List<User> allUsers = userRepository.findAll();
            Long userId = allUsers.get(0).getId();

            BoardRequest.SaveDTO saveDTO =
                    BoardRequest.SaveDTO.builder()
                            .title("testTitle")
                            .content("testContent")
                            .build();

            // When
            // Then
            boardService.save(saveDTO, userId);
        }

        @DisplayName("실패 - 유효하지 않은 userId")
        @Test
        void failure_Test_InvalidUserId() {
            // Given
            Long userId = 2L;
            BoardRequest.SaveDTO saveDTO =
                    BoardRequest.SaveDTO.builder()
                            .title("testTitle")
                            .content("testContent")
                            .build();
            // When
            // Then
            Assertions.assertThrows(BoardSaveFailureException.class,
                    () -> boardService.save(saveDTO, userId));
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회 통합 테스트")
    class GetAllList {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Pageable pageable = PageRequest.of(0, 3);

            // When
            Page<BoardResponse.ListDTO> actual = boardService.getAllList(pageable);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(3, actual.getSize()),
                    () -> Assertions.assertEquals(2, actual.getTotalPages()),
                    () -> Assertions.assertEquals(5, actual.getTotalElements())
            );
        }

        @DisplayName("실패 - 페이징 사이즈 범위 초과")
        @Test
        void failure_Test_ExceededPagingSize() {
            // Given
            Pageable pageable = PageRequest.of(0, 200);

            // When
            // Then
            Assertions.assertThrows(ExceededMaximumPageSizeException.class, () ->
                    boardService.getAllList(pageable));
        }
    }

    @Nested
    @DisplayName("특정 게시글 조회 통합 테스트")
    class getBoardBy {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Board board = boardRepository.findAll().get(0);
            Long boardId = board.getId();

            // When
            BoardResponse.DetailDTO actual = boardService.getBoardBy(boardId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(boardId, actual.getId()),
                    () -> Assertions.assertEquals("title", actual.getTitle()),
                    () -> Assertions.assertEquals("content", actual.getContent())
            );
        }

        @DisplayName("실패 - 유효하지 않은 게시글 아이디")
        @Test
        void failure_Test_InvalidBoardId() {
            // Given
            boardRepository.deleteAll();
            Long boardId = 1L;

            // When
            // Then
            Assertions.assertThrows(BoardNotFoundException.class, () ->
                    boardService.getBoardBy(boardId));
        }
    }

    @Nested
    @DisplayName("특정 게시글 수정 통합 테스트")
    class update {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Board board = boardRepository.findAll().get(0);

            Long userId = board.getUser().getId();
            Long boardId = board.getId();
            BoardRequest.updateDTO updateDTO =
                    BoardRequest.updateDTO.builder()
                            .title("changeTitle")
                            .content("changeContent")
                            .build();

            // When
            boardService.updateBy(boardId, updateDTO, userId);
            Board actual = boardRepository.findById(boardId).get();

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals("changeTitle", actual.getTitle()),
                    () -> Assertions.assertEquals("changeContent", actual.getContent())
            );
        }

        @DisplayName("실패 - 유효하지 않은 게시글 번호")
        @Test
        void failure_Test_InvalidBoardId() {
            // Given
            Long userId = 1L;
            Long boardId = 200L;
            BoardRequest.updateDTO updateDTO =
                    BoardRequest.updateDTO.builder()
                            .title("changeTitle")
                            .content("changeContent")
                            .build();

            // When
            // Then
            Assertions.assertThrows(BoardNotFoundException.class, () ->
                    boardService.updateBy(boardId, updateDTO, userId));
        }

        @DisplayName("실패 - 게시글 작성자와 수정 요청자가 일치하지 않음")
        @Test
        void failure_Test_InvalidUserId() {
            // Given
            Board board = boardRepository.findAll().get(0);

            Long userId = board.getUser().getId() + 1;
            Long boardId = board.getId();
            BoardRequest.updateDTO updateDTO =
                    BoardRequest.updateDTO.builder()
                            .title("changeTitle")
                            .content("changeContent")
                            .build();

            // When
            // Then
            Assertions.assertThrows(BoardUpdateFailureException.class, () ->
                    boardService.updateBy(boardId, updateDTO, userId));
        }
    }

    @Nested
    @DisplayName("특정 게시글 삭제 통합 테스트")
    class delete {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Board board = boardRepository.findAll().get(0);

            Long userId = board.getUser().getId();
            Long boardId = board.getId();

            // When
            boardService.deleteBy(boardId, userId);
            Optional<Board> boardOptional = boardRepository.findById(boardId);

            // Then
            Assertions.assertFalse(boardOptional.isPresent());
        }

        @DisplayName("실패 - 유효하지 않은 게시글 번호")
        @Test
        void failure_Test_InvalidBoardId() {
            // Given
            List<Board> boardList = boardRepository.findAll();
            Board board = boardList.get(boardList.size() - 1);

            Long userId = board.getUser().getId();
            Long boardId = board.getId() + 1;

            // When
            // Then
            Assertions.assertThrows(BoardNotFoundException.class, () ->
                    boardService.deleteBy(boardId, userId));
        }

        @DisplayName("실패 - 게시글 작성자와 삭제 요청자가 일치하지 않음")
        @Test
        void failure_Test_InvalidUserId() {
            // Given
            Board board = boardRepository.findAll().get(0);

            Long userId = board.getUser().getId() + 1;
            Long boardId = board.getId();

            // When
            // Then
            Assertions.assertThrows(BoardDeleteFailureException.class, () ->
                    boardService.deleteBy(boardId, userId));
        }
    }
}
