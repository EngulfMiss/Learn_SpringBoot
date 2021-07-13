package com.engulf.mapper;

import com.engulf.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    //按用户名查询用户
    public abstract User getUserByName(String name);
}
