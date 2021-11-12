var gv_stompClient = null;
var room_stompClient = null;
var seller_serviceData = null;
var isAllowReceive = true;
var lastDateTime = "";

function fnConnectChat() {

    var gv_socket = new SockJS("/ws");
    gv_stompClient = Stomp.over(gv_socket);
    gv_stompClient.connect({}, fnGVOnConnected, fnOnError);

}

function fnConnectChatInRoom() {

    var room_socket = new SockJS("/ws");
    room_stompClient = Stomp.over(room_socket);
    room_stompClient.connect({}, fnRoomOnConnected, fnOnError);
    setChattingListToBottom();
}

function fnGVOnConnected() {

    gv_stompClient.subscribe("/topic/public", fnReceivedMessage);

    gv_stompClient.send("/chat/chat.test_gv",
        {},
        JSON.stringify({path: 'public', content: "global_send"})
    )

}

function fnRoomOnConnected() {
    var gv_path = $('#socketPath').val();

    room_stompClient.subscribe("/topic/" + gv_path, fnReceivedMessageInRoom);

    addUserInChatRoom();

    // //test
    //     var eachHtml = getSimpleQuestionList();
    //     var html = $('.screen-list').html();
    //     html += '<div class="message-right" id="simple_qus_list"><div class="message-box message-mint">' + eachHtml + '</div></div>';
    //     $('.screen-list').html(html);
    //     setChattingListToBottom();



    // gv_stompClient.send("/chatting/chat.addUser",
    //     {},
    //     JSON.stringify({userPk: $("#uPk").val(), userNm: $("#uName").val(), userType: $("#uType").val(), svcCd: $("#svcCd").val(), path: gv_path, userStatus: 'JOIN'})
    // )

}

function fnReceivedMessage(payload) {
    var message = JSON.parse(payload.body);
    //console.log("#### message1 ::: ", message);

}

