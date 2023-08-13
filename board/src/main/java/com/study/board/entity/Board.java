package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data //board.getTitle() 등의 함수 쓸 수 있게 해줌
public class Board {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    private Integer id;
    private String title;
    private String content;

}
