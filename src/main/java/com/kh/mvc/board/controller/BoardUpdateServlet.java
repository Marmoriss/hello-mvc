package com.kh.mvc.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.service.BoardService;
import com.kh.mvc.common.HelloMvcFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class BoardUpdateServlet
 */
@WebServlet("/board/boardUpdate")
public class BoardUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * GET 수정폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int no = Integer.parseInt(request.getParameter("no"));
			Board board = boardService.findByNo(no);
			request.setAttribute("board", board);
			
			request.getRequestDispatcher("/WEB-INF/views/board/boardUpdate.jsp")
				.forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * POST DB update파일 요청 - 파일 업로드
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 1. 첨부파일 처리
			ServletContext application = getServletContext();
			String saveDirectory = application.getRealPath("/upload/board");
//			System.out.println("saveDirectory = " + saveDirectory);
			int maxPostsize = 1024 * 1024 * 10; // 10MB
			String encoding = "utf-8";
			FileRenamePolicy policy = new HelloMvcFileRenamePolicy();

			MultipartRequest multiReq = new MultipartRequest(
					request, saveDirectory, maxPostsize, encoding, policy);

			// 2. db update처리
			// 삭제 파일 처리
			String[] delFiles = multiReq.getParameterValues("delFile");
			if(delFiles != null) {
				for(String temp : delFiles) {
					int attachNo = Integer.parseInt(temp);

					// 첨부파일 삭제
					Attachment attach = boardService.findAttachmentByNo(attachNo);
					File delFile = new File(saveDirectory, attach.getRenamedFilename());
					delFile.delete();
					
					// db 레코드 삭제
					int result = boardService.deleteAttachment(attachNo);
					System.out.println("[첨부파일" + attachNo + "번 삭제! : " + attach.getRenamedFilename());
				}
			}
			
			int no = Integer.parseInt(multiReq.getParameter("no"));
			String title = multiReq.getParameter("title");
			String writer = multiReq.getParameter("writer");
			String content = multiReq.getParameter("content");
			BoardExt board = new BoardExt(no, title, writer, content, 0, null);

			System.out.println("board@BoardUpdateServlet = " + board);
			
			Enumeration<String> filenames = multiReq.getFileNames();
			while (filenames.hasMoreElements()) {
				String filename = filenames.nextElement();
				File upFile = multiReq.getFile(filename);
				if (upFile != null) {
					Attachment attach = new Attachment();
					attach.setBoardNo(no); //게시글 번호
					attach.setOriginalFilename(multiReq.getOriginalFileName(filename));
					attach.setRenamedFilename(multiReq.getFilesystemName(filename));
					board.addAttachment(attach);
				}
			}

			// 2. 업무로직
			int result = boardService.updateBoard(board);

			// 3. redirect
			request.getSession().setAttribute("msg", "게시글을 성공적으로 수정했습니다.");
			response.sendRedirect(request.getContextPath() + "/board/boardView?no=" + no);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