function fnReceivedMessageInRoom(payload) {
    var message = JSON.parse(payload.body);
    var cmPk = '';
    
    //console.log("#### message2 ::: ", message);
    if(!isAllowReceive){
        return;
    }
    
    if(message.userStatus === "JOIN") {

    	if($('#userType').val() == 1 && message.userType == 1){                 //회원페이지일때
            sendServiceQuestion();
        }
        if(message.msgType == 'restart_user'){
       		addStartChat( message.inTime );
        }
    }
    else if(message.userStatus === "CHAT") { // 일반 채팅 처리
    	cmPk = message.cmPk;
        if(message.content != null) {

        	if(message.msgType == 'date_line'){
                showChatDate(message.content);
            }else{
            	//방장이 채팅메시지를 받음
            	if(message.userPk == $("#uPk").val()){
                    if(message.msgType == 'estimate_send'){
                        addRightEstimateChat(message.cdPk, message.filePath, message.fileName, message.content, message.msgType, message.inTime, message.prPk);
                    }
                    else if(message.msgType == 'estimate_cancel'){
                        removeEstimateChat(message.cdPk);
                        addRightChat(message.content, message.inTime);
                    }
                    else if(message.msgType == 'estimate_update'){
                        removeEstimateChat(message.cdPk);
                        addRightEstimateChat(message.cdPk, message.filePath, message.fileName, message.content, message.msgType, message.inTime, message.prPk);
                    }
                    else if(message.msgType == 'request_send'){
                        addRightRequestChat(message.cdPk, message.filePath, message.fileName, message.content, message.msgType, message.inTime);
                    }
                    else if(message.msgType == 'exit'){
                        if($('#userType').val() == 1)
                            addLeftChat(message.content, message.inTime);
                        else
                            addRightChat(message.content, message.inTime);
                        disableAllButtons(true);
                        $("#btnRestart").attr("disabled", "");
                        addExitChat(message.inTime);
                    }
                    else if(message.msgType == 'reason'){
                    	addRightChat(message.content, message.inTime);
                    	sendExitToCustomer();
                    }
                    // 고객사 화면( 고객사가 매니저 상담 전환 신청했을 경우 중단 )
                    else if(message.msgType == 'customer_castingn_requst' && $('#userType').val() == 1){
                    	onCastingnBtnChange('off');
                    	addRightChat(message.content, message.inTime);
                    } 
                    else if(( message.msgType == 'serviceSelect' || message.msgType == 'serviceCancel') && $('#userType').val() == 1){
                    	addRightChat(message.content, message.inTime);
                    	SetTopServiceInfo(cmPk);
                    }                    
                    // 고객사 화면( 파트너사가 매니저 상담 전환 신청했을 경우 중단 )
                    else if(message.msgType == 'seller_castingn_requst' && $('#userType').val() == 1){
                        disableAllButtons();
                        isAllowReceive = false;
                        if(room_stompClient != null)
                            room_stompClient.disconnect();
                    }
                    //채팅방 오픈 시, 서비스 질문
                    else if($('#userType').val() == 1 && message.msgType == 'service_question'){
                    	addRightChat(message.content, message.inTime);
                    	showRequestQuestion();
                    	SetTopServiceInfo(cmPk);
                    }
                    //채팅방 오픈 시, 서비스 질문 답변 후, 상단 서비스 셋팅
                    else if( $('#userType').val() == 1 && message.msgType == 'simpleAnswer'){
                    	addRightChat(message.content, message.inTime);
                    	SetTopServiceInfo(cmPk);
                    }
                    //제공서비스 선택 시, 메시지 전송
                    else if($('#userType').val() == 1 && message.msgType == 'service_question_another'){
                    	addRightChat(message.content, message.inTime);
                    	sendPopChat();
                    }
                    else {
                        addRightChat(message.content, message.inTime);
                    }

                }
            	//방장이 아닌 유저가 채팅메시지를 받음
                else {
                    if(message.msgType == 'estimate_send' || message.msgType == 'estimate_select'){
                        addLeftEstimateChat(message.cdPk, message.filePath, message.fileName, message.content, message.msgType, message.inTime);
                    }
                    else if(message.msgType == 'estimate_cancel'){
                        removeEstimateChat(message.cdPk);
                        addLeftChat(message.content, message.inTime);
                    }
                    else if(message.msgType == 'estimate_update'){
                        removeEstimateChat(message.cdPk);
                        addLeftEstimateChat(message.cdPk, message.filePath, message.fileName, message.content, message.msgType, message.inTime);
                    }
                    else if(message.msgType == 'request_send'){
                        addLeftRequestChat(message.cdPk, message.filePath, message.fileName, message.content, message.msgType, message.inTime, message.prPk);
                    }
                    else if(message.msgType == 'exit'){
                        if($('#userType').val() == 1)
                            addLeftChat(message.content, message.inTime);
                        else
                            addRightChat(message.content, message.inTime);
                        disableAllButtons(true);
                        $("#btnRestart").attr("disabled", "");
                        addExitChat(message.inTime);
                    }
                    else if(message.msgType == 'reason'){
                    	addLeftChat(message.content, message.inTime);
                    	sendExitToCustomer();
                    }
                        	/*
                    else if(message.msgType == 'castingn_requst'){
                        if($('#userType').val() == 4){
                            disableAllButtons(true);
                            room_stompClient.disconnect();
                        }
                        addLeftChat(message.content, message.inTime);
                    }*/
                    // 고객사 화면( 고객사가 매니저 상담 전환 신청했을 경우 중단 )
                    else if(message.msgType == 'customer_castingn_requst' && $('#userType').val() == 1){
                    	onCastingnBtnChange('off');
                    	addLeftChat(message.content, message.inTime);
                    }    
                    else if(( message.msgType == 'serviceSelect' || message.msgType == 'serviceCancel') && $('#userType').val() == 4){
                    	addLeftChat(message.content, message.inTime);
                    	SetTopServiceInfo(cmPk);
                    }                    
                    // 파트너사 화면( 고객사가 매니저 상담 전환 신청했을 경우 중단 )
                    else if(message.msgType == 'customer_castingn_requst' && $('#userType').val() == 4){
                    	addCastingnRequest();
                    	
                        disableAllButtons();
                        isAllowReceive = false;
                        if(room_stompClient != null)
                            room_stompClient.disconnect();
                    }
                    // 고객사 화면( 파트너사가 매니저 상담 전환 신청했을 경우 중단 )
                    else if(message.msgType == 'seller_castingn_requst' && $('#userType').val() == 1){
                        disableAllButtons();
                        isAllowReceive = false;
                        if(room_stompClient != null)
                            room_stompClient.disconnect();
                    }
                    //채팅방 오픈 시, 서비스 질문 답변 후, 상단 서비스 셋팅
                    else if( $('#userType').val() == 4 && message.msgType == 'simpleAnswer'){
                    	SetTopServiceInfo(cmPk);
                    }                    
                    //채팅방 오픈 시, 서비스 질문
                    else if($('#userType').val() == 1 && message.msgType == 'service_question'){
                    	addLeftChat(message.content, message.inTime);
                        SetTopServiceInfo(cmPk);
                    }                    
                    //제공서비스 선택 시, 메시지 전송
                    else if($('#userType').val() == 1 && message.msgType == 'service_question_another'){
                    	sendPopChat();
                    }                    
                    else {
                        addLeftChat(message.content, message.inTime);
                    }
                }
            }
        	
            setChattingListToBottom();
            lastDateTime = message.inDate;
        }
        
    }
    else if(message.userStatus === "FILE") {    //파일 처리
    	
        if(message.userPk == $("#uPk").val()){
            addRightFileChat(message.filePath, message.fileName, message.inTime);
        }
        else {
            addLeftFileChat(message.filePath, message.fileName, message.inTime, message.userType);
        }        
        setChattingListToBottom();
        lastDateTime = message.inDate;
    }else if(message.userStatus === "FAILED") {
        if(message.content == "forbid_word"){
            if($('#userType').val() == 1 && message.userType == 1){
                alert("금칙어를 사용하실수 없습니다.");
            }
            else if($('#userType').val() == 4 && message.userType == 4){
                alert("금칙어를 사용하실수 없습니다.");
            }
        }
    }


}

