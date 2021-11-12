package castingn.com.chat.web;


import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import castingn.com.chat.service.ChatService;

@RestController
public class ChatController {

	@Autowired
	private ChatService chatService;


	@Autowired
    private SimpMessageSendingOperations messagingTemplate;

	@Value("${castingn.fileStorePath}")
	private String fileStorePath;



	// 접속 된 클라이언트 WebSocket session 관리 리스트
//	private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());

	/**
	 * 채팅화면 이동
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/openChat")
	public ModelAndView chat(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView();

		String stndCd = param.get("stndCd").toString();
		String cmChatType = param.get("cmChatType").toString();
		String svcCd = param.get("svcCd").toString();
		String[] uPkArr = param.get("uPk").toString().split("\\:");

		Map<String, Object> userInfo = null;
		Map<String, Object> projectInfo = null;

		if(svcCd.equals("SVC01")) { // SVC01 : www.castingn.com or admin.castingn.com
			userInfo = chatService.selectUserInfo(param); // 사용자 정보
			projectInfo = chatService.selectProjectInfo(param); // 의뢰건 정보
		}else if(svcCd.equals("SVC02")) { // SVC02 : mall.castingn.com
			userInfo = chatService.selectMallMemberInfo(param); // 사용자 정보
			projectInfo = chatService.selectMallProductInfo(param); // 의뢰건 정보
		}

		param.put("uPkArr", String.join(",", uPkArr));

		String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK 존재유무 확인

		if(cmPk == null) {

			String cmPkNum = chatService.selectCmPkNum(param) + "";

			cmPk = stndCd + "_" + cmPkNum; // CHAT_MASTER.CM_PK 생성

			param.put("cmPk", cmPk); // 채팅방 Pk(CHAT_MASTER.CM_PK)

			param.put("uPk", "0");
			param.put("pUpk", "0");
			param.put("uPkArr", "0");

			chatService.insertChatMaster(param);

			for(int i=0; i < uPkArr.length; i++) {

				param.put("uPk", uPkArr[i]);

				if(svcCd.equals("SVC01")) {
					chatService.insertChatUser(param);
				} else if(svcCd.equals("SVC02")) {
					param.put("uPkOrder", i);
					chatService.insertMallChatUser(param);
				}

			}

		}else {
			param.put("cmPk", cmPk);
		}

		if(userInfo != null) {
			String uPk = String.valueOf(userInfo.get("U_PK"));
			String uName = String.valueOf(userInfo.get("U_NAME"));
			String uType = String.valueOf(userInfo.get("U_TYPE"));
			param.put("uPk", uPk);
			param.put("uName", uName);
			param.put("uType", uType);
			chatService.updateChatUser(param);
		}

		mv.addObject("projectInfo", projectInfo); // 의뢰/서비스건 정보
		mv.addObject("userInfo", userInfo); // 사용자 정보
		mv.addObject("cmPk", cmPk); // 채팅방 PK
		mv.addObject("path", cmPk); // 채팅방 경로(채팅방 PK)
		mv.addObject("svcCd", svcCd); // 접속 서비스 코드 (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
//		mv.addObject("chatHistList", chatService.selectChatHistList(param)); // 채팅방 이전 채팅내역
		mv.addObject("cmChatType", cmChatType);

		mv.setViewName("openChat");

		return mv;
	}

	@RequestMapping("/selectChatHist")
	public ModelAndView selectChatHist(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		mv.addObject("chatHistList", chatService.selectChatHistList(param)); // 채팅방 이전 채팅내역

		return mv;
	}

//	/**
//	 * 의뢰건별 채팅방 접속
//	 * @param ChatVo
//	 * @param SimpMessageHeaderAccessor
//	 * @throws Exception
//	 */
//	@MessageMapping("/chat.addUser")
//    public void addUser(@Payload ChatVo chatVo, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//
//		Map<String, Object> param = new HashMap<String, Object>();
//
//		param.put("cmPk", chatVo.getPath());   // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
//		param.put("uPk", chatVo.getUserPk());  // USER_INFO.U_PK (채팅방 참여자 PK )
//		param.put("svcCd", chatVo.getSvcCd()); // 접속 서비스 구분 코드(SVC01 : mall.castingn.com, SVC02: www.castingn.com/admin.castingn.com)
//
//		chatVo.setContent(chatVo.getUserNm() + " 님이 채팅방에 참여하셨습니다.");
//
//		param.put("content", chatVo.getContent());
//		param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
//
//		int readCnt = chatService.selectChatMessageReadCnt(param);
//
//		chatService.updateChatDetailRead(param); // 채팅 내용 읽기 여부 확인 (CDR_READ_YN update)
//
//		chatVo.setReadCnt(readCnt);
//
//		chatService.insertChatDetail(param); // 채팅내용 등록
//
//        headerAccessor.getSessionAttributes().put("username", chatVo.getUserNm());
//        headerAccessor.getSessionAttributes().put("path", chatVo.getPath());
//        headerAccessor.getSessionAttributes().put("uPk", chatVo.getUserPk());
//
//        messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
//    }

//	/**
//	 * 메세지 전송
//	 * @param ChatVo
//	 * @throws Exception
//	 */
//	@MessageMapping("/chat.sendMessage")
//	/* @SendTo("/topic/{chatRoomId}") */
//    public void sendMessage(@Payload ChatVo chatVo) throws Exception {
//
//		Map<String, Object> param = new HashMap<String, Object>();
//
//		param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
//		param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (채팅방 참여자 PK )
//
//		param.put("content", chatVo.getContent());
//		param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
//
//		chatVo.setReadCnt(chatService.insertChatDetail(param)); // 채팅내용 등록
//
//		messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
//    }

//	/**
//	 * 파일 전송
//	 * @param ChatVo
//	 * @throws Exception
//	 */
//	@MessageMapping("/chat.sendFile")
//	public void sendFile(@Payload ChatVo chatVo) throws Exception {
//
//		Map<String, Object> param = new HashMap<String, Object>();
//		FileOutputStream fOut = null;
//
//		try {
//
//	        String fileName = chatVo.getFileName();
//	//        String filePath = "D:/chat/file/"; // local
////	        String filePath = "/var/www/chat/file/"; // prod
//	        byte[] decodedBytes = Base64.getDecoder().decode(chatVo.getFile().toString());
//
//	        int index = fileName.lastIndexOf(".");
//	        String fileExt = fileName.substring(index + 1); // 첨부파일 확장자
//	        String orgName = fileName.split("." + fileExt)[0];
//	        String fileNewName = orgName + "_" + getTimeStamp() + "." + fileExt;
//
//	        File file =  new File(fileStorePath);
//
//	        if (!file.exists()) {
//	        	file.mkdirs();
//			}
//
//	        chatVo.setFilePath(fileStorePath + fileNewName);
//	        chatVo.setFileNewName(fileNewName);
//
//	        fOut = new FileOutputStream(fileStorePath + fileNewName);
//	        fOut.write(decodedBytes);
//
//	        param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
//	        param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (채팅방 참여자 PK )
//	        param.put("orgFileNm", chatVo.getFileName()); // 원본 파일명
//	        param.put("newFileNm", chatVo.getFileNewName()); // 저장 파일명
//	        param.put("filePath" , chatVo.getFilePath()); // 파일 저장 경로
//
//		} catch(IOException e) {
//
//			chatVo.setUserStatus(chatVo.getUserStatus().FAILED); // 사용자 채팅 상태
//
//	    }finally {
//
//
//	    	try {
//
//	    		fOut.close();
//
//	    		param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
//
//	    		chatVo.setReadCnt(chatService.insertChatDetail(param)); // 채팅내용 등록
//
//	    		messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//
//		// messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
////        return chatVo;
//    }

//	/**
//	 * 메세지 읽기 확인
//	 * @param ChatVo
//	 * @throws Exception
//	 */
//	@MessageMapping("/chat.readMessage")
//	/* @SendTo("/topic/{chatRoomId}") */
//    public void readMessage(@Payload ChatVo chatVo) throws Exception {
//
//		Map<String, Object> param = new HashMap<String, Object>();
//
//		param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
//		param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (채팅방 참여자 PK )
//		param.put("cdPk", ""); // CHAT_DETAIL.CD_PK (채팅 내용 PK )
//
//		/*
//		 * List<Map<String, Object>> userList = chatService.selectChatUserList(param);
//		 *
//		 * String[] uPkArr = new String[userList.size()];
//		 *
//		 * for(int i=0; i < userList.size(); i++ ) { uPkArr[i] =
//		 * userList.get(i).get("uPk").toString(); }
//		 *
//		 * param.put("uPk", String.join(",", uPkArr));
//		 */
//
//		int readCnt = chatService.selectChatMessageReadCnt(param);
//
//		chatService.updateChatDetailRead(param); // 채팅 내용 읽기 여부 확인 (CDR_READ_YN update)
//
//		chatVo.setReadCnt(readCnt);
//
//		messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
//    }

//	/**
//	 * 전송 파일 다운로드
//	 * @param HttpServletResponse
//	 * @param HttpServletRequest
//	 * @throws Exception
//	 */
//    @RequestMapping(value="/attachFile/downloadFile.do")
//	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//    	String filePath = request.getParameter("filePath");
////    	String filenameOutput = URLEncoder.encode(request.getParameter("originalFileName"), "UTF-8");
//    	String filenameOutput = request.getParameter("originalFileName");
//
//    	File file = new File(filePath);
//    	byte b[] = new byte[(int)file.length()];
//
//    	setDisposition(filenameOutput, request, response);
//    	response.setHeader("Content-Length",String.valueOf(file.length()));
//
//		if (file.isFile()) {
//
//			BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
//			BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
//
//			int read = 0;
//
//			while ((read = fin.read(b)) != -1) {
//				outs.write(b, 0, read);
//			}
//
//			outs.close();
//			fin.close();
//		}
//	}

    /**
     * Disposition 지정하기.
     * @param filename
     * @param request
     * @param response
     * @throws Exception
     */
    public void setDisposition(String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String browser = getBrowser(request);
		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
		    encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
		    encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
		    encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < fileName.length(); i++) {
				char c = fileName.charAt(i);
				if (c > '~') {
				    sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
				    sb.append(c);
				}
		    }
		    encodedFilename = sb.toString();
		} else {
		    //throw new RuntimeException("Not supported browser");
		    throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

		if ("Opera".equals(browser)){
		    response.setContentType("application/octet-stream;charset=UTF-8");
		}
    }

    /**
     * 브라우저 구분 얻기.
     * @param request
     * @return
     */
	public String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }

	/**
     * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
     *
     * @param
     * @return the time stamp
     * @see
     */
	public static String getTimeStamp() {

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";

        SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        rtnStr = sdfCurrent.format(ts.getTime());

        return rtnStr;
    }
}

