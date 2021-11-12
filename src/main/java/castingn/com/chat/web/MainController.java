package castingn.com.chat.web;


import castingn.com.chat.ChatVo;
import castingn.com.chat.service.ChatService;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
public class MainController {

	@Autowired
	private ChatService chatService;


	@Autowired
    private SimpMessageSendingOperations messagingTemplate;

	 @Value("${castingn.fileStorePath}")
	 private String fileStorePath;

	@RequestMapping("/chat.test_post")
	public Map<String, Object> testGV(@RequestParam Map<String, Object> param) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		ChatVo retMap = new ChatVo();
		retMap.setContent("aaa");
		ret.put("aaa", "cyh");
		return ret;
	}

	/**
	 * 고객
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/customer")
	public ModelAndView customer(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView();
		System.out.println(param.toString());
		String stndCd = param.get("stndCd").toString();		//상품번호
		String svcCd = param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
		String[] uPkArr = param.get("uPk").toString().split("\\:");	//고객:판매자코드
//		String sellerUPk = param.get("sellerUPk").toString();	//공급사의 회원아이디
		String cmChatType = param.get("cmChatType").toString();
		String pUpk = param.get("pUpk").toString();	//고객아이디:판매자아이디
		String pUid = param.get("P_uid").toString(); //상품아이디

		String customerUpk = uPkArr[0];    //고객코드
		String sellerUPk = uPkArr[1];   //판매자코드

		String[] pUpkArr = pUpk.split(":");
		String customerId = pUpkArr[0];    //고객아이디
		String sellerId = pUpkArr[1];    //판매자아이디

		String cmPk = MapUtils.getString(param, "cmPk", "");
		
		//Map<String, Object> userInfo = null;
		param.put("uPk", customerUpk);
		//userInfo = chatService.selectUserInfo(param); // 사용자 정보
		mv.addObject("cmPk", cmPk);
		mv.addObject("stndCd", stndCd); // 상품번호
		mv.addObject("svcCd", svcCd); // 접속 서비스 코드 (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
		mv.addObject("userType", 1);
		mv.addObject("uPk", customerUpk);
		mv.addObject("sellerUPk", sellerUPk);
		mv.addObject("cmChatType", cmChatType);
		mv.addObject("customerId", customerId);
		mv.addObject("sellerId", sellerId);
		//mv.addObject("uName", "");
		mv.addObject("pUid", pUid);
		
		mv.addObject("uPkArr", String.join(":", uPkArr));
		//mv.addObject("uPkArr", param.get("uPk"));

		mv.setViewName("customer/customer");

		return mv;
	}

	/**
     * 판매자
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping("/seller")
    public ModelAndView seller(@RequestParam Map<String, Object> param/*, HttpServletRequest request*/) throws Exception {

		/*HttpSession session = request.getSession();
		System.out.println("session => " + session);
		Object obj = session.getAttribute("USER");
		System.out.println("obj => " + obj);

		System.out.println("getId => " + session.getId());*/

		String stndCd = MapUtils.getString(param, "stndCd", "");//param.get("stndCd").toString();		//상품번호
		String[] uPkArr = MapUtils.getString(param, "uPk", "").toString().split("\\:");//param.get("uPk").toString().split("\\:");	//고객:판매자코드
//		String sellerUPk = param.get("sellerUPk").toString();	//공급사의 회원아이디
        String svcCd = MapUtils.getString(param, "svcCd", "");//param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
		String cmChatType = MapUtils.getString(param, "cmChatType", "");//param.get("cmChatType").toString();
		String pUpk = MapUtils.getString(param, "pUpk", "");//param.get("pUpk").toString();	//고객아이디:판매자아이디
		String pUid = MapUtils.getString(param, "P_uid", "");//param.get("P_uid").toString(); //판매자 서비스코드		

		String customerUPk = uPkArr[0];    //고객코드
		String sellerUPk = uPkArr[1];   //판매자코드

		String[] pUpkArr = pUpk.split(":");
		String customerId = pUpkArr[0];    //고객아이디
		String sellerId = pUpkArr[1];    //판매자아이디

		Map<String, Object> userInfo = null;
		param.put("uPk", sellerUPk);		

		String cmPk = MapUtils.getString(param, "cmPk", "");
		//String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK 존재유무 확인
		System.out.println(cmPk);
		if(cmPk == null) {
			
		}

        ModelAndView mv = new ModelAndView();
System.out.println("cmPk : " + cmPk);
		mv.addObject("cmPk", cmPk); // 채팅방 PK
		mv.addObject("path", cmPk); // 채팅방 경로(채팅방 PK)

        mv.addObject("stndCd", stndCd); // 상품번호
        mv.addObject("svcCd", svcCd); // 접속 서비스 코드 (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
        mv.addObject("userType", 4);
        mv.addObject("uPk", sellerUPk);
		mv.addObject("customerUPk", customerUPk);
		mv.addObject("cmChatType", cmChatType);
		mv.addObject("customerId", customerId);
		mv.addObject("sellerId", sellerId);

		mv.addObject("pUid", pUid);
		mv.addObject("uPkArr", String.join(":", uPkArr));
		
        mv.setViewName("seller/seller");

        return mv;
    }

	/**
	 * 관리자
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/admin_chat")
	public ModelAndView admin(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView();

		String[] uPkArr = param.get("uPk").toString().split("\\:");	//고객:판매자:관리자 코드
		String[] pUpkArr = param.get("pUpk").toString().split("\\:");	//고객:판매자:관리자 아이디
		String cmPk = MapUtils.getString(param, "cmPk", "");

		String customerUPk = uPkArr[0];    //고객코드
		String sellerUPk = uPkArr[1];   //판매자코드
		String adminUPk = uPkArr[2];   //관리자코드

		String customerId = pUpkArr[0];    //고객아이디
		String sellerId = pUpkArr[1];    //판매자아이디
		String adminId = pUpkArr[2];    //관리자아이디

		String svcCd = "SVC10";	//param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
		Map<String, Object> chatMasterInfo = chatService.selectChatMasterInfo(param);

		//String stndCd = chatMasterInfo.get("R_STND_CD").toString();		//상품번호
		//String cmChatType = chatMasterInfo.get("CM_CHAT_TYPE").toString();
		
		String stndCd = "";
		String cmChatType = "";
		try {
			stndCd = chatMasterInfo.get("R_STND_CD").toString();        //상품번호
			cmChatType = chatMasterInfo.get("CM_CHAT_TYPE").toString();
		}catch (Exception e){
			stndCd = "";
			cmChatType = "";
		}
		/*
		HashMap<String, Object> paramList = new HashMap<String, Object>();
		paramList.put("pks", param.get("uPk").toString());
		List<Map<String, Object>> resonseData = chatService.getAvgResponse(paramList);
		if(resonseData.size() == 0){
			paramList.put("rTime", "15:15:15");
			chatService.insertAvgResponseTime(paramList);
			mv.addObject("customerAvgResTime", "15");
			mv.addObject("sellerAvgResTime", "15");
			mv.addObject("adminAvgResTime", "15");
		}else{
			String[] responseInfo = resonseData.get(0).get("CRT_R_TIME").toString().split("\\:");
			mv.addObject("customerAvgResTime", responseInfo[0]);
			mv.addObject("sellerAvgResTime", responseInfo[1]);
			mv.addObject("adminAvgResTime", responseInfo[2]);
		}
				*/
		mv.addObject("stndCd", stndCd); // 상품번호
		mv.addObject("pUid", stndCd); // 상품번호
		mv.addObject("svcCd", svcCd); // 접속 서비스 코드 (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
		mv.addObject("userType", 11);
		mv.addObject("uPk", adminUPk);
		mv.addObject("customerUPk", customerUPk);
		mv.addObject("sellerUPk", sellerUPk);
		mv.addObject("cmChatType", cmChatType);
		mv.addObject("customerId", customerId);
		mv.addObject("sellerId", sellerId);
		mv.addObject("uPkArr", String.join(",", uPkArr));
		mv.addObject("adminId", adminId);
		mv.addObject("cmPk", cmPk);

		mv.setViewName("admin/admin");

		return mv;
	}

    /**
     * Disposition 지정하기.
     * @param fileName
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

