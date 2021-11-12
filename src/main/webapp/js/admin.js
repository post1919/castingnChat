function sendExitToPartner(){
    var html = '<div class="message-box message-pink"><p>상담이 종료 되었습니다.</p></div>';

    var content = html;
    var gv_path = $('#socketPath').val();
    let sendData = {
        cmPk:$("#cmPk").val(),
        userPk: $("#uPk").val(),
        userNm: $("#uName").val(),
        userType: $("#userType").val(),
        pks: $("#uPkArr").val(),
        msgType: 'exit',
        svcCd: $("#svcCd").val(),
        path: gv_path,
        content: content,
        lastDateTime: lastDateTime,
        userStatus: 'CHAT'
    }
    room_stompClient.send("/chat/chat.sendMessage", {}, JSON.stringify(sendData));

}

function getEstimateHTML(){

    var eachHtml = '<div class="message-box message-pink">' +
        '               <dl class="tt-type">' +
        '                   <dt>간편견적</dt>' +
        '                   <dd>' +
        '                       <div class="label-box">' +
        '                           <strong class="label">제공 서비스 내용 :</strong>' +
        '                           <span>' + $("input:radio[name=p_uid]:checked").next('label').find('.service-chk-info').text() + '</span>' +
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
            userStatus: 'CHAT'
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
            otherInfo: $("#estimate_cdpk").val()
        }
        room_stompClient.send("/chat/chat.update.estimate", {}, JSON.stringify(sendData));
    }
    $('#estimate-pop').removeClass('show');
}

function onEstimateShow(cdPk){
    popShow('estimate-pop');
    $("#estimate_cdpk").val(cdPk);
}

//견적서 철회 html 얻기
function getEstimateCancelHtml() {
    var eachHtml = '<div class="message-box message-pink">' +
        '               <p class="message-txt">견적서가 철회되었습니다.</p>' +
        '           </div>';

    return eachHtml;

}

function onSelectEstimateFile(obj){
    if(obj.files.length > 0){
        $(obj).next('label').find('span').html(obj.files[0].name);

    }
}

function sendFirstChatToCustomer(){
    var html = '<div class="message-box message-pink"><p>상담이 종료 되었습니다.</p></div>';

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