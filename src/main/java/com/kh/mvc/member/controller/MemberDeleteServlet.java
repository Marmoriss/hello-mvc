package com.kh.mvc.member.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			// 1. 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			System.out.println("delete@memberId = " + memberId);
			
			// 2. 서비스 로직 호출
			// delete from member where member_id = ?;
			int result = memberService.deleteMember(memberId);
			
			// 모든 속성 제거하기
			// 1번 세션 객체는 놔두고 내부 속성만 지우기
			
			HttpSession session = request.getSession();
			Enumeration<String> names = session.getAttributeNames();
			
			while(names.hasMoreElements()) {
				String name = names.nextElement();
				session.removeAttribute(name);
			}
			
			// 2번 세션 무효화
			// 2번을 사용할 수 있지만, msg전송이 불가능하기 때문에 현재로는 속성만 제거하는 것이 좋음.
//			session.invalidate();
			
			// saveId cookie 제거
			Cookie cookie = new Cookie("saveId", memberId);
			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(0); // 쿠키의 유효기간 0 => 즉시 삭제
			response.addCookie(cookie);
			
			// 3. 리다이렉트 처리
			session.setAttribute("msg", "회원을 성공적으로 삭제했습니다.");
			response.sendRedirect(request.getContextPath() + "/");
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}
