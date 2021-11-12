
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!doctype html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>CASTINGN 판매자 화면</title>
	<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>

	<link rel="stylesheet" href="/css/app.css">
	<link rel="stylesheet" href="/css/advmodals.css">
	<link rel="stylesheet" href="/css/reset.css">
	<link rel="stylesheet" href="/css/layout.css">

	<link rel="stylesheet" href="/css/seller.css">
	

	<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
	<script src="/js/app.js"></script>
	<script src="/js/sockjs-client-1.4.0.js"></script>
	<script src="/js/stomp.min-2.3.3.js"></script>
	<script type="text/javascript" src="/js/jquery.fileDownload.js"></script>
	<script src="/js/moment.min.js"></script>

	<script src="/js/main.js"></script>
	<script src="/js/chat-search.js"></script>
	<script src="/js/seller.js"></script>
	<script type="text/javascript" src="../js/swiper-bundle.min.js"></script>
	<link rel="stylesheet" href="/css/swiper.css">

</head>
<body>

<input type="hidden" name="userType" id="userType" value="4">
<input type="hidden" name="uPk" id="uPk" value="${uPk}">
<input type="hidden" name="customerUPk" id="customerUPk" value="${customerUPk}">
<input type="hidden" name="svcCd" id="svcCd" value="${svcCd}">
<input type="hidden" name="stndCd" id="stndCd" value="${stndCd}">
<input type="hidden" name="cmChatType" id="cmChatType" value="${cmChatType}">
<input type="hidden" name="customerId" id="customerId" value="${customerId}">
<input type="hidden" name="sellerId" id="sellerId" value="${sellerId}">
<input type="hidden" name="pUid" id="pUid" value="${pUid}">
<input type="hidden" name="uPkArr" id="uPkArr" value="${uPkArr}">

<input type="hidden" name="uName" id="uName" value="">

<input type="hidden" name="socketPath" id="socketPath" value="">
<input type="hidden" name="cmPk" id="cmPk" value="${cmPk}">

