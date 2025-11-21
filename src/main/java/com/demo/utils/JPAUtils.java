package com.demo.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtils {
    private static EntityManagerFactory factory;

    static {
        try {
            factory = Persistence.createEntityManagerFactory("BT21PU"); // đảm bảo tên này trùng persistence.xml
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi tạo EntityManagerFactory: " + e.getMessage(), e);
        }
    }

    public static EntityManager getEntityManager() {
        if (factory == null) {
            throw new IllegalStateException("EntityManagerFactory chưa được khởi tạo.");
        }
        return factory.createEntityManager();
    }

    // gọi khi ứng dụng shutdown để giải phóng resource
    public static void closeFactory() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
