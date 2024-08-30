package com.mj.leapremote.service;

import com.mj.leapremote.dao.InfoDao;
import com.mj.leapremote.model.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service("infoService")
public class InfoService {

    @Autowired
    private InfoDao infoDao;

    public Info getByDescription(String description) {
        return infoDao.getByDescription(description);
    }

    public List<Info> getAll() {
        return infoDao.getAll();
    }

    public int insert(Info info) {
        return infoDao.insert(info);
    }

    public int delete(String description) {
        return infoDao.delete(description);
    }
}