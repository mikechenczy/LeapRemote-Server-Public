package com.mj.leapremote.dao;

import com.mj.leapremote.model.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InfoDao {

    Info getByDescription(@Param("description") String description);

    List<Info> getAll();

    int insert(Info info);

    int delete(@Param("description") String description);
}
