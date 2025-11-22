package com.demo.controller;

import com.demo.entity.Category;
import com.demo.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/category")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 5 * 1024 * 1024,   // 5MB
    maxRequestSize = 10 * 1024 * 1024
)
public class CategoryServlet extends HttpServlet {

    private CategoryService categoryService = new CategoryService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            Integer id = Integer.parseInt(req.getParameter("id"));
            Category c = categoryService.findById(id);
            req.setAttribute("category", c);
            req.getRequestDispatcher("/admin/category-form.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            Integer id = Integer.parseInt(req.getParameter("id"));
            categoryService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/category");
            return;
        }

        List<Category> categories = categoryService.findAll();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/admin/category-list.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String statusStr = req.getParameter("status");
        Boolean status = "on".equals(statusStr);

        Part filePart = req.getPart("images");
        String fileName = filePart != null ? filePart.getSubmittedFileName() : null;
        if (fileName != null && !fileName.isEmpty()) {
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            filePart.write(uploadPath + File.separator + fileName);
        }

        if (idStr == null || idStr.isEmpty()) {
            Category c = new Category(name, code, fileName, status);
            categoryService.create(c);
        } else {
            Integer id = Integer.parseInt(idStr);
            Category c = categoryService.findById(id);
            c.setCategoryname(name);
            c.setCategorycode(code);
            if (fileName != null && !fileName.isEmpty()) c.setImages(fileName);
            c.setStatus(status);
            categoryService.update(c);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/category");
    }
}
