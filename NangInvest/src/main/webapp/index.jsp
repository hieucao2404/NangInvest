<%-- 
    Document   : index
    Created on : Jul 11, 2025, 10:00:00 AM
    Author     : Admin
    Description: Entry point for NangInvest application
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // For now, simply redirect to landing page
    // We'll add user session checking later once servlets are working
    response.sendRedirect(request.getContextPath() + "/public/landing.jsp");
%>
