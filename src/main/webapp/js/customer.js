//서비스 문의 html 얻기
function getServiceQuestion() {
    // var eachHtml = '<div class="message-right">' +
    // var eachHtml = '<div class="message-box message-blue">' +
    var eachHtml = '<div class="message-box message-blue">' +
        '               <dl class="tt-type">' +
        '                   <dt>문의 서비스</dt>' +
        '                   <dd>' +
        '                       <div class="thumb-box">' +
        '                           <div class="thumb-img"><img src="'+seller_serviceData.logo+'" alt=""></div>' +
        '                           <div class="thumb-info">' +
        '                               <p class="thumb-tt">'+seller_serviceData.p_name.split(';')[0]+'</p>' +
        '                               <p class="thumb-price"><span>'+comma(seller_serviceData.option_price.split(';')[0])+'</span>원</p>' +
        '			                </div>' +
        '				        </div>' +
        '			        </dd>' +
        '               </dl>' +
        '           </div>';

    return eachHtml;

}

//요구사항 요청차트 출력
function showRequestQuestion(){
    if($("#userType").val() == "1" && $("#isRoomCreate").val() == "1" ) {
        var eachHtml = getRequestQuestion();
        var html = $('.screen-list').html();
        html += '<div class="message-left" id="request_question">' + eachHtml + '</div>';
        $('.screen-list').html(html);        
        setChattingListToBottom();
    }
}

function getRequestQuestion() {
    var eachHtml = '<div class="message-box message-mint">' +
        '               <p class="message-txt">구체적인 요구사항이 있으시면 알려주시면 감사하겠습니다.</p>' +
        '               <div class="message-btn-area">' +
        '                   <button type="button" class="btn-half gray-bg" onclick="onShowRequestDlg(0);"><span>요구사항 없음</span></button>' +
        '                   <button type="button" class="btn-half emerald-bg" onclick="onShowRequestDlg(1);"><span>요구사항 있음</span></button>' +
        '               </div>' +
        '           </div>';
    return eachHtml;

}

//간편 질문 리스트 출력
function showSimpleQuestionList(){
    if($("#userType").val() == "1" && $("#isRoomCreate").val() == "1" ) {
    	var infoHtml =getInfoRfqMessage();
        var html = $('.screen-list').html();
        html += '<div class="message-left" id="request_question">' + infoHtml + '</div>';
        $('.screen-list').html(html);          	
    	
        var eachHtml = getSimpleQuestionList();
        html = $('.screen-list').html();
        html += '<div class="message-right" id="simple_qus_list">' + eachHtml + '</div>';
        $('.screen-list').html(html);        
        setChattingListToBottom();
    }
}

function getInfoRfqMessage() {
    var eachHtml = '<div class="message-box message-mint">' +
        '               <p class="message-txt">요구사항이 필요하시면 하단 요구사항 작성을 통해<br/>언제든지 요구사항을 작성 하실 수 있습니다.</p>' +
        '           </div>';
    return eachHtml;

}

function getSimpleQuestionList() {
    var eachHtml = '<div class="message-box message-blue">' +
        '               <dl class="tt-type">' +
        '                   <dt>간편질문</dt>' +
        '                   <dd>' +
        '                       <ul class="message-slc-list">' +
        '                           <li><button type="button" class="btn-message-slc" onclick="setSimpleQuestion(this);"><i class="ico-chk"></i><span>안녕하세요. 진행방식은 어떻게 되나요?</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="setSimpleQuestion(this);"><i class="ico-chk"></i><span>안녕하세요. 언제 가능 하세요?</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="setSimpleQuestion(this);"><i class="ico-chk"></i><span>제 문의와 비슷한 서비스 경험이 있으신가요?</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="setSimpleQuestion(this);"><i class="ico-chk"></i><span>빠른 견적 부탁드려요.</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="setSimpleQuestion(this);"><i class="ico-chk"></i><span>죄송해요. 다음에 부탁 드릴게요.</span></button></li>' +
        '			            </ul>' +
        '			        </dd>' +
        '               </dl>' +
        '           </div>';
    return eachHtml;

}

