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
//				 timeString += avgHour + "?????? ";
//			 }
//			 if (avgMin > 0) {
//				 timeString += avgMin + "??? ";
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

			String stndCd = param.get("stndCd").toString();		//????????????
			String cmChatType = param.get("cmChatType").toString();		//????????? ??????(CR01:?????????-??????, CR02:?????????-?????????, CR03:??????-?????????, CR04:?????????-??????-?????????)
			String svcCd = param.get("svcCd").toString();	//SVC01: ????????????, SVC02:plaza??????
			String userType = param.get("userType").toString();	//1:??????, 4:?????????

			String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK ???????????? ??????
			if(cmPk == null){
				ret.put("data", null); // ????????? ?????? ????????????
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

		String stndCd = param.get("stndCd").toString();		//????????????
		String cmChatType = param.get("cmChatType").toString();		//????????? ??????(CR01:?????????-??????, CR02:?????????-?????????, CR03:??????-?????????, CR04:?????????-??????-?????????)
		String svcCd = param.get("svcCd").toString();	//SVC01: ????????????, SVC02:plaza??????
		String userType = param.get("userType").toString();	//1:??????, 4:?????????

		//String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK ???????????? ??????
		//if(cmPk == null){
		//	ret.put("data", null); // ????????? ?????? ????????????
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

		// ??????
		if(userType.equals("1")){
			if(type.equals("unread")){ //????????? ?????????
				whereSql += " CDR.CDR_READ_YN = 'N' AND CDR.CDR_U_PK = " + uPk;
			}else if (type.equals("transaction")){ //????????? ?????????
				whereSql += " CDR.CDR_READ_YN = 'Y' AND CUM.CM_CHAT_STAT='1' ";
			}else if (type.equals("important")){ //???????????????
				whereSql += " CL.CL_U_PK = " + uPk;
			}
			else if (type.equals("exit")){	//?????? ?????????
				whereSql += " CUM.CM_CHAT_STAT='0' ";
			} else {
				whereSql += " 0=0 ";
			}
		}
		else if(userType.equals("4")){ // ?????????
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
			else if(type.equals("exit")){ //????????? ??????
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
		ret.put("state", 200); // ??????
		return ret;
	}

	@RequestMapping("/setChangeChatRoom")
	public Map<String, Object> setChangeChatRoom(@RequestParam Map<String, Object> param) throws Exception {

		//System.out.println("chat room chage : "+param.toString());
		Map<String, Object> map = chatService.selectChatMasterUserInfo(param);

		String cmPk = map.get("CM_PK").toString();
		String stndCd = map.get("R_STND_CD").toString();		//????????????
		String svcCd = map.get("CU_SVC_CD").toString();	//SVC01: ????????????, SVC02:plaza??????
		String[] uPkArr = map.get("CM_PU_PK").toString().split("\\:");	//??????:???????????????
//		String sellerUPk = param.get("sellerUPk").toString();	//???????????? ???????????????
		String cmChatType = map.get("CM_CHAT_TYPE").toString();
		String pUpk = map.get("CM_P_PK").toString();	//???????????????:??????????????????
		String pUid = map.get("R_STND_CD").toString(); //???????????????
		String customerUpk = uPkArr[0];    //????????????
		String sellerUPk = uPkArr[1];   //???????????????
		String[] pUpkArr = pUpk.split(":");
		String customerId = pUpkArr[0];    //???????????????
		String sellerId = pUpkArr[1];    //??????????????????

		map.put("cmPk", cmPk);
		map.put("stndCd", stndCd); // ????????????
		map.put("svcCd", svcCd); // ?????? ????????? ?????? (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
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
	 * ?????????
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
	 * ??????
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
	 * ????????? ?????? ???????????? ?????? ?????? ?????? ????????? ??? ???????????? ????????????.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.customer.enter.room.post")
	public Map<String, Object> chatEnterRoomPost(@RequestParam Map<String, Object> param) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();

		String stndCd = param.get("stndCd").toString();		//????????????
		String cmChatType = param.get("cmChatType").toString();		//????????? ??????(CR01:?????????-??????, CR02:?????????-?????????, CR03:??????-?????????, CR04:?????????-??????-?????????)
		String svcCd = param.get("svcCd").toString();	//SVC01: ????????????, SVC02:plaza??????
		String uPk = param.get("uPk").toString();	//???????????????
		String uPkArr = param.get("uPkArr").toString();	//??????,???????????????
		String pUid = param.get("pUid").toString();		//???????????????
		String cmPk = MapUtils.getString(param, "cmPk", "");
		String uName = param.get("uName").toString();
		String uType = param.get("userType").toString();
		String sName = param.get("sName").toString();
		String user_logo = ""; 
		if(param.get("userLogo") != null ) {
			param.get("userLogo").toString();
		}
		String seller_logo = param.get("sellerLogo").toString();

		//??????:????????? ????????????
		//param.put("uUpk", uPkArr.replace(",", "\\:"));

//		Map<String, Object> userInfo = chatService.selectUserInfo(param); // ????????????

		if(cmPk.isEmpty()) {
			cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK ???????????? ??????
		}

		String isRoomCreate = "0";

		if(cmPk == null) {

			String cmPkNum = chatService.selectCmPkNum(param) + "";
			cmPk = stndCd + "_" + cmPkNum; // CHAT_MASTER.CM_PK ??????
			param.put("cmPk", cmPk); // ????????? Pk(CHAT_MASTER.CM_PK)
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

		ret.put("state", 200); // ??????
		ret.put("cmPk", cmPk); // ????????? PK
		ret.put("path", cmPk); // ????????? ??????(????????? PK)
		ret.put("svcCd", svcCd); // ?????? ????????? ?????? (SVC01 : www.castingn.com or admin.castingn.com, SVC02 : mall.castingn.com)
		ret.put("cmChatType", cmChatType);
		ret.put("userType", 1);
		ret.put("uPk", uPk);
//		ret.put("uName", String.valueOf(userInfo.get("U_NAME")));
		ret.put("isRoomCreate", isRoomCreate);

		return ret;
	}

	/**
	 * ??????????????? ????????????.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.get.chat.list.post")
	public ModelAndView getChatListPost(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		String stndCd = param.get("stndCd").toString();		//????????????
		String cmChatType = param.get("cmChatType").toString();		//????????? ??????(CR01:?????????-??????, CR02:?????????-?????????, CR03:??????-?????????, CR04:?????????-??????-?????????)
		String svcCd = param.get("svcCd").toString();	//SVC01: ????????????, SVC02:plaza??????
		String uPk = param.get("uPk").toString();	//???????????????
		String userType = param.get("userType").toString();	//1:??????, 4:?????????, 11:?????????.
		String uName = MapUtils.getString(param, "uName", "");
		param.put("uName", uName);

		String cmPk = MapUtils.getString(param, "cmPk", "");

		if(cmPk.isEmpty() && userType.equals("1")) {	//????????? ????????? cmPK??? ??????
			cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK ???????????? ??????
		}

		if(cmPk == null) {
			mv.addObject("chatHistList", null); // ????????? ?????? ????????????
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

		//?????? ?????? ??????
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

		//?????? ?????? ??????
		Calendar yesterday=Calendar.getInstance();
		yesterday.add(Calendar.DATE,-1);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("a h:mm");
		String yesterdayStr = sdf1.format(yesterday.getTime());


		for (Map<String, Object> item : tmpChatList) {
			String timeStr = getKoreanTimeType(item.get("INDATE").toString());
			String[] dateTimeArr = item.get("INDATE").toString().split(" ");

			if(yesterdayStr.equals(dateTimeArr[0])){
				item.put("INTIME", "?????? " + timeStr);
			}
			else{
				item.put("INTIME", timeStr);
			}

			//?????? ????????? ?????????
			if(!dateTimeArr[0].equals(lastDate)){
				Map<String, Object> timeObj = getTimeMap(dateTimeArr[0]);
				chatList.add(timeObj);
				lastDate = dateTimeArr[0];
			}

			chatList.add(item);
		}



		mv.addObject("chatHistList", chatList); // ????????? ?????? ????????????
		//mv.addObject("chatHistList", chatService.selectChatHistList(paramList)); // ????????? ?????? ????????????

		//left panel??? ????????? ??????
		int totalCount = chatService.getTotalCountInRoom(paramList);
		mv.addObject("totalCount", totalCount);		//?????? ??????
		paramList.put("userType", userType);



		//==================== ???????????? ======================================

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



		//==================== ????????????  ??? ========================





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
			timeString = "0???";
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
				timeString += avgHour + "?????? ";
			}
			if (avgMin > 0) {
				timeString += avgMin + "??? ";
			}
		}
		mv.addObject("responseInfo", timeString);

		*/


		if(userType.equals("1")){	//????????????
			//paramList = new HashMap<String, Object>();

			paramList = new HashMap<String, Object>();
			paramList.put("cmPk", cmPk);
			paramList.put("uPk", uPk);

			int unreadCount = chatService.selectChatMessageReadCnt(paramList);
			mv.addObject("unreadCount", unreadCount);	//????????? ??????

			//int exitId = 0;
			//Map<String, Object> exitInfo = chatService.getLastExitMessageId(paramList);
			//if(exitInfo != null){
			//	exitId = Integer.valueOf(exitInfo.get("CD_PK").toString());
			//}

			//paramList.put("exitId", exitId);
			int exitAfterCount = chatService.getExitAfterCnt(paramList);
			mv.addObject("transactionCount", exitAfterCount);	//????????? ??????

			int exitBeforeCount = chatService.getExitBeforeCnt(paramList);
			mv.addObject("cnt_exit", exitBeforeCount);	//?????? ??????

			int importantCount = chatService.getImportantCnt(paramList);
			mv.addObject("cnt_important", importantCount);	//?????? ??????
		}
		else if(userType.equals("4")){	//???????????????
			paramList = new HashMap<String, Object>();
			paramList.put("cmPk", cmPk);
			paramList.put("uPk", uPk);
			int talkingRequestCount = chatService.getTalkingRequestCnt(paramList);
			mv.addObject("talkingRequestCount", talkingRequestCount);	//???????????? ??????

			int talkingRunCount = chatService.getTalkingRunCnt(paramList);
			mv.addObject("talkingRunCount", talkingRunCount);	//??????????????? ??????

			int estimateSendCount = chatService.getEstimateSendCnt(paramList);
			mv.addObject("estimateSendCount", estimateSendCount);	//???????????? ??????

			int estimateSelectCount = chatService.getEstimateSelectCnt(paramList);
			mv.addObject("estimateSelectCount", estimateSelectCount);	//??????????????? ??????

			int settleOkCount = chatService.getSettleOkCnt(paramList);
			mv.addObject("settleOkCount", settleOkCount);	//???????????? ??????

			//int exitId = 0;
			//Map<String, Object> exitInfo = chatService.getLastExitMessageId(paramList);
			//if(exitInfo != null){
			//	exitId = Integer.valueOf(exitInfo.get("CD_PK").toString());
			//}

			//paramList.put("exitId", exitId);
			int exitBeforeCount = chatService.getExitBeforeCnt(paramList);
			mv.addObject("cnt_exit", exitBeforeCount);	//?????? ??????

			int importantCount = chatService.getImportantCnt(paramList);
			mv.addObject("cnt_important", importantCount);	//?????? ??????

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
			timeStr = timeStr.replaceAll("AM", "??????");
			timeStr = timeStr.replaceAll("PM", "??????");
		}
		return timeStr;
	}

	/**
	 * ????????????????????? ????????????.
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/chat.get.chatfile.list.post")
	public ModelAndView getChatFileListPost(@RequestParam Map<String, Object> param) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");

		String stndCd = param.get("stndCd").toString();		//????????????
		String cmChatType = param.get("cmChatType").toString();		//????????? ??????(CR01:?????????-??????, CR02:?????????-?????????, CR03:??????-?????????, CR04:?????????-??????-?????????)
		String svcCd = param.get("svcCd").toString();	//SVC01: ????????????, SVC02:plaza??????
		String uPk = param.get("uPk").toString();	//???????????????
		String userType = param.get("userType").toString();	//1:??????, 4:?????????

		String cmPk = param.get("cmPk").toString();
		//String cmPk = chatService.selectCmPk(param); // CHAT_MASTER.CM_PK ???????????? ??????
		if(cmPk == null){
			mv.addObject("chatHistList", null); // ????????? ?????? ????????????
			return mv;
		}

		Map<String, Object> paramList = new HashMap<String, Object>();
		paramList.put("cmPk", cmPk);
		paramList.put("isGroup", true);

		//mv.addObject("chatHistList", chatService.selectChatFileHistList(paramList)); // ????????? ?????? ????????????

		List<Map<String, Object>> chatList = chatService.selectChatFileHistList(paramList);

		for (Map<String, Object> item : chatList) {
			paramList.replace("isGroup", false);
			paramList.put("inDate", item.get("CD_INDATE"));

			List<Map<String, Object>> chatSubList = chatService.selectChatFileHistList(paramList);
			item.put("list", chatSubList);
		}

		mv.addObject("chatHistList", chatList); // ????????? ?????? ????????????
		mv.addObject("path", cmPk);
		mv.addObject("cmPk", cmPk);

		return mv;
	}



	/**
	 * ???????????? ????????? ??????
	 * @param chatVo
	 * @param headerAccessor
	 * @throws Exception
	 */
	@MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatVo chatVo, SimpMessageHeaderAccessor headerAccessor) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("cmPk", chatVo.getPath());   // CHAT_MASTER.CM_PK (???????????? ????????? PK)
		param.put("uPk", chatVo.getUserPk());  // USER_INFO.U_PK (????????? ????????? PK )
		param.put("svcCd", chatVo.getSvcCd()); // ?????? ????????? ?????? ??????(SVC01 : mall.castingn.com, SVC02: www.castingn.com/admin.castingn.com)

		chatVo.setContent(chatVo.getUserNm() + " ?????? ???????????? ?????????????????????.");

		param.put("content", chatVo.getContent());
		param.put("chatStat", chatVo.getUserStatus()); // ????????????(JOIN: ???????????????, LEAVE:???????????????, CHAT:????????????, FILE:????????????)
		param.put("msgType", chatVo.getMsgType());

		int readCnt = chatService.selectChatMessageReadCnt(param);

		chatService.updateChatDetailRead(param); // ?????? ?????? ?????? ?????? ?????? (CDR_READ_YN update)

		chatVo.setReadCnt(readCnt);

		chatVo.setInDate(getTimeStamp2());


		chatService.insertChatDetail(param); // ???????????? ??????

        headerAccessor.getSessionAttributes().put("username", chatVo.getUserNm());
        headerAccessor.getSessionAttributes().put("path", chatVo.getPath());
        headerAccessor.getSessionAttributes().put("uPk", chatVo.getUserPk());

        messagingTemplate.convertAndSend("/topic/" + chatVo.getPath(), chatVo);
    }

	/**
	 * ????????? ??????
	 * @param chatVo
	 * @throws Exception
	 */
	@MessageMapping("/chat.sendMessage")
	/* @SendTo("/topic/{chatRoomId}") */
    public void sendMessage(@Payload ChatVo chatVo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();


		//????????? ?????? ?????????????????? ??? ????????? ????????? ?????????.
		if(chatVo.getUserType().equals("1") || chatVo.getUserType().equals("4")) {
			if(chatVo.getContent() != null && !chatVo.getContent().equals("")) {
				boolean isForbidFind = false;
				String chatContent = chatVo.getContent();
				//????????? ????????? ??????
				List<Map<String, Object>> forbidList = chatService.selectForbidList();
				for (Map<String, Object> item : forbidList) {
					String forbidWord = item.get("FORBID_WORD").toString();
					if (chatContent.contains(forbidWord)) {
						isForbidFind = true;
						break;
					}
				}

				if (isForbidFind) {    //???????????? ?????????
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

			//???????????? ?????? ???????????? ???????????????.
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
					if(nextItem_Upk == Integer.valueOf(uPkArr[0])) { //??????
						customerChatCnt++;
						customerDiffTime += ((nextItem_date.getTime() - item_date.getTime()) / 1000);
					}
					else if(item_Upk == Integer.valueOf(uPkArr[0]) && nextItem_Upk == Integer.valueOf(uPkArr[1])) { //?????????
						sellerChatCnt++;
						sellerDiffTime += ((nextItem_date.getTime() - item_date.getTime()) / 1000);
					}
					else if(item_Upk == Integer.valueOf(uPkArr[0]) && nextItem_Upk == Integer.valueOf(uPkArr[2])) { //?????????
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
					String fileExt = fileName.substring(index + 1); // ???????????? ?????????
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

			chatVo.setUserStatus(chatVo.getUserStatus().FAILED); // ????????? ?????? ??????

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

				param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (???????????? ????????? PK)
				param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (????????? ????????? PK )

				param.put("content", chatVo.getContent());
				param.put("chatStat", chatVo.getUserStatus()); // ????????????(JOIN: ???????????????, LEAVE:???????????????, CHAT:????????????, FILE:????????????)
				param.put("msgType", chatVo.getMsgType());
				param.put("orgFileNm", chatVo.getFileName()); // ?????? ?????????
				param.put("newFileNm", chatVo.getFileNewName()); // ?????? ?????????
				param.put("filePath" , chatVo.getFilePath()); // ?????? ?????? ??????
				param.put("prPk" , chatVo.getPrPk()); // ????????????
				param.put("paPk" , chatVo.getPaPk()); // ??????

				//??????
				param.put("uType", chatVo.getUserType());
				chatVo.setReadCnt(chatService.insertChatDetail(param)); // ???????????? ??????


				//???????????? ?????????
				if(chatVo.getMsgType().equals("normal")) {
					//?????????
					kakao_msg(param);
				}

				//????????? ???????????? MASTER?????? ????????????
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
	 * ?????? ??????
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
			String fileExt = fileName.substring(index + 1); // ???????????? ?????????
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

			param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (???????????? ????????? PK)
			param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (????????? ????????? PK )
			param.put("orgFileNm", chatVo.getFileName()); // ?????? ?????????
			param.put("newFileNm", chatVo.getFileNewName()); // ?????? ?????????
			param.put("filePath" , chatVo.getFilePath()); // ?????? ?????? ??????

		} catch(IOException e) {

			chatVo.setUserStatus(chatVo.getUserStatus().FAILED); // ????????? ?????? ??????

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



				param.put("chatStat", chatVo.getUserStatus()); // ????????????(JOIN: ???????????????, LEAVE:???????????????, CHAT:????????????, FILE:????????????)
				param.put("msgType", "normal");

				chatVo.setReadCnt(chatService.insertChatDetail(param)); // ???????????? ??????
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
	 * ????????? ?????? ??????
	 * @param chatVo
	 * @throws Exception
	 */
	@MessageMapping("/chat.readMessage")
	public void readMessage(@Payload ChatVo chatVo) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("cmPk", chatVo.getPath()); // CHAT_MASTER.CM_PK (???????????? ????????? PK)
		param.put("uPk", chatVo.getUserPk()); // USER_INFO.U_PK (????????? ????????? PK )
		param.put("cdPk", ""); // CHAT_DETAIL.CD_PK (?????? ?????? PK )

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

		chatService.updateChatDetailRead(param); // ?????? ?????? ?????? ?????? ?????? (CDR_READ_YN update)

		chatVo.setReadCnt(readCnt);
		chatVo.setInDate(getTimeStamp2());

		messagingTemplate.convertAndSend("/topic/public", chatVo);
	}

	/**
	 * ?????? ????????? ?????????.
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
	 * ????????? ??????
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
	 * ????????? ??????
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
	 * ????????? ??????
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
			html = "<div class=\"message-box message-pink\"><p>?????? ?????????????????????.</p></div>";
			settleState = "settle_ok";
		}
		else{
			html = "<div class=\"message-box message-pink\"><p>?????? ?????????????????????. </p></div>";
			settleState = "settle_cancel";
		}

		Map<String, Object> dbParam = new HashMap<String, Object>();

		dbParam.put("cdPk", cdPk);
		Map<String, Object> paramList = chatService.selectChatDetailInfo(dbParam);

		dbParam.put("cmPk", paramList.get("R_CM_PK").toString()); // CHAT_MASTER.CM_PK (???????????? ????????? PK)
		dbParam.put("uPk", paramList.get("CD_U_PK").toString()); // USER_INFO.U_PK (????????? ????????? PK )
		dbParam.put("content", html);
		dbParam.put("chatStat", "CHAT"); // ????????????(JOIN: ???????????????, LEAVE:???????????????, CHAT:????????????, FILE:????????????)
		dbParam.put("msgType", settleState);
		dbParam.put("orgFileNm", "");
		dbParam.put("newFileNm", "");
		dbParam.put("filePath", "");
		chatVo.setReadCnt(chatService.insertChatDetail(dbParam)); // ???????????? ??????

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
	 * ?????? ?????? ????????????
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
	 * ?????????????????????????????? ???????????? ???????????? ?????? ???????????????17?????????TIMESTAMP?????? ????????? ??????
	 *
	 * @param
	 * @return the time stamp
	 * @see
	 */
	public static String getTimeStamp() {

		String rtnStr = null;

		// ???????????? ???????????? ?????? ?????? ??????(??????-???-??? ???:???:???:???(???????????? ???))
		String pattern = "yyyyMMddhhmmssSSS";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}

	/**
	 * Disposition ????????????.
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
	 * ???????????? ?????? ??????.
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

		// ???????????? ???????????? ?????? ?????? ??????(??????-???-??? ???:???:???:???(???????????? ???))
		//String pattern = "yyyy???MM???dd??? hh???mm???";
		String pattern = "yyyy.MM.dd hh:mm";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}

	public void kakao_msg( Map<String, Object> param ) throws Exception{

		//????????? ?????????
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

			String C_CNT = strArr[4];//????????? ?????? ????????? ??????
			String U_CNT = strArr[5];//?????? ?????? ????????? ??????

			//5?????? ?????? ??????, ???????????????
			//if( Integer.parseInt(strArr[0]) >= 5 || ( "1".equals(C_CNT) ) || ( "1".equals(U_CNT) ) ) {
			if( ( "1".equals(C_CNT) ) || ( "1".equals(U_CNT) ) ) {

				String msg_type = "6";
				String dstaddr = "";
				String callback = "16442653";
				String stat = "0";
				String subject = "";
				String text = "";
				String text2 = text;
				String k_template_code = "";//????????? ??????
				String k_expiretime = "86400";
				String k_next_type = "6";
				String sender_key = "";//????????? ??????
				String k_button_url = "";
				String k_button_name = "";
				String uType = param.get("uType").toString();

				Map<String, Object> mallParam = new HashMap<String, Object>();;

				mallParam.put("uPK", uPK);
				mallParam.put("cPK", cPK);

				boolean kakao_msg = false;
				if( "1".equals(uType) ) { //???????????????, ?????? ?????????

					//????????? ?????? ????????????
					Map<String, Object> userInfo = null;
					userInfo = chatService.selectMallCompanyInfo(mallParam); // ????????? ??????

					if( userInfo != null ) {

						dstaddr = String.valueOf(userInfo.get("C_mobile"));
						subject = "????????? ????????? ?????? ?????? ?????????";
						text = "[????????????] ????????? ????????? ?????? ?????? ????????? \r\n" + String.valueOf(userInfo.get("C_NAME")) + " / " + String.valueOf(userInfo.get("U_NAME")) +"???,\r\n?????? ???????????? ????????? ?????? ????????? ??????????????????.";

						k_button_url = "https://chat.castingn.com:28443/seller?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;
						k_button_name = "????????? ????????????";

						k_template_code = "SJT_067289";//????????? ??????
						sender_key = "88b5151f1ccdbea9c222f7bb15a25ca4b98aeece";//????????? ??????
						kakao_msg = true;
					}
				} else if("4".equals(uType)) {//??????

					//????????? ?????? ????????????
					Map<String, Object> userInfo = null;
					userInfo = chatService.selectMallCustomerInfo(mallParam); // ?????? ??????

					if( userInfo != null ) {
						dstaddr = String.valueOf(userInfo.get("U_mobile"));
						subject = "????????? ????????? ?????? ?????? ?????????";
						text = "[????????????] ????????? ????????? ?????? ?????? ????????? \r\n" + String.valueOf(userInfo.get("U_NAME")) +"???,\r\n??????????????? ???????????? ????????? ?????? ????????? ??????????????????.";

						//text += "\n\n????????? ????????????";
						//text += "\nhttps://chat.castingn.com:28443/customer?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;

						k_button_url = "https://chat.castingn.com:28443/customer?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;
						k_button_name = "????????? ????????????";

						k_template_code = "SJT_067292";//?????? ??????
						sender_key = "f861a7eef35eaf1cc81bfe7b6b6cf813491ff47d";//?????? ??????
						kakao_msg = true;
					}
				} else {//????????? , 11

				}

				//f861a7eef35eaf1cc81bfe7b6b6cf813491ff47d, ?????? ??????
				//SJT_066488 ?????? ??????, ????????? ?????????

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
	 * ????????? ??????????????? ????????????.
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
			qList  = chatService.selectChatServiceQuestion(param); // CHAT_MASTER.CM_PK ???????????? ??????

			mv.addObject("data", qList); // ????????? ?????? ????????????
			mv.addObject("code", "200"); // ????????? ?????? ????????????
			
		}catch(Exception e) {

		}

		return mv;
	}
	
	/**
	 * ????????? ????????? ??????.
	 * @return 
	 * @throws Exception
	 */
	@MessageMapping("/chat.customer.reconnect.room.post")
	public void reconnectRoom(@Payload ChatVo chatVo, SimpMessageHeaderAccessor headerAccessor) throws Exception {
		System.out.println("################################################################");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String nowDateTimeStr = getTimeStamp2();
		chatVo.setInTime(getKoreanTimeType(nowDateTimeStr));
		
		param.put("cmPk", chatVo.getPath());   // CHAT_MASTER.CM_PK (???????????? ????????? PK)
		param.put("uPk", chatVo.getUserPk());  // USER_INFO.U_PK (????????? ????????? PK )
		param.put("svcCd", chatVo.getSvcCd()); // ?????? ????????? ?????? ??????(SVC01 : mall.castingn.com, SVC02: www.castingn.com/admin.castingn.com)

		chatVo.setContent(chatVo.getUserNm() + " ?????? ????????? ????????????????????????.");

		param.put("content", chatVo.getContent());
		param.put("chatStat", chatVo.getUserStatus()); // ????????????(JOIN: ???????????????, LEAVE:???????????????, CHAT:????????????, FILE:????????????)
		param.put("msgType", chatVo.getMsgType());

		param.put("cmStat", "1");
		chatService.updateStatusChange(param); // ????????? ????????? ??????

		int readCnt = chatService.selectChatMessageReadCnt(param);

		chatService.updateChatDetailRead(param); // ?????? ?????? ?????? ?????? ?????? (CDR_READ_YN update)

		chatVo.setReadCnt(readCnt);

		chatVo.setInDate(nowDateTimeStr);


		chatService.insertChatDetail(param); // ???????????? ??????

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
			
			//????????? ?????? ????????????
			Map<String, Object> userInfo = null;
			userInfo = chatService.selectMallCompanyInfo(mallParam); // ????????? ??????
			
			if( userInfo != null ) {
				
				String msg_type = "6";
				String callback = "16442653";
				String stat = "0";
				String k_expiretime = "86400";
				String k_next_type = "6";
				String dstaddr = String.valueOf(userInfo.get("C_mobile"));
				String subject = "????????? ????????? ?????? ?????? ?????????";
				String text = "[????????????] ?????? ????????? ????????? ??????\r\n" + String.valueOf(userInfo.get("C_NAME")) + " / " + String.valueOf(userInfo.get("U_NAME")) +"???,\r\n??????????????? ?????? ???????????? ????????? ?????? ?????? ????????? ????????? ???????????????.\r\n";
				text +=  "???????????? ?????? ??????????????? ??????????????????.\r\n\r\n";
				text +=  "?????? ???????????? ?????? ?????? ???????????? ???????????? ?????? ????????? ?????? ?????????????????????.";
				String text2 = text;
				
				String k_button_url = "https://chat.castingn.com:28443/seller?uPk="+strArr[1]+"&pUpk="+strArr[2]+"&cmPk="+param.get("cmPk").toString()+"&stndCd="+R_STND_CD+"&cmChatType=CR10&svcCd=SVC10&&P_uid="+R_STND_CD;
				String k_button_name = "????????? ????????????";
				
				String k_template_code = "SJT_067289";//????????? ??????
				String sender_key = "88b5151f1ccdbea9c222f7bb15a25ca4b98aeece";//????????? ??????
				
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

