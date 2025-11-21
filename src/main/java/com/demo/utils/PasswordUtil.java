package com.demo.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class cho việc hash và verify mật khẩu bằng BCrypt.
 * - hashPassword: dùng khi lưu mật khẩu mới (register/change password)
 * - verify: dùng khi kiểm tra mật khẩu nhập vào khi login
 */
public class PasswordUtil {

    /** Sinh hash từ mật khẩu plain-text. */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) return null;
        // gensalt(12) là một mức cost hợp lý cho dev/prod nhỏ; có thể tăng lên 14 nếu cần.
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /** So sánh plain password với hash đã lưu. Trả về true nếu khớp. */
    public static boolean verify(String plainPassword, String hashed) {
        if (plainPassword == null || hashed == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, hashed);
        } catch (Exception e) {
            // Nếu format hashed không phải từ BCrypt, checkpw sẽ ném; trả về false an toàn
            return false;
        }
    }
}