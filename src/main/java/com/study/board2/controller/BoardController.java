package com.study.board2.controller;

import com.study.board2.entity.Board2;
import com.study.board2.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") // localhost:8080/board/write
    public String boardWriteFrom(){

        return "board_write";
    }

    @PostMapping("/board/store")
    public String boardStore(Board2 board2, Model model, MultipartFile file) throws Exception {

        boardService.write(board2, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword){

        Page<Board2> list = null;

        if(searchKeyword == null){
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); // 매쓰 클래스의 민

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board_list";
    }

    @GetMapping("/board/detail")
    public String boardDetail(Model model, Integer id) {

        model.addAttribute("detail", boardService.boardDetail(id));
        return "board_detail";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {

        boardService.boardDelete(id);

        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {

        model.addAttribute("board",boardService.boardDetail(id));

        return "board_modify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board2 board2, MultipartFile file) throws Exception { // board2 에는 수정할 때 넘어온 데이터

        Board2 boardTemp = boardService.boardDetail(id);
        boardTemp.setTitle(board2.getTitle());
        boardTemp.setContent(board2.getContent());

        boardService.write(boardTemp, file);

        return "redirect:/board/detail?id="+id;
    }
}