function setSimpleQuestion(obj){
    var content = $(obj).find('span').html();
    if(content != ''){
        $('#simple_qus_list').remove();
        sendSimpleQuestion(content);
    }
}

function getSimpleQuestionHTML(content){
    var eachHtml = '<div class="message-box message-blue">' +
        '               <dl class="tt-type">' +
        '                   <dt>간편질문</dt>' +
        '                   <dd>' +
        '                       <ul class="message-slc-list">' +
        '                           <li><button type="button" class="btn-message-slc active-slc"><i class="ico-chk"></i><span>' + content + '</span></button></li>' +
        '                       </ul>' +
        '                   </dd>' +
        '               </dl>' +
        '           </div>';
    return eachHtml;
}

//선택된 간편 질문을 소켓으로 보내기
function sendSimpleQuestion(content) {
    var gv_path = $('#socketPath').val();
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'simpleAnswer',
        svcCd: $("#svcCd").val(),
        qPuid: $("#stndCd").val(),
        path: gv_path,
        content: getSimpleQuestionHTML(content),
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
}

function getRequestHTML(content){
	
	var $itemContainers = $(".dev_rfp_titles");
		
	var eachHtml = '<div class="message-box message-blue">  ' +
				   '                   <dl class="tt-type"> ' +
				   '                       <dt>요구사항</dt>';
	
	if( content.length ){
		
		var data = JSON.parse(content);
		
		if( $itemContainers.length > 0 ){
			for(var i = 0; i < $itemContainers.length; i++) {
				
				var $item = $itemContainers.eq(i);
				var list = '        <dd>' +
						   '            <div>' + $item.html() + '</div>'+
						   '            <p class="message-txt"><span class="answer-box">' + data[i].answers[0].answer + '</span></p>'+
						   '        </dd>';
						
				eachHtml += list;
			};
		} else {
			//eachHtml += "<dd><div></div>요구사항<p class='message-txt'><span class='answer-box'>-</span></p> </dd>";
		}
	}
			
	var detailText = $("textarea[name=pr_description]");	
    detailText.val( detailText.val().replace(/(?:\r\n|\r|\n)/g, '<br/>') );

    eachHtml += '           <dd>' +
	   '                        <div>상세 요구사항</div>'+
	   '                        <p class="message-txt"><span class="answer-box">' + detailText.val() + '</span></p>'+
	   '                    </dd>';
	
    eachHtml += '       </dl>';
    
    detailText.val( '' );
    
    return eachHtml;
}

//요구사항을 보냈을때
async function sendRequstToPartner() {

    var originalFileName = "";
    var base64Data = null;    
    if($('#file01')[0].files[0] != null) {
        if ($('#file01')[0].files[0].size > 10485760) {
            swal("파일 용량은 10MB 이하만 전송 가능합니다.");
            return;
        }

        originalFileName = $('#file01')[0].files[0].name;
        base64Data = await getBase64Code($('#file01')[0].files[0]);
    }
    
    var content = $("#request_items").val();
    var gv_path = $('#socketPath').val();
    
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'request_send',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        content: getRequestHTML(content),
        file: base64Data,
        fileName: originalFileName,
        lastDateTime: lastDateTime,
        userStatus: 'CHAT',
        prPk: $("#prPk").val(),
        paPk: $("#paPk").val()
    }    
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
    $('#requirement-pop2').removeClass('show');
}

