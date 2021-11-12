function getEstimateHTML(){

	//console.log($('input:radio[name="p_uid"]:checked').next('label').html() );
    var eachHtml = '<div class="message-box message-mint">' +
        '               <dl class="tt-type">' +
        '                   <dt>간편견적</dt>' +
        '                   <dd>' +
        '                       <div class="label-box">' +
        '                           <strong class="label">제공 서비스 내용 :</strong>' +
        '                           <span>(' + $("input:radio[name=p_uid]:checked").next('label').find('.service-chk-info').text() + ') ' + $("#pa_content").val() + '</span>' +
        '                       </div>' +
        '                       <div class="label-box">' +
        '                           <strong class="label">작업일수 :</strong>' +
        '                           <span>' + $("#pa_period").val() + ' 일</span>' +
        '                       </div>' +
        '                       <div class="label-box">' +
        '                           <strong class="label">결제 금액 :</strong>' +
        '                           <span class="t-blue">' + $("#pa_price").val() + '원</span>' +
        '                       </div>';
    return eachHtml;
}

//간편견적을 보냈을때
async function sendEstimateToCustomer() {
    var gv_path = $('#socketPath').val();
    var uPk = $("#uPk").val();

    var originalFileName = "";
    var base64Data = null;
    if($('#addEstimateFile')[0].files[0] != null) {
        if ($('#addEstimateFile')[0].files[0].size > 10485760) {
            swal("파일 용량은 10MB 이하만 전송 가능합니다.");
            return;
        }

        originalFileName = $('#addEstimateFile')[0].files[0].name;
        base64Data = await getBase64Code($('#addEstimateFile')[0].files[0]);
    }

    if($("#estimate_cdpk").val() == 0) {
        let sendData = {
            cmPk: $("#cmPk").val(),
            userPk: uPk,
            userNm: $("#uName").val(),
            userType: $("#userType").val(),
            msgType: 'estimate_send',
            svcCd: $("#svcCd").val(),
            path: gv_path,
            content: getEstimateHTML(),
            file: base64Data,
            fileName: originalFileName,
            lastDateTime: lastDateTime,
            userStatus: 'CHAT',
            prPk: $("#prPk").val(),
            paPk: $("#paPk").val()
        }
        room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));
    }
    else{
        let sendData = {
            cmPk:$("#cmPk").val(),
            userPk: uPk,
            userNm: $("#uName").val(),
            userType: $("#userType").val(),
            msgType: 'estimate_update',
            svcCd: $("#svcCd").val(),
            path: gv_path,
            content: getEstimateHTML(),
            file: base64Data,
            fileName: originalFileName,
            lastDateTime: lastDateTime,
            userStatus: 'CHAT',
            otherInfo: $("#estimate_cdpk").val(),
            prPk: $("#prPk").val(),
            paPk: $("#paPk").val()
        }
        room_stompClient.send("/chat/chat.update.estimate", {}, JSON.stringify(sendData));
    }
    $('#estimate-pop').removeClass('show');
}

function onEstimateShow(cdPk){
	popShow('estimate-pop');
	$("#estimate_cdpk").val(0);
	$("#prPk").val(cdPk);
}

function onResetEstimate(cdPk){
    onEstimateShow(cdPk);
}

//견적서 철회 html 얻기
function getEstimateCancelHtml() {
    var eachHtml = '<div class="message-box message-mint">' +
        '               <p class="message-txt">견적서가 철회되었습니다.</p>' +
        '           </div>';

    return eachHtml;

}

function sendExitToPartner(){
    var html = '<div class="message-box message-mint"><p>상담이 종료 되었습니다.</p></div>';

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
    $("#cnt_talking_request").html(data.talkingRequestCount);
    $("#cnt_talking_run").html(data.talkingRunCount);
    $("#cnt_estimate_send").html(data.estimateSendCount);
    $("#cnt_estimate_select").html(data.estimateSelectCount);
    $("#cnt_settle_ok").html(data.settleOkCount);
    $("#cnt_exit").html(data.cnt_exit);
    $("#cnt_important").html(data.cnt_important);
}

function onSelectEstimateFile(obj){
    if(obj.files.length > 0){
        $(obj).next('label').find('span').html(obj.files[0].name);

    }
}

function getCastingnTalkContentHtml(content){
    var html = '<div class="message-box message-mint"><p>' + content + '</p></div>';
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
        msgType: 'seller_castingn_requst',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        content: getCastingnTalkContentHtml(content),
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));

}

//캐스팅엔 연결을 눌렀을때
function onCastingnTalk(){
    var html = $('.screen-list').html();
    html += '<div class="message-left">' +
        '       <div class="message-box message-blue">' +
        '           <dl class="tt-type">' +
        '               <dt>캐스팅엔 상담</dt>' +
        '               <dd>' +
        '                   <p class="message-txt">불편사항 있으시면 캐스팅엔 전담 매니저를 지원해드립니다. 어떤 상담 원하세요?</p>' +
        '               </dd>' +
        '           </dl>' +
        '       </div>' +
        '   </div>';
    $('.screen-list').html(html);
    
    html = $('.screen-list').html();
    html += '<div class="message-right" id="castingn_talk_select_list">' +
        '       <div class="message-box message-mint">' +
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
}

