package com.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Register extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();

        RequestDispatcher dispatcher = null;

        if (username.isEmpty() || password.isEmpty()){
            dispatcher = req.getRequestDispatcher("register.jsp");
            req.setAttribute("error", "Username and password are required");
            req.setAttribute("status", "failed");   
            dispatcher.forward(req, resp);
            return;
        }

        session.removeAttribute(username);

        if (password.length() < 6){
            dispatcher = req.getRequestDispatcher("register.jsp");
            req.setAttribute("error", "Password must contain at least 6 characters");
            req.setAttribute("status", "failed");   
            dispatcher.forward(req, resp);
            return;
        }
    
        PrintWriter out = resp.getWriter();

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            DB db = new DB();

            Connection conn = db.connect();

            st = conn.prepareStatement("SELECT count(*) AS count FROM users WHERE username = ?");
            st.setString(1, username);
            rs = st.executeQuery();

            rs.next();

            if (rs.getInt(1) > 0){
                dispatcher = req.getRequestDispatcher("register.jsp");
                req.setAttribute("error", "User already exists");
                req.setAttribute("status", "failed");   
                dispatcher.forward(req, resp);
                return;
            }

            st = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS); 
            st.setString(1, username); 
            st.setString(2, password); 
            int result = st.executeUpdate(); 

            if (result > 0){
                ResultSet generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    dispatcher = req.getRequestDispatcher("index.jsp");
                    session.setAttribute("id", generatedKeys.getInt(1));
                } else {
                    dispatcher = req.getRequestDispatcher("register.jsp");
                    req.setAttribute("error", "Registration error");
                    req.setAttribute("status", "failed");   
                }
            } else {
                dispatcher = req.getRequestDispatcher("register.jsp");
                req.setAttribute("error", "Registration error");
                req.setAttribute("status", "failed");   
            }
        }
        catch (Exception e) { 
            out.println(e.toString());
        } 

        dispatcher.forward(req, resp);
    }
}
