package castingn.com.chat;

public class ChatVo {

	/**
	 *
	 * 메세지  상태
	 *
	 */
	public enum UserStatus {
    	CHAT, JOIN, LEAVE, FILE, READ, FAILED
	}

	private String pks;             // 고객PK:판매자PK:관리자PK
	private int cdPk;
	private UserStatus userStatus; // 사용자 채팅 상태
	private String cmPk;           // 채팅방Pk(CHAT_MASTER.CM_PK)
	private String content;        // 메세지 내용
	private String userPk;         // 사용자PK
	private String userNm;         // 사용자 이름
	private String userType;       // 사용자 유형코드(1: 고객, 4: 파트너, 11: 관리자)
    private String msgType;        // 메세지 타입
	private String path;           // 채팅방 경로
	private String file;           // 전송 파일 (binary data)
	private String fileName;       // 원본파일명
	private String filePath;       // 파일 저장 경로
	private String fileNewName;    // 저장 파일명
	private String svcCd;		   // 접속 서비스 구분 코드(SVC01 : mall.castingn.com, SVC02: www.castingn.com/admin.castingn.com)
	private int readCnt;		   // 메세지 읽음 카운트
    private String otherInfo;	    // 기타 정보
    private String inDate;          // 채팅시간(년.월.일 시:분)
    private String inTime;          // 채팅시간(시:분)
    private String lastDateTime;    // 마지막채팅시간

    private String qPuid;    		// 서비스상품


	public String getqPuid() {
		return qPuid;
	}
	public void setqPuid(String qPuid) {
		this.qPuid = qPuid;
	}

	private int prPk = 0;    // 요구사항 pk
    private int paPk = 0;    // 견적 pk

    public int getPrPk() { return prPk; }
    public void setprPk(int prPk) {this.prPk = prPk;}

    public int getPaPk() { return paPk; }
    public void setpaPk(int paPk) {this.paPk = paPk;}

    public String getPks() { return pks; }
    public void setPks(String pks) {this.pks = pks;}

    public int getCdPk() {
        return cdPk;
    }

    public void setCdPk(int cdPk) {
        this.cdPk = cdPk;
    }

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public String getCmPk() {
		return cmPk;
    }

    public void setCmPk(String cmPk) {
        this.cmPk = cmPk;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserPk() {
        return userPk;
    }

    public void setUserId(String userPk) {
        this.userPk = userPk;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFile() {
    	return file;
    }

    public void setFile(String file) {
    	this.file = file;
    }

    public String getFileName() {
    	return fileName;
    }

    public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

    public String getFilePath() {
    	return filePath;
    }

	public void setFilePath(String filePath) {
		this.filePath = filePath;
    }

    public String getFileNewName() {
    	return fileNewName;
    }

    public void setFileNewName(String fileNewName) {
    	this.fileNewName = fileNewName;
    }

    public String getSvcCd() {
    	return svcCd;
    }

    public void setSvcCd(String svcCd) {
    	this.svcCd = svcCd;
    }

    public int getReadCnt() {
    	return readCnt;
    }

    public void setReadCnt(int readCnt) {
    	this.readCnt = readCnt;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getInDate() { return inDate; }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getInTime() { return inTime; }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getLastDateTime() { return lastDateTime; }

    public void setLastDateTime(String lastDateTime) {
        this.lastDateTime = lastDateTime;
    }
}
