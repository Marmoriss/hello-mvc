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
 * Servlet implementation class MemberDeleteServlet
 */
@WebServlet("/member/memberDelete")
public class MemberDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 2. 사용자 입력값 처리
			HttpSession session = request.getSession();
			Member loginMember = (Member) session.getAttribute("loginMember");
			String memberId = loginMember.getMemberId();
			System.out.println("delete@memberId = " + memberId);
			
			// 3. 업무 로직
			// delete from member where member_id = ?;
			int result = memberService.deleteMember(memberId);
			
			// 4. 응답
			// 탈퇴처리 후 세션 만료
			if(result > 0 ) {
				
			if(session != null)
				session.invalidate();
			
			// 홈으로 돌아가기
			response.sendRedirect(request.getContextPath() + "/");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}
