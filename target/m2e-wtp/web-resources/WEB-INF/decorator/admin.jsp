<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.demo.entity.User"%>
<%
    Object obj = session.getAttribute("user");
    if (obj == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    User user;
    try {
        user = (User) obj;
    } catch (ClassCastException e) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    if (Boolean.FALSE.equals(user.getAdmin())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<style>
    body { font-family: Arial; background: #f4f4f4; margin:0; }
    .header { background:#2980b9; color:#fff; padding:10px 20px; }
    .header h1 { margin:0; font-size:24px; }
    .sidebar { width:220px; background:#2c3e50; height:100vh; position:fixed; padding-top:20px; }
    .sidebar a { display:block; padding:10px 20px; color:#ecf0f1; text-decoration:none; }
    .sidebar a:hover { background:#34495e; }
    .main { margin-left:220px; padding:20px; }
</style>
</head>
<body>
<div class="sidebar">
    <h2 style="text-align:center;color:#ecf0f1;">Admin Panel</h2>
    <a href="<%=request.getContextPath()%>/admin/category">Manage Categories</a>
    <a href="<%=request.getContextPath()%>/admin/video">Manage Videos</a>
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
</div>
<div class="main">
    <div class="header">
        <h1>Welcome Admin: <%=user.getFullname()%></h1>
    </div>
    <p>Here you can manage categories, videos and users.</p>
</div>
</body>
</html>
