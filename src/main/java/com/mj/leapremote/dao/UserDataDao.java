package com.mj.leapremote.dao;

import com.mj.leapremote.model.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDataDao {

    int insert(UserData userData);

    int update(UserData userData);

    int delete(@Param("deviceId") String deviceId);

    int deleteByUserId(@Param("userId") int userId);

    UserData getUserDataByDeviceId(@Param("deviceId") String deviceId);

    UserData getUserDataByUserId(@Param("userId") int userId);

    UserData getUserDataByConnectId(@Param("connectId") String connectId);

    List<UserData> getAll();
}