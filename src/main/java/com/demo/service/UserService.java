package com.demo.service;

import com.demo.dao.UserDAO;
import com.demo.entity.User;
import com.demo.utils.PasswordUtil;

import java.util.Objects;

/**
 * Service xử lý logic liên quan tới User:
 * - register: tạo user mới (hash mật khẩu trước khi lưu)
 * - login: kiểm tra username + password (dùng BCrypt nếu password đã được hash)
 * - changePassword: đổi mật khẩu (kiểm tra mật khẩu cũ rồi lưu hash mới)
 *
 * Ghi chú bảo mật:
 * - KHÔNG lưu mật khẩu plain-text vào database.
 * - Khi đăng nhập, so sánh bằng hàm verify của BCrypt.
 */
public class UserService {
    private final UserDAO userDao = new UserDAO();

    /**
     * Thử đăng nhập với username và password plain-text.
     * - Lấy user từ DB theo username.
     * - Nếu user null trả về null.
     * - Nếu password trong DB là hash (BCrypt), dùng PasswordUtil.verify.
     * - Nếu password trong DB là plain-text (chỉ cho dev), fallback so sánh chuỗi.
     *
     * Trả về đối tượng User nếu thành công, ngược lại trả về null.
     */
    public User login(String username, String password) {
        if (username == null || password == null) return null;

        User user = userDao.findByUsername(username);
        if (user == null) return null;

        String stored = user.getPassword();
        if (stored == null) return null;

        // Thử verify bằng BCrypt trước
        if (PasswordUtil.verify(password, stored)) {
            // có thể kiểm tra thêm user.getActive() == true trước khi trả về
            if (Boolean.FALSE.equals(user.getActive())) return null; // tài khoản bị khóa
            return user;
        }

        // Nếu không match bằng BCrypt, fallback so sánh plain-text (chỉ dành cho hệ thống legacy/dev)
        if (Objects.equals(stored, password)) {
            if (Boolean.FALSE.equals(user.getActive())) return null;
            return user;
        }

        // Không khớp
        return null;
    }

    /**
     * Đăng ký user mới.
     * - Kiểm tra username chưa tồn tại.
     * - Hash mật khẩu rồi lưu.
     * - Trả về user đã lưu (hoặc null nếu username đã tồn tại).
     */
    public User register(User newUser, String plainPassword) {
        if (newUser == null || newUser.getUsername() == null || plainPassword == null) {
            throw new IllegalArgumentException("User và mật khẩu không được null");
        }

        // Kiểm tra tồn tại
        User existing = userDao.findByUsername(newUser.getUsername());
        if (existing != null) {
            return null; // username đã có
        }

        // Hash mật khẩu trước khi lưu
        String hashed = PasswordUtil.hashPassword(plainPassword);
        newUser.setPassword(hashed);

        // Thiết lập giá trị mặc định nếu cần
        if (newUser.getActive() == null) newUser.setActive(true);
        if (newUser.getAdmin() == null) newUser.setAdmin(false);

        userDao.create(newUser); // create trong DAO của bạn sẽ persist entity
        return newUser;
    }

    /**
     * Đổi mật khẩu: kiểm tra mật khẩu cũ, nếu đúng thì hash mật khẩu mới và update.
     * - Trả về true nếu đổi thành công.
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (username == null || oldPassword == null || newPassword == null) return false;

        User user = userDao.findByUsername(username);
        if (user == null) return false;

        String stored = user.getPassword();
        // Kiểm tra old password (BCrypt hoặc plain fallback)
        boolean ok = PasswordUtil.verify(oldPassword, stored) || Objects.equals(oldPassword, stored);
        if (!ok) return false;

        // Hash mật khẩu mới rồi lưu lại
        String hashedNew = PasswordUtil.hashPassword(newPassword);
        user.setPassword(hashedNew);

        // Cập nhật entity: dùng transaction. Nếu DAO chưa có update, ta dùng create() không phù hợp.
        // Giả sử bạn thêm method update(User) trong UserDAO; nếu chưa, mình sẽ cung cấp.
        userDao.update(user);
        return true;
    }
}