//매니저 상담 호출 버튼 상태 변경
function onCastingnBtnChange( onoff ){
	
	if(onoff == 'on'){
		$('#btnCallCastingn').attr("disabled",false);
		
		$('#btnCallCastingn').hover(function() {

			$('#inq-p').html("서비스 제공사와 채팅이 어려우신가요? <br />캐스팅엔 매니저와 상담으로 전환하여 상담을 계속 하실 수 있습니다.");
			$(this).attr("background","2b79fc");
		});
	} else if(onoff == 'off'){
		$('#btnCallCastingn').hover(function() {
			$(this).attr("background","gray");

			$('#inq-p').html("현재 캐스팅엔 매니저와 상담중입니다.<br />공급사와 채팅은 더 이상 진행 되지 않습니다.");
			$('#btnCallCastingn').attr("disabled",true);
		});	
	}
	
}

function fnOnError(error) {
    $(".connecting").text("연결 안됨! 에러 났다!!!!!!!!!!!!!!!");
    $(".connecting").css("color", "red");
}

/**
 *  함수명      : 메세지 읽기 확인
 */
function fnReadMessage(){
    if(room_stompClient == null)
        return;

    var chatMessage = {
        userPk: $("#uPk").val(),
        path: "${cmPk}",
        userStatus: "READ"
    };

    room_stompClient.send("/chat/chat.readMessage", {}, JSON.stringify(chatMessage));


}

function fnSendMessage(event) {

    event.preventDefault();

    // var messageContent = $.trim($("#message").val());
    //
    // if(messageContent && gv_stompClient) {
    //
    //     var chatMessage = {
    //         userPk: $("#uPk").val(),
    //         userNm: $("#uName").val(),
    //         content: $("#message").val().replace(/(?:\r\n|\r|\n)/g, "<br/>"),
    //         path: gv_path,
    //         userStatus: "CHAT"
    //     };
    //
    //     gv_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(chatMessage));
    //
    //     $("#message").val("");
    // }

}

//채팅방에 참여
function addUserInChatRoom(){
    var gv_path = $('#socketPath').val();

    // if($("#userType").val() == "1" && $("#isRoomCreate").val() == "1" ) {
        let sendData = {
            cmPk:$("#cmPk").val(),
            userPk: $("#uPk").val(),
            userNm: $("#uName").val(),
            userType: $("#userType").val(),
            msgType: 'add_user',
            svcCd: $("#svcCd").val(),
            path: gv_path,
            // content: getServiceQuestion(),
            userStatus: 'JOIN'
        }
        room_stompClient.send("/chat/chat.addUser", {}, JSON.stringify(sendData));
    // }
}

//문의 서비스 소켓 보내기
function sendServiceQuestion(){
    var gv_path = $('#socketPath').val();

    if($("#userType").val() == "1" && $("#isRoomCreate").val() == "1" ) {
        let sendData = {
            cmPk:$("#cmPk").val(),
            userPk: $("#uPk").val(),
            userNm: $("#uName").val(),
            userType: $("#userType").val(),
            msgType: 'service_question',
            svcCd: $("#svcCd").val(),
            qPuid: $("#stndCd").val(),
            path: gv_path,
            content: getServiceQuestion(),
            lastDateTime: lastDateTime,
            userStatus: 'CHAT'
        }
        room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
    }
}

function addCastingnRequest(){
	var html = $('.screen-list').html();
	
    html +=	'	<div class="message-text-line">' +
        	'       <dl>' +
        	'           <dt>죄송합니다, 고객의 사유로 상담이 종료되었습니다.</dt>' +
        	'       </dl>' +
        	'   </div>';
    
    $('.screen-list').html(html);	
}

function addCatingnTalkChat() {
    var html = $('.screen-list').html();
    html +=
    	'	<div class="message-text-line">' +
        '       <dl>' +
        '           <dt>캐스팅엔 매니저와 상담이 시작되었습니다.</dt>' +
        '       </dl>' +
        '   </div>';    
    $('.screen-list').html(html);

}

function addStartChat( date ) {
	var html = $('.screen-list').html();

	html += '<div class="message-text-line">' +
    '               <dl>' +
    '               	<dt>채팅이 시작되었습니다.</dt>' +
    '               	<dd>'+date+'</dd>' +
    '               </dl>' +
    '           </div>';
	
	$('.screen-list').html(html);
	$("#divRestart").remove();
	disableAllButtons(false);
	setChattingListToBottom();
}

