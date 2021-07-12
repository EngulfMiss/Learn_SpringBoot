package com.engulf.mapper;

import com.engulf.pojo.Books;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper这个注解表示了这是一个mybatis的mapper类
@Mapper
@Repository
public interface BookMapper {
    //查询所有书籍
    List<Books> selectBookList();

    //通过id查询书籍
    Books selectBookById(Integer id);

    //添加书籍
    int addBook(Books books);

    //修改书籍
    int updateBook(Books books);

    //删除书籍
    int delBook(Integer id);
}
