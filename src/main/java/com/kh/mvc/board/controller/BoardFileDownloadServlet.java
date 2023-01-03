package com.kh.mvc.board.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.service.BoardService;

/**
 * Servlet implementation class BoardFileDownloadServlet
 */
@WebServlet("/board/fileDownload")
public class BoardFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			// 1. 사용자 입력값 처리
			int no = Integer.parseInt(request.getParameter("no"));
//			System.out.println("no = " + no);
			
			// 2. 업무로직
			// a. 받은 no를 가지고 attachment객체 조회
			Attachment attach = boardService.findAttachmentByNo(no);
//			System.out.println("attach = " + attach);
			
			// 3. 응답 - 파일 입출력 처리
			// 입력 - saveDirectory + renamedFilename
			// 출력 - http 응답 메세지 출력 response.getOutputStream()
			// 응답 헤더 contentType application/octet-stream
			
			response.setContentType("application/ocset-stream");
			String filename = URLEncoder.encode(attach.getOriginalFilename(), "utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			
			String saveDirectory = getServletContext().getRealPath("/upload/board");
			File downFile = new File(saveDirectory, attach.getRenamedFilename());
			
			try(
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downFile));
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())){
				byte[] buffer = new byte[8192]; // 8kb 기본 값
				int len = 0; // 읽어온 byte 수
				while((len = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, len); // buffer에서 0 ~ len까지 출력
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}
