package com.kh.mvc.chat.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChatroomServlet
 */
@WebServlet("/chat/chatroom")
public class ChatroomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String chatroomId = request.getParameter("chatroomId");
		// 웹소켓 연결시 chatroomId관리를 위해 session 속성으로 저장
		request.getSession().setAttribute("chatroomId", chatroomId);
		
		request.getRequestDispatcher("/WEB-INF/views/chat/chatroom.jsp")
			.forward(request, response);
		
	}

}