function addExitChat( date ) {
	var html = $('.screen-list').html();

	html += '<div class="message-text-line">' +
		'	 	<dl>' +
		'       	<dt>채팅이 종료되었습니다</dt>' +
		'           <dd>'+date+'</dd>' +
		'       </dl>' +
		'    </div>';
	
	if($('#userType').val() == 1) {
		
		html += '    <div class="chat-restart" id="divRestart">' +
				'    	<p>문의하실 내용이 더 있으신가요?</p>' +
				'       <button type="button" class="btn-qoute-iq" id="btnRestart" onclick="onReconnectRoom()"><span>상담 시작하기</span><i class="ico-pen"></i></button>' +
				'    </div>';
	}
	$('.screen-list').html(html);
	
}

function addRightChat(eachHtml, inDate) {
	var html = $('.screen-list').html();
	html += '<div class="message-right">' + eachHtml + '<div class="message-date">' + inDate + '</div></div>';
	$('.screen-list').html(html);
	
}

function addLeftChat(eachHtml, inDate) {
    var html = $('.screen-list').html();
    html += '<div class="message-left">' + eachHtml + '<div class="message-date">' + inDate + '</div></div>';
    $('.screen-list').html(html);
}

function addRightFileChat(filePath, fileName, inDate) {
    var html = $('.screen-list').html();
    html += '<div class="message-right">';
    if($("#userType").val() == 1){
        html += '<div class="message-box message-blue">';
    }
    else if($("#userType").val() == 4){
        html += '<div class="message-box message-mint">';
    }
    else if($("#userType").val() == 11){
        html += '<div class="message-box message-pink">';
    }
    html += '       <dl class="tt-type">' +
        '               <strong class="label">첨부파일</strong>' +
        '               <span class="btn-file" onclick="fnFileDownLoad(\'' + filePath + '\', \'' + fileName + '\')">' + fileName + '</span>' +
        '           </dl>' +
        '       </div>' +
        '       <div class="message-date">' + inDate + '</div>' +
        '   </div>';
    $('.screen-list').html(html);
}

function addLeftFileChat(filePath, fileName, inDate, userType) {
    var html = $('.screen-list').html();
    html += '<div class="message-left">';
    
    if(userType == ""){
    	if($("#userType").val() == 1){
            html += '<div class="message-box message-mint">';
        }
        else if($("#userType").val() == 4){
            html += '<div class="message-box message-blue">';
        }
        else if($("#userType").val() == 11){
            html += '<div class="message-box message-pink">';
        }
    }else{    	
    	if(userType == 1){
    		html += '<div class="message-box message-blue">';            
        }
        else if(userType == 4){
        	html += '<div class="message-box message-mint">';
        }
        else if(userType == 11){
            html += '<div class="message-box message-pink">';
        }
    }
    
    html += '       <dl class="tt-type">' +
        '               <strong class="label">첨부파일</strong>' +
        '               <span class="btn-file" onclick="fnFileDownLoad(\'' + filePath + '\', \'' + fileName + '\')">' + fileName + '</span>' +
        '           </dl>' +
        '       </div>' +
        '       <div class="message-date">' + inDate + '</div>' +
        '   </div>';
    $('.screen-list').html(html);
}

function addRightEstimateChat(cdPk, filePath, fileName, eachHtml, msgType, inDate, prPk) {
    var html = $('.screen-list').html();
    html += '<div class="message-right" id="div_estimate' + cdPk + '">' + eachHtml;
    if(fileName != ''){
        html += '<div class="file-box">' +
            '       <strong class="label">첨부파일</strong>' +
            '       <span class="btn-file" onclick="fnFileDownLoad(\'' + filePath + '\', \'' + fileName + '\')">' + fileName + '</span>' +
            '   </div>';
    }
    html += '</dd>' +
    '   </dl>';

    html += '<div class="message-btn-area">' +
    '           <button type="button" class="btn-half gray-bg" onclick="onCancelEstimate('+ cdPk +');"><span>견적서 철회</span></button>' +
    '           <button type="button" class="btn-half emerald-bg" onclick="onEstimateShow('+ prPk +');"><span>견적서 재전송</span></button>' +
    '        </div>' +
    '   </div>' +
    '   <div class="message-date">' + inDate + '</div>' +
    '</div>';
    $('.screen-list').html(html);
}

function addLeftEstimateChat(cdPk, filePath, fileName, eachHtml, msgType, inDate) {
    var html = $('.screen-list').html();
    html += '<div class="message-left" id="div_estimate' + cdPk + '">' + eachHtml;
    if(fileName != ''){
        html += '<div class="file-box">' +
            '       <strong class="label">첨부파일</strong>' +
            '       <span class="btn-file" onclick="fnFileDownLoad(\'' + filePath + '\', \'' + fileName + '\')">' + fileName + '</span>' +
            '   </div>';
    }
    html += '</dd>' +
        '   </dl>';

    html += '<div class="message-btn-area">';
    if(msgType == "estimate_send" || msgType == "estimate_update") {
        html += '   <button type="button" class="btn-full emerald-bg btn-estimate-select" onclick="onEstimateSelect(this, ' + cdPk + ');"><span id="btnEstimate' + cdPk +'">장바구니 담기</span></button>';
    }
    else if(msgType == "estimate_select"){
        html += '   <button type="button" class="btn-full emerald-bg" onclick="onSettlement(' + cdPk + ');"><span id="btnEstimate' + cdPk +'">장바구니 이동</span></button>';
    }
    html += '    </div>' +
        '   </div>' +
        '   <div class="message-date">' + inDate + '</div>' +
        '</div>';
    $('.screen-list').html(html);
}

