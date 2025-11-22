package com.demo.controller;

import com.demo.entity.User;
import com.demo.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward đến login.jsp trong WEB-INF
        req.getRequestDispatcher("/WEB-INF/decorator/login.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.login(username, password);
        if (user != null) {
            req.getSession().setAttribute("user", user);

            if (Boolean.TRUE.equals(user.getAdmin())) {
                resp.sendRedirect(req.getContextPath() + "/admin.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home.jsp");
            }
        } else {
            req.setAttribute("error", "Sai username hoặc password hoặc tài khoản bị khóa");
            req.getRequestDispatcher("/WEB-INF/decorator/login.jsp").forward(req, resp);
        }
    }
}