//요구사항 다이얼로그를 띄우기
function onShowRequestDlg(showFlag){
    $('#request_question').remove();
    if(showFlag == 0){  //나중에
        //onExitConsult();
    	showSimpleQuestionList();
    }
    else{       //등록
        showSimpleQuestionList();
        rfpDlgShow('requirement-pop2');
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

function rfpDlgShow(getID) {
    $('#'+getID).addClass('show');
    GetRfpInfo();
}

//견적선택을 눌렀을때
function onEstimateSelect(btnObj, cdPk) {
    $.ajax({
        type: 'POST',
        url: "/chat.select.estimate.post",
        data:{cdPk:cdPk},
        dataType: "json",
        success: function (res) {
            $("#btnEstimate" + cdPk).html("장바구니 이동");
            $(btnObj).attr("onclick", "onSettlement(" + cdPk + ")");

            $(".btn-estimate-select").removeClass('emerald-bg');
            $(".btn-estimate-select").addClass('gray-bg');
            $(".btn-estimate-select").attr('disabled', true);

            $(btnObj).removeClass('gray-bg');
            $(btnObj).addClass('emerald-bg');
            $(btnObj).attr('disabled', false);

        },
        error: function (data, status, e) {
            alert("연결이 끊어졌습니다.");
            return;
        }
    });
}

//결제하기 버튼을 눌렀을때
function onSettlement(cdPk){
    //location.href = serverUrl + "/casting/Buyr/?page_mode=pay&page_key=form&chat_id=" + cdPk;
	window.open(serverUrl + "/casting/Mypage/?mode=chat_pay&chat_id=" + cdPk);
	//alert("결제하기는 캐스팅엔으로 문의해 주시기 바랍니다.\n\n 대표번호 : 1644-2653");
	return;
}

function onExitConsult(){

    var html = $('.screen-list').html();
    html += '<div class="message-left" id="div_ques_ok">' +
        '       <div class="message-box message-mint">' +
        '           <p class="message-txt">고객님 채팅 상담에 만족하셨습니까?</p>' +
        '           <div class="message-btn-area">' +
        '               <button type="button" class="btn-half gray-bg" onclick="onShowNoConsult();"><span>아니오</span></button>' +
        '               <button type="button" class="btn-half emerald-bg" onclick="onShowYesConsult();"><span>네</span></button>' +
        '           </div>' +
        '       </div>' +
        '   </div>';
    $('.screen-list').html(html);    
    setChattingListToBottom();
}

//상담 만족 아니오
function onShowNoConsult(){

    var html = $('.screen-list').html();
    html += '<div class="message-right">' +
        '       <div class="message-box message-blue"><p>아니오</p></div>' +
        '   </div>';
    $('.screen-list').html(html);
    setChattingListToBottom();
    onShowNoReasonList();
}

function onShowNoReasonList() {
    var html = $('.screen-list').html();
    var eachHtml = '<div class="message-box message-blue" id="div_reason_list">' +
        '               <dl class="tt-type">' +
        '                   <dt>사유</dt>' +
        '                   <dd>' +
        '                       <ul class="message-slc-list">' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>전반적으로 별로였습니다.</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>답변이 너무 느렸습니다.</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>견적이 너무 비쌌습니다. </span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>기타작성 </span></button></li>' +
        '			            </ul>' +
        '			        </dd>' +
        '               </dl>' +
        '           </div>';
    html += eachHtml;
    $('.screen-list').html(html);    
    setChattingListToBottom();

}

//상담 만족 네
function onShowYesConsult(){
    var html = $('.screen-list').html();
    html += '<div class="message-right">' +
        '       <div class="message-box message-blue"><p>네</p></div>' +
        '   </div>';
    $('.screen-list').html(html);    
    setChattingListToBottom();
    onShowYesReasonList();
}

function onShowYesReasonList() {
    var html = $('.screen-list').html();
    var eachHtml = '<div class="message-box message-blue" id="div_reason_list">' +
        '               <dl class="tt-type">' +
        '                   <dt>사유</dt>' +
        '                   <dd>' +
        '                       <ul class="message-slc-list">' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>전체적으로 만족했습니다.</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>견적가가 너무 만족스럽습니다.</span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>성실한 답변에 만족했습니다. </span></button></li>' +
        '                           <li><button type="button" class="btn-message-slc" onclick="showRealReason(this);"><i class="ico-chk"></i><span>기타작성 </span></button></li>' +
        '			            </ul>' +
        '			        </dd>' +
        '               </dl>' +
        '           </div>';
    html += eachHtml;
    $('.screen-list').html(html);    
    setChattingListToBottom();

}

function showRealReason(obj){
    $("#div_reason_list").remove();
    var content = $(obj).find('span').html();
    if(content != ''){
    	var html = '<div class="message-right">' +
        '           <div class="message-box message-blue"><p>' + content + '</p></div>' +
        '       </div>';

	    var gv_path = $('#socketPath').val();
	    let sendData = {
	        cmPk:$("#cmPk").val(),
	        userPk: $("#uPk").val(),
	        userNm: $("#uName").val(),
	        userType: $("#userType").val(),
	        msgType: 'reason',
	        svcCd: $("#svcCd").val(),
	        path: gv_path,
	        content: html,
	        lastDateTime: lastDateTime,
	        userStatus: 'CHAT'
	    }
    	room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
	    setChattingListToBottom();
    }
}

function sendExitToCustomer(){
    var html = '<div class="message-box message-mint"><p>감사합니다. 상담이 종료 되었습니다.</p></div>';

    var content = html;
    var gv_path = $('#socketPath').val();
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'exit',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        content: content,
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));

}