<div id="wrap" class="chat-wrap">
	<%@ include file="partial/header.jsp" %>
	<div id="container" class="left-hide chat-list-hide">

		<div class="content-wrap">
			<div class="content-wrap-area">
				<!-- 채팅리스트 영역 -->
				<div class="cont-sec chat-list-area">
					<!-- 0813 추가 :Mobile 헤더 -->
					<div class="flex-wrap p-hide">
						<button type="button" class="btn-back top-left">뒤로</button>
						<div class="head-tt">전체</div>
					</div>
					<!-- //0813 추가 :Mobile 헤더 -->
					<div class="search-form">
						<input type="text" id="search_text" name="" placeholder="검색어를 입력하세요" onkeyup="onSearch()">
						<button type="button" class="btn-sh" id="btn_search" onclick="onSearch()">검색</button>
						<button type="button" class="btn-close" id="btn_close" onclick="onClearSearch()"></button>
					</div>
					<div class="chat-list-box">
						<div class="chat-list">

						</div><!-- //chat-list -->
					</div>
					<div class="chat-answer-per">
						<dl>
							<dt>나의 메시지 응답률</dt>
							<dd id="responsePro"></dd>
						</dl>
					</div>
				</div>
				<!-- //채팅리스트 영역 -->
				<!-- 채팅영역 -->
				<div class="cont-sec chat-center-area">
					<!-- 0813 추가 : Mobile 헤더 -->
					<div class="flex-wrap p-hide">
						<button type="button" class="btn-back top-left">뒤로</button>
						<div class="head-tt">캐스팅엔</div>
						<button type="button" class="btn-comp-info top-right">정보보기</button>
					</div>
					<!-- //0813 추가 : Mobile 헤더 -->
					<!-- if : 선택된 채팅이 없을 때 -->
					<div class="chat-no-slc" id="chat_no" style="display:none;">
						<dl>
							<dt>
								<i class="ico-chat"></i>
								<p>채팅을 선택해 주세요</p>
							</dt>
							<!-- <dd> 원하는 판매처와 연결이 어렵나요? <br/>맞춤 견적으로 해결하세요. </dd> -->
						</dl>
						<!-- <button type="button" class="btn-qoute-iq" onclick="onRequestEstimate()"><span>맞춤견적 요청하기</span><i class="ico-pen"></i></button> -->
					</div>
					<!-- else if : 선택된 채팅이 있을때 -->
					<div class="chat-slc-wrap" id="chat_wrap">
						<div class="chat-slc-head m-hide">
							<div class="chat-thumb"><img class="chat-icon" src="/images/chat-thumb.png" alt=""></div>
							<div class="chat-name">skyimage</div>
							<button type="button" class="btn-box" onclick="sendExitToPartner();"><i class="ico-x"></i><span>상담 종료</span></button>							
						</div><!-- //chat-slc-head -->
						<!-- 1001 -->
						<div class="chat-prod-section">
							<p class="chat-prod-title">문의중인 서비스</p>
							<div class="chat-prod-area">
								<div class="sec-in-scroll" id="service-item-top">
								</div>
							</div>
						</div><!--  //chat-prod-section -->						
						<div class="chat-slc-screen">
							<div class="screen-area">
								<div class="screen-list">
								</div>
							</div>
						</div><!-- //chat-slc-screen -->
						<div class="chat-slc-bottom">
							<div class="message-input-box">
								<textarea id="txtNormalChat" name="txtNormalChat"></textarea>
							</div>
							<div class="bottom-other">
								<div class="file-area"><input type="file" id="addFile" name="addFile" multiple onchange="onSelectFiles(this);"><label for="addFile"><i class="ico-file"></i><span>파일첨부파일</span></label></div>
								<!-- <button type="button" class="btn-out" id="btnGoOut" onclick="onClose();">나가기</button>
								<button type="button" class="btn-box" onclick="onCastingnTalk();"><i class="ico-cs"></i><span>캐스팅엔 연결</span></button> -->
								<button type="button" class="btn-box" data-toggle="modal" data-href="#estimate-modal" href="#estimate-modal" onclick="onEstimateShow(0);"><i class="ico-inq"></i><span>간편견적</span></button>
								<button type="button" class="btn-send" id="btnNormalChat" onclick="sendNormalChat();"><span>전송</span><i class="ico-send"></i></button>

							</div>
						</div><!-- //chat-slc-bottom -->
					</div>
					<!-- end if -->
				</div>
				<!-- //채팅영역 -->
				<%@ include file="partial/other_info.jsp" %>
			</div>
		</div><!-- //content-wrap -->
	</div><!-- //container -->
</div>


<%@ include file="message_modal.jsp" %>

