package com.hyunsb.wanted.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunsb.wanted._core.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@WebMvcTest(controllers = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        })
class BoardControllerTest {

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("게시글 생성 컨트롤러 단위 테스트")
    class Save {

        @DisplayName("성공")
        @Test
        @WithMockUser
        void success_Test() throws Exception {
            // Given
            String uri = "/board";
            String title = "테스트용 게시글 제목";
            String content = "테스트용 게시글 내용";

            BoardRequest.SaveDTO saveDTO =
                    BoardRequest.SaveDTO.builder()
                            .title(title)
                            .content(content)
                            .build();

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .content(objectMapper.writeValueAsString(saveDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회 컨트롤러 단위 테스트")
    class GetAllList {

        @DisplayName("성공")
        @Test
        @WithMockUser
        void success_Test() throws Exception {
            // Given
            String uri = "/board";

            List<BoardResponse.ListDTO> mockList = List.of(
                    BoardResponse.ListDTO.builder().id(1L).title("title").content("content").userId(1L).build(),
                    BoardResponse.ListDTO.builder().id(21L).title("title").content("content").userId(2L).build(),
                    BoardResponse.ListDTO.builder().id(3L).title("title").content("content").userId(3L).build(),
                    BoardResponse.ListDTO.builder().id(4L).title("title").content("content").userId(4L).build(),
                    BoardResponse.ListDTO.builder().id(5L).title("title").content("content").userId(5L).build()
            );

            Pageable pageable = PageRequest.of(0, 5);
            Page<BoardResponse.ListDTO> mockPage = new PageImpl<>(mockList, pageable, 1);

            Mockito.when(boardService.getAllList(ArgumentMatchers.any(Pageable.class)))
                    .thenReturn(mockPage);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("특정 게시글 조회 컨트롤러 단위 테스트")
    class getBoardBy {

        @DisplayName("성공")
        @Test
        @WithMockUser
        void success_Test() throws Exception {
            // Given
            String uri = "/board/" + 1;

            BoardResponse.DetailDTO detailDTO =
                    BoardResponse.DetailDTO.builder()
                            .id(1L)
                            .title("title")
                            .content("content")
                            .userId(1L)
                            .build();

            Mockito.when(boardService.getBoardBy(ArgumentMatchers.anyLong()))
                    .thenReturn(detailDTO);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("특정 게시글 수정 컨트롤러 단위 테스트")
    class update {

        @DisplayName("성공")
        @Test
        @WithMockUser
        void success_Test() throws Exception {
            // Given
            String uri = "/board/" + 1;

            BoardRequest.updateDTO updateDTO =
                    BoardRequest.updateDTO.builder()
                            .title("title")
                            .content("content")
                            .build();

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.put(uri)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
    }

    @Nested
    @DisplayName("특정 게시글 삭제 컨트롤러 단위 테스트")
    class delete {

        @DisplayName("성공")
        @Test
        @WithMockUser
        void success_Test() throws Exception {
            // Given
            String uri = "/board/" + 1;

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
    }
}
