package com.engulf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JDBCController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //查询数据库的所有信息 万能MAP
    @GetMapping("/books")
    public List<Map<String,Object>> bookList(){
        String sql = "select * from books";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
    }

    @GetMapping("/addBook")
    public String addBook(){
        String sql = "insert into books(bookID,bookName,bookCounts,detail) values(6,'JavaWeb',20,'javaweb入门')";
        jdbcTemplate.update(sql);
        return "Add,OK";
    }

    @GetMapping("/updateBook/{id}")
    public String updateBook(@PathVariable("id") int id){
        String sql = "update books set bookName = ?,bookCounts = ? where bookID = "+id;
        Object[] obj = new Object[2];
        obj[0] = "JavaWeb";
        obj[1] = "10";
        jdbcTemplate.update(sql,obj);
        return "update,OK";
    }

    @GetMapping("/delBook/{id}")
    public String delBook(@PathVariable("id") int id){
        String sql = "delete from books where bookID = ?";
        jdbcTemplate.update(sql,id);
        return "Del,OK";
    }
}
