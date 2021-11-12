package castingn.com.chat.web;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import castingn.com.chat.ChatVo;
import castingn.com.chat.service.ChatService;

@RestController
public class ChattingController {

	@Autowired
	private ChatService chatService;


	@Autowired
    private SimpMessageSendingOperations messagingTemplate;

	 @Value("${castingn.fileStorePath}")
	 private String fileStorePath;


	 String getResonseTimeString(int chatCnt, int diffTime){

		 String timeString="15";
		 if(chatCnt == 0 || diffTime == 0){
			 timeString = "15";
		 }else {
			 int avgTime = diffTime / chatCnt;
			 int avgSec = avgTime % 60;
			 avgTime /= 60;
			 int avgMin = avgTime % 60;
			 avgTime /= 60;
			 int avgHour = avgTime % 24;
			 avgTime /= 24;
			 int avgDays = avgTime % 30;
			 long avgMonths = avgTime % 12;
			 long avgYears = avgTime / 12;

			 if(avgMin > 600){
				 timeString = "600";
			 }
			 else if(avgMin == 0){
				 timeString = "1";
			 }
//			 if (avgHour > 0) {
//				 timeString += avgHour + "시간 ";
//			 }
//			 if (avgMin > 0) {
//				 timeString += avgMin + "분 ";
//			 }
		 }

		 return timeString;
	 }


	 @RequestMapping("/getResponseInfo")
		public Map<String, Object> getResponseInfo(@RequestParam Map<String, Object> param) throws Exception {
			Map<String, Object> ret = new HashMap<String, Object>();

			String whereSql = "";
			String type = param.get("type").toString();
			String uPk = param.get("uPk").toString();

			String stndCd = param.get("stndCd").toString();		//상품번호
			String cmChatType = param.get("cmChatType").toString();		//채팅방 유형(CR01:담당자-고객, CR02:담당자-파트너, CR03:고객-파트너, CR04:담당자-고객-파트너)
			String svcCd = param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
			String userType = param.get("userType").toString();	//1:고객, 4:파트너

			String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK 존재유무 확인
			if(cmPk == null){
				ret.put("data", null); // 채팅방 이전 채팅내역
				return ret;
			}

			Map<String, Object> paramList = new HashMap<String, Object>();
			paramList.put("cmPk", cmPk);

			return ret;
		}


	@RequestMapping("/getChatListByMessageType")
	public Map<String, Object> getChatListByMessageType(@RequestParam Map<String, Object> param) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();

		String whereSql = "";
		String type = param.get("type").toString();
		String uPk = param.get("uPk").toString();

		String stndCd = param.get("stndCd").toString();		//상품번호
		String cmChatType = param.get("cmChatType").toString();		//채팅방 유형(CR01:담당자-고객, CR02:담당자-파트너, CR03:고객-파트너, CR04:담당자-고객-파트너)
		String svcCd = param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
		String userType = param.get("userType").toString();	//1:고객, 4:파트너

		//String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK 존재유무 확인
		//if(cmPk == null){
		//	ret.put("data", null); // 채팅방 이전 채팅내역
		//	return ret;
		//}

		Map<String, Object> paramList = new HashMap<String, Object>();
		//paramList.put("cmPk", cmPk);
		paramList.put("uPk", uPk);

		//int exitId = 0;
		//Map<String, Object> exitInfo = chatService.getLastExitMessageId(paramList);
		//if(exitInfo != null){
		//	exitId = Integer.valueOf(exitInfo.get("CD_PK").toString());
		//}

		// 고객
		if(userType.equals("1")){
			if(type.equals("unread")){ //안읽은 메시지
				whereSql += " CDR.CDR_READ_YN = 'N' AND CDR.CDR_U_PK = " + uPk;
			}else if (type.equals("transaction")){ //거래중 메시지
				whereSql += " CDR.CDR_READ_YN = 'Y' AND CUM.CM_CHAT_STAT='1' ";
			}else if (type.equals("important")){ //중요메시지
				whereSql += " CL.CL_U_PK = " + uPk;
			}
			else if (type.equals("exit")){	//완료 메시지
				whereSql += " CUM.CM_CHAT_STAT='0' ";
			} else {
				whereSql += " 0=0 ";
			}
		}
		else if(userType.equals("4")){ // 판매자
			if(type.equals("talking_request")){
				whereSql += " CUM.CU_U_PK = '"+ uPk +"' AND CD.CD_MSG_TYPE = 'service_question' AND CUM.CM_CHAT_STAT = '1' ";
			}
			else if(type.equals("talking_run")){
				whereSql += " CUM.CU_U_PK = '" + uPk + "' AND CD.CD_MSG_TYPE = 'normal' AND CUM.CM_CHAT_STAT = '1'";
			}
			else if(type.equals("estimate_send")){
				whereSql += " CUM.CU_U_PK = '" + uPk + "' AND CD.CD_MSG_TYPE = 'estimate_send' AND CUM.CM_CHAT_STAT = '1'";
			}
			else if(type.equals("estimate_select")){
				whereSql += " CUM.CU_U_PK = '" + uPk + "' AND CD.CD_MSG_TYPE = 'estimate_select' AND CUM.CM_CHAT_STAT = '1'";
			}
			else if(type.equals("settle_ok")){
				whereSql += " CUM.CU_U_PK = '" + uPk + "' AND CD.CD_MSG_TYPE = 'settle_ok' AND CUM.CM_CHAT_STAT = '1'";
			}
			else if(type.equals("exit")){ //종료된 채팅
				whereSql += " CUM.CU_U_PK = '" + uPk + "' AND CUM.CM_CHAT_STAT='0' ";
			}
			else if(type.equals("important")){
				whereSql += " CL.CL_U_PK = " + uPk;
			}else {
				whereSql += " 0=0 ";
			}
		}

		param.put("where", whereSql);

		if( "4".equals(param.get("userType").toString()) ) {
			param.put("userType", "1");
		} else {
			param.put("userType", "4");
		}



		List<Map<String, Object>> data = chatService.getChatListByMessageType(param);

		for (Map<String, Object> item : data) {
			String chatStat = item.get("CD_CHAT_STAT").toString();
			if(chatStat.equals("CHAT")){
				String html = item.get("CD_CONTENT").toString();
				String removeHtml = html.replaceAll("<[^>]+>", "").trim();
				item.replace("CD_CONTENT", removeHtml);
			}
		}

