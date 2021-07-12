package com.engulf.controller;

import com.engulf.mapper.BookMapper;
import com.engulf.pojo.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/books")
    public List<Books> selectBookList(){
        List<Books> books = bookMapper.selectBookList();
        return books;
    }

    @GetMapping("/books/{id}")
    public Books selectBookListById(@PathVariable("id") Integer id){
        Books books = bookMapper.selectBookById(id);
        return books;
    }


}
