
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
	<title>CASTINGN 고객화면</title>

	<link rel="stylesheet" href="/css/app.css">
	<link rel="stylesheet" href="/css/advmodals.css">
	<link rel="stylesheet" href="/css/reset.css">
	<link rel="stylesheet" href="/css/layout.css">
	<link rel="stylesheet" href="/css/sweetalert.css">
	<link rel="stylesheet" href="/css/swiper.css">

	<link rel="stylesheet" href="/css/customer.css">

	<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
	<script src="/js/app.js"></script>
	<script src="/js/sockjs-client-1.4.0.js"></script>
	<script src="/js/stomp.min-2.3.3.js"></script>
	<script src="/js/sweetalert.min.js"></script>
	<script src="/js/sweetalert-dev.js"></script>
	<script type="text/javascript" src="/js/jquery.fileDownload.js"></script>

	<script type="text/javascript" src="/js/swiper-bundle.min.js"></script>
	<script src="/js/moment.min.js"></script>

	<script src="/js/main.js"></script>
	<script src="/js/chat-search.js"></script>
	<script src="/js/customer.js"></script>
</head>
<body onclick="fnReadMessage();">

<input type="hidden" name="userType" id="userType" value="1">
<input type="hidden" name="uPk" id="uPk" value="${uPk}">
<input type="hidden" name="sellerUPk" id="sellerUPk" value="${sellerUPk}">
<input type="hidden" name="svcCd" id="svcCd" value="${svcCd}">
<input type="hidden" name="stndCd" id="stndCd" value="${stndCd}">
<input type="hidden" name="cmChatType" id="cmChatType" value="${cmChatType}">
<input type="hidden" name="customerId" id="customerId" value="${customerId}">
<input type="hidden" name="sellerId" id="sellerId" value="${sellerId}">
<input type="hidden" name="pUid" id="pUid" value="${pUid}">
<input type="hidden" name="uPkArr" id="uPkArr" value="${uPkArr}">
<input type="hidden" name="uName" id="uName" value="">
<input type="hidden" name="sName" id="sName" value="">

<input type="hidden" name="prPk" id="prPk" value="">
<input type="hidden" name="paPk" id="paPk" value="">

<input type="hidden" name="socketPath" id="socketPath" value="">
<input type="hidden" name="cmPk" id="cmPk" value="${cmPk}">
<input type="hidden" name="isRoomCreate" id="isRoomCreate" value="1">
<input type="hidden" name="qPuid" id="qPuid" value="">

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
				<div class="cont-sec chat-center-area" style="display:block;">
					<!-- 0813 추가 : Mobile 헤더 -->
					<div class="flex-wrap p-hide">
						<button type="button" class="btn-back top-left">뒤로</button>
						<div class="head-tt">캐스팅엔 </div>
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
							<dd> 원하는 판매처와 연결이 어렵나요? <br/>맞춤 견적으로 해결하세요. </dd>
						</dl>
						<button type="button" class="btn-qoute-iq" onclick="onRequestEstimate()"><span>채팅 시작하기</span><i class="ico-pen"></i></button>
					</div>
					<!-- else if : 선택된 채팅이 있을때 -->
					<div class="chat-slc-wrap" id="chat_wrap"> <!--  종료된 채팅일때 disabled-wrap -->
						<div class="chat-slc-head m-hide">
							<div class="chat-thumb">
								<img class="chat-icon" src="/images/chat-thumb.png" alt="">
							</div>
							<div class="chat-name">skyimage</div>
							<button type="button" class="btn-box" onclick="onExitConsult();">
								<i class="ico-x"></i> <span>상담 종료</span>
							</button>
						</div>
						<!-- //chat-slc-head -->
						<!-- 1001 -->
						<div class="chat-prod-section">
							<p class="chat-prod-title">문의중인 서비스</p>
							<div class="chat-prod-area">
								<div class="sec-in-scroll" id="service-item-top">
								</div>
							</div>
						</div>
						<!--  //chat-prod-section -->
						<div class="chat-slc-screen">
							<div class="screen-area">
								<div class="screen-list">
								</div>
							</div>
							<div class="chat-inq-area">
								<div class="inq-layer">
									<p id="inq-p">
										서비스 제공사와 채팅이 어려우신가요? <br />캐스팅엔 매니저와 상담으로 전환하여 상담을 계속 하실 수 있습니다.
									</p>
								</div>
								<button type="button" class="btn-box" id="btnCallCastingn" onclick="onCastingnTalk();">
									<i class="ico-cs"></i>
									<span>캐스팅엔 매니저상담으로 전환</span>
								</button>
							</div>
						</div>
						<!-- //chat-slc-screen -->
						<div class="chat-slc-bottom">
							<div class="message-input-box">								
								<textarea id="txtNormalChat" name="txtNormalChat"></textarea>
							</div>
							<div class="bottom-other">
								<div class="file-area"><input type="file" id="addFile" name="addFile" multiple onchange="onSelectFiles(this);"><label for="addFile"><i class="ico-file"></i><span>파일첨부파일</span></label></div>								
								<button type="button" class="btn-box" id="btn_request_modal" onclick="rfpDlgShow('requirement-pop2')"><i class="ico-inq"></i><span>요구사항 작성</span></button>
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

