
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- 담당자 정보 영역 -->
<div class="cont-sec chat-other-area">
    <div class="company-info-top2">
        <div class="company-logo"><img src="" name="admin_logo" alt=""></div>
        <div class="compnay-info-area">
            <div class="company-name"><i class="ico-manager">담당자</i><span name="admin_name">담당자</span></div>
            <ul class="info-detail">
                <li><span class="label">평균응답시간</span><span class="info-cont" id="admin_avg_time"></span></li>
                <li><span class="label">처리상태</span><span class="info-cont dev_status">상담접수</span></li>
                <li><span class="label">처리일자</span><span class="info-cont overflow-none"><em class="time dev_entry_date"></em></span></li>
            </ul>
        </div>
    </div><!-- //company-info-top2 -->
    <div class="company-detail-cont">
        <div class="detail-tab-area">
            <button type="button" class="btn-tab active-tab" data-tab="manager-cont1">상담정보</button>
            <button type="button" class="btn-tab" data-tab="manager-cont2">파일</button>
            <button type="button" class="btn-tab" data-tab="manager-cont3">메모</button>
        </div>
        <div class="detail-cont-area">
            <!-- 상담정보 -->
            <div class="detail-cont active-cont" id="manager-cont1">
                <div class="consult-info">
                    <ul>
                        <li>
                            <span class="label">접수일자</span>
                            <p class="cont-info dev_cm_date">년 월 일</p>
                        </li>
                        <li>
                            <span class="label">카테고리</span>
                            <p class="cont-info dev_catename">카테고리정보</p>
                        </li>
                        <li>
                            <span class="label">상품명</span>
                            <p class="cont-info dev_itemname">상품정보</p>
                        </li>
                        <li>
                            <span class="label">진행상태</span>
                            <p class="cont-info t-blue  dev_quote">견적요청</p>
                        </li>
                        <li>
                            <span class="label">견적서</span>
                            <p class="cont-info dev_cm_quote_file"></p>
                        </li>
                        <li>
                            <span class="label">구매여부</span>
                            <p class="cont-info"><span class="ico-txt dev_pay_flag">N</span></p>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- //상담정보 -->
            <!-- 파일 -->
            <div class="detail-cont" id="manager-cont2">
            </div>
            <!-- //파일 -->
            <!-- 메모 -->
            <div class="detail-cont" id="manager-cont3">
                <div class="memo-create">
                    <p class="date">2021.08.17</p>
                    <div class="create">
                        <textarea rows="4" id="memo_text"></textarea>
                    </div>
                    <div style="text-align: right;">
                        <button onclick="onAddMemo()">추가</button>
                    </div>
                </div>
                <div class="memo-list">
                </div>
            </div>
            <!-- //메모 -->
        </div>
    </div>
</div>
<!-- //담당자 정보 영역 -->

