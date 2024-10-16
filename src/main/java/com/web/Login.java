package com.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session2 = HibernateUtil.getSessionFactory().openSession();

		session2.beginTransaction();
		User user = new User();

		user.setUserId(1);
		user.setUsername("Mukesh");
		user.setCreatedBy("Google");
		user.setCreatedDate(new Date());

		session2.save(user);
		session2.getTransaction().commit();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();

        RequestDispatcher dispatcher = null;

        if (username.isEmpty() || password.isEmpty()){
            dispatcher = req.getRequestDispatcher("login.jsp");
            req.setAttribute("error", "Username and password are required");
            req.setAttribute("status", "failed");   
            dispatcher.forward(req, resp);
            return;
        }

        PrintWriter out = resp.getWriter();
        
        try {
            DB db = new DB();

            Connection conn = db.connect();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?"); 
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            
            if (!rs.next()){
                dispatcher = req.getRequestDispatcher("login.jsp");
                req.setAttribute("error", "Username not found");
                req.setAttribute("status", "failed");   
                dispatcher.forward(req, resp);
                return;
            }
            
            if (rs.getString(3).compareTo(password) != 0){
                dispatcher = req.getRequestDispatcher("login.jsp");
                req.setAttribute("error", "Incorrect password");
                req.setAttribute("status", "failed");   
                dispatcher.forward(req, resp);
                return;
            }

            session.setAttribute("id", rs.getInt(1));
            dispatcher = req.getRequestDispatcher("index.jsp");
            dispatcher.forward(req, resp);
        }
        catch (Exception e) { 
            out.println(e.toString());
        } 
    }
}
