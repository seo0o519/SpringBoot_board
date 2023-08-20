package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 파일 저장할 경로
                             //이 프로젝트의 경로를 가져옴
        UUID uuid = UUID.randomUUID(); //파일에 붙일 랜덤 이름 생성
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName); // 파일 객체 생성하여 경로 지정, 이름 지정
        file.transferTo(saveFile);

        board.setFilename(fileName); //파일의 이름
        board.setFilepath("/files/" + fileName); //파일의 경로 DB에 추가
        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public Page<Board> boardlist(Pageable pageable){

        return boardRepository.findAll(pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id){

        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }
}
