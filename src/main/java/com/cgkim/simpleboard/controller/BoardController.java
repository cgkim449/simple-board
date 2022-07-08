package com.cgkim.simpleboard.controller;

import com.cgkim.simpleboard.service.BoardService;
import com.cgkim.simpleboard.util.FileHandler;
import com.cgkim.simpleboard.vo.board.BoardListResponse;
import com.cgkim.simpleboard.vo.board.BoardSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
@Controller
public class BoardController {

    private final BoardService boardService;

    private final FileHandler fileHandler;

    @GetMapping
    public String viewBoardList(Model model, BoardSearchRequest boardSearchRequest) {

        List<BoardListResponse> boardList = boardService.viewBoardList(boardSearchRequest);
        long boardTotalCount = boardService.getTotalCount(boardSearchRequest);

        model.addAttribute("boardList", boardList);
        model.addAttribute("boardTotalCount", boardTotalCount);

        return "list";
    }
}