		ret.put("data", data);
		ret.put("state", 200); // 상태
		return ret;
	}

	@RequestMapping("/setChangeChatRoom")
	public Map<String, Object> setChangeChatRoom(@RequestParam Map<String, Object> param) throws Exception {

		//System.out.println("chat room chage : "+param.toString());
		Map<String, Object> map = chatService.selectChatMasterUserInfo(param);

		String cmPk = map.get("CM_PK").toString();
		String stndCd = map.get("R_STND_CD").toString();		//상품번호
		String svcCd = map.get("CU_SVC_CD").toString();	//SVC01: 일반회원, SVC02:plaza회원
		String[] uPkArr = map.get("CM_PU_PK").toString().split("\\:");	//고객:판매자코드
//		String sellerUPk = param.get("sellerUPk").toString();	//공급사의 회원아이디
		String cmChatType = map.get("CM_CHAT_TYPE").toString();
		String pUpk = map.get("CM_P_PK").toString();	//고객아이디:판매자아이디
		String pUid = map.get("R_STND_CD").toString(); //상품아이디
		String customerUpk = uPkArr[0];    //고객코드
		String sellerUPk = uPkArr[1];   //판매자코드
		String[] pUpkArr = pUpk.split(":");
		String customerId = pUpkArr[0];    //고객아이디
		String sellerId = pUpkArr[1];    //판매자아이디

		map.put("cmPk", cmPk);
		map.put("stndCd", stndCd); // 상품번호
		map.put("svcCd", svcCd); // 접속 서비스 코드 (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
		map.put("userType", 1);
		map.put("uPk", customerUpk);
		map.put("sellerUPk", sellerUPk);
		map.put("cmChatType", cmChatType);
		map.put("customerId", customerId);
		map.put("sellerId", sellerId);
		map.put("uName", map.get("CU_U_NAME"));
		map.put("pUid", pUid);
		map.put("uPkArr", String.join(":", uPkArr));

		return map;
	}

	@MessageMapping("/chat.test_gv")
	public void testGV(@Payload ChatVo chatVo, SimpMessageHeaderAccessor headerAccessor) throws Exception {

		messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
	}

	/**
	 * 찜하기
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/likeChat")
	public ModelAndView likeChat(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		chatService.likeChat(param);
		mv.addObject("status", "1");
		return mv;
	}


	/**
	 * 검색
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/search")
	public ModelAndView search(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

        List<Map<String, Object>> data = chatService.search(param);

        for (Map<String, Object> item : data) {
            String chatStat = item.get("CD_CHAT_STAT").toString();
            if(chatStat.equals("CHAT")){
                String html = item.get("CD_CONTENT").toString();
                String removeHtml = html.replaceAll("<[^>]+>", "").trim();
                item.replace("CD_CONTENT", removeHtml);
            }
        }

		mv.addObject("data", data);
		return mv;
	}


	/**
	 * 회원이 방을 만들거나 이미 만든 방이 있으면 그 방정보를 리턴한다.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.customer.enter.room.post")
	public Map<String, Object> chatEnterRoomPost(@RequestParam Map<String, Object> param) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();

		String stndCd = param.get("stndCd").toString();		//상품번호
		String cmChatType = param.get("cmChatType").toString();		//채팅방 유형(CR01:담당자-고객, CR02:담당자-파트너, CR03:고객-파트너, CR04:담당자-고객-파트너)
		String svcCd = param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
		String uPk = param.get("uPk").toString();	//회원아이디
		String uPkArr = param.get("uPkArr").toString();	//고객,판매자코드
		String pUid = param.get("pUid").toString();		//상품아이디
		String cmPk = MapUtils.getString(param, "cmPk", "");
		String uName = param.get("uName").toString();
		String uType = param.get("userType").toString();
		String sName = param.get("sName").toString();
		String user_logo = ""; 
		if(param.get("userLogo") != null ) {
			param.get("userLogo").toString();
		}
		String seller_logo = param.get("sellerLogo").toString();

		//고객:판매자 고유코드
		//param.put("uUpk", uPkArr.replace(",", "\\:"));

//		Map<String, Object> userInfo = chatService.selectUserInfo(param); // 회원정보

		if(cmPk.isEmpty()) {
			cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK 존재유무 확인
		}

		String isRoomCreate = "0";

		if(cmPk == null) {

			String cmPkNum = chatService.selectCmPkNum(param) + "";
			cmPk = stndCd + "_" + cmPkNum; // CHAT_MASTER.CM_PK 생성
			param.put("cmPk", cmPk); // 채팅방 Pk(CHAT_MASTER.CM_PK)
			param.put("cmChatType", cmChatType);

			chatService.insertChatMaster(param);
			isRoomCreate = "1";

			//String[] uPkList = uPkArr.split(",");
			String[] uPkList = uPkArr.split("\\:");
			param.put("pUpk", uPkList[1]);

			for(int i=0; i < uPkList.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uPk", uPkList[i]);
				map.put("cmPk", cmPk);
				map.put("svcCd", svcCd);
				if(i==0) {
					map.put("userType", "1");
					map.put("uName", uName);
					map.put("userLogo", user_logo);
				}else {
					map.put("userType", "4");
					map.put("uName", sName);
					map.put("userLogo", seller_logo);
				}

				//System.out.println("create test : "+param);
				chatService.insertChatUser(map);
				chatService.updateChatUserInLogo(map);
			}


		} else {
			param.put("cmPk", cmPk);

			String[] uPkList = uPkArr.split("\\:");

			for(int i=0; i < uPkList.length; i++) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uPk", uPkList[i]);
				map.put("cmPk", cmPk);
				map.put("svcCd", svcCd);
				if(i == 0) {
					map.put("userType", "1");
					map.put("uName", uName);
					map.put("userLogo", user_logo);
				} else if(i == 1){
					map.put("userType", "4");
					map.put("uName", sName);
					map.put("userLogo", seller_logo);
				} else {
					map.put("userType", "11");
					map.put("uName", uName);
					map.put("userLogo", user_logo);
				}

				chatService.updateChatUser(map);
			}

		}

//		if(userInfo != null) {

//		}

		ret.put("state", 200); // 상태
		ret.put("cmPk", cmPk); // 채팅방 PK
		ret.put("path", cmPk); // 채팅방 경로(채팅방 PK)
		ret.put("svcCd", svcCd); // 접속 서비스 코드 (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
		ret.put("cmChatType", cmChatType);
		ret.put("userType", 1);
		ret.put("uPk", uPk);
//		ret.put("uName", String.valueOf(userInfo.get("U_NAME")));
		ret.put("isRoomCreate", isRoomCreate);

		return ret;
	}

	/**
	 * 채팅이력을 로드한다.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.get.chat.list.post")
	public ModelAndView getChatListPost(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		String stndCd = param.get("stndCd").toString();		//상품번호
		String cmChatType = param.get("cmChatType").toString();		//채팅방 유형(CR01:담당자-고객, CR02:담당자-파트너, CR03:고객-파트너, CR04:담당자-고객-파트너)
		String svcCd = param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
		String uPk = param.get("uPk").toString();	//회원아이디
		String userType = param.get("userType").toString();	//1:고객, 4:파트너, 11:관리자.
		String uName = MapUtils.getString(param, "uName", "");
		param.put("uName", uName);

		String cmPk = MapUtils.getString(param, "cmPk", "");

		if(cmPk.isEmpty() && userType.equals("1")) {	//고객은 무조건 cmPK로 검색
			cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK 존재유무 확인
		}

		if(cmPk == null) {
			mv.addObject("chatHistList", null); // 채팅방 이전 채팅내역
			return mv;
		}

		Map<String, Object> paramList = new HashMap<String, Object>();
		paramList.put("uPk", uPk);
		paramList.put("cmPk", cmPk);

		//if(userType.equals("4")){
		//	Map<String, Object> castingnTalkInfo = chatService.getCDPKByExit(paramList);
		//	if(castingnTalkInfo != null && Integer.valueOf(castingnTalkInfo.get("CD_PK").toString())> 0)
		//		paramList.put("castingnCdpk", castingnTalkInfo.get("CD_PK").toString());
		//}

		//유저 정보 입력
		if( !"".equals(cmPk) && stndCd != null && !"".equals(stndCd) && (userType.equals("4") || userType.equals("11"))){
			try {
				int cnt = chatService.selectChatUserCnt(param);
				if(cnt <= 0) {
					chatService.insertMallChatUser(param);
				}else {
					chatService.updateChatMallUser(param);
				}
			}catch(Exception e) {

			}
		}

		List<Map<String, Object>> tmpChatList = chatService.selectChatHistList(paramList);
		ArrayList<Map<String, Object>> chatList = new ArrayList<Map<String, Object>>();

		String lastDate = "";
		if(tmpChatList.size() > 0){
			Map<String, Object> firstObj = tmpChatList.get(0);
			String[] firstDateArr = firstObj.get("INDATE").toString().split(" ");
			Map<String, Object> timeObj = getTimeMap(firstDateArr[0]);
			lastDate = timeObj.get("CD_CONTENT").toString();
			chatList.add(timeObj);
		}

		//어제 날짜 얻기
		Calendar yesterday=Calendar.getInstance();
		yesterday.add(Calendar.DATE,-1);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("a h:mm");
		String yesterdayStr = sdf1.format(yesterday.getTime());


		for (Map<String, Object> item : tmpChatList) {
			String timeStr = getKoreanTimeType(item.get("INDATE").toString());
			String[] dateTimeArr = item.get("INDATE").toString().split(" ");

			if(yesterdayStr.equals(dateTimeArr[0])){
				item.put("INTIME", "어제 " + timeStr);
			}
			else{
				item.put("INTIME", timeStr);
			}

			//이전 날짜와 다르면
			if(!dateTimeArr[0].equals(lastDate)){
				Map<String, Object> timeObj = getTimeMap(dateTimeArr[0]);
				chatList.add(timeObj);
				lastDate = dateTimeArr[0];
			}

			chatList.add(item);
		}



		mv.addObject("chatHistList", chatList); // 채팅방 이전 채팅내역
		//mv.addObject("chatHistList", chatService.selectChatHistList(paramList)); // 채팅방 이전 채팅내역

		//left panel에 출력할 정보
		int totalCount = chatService.getTotalCountInRoom(paramList);
		mv.addObject("totalCount", totalCount);		//전체 갯수
		paramList.put("userType", userType);



		//==================== 추가내용 ======================================

		if(!userType.equals("11")) {
			paramList.put("userType", userType);
			paramList.put("where", "CD_U_PK != " + uPk);
			List<Map<String, Object>> data = chatService.getChatInRoom(paramList);
			int totalChatCnt = data.size();

			paramList.replace("where", "1 = 1");
			data = chatService.getChatInRoom(paramList);

			int sendChatCnt = 0;
			for (int i = 0; i < data.size() - 1; i++) {
				Map<String, Object> item = data.get(i);
				Map<String, Object> nextItem = data.get(i + 1);
				int item_Upk = Integer.valueOf(item.get("CD_U_PK").toString());
				int nextItem_Upk = Integer.valueOf(nextItem.get("CD_U_PK").toString());

				if (item_Upk != nextItem_Upk && nextItem_Upk == Integer.valueOf(uPk)) {
					sendChatCnt++;
				}
			}

			double responseInfo = 0;
			if (totalChatCnt == 0 || sendChatCnt == 0) responseInfo = 0;
			else responseInfo = (double) sendChatCnt / totalChatCnt * 100;
			mv.addObject("responseInfo", responseInfo);
		}



		//==================== 추가내용  끝 ========================





		/*

		List<Map<String, Object>> data = chatService.getChatInRoom(paramList);

		int cnt = 0 ;
		int diffTime = 0;
		for(int i = 0 ; i < data.size() - 1 ; i++){
			Map<String, Object> item = data.get(i);
			Map<String, Object> nextItem = data.get(i + 1);
			int item_Upk = Integer.valueOf(item.get("CD_U_PK").toString());
			int nextItem_Upk = Integer.valueOf(nextItem.get("CD_U_PK").toString());

			Timestamp item_date = Timestamp.valueOf(item.get("CD_INDATE").toString());
			Timestamp nextItem_date = Timestamp.valueOf(nextItem.get("CD_INDATE").toString());


			if(item_Upk != nextItem_Upk){
				cnt ++;
				diffTime += ((nextItem_date.getTime() - item_date.getTime()) / 1000);
			}
		}

		System.out.println("=====================");
		System.out.println(diffTime + "/" + cnt);
		System.out.println("=====================");

		String timeString="";
		if(cnt == 0){
			timeString = "0분";
		}else {
			int avgTime = diffTime / cnt;
			int avgSec = avgTime % 60;
			avgTime /= 60;
			int avgMin = avgTime % 60;
			avgTime /= 60;
			int avgHour = avgTime % 24;
			avgTime /= 24;
			int avgDays = avgTime % 30;
			long avgMonths = avgTime % 12;
			long avgYears = avgTime / 12;

			if (avgHour > 0) {
				timeString += avgHour + "시간 ";
			}
			if (avgMin > 0) {
				timeString += avgMin + "분 ";
			}
		}
		mv.addObject("responseInfo", timeString);

		*/


		if(userType.equals("1")){	//고객이면
			//paramList = new HashMap<String, Object>();

			paramList = new HashMap<String, Object>();
			paramList.put("cmPk", cmPk);
			paramList.put("uPk", uPk);

			int unreadCount = chatService.selectChatMessageReadCnt(paramList);
			mv.addObject("unreadCount", unreadCount);	//안읽은 갯수

			//int exitId = 0;
			//Map<String, Object> exitInfo = chatService.getLastExitMessageId(paramList);
			//if(exitInfo != null){
			//	exitId = Integer.valueOf(exitInfo.get("CD_PK").toString());
			//}

			//paramList.put("exitId", exitId);
			int exitAfterCount = chatService.getExitAfterCnt(paramList);
			mv.addObject("transactionCount", exitAfterCount);	//거래중 갯수

			int exitBeforeCount = chatService.getExitBeforeCnt(paramList);
			mv.addObject("cnt_exit", exitBeforeCount);	//완료 갯수

			int importantCount = chatService.getImportantCnt(paramList);
			mv.addObject("cnt_important", importantCount);	//중요 갯수
		}
		else if(userType.equals("4")){	//판매자이면
			paramList = new HashMap<String, Object>();
			paramList.put("cmPk", cmPk);
			paramList.put("uPk", uPk);
			int talkingRequestCount = chatService.getTalkingRequestCnt(paramList);
			mv.addObject("talkingRequestCount", talkingRequestCount);	//상담요청 갯수

			int talkingRunCount = chatService.getTalkingRunCnt(paramList);
			mv.addObject("talkingRunCount", talkingRunCount);	//상담진행중 갯수

			int estimateSendCount = chatService.getEstimateSendCnt(paramList);
			mv.addObject("estimateSendCount", estimateSendCount);	//견적요청 갯수

			int estimateSelectCount = chatService.getEstimateSelectCnt(paramList);
			mv.addObject("estimateSelectCount", estimateSelectCount);	//결제진행중 갯수

			int settleOkCount = chatService.getSettleOkCnt(paramList);
			mv.addObject("settleOkCount", settleOkCount);	//결제완료 갯수

			//int exitId = 0;
			//Map<String, Object> exitInfo = chatService.getLastExitMessageId(paramList);
			//if(exitInfo != null){
			//	exitId = Integer.valueOf(exitInfo.get("CD_PK").toString());
			//}

			//paramList.put("exitId", exitId);
			int exitBeforeCount = chatService.getExitBeforeCnt(paramList);
			mv.addObject("cnt_exit", exitBeforeCount);	//완료 갯수

			int importantCount = chatService.getImportantCnt(paramList);
			mv.addObject("cnt_important", importantCount);	//중요 갯수

		}

		mv.addObject("path", cmPk);
		mv.addObject("cmPk", cmPk);

		return mv;
	}


	public Map<String, Object> getTimeMap(String dateTimeStr){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("U_NAME", "");
		map.put("CD_PK", "");
		map.put("CD_U_PK", "");
		map.put("CD_CONTENT", "");

		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		Date dateTime = null;
		try {
			dateTime = df.parse(dateTimeStr);
		} catch (Exception e) {
		}
		if(dateTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			map.put("CD_CONTENT", sdf.format(dateTime));
		}
		map.put("CD_ORG_FILE_NM", "");
		map.put("CD_NEW_FILE_NM", "");
		map.put("CD_FILE_PATH", "");
		map.put("CD_CHAT_STAT", "CHAT");
		map.put("CD_MSG_TYPE", "date_line");
		map.put("INDATE", "");
		map.put("CDR_READ_CNT", "");

		return map;
	}

	public String getKoreanTimeType(String dateTime){
		SimpleDateFormat sdf2 = new SimpleDateFormat("a h:mm");
		String[] dateTimeArr = dateTime.split(" ");
		Date time = null;
		String timeStr = "";
		try {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			time = df.parse(dateTimeArr[1]);
		} catch (Exception e) {
		}
		if(time != null) {
			timeStr = sdf2.format(time);
			timeStr = timeStr.replaceAll("AM", "오전");
			timeStr = timeStr.replaceAll("PM", "오후");
		}
		return timeStr;
	}

	/**
	 * 채팅파일이력을 로드한다.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.get.chatfile.list.post")
	public ModelAndView getChatFileListPost(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		String stndCd = param.get("stndCd").toString();		//상품번호
		String cmChatType = param.get("cmChatType").toString();		//채팅방 유형(CR01:담당자-고객, CR02:담당자-파트너, CR03:고객-파트너, CR04:담당자-고객-파트너)
		String svcCd = param.get("svcCd").toString();	//SVC01: 일반회원, SVC02:plaza회원
		String uPk = param.get("uPk").toString();	//회원아이디
		String userType = param.get("userType").toString();	//1:고객, 4:파트너

		String cmPk = param.get("cmPk").toString();
		//String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK 존재유무 확인
		if(cmPk == null){
			mv.addObject("chatHistList", null); // 채팅방 이전 채팅내역
			return mv;
		}

		Map<String, Object> paramList = new HashMap<String, Object>();
		paramList.put("cmPk", cmPk);
		paramList.put("isGroup", true);

		//mv.addObject("chatHistList", chatService.selectChatFileHistList(paramList)); // 채팅방 이전 채팅내역

		List<Map<String, Object>> chatList = chatService.selectChatFileHistList(paramList);

		for (Map<String, Object> item : chatList) {
			paramList.replace("isGroup", false);
			paramList.put("inDate", item.get("CD_INDATE"));

			List<Map<String, Object>> chatSubList = chatService.selectChatFileHistList(paramList);
			item.put("list", chatSubList);
		}

		mv.addObject("chatHistList", chatList); // 채팅방 이전 채팅내역
		mv.addObject("path", cmPk);
		mv.addObject("cmPk", cmPk);

		return mv;
	}



	/**
	 * 의뢰건별 채팅방 접속
	 * @param chatVo
	 * @param headerAccessor
	 * @throws Exception
	 */
	@MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatVo chatVo, SimpMessageHeaderAccessor headerAccessor) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("cmPk", chatVo.getPath());   // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
		param.put("uPk", chatVo.getUserPk());  // USER_INFO.U_PK (채팅방 참여자 PK )
		param.put("svcCd", chatVo.getSvcCd()); // 접속 서비스 구분 코드(SVC01 : mall.castingn.com, SVC02: www.castingn.com/admin.castingn.com)

		chatVo.setContent(chatVo.getUserNm() + " 님이 채팅방에 참여하셨습니다.");

		param.put("content", chatVo.getContent());
		param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
		param.put("msgType", chatVo.getMsgType());

		int readCnt = chatService.selectChatMessageReadCnt(param);

		chatService.updateChatDetailRead(param); // 채팅 내용 읽기 여부 확인 (CDR_READ_YN update)

		chatVo.setReadCnt(readCnt);

		chatVo.setInDate(getTimeStamp2());


		chatService.insertChatDetail(param); // 채팅내용 등록

        headerAccessor.getSessionAttributes().put("username", chatVo.getUserNm());
        headerAccessor.getSessionAttributes().put("path", chatVo.getPath());
        headerAccessor.getSessionAttributes().put("uPk", chatVo.getUserPk());

        messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
    }

	/**
	 * 메세지 전송
	 * @param chatVo
	 * @throws Exception
	 */
	@MessageMapping("/chat.sendMessage")
	/* @SendTo("/topic/{chatRoomId}") */
    public void sendMessage(@Payload ChatVo chatVo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();


		//금칙어 검사 고객과판매자 만 금칙어 계산을 합니다.
		if(chatVo.getUserType().equals("1") || chatVo.getUserType().equals("4")) {
			if(chatVo.getContent() != null && !chatVo.getContent().equals("")) {
				boolean isForbidFind = false;
				String chatContent = chatVo.getContent();
				//금칙어 리스트 얻기
				List<Map<String, Object>> forbidList = chatService.selectForbidList();
				for (Map<String, Object> item : forbidList) {
					String forbidWord = item.get("FORBID_WORD").toString();
					if (chatContent.contains(forbidWord)) {
						isForbidFind = true;
						break;
					}
				}

				if (isForbidFind) {    //금칙어가 있으면
					chatVo.setUserStatus(chatVo.getUserStatus().FAILED);
					chatVo.setContent("forbid_word");
					messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
					return;
				}
			}
		}

		String msgType = chatVo.getMsgType();
		String fileName = chatVo.getFileName();

		String userType = chatVo.getUserType();

		if(msgType.equals("exit") && userType.equals("11")){

			Map<String, Object> paramList = new HashMap<String, Object>();
			paramList.put("cmPk", chatVo.getCmPk());
			paramList.put("where", "1 = 1");
			List<Map<String, Object>> data = chatService.getChatInRoom(paramList);

			//채팅방에 속한 유저들을 호출합니다.
			List<Map<String, Object>> userUpk = chatService.selectChatUserInfo(paramList);

			int adminChatCnt = 0, customerChatCnt = 0, sellerChatCnt = 0;
			int adminDiffTime = 0, customerDiffTime = 0, sellerDiffTime = 0;
			String[] uPkArr = new String[3];
			for(Map<String, Object> map : userUpk) {
				String uType = map.get("CU_U_TYPE").toString();
				int inType = 0;
				switch(uType) {
				case "1":
					inType = 0;
					break;
				case "4":
					inType = 1;
					break;
				case "11":
					inType = 2;
					break;
				}
				uPkArr[inType] = map.get("CU_U_PK").toString();
			}
			//String[] uPkArr =  chatVo.getPks().split("\\,");

			for (int i = 0; i < data.size() - 1; i++) {
				Map<String, Object> item = data.get(i);
				Map<String, Object> nextItem = data.get(i + 1);
				int item_Upk = Integer.valueOf(item.get("CD_U_PK").toString());
				int nextItem_Upk = Integer.valueOf(nextItem.get("CD_U_PK").toString());

				Timestamp item_date = Timestamp.valueOf(item.get("CD_INDATE").toString());
				Timestamp nextItem_date = Timestamp.valueOf(nextItem.get("CD_INDATE").toString());

				if (item_Upk != nextItem_Upk) {
					if(nextItem_Upk == Integer.valueOf(uPkArr[0])) { //고객
						customerChatCnt++;
						customerDiffTime += ((nextItem_date.getTime() - item_date.getTime()) / 1000);
					}
					else if(item_Upk == Integer.valueOf(uPkArr[0]) && nextItem_Upk == Integer.valueOf(uPkArr[1])) { //판매자
						sellerChatCnt++;
						sellerDiffTime += ((nextItem_date.getTime() - item_date.getTime()) / 1000);
					}
					else if(item_Upk == Integer.valueOf(uPkArr[0]) && nextItem_Upk == Integer.valueOf(uPkArr[2])) { //관리자
						adminChatCnt++;
						adminDiffTime += ((nextItem_date.getTime() - item_date.getTime()) / 1000);
					}
				}
			}

			String customerResString = getResonseTimeString(customerChatCnt, customerDiffTime);
			String sellerResString = getResonseTimeString(sellerChatCnt, sellerDiffTime);
			String adminResString = getResonseTimeString(adminChatCnt, adminDiffTime);

			for(Map<String, Object> map : userUpk) {

				String uType = map.get("CU_U_TYPE").toString();
				switch(uType) {
				case "1":
					map.put("rTime", customerResString);
					break;
				case "4":
					map.put("rTime", sellerResString);
					break;
				case "11":
					map.put("rTime", adminResString);
					break;
				}

				chatService.updateAvgResTime(map);
			}
			/*
			paramList = new HashMap<String, Object>();
			String pks = chatVo.getPks().replace(',', ':');
			paramList.put("pks", pks);
			paramList.put("rTime", customerResString + ":" + sellerResString + ":" + adminResString);
			chatService.updateAvgResponseTime(paramList);
			*/
		}

		FileOutputStream fOut = null;

		try {
			if(fileName!= null && fileName != "") {
				if ((msgType.equals("estimate_send") || msgType.equals("estimate_update")) || msgType.equals("request_send")) {

					byte[] decodedBytes = Base64.getDecoder().decode(chatVo.getFile().toString());

					int index = fileName.lastIndexOf(".");
					String fileExt = fileName.substring(index + 1); // 첨부파일 확장자
					String orgName = fileName.split("." + fileExt)[0];
					String fileNewName = orgName + "_" + getTimeStamp() + "." + fileExt;

					File file = new File(fileStorePath);

					if (!file.exists()) {
						file.mkdirs();
					}

					chatVo.setFilePath(fileStorePath + fileNewName);
					chatVo.setFileNewName(fileNewName);

					fOut = new FileOutputStream(fileStorePath + fileNewName);
					fOut.write(decodedBytes);
				}
			}

		} catch(IOException e) {

			chatVo.setUserStatus(chatVo.getUserStatus().FAILED); // 사용자 채팅 상태

		}finally {

			try {

				if(fileName!= null && fileName != "") {
					fOut.close();
				}

				String nowDateTimeStr = getTimeStamp2();
				String[] nowDateTimeArr = nowDateTimeStr.split(" ");

				if(chatVo.getLastDateTime().equals("")){
					ChatVo dateLineChatVo = new ChatVo();
					dateLineChatVo.setPath(chatVo.getPath());
					dateLineChatVo.setContent(nowDateTimeArr[0]);
					dateLineChatVo.setMsgType("date_line");
					dateLineChatVo.setUserStatus(dateLineChatVo.getUserStatus().CHAT);
					messagingTemplate.convertAndSend("/topic/" + dateLineChatVo.getPath(), dateLineChatVo);
				}
				else{
					String[] lastDateTimeArr = chatVo.getLastDateTime().split(" ");
					if(!lastDateTimeArr[0].equals(nowDateTimeArr[0])){
						ChatVo dateLineChatVo = new ChatVo();
						dateLineChatVo.setPath(chatVo.getPath());
						dateLineChatVo.setContent(nowDateTimeArr[0]);
						dateLineChatVo.setMsgType("date_line");
						dateLineChatVo.setUserStatus(dateLineChatVo.getUserStatus().CHAT);
						messagingTemplate.convertAndSend("/topic/" + dateLineChatVo.getPath(), dateLineChatVo);
					}
				}

				param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
				param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (채팅방 참여자 PK )

				param.put("content", chatVo.getContent());
				param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
				param.put("msgType", chatVo.getMsgType());
				param.put("orgFileNm", chatVo.getFileName()); // 원본 파일명
				param.put("newFileNm", chatVo.getFileNewName()); // 저장 파일명
				param.put("filePath" , chatVo.getFilePath()); // 파일 저장 경로
				param.put("prPk" , chatVo.getPrPk()); // 요구사항
				param.put("paPk" , chatVo.getPaPk()); // 견적

				//추가
				param.put("uType", chatVo.getUserType());
				chatVo.setReadCnt(chatService.insertChatDetail(param)); // 채팅내용 등록


				//내용전송 일때만
				if(chatVo.getMsgType().equals("normal")) {
					//체크함
					kakao_msg(param);
				}

				//채팅방 종료이면 MASTER에도 적어주자
				if(chatVo.getMsgType().equals("exit")) {
					param.put("cmStat", "0");
					chatService.updateStatusChange(param);
				} else if(chatVo.getMsgType().equals("restart")) {
					param.put("cmStat", "1");
					chatService.updateStatusChange(param);
				} else if(chatVo.getMsgType().equals("serviceSelect")
						|| chatVo.getMsgType().equals("service_question")
						|| chatVo.getMsgType().equals("simpleAnswer") ) {
					param.put("qCmPk", chatVo.getPath());
					param.put("qPuid", chatVo.getqPuid());
					chatService.insertChatServiceQuestion(param);
				} else if(chatVo.getMsgType().equals("serviceCancel")) {
					param.put("qCmPk", chatVo.getPath());
					param.put("qPuid", chatVo.getqPuid());
					param.put("qQuestionStat", "0");
					chatService.updateChatServiceQuestion(param);
				}

				String cdPk = MapUtils.getString(param, "CD_PK", "");
				chatVo.setCdPk(Integer.valueOf(cdPk));

				//chatVo.setInDate(getTimeStamp2());
				chatVo.setInDate(nowDateTimeStr);
				chatVo.setInTime(getKoreanTimeType(nowDateTimeStr));
				messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

	/**
	 * 파일 전송
	 * @param chatVo
	 * @throws Exception
	 */
	@MessageMapping("/chat.sendFile")
	public void sendFile(@Payload ChatVo chatVo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		FileOutputStream fOut = null;

		try {

			String fileName = chatVo.getFileName();
			byte[] decodedBytes = Base64.getDecoder().decode(chatVo.getFile().toString());

			int index = fileName.lastIndexOf(".");
			String fileExt = fileName.substring(index + 1); // 첨부파일 확장자
			String orgName = fileName.split("." + fileExt)[0];
			String fileNewName = orgName + "_" + getTimeStamp() + "." + fileExt;

			File file =  new File(fileStorePath);

			if (!file.exists()) {
				file.mkdirs();
			}

			chatVo.setFilePath(fileStorePath + fileNewName);
			chatVo.setFileNewName(fileNewName);

			fOut = new FileOutputStream(fileStorePath + fileNewName);
			fOut.write(decodedBytes);

			param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
			param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (채팅방 참여자 PK )
			param.put("orgFileNm", chatVo.getFileName()); // 원본 파일명
			param.put("newFileNm", chatVo.getFileNewName()); // 저장 파일명
			param.put("filePath" , chatVo.getFilePath()); // 파일 저장 경로

		} catch(IOException e) {

			chatVo.setUserStatus(chatVo.getUserStatus().FAILED); // 사용자 채팅 상태

		}finally {


			try {

				fOut.close();


				String nowDateTimeStr = getTimeStamp2();
				String[] nowDateTimeArr = nowDateTimeStr.split(" ");

				if(chatVo.getLastDateTime().equals("")){
					ChatVo dateLineChatVo = new ChatVo();

					dateLineChatVo.setPath(chatVo.getPath());
					dateLineChatVo.setContent(nowDateTimeArr[0]);
					dateLineChatVo.setMsgType("date_line");
					dateLineChatVo.setUserStatus(dateLineChatVo.getUserStatus().CHAT);
					messagingTemplate.convertAndSend("/topic/" + dateLineChatVo.getPath(), dateLineChatVo);
				}
				else{
					String[] lastDateTimeArr = chatVo.getLastDateTime().split(" ");
					if(!lastDateTimeArr[0].equals(nowDateTimeArr[0])){
						ChatVo dateLineChatVo = new ChatVo();
						dateLineChatVo.setPath(chatVo.getPath());
						dateLineChatVo.setContent(nowDateTimeArr[0]);
						dateLineChatVo.setMsgType("date_line");
						dateLineChatVo.setUserStatus(dateLineChatVo.getUserStatus().CHAT);
						messagingTemplate.convertAndSend("/topic/" + dateLineChatVo.getPath(), dateLineChatVo);
					}
				}



				param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
				param.put("msgType", "normal");

				chatVo.setReadCnt(chatService.insertChatDetail(param)); // 채팅내용 등록
				//chatVo.setInDate(getTimeStamp2());
				chatVo.setInDate(nowDateTimeStr);
				chatVo.setInTime(getKoreanTimeType(nowDateTimeStr));

				messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		// messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
//        return chatVo;
	}

	/**
	 * 메세지 읽기 확인
	 * @param chatVo
	 * @throws Exception
	 */
	@MessageMapping("/chat.readMessage")
	public void readMessage(@Payload ChatVo chatVo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
		param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (채팅방 참여자 PK )
		param.put("cdPk", ""); // CHAT_DETAIL.CD_PK (채팅 내용 PK )

		/*
		 * List<Map<String, Object>> userList = chatService.selectChatUserList(param);
		 *
		 * String[] uPkArr = new String[userList.size()];
		 *
		 * for(int i=0; i < userList.size(); i++ ) { uPkArr[i] =
		 * userList.get(i).get("uPk").toString(); }
		 *
		 * param.put("uPk", String.join(",", uPkArr));
		 */

		int readCnt = chatService.selectChatMessageReadCnt(param);

		chatService.updateChatDetailRead(param); // 채팅 내용 읽기 여부 확인 (CDR_READ_YN update)

		chatVo.setReadCnt(readCnt);
		chatVo.setInDate(getTimeStamp2());

		messagingTemplate.convertAndSend("/topic/public", chatVo);
	}

	/**
	 * 견적 선택을 누른다.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.select.estimate.post")
	public ModelAndView selectEstimatePost(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		param.put("msgType", "estimate_select");
		chatService.updateMsgTypeDetail(param);

		return mv;
	}

	/**
	 * 견적서 철회
	 * @param chatVo
	 * @throws Exception
	 */
	@MessageMapping("/chat.cancel.estimate")
	public void cancelEstimate(@Payload ChatVo chatVo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cdPk", chatVo.getOtherInfo());
		param.put("msgType", "estimate_cancel");
		param.put("content", chatVo.getContent());
		chatService.updateMsgTypeAndContentDetail(param);

		chatVo.setMsgType("estimate_cancel");
		chatVo.setCdPk(Integer.valueOf(param.get("cdPk").toString()));
		chatVo.setInDate(getTimeStamp2());
		messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
	}

	/**
	 * 견적서 갱신
	 * @param chatVo
	 * @throws Exception
	 */
	@MessageMapping("/chat.update.estimate")
	public void updateEstimate(@Payload ChatVo chatVo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cdPk", chatVo.getOtherInfo());
		param.put("msgType", "estimate_update");
		param.put("content", chatVo.getContent());
		chatService.updateMsgTypeAndContentDetail(param);

		chatVo.setMsgType("estimate_update");
		chatVo.setCdPk(Integer.valueOf(param.get("cdPk").toString()));
		chatVo.setInDate(getTimeStamp2());
		messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
	}

	@RequestMapping("/setSettle")
	public void setSettle(@RequestParam Map<String, Object> param) throws Exception {
		settleChat(param);
	}

	/**
	 * 견적서 갱신
	 * @param param
	 * @throws Exception
	 */
	@MessageMapping("/settle")
	public void settleChat(@RequestParam Map<String, Object> param) throws Exception {

		ChatVo chatVo = new ChatVo();
		String cdPk = param.get("chat_id").toString();
		String isSuccess = param.get("is_success").toString();

		String html = "";
		String settleState = "";
		if(isSuccess.equals("1")){
			html = "<div class=\"message-box message-pink\"><p>결제 완료되었습니다.</p></div>";
			settleState = "settle_ok";
		}
		else{
			html = "<div class=\"message-box message-pink\"><p>결제 취소하였습니다. </p></div>";
			settleState = "settle_cancel";
		}

		Map<String, Object> dbParam = new HashMap<String, Object>();

		dbParam.put("cdPk", cdPk);
		Map<String, Object> paramList = chatService.selectChatDetailInfo(dbParam);

		dbParam.put("cmPk", paramList.get("R_CM_PK").toString()); // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
		dbParam.put("uPk", paramList.get("CD_U_PK").toString()); // USER_INFO.U_PK (채팅방 참여자 PK )
		dbParam.put("content", html);
		dbParam.put("chatStat", "CHAT"); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
		dbParam.put("msgType", settleState);
		dbParam.put("orgFileNm", "");
		dbParam.put("newFileNm", "");
		dbParam.put("filePath", "");
		chatVo.setReadCnt(chatService.insertChatDetail(dbParam)); // 채팅내용 등록

		String nowDateTimeStr = getTimeStamp2();
		chatVo.setCdPk(Integer.valueOf(cdPk));
		chatVo.setCmPk(paramList.get("R_CM_PK").toString());
		chatVo.setUserId(paramList.get("CD_U_PK").toString());
		chatVo.setContent(html);
		chatVo.setMsgType(settleState);
		//chatVo.setInDate(getTimeStamp2());
		chatVo.setInDate(nowDateTimeStr);
		chatVo.setInTime(getKoreanTimeType(nowDateTimeStr));
		chatVo.setPath(paramList.get("R_CM_PK").toString());
		chatVo.setUserStatus(chatVo.getUserStatus().CHAT);

		messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
	}


	/**
	 * 전송 파일 다운로드
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/attachFile/downloadFile.do")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String filePath = request.getParameter("filePath");
//    	String filenameOutput = URLEncoder.encode(request.getParameter("originalFileName"), "UTF-8");
		String filenameOutput = request.getParameter("originalFileName");

		File file = new File(filePath);
		byte b[] = new byte[(int)file.length()];

		setDisposition(filenameOutput, request, response);
		response.setHeader("Content-Length",String.valueOf(file.length()));

		if (file.isFile()) {

			BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());

			int read = 0;

			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}

			outs.close();
			fin.close();
		}
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

	public static String getTimeStamp2() {

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		//String pattern = "yyyy년MM월dd일 hh시mm분";
		String pattern = "yyyy.MM.dd hh:mm";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}

	public void kakao_msg( Map<String, Object> param ) throws Exception{

		//분으로 리턴함
		param.put("restart", "N");
		String msgSendYn = chatService.selectLastChat(param);

		if( msgSendYn != null ) {
			String[] strArr = null;
			if(msgSendYn != null || "".equals(msgSendYn)) {
				strArr = msgSendYn.split(",");
			}

			String[] uPKArr = strArr[1].split(":");

			String uPK = uPKArr[0];
			String cPK = uPKArr[1];
			String R_STND_CD = strArr[3];

			String C_CNT = strArr[4];//파트너 최초 메세지 여부
			String U_CNT = strArr[5];//고객 최초 메세지 여부

			//5분이 넘을 경우, 처음인경우
			//if( Integer.parseInt(strArr[0]) >= 5 || ( "1".equals(C_CNT) ) || ( "1".equals(U_CNT) ) ) {
			if( ( "1".equals(C_CNT) ) || ( "1".equals(U_CNT) ) ) {

				String msg_type = "6";
				String dstaddr = "";
				String callback = "16442653";
				String stat = "0";
				String subject = "";
				String text = "";
				String text2 = text;
				String k_template_code = "";//파트너 받음
				String k_expiretime = "86400";
				String k_next_type = "6";
				String sender_key = "";//파트너 받음
				String k_button_url = "";
				String k_button_name = "";
				String uType = param.get("uType").toString();

				Map<String, Object> mallParam = new HashMap<String, Object>();;

				mallParam.put("uPK", uPK);
				mallParam.put("cPK", cPK);

				boolean kakao_msg = false;
				if( "1".equals(uType) ) { //고객이라면, 최초 문의시

					//수신자 정보 가져오기
					Map<String, Object> userInfo = null;
					userInfo = chatService.selectMallCompanyInfo(mallParam); // 사용자 정보

					if( userInfo != null ) {

						dstaddr = String.valueOf(userInfo.get("C_mobile"));
						subject = "실시간 메시지 도착 알림 메시지";
						text = "[캐스팅엔] 실시간 메시지 도착 알림 메시지 \r\n" + String.valueOf(userInfo.get("C_NAME")) + " / " + String.valueOf(userInfo.get("U_NAME")) +"님,\r\n판매 등록하신 상품에 고객 문의가 도착했습니다.";

						k_button_url = "https://chat.castingn.com:28443/seller?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;
						k_button_name = "메세지 답변하기";

						k_template_code = "SJT_067289";//파트너 받음
						sender_key = "88b5151f1ccdbea9c222f7bb15a25ca4b98aeece";//파트너 받음
						kakao_msg = true;
					}
				} else if("4".equals(uType)) {//셀러

					//수신자 정보 가져오기
					Map<String, Object> userInfo = null;
					userInfo = chatService.selectMallCustomerInfo(mallParam); // 셀러 정보

					if( userInfo != null ) {
						dstaddr = String.valueOf(userInfo.get("U_mobile"));
						subject = "실시간 메시지 도착 알림 메시지";
						text = "[캐스팅엔] 실시간 메시지 도착 알림 메시지 \r\n" + String.valueOf(userInfo.get("U_NAME")) +"님,\r\n고객님께서 문의하신 내용에 대한 답변이 도착했습니다.";

						//text += "\n\n메세지 확인하기";
						//text += "\nhttps://chat.castingn.com:28443/customer?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;

						k_button_url = "https://chat.castingn.com:28443/customer?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;
						k_button_name = "메세지 확인하기";

						k_template_code = "SJT_067292";//고객 받음
						sender_key = "f861a7eef35eaf1cc81bfe7b6b6cf813491ff47d";//고객 받음
						kakao_msg = true;
					}
				} else {//관리자 , 11

				}

				//f861a7eef35eaf1cc81bfe7b6b6cf813491ff47d, 고객 받음
				//SJT_066488 고객 받음, 실시간 메시지

				param.put("msg_type", msg_type);
				param.put("dstaddr", dstaddr);
				param.put("callback", callback);
				param.put("stat", stat);
				param.put("subject", subject);
				param.put("text", text);
				param.put("text2", text2);
				param.put("k_template_code", k_template_code);
				param.put("k_expiretime", k_expiretime);
				param.put("k_next_type", k_next_type);
				param.put("sender_key", sender_key);
				param.put("k_button_name", k_button_name);
				param.put("k_button_url", k_button_url);

				if( kakao_msg ) {
					chatService.kakao_insert(param);
				}


			}
		}

	}

	/**
	 * 서비스 문의내역을 로드한다.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.get.service.list.post")
	public ModelAndView getTopServiceInfo(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		String cmPk = MapUtils.getString(param, "cmPk", "");

		List<Map<String, Object>> qList = null;
		
		try {
			
			param.put("qCmPk", cmPk);
			qList  = chatService.selectChatServiceQuestion(param); // CHAT_MASTER.CM_PK 존재유무 확인

			mv.addObject("data", qList); // 채팅방 이전 채팅내역
			mv.addObject("code", "200"); // 채팅방 이전 채팅내역
			
		}catch(Exception e) {

		}

		return mv;
	}
	
	/**
	 * 상담을 재시작 한다.
	 * @return 
	 * @throws Exception
	 */
	@MessageMapping("/chat.customer.reconnect.room.post")
	public void reconnectRoom(@Payload ChatVo chatVo, SimpMessageHeaderAccessor headerAccessor) throws Exception {
		System.out.println("################################################################");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String nowDateTimeStr = getTimeStamp2();
		chatVo.setInTime(getKoreanTimeType(nowDateTimeStr));
		
		param.put("cmPk", chatVo.getPath());   // CHAT_MASTER.CM_PK (의뢰건별 채팅방 PK)
		param.put("uPk", chatVo.getUserPk());  // USER_INFO.U_PK (채팅방 참여자 PK )
		param.put("svcCd", chatVo.getSvcCd()); // 접속 서비스 구분 코드(SVC01 : mall.castingn.com, SVC02: www.castingn.com/admin.castingn.com)

		chatVo.setContent(chatVo.getUserNm() + " 님이 상담이 재시작되었습니다.");

		param.put("content", chatVo.getContent());
		param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
		param.put("msgType", chatVo.getMsgType());

		param.put("cmStat", "1");
		chatService.updateStatusChange(param); // 채팅방 상태값 변경

		int readCnt = chatService.selectChatMessageReadCnt(param);

		chatService.updateChatDetailRead(param); // 채팅 내용 읽기 여부 확인 (CDR_READ_YN update)

		chatVo.setReadCnt(readCnt);

		chatVo.setInDate(nowDateTimeStr);


		chatService.insertChatDetail(param); // 채팅내용 등록

        headerAccessor.getSessionAttributes().put("username", chatVo.getUserNm());
        headerAccessor.getSessionAttributes().put("path", chatVo.getPath());
        headerAccessor.getSessionAttributes().put("uPk", chatVo.getUserPk());

        param.put("restart", "Y");
        String msgSendYn = chatService.selectLastChat(param);

		if( msgSendYn != null ) {
			String[] strArr = null;
			if(msgSendYn != null || "".equals(msgSendYn)) {
				strArr = msgSendYn.split(",");
			}

			String[] uPKArr = strArr[1].split(":");

			String uPK = uPKArr[0];
			String cPK = uPKArr[1];
			String R_STND_CD = strArr[3];

			Map<String, Object> mallParam = new HashMap<String, Object>();
			
			mallParam.put("uPK", uPK);
			mallParam.put("cPK", cPK);
			
			//수신자 정보 가져오기
			Map<String, Object> userInfo = null;
			userInfo = chatService.selectMallCompanyInfo(mallParam); // 사용자 정보
			
			if( userInfo != null ) {
				
				String msg_type = "6";
				String callback = "16442653";
				String stat = "0";
				String k_expiretime = "86400";
				String k_next_type = "6";
				String dstaddr = String.valueOf(userInfo.get("C_mobile"));
				String subject = "실시간 메시지 도착 알림 메시지";
				String text = "[캐스팅엔] 상담 재시작 메시지 알림\r\n" + String.valueOf(userInfo.get("C_NAME")) + " / " + String.valueOf(userInfo.get("U_NAME")) +"님,\r\n캐스팅엔에 판매 등록하신 상품에 대해 고객 상담이 재시작 되었습니다.\r\n";
				text +=  "구체적인 상담 요구사항을 확인해주세요.\r\n\r\n";
				text +=  "해당 요구사항 도착 알림 메시지는 고객님의 알림 신청에 의해 발송되었습니다.";
				String text2 = text;
				
				String k_button_url = "https://chat.castingn.com:28443/seller?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;
				String k_button_name = "메세지 답변하기";
				
				String k_template_code = "SJT_067289";//파트너 받음
				String sender_key = "88b5151f1ccdbea9c222f7bb15a25ca4b98aeece";//파트너 받음
				
				param.put("msg_type", msg_type);
				param.put("dstaddr", dstaddr);
				param.put("callback", callback);
				param.put("stat", stat);
				param.put("subject", subject);
				param.put("text", text);
				param.put("text2", text2);
				param.put("k_template_code", k_template_code);
				param.put("k_expiretime", k_expiretime);
				param.put("k_next_type", k_next_type);
				param.put("sender_key", sender_key);
				param.put("k_button_name", k_button_name);
				param.put("k_button_url", k_button_url);				

				chatService.kakao_insert(param);
			}
		}
		
        
        
        messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);		
	}
}

