
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
	<title>CASTINGN 관리자화면</title>

<%--	<link rel="stylesheet" href="/css/app.css">--%>
	<link rel="stylesheet" href="/css/advmodals.css">
	<link rel="stylesheet" href="/css/reset.css">
	<link rel="stylesheet" href="/css/layout.css">
	<link rel="stylesheet" href="/css/layout_admin.css">
	<link rel="stylesheet" href="/css/sweetalert.css">
	<link rel="stylesheet" href="/css/swiper.css">
	<link rel="stylesheet" href="/css/admin.css">

	<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
<%--	<script src="/js/app.js"></script>--%>
	<script src="/js/sockjs-client-1.4.0.js"></script>
	<script src="/js/stomp.min-2.3.3.js"></script>
	<script src="/js/sweetalert.min.js"></script>
	<script src="/js/sweetalert-dev.js"></script>
	<script type="text/javascript" src="/js/jquery.fileDownload.js"></script>

	<script type="text/javascript" src="/js/swiper-bundle.min.js"></script>
	<script src="/js/moment.min.js"></script>

	<script src="/js/main.js"></script>
	<script src="/js/admin.js"></script>
	<script type="text/javascript" src="../js/swiper-bundle.min.js"></script>
	<link rel="stylesheet" href="/css/swiper.css">
</head>
<body onclick="fnReadMessage();">

<input type="hidden" name="userType" id="userType" value="11">
<input type="hidden" name="uPk" id="uPk" value="${uPk}">
<input type="hidden" name="customerUPk" id="customerUPk" value="${customerUPk}">
<input type="hidden" name="sellerUPk" id="sellerUPk" value="${sellerUPk}">
<input type="hidden" name="svcCd" id="svcCd" value="${svcCd}">
<input type="hidden" name="stndCd" id="stndCd" value="${stndCd}">
<input type="hidden" name="pUid" id="pUid" value="${pUid}">
<input type="hidden" name="cmChatType" id="cmChatType" value="${cmChatType}">
<input type="hidden" name="adminId" id="adminId" value="${adminId}">
<input type="hidden" name="customerId" id="customerId" value="${customerId}">
<input type="hidden" name="sellerId" id="sellerId" value="${sellerId}">
<input type="hidden" name="uPkArr" id="uPkArr" value="${uPkArr}">
<input type="hidden" name="cmPk" id="cmPk" value="${cmPk}">
<input type="hidden" name="uName" id="uName" value="">

<input type="hidden" name="socketPath" id="socketPath" value="">

<div id="wrap" class="chat-wrap">
	<%@ include file="partial/header.jsp" %>
	<div id="container" class="layout-default">
		<div class="content-wrap">
			<div class="content-wrap-area">
				<%@ include file="partial/user_seller_info.jsp" %>
				<!-- 채팅영역 -->
				<div class="cont-sec chat-center-area">

					<div class="chat-slc-wrap" id="chat_wrap">
						<div class="chat-slc-head">
							<div class="chat-thumb"><img class="chat-icon" src="/images/chat-thumb.png" alt=""></div>
							<div class="chat-name">skyimage</div>
						</div><!-- //chat-slc-head -->
						<div class="chat-slc-screen">
							<div class="screen-list">

							</div>
						</div><!-- //chat-slc-screen -->
						<div class="chat-slc-bottom">
							<div class="message-input-box">
								<input type="text" id="txtNormalChat" name="txtNormalChat">
								<button type="button" class="btn-send" id="btnNormalChat" onclick="sendNormalChat();"><span>전송</span><i class="ico-send"></i></button>
							</div>
							<div class="bottom-other">
								<div class="file-area"><input type="file" id="addFile" name="addFile" multiple onchange="onSelectFiles(this);"><label for="addFile"><i class="ico-file"></i><span>파일첨부파일</span></label></div>
								<div class="bottom-right">
									<button type="button" class="btn-out" id="btnGoOut" onclick="onClose();">나가기</button>
									<button type="button" class="btn-box" onclick="sendExitToPartner();"><i class="ico-x"></i><span>상담 종료</span></button>
									<button type="button" class="btn-box blue-type" onclick="onEstimateShow(0);"><i class="ico-inq"></i><span>간편견적</span></button>
								</div>
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


