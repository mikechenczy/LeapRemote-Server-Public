package com.mj.leapremote.dao;

import com.mj.leapremote.model.LoginInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginInfoDao {

    LoginInfo getByUserId(@Param("userId") int userId);

    List<LoginInfo> getAll();

    int insert(LoginInfo loginInfo);

    int delete(@Param("userId") int userId);

    int deleteAll();

    int update(LoginInfo loginInfo);
}