function addRightRequestChat(cdPk, filePath, fileName, eachHtml, msgType, inDate) {
    var html = $('.screen-list').html();
    html += '<div class="message-right" id="div_request' + cdPk + '">' + eachHtml;
    if(fileName != ''){
        html += '<div class="file-box">' +
            '       <strong class="label">첨부파일</strong>' +
            '       <span class="btn-file" onclick="fnFileDownLoad(\'' + filePath + '\', \'' + fileName + '\')">' + fileName + '</span>' +
            '   </div>';
    }
    html += '</div>' +
        '   <div class="message-date">' + inDate + '</div>' +
        '</div>';
    $('.screen-list').html(html);
}

function addLeftRequestChat(cdPk, filePath, fileName, eachHtml, msgType, inDate, prPk) {
    var html = $('.screen-list').html();
    html += '<div class="message-left" id="div_request' + cdPk + '">' + eachHtml;
    if(fileName != ''){
        html += '<div class="file-box">' +
            '       <strong class="label">첨부파일</strong>' +
            '       <span class="btn-file" onclick="fnFileDownLoad(\'' + filePath + '\', \'' + fileName + '\')">' + fileName + '</span>' +
            '   </div>';
    }
    html += '   <div class="message-btn-area">' +
        '           <button type="button" class="btn-full sky-bg" onclick="onEstimateShow('+prPk+');"><span>간편 견적 보내기</span></button>' +
        '        </div>' +
        '   </div>' +
        '   <div class="message-date">' + inDate + '</div>' +
        '</div>';
    $('.screen-list').html(html);
}


function onCancelEstimate(cdPk){
    var gv_path = $('#socketPath').val();

    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'estimate_cancel',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        content: getEstimateCancelHtml(),
        lastDateTime: lastDateTime,
        userStatus: 'CHAT',
        otherInfo: cdPk,
        prPk: $("#prPk").val(),
        paPk: $("#paPk").val()
    }
    room_stompClient.send("/chat/chat.cancel.estimate", {}, JSON.stringify(sendData));

}

function removeEstimateChat(cdPk){
    if($("#div_estimate" + cdPk) != undefined) {
        $("#div_estimate" + cdPk).remove();
    }
}
	
function setChattingListToBottom(){
	$(".screen-area").stop().animate({ scrollTop: $(".screen-area")[0].scrollHeight}, 1000);
	$(window).on('load', function(){	
		$(".screen-area").stop().animate({ scrollTop: $(".screen-area")[0].scrollHeight}, 1000);
	});
}

function sendNormalChat(){
    if($('#txtNormalChat').val() == ''){
        return;
    }

    var handphone = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test($('#txtNormalChat').val());
    	if(!handphone) handphone = /^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/.test($('#txtNormalChat').val());
    if(handphone) {
        alert("연락처를 이용하실수 없습니다.");
        return;
    }
    
    var phone = /^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}/.test($('#txtNormalChat').val());
    	if(!phone) phone = /^[0-9]{2,3}[0-9]{3,4}[0-9]{4}/.test($('#txtNormalChat').val());
    if(phone) {
        alert("연락처를 이용하실수 없습니다.");
        return;
    }
    
    var handphone = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test($('#txtNormalChat').val());
    if(handphone) {
        alert("연락처를 이용하실수 없습니다.");
        return;
    }
    
 // var emailCheck = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test($('#txtNormalChat').val());
    var emailCheck = /(\w+\.)*\w+@(\w+\.)+[A-Za-z]+/.test($('#txtNormalChat').val());

    if(emailCheck) {
        alert("이메일을 이용하실수 없습니다.");
        return;
    }

    var accountCheck = /([0-9]+)-([0-9]+)/.test($('#txtNormalChat').val());
    if(accountCheck) {
        alert("계좌번호를 이용하실수 없습니다.");
        return;
    }
    
    var contents = $('#txtNormalChat').val();
    
    //shift+엔터 줄바꿈 적용
    contents = contents.replace(/(?:\r\n|\r|\n)/g, '<br/>');
    
    $('#txtNormalChat').val('');
    if($('#userType').val() == 1){
        contents = "<div class=\"message-box message-blue\"><p>" + contents + "</p></div>";
    }
    else if($('#userType').val() == 4){
        contents = "<div class=\"message-box message-mint\"><p>" + contents + "</p></div>";
    }
    else if($('#userType').val() == 11){
        contents = "<div class=\"message-box message-pink\"><p>" + contents + "</p></div>";
    }

    var gv_path = $('#socketPath').val();
    var uPk = $("#uPk").val();

    var cmPk = $("#cmPk").val();
    if(stringNvl(cmPk)) return;
    
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: uPk,
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'normal',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        content: contents,
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
}

