package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberViewServlet
 */
@WebServlet("/member/memberView")
public class MemberViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 정보 조회 페이지 불러오기
		request.getRequestDispatcher("/WEB-INF/views/member/memberView.jsp").forward(request, response);

//		// 1. 인코딩 처리
//		request.setCharacterEncoding("utf-8");
//
//		// 2. 세션에서 로그인 정보 받아오기
//		HttpSession session = request.getSession();
//		
//		System.out.println("MemberViewServlet@loginMember = " + loginMember);
//
//		Member member = memberService.viewMemberById(loginMember.getMemberId());
//		System.out.println("MemberViewServlet@member = " + member);
//
//		session.setAttribute("loginMemberData", member);

	}

	/**
	 * db select 요청
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
