package com.sct.springsecuritytest.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;


public class AppServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(AppServlet.class);

    public AppServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {            
        InputStream input = request.getInputStream();
        DataInputStream dataReader = new DataInputStream(input);
        OutputStream output = response.getOutputStream();
        DataOutputStream dataWritter = new DataOutputStream(output);
        
        logger.error("Receive request data error,ip addr(" + request.getRemoteAddr() + ").");
        dataWritter.write("Request format error.".getBytes());
        dataWritter.flush();
        dataWritter.close();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