function setLeftPanelCount(data){
    $("#cnt_total").html(data.totalCount);
    $("#cnt_unread").html(data.unreadCount);
    $("#cnt_transaction").html(data.transactionCount);
    $("#cnt_exit").html(data.cnt_exit);
    $("#cnt_important").html(data.cnt_important);
}





function getCastingnTalkContentHtml(content){
    var html = '<div class="message-box message-blue"><p>' + content + '</p></div>';
    return html;
}

//캐스팅엔에게 요청을 보내기
function sendCastingnTalkContent(obj){
    var content = $(obj).find('span').html();

    $("#castingn_talk_select_list").remove();

    var gv_path = $('#socketPath').val();
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'customer_castingn_requst',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        content: getCastingnTalkContentHtml(content),
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));

}


function onSelectRequestFile(obj){
    if(obj.files.length > 0){
        $(obj).next('label').find('span').html(obj.files[0].name);

    }
}
//캐스팅엔 연결을 눌렀을때
function onCastingnTalk(){
    var html = $('.screen-list').html();
    html +=
    	'	<div class="message-text-line">' +
        '       <dl>' +
        '           <dt>캐스팅엔 매니저와 상담이 시작되었습니다.</dt>' +
        '       </dl>' +
        '   </div>' +
    	
    	'	<div class="message-left">' +
        '       <div class="message-box message-mint">' +
        '           <dl class="tt-type">' +
        '               <dt>캐스팅엔 상담</dt>' +
        '               <dd>' +
        '                   <p class="message-txt">불편사항 있으시면 캐스팅엔 전담 매니저를 지원해드립니다. 어떤 상담 원하세요?</p>' +
        '               </dd>' +
        '           </dl>' +
        '       </div>' +
        '   </div>';
    $('.screen-list').html(html);    
    setChattingListToBottom();

    html = $('.screen-list').html();
    html += '<div class="message-right" id="castingn_talk_select_list">' +
        '       <div class="message-box message-blue">' +
        '           <dl class="tt-type">' +
        '               <dt>상담선택</dt>' +
        '               <dd>' +
        '                   <ul class="message-slc-list">' +
        '                       <li><button type="button" class="btn-message-slc" onclick="sendCastingnTalkContent(this);"><i class="ico-chk"></i><span>견적 지연 상담 원합니다.</span></button></li>' +
        '                       <li><button type="button" class="btn-message-slc" onclick="sendCastingnTalkContent(this);"><i class="ico-chk"></i><span>답변 지연 상담 원합니다.</span></button></li>' +
        '                       <li><button type="button" class="btn-message-slc" onclick="sendCastingnTalkContent(this);"><i class="ico-chk"></i><span>취소/교환/환불 상담 원합니다.</span>. </span></button></li>' +
        '                       <li><button type="button" class="btn-message-slc" onclick="sendCastingnTalkContent(this);"><i class="ico-chk"></i><span>맞춤견적 원합니다.</span></button></li>' +
        '			        </ul>' +
        '			    </dd>' +
        '           </dl>' +
        '       </div>';
        '   </div>';
    $('.screen-list').html(html);    
    setChattingListToBottom();
}