<script>
    function SetFileList() {    	
    	
        $.ajax({
            type: 'POST',
            url: "/chat.get.chatfile.list.post",
            data:{userType:$('#userType').val(), uPk:$('#uPk').val(), svcCd:$('#svcCd').val(), stndCd:$('#stndCd').val(), cmChatType:$('#cmChatType').val(), uPkArr:$('#uPkArr').val(), pUid:$("#pUid").val(),cmPk:$("#cmPk").val()},
            dataType: "json",
            success: function (res) {
            	
                let data = res.chatHistList;
                let htmlText = "";
                for(let i = 0 ; i < data.length ; i ++){
                    htmlText += '<div class="file-item">';
                    htmlText += '<span class="date">' + data[i].CD_INDATE + '</span>';
                    htmlText += '<div class="file-info">';
                    let list = data[i].list;
                    for(let j = 0 ; j < list.length; j ++)
                    {
                        let userType = "";
                        if(list[j].U_TYPE == 1) userType = "[고객] ";
                        else if(list[j].U_TYPE == 4) userType = "[파트너] ";
                        else if(list[j].U_TYPE == 11) userType = "[관리자] ";
                        htmlText += '<p><a onclick="fnFileDownLoad(\'' + list[j].CD_FILE_PATH + '\', \'' + list[j].CD_ORG_FILE_NM + '\')">' + userType + list[j].CD_ORG_FILE_NM + '</a></p>';
                    }
                    htmlText += '</div>';
                    htmlText += '</div>';
                }
                //console.log(htmlText);

                $("#manager-cont2").html(htmlText);
            },
            error: function (data, status, e) {
                alert("파일정보 호출오류가 발생했습니다.");
                return;
            }
        });
    }
    SetFileList();

    function SetMemoList() {
        $.ajax({
            type: 'POST',
            url: "/admin/memo",
            data:{userType:$('#userType').val(), uPk:$('#uPk').val(), svcCd:$('#svcCd').val(), stndCd:$('#stndCd').val(), cmChatType:$('#cmChatType').val(), uPkArr:$('#uPkArr').val()},
            dataType: "json",
            success: function (res) {
                let data = res.memo;
                //console.log(res.memo);
                let htmlText = "";
                for(let i = 0 ; i < data.length ; i ++){
                    htmlText += '<div class="memo-item">';
                    htmlText += '<span class="date">'+data[i].MM_INDATE+'</span>';

                    let list = data[i].list;
                    for(let j = 0 ; j < list.length ; j ++){
                        htmlText += '<div class="memo-info">';
                        htmlText += '<div class="memo-text">';
                        htmlText += '<p>'+list[j].MM_CONTENTS+'</p>';
                        htmlText += '</div>';
                        htmlText += '<div style="text-align: right;">';
                        htmlText += '<button onclick="onDeleteMemo('+list[j].MM_PK+')">삭제</button>';
                        htmlText += '</div>';
                        htmlText += '</div>';
                    }

                    htmlText += '</div>';
                }
                $(".memo-list").html(htmlText);
            },
            error: function (data, status, e) {
                alert("메모 호출오류가 발생했습니다.");
                return;
            }
        });
    }
    SetMemoList();

    function onAddMemo(){
        if($("#memo_text").val() == ""){
            swal("메모를 입력하세요.");
            return ;
        }

        $.ajax({
            type: 'POST',
            url: "/admin/memo/add",
            data:{ content: $("#memo_text").val() },
            dataType: "json",
            success: function (res) {
                SetMemoList();
            },
            error: function (data, status, e) {
                alert("메모 등록에 실패 하였습니다.");
                return;
            }
        });
    }


    function onDeleteMemo(id){
        $.ajax({
            type: 'POST',
            url: "/admin/memo/delete",
            data:{ mmPK: id },
            dataType: "json",
            success: function (res) {
                SetMemoList();
            },
            error: function (data, status, e) {
                alert("메모 삭제에 실패했습니다.");
                return;
            }
        });
    }

    function setToday() {
        let today = new Date();
        let year = today.getFullYear();
        let month = today.getMonth()+1;
        let date = today.getDate();
        $(".memo-create .date").html(year+"."+month+"."+date);
    }
    setToday();
    
    
    function SetChatInfo() {
    	
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/admin/admin_chat_info.php",
            data: {AppID:"admin_chat_info", Passwd:"admin_chat_info_pwd", cmPk:$("#cmPk").val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    $(".dev_entry_date").text(res.data[0].entry_date);
                    $(".dev_cm_date").text(res.data[0].entry_date);
                    $(".dev_catename").text(res.data[0].category_name);
                    $(".dev_itemname").text(res.data[0].item_name);
                    $(".dev_quote").text(res.data[0].status);
                    $(".dev_status").text(res.data[0].status);
                    
                    if( res.data[0].quote_flag == "Y" ){
                    	$(".dev_cm_quote_file").html("<a href='"+serverUrl+"/"+res.data[0].quote_filename+"' target='_blank'>"+res.data[0].quote_origin_filename+"</a>");
                    } else {
                    	$(".dev_cm_quote_file").text("-");
                    }
                    
                    
                    $(".dev_pay_flag").text(res.data[0].pay_flag);
                } else {
                    console.log("상담 정보 호출에 실패하였습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("상담 정보 호출 오류가 발생했습니다.");
                return;
            }
        });
    }

    SetChatInfo();
</script>
