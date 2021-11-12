package castingn.com.chat.mapper.chat;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

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

	/** 찜하기 추가 **/
	void likeChatInsert(Map<String, Object> param) throws Exception;

	/** 찜하기 삭제 **/
	void likeChatDelete(Map<String, Object> param) throws Exception;

	/** 검색 **/
	List<Map<String, Object>> search(Map<String, Object> param) throws Exception;

	/** 메모 내역 조회  **/
	List<Map<String, Object>> selectMemo(Map<String, Object> param) throws Exception;

	/** 메모 추가  **/
	void addMemo(Map<String, Object> param) throws Exception;

	/** 메모 삭제  **/
	void deleteMemo(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 생성
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	int insertChatMaster(Map<String, Object> param) throws Exception;

	/**
	 * 카카오 알림톡 생성
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	int kakao_insert(Map<String, Object> param) throws Exception;

	/**
	 * 사용자 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectUserInfo(Map<String, Object> param) throws Exception;

	/**
	 * 의뢰건 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectProjectInfo(Map<String, Object> param) throws Exception;

	/**
	 * 이전 채팅 이력 조회
	 * @param Map<String, Object>
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectChatHistList(Map<String, Object> param) throws Exception;

	/**
	 * 이전 파일 이력 조회
	 * @param Map<String, Object>
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectChatFileHistList(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 존재 유무 확인
	 * @param Map<String, Object>
	 * @return String
	 * @throws Exception
	 */
	String selectCmPk(Map<String, Object> param) throws Exception;

	/**
	 * 최근 메세지
	 * @param Map<String, Object>
	 * @return String
	 * @throws Exception
	 */
	String selectLastChat(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 넘버링 조회
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	int selectCmPkNum(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 참여 확인
	 * @param Map<String, Object>
	 * @return int
	 * @throws Exception
	 */
	int selectChatUserCnt(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 참여자 등록
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	void insertChatUser(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 참여자 등록(쇼핑몰)
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	void insertMallChatUser(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 참여자 정보 변경
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	void updateChatUser(Map<String, Object> param) throws Exception;

	/**
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	void insertChatDetail(Map<String, Object> param) throws Exception;

	/**
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	void updateMsgTypeDetail(Map<String, Object> param) throws Exception;

	/**
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	void updateMsgTypeAndContentDetail(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방별 유저 조회
	 * @param Map<String, Object>
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectChatUserList(Map<String, Object> param) throws Exception;

	/**
	 * 채팅 내용 읽기 확인 등록
	 * @param List<Map<String, Object>>
	 * @throws Exception
	 */
	void insertChatDetailRead(List<Map<String, Object>> param) throws Exception;

	/**
	 * 채팅 내용 읽기 확인 수정 (CDR_READ_YN update)
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	void updateChatDetailRead(Map<String, Object> param) throws Exception;

	/**
	 * 채팅 내용 읽기 확인 카운트
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int selectChatMessageReadCnt(Map<String, Object> param) throws Exception;

	/**
	 * 채팅 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectChatDetailInfo(Map<String, Object> param) throws Exception;

	/**
	 * 방에 해당한 총 채팅갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getTotalCountInRoom(Map<String, Object> param) throws Exception;

	/**
	 * 상담 종료에 해당한 채팅아이디 얻기
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> getLastExitMessageId(Map<String, Object> param) throws Exception;

	/**
	 * 상담 종료이후 갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getExitAfterCnt(Map<String, Object> param) throws Exception;

	/**
	 * 상담 종료이전 갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getExitBeforeCnt(Map<String, Object> param) throws Exception;

	/**
	 * 상담 요청 갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getTalkingRequestCnt(Map<String, Object> param) throws Exception;

	/**
	 * 상담 진행중 갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getTalkingRunCnt(Map<String, Object> param) throws Exception;

	/**
	 * 견적요청 갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getEstimateSendCnt(Map<String, Object> param) throws Exception;

	/**
	 * 결제진행 중 갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getEstimateSelectCnt(Map<String, Object> param) throws Exception;

	/**
	 * 결제완료 갯수 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	int getSettleOkCnt(Map<String, Object> param) throws Exception;

/**
	 * 채팅방 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectChatMasterInfo(Map<String, Object> param) throws Exception;

	/**
	 * 캐스팅엔 연결을 눌렀을때 CD_PK를 얻기
	 * @param Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> getCDPKByExit(Map<String, Object> param) throws Exception;

	/**
	 * 채팅방 상태 변경
	 * @param param cmPk
	 */
	void updateStatusChange(Map<String, Object> param);

	/**
	 * 채팅방 정호 호출(전환)
	 * @param param
	 * @return
	 */
	Map<String, Object> selectChatMasterUserInfo(Map<String, Object> param);

	/**
	 * 채팅방참여 판매자 정보 업데이트
	 * @param param
	 * @return
	 */
	Object updateChatMallUser(Map<String, Object> param);

	/**
	 * 금칙어 리스트 얻기
	 * @param
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectForbidList() throws Exception;

	/**
	 * 로고등록
	 * @param param
	 */
	void updateChatUserInLogo(Map<String, Object> param) throws Exception;
	
	/**
	 * 채팅방 유저 upk 호출
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>>selectChatUserInfo(Map<String, Object> param) throws Exception;
	
	/**
	 * 채팅 서비스 문의 요청 등록
	 * @param param
	 * @return int
	 * @throws Exception
	 */
	int insertChatServiceQuestion(Map<String, Object> param) throws Exception;
	
	/**
	 * 채팅 서비스 문의 요청 상태 변경
	 * @param param
	 * @return int
	 * @throws Exception
	 */
	int  updateChatServiceQuestion(Map<String, Object> param) throws Exception;
	
	/**
	 * 채팅 서비스 문의 요청 내역 조회
	 * @param param
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectChatServiceQuestion(Map<String, Object> param) throws Exception;
	
}
