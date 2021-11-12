package castingn.com.chat.service;

import java.util.List;
import java.util.Map;

public interface ChatService {

	/** 평균 응답률갱신 **/
	void updateAvgResponseTime(Map<String, Object> param) throws Exception;

	/** 평균 응답률추가 **/
	void insertAvgResponseTime(Map<String, Object> param) throws Exception;

	/** 평균 응답률얻기 **/
	List<Map<String, Object>> getAvgResponse(Map<String, Object> param) throws Exception;
	
	/** 방채팅얻기 **/
	List<Map<String, Object>> getChatInRoom(Map<String, Object> param) throws Exception;

	/** 중요메세지 갯수 **/
	int getImportantCnt(Map<String, Object> param) throws Exception;

	/** 메세지얻기(안읽은 메세지, 거래중, 완료, 중요) **/
	List<Map<String, Object>> getChatListByMessageType(Map<String, Object> param) throws Exception;

	/**
	 * 채팅정보 호출(내부전환)
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectChatMasterUserInfo(Map<String, Object> param) throws Exception;

	/** 찜하기 **/
	void likeChat(Map<String, Object> param) throws Exception;

	/** 검색 **/
	List<Map<String, Object>> search(Map<String, Object> param) throws Exception;

	/** 메모 내역 조회  **/
	List<Map<String, Object>> selectMemo(Map<String, Object> param) throws Exception;

	/** 메모 추가  **/
	void addMemo(Map<String, Object> param) throws Exception;

	/** 메모 삭제  **/
	void deleteMemo(Map<String, Object> param) throws Exception;

	/** 채팅방 생성  **/
	int insertChatMaster(Map<String, Object> param) throws Exception;

	/** 사용자 정보 조회  **/
	Map<String, Object> selectUserInfo(Map<String, Object> param) throws Exception;

	/** 의뢰건 정보 조회  **/
	Map<String, Object> selectProjectInfo(Map<String, Object> param) throws Exception;

	/** 이전 채팅 내역 조회  **/
	List<Map<String, Object>> selectChatHistList(Map<String, Object> param) throws Exception;

	/** 이전 파일 내역 조회  **/
	List<Map<String, Object>> selectChatFileHistList(Map<String, Object> param) throws Exception;

	/** 채팅방 존재 유무 확인  **/
	String selectCmPk(Map<String, Object> param) throws Exception;

	/** 최근 발송 메세지 존재 유무 확인  **/
	String selectLastChat(Map<String, Object> param) throws Exception;

	/** 채팅방 넘버링 조회 **/
	int selectCmPkNum(Map<String, Object> param) throws Exception;

	/** 채팅방 참여 확인 **/
	int selectChatUserCnt(Map<String, Object> param) throws Exception;

	/** 채팅방 참여자 등록 **/
	void insertChatUser(Map<String, Object> param) throws Exception;

	/** 채팅방 참여자 정보 변경 **/
	void updateChatUser(Map<String, Object> param) throws Exception;

	/** 채팅 내용 등록 **/
	int insertChatDetail(Map<String, Object> param) throws Exception;

	/** 채팅 메시지타입 갱신 **/
	void updateMsgTypeDetail(Map<String, Object> param) throws Exception;

	/** 채팅 메시지타입, 내용 갱신 **/
	void updateMsgTypeAndContentDetail(Map<String, Object> param) throws Exception;

	/** 채팅방별 유저 조회 **/
	List<Map<String, Object>> selectChatUserList(Map<String, Object> param) throws Exception;

	/** 메세지 읽기 확인 카운트 **/
	int selectChatMessageReadCnt(Map<String, Object> param) throws Exception;

	/** 채팅 내용 읽기 확인 수정 (CDR_READ_YN update) **/
	void updateChatDetailRead(Map<String, Object> param) throws Exception;

	/** 쇼핑몰 사용자 정보 조회  **/
	Map<String, Object> selectMallMemberInfo(Map<String, Object> param) throws Exception;

	/** 쇼핑몰 서비스 상품 정보 조회  **/
	Map<String, Object> selectMallProductInfo(Map<String, Object> param) throws Exception;

	/** 쇼핑몰 채팅방 참여자 등록  **/
	void insertMallChatUser(Map<String, Object> param) throws Exception;

	/** 채팅 정보 조회  **/
	Map<String, Object> selectChatDetailInfo(Map<String, Object> param) throws Exception;

	/** 방에 해당한 총 채팅갯수 얻기 **/
	int getTotalCountInRoom(Map<String, Object> param) throws Exception;

	/** 상담 종료에 해당한 채팅아이디 얻기  **/
	Map<String, Object> getLastExitMessageId(Map<String, Object> param) throws Exception;

	/** 상담 종료이후 갯수 얻기 **/
	int getExitAfterCnt(Map<String, Object> param) throws Exception;

	/** 상담 종료이전 갯수 얻기 **/
	int getExitBeforeCnt(Map<String, Object> param) throws Exception;

	/** 상담 요청 갯수 얻기 **/
	int getTalkingRequestCnt(Map<String, Object> param) throws Exception;

	/** 상담 진행중 갯수 얻기 **/
	int getTalkingRunCnt(Map<String, Object> param) throws Exception;

	/** 견적요청 갯수 얻기 **/
	int getEstimateSendCnt(Map<String, Object> param) throws Exception;

	/** 견결제진행 중 갯수 얻기 **/
	int getEstimateSelectCnt(Map<String, Object> param) throws Exception;

	/** 결제완료 갯수 얻기 **/
	int getSettleOkCnt(Map<String, Object> param) throws Exception;

	/** 채팅방 정보 조회  **/
	Map<String, Object> selectChatMasterInfo(Map<String, Object> param) throws Exception;

	/** 캐스팅엔 연결을 눌렀을때 CD_PK를 얻기 **/
	Map<String, Object> getCDPKByExit(Map<String, Object> param) throws Exception;

	void updateStatusChange(Map<String, Object> param) throws Exception;

	/** 채팅방 판매자 정보 업데이트 **/
	void updateChatMallUser(Map<String, Object> param);

	/** 금칙어 리스트 얻기 **/
	List<Map<String, Object>> selectForbidList() throws Exception;

	/** 카카오 알림톡 발송  **/
	int kakao_insert(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMallCompanyInfo(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMallCustomerInfo(Map<String, Object> param) throws Exception;

	void updateChatUserInLogo(Map<String, Object> param) throws Exception;
		
	List<Map<String, Object>> selectChatUserInfo(Map<String, Object> param) throws Exception;

	void updateAvgResTime(Map<String, Object> map) throws Exception;

	int insertChatServiceQuestion(Map<String, Object> param) throws Exception;
	
	int updateChatServiceQuestion(Map<String, Object> param) throws Exception;
	
	List<Map<String, Object>> selectChatServiceQuestion(Map<String, Object> param) throws Exception;
}