<!-- 1001 -->
<div class="pop" id="servicePopup">
	<div class="pop-flex-wrap">
		<div class="pop-wrap">
			<button type="button" class="btn-pop-close">닫기</button>
			<div class="pop-container estimate-container">
				<div class="pop-title"><strong>상품 공유</strong></div>
				<div class="pop-content estimate-content">
					<div class="estimate-scroll-area">
						<div class="estimate-set-box">
							<div class="estimate-sec">
								<div class="sec-tt">
									<i class="ico-sec"></i>
									<strong>서비스 선택 </strong>
								</div>
								<div class="sec-cont">
									<div class="service-item" id="service-item-pop">
									</div>
								</div>
							</div>
							<div class="estimate-sec">
								<div class="sec-tt">
									<i class="ico-sec"></i>
									<strong>채팅 메세지</strong>
								</div>
								<div class="sec-cont">
									<div class="form-text">
										<textarea name="txtPopChat" id="txtPopChat" ></textarea>
									</div>									
								</div>
							</div>
							<div class="estimate-item-control">
								<button type="button" class="btn-send" id="btnNormalChat" onclick="sendServiceQuestionPop();"><span>전송</span><i class="ico-send"></i></button>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
	</div>
</div>



<%@ include file="message_modal.jsp" %>





<script type="text/javascript">

	let chatNoSlc = document.getElementById("chat_no");
	let chatSlc = document.getElementById("chat_wrap");
	chatNoSlc.style.display = "flex";
	chatSlc.style.display = "none";

	document.getElementById("btn_search").style.display = "flex";
	document.getElementById("btn_close").style.display = "none";

	function onRequestEstimate(){
		//console.log("customer.jsp : onRequestEstimate");
		//방을 만들고 들어간다
		$.ajax({
			type: 'POST',
			url: "/chat.customer.enter.room.post",
			data:{
				userType:$('#userType').val(), 
				uPk:$('#uPk').val(), 
				uName:$('#uName').val(), 
				sName:$('#sName').val(), 
				svcCd:$('#svcCd').val(), 
				stndCd:$('#stndCd').val(), 
				cmChatType:$('#cmChatType').val(), 
				uPkArr:$('#uPkArr').val(), 
				pUid:$('#pUid').val(), 
				sellerUPk:$("#sellerUPk").val(), 
				pUpk:$('#customerId').val()+":"+$('#sellerId').val(),
				cmPk:$('#cmPk').val(),sName:$("#seller_name").text(),				
				userLogo:$("#logo").attr("src"),
				sellerLogo:$("#seller_company_logo").attr("src")
			},
			dataType: "json",
			success: function (res) {
				if(res.state == "200"){
					$("#socketPath").val(res.path);
					$("#cmPk").val(res.cmPk);
					$("#cmPk1").val(res.cmPk);
					$("#isRoomCreate").val(res.isRoomCreate);
					fnConnectChatInRoom();

				}else{
					alert("오류");
					return;
				}
			},
			error: function (data, status, e) {
				alert("연결이 끊어졌습니다.");
				return;
			}
		});
		
        chatNoSlc.style.display = "none";
        chatSlc.style.display = "block";
        
    }
	
	function onClose(){
		if(room_stompClient != null)
			room_stompClient.disconnect();
		chatNoSlc.style.display = "flex";
		chatSlc.style.display = "none";
	}
	

    function setChatHistList(){

        //$(".chat-item:first-child").addClass("active-item");        
        $.ajax({
            type: 'POST',
            url: "/chat.get.chat.list.post",
            data:{userType:$('#userType').val(), uPk:$('#uPk').val(), svcCd:$('#svcCd').val(), stndCd:$('#stndCd').val(), 
            	cmChatType:$('#cmChatType').val(), uPkArr:$('#uPkArr').val(), pUid:$("#pUid").val(), cmPk:$("#cmPk").val()},
            dataType: "json",
            success: function (res) {
				//console.log(res);
                //이전 채팅내역 표시
                if(res.chatHistList != null && res.chatHistList.length > 0) {
					SetChatList(res.chatHistList);
					onRequestEstimate();
				}
                $("#responsePro").html(parseFloat(stringNvl(res.responseInfo)? 0:res.responseInfo).toFixed(1) + "%");
            },
            error: function (data, status, e) {
                alert("연결오류가 발생했습니다.");
                return;
            }
        });
    }

	$(document).ready(function(){
		
		//접속정보 레이어
		$('.login-info-pop .user-name').on('click',function(){
			$(this).parents('.login-info-pop').toggleClass('show-myInfo');
		});

		//좌측 메뉴 컨트롤
		$('.left-wrap .btn-left-toggle').on('click',function(){
			$('#container').toggleClass('left-hide');
		});
		
		//cmPK가 있을 경우
		if(  $('#cmPk').val().length > 1 ){
			$('#container').addClass('left-hide');
			$('#container').addClass('chat-list-hide');
		} else {
			//$('.chat-list-area').css("display","none");
			//$('#chat_no').css("display","none");
			//$('#chat_wrap').css("display","flex");
			//$('#container').toggleClass('left-hide');
			
		}
		
		$('#container').addClass('left-hide');
		
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
            if(keycode == 13 && !e.shiftKey ){
                sendNormalChat();
                e.preventDefault();
            }
            if( keycode === 13 && e.shiftKey ) {
            	$('#txtNormalChat').text($('#txtNormalChat').val()+'\n');
            }	
        });


		// 팝업닫기
		$('.pop .btn-pop-close').on('click',function(){
			$(this).parents('.pop').removeClass('show');
		});
		
		//0813 : 모바일에서 컨트롤
		$('.left-wrap .chat-link').on('click',function(){
			$('#container').addClass('left-hide');
		});
		
		$('.chat-item').on('click',function(){
			$('#container').addClass('chat-list-hide');
		});
		
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
		
		
		setChatHistList();
		
		//좌측 채팅방 리스트
		getChatListByMessageType('');
		
		fnConnectChat(); // 소켓연결
	
		//$('.left-wrap .btn-left-toggle').trigger('click');
				
	});

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
		pfSwiper_wrap();
	}

</script>

</body>
</html>
