package com.kh.mvc.member.model.dao;

import static com.kh.mvc.common.JdbcTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.dto.MemberRole;
import com.kh.mvc.member.model.exception.MemberException;

public class MemberDao {

	private Properties prop = new Properties();
	
	public MemberDao() {
		String filename = MemberDao.class.getResource("/sql/member/member-query.properties").getPath();
		//System.out.println("filename@MemberDao = " + filename);
		
		try {
			prop.load(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	/**
	 * DQL 요청 - dao
	 * 1. PreparedStatement 객체 생성 (sql 전달) & 값 대입
	 * 2. 쿼리 실행 executeQuery - ResultSet 반환
	 * 3. ResultSet처리 - dto객체 변환
	 * 4. ResultSet, PreparedStatement 객체 반환
	 * 
	 * @param conn
	 * @param memberId
	 * @return
	 */
	public Member findById(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		String sql = prop.getProperty("findById");
		// select * from member where member_id = ?
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				member = handleMemberResultSet(rset);
			}
			
		} catch (SQLException e) {
			throw new MemberException("회원 아이디 조회 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return member;
	}

	private Member handleMemberResultSet(ResultSet rset) throws SQLException {
		String memberId = rset.getString("member_id");
		String password = rset.getString("password");
		String memberName = rset.getString("member_name");
		MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role")); //MemberRole.U / MemberRole.A 를 반환
		
		String _gender = rset.getString("gender");
		Gender gender = _gender != null ? Gender.valueOf(_gender) : null;
		
		Date birthday = rset.getDate("birthday");
		String email = rset.getString("email");
		String phone = rset.getString("phone");
		String hobby = rset.getString("hobby");
		int point = rset.getInt("point");
		Timestamp enrollDate = rset.getTimestamp("enroll_date");
		return new Member(memberId, password, memberName, memberRole, gender
				, birthday, email, phone, hobby, point, enrollDate);
	}

	/**
	 * DML 요청 - dao
	 * 1. PreparedStatement 객체 생성 (sql 전달) & 값 대입
	 * 2. 쿼리 실행 executeUpdate - int 반환
	 * 3. PreparedStatement 객체 반환
	 * 
	 * @param conn
	 * @param memberId
	 * @return
	 */
	
	public int insertMember(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertMember");
		//insert into member values (?, ?, ?, default, ?, default, ?, ?, ?, ?, ?, default, default)
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender() != null ? member.getGender().name() : null);//Gender.M
			// member.getGender().name() -> enum의 값만 가져오기. .toString()도 가능
			pstmt.setDate(5, member.getBirthday());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getHobby());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// service에 예외 던짐(unchecked, 비지니스를 설명가능한 구체적 커스텀 예외로 전환)
			throw new MemberException("회원가입 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateMember(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMember");
		//update member set member_name = ?, birthday = ?, 
		//email = ?, phone = ?, gender = ?, hobby = ? where member_id = ?
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getMemberName());
			pstmt.setDate(2, member.getBirthday());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getPhone());
			pstmt.setString(5, member.getGender() != null ? member.getGender().name() : null);
			pstmt.setString(6, member.getHobby());
			pstmt.setString(7, member.getMemberId());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// service 예외 던짐(unchecked, 비지니스를 설명가능한 
			throw new MemberException("회원 정보 수정 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updatePassword(Connection conn, String memberId, String newPassword) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updatePassword");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, newPassword);
			pstmt.setString(2, memberId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MemberException("비밀번호 변경 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public List<Member> findAll(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		String sql = prop.getProperty("findAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Member member = handleMemberResultSet(rset);
				list.add(member);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	public int deleteMember(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new MemberException("회원 정보 삭제 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int getTotalContent(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContent = 0;
		String sql = prop.getProperty("getTotalContent");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				totalContent = rset.getInt(1); // colum 인덱스로 받아오기(테이블 구조 바뀌지 않을 때만)
			}
		} catch (SQLException e) {
			throw new MemberException("전체 회원 수 조회 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return totalContent;
	}

	public List<Member> findMemberLike(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		String sql = prop.getProperty("findMemberLike");
		// select * from member where # like ?
		String col = (String) param.get("searchType");
		String val = (String) param.get("searchKeyword");
		int start = (int) param.get("start");
		int end = (int) param.get("end");
		
		sql = sql.replace("#", col);
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + val + "%");
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rset = pstmt.executeQuery();
			while(rset.next())
				list.add(handleMemberResultSet(rset));
		
		} catch (SQLException e) {
			throw new MemberException("관리자 회원 검색 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	public int getTotalContentLike(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContent = 0;
		String sql = prop.getProperty("getTotalContentLike");
		String col = (String) param.get("searchType");
		String val = (String) param.get("searchKeyword");
		sql = sql.replace("#", col);
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + val + "%");
			
			rset = pstmt.executeQuery();
			if(rset.next())
				totalContent = rset.getInt(1);
			
			
		} catch (SQLException e) {
			throw new MemberException("관리자 검색된 회원 수 조회 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return totalContent;
	}

	public int updateMemberRole(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMemberRole");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getMemberRole().name());
			pstmt.setString(2, member.getMemberId());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MemberException("회원권한정보 수정 오류!", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

}






