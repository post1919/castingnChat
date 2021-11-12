
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" type="text/css" href="/css/resetOpenChat.css">
<link rel="stylesheet" type="text/css" href="/css/openChat.css">

<style type="text/css">

	.white_popup_wrap .chatting_wrap .item_chatting_input textarea{border:1px solid #ddd;margin-bottom:0;box-shadow:none;transition:none;padding:5px 13px;width:100%;font-size:14px;color:#999999;box-sizing:border-box;-webkit-box-sizing:border-box;height:100px;line-height:20px;}

	@media screen and (min-width:768px) {
			.white_popup_wrap .chatting_wrap .item_chatting_input textarea{margin-bottom:0;box-shadow:none;transition:none;padding:7px 13px;width:100%;font-size:14px;color:#999999;box-sizing:border-box;-webkit-box-sizing:border-box;height:100px;line-height: 20px}
		}

	.resizable-textarea{resize:none;overflow:hidden;}

</style>
</head>
<body onclick="fnReadMessage();">

<div class="white_popup_wrap show v_window_popup" id="chatting_popup" style="">
    <div class="white_popup">
    	<div class="white_popup_contents border0">
    		<div class="my_popup">
		        <div class="my_head">
					<c:out value="${projectInfo.CATE_NAME}"/>
					<span class="item_gt">&gt;</span>
					<c:out value="${projectInfo.PR_TITLE}"/>
					<span class="item_gt">&gt;</span> 1:1 채팅
					<button class="mobile_x" onClick="history.back();"></button>
				</div>
			<!-- 	<div class="my_pt_info">
					<div class="item_pt_m">
						<div class="item_pt_inner">
							<span class="ico ico_manager01"></span>
							<p class="txt"><span class="key">캐스팅엔 매니저</span>:<span class="val"><c:out value="${CHATTING_USER.MANAGER_NAME}"/></span></p>
							<p class="txt">
								<span class="key">이메일</span>:<span class="val"><c:out value="${CHATTING_USER.MANAGER_EMAIL}"/></span><span class="item_unit">&#47;</span><span class="key">문의전화</span>:<span class="val">1644-2653</span>
							</p>
						</div>
					</div>
				</div>  -->
				<div class="connecting">
					연결중...
				</div>
				<div class="item_my v2">
					<div class="chatting_wrap">
						<div class="item_chat_wrap">
							<ul id="messageArea" class="item_chatting">
							</ul>
							<div class="item_chatting_input" style="max-height: 160px; height: 160px;">
				                <div class="message_wrap">
				                	<label for="message">
				                 	   <textarea class="resizable-textarea shift-enter" id="message" placeholder="메세지를 입력 하세요" autocomplete="off" ></textarea>
				                    </label>
				                    <input type="file" id="attachFile" name="attachFile" style="display:none;" accept=".jpg, .png, application/pdf"/>
				                    <button type="button"class="btn_chat" id="btnSend">보내기</button>
				                    <button type="button"class="btn_chat" id="btnFile" style="margin-top: 55px;">파일전송</button>
				                </div>
				            </div>
						</div>
					</div>
					<input type="hidden" id="uName" value="${userInfo['U_NAME']}"/>
					<input type="hidden" id="uPk" value="${userInfo['U_PK']}"/>
					<input type="hidden" id="uType" value="${userInfo['U_TYPE']}"/>
					<input type="hidden" id="svcCd" value="${svcCd}"/>
					<input type="hidden" id="uPkArr" value="${uPkArr}"/>
				</div>
            </div>
         </div>
    </div>
</div>
<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/js/jquery.fileDownload.js"></script>
<script src="/js/sockjs-client-1.4.0.js"></script>
<script src="/js/stomp.min-2.3.3.js"></script>
<script type="text/javascript">
var gv_messageArea = document.querySelector('#messageArea');

var gv_stompClient = null;
var gv_path = "${path}";

$(document).ready(function(){
	document.onkeydown = fnLockF5;

	// $("#btnConnect").click(fnConnectChat);
	$("#btnSend").click(fnSendMessage); // 보내기 버튼 클릭시
	
	$("#btnFile").click(fnSendFile); // 파일전송 버튼 클릭시
	
	$("#message").on("keydown", function(event) { // 메시지 입력창 enter 클릭시 전송
		
		var val = $.trim($(this).val());

		if(event.keyCode == 13 && val !== "" && !event.shiftKey) {
     		$("#btnSend").click();
     	}

	});

	$("#message").on("keyup", function(event){ // 메시지 바이트 수 계산

		var str = $(this).val();
	    var strLngth = str.length;

	    var rbyte = 0;
	    var rlen = 0;
	    var oneChar = "";
	    var str2 = "";

	    for(var i=0; i<strLngth; i++) {

	    	oneChar = str.charAt(i);

	        if(escape(oneChar).length > 4) {
			rbyte += 2; // 한글 2Byte
	        } else {
	            rbyte++;   // 한글외 나머지 1Byte
	        }

	        if(rbyte <= 3000){
	            rlen = i + 1;
	        }
	     }

	     if(rbyte > 3000){

	    	 alert("메세지는 최대 3000byte를 초과할 수 없습니다.");

			str2 = str.substr(0, rlen); // 문자열 자르기
			$(this).val(str2);

	     }
	});
	 
	 $("#message").on("keypress", function(event) { // 메시지 입력창 enter 줄바꿈 비활성, shift + enter 시 줄바꿈
		 var key = (event.keyCode ? event.keyCode : event.which);
		    if(event.keyCode == 13) {
		    	 if (!event.shiftKey){
		          event.preventDefault(); 
		    	 }
		    }
	 });
	 
	 fnSelectChatHist();
	 
});

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function fnSelectChatHist(){

	$.ajax({
		url: "/selectChatHist",
		data: {cmPk: "${cmPk}"},
		method: "POST",
		dataType: "JSON",
		success: function(json) {
			//console.log("########## :::" , json.chatHistList);
			var chatHistList = json.chatHistList;

			for(var i=0; i < chatHistList.length; i++){
				var msgHtml = "";
				var msgContent = "";
				var CD_U_PK = String(chatHistList[i].CD_U_PK);
				
				var CDR_READ_CNT = chatHistList[i].CDR_READ_CNT == 0 ? "" : chatHistList[i].CDR_READ_CNT;

				if (chatHistList[i].CD_CHAT_STAT === "FILE") {  // 채팅방 파일전송시 처리

			    	if(CD_U_PK === $("#uPk").val()){
			    		msgHtml += "<li class='chatting_me'>";
			    		msgHtml += "<span class='read read_right'>" + CDR_READ_CNT + "</span>";
			    	}else{
			    		msgHtml += "<li class='chatting_user'>";
			    	}

			    	msgContent += "<div class='file_div' onclick='fnFileDownLoad(\"" + chatHistList[i].CD_FILE_PATH + "\", \"" + chatHistList[i].CD_ORG_FILE_NM + "\")'>";

			    	if(fnGetExt(chatHistList[i].CD_ORG_FILE_NM) != "pdf"){ // 전송파일 PDF, 이미지 구분 처리
			    		msgContent += "<img src='/chat/file/" + chatHistList[i].CD_NEW_FILE_NM  + "' width='200' height='200' />";
			    		msgContent += "<p class='img_file'><u class='save_text'>저장</u></p></div>";
			    	}else{
			    		msgContent += "<div class='pdf_file_div'>" + chatHistList[i].CD_ORG_FILE_NM + "<p><u class='save_text'>저장</u></p></div><img class='ico_pdf' src='/images/ico_pdf.png'/>";
			    		msgContent += "</div>";
			    	}

			    	if(CD_U_PK != $("#uPk").val()){
			    		msgContent += "<span class='read read_left'>" + CDR_READ_CNT + "</span>"
			    	}

			    } else if(chatHistList[i].CD_CHAT_STAT === "CHAT"){ // 일반 채팅 처리 

			    	if(CD_U_PK === $("#uPk").val()){
			    		msgHtml += "<li class='chatting_me'>";
			    		msgHtml += "<span class='read read_right'>" + CDR_READ_CNT + "</span>";
			    	}else {

			    		msgHtml += "<li class='chatting_user'>";

						msgContent += "<div>"
								   +  "<span>" + chatHistList[i].U_NAME + "</span></div>";
			    	}

			    	if(chatHistList[i].CD_CONTENT != null) {
				    	msgContent += "<p class='item_say' style='text-align:left;'>";
				
				    	msgContent += fnReplaceUrl(chatHistList[i].CD_CONTENT);
				
				    	msgContent += "</p>";
			    	}
			    	
			    	if(CD_U_PK != $("#uPk").val()){
			    		msgContent += "<span class='read read_left'>" + CDR_READ_CNT + "</span>"
			    	}

			    }

			    msgHtml += msgContent + "</li>";

				$("#messageArea").append(msgHtml);

				gv_messageArea.scrollTop = gv_messageArea.scrollHeight;
			}
			
			fnConnectChat(); // 소켓연결
		}
	});
}

/**
 *  함수명          : 채팅방 WebSocket 연결
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnConnectChat() {

//	event.preventDefault();
	  
   var uName = $.trim($("#uName").val());

    if(uName) {
        $("#username-page").hide();
        $("#chatting_popup").show();

        var socket = new SockJS("/ws");
        gv_stompClient = Stomp.over(socket);

        gv_stompClient.connect({}, fnOnConnected, fnOnError);
    }
}

/**
 *  함수명          : 채팅방 참여
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnOnConnected() {

	gv_stompClient.subscribe("/topic/" + gv_path, fnReceivedMessage);

    gv_stompClient.send("/chat/chat.addUser",
        {},
        JSON.stringify({userPk: $("#uPk").val(), userNm: $("#uName").val(), userType: $("#uType").val(), svcCd: $("#svcCd").val(), path: gv_path, userStatus: 'JOIN'})
    )

    $(".connecting").hide();
}

/**
 *  함수명          : 채팅방 연결 실패시 메시지 처리
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnOnError(error) {
	  $(".connecting").text("연결 안됨! 에러 났다!!!!!!!!!!!!!!!");
	  $(".connecting").css("color", "red");
}

/**
 *  함수명          : 채팅방 메시지 전송
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnSendMessage(event) { 
	 
	event.preventDefault();

	var messageContent = $.trim($("#message").val());

    if(messageContent && gv_stompClient) {

		var chatMessage = {
			userPk: $("#uPk").val(),
			userNm: $("#uName").val(),
            content: $("#message").val().replace(/(?:\r\n|\r|\n)/g, "<br/>"),
            path: gv_path,
            userStatus: "CHAT"
        };

        gv_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(chatMessage));

        $("#message").val("");
	}

}

/**
 *  함수명          : 채팅방 파일 전송
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnSendFile(event) { 

	event.stopPropagation();
	
	var attachFile = $("#attachFile");
	
	attachFile.off("change").on("change", function(e) {
		
		if($(this).val() == ""){ return false;}
		
		if(10485760 < e.target.files[0].size){
			alert("파일 용량은 10MB 이하만 전송 가능합니다.");
			return false;
		}

		var file = e.target.files[0];
		var originalFileName = file.name;

		var reader = new FileReader();
		
		reader.onload = function(){
			
			var pos = 0;
			var base64Data = reader.result.split(',')[1];

			var chatMessage = {
	            	file: base64Data,
	            	fileName: originalFileName,
	            	content: "",
	            	userPk: $("#uPk").val(),
	            	userNm: $("#uName").val(),
	            	path: gv_path,
	            	userStatus: "FILE"
	        	};
		
			gv_stompClient.send("/chat/chat.sendFile", {}, JSON.stringify(chatMessage));

		}

		reader.readAsDataURL(file);

	});
	
	$("#attachFile").trigger("click");
}

/**
 *  함수명      : 메세지 읽기 확인
 *  최초 작성자  : 차성순
 *  최초 작성일  : 2021-04-13
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-04-13
 */
function fnReadMessage(){
	 
	if($("li .read").last().hasClass("read_left")){

		var chatMessage = {
					userPk: $("#uPk").val(),
		            path: "${cmPk}",
		            userStatus: "READ"
		};

		gv_stompClient.send("/chat/chat.readMessage", {}, JSON.stringify(chatMessage));

	}
}

/**
 *  함수명      : 채팅방 전송 메시지 받은 후 처리
 *  최초 작성자  : 차성순
 *  최초 작성일  : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnReceivedMessage(payload) {

    var message = JSON.parse(payload.body);

    var msgHtml = "";
    var msgContent = "";
    
    //console.log("#### message ::: ", message);
	if(message.userStatus === "JOIN") { // 채팅방 참여시 상태 메시지 처리

		msgHtml += "<li>";
//        message.content = message.userNm + " 님이 채팅방에 참여하셨습니다.";
        
        msgContent += "<p style='text-align: center;'>" + message.content + "</p>";

    } else if (message.userStatus === "LEAVE") {  // 채팅방 퇴장시 상태 메시지 처리

    	msgHtml += "<li>";
    	
    //	message.content = message.userNm + " 님이 채팅방에서 퇴장셨습니다.";
        
        msgContent += "<p style='text-align: center;'>" + message.content + "</p>";
        
    } else if (message.userStatus === "FILE") {  // 채팅방 파일전송시 처리

    	if(message.userPk === $("#uPk").val()){
    		msgHtml += "<li class='chatting_me'>";
    		msgHtml += "<span class='read read_right'>" + message.readCnt + "</span>";
    	}else{
    		msgHtml += "<li class='chatting_user'>";
    	}

    	msgContent += "<div class='file_div' onclick='fnFileDownLoad(\"" + message.filePath + "\", \"" + message.fileName + "\")'>";

    	if(fnGetExt(message.fileName) != "pdf"){ // 전송파일 PDF, 이미지 구분 처리
    		msgContent += "<img src='/chat/file/" + message.fileNewName  + "' width='200' height='200' />";
    		msgContent += "<p class='img_file'><u class='save_text'>저장</u></p></div>";
    	}else{
    		msgContent += "<div class='pdf_file_div'>" + message.fileName + "<p><u class='save_text'>저장</u></p></div><img class='ico_pdf' src='/images/ico_pdf.png'/>";
    		msgContent += "</div>";
    	}
    	
    	if(message.userPk != $("#uPk").val()){
    		msgContent += "<span class='read read_left'>" + message.readCnt + "</span>"
    	}

    } else if(message.userStatus === "CHAT") { // 일반 채팅 처리 

    	if(message.userPk === $("#uPk").val()){
    		msgHtml += "<li class='chatting_me'>";
    		msgHtml += "<span class='read read_right'>" + message.readCnt + "</span>";
    	}else{

    		msgHtml += "<li class='chatting_user'>";

			msgContent += "<div>"
					   +  "<span>" + message.userNm + "</span></div>";
    	}
    
    	if(message.content != null) {
	    	msgContent += "<p class='item_say' style='text-align:left;'>";
	
	    	msgContent += fnReplaceUrl(message.content);
	
	    	msgContent += "</p>";
    	}

    	if(message.userPk != $("#uPk").val()){
    		msgContent += "<span class='read read_left'>" + message.readCnt + "</span>"
    	}
    
    }

	msgHtml += msgContent + "</li>";
	
    $("#messageArea").append(msgHtml);
    
    gv_messageArea.scrollTop = gv_messageArea.scrollHeight;
    
    if("READ,JOIN".indexOf(message.userStatus) != -1){
    
    	if(0 < message.readCnt){
    		
    		$($("li .read").get().reverse()).each(function(i, obj){
    			var str = $(obj).text();
    			
    			if(i < message.readCnt){
	    			if(str != ""){
	    				
	    				var value = Number(str) - 1;
	    				
	    				value == 0 ? $(obj).text("") : $(obj).text(value);
	    				
	    			}
    			}
    			
    		});
    	}
    }
}

/**
 *  함수명          : 채팅방 참여인원 구분 (이름 앞에)
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnGetAvatarColor(messageUserNm) {
    var hash = 0;
    for (var i = 0; i < messageUserNm.length; i++) {
        hash = 31 * hash + messageUserNm.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

/**
 *  함수명          : 채팅방 전송 파일 다운로드 
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnFileDownLoad(filePath, originalFileName) {

	var param = new Object();
	
	param["filePath"] = filePath; // 첨부파일경로
	param["originalFileName"] = originalFileName; // 첨부파일명(원본파일명)

	setTimeout(function() {
		$.fileDownload("/attachFile/downloadFile.do",{httpMethod:"POST", data:param});
	}, 0);
}

/**
 *  함수명          : 전송파일 확장자 get 
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnGetExt(fileName){
	var v_fileLngth = fileName.length;

	var v_lastSplit = fileName.lastIndexOf(".");
	var v_fileExt = fileName.substring(v_lastSplit + 1, v_fileLngth);
	
	return v_fileExt;
}

/**
 *  함수명          : 채팅방 새로고침 비활성 
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnLockF5() {

	if (event.keyCode == 116) {
		event.keyCode = 0; return false; 
	}
}

/**
 *  함수명          : 채팅방 메시지 url 구분 
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnValidHttpUrl(url) {

	var regexp =  /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
	// var regexp = /(?:(?:https?|ftp):\/\/|[a-z\w][a-z\w][a-z\w]\.)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}([\/a-z0-9-%#?&=\w])+(\.[a-z0-9]{2,4}(\?[\/a-z0-9-%#?&=\w]+)*)*/gi;
	if (regexp.test(url)){ return true; }
	else { return false; }
}
 
/**
 *  함수명          : 채팅방 메시지 url 문자열 링크 형식으로 변경 
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-16
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-16
 */
function fnReplaceUrl(content) {

	var regexp = /(?:(?:https?|ftp):\/\/|[a-z\w]*\.)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}([\/a-z0-9-%#?&=\w])+(\.[a-z0-9]{2,4}(\?[\/a-z0-9-%#?&=\w]+)*)*/gi;
	var urlArr = new Array();

	content.replace(regexp, function (url) {
		urlArr.push(url);
	});
	
	var newContent = content;
	
	if( 0 < urlArr.length){

		for(var i=0; i < urlArr.length; i++){

			newContent = newContent.replace(urlArr[i], "<a onclick='fnOpenUrl(\"" + urlArr[i] + "\")'><u style='color: blue; cursor:pointer;'>" + urlArr[i] + "</u></a>");
		}

	}else{

		newContent = content;

	}
	
	return newContent;
}

/**
 *  함수명          : url 메시지 오픈 
 *  최초 작성자    : 차성순
 *  최초 작성일    : 2021-03-11
 *  마지막 작성자 : 차성순
 *  마지막 작성일 : 2021-03-11
 */
function fnOpenUrl(url){
	var v_url = url.match(/^https?:/) ? url : '//' + url;
	window.open(v_url, "_blank");
}

function fnRead(){
//	$(".item_say").before("<div style='font-size:9px; color : #fff'>읽음</div>");
}
</script>
</body>
</html>