package com.kh.mvc.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Member;

/**
 * Servlet Filter implementation class LoginFilter
 * 
 * @WebFilter#urlPatterns
 * @WebFilter#value (속성명을 생략하고 저장가능)
 * 
 */
@WebFilter({ 
	"/member/memberView", 
	"/memebr/memberUpdate", 
	"/member/memberDelete",
	"/member/passwordUpdate",
	"/board/boardEnroll",
	"/board/boardUpdate",
	"/board/boardDelete",
	"/board/boardCommentEnroll",
	"/chat/*"
	})
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		//로그인 여부 체크 - 세션에 있는 loginMember찾아가기
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;
		HttpSession session = httpReq.getSession();
		
		Member loginMember = (Member) session.getAttribute("loginMember");

		//로그인 하지 않고 허락되지 않는 페이지 접근할 때
		if(loginMember == null) {
			session.setAttribute("msg", "로그인 후 이용할 수 있습니다.");
			httpRes.sendRedirect(httpReq.getContextPath() + "/");
			return;
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
