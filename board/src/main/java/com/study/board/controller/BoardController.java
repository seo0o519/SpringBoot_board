package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;
    @GetMapping("/board/write") //localhost:8080/board/write 주소로 접근하면 아래 내용을 실행
    public String BoardWriteForm(){
        return "boardwrite"; //boardwrite 라는 html 파일로 연결
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{
        boardService.write(board, file);
        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page=0, size=15, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> list = boardService.boardlist(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardList"; //boardlist.html로 연결
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id){ //localhost:8080/board/view?id=1로 접속하면 파라미터 id에 1이 들어감 (query string)
        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete") ////localhost:8080/board/delete?id=1로 접속하면 파라미터 id에 1이 들어감 (query string)
    public String boardDelete(Integer id, Model model){
        boardService.boardDelete(id);
        model.addAttribute("message","글 삭제가 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";
    }

    @GetMapping("/board/modify/{id}") //path variable
    public String boardModify(@PathVariable("id") Integer id, Model model){
        model.addAttribute("board",boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception{ //PathVariable의 id를 Integer id에 담아줌
        Board boardTemp = boardService.boardView(id); //id를 통해 기존의 객체 가져옴
        boardTemp.setTitle(board.getTitle()); //기존 객체의 title을 새로 입력한 것으로 덮음
        boardTemp.setContent(board.getContent()); //기존 객체의 content를 새로 입력한 것으로 덮음

        boardService.write(boardTemp, file);
        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";
    }
}
