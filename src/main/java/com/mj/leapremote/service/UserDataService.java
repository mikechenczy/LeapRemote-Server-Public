package com.mj.leapremote.service;

import com.mj.leapremote.dao.UserDataDao;
import com.mj.leapremote.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Random;

@Service("userDataService")
public class UserDataService {

    @Autowired
    private UserDataDao userDataDao;

    public UserData getUserDataByDeviceId(String deviceId) {
        return userDataDao.getUserDataByDeviceId(deviceId);
    }

    public UserData getUserDataByUserId(int userId) {
        return userDataDao.getUserDataByUserId(userId);
    }

    public UserData getUserDataByConnectId(String connectId) {
        return userDataDao.getUserDataByConnectId(connectId);
    }

    public int delete(String deviceId) {
        return userDataDao.delete(deviceId);
    }

    public int insert(UserData userData) {
        return userDataDao.insert(userData);
    }

    public int update(UserData userData) {
        return userDataDao.update(userData);
    }

    public List<UserData> getAll() {
        return userDataDao.getAll();
    }

    public int deleteByUserId(int userId) {
        return userDataDao.deleteByUserId(userId);
    }

    public int insertOrUpdate(UserData userData) {
        if(getUserDataByDeviceId(userData.getDeviceId())!=null) {
            return update(userData);
        }
        return insert(userData);
    }

    public synchronized String generateConnectId() {
        return generateConnectId(getAll());
    }

    private synchronized String generateConnectId(List<UserData> userDataList) {
        String id = String.valueOf(10000+new Random().nextInt(90000));
        for(UserData userData : userDataList) {
            if(userData.getConnectId().equals(id))
                return generateConnectId(userDataList);
        }
        return id;
    }

    public synchronized String generateConnectPin() {
        return generateConnectPin(getAll());
    }

    private synchronized String generateConnectPin(List<UserData> userDataList) {
        String id = String.valueOf(10000+new Random().nextInt(90000));
        for(UserData userData : userDataList) {
            if(userData.getConnectPin().equals(id))
                return generateConnectId(userDataList);
        }
        return id;
    }
}