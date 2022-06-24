<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
    String memberId = loginMember.getMemberId();
    String memberName = loginMember.getMemberName();
    String birthday = loginMember.getBirthday() != null ? 
            loginMember.getBirthday().toString() : ""; // null값이어도 input:datetime에서 무시함.
    String email = loginMember.getEmail() != null ? loginMember.getEmail() : "";
    String phone = loginMember.getPhone();
    int point = loginMember.getPoint();
    String gender = loginMember.getGender() != null ? loginMember.getGender().name() : "";
    String hobby = loginMember.getHobby(); // 등산,게임
    
    List<String> hobbyList = null;
    if(hobby != null){
        String[] arr = hobby.split(",");
        hobbyList = Arrays.asList(arr); // String[] -> List<String>
    }


%>
<section id=enroll-container>
	<h2>회원 정보</h2>
	<form name="memberUpdateFrm" action="<%=request.getContextPath() %>/member/memberUpdate" method="POST">
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" name="memberId" id="memberId" value="<%= loginMember.getMemberId() %>" readonly>
				</td>
			</tr>
			<tr>
				<th>이름<sup>*</sup></th>
				<td>	
				<input type="text"  name="memberName" id="memberName" value="<%= loginMember.getMemberName() %>"  required><br>
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>	
				<input type="date" name="birthday" id="birthday" value="<%= loginMember.getBirthday() %>"><br>
				</td>
			</tr> 
			<tr>
				<th>이메일</th>
				<td>	
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="<%= loginMember.getEmail() %>"><br>
				</td>
			</tr>
			<tr>
				<th>휴대폰<sup>*</sup></th>
				<td>	
					<input type="tel" placeholder="(-없이)01012345678" name="phone" id="phone" maxlength="11" value="<%= loginMember.getPhone() %>" required><br>
				</td>
			</tr>
			<tr>
				<th>포인트</th>
				<td>	
					<input type="text" placeholder="" name="point" id="point" value="<%= loginMember.getPoint() %>" readonly><br>
				</td>
			</tr>
			<tr>
				<th>성별 </th>
				    <td>
                        <input type="radio" name="gender" id="gender0" value="M" <%= "M".equals(gender) ? "checked" : "" %>>
                         <label for="gender0">남</label>
                         <input type="radio" name="gender" id="gender1" value="F" <%= "F".equals(gender) ? "checked" : "" %>>
                         <label for="gender1">여</label>
				    </td>
			</tr>
			<tr>
				<th>취미 </th>
				<td>
                    <input type="checkbox" name="hobby" id="hobby0" value="운동" <%= hobbyChecked(hobbyList, "운동") %>><label for="hobby0">운동</label>
                    <input type="checkbox" name="hobby" id="hobby1" value="등산" <%= hobbyChecked(hobbyList, "등산") %>><label for="hobby1">등산</label>
                    <input type="checkbox" name="hobby" id="hobby2" value="독서" <%= hobbyChecked(hobbyList, "독서") %>><label for="hobby2">독서</label><br />
                    <input type="checkbox" name="hobby" id="hobby3" value="게임" <%= hobbyChecked(hobbyList, "게임") %>><label for="hobby3">게임</label>
                    <input type="checkbox" name="hobby" id="hobby4" value="여행" <%= hobbyChecked(hobbyList, "여행") %>><label for="hobby4">여행</label><br />
				</td>
			</tr>
		</table>
        <input type="submit" value="정보수정"/>
        <input type="button" value="비밀번호 변경" onclick="updatePassword();"/>
        <input type="button"  value="탈퇴" onclick="deleteMember();"/>
	</form>
</section>
<form action="<%=request.getContextPath() %>/member/memberDelete" name="memberDelFrm" method="POST"></form>
    <input type="hidden" name="memberId" id="memberId" value="<%= loginMember.getMemberId() %>" />
<script>

const updatePassword = () => {
  
    location.href ="<%= request.getContextPath() %>/member/passwordUpdate";
};
/**
 * POST /member/memberDelete
 * POST는 폼안에서 제출하는 방법으로만 제출할 수 있다. 
 * POST
 * form submit[method=POST]
 *
 * - GET 방식
 * 1. a tag
 * 2. location.href
 * 3. form submit[method=GET]
 * memberDelFrm 제출
 */
const deleteMember = () => {
   //memberDelFrm 제출
   const bool = confirm("정말 탈퇴하시겠습니까?")
   if(bool){
       document.memberDelFrm.submit();
   };
   
   
};

document.memberUpdateFrm.onsubmit = (e) => {
    // 이름 유효성 확인
    const memberName = document.querySelector("#memberName");
    if(!/^[가-힣]{2,}$/.test(memberName.value)){
        alert("이름은 최소 한글 2글자 이상이어야 합니다.");
        memberName.select();
        return false;
    }
   
    // 전화번호 유효성 확인
    const phone = document.querySelector("#phone");
    if(!/^010[0-9]{8}$/.test(phone.value)){
        alert("유효한 전화번호를 입력해주세요");
        phone.select();
        return false;
    }
    
}
</script>
<%!
/**
* compile시 메소드로 선언처리됨.
* 선언위치는 어디든 상관없다.
*/ 
public String hobbyChecked(List<String> hobbyList, String hobby){
    return hobbyList != null && hobbyList.contains(hobby) ? "checked" : "";
}

%>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
