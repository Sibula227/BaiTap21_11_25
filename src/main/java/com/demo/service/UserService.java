package com.demo.service;

import com.demo.dao.UserDAO;
import com.demo.entity.User;
import com.demo.utils.PasswordUtil;

import java.util.Objects;

public class UserService {
    private final UserDAO userDao = new UserDAO();

    /**
     * Login user với username + password.
     * Trả về User nếu thành công, null nếu sai hoặc tài khoản inactive.
     */
    public User login(String username, String password) {
        if (username == null || password == null) return null;

        User user = userDao.findByUsername(username);
        if (user == null) return null;

        String stored = user.getPassword();
        if (stored == null) return null;

        // Kiểm tra password
        if (PasswordUtil.verify(password, stored) || Objects.equals(password, stored)) {
            if (Boolean.FALSE.equals(user.getActive())) return null; // account bị khóa
            return user;
        }

        return null; // password không khớp
    }

    /**
     * Đăng ký user mới
     */
    public User register(User newUser, String plainPassword, Boolean isAdmin) {
        if (newUser == null || newUser.getUsername() == null || plainPassword == null) {
            throw new IllegalArgumentException("User và mật khẩu không được null");
        }

        User existing = userDao.findByUsername(newUser.getUsername());
        if (existing != null) return null; // username tồn tại

        newUser.setPassword(PasswordUtil.hashPassword(plainPassword));
        if (newUser.getActive() == null) newUser.setActive(true);
        if (isAdmin == null) isAdmin = false;
        newUser.setAdmin(isAdmin);

        userDao.create(newUser);
        return newUser;
    }

    /**
     * Đổi mật khẩu
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (username == null || oldPassword == null || newPassword == null) return false;

        User user = userDao.findByUsername(username);
        if (user == null) return false;

        boolean ok = PasswordUtil.verify(oldPassword, user.getPassword()) 
                  || Objects.equals(oldPassword, user.getPassword());
        if (!ok) return false;

        user.setPassword(PasswordUtil.hashPassword(newPassword));
        userDao.update(user);
        return true;
    }
}
