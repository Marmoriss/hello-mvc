package com.kh.mvc.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public class HelloMvcUtils {
	/**
	 * 1. 암호화 2. 인코딩처리
	 * 
	 * @param parameter
	 * @return
	 */
	public static String getEncryptedPassword(String rawPassword, String salt) {
		String encryptedPassword = null;

		try {
			// 1. 암호화
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] input = rawPassword.getBytes("utf-8");
			byte[] saltBytes = salt.getBytes("utf-8");
			md.update(saltBytes);// salt 전달
			byte[] encryptedBytes = md.digest(input);
			System.out.println(new String(encryptedBytes));

			// 2. 인코딩처리 : 영문자 숫자 + / (=패딩문자) -> 총 64개 문자
			Encoder encoder = Base64.getEncoder();
			encryptedPassword = encoder.encodeToString(encryptedBytes);

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return encryptedPassword;
	}
	
	/**
	 * @param cPage
	 * @param numPerPage
	 * @param totalContent
	 * @param url
	 * @return
	 * 
	 * totalPage 전체 페이지 수
	 * pagebarSize 한 페이지에 표시할 페이지 번호 개수
	 * pagebarStart ~ pagebarEnd
	 * pageNo 증감변수
	 * 
	 * 1. 이전영역
	 * 2. pageNo영역
	 * 3. 다음 영역
	 * 
	 */
	public static String getPagebar(int cPage, int numPerPage, int totalContent, String url) {
		StringBuilder pagebar = new StringBuilder();
		url += (url.indexOf("?") < 0) ? "?cPage=" : "&cPage=";
		int totalPage = (int) Math.ceil((double) totalContent / numPerPage);
		int pagebarSize = 5;
		int pagebarStart = ((cPage - 1) / pagebarSize * pagebarSize) + 1;
		int pagebarEnd = pagebarStart + pagebarSize - 1;
		int pageNo = pagebarStart;
		
		// 이전 영역
		if(pageNo == 1) {
			
		} else {
			pagebar.append("<a href='" + url + (pageNo - 1) + "'>이전</a>\n");
		}
		
		// pageNo 영역
		while(pageNo <= pagebarEnd && pageNo <= totalPage) {
			// 현재 페이지
			if(pageNo == cPage) {
				pagebar.append("<span class='cPage'>" + pageNo + "</span>\n");
			} 
			//현재 페이지가 아닌 경우
			else {
				pagebar.append("<a href='" + url + pageNo + "'>" + pageNo + "</a>\n");
			}
			pageNo++;
		}
		
		// 다음 영역
		if(pageNo > totalPage) {
			
			
		}
		else {
			pagebar.append("<a href='" + url + pageNo + "'>다음</a>\n");
		}
		return pagebar.toString();
	}

	public static String convertLineFeedToBr(String str) {
		
		return str.replaceAll("\\n", "<br/>");
	}

	public static String escapeXml(String str) {
		
		return str.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

}






