package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<Object> save(
            @RequestBody BoardRequest.SaveDTO saveDTO,
            HttpServletRequest request) {
        log.info("POST /board : " + saveDTO);
        Long userId = (Long) request.getAttribute(JwtProvider.REQUEST);

        boardService.save(saveDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/board")
    public ResponseEntity<Page<BoardResponse.ListDTO>> getAllList(
            @PageableDefault(size = 5) Pageable pageable) {
        log.info("GET /board : " + pageable);

        Page<BoardResponse.ListDTO> allList = boardService.getAllList(pageable);
        return ResponseEntity.ok(allList);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<BoardResponse.DetailDTO> getDetail(@PathVariable Long boardId) {
        log.info("GET /board/" + boardId);

        BoardResponse.DetailDTO boardDetail = boardService.getBoardBy(boardId);
        return ResponseEntity.ok(boardDetail);
    }

    @PutMapping("/board/{boardId}")
    public ResponseEntity<BoardResponse.DetailDTO> update(
            @PathVariable Long boardId,
            @RequestBody BoardRequest.updateDTO updateDTO,
            HttpServletRequest request) {
        log.info("PUT /board/" + boardId + ", " + updateDTO);
        Long userId = (Long) request.getAttribute(JwtProvider.REQUEST);

        boardService.updateBy(boardId, updateDTO, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long boardId,
            HttpServletRequest request) {
        log.info("DELETE /board/" + boardId);
        Long userId = (Long) request.getAttribute(JwtProvider.REQUEST);

        boardService.deleteBy(boardId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