/**
 *  함수명          : 채팅방 전송 파일 다운로드
 */
function fnFileDownLoad(filePath, originalFileName) {

    var param = new Object();

    param["filePath"] = filePath; // 첨부파일경로
    param["originalFileName"] = originalFileName; // 첨부파일명(원본파일명)

    setTimeout(function() {

        $.fileDownload("/attachFile/downloadFile.do",{httpMethod:"POST", data:param});
    }, 0);
}


function SetChatList(data){
    var uPk = $('#uPk').val();
    onCastingnBtnChange('on');
    var disabled = false;
    var cmPk = '';
    
    for(let i = 0 ; i < data.length ; i ++){
        let chatData = data[i];
        
        if(chatData.CM_CHAT_STAT == "1" )
        	disabled = false;
        else 
        	disabled = true;
        
        cmPk = chatData.CM_PK;

        if(chatData.CD_CHAT_STAT === "JOIN") {
        	
            if(chatData.CD_MSG_TYPE == 'restart_user'){
           		addStartChat( chatData.INTIME );
            }

        }
        else if(chatData.CD_CHAT_STAT === "CHAT") { // 일반 채팅 처리

            if(chatData.CD_CONTENT != null) {

            	
            	if(chatData.CD_MSG_TYPE == 'date_line'){
                    showChatDate(chatData.CD_CONTENT);
                }
                else {
                	if(chatData.CD_U_PK == uPk){
                        if(chatData.CD_MSG_TYPE == 'estimate_send' || chatData.CD_MSG_TYPE == 'estimate_select'){
                            addRightEstimateChat(chatData.CD_PK, chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.CD_CONTENT, chatData.CD_MSG_TYPE, chatData.INTIME, chatData.CD_R_PR_PK);
                        }
                        else if(chatData.CD_MSG_TYPE == 'estimate_cancel'){
                            removeEstimateChat(chatData.CD_PK);
                            addRightChat(chatData.CD_CONTENT, chatData.INTIME);
                        }
                        else if(chatData.CD_MSG_TYPE == 'estimate_update'){
                            removeEstimateChat(chatData.CD_PK);
                            addRightEstimateChat(chatData.CD_PK, chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.CD_CONTENT, chatData.CD_MSG_TYPE, chatData.INTIME, chatData.CD_R_PR_PK);
                        }
                        else if(chatData.CD_MSG_TYPE == 'request_send'){
                            addRightRequestChat(chatData.CD_PK, chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.CD_CONTENT, chatData.CD_MSG_TYPE, chatData.INTIME);
                        }
                        else if(chatData.CD_MSG_TYPE == 'exit'){
                            if($('#userType').val() == 1)
                                addLeftChat(chatData.CD_CONTENT, chatData.INTIME);
                            else
                                addRightChat(chatData.CD_CONTENT, chatData.INTIME);
                            
                            addExitChat(chatData.INTIME);
                            
                        }
                        // 고객사 화면( 고객사가 매니저 상담 전환 신청했을 경우 중단 )
                        else if(chatData.CD_MSG_TYPE == 'customer_castingn_requst' && $('#userType').val() == 1){
                        	addCatingnTalkChat();
                        	addRightChat(chatData.CD_CONTENT, chatData.INTIME);
                        	onCastingnBtnChange('off');
                        }
                        else if(chatData.CD_MSG_TYPE == 'service_question' && $('#userType').val() == 4){
                        	addRightChat(chatData.CD_CONTENT, chatData.INTIME);
                        }                        
                        // 파트너사 화면( 고객사가 매니저 상담 전환 신청했을 경우 중단 )
                        else if(chatData.CD_MSG_TYPE == 'customer_castingn_requst' && $('#userType').val() == 4){
                            disableAllButtons();
                            isAllowReceive = false;
                            if(room_stompClient != null)
                                room_stompClient.disconnect();
                            break;
                        }
                        // 고객사 화면( 파트너사가 매니저 상담 전환 신청했을 경우 중단 )
                        else if(chatData.CD_MSG_TYPE == 'seller_castingn_requst' && $('#userType').val() == 1){
                            disableAllButtons();
                            isAllowReceive = false;
                            if(room_stompClient != null)
                                room_stompClient.disconnect();
                            break;
                        }
                        else {
                            addRightChat(chatData.CD_CONTENT, chatData.INTIME);
                        }
                    }
                    else {
                        if (chatData.CD_MSG_TYPE == 'estimate_send' || chatData.CD_MSG_TYPE == 'estimate_select') {
                            addLeftEstimateChat(chatData.CD_PK, chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.CD_CONTENT, chatData.CD_MSG_TYPE, chatData.INTIME);
                        }
                        else if(chatData.CD_MSG_TYPE == 'estimate_cancel'){
                            removeEstimateChat(chatData.CD_PK);
                            addLeftChat(chatData.CD_CONTENT, chatData.INTIME);
                        }
                        else if(chatData.CD_MSG_TYPE == 'estimate_update'){
                            removeEstimateChat(chatData.CD_PK);
                            addLeftEstimateChat(chatData.CD_PK, chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.CD_CONTENT, chatData.CD_MSG_TYPE, chatData.INTIME);
                        }
                        else if(chatData.CD_MSG_TYPE == 'request_send'){
                            addLeftRequestChat(chatData.CD_PK, chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.CD_CONTENT, chatData.CD_MSG_TYPE, chatData.INTIME, chatData.CD_R_PR_PK);
                        }
                        else if(chatData.CD_MSG_TYPE == 'exit'){
                            if($('#userType').val() == 1)
                                addLeftChat(chatData.CD_CONTENT, chatData.INTIME);
                            else
                                addRightChat(chatData.CD_CONTENT, chatData.INTIME);
                            
                            addExitChat(chatData.INTIME);
                        }
                        // 고객사 화면( 고객사가 매니저 상담 전환 신청했을 경우 중단 )
                        else if(chatData.CD_MSG_TYPE == 'customer_castingn_requst' && $('#userType').val() == 1){
                        	addCatingnTalkChat();
                        	addLeftChat(chatData.CD_CONTENT, chatData.INTIME);
                            onCastingnBtnChange('off');
                        }             
                        else if(chatData.CD_MSG_TYPE == 'service_question' && $('#userType').val() == 4){
                        	addLeftChat(chatData.CD_CONTENT, chatData.INTIME);

                        }                          
                        // 파트너사 화면( 고객사가 매니저 상담 전환 신청했을 경우 중단 )
                        else if(chatData.CD_MSG_TYPE == 'customer_castingn_requst' && $('#userType').val() == 4){
                        	addCastingnRequest();
                            disableAllButtons();
                            isAllowReceive = false;
                            if(room_stompClient != null)
                                room_stompClient.disconnect();
                            break;
                        }
                        // 고객사 화면( 파트너사가 매니저 상담 전환 신청했을 경우 중단 )
                        else if(chatData.CD_MSG_TYPE == 'seller_castingn_requst' && $('#userType').val() == 1){
                            disableAllButtons();
                            isAllowReceive = false;
                            if(room_stompClient != null)
                                room_stompClient.disconnect();
                            break;
                        }
                        else {
                            addLeftChat(chatData.CD_CONTENT, chatData.INTIME);
                        }
                    }
                }
            	lastDateTime = chatData.INDATE;
            }
        }
        else if(chatData.CD_CHAT_STAT === "FILE") {    //파일 처리

            if(chatData.CD_U_PK == uPk){
                addRightFileChat(chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.INTIME);
            }
            else {            	
            	 
            	var sUpk = $('#sellerUPk').val();
        		var cUpk = $('#customerUPk').val();
            	var setUserType = "1";
            	
        		if(cUpk == chatData.CD_U_PK){
        			setUsetType = "1";
        		}else if(sUpk == chatData.CD_U_PK){
        			setUsetType = "4";
        		}else{
        			setUsetType = "11";
        		}
        		//console.log("cupk : "+ cUpk + " sUpk : " +sUpk + " setUserType : "+ setUsetType);
                addLeftFileChat(chatData.CD_FILE_PATH, chatData.CD_ORG_FILE_NM, chatData.INTIME, setUsetType);
            }
            
            lastDateTime = chatData.INDATE;
        }
    }
    
    //상단 서비스 정보
   	SetTopServiceInfo(cmPk);
    
   	disableAllButtons(disabled);
}

