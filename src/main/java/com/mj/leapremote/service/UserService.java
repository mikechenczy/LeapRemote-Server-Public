package com.mj.leapremote.service;

import com.mj.leapremote.dao.UserDao;
import com.mj.leapremote.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserService {

    @Autowired
    private UserDao userdao;

    public User getUserByUserId(int userId) {
        return userdao.getUserByUserId(userId);
    }

    public User getUserByUsername(String username) {
        return userdao.getUserByUsername(username);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userdao.getUserByUsernameAndPassword(username, password);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        return userdao.getUserByEmailAndPassword(email, password);
    }

    public User getUserByEmail(String email) {
        return userdao.getUserByEmail(email);
    }

    public int insert(User user) {
        return userdao.insert(user);
    }

    public int update(User user) {
        return userdao.update(user);
    }

    public List<User> getAll() {
        return userdao.getAll();
    }

    public int delete(int userId) {
        return userdao.deleteByUserId(userId);
    }

    public User getUserByDeviceId(String deviceId) {
        return userdao.getUserByDeviceId(deviceId);
    }
}