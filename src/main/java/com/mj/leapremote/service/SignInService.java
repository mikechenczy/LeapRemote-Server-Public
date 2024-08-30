package com.mj.leapremote.service;

import com.mj.leapremote.dao.SignInDao;
import com.mj.leapremote.model.SignIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service("signInService")
public class SignInService {

    @Autowired
    private SignInDao signInDao;

    public SignIn getByUserId(String userId) {
        return signInDao.getByUserId(userId);
    }

    public List<SignIn> getAll() {
        return signInDao.getAll();
    }

    public int insert(SignIn signIn) {
        return signInDao.insert(signIn);
    }

    public int delete(String userId) {
        return signInDao.delete(userId);
    }

    public int deleteAll() {
        return signInDao.deleteAll();
    }
}