async function onSelectFiles(obj){
    if(obj.files.length > 6){
        swal("최대 6개까지 업로드 가능합니다.");
        return;
    }

    for(var i=0; i<obj.files.length; i++){
        if(obj.files[i].size > 10485760){
            swal("파일 용량은 10MB 이하만 전송 가능합니다.");
            return;
        }
    }
    var gv_path = $('#socketPath').val();

    for(var i=0; i<obj.files.length; i++) {
        var originalFileName = obj.files[i].name;
        var base64Data = await getBase64Code(obj.files[i]);
        let sendData = {
            cmPk:$("#cmPk").val(),
            userPk: $("#uPk").val(),
            userNm: $("#uName").val(),
            userType: $("#userType").val(),
            svcCd: $("#svcCd").val(),
            path: gv_path,
            content: "",
            file: base64Data,
            fileName: originalFileName,
            lastDateTime: lastDateTime,
            userStatus: 'FILE'
        }
        room_stompClient.send("/chat/chat.sendFile", {}, JSON.stringify(sendData));
    }
}

function getBase64Code(file){
    return new Promise(resolve => {
        var reader = new FileReader();
        reader.onloadend = function(res) {
            var base64Data = res.target.result.split(',')[1];
            resolve(base64Data);
        };
        reader.readAsDataURL(file);
    });
}

