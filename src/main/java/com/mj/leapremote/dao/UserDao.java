package com.mj.leapremote.dao;

import com.mj.leapremote.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User user);

    int insertSelective(User record);

    int update(User user);

    User getUserByDeviceId(@Param("deviceId") String deviceId);

    User getUserByUserId(@Param("userId") int userId);

    User getUserByUsername(@Param("username") String username);

    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    User getUserByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    User getUserByEmail(@Param("email") String email);

    List<User> getAll();

    int deleteByUserId(@Param("userId") int userId);
}