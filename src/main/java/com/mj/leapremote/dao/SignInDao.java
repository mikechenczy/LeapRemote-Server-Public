package com.mj.leapremote.dao;

import com.mj.leapremote.model.SignIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SignInDao {

    SignIn getByUserId(@Param("userId") String userId);

    List<SignIn> getAll();

    int insert(SignIn info);

    int delete(@Param("userId") String userId);

    int deleteAll();
}