<script type="text/javascript">

    let chatUserList = [];
	let chatUserId = 0;
	let chatNoSlc = document.getElementById("chat_no");
	let chatSlc = document.getElementById("chat_wrap");
	chatNoSlc.style.display = "flex";
	chatSlc.style.display = "none";
	
	document.getElementById("btn_search").style.display = "flex";
	document.getElementById("btn_close").style.display = "none";

	function onClose(){
		
		room_stompClient.disconnect();
		
		chatNoSlc.style.display = "flex";
		chatSlc.style.display = "none";
	}
	
	//즐겨찾기 표시
	function onLike(e){
		$(e).toggleClass('active-pick');
	}

	function onRequestEstimate(){

		// if(chatUserList.length == 0) return ;
		// chatUserId = chatUserList[0].id;

		//$(".chat-item:first-child").addClass("active-item");

		// $(".chat-name").html(chatUserList[0].user_id);
		// $(".chat-thumb").attr("src", chatUserList[0].avatar_url);

		chatNoSlc.style.display = "none";
		chatSlc.style.display = "block";

		//회원이 만든 방에 들어간다
		$.ajax({
			type: 'POST',
			url: "/chat.get.chat.list.post",
			data:{
				userType:$('#userType').val(), 
				uPk:$('#uPk').val(), 
				uName:$('#uName').val(),
				svcCd:$('#svcCd').val(), 
				stndCd:$('#stndCd').val(), 
				cmChatType:$('#cmChatType').val(), 
				uPkArr:$('#uPkArr').val(), 
				pUid:$("#pUid").val(),
				cmPk:$("#cmPk").val(),
				userLogo:$("#logo").attr("src")
			},
			dataType: "json",
			success: function (res) {

				$("#socketPath").val(res.path);
				$("#cmPk").val(res.cmPk);
				$("#request_cmPk").val(res.cmPk);

				//이전 채팅내역 표시
				if(res.chatHistList != null){
					SetChatList(res.chatHistList);			
					fnConnectChatInRoom();
				}
				
				$("#responsePro").html(parseFloat(stringNvl(res.responseInfo)? 0:res.responseInfo).toFixed(1) + "%");
				setLeftPanelCount(res);	
			},
			error: function (data, status, e) {
				alert("연결이 끊어졌습니다.");
				return;
			}
		});
    }

	$(document).ready(function(){
		//cmPK가 있을 경우
		if(  $('#cmPk').val().length > 1 ){
			$('#container').removeClass('left-hide');
			$('#container').addClass('chat-list-hide');
		}

		//접속정보 레이어
		$('.login-info-pop .user-name').on('click',function(){
			$(this).parents('.login-info-pop').toggleClass('show-myInfo');
		});

		//좌측 메뉴 컨트롤
		$('.left-wrap .btn-left-toggle').on('click',function(){
			$('#container').toggleClass('left-hide');
		});

		//컴퍼니 정보영역 탭 스크립트
		$('.company-detail-cont .detail-tab-area .btn-tab').click(function(){
			var tab_id = $(this).attr('data-tab');
			$('.company-detail-cont .detail-tab-area .btn-tab').removeClass('active-tab');
			$('.company-detail-cont .detail-cont').removeClass('active-cont');
			$(this).addClass('active-tab');
			$("#"+tab_id).addClass('active-cont');
		});

		$('#txtNormalChat').bind('keypress', function(e) {
			var keycode= (e.keyCode ? e.keyCode : e.which);
			if(keycode == 13){
				sendNormalChat();
                e.preventDefault();
            }
            if( keycode === 13 && e.shiftKey ) {
            	$('#txtNormalChat').text($('#txtNormalChat').val()+'\n');
            }	
		});


		fnConnectChat(); // 소켓연결
		//좌측 채팅방 리스트
		getChatListByMessageType('');
		
		//채팅방 입장
		onRequestEstimate();		
		
		$('.left-wrap .btn-left-toggle').trigger('click');
	});
	
	
	//서비스선택 슬라이드
	var serviceChkSwiper1 = undefined;
	var serviceChkSwiper2 = undefined;
	function serviceChkSwiper_wrap(){
		var serviceChkSwiper1 = new Swiper(".service-slide-container1", {
			spaceBetween: 14,
			slidesPerView: 3,
			navigation: {
				nextEl: ".service-slide-wrap1 .swiper-button-next",
				prevEl: ".service-slide-wrap1 .swiper-button-prev",
			},
			pagination: {
			  el: '.service-slide-wrap1 .swiper-pagination',
			},
		});
		var serviceChkSwiper2 = new Swiper(".service-slide-container2", {
			spaceBetween: 14,
			slidesPerView: 3,
			navigation: {
				nextEl: ".service-slide-wrap2 .swiper-button-next",
				prevEl: ".service-slide-wrap2 .swiper-button-prev",
			},
			pagination: {
			  el: '.service-slide-wrap2 .swiper-pagination',
			},
		});
	}
	
	// 팝업닫기
	$('.pop .btn-pop-close').on('click',function(){
		$(this).parents('.pop').removeClass('show');
	});
	
	//0813 : 모바일에서 컨트롤
	$('.left-wrap .chat-link').on('click',function(){
		$('#container').addClass('left-hide');
	});
	//$('.chat-item').on('click',function(){
	//	$('#container').addClass('chat-list-hide');
	//});
	$('.btn-comp-info').on('click',function(){
		$('.chat-other-area').addClass('pop show');
	});
	$('.chat-other-area .btn-pop-close').on('click',function(){
		$('.chat-other-area').removeClass('pop show');
	});
	$('.chat-list-area .btn-back').on('click',function(){
		$('#container').removeClass('left-hide');
	});
	$('.chat-center-area .btn-back').on('click',function(){
		$('#container').removeClass('chat-list-hide');
	});
	
	function popShow(getID) {		
		$('#'+getID).addClass('show');	
		GetSellerServices();
	}


</script>

</body>
</html>