<!-- 포트폴리오 팝업 -->
<div class="pop" id="pf-pop">
	<div class="pop-flex-wrap">
		<div class="pop-wrap">
			<button type="button" class="btn-pop-close">닫기</button>
			<div class="pop-container portfolio-container">
				<div class="portfolio-wrap swiper-container">
					<div class="swiper-wrapper">
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
					</div>
					<div class="swiper-pagination"></div>
					<div class="swiper-button-next"></div>
					<div class="swiper-button-prev"></div>
				</div>
				<div class="thumb-wrap">
					<div thumbsSlider="" class="swiper-container portfolio-thumb-wrap">
						<div class="swiper-wrapper">
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
							<div class="swiper-slide"><div class="pf-img"><img src="../images/pf-img.jpg" alt=""></div></div>
						</div>
					</div>
					<div class="thumb-button-next"></div>
					<div class="thumb-button-prev"></div>
				</div>
			</div>
		</div>
	</div>
</div>


<%@ include file="message_modal.jsp" %>

<script type="text/javascript">

	function onClose(){
		
		room_stompClient.disconnect();
		window.close();
	}

	//즐겨찾기 표시
	function onLike(e){
		$(e).toggleClass('active-pick');
	}

	function onRequestEstimate(){
		// if(chatUserList.length == 0) return ;
		// chatUserId = chatUserList[0].id;
		console.log("admin.jsp : onRequestEstimate");
		$(".chat-item:first-child").addClass("active-item");

		// $(".chat-name").html(chatUserList[0].user_id);
		// $(".chat-thumb").attr("src", chatUserList[0].avatar_url);

		// chatNoSlc.style.display = "none";
		// chatSlc.style.display = "block";

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
				cmPk:$('#cmPk').val(), 
				userLogo:$('#logo').attr("src")
			},
			dataType: "json",
			success: function (res) {

				$("#socketPath").val(res.path);
				$("#cmPk").val(res.cmPk);

				//이전 채팅내역 출력
				if(res.chatHistList != null) {
					SetChatList(res.chatHistList);
				}
				else{

				}

				fnConnectChatInRoom();

			},
			error: function (data, status, e) {
				alert("연결이 끊어졌습니다.");
				return;
			}
		});

    }

	$(document).ready(function(){
		//접속정보 레이어
		$('.login-info-pop .user-name').on('click',function(){
			$(this).parents('.login-info-pop').toggleClass('show-myInfo');
		});


		//파트너 정보영역 탭 스크립트
		$('.partner-detail-cont .detail-tab-area .btn-tab').click(function(){
			var tab_id = $(this).attr('data-tab');
			$('.partner-detail-cont .detail-tab-area .btn-tab').removeClass('active-tab');
			$('.partner-detail-cont .detail-cont').removeClass('active-cont');
			$(this).addClass('active-tab');
			$("#"+tab_id).addClass('active-cont');
		});

		//컴퍼니 정보영역 탭 스크립트
		$('.company-detail-cont .detail-tab-area .btn-tab').click(function(){
			var tab_id = $(this).attr('data-tab');
			$('.company-detail-cont .detail-tab-area .btn-tab').removeClass('active-tab');
			$('.company-detail-cont .detail-cont').removeClass('active-cont');
			$(this).addClass('active-tab');
			$("#"+tab_id).addClass('active-cont');
		});

		//리뷰 더보기
		$('.review-txt-area .btn-review-open').on('click',function(){
			$(this).parents('.review-txt-area').toggleClass('show-all');
			if($(this).parents('.review-txt-area').hasClass('show-all')){
				// 접기 상태
				$(this).text('[접기]');
				$(this).parents('.review-txt-area').addClass('show-all');
			}else{
				// 더보기 상태
				$(this).text('[더보기]');
				$(this).parents('.review-txt-area').removeClass('show-all');
			}
		});


		// 팝업닫기
		$('.pop .btn-pop-close').on('click',function(){
			$(this).parents('.pop').removeClass('show');
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
		onRequestEstimate();

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

	//포트폴리오 슬라이드
	var pfThumb = undefined;
	var pfSwiper = undefined;
	function pfSwiper_wrap(){
		var pfThumb = new Swiper(".portfolio-thumb-wrap", {
			spaceBetween: 7,
			slidesPerView: 7,
			freeMode: true,
			watchSlidesVisibility: true,
			watchSlidesProgress: true,
			navigation: {
				nextEl: ".thumb-button-next",
				prevEl: ".thumb-button-prev",
			},

		});
		var pfSwiper = new Swiper(".portfolio-wrap", {
			spaceBetween: 0,
			navigation: {
				nextEl: ".swiper-button-next",
				prevEl: ".swiper-button-prev",
			},
			thumbs: {
				swiper: pfThumb,
			},
			pagination: {
				el: '.swiper-pagination',
				type: 'fraction',
			},

		});
	}


	// id값으로 팝업띄우기
	function popShow(getID) {
		$('#'+getID).addClass('show');
		GetSellerServices();
	}


</script>

</body>
</html>
