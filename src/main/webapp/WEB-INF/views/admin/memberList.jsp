<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.kh.mvc.member.model.dto.Gender"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!-- 관리자용 admin.css link -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css" />
<%
    List<Member> list = (List<Member>) request.getAttribute("list");
%>
<section id="memberList-container">
	<h2>회원관리</h2>
	
	<table id="tbl-member">
		<thead>
			<tr>
				<th>아이디</th>
				<th>이름</th>
				<th>회원권한</th> <%-- select 태그로 처리 --%>
				<th>성별</th>
				<th>생년월일</th>
				<th>이메일</th>
				<th>전화번호</th>
				<th>포인트</th> <%-- 세자리 콤마 찍기 decimal format찾아보기 --%>
				<th>취미</th>
				<th>가입일</th><%-- 날짜 형식 yyyy-MM-dd --%>
			</tr>
		</thead>
		<tbody>
        <% 
            if(list != null) { 
                for(Member member : list) {
        %>
            <tr>
                <td><%= member.getMemberId() %></td>
                <td><%= member.getMemberName() %></td>
                <td>
                    <select>
                        <option value="U" <%= member.getMemberRole() == MemberRole.U ? "selected" : "" %> >유저</option>
                        <option value="A" <%= member.getMemberRole() == MemberRole.A ? "selected" : "" %> >관리자</option>
                    </select>
                </td>
                <td><%= member.getGender() %></td>
                <td><%= member.getBirthday() %></td>
                <td><%= member.getEmail() %></td>
                <td><%= member.getPhone() %></td>
                <td>
                <%= new DecimalFormat("#,###,###").format(member.getPoint()) %>
                </td>
                <td><%= member.getHobby() %></td>
                <td>
                <%= new SimpleDateFormat("yyyy-MM-dd").format(member.getEnrollDate()) %>
                </td>
            </tr>
        <% 
                }
            } 
        %>
        </tbody>
	</table>
</section>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>

