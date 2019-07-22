package com.advanceSearch.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

    @Select("select password from users where user='admin'")
    String getAdminPassword();
}