//콤마찍기
function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

//채팅 - 팝업에 담긴 '문의중인서비스' 멘트
function sendPopChat(){
    if($('#txtPopChat').val() == ''){
        return;
    }

    var handphone = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test($('#txtPopChat').val());
    	if(!handphone) handphone = /^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/.test($('#txtPopChat').val());
    if(handphone) {
        alert("연락처를 이용하실수 없습니다.");
        return;
    }
    
    var phone = /^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}/.test($('#txtPopChat').val());
    	if(!phone) phone = /^[0-9]{2,3}[0-9]{3,4}[0-9]{4}/.test($('#txtPopChat').val());
    if(phone) {
        alert("연락처를 이용하실수 없습니다.");
        return;
    }
    
    var handphone = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test($('#txtPopChat').val());
    if(handphone) {
        alert("연락처를 이용하실수 없습니다.");
        return;
    }
    
    var emailCheck = /(\w+\.)*\w+@(\w+\.)+[A-Za-z]+/.test($('#txtPopChat').val());

    if(emailCheck) {
        alert("이메일을 이용하실수 없습니다.");
        return;
    }

    var accountCheck = /([0-9]+)-([0-9]+)/.test($('#txtPopChat').val());
    if(accountCheck) {
        alert("계좌번호를 이용하실수 없습니다.");
        return;
    }
    
    var contents = $('#txtPopChat').val();
    
    //shift+엔터 줄바꿈 적용
    contents = contents.replace(/(?:\r\n|\r|\n)/g, '<br/>');
    
    $('#txtPopChat').val('');
    contents = "<div class=\"message-box message-blue\"><p>" + contents + "</p></div>";

    var gv_path = $('#socketPath').val();
    var uPk = $("#uPk").val();

    var cmPk = $("#cmPk").val();
    if(stringNvl(cmPk)) return;
    
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: uPk,
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'serviceSelect',
        svcCd: $("#svcCd").val(),
        qPuid: $("#qPuid").val(),
        path: gv_path,
        content: contents,
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
    
    closePopup();
}

//팝업에 담긴 '문의중인서비스' 상품 추가
function sendServiceQuestionPop(){
    var gv_path = $('#socketPath').val();

    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'service_question_another',
        svcCd: $("#svcCd").val(),
        //stndCd: $("#qPuid").val(),
        path: gv_path,
        content: getServiceQuestionPop(),
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
}

//'문의중인 서비스' 팝업 내용
function getServiceQuestionPop() {
    var eachHtml = '<div class="message-box message-blue">' +
        '               <dl class="tt-type">' +
        '                   <dt>문의 서비스</dt>' +
        '                   <dd>' +
        '                       <div class="thumb-box">' +
        '                           <div class="thumb-img"><img src="'+ $("#popLogo").attr('src') +'" alt=""></div>' +
        '                           <div class="thumb-info">' +
        '                               <p class="thumb-tt">'+$("#popDesc").text()+'</p>' +
        '                               <p class="thumb-price"><span>'+$("#popPrice").text()+'</span>원</p>' +
        '			                </div>' +
        '				        </div>' +
        '			        </dd>' +
        '               </dl>' +
        '           </div>';

    return eachHtml;

}

//상담종료 후 다시 상담시작하기 
function onReconnectRoom(){
    var gv_path = $('#socketPath').val();
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        msgType: 'restart_user',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        userStatus: 'JOIN'
    }
    room_stompClient.send("/chat/chat.customer.reconnect.room.post", {}, JSON.stringify(sendData));
}