function disableAllButtons(flags){

	$("#txtNormalChat").attr("disabled", flags);
    $("#btnNormalChat").attr("disabled", flags);
    $("#addFile").attr("disabled", flags);
    $(".bottom-right button").attr("disabled", flags);
    $("#btnGoOut").attr("disabled", false);
    $(".screen-list button").attr("disabled", flags);
    $(".screen-list a").attr("disabled", flags);
 
    $("#btn_request_modal").attr("disabled", flags);
    $("#btnCallCastingn").attr("disabled", flags);
    $(".chat-slc-head button").attr("disabled", flags);
    $(".chat-slc-bottom button").attr("disabled", flags);
    $(".service-item-top button").attr("disabled", flags);
    
    $("#btnRestart").attr("disabled", false);
    
    if(flags)
    	$('.service-item').bind('click', false);
    else 
    	$('.service-item').unbind('click', false);

    if(flags) alert("종료된 상담입니다.");
}


function showChatDate(inDate){

    var html = $('.screen-list').html();

    // var dateHtml = moment(inDate).format('YYYY.MM.DD');

    html += '<div class="message-date-line"><span>' + inDate + '</span></div>';
    $('.screen-list').html(html);

}

//빈 문자열 체크
function stringNvl(str){
	if( typeof str == "undefined" || str == null || str == "" ){
		return true;
	}else{
		return false;
	}
}

function stringNvltoInt(str){
	if( typeof str == "undefined" || str == null || str == "" ){
		return 0;
	}else{
		return pasInt(str);
	}
}

//빈 문자열 체크 후 변환
function stringNvltoStr(input_str, output_str){
	if( typeof input_str == "undefined" || input_str == null || input_str == "" ){
		return output_str;
	}else{
		return input_str;
	}
}

//상단 - '문의중인서비스' 조회 
function SetTopServiceInfo(pcmpk) {
    $.ajax({
        type: 'POST',
        url: "/chat.get.service.list.post",
        data: {cmPk:pcmpk},
        dataType: "json",
        success: function (res) {
            if (res.code == "200") {
                let htmlText = "";
                let data = res.data;
                
                $("#service-item-top").html();
                for(let i = 0 ; i < data.length ; i ++){
                	htmlText += "<div class='service-item'>";

                	if($('#userType').val() == 1 && i != 0 ){
                		htmlText += "	<button type=\"button\" class=\"btn-del\" onclick=\"javascript:serviceCancel('"+data[i].P_UID+"', '" + data[i].LOGO + "', '" + data[i].OPTION_DESC.split(';')[0] + "', '" + comma(data[i].OPTION_PRICE.split(';')[0]) + "');\">닫기</button>";
                	}
                	
                	htmlText += "	<a href='https://mall.castingn.com/casting/service/?cate_mode=view&no="+data[i].P_UID+"' target='_blank'>";
                	htmlText += "		<div class='service-thumb'>";
                	htmlText += "			<img src='" + data[i].LOGO + "' alt=''>";
                	htmlText += "		</div>";
                	htmlText += "		<div class='service-info'>";
                	htmlText += "			<dt>" + data[i].OPTION_DESC.split(';')[0]+ "</dt>";
                	htmlText += "			<dd>" + comma(data[i].OPTION_PRICE.split(';')[0]) + " 원</dd>";
                	htmlText += "		</div>";
                	htmlText += "	</a>";
                	htmlText += "</div>";
                    
                }
                $("#service-item-top").html(htmlText);
            } else {
            	console.log("문의중인 서비스가 없습니다.");                    
                return;
            }
        },
        error: function (data, status, e) {
            alert("문의중인 서비스정보 호출오류가 발생했습니다.");
            return;
        }
    });
}


//상단 - '문의중인서비스' 상품 취소
function serviceCancel(P_UID, logo, desc, price){

	if(confirm('이 서비스 상품에 대한 문의를 취소하시겠습니까?')){
		var gv_path = $('#socketPath').val();

		let sendData = {
			cmPk:$("#cmPk").val(),
			userPk: $("#uPk").val(),
			userNm: $("#uName").val(),
			userType: $("#userType").val(),
			msgType: 'serviceCancel',
			svcCd: $("#svcCd").val(),
			qPuid: P_UID,
			path: gv_path,
			content: getServiceCancel(logo, desc, price),
			lastDateTime: lastDateTime,
			userStatus: 'CHAT'
		}
		room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
	}
}

//채팅 - '문의중인서비스' 상품 취소 멘트
function getServiceCancel(logo, desc, price) {
	var eachHtml = '<div class="message-box message-blue">' +
	'               <dl class="tt-type">' +
	'                   <dt>아래 서비스 문의를 취소합니다. </dt>' +
	'                   <dd>' +
	'                       <div class="thumb-box">' +
	'                           <div class="thumb-img"><img src="'+ logo +'" alt=""></div>' +
	'                           <div class="thumb-info">' +
	'                               <p class="thumb-tt">'+ desc +'</p>' +
	'                               <p class="thumb-price"><span>'+ price +'</span>원</p>' +
	'			                </div>' +
	'				        </div>' +
	'			        </dd>' +
	'               </dl>' +
	'           </div>';
	
	return eachHtml;
	
}
