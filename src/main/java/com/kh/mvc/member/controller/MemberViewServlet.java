package com.kh.mvc.member.controller;


import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberDetailSurvlet
 */
@WebServlet("/member/memberView")
public class MemberViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher reqDispatcher = request.getRequestDispatcher("/WEB-INF/views/member/memberView.jsp");
		reqDispatcher.forward(request, response);
	}
	
	/**
	 * POST DB Update 요청
	 * 
	 * update member set ? = ?, ? = ? where member_id = ?
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//1. 인코딩 처리
		request.setCharacterEncoding("utf-8");
		
		//2. 사용자 입력값 처리
		String memberId = request.getParameter("memberId");
		String password = request.getParameter("password");
		String memberName = request.getParameter("memberName");
		String _birthday = request.getParameter("birthday");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String _gender = request.getParameter("gender");
		String[] hobbies = request.getParameterValues("hobby");
		
		Date birthday = (_birthday != null && !"".equals(_birthday))
				? Date.valueOf(_birthday) : null;
		Gender gender = _gender != null ? Gender.valueOf(_gender) : null;
		String hobby = (hobbies != null) ? String.join(",", hobbies) : null;
		
		Member member = 
				new Member(memberId, password, memberName, null, 
						gender, birthday, email, phone, hobby, 0, null);
//		System.out.println("member@MemberViewServlet = " + member);
		
		//3. 업무로직
		int result = memberService.updateMember(member);
		System.out.println("result@MemberViewServlet = " + result);
		
		//4. 리다이렉트
		HttpSession session = request.getSession();
		session.setAttribute("msg", "정보 수정이 정상적으로 처리되었습니다.");
		session.setAttribute("loginMember", member);
		response.sendRedirect(request.getContextPath() + "/");
	}

}
