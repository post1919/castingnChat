package castingn.com.chat.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import castingn.com.chat.mapper.chat.ChatMapper;
import castingn.com.chat.mapper.mall.MallMapper;
import castingn.com.chat.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	@Resource(name="chatMapper")
	ChatMapper chatMapper;

	@Resource(name="mallMapper")
	MallMapper mallMapper;

	/** 평균 응답률갱신 **/
	@Override
	public void updateAvgResponseTime(Map<String, Object> param) throws Exception{
		chatMapper.updateAvgResponseTime(param);
	}

	/** 평균 응답률추가 **/
	@Override
	public void insertAvgResponseTime(Map<String, Object> param) throws Exception{
		chatMapper.insertAvgResponseTime(param);
	}

	/** 평균 응답률얻기 **/
	@Override
	public List<Map<String, Object>> getAvgResponse(Map<String, Object> param) throws Exception{
		return chatMapper.getAvgResponse(param);
	}
	
	/** 방채팅얻기 **/
	@Override
	public List<Map<String, Object>> getChatInRoom(Map<String, Object> param) throws Exception{
		return chatMapper.getChatInRoom(param);
	}

	/** 중요메세지 갯수 **/
	@Override
	public int getImportantCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getImportantCnt(param);
	}

	/** 메세지얻기(안읽은 메세지, 거래중, 완료, 중요) **/
	@Override
	public List<Map<String, Object>> getChatListByMessageType(Map<String, Object> param) throws Exception{
		return chatMapper.getChatListByMessageType(param);
	}

	@Override
	public Map<String, Object> selectChatMasterUserInfo(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatMasterUserInfo(param);
	}

	/** 찜하기 **/
	@Override
	public void likeChat(Map<String, Object> param) throws Exception{
		String type = param.get("isLike").toString();
		if(type.equals("true")) chatMapper.likeChatInsert(param);
		else chatMapper.likeChatDelete(param);
	}

	/** 검색 **/
	@Override
	public List<Map<String, Object>> search(Map<String, Object> param) throws Exception {
		return chatMapper.search(param);
	}

	/** 메모 내역 조회  **/
	@Override
	public List<Map<String, Object>> selectMemo(Map<String, Object> param) throws Exception {
		return chatMapper.selectMemo(param);
	}

	/** 메모 추가  **/
	@Override
	public void addMemo(Map<String, Object> param) throws Exception{
		chatMapper.addMemo(param);
	}

	/** 메모 삭제  **/
	@Override
	public void deleteMemo(Map<String, Object> param) throws Exception{
		chatMapper.deleteMemo(param);
	}


	/**
	 * 채팅방 생성
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int insertChatMaster(Map<String, Object> param) throws Exception {

		return chatMapper.insertChatMaster(param); // 채팅방 생성

	}

	/**
	 * 카카오 알림톡 발송
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int kakao_insert(Map<String, Object> param) throws Exception {

		return chatMapper.kakao_insert(param); // 채팅방 생성

	}

	/**
	 * 사용자 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectUserInfo(Map<String, Object> param) throws Exception {
		return chatMapper.selectUserInfo(param);
	}

	/**
	 * 의뢰건 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectProjectInfo(Map<String, Object> param) throws Exception {
		return chatMapper.selectProjectInfo(param);
	}

	/**
	 * 이전 채팅 이력 조회
	 * @param Map<String, Object>
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectChatHistList(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatHistList(param);
	}


	/**
	 * 이전 파일 이력 조회
	 * @param Map<String, Object>
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectChatFileHistList(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatFileHistList(param);
	}

	/**
	 * 채팅방 존재 유무 확인
	 * @param Map<String, Object>
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String selectCmPk(Map<String, Object> param) throws Exception {
		return chatMapper.selectCmPk(param);
	}

	/**
	 * 최근 메세지 발송 유무
	 * @param Map<String, Object>
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String selectLastChat(Map<String, Object> param) throws Exception {
		return chatMapper.selectLastChat(param);
	}

	/**
	 * 채팅방 넘버링 조회
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int selectCmPkNum(Map<String, Object> param) throws Exception {
		return chatMapper.selectCmPkNum(param);
	}

	/**
	 * 채팅방 참여 확인
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int selectChatUserCnt(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatUserCnt(param);
	}

	/**
	 * 채팅방 참여자 등록
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public void insertChatUser(Map<String, Object> param) throws Exception {
		chatMapper.insertChatUser(param);
	}

	/**
	 * 채팅방 참여자 정보 변경
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public void updateChatUser(Map<String, Object> param) throws Exception {
		chatMapper.updateChatUser(param);
	}

	/**
	 * 채팅 내용 등록
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public int insertChatDetail(Map<String, Object> param) throws Exception {

		chatMapper.insertChatDetail(param);

		if(param.get("chatStat").toString().equals("CHAT") || param.get("chatStat").toString().equals("FILE")) {

			List<Map<String, Object>> uPkList = selectChatUserList(param);
			if(uPkList.size() > 0) {
				chatMapper.insertChatDetailRead(uPkList); // 채팅 내용 읽기 확인 등록
				Map<String, Object> obj = uPkList.get(uPkList.size() - 1);
				param.put("CD_PK", obj.get("cdPk"));
			}
		}

		return selectChatMessageReadCnt(param);

	}

	/**
	 * 채팅 내용 갱신
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public void updateMsgTypeDetail(Map<String, Object> param) throws Exception {

		chatMapper.updateMsgTypeDetail(param);

	}


	/**
	 * 채팅 마스터 상태 변경
	 */
	@Override
	public void updateStatusChange(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		chatMapper.updateStatusChange(param);
	}

	/**
	 * 채팅 내용 갱신
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public void updateMsgTypeAndContentDetail(Map<String, Object> param) throws Exception {

		chatMapper.updateMsgTypeAndContentDetail(param);

	}

//	/**
//	 * 채팅 내용 등록
//	 * @param Map<String, Object>
//	 * @throws Exception
//	 */
//	@Override
//	public int deleteChatDetail(Map<String, Object> param) throws Exception {
//
//		chatMapper.deleteChatDetail(param);
//
//		if(param.get("chatStat").toString().equals("CHAT") || param.get("chatStat").toString().equals("FILE")) {
//
//
//				chatMapper.deleteChatDetailRead(param.get("CD_PK").toString()); // 채팅 내용 읽기 확인 등록
//
//		}
//
//		return selectChatMessageReadCnt(param);
//
//	}

	/**
	 * 채팅방별 유저 조회
	 * @param Map<String, Object>
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectChatUserList(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatUserList(param);
	}

	/**
	 * 메세지 읽기 확인 카운트
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int selectChatMessageReadCnt(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatMessageReadCnt(param);
	}

	@Override
	public void updateChatDetailRead(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		chatMapper.updateChatDetailRead(param);
	}

	/**
	 * 쇼핑몰 사용자 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectMallMemberInfo(Map<String, Object> param) throws Exception {
		switch (String.valueOf(param.get("ordrKey"))) {
		default:
		case "MCA":
			return mallMapper.selectMallMemberInfo(param);
		case "SCA":
			return mallMapper.selectMallSubMemberInfo(param);
		case "CMA":
		case "CSA":
			return mallMapper.selectMallCompanyInfo(param);
		case "AMC":
		case "ASC":
			return mallMapper.selectMallAdminInfo(param);
		}
	}

	/**
	 * 쇼핑몰 고객 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectMallCustomerInfo(Map<String, Object> param) throws Exception {
		return mallMapper.selectMallCustomerInfo(param);
	}

	/**
	 * 쇼핑몰 셀러 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectMallCompanyInfo(Map<String, Object> param) throws Exception {
		return mallMapper.selectMallCompanyInfo(param);
	}

	/**
	 * 쇼핑몰 서비스 상품 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectMallProductInfo(Map<String, Object> param) throws Exception {
		return mallMapper.selectMallProductInfo(param);
	}

	/**
	 * 쇼핑몰 채팅방 참여자 등록
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public void insertMallChatUser(Map<String, Object> param) throws Exception {
		String uType = "";
		switch (String.valueOf(param.get("ordrKey"))) {
		default:
		case "MCA":
			switch (String.valueOf(param.get("uPkOrder"))) {
			default:
			case "0":
				uType = "1";
				break;
			case "1":
				uType = "4";
				break;
			case "2":
				uType = "11";
				break;
			}
			break;
		case "SCA":
			switch (String.valueOf(param.get("uPkOrder"))) {
			default:
			case "0":
				uType = "2";
				break;
			case "1":
				uType = "4";
				break;
			case "2":
				uType = "11";
				break;
			}
			break;
		case "CMA":
			switch (String.valueOf(param.get("uPkOrder"))) {
			default:
			case "0":
				uType = "4";
				break;
			case "1":
				uType = "1";
				break;
			case "2":
				uType = "11";
				break;
			}
			break;
		case "CSA":
			switch (String.valueOf(param.get("uPkOrder"))) {
			default:
			case "0":
				uType = "4";
				break;
			case "1":
				uType = "2";
				break;
			case "2":
				uType = "11";
				break;
			}
			break;
		case "AMC":
			switch (String.valueOf(param.get("uPkOrder"))) {
			default:
			case "0":
				uType = "11";
				break;
			case "1":
				uType = "1";
				break;
			case "2":
				uType = "4";
				break;
			}
			break;
		case "ASC":
			switch (String.valueOf(param.get("uPkOrder"))) {
			default:
			case "0":
				uType = "11";
				break;
			case "1":
				uType = "2";
				break;
			case "2":
				uType = "4";
				break;
			}
			break;
		}
		param.put("uType", uType);
		chatMapper.insertMallChatUser(param);
	}

	/**
	 * 채팅 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectChatDetailInfo(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatDetailInfo(param);
	}

	/**
	 * 방에 해당한 총 채팅갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getTotalCountInRoom(Map<String, Object> param) throws Exception {
		return chatMapper.getTotalCountInRoom(param);
	}

	/**
	 * 상담 종료에 해당한 채팅아이디 얻기
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getLastExitMessageId(Map<String, Object> param) throws Exception {
		return chatMapper.getLastExitMessageId(param);
	}

	/**
	 * 상담 종료이후 갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getExitAfterCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getExitAfterCnt(param);
	}

	/**
	 * 상담 종료이전 갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getExitBeforeCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getExitBeforeCnt(param);
	}
	/**
	 * 상담 요청 갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getTalkingRequestCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getTalkingRequestCnt(param);
	}

	/**
	 * 상담 진행중 갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getTalkingRunCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getTalkingRunCnt(param);
	}

	/**
	 * 견적요청 갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getEstimateSendCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getEstimateSendCnt(param);
	}

	/**
	 * 결제진행 중 갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getEstimateSelectCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getEstimateSelectCnt(param);
	}

	/**
	 * 결제완료 갯수 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getSettleOkCnt(Map<String, Object> param) throws Exception {
		return chatMapper.getSettleOkCnt(param);
	}

	/**
	 * 채팅방 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectChatMasterInfo(Map<String, Object> param) throws Exception {
		return chatMapper.selectChatMasterInfo(param);
	}

	/**
	 * 캐스팅엔 연결을 눌렀을때 CD_PK를 얻기
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getCDPKByExit(Map<String, Object> param) throws Exception {
		return chatMapper.getCDPKByExit(param);
	}

	@Override
	public void updateChatMallUser(Map<String, Object> param) {
		// TODO Auto-generated method stub
		chatMapper.updateChatMallUser(param);
	}

	/**
	 * 금칙어 리스트 얻기
	 * @param
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectForbidList() throws Exception {
		return chatMapper.selectForbidList();
	}

	/**
	 * 로고등록
	 */
	@Override
	public void updateChatUserInLogo(Map<String, Object> param) throws Exception{
		chatMapper.updateChatUserInLogo(param);		
	}

	/*
	 * 채팅방 유저 upk 호출
	 * @see castingn.com.chat.service.ChatService#selectChatUserInfo(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> selectChatUserInfo(Map<String, Object> param) throws Exception {		
		return chatMapper.selectChatUserInfo(param);
	}

	@Override
	public void updateAvgResTime(Map<String, Object> map) throws Exception {
		chatMapper.selectChatUserInfo(map);		
	}
	
	/**
	 * 채팅 서비스 문의 요청 등록 
	 */
	@Override
	public int insertChatServiceQuestion(Map<String, Object> param) throws Exception {

		int rst = chatMapper.insertChatServiceQuestion(param);
		
		return rst;
	}	
	
	/**
	 * 채팅 서비스 문의 요청 상태 변경
	 */
	@Override
	public int updateChatServiceQuestion(Map<String, Object> param) throws Exception {
		
		int rst = chatMapper.updateChatServiceQuestion(param);
		
		return rst;
	}	
	
	/**
	 * 채팅 서비스 문의 요청 내역 조회
	 */
	@Override
	public List<Map<String, Object>> selectChatServiceQuestion(Map<String, Object> param) throws Exception {
		
		List<Map<String, Object>> sList = chatMapper.selectChatServiceQuestion(param);
		
		return sList;
	}	
	

}
