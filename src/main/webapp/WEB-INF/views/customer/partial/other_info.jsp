
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- 컴퍼니 정보 영역 -->
<div class="cont-sec chat-other-area">
	<!-- 0813 추가 : Mobile 헤더 -->
	<div class="pop-head p-hide">
		<div class="pop-title"><strong>전체</strong></div>
		<button type="button" class="btn-pop-close">닫기</button>
	</div>
	<!-- //0813 추가 : Mobile 헤더 -->
    <div class="company-info-top">
        <div class="company-logo"><img src="" id="seller_company_logo" alt=""></div>
        <div class="company-name"><i class="ico-grade"></i><span id="seller_name">캐스팅엔</span></div>
        <div class="company-rating-area">
            <div class="rating-box">
                <dl>
                    <dt>평가지표</dt>
                    <dd>
                        <div class="rating-star total_rating">
                            <!-- <span class="ico-star star-on"></span>
                            <span class="ico-star star-on"></span>
                            <span class="ico-star star-on"></span>
                            <span class="ico-star star-on"></span>
                            <span class="ico-star star-on"></span> -->
                        </div>
                    </dd>
                </dl>
            </div>
            <div class="rating-box">
                <dl>
                    <dt>구매건수</dt>
                    <dd><div class="rating-case" id="seller_order_cnt"></div></dd>
                </dl>
            </div>
        </div>
    </div><!-- //company-info-top -->
    <div class="company-detail-cont">
        <div class="detail-tab-area">
            <button type="button" class="btn-tab active-tab" data-tab="cont1">판매처 정보</button>
            <button type="button" class="btn-tab" data-tab="cont2">포트폴리오</button>
            <div class="tab-other"><button type="button" class="btn-review">리뷰<strong class="count total_reviews"></strong></button></div>
        </div>
        <div class="detail-cont-area">
            <!-- 판매처 정보 -->
            <div class="detail-cont active-cont" id="cont1">
                <dl class="detail-box">
                    <dt class="detail-tt">기업소개</dt>
                    <dd class="detail-in-box">
                        <p class="in-txt" id="seller_intro"></p>
                    </dd>
                </dl>
                <dl class="detail-box">
                    <dt class="detail-tt">기본정보</dt>
                    <dd class="detail-in-box">
                        <ul class="info-list">
                            <li>
                                <i class="ico-info1"></i>
                                <p>직원수 : 32명</p>
                            </li>
                            <li>
                                <i class="ico-info2"></i>
                                <p>설립일 : 2018년 12월 30일</p>
                            </li>
                            <li>
                                <i class="ico-info3"></i>
                                <p>최근 매출액 : 500 천원</p>
                            </li>
                            <li>
                                <i class="ico-info4"></i>
                                <p>위치 : 서울시 강남구 도곡동</p>
                            </li>
                            <li>
                                <i class="ico-info5"></i>
                                <p>연락 가능 시간 : 12:00~ 19:00</p>
                            </li>
                            <li>
                                <i class="ico-info6"></i>
                                <p>평균 응답 시간 : 15분</p>
                            </li>
                        </ul>
                    </dd>
                </dl>
                <dl class="detail-box">
                    <dt class="detail-tt">제공서비스<!--  <a href="#" class="btn-more">전체 서비스<i class="ico-more"></i></a> --></dt>
                    <dd class="detail-in-box">
                        <div class="service-list" id="service-list">

                        </div>
                    </dd>
                </dl>
            </div>
            <!-- //판매처 정보 -->
            <!-- 포트폴리오 -->
            <div class="detail-cont" id="cont2">
                <dl class="detail-box">
                    <dt class="detail-tt">포트폴리오 <!-- <a href="" class="btn-more">전체 보기<i class="ico-more"></i></a> --></dt>
                    <dd class="detail-in-box">
                        <ul class="portfolio-list">
                        </ul>
                    </dd>
                </dl>
                <dl class="detail-box">
                    <dt class="detail-tt">리뷰 <!-- <a href="" class="btn-more">전체 보기<i class="ico-more"></i></a> --></dt>
                    <dd class="detail-in-box">
                        <div class="review-total">
                            <strong class="rating-num total_score">0.0</strong>
                            <div class="rating-star total_rating">
                                <!-- <span class="ico-star star-on"></span>
                                <span class="ico-star star-on"></span>
                                <span class="ico-star star-on"></span>
                                <span class="ico-star star-on"></span>
                                <span class="ico-star star-on"></span> -->
                            </div>
                            <div class="total-count">총 리뷰<span class="t-blue total_reviews">0건</span></div>
                        </div><!-- //review-total -->
                        <div class="review-list">
                        </div><!-- //review-list -->
                    </dd>
                </dl>
            </div>
            <!-- //포트폴리오 -->
        </div>
    </div>
</div>
<!-- //컴퍼니 정보 영역 -->
<script type="text/javascript">


    function onReviewItem(e){
        $(e).parents('.review-txt-area').toggleClass('show-all');
        if($(e).parents('.review-txt-area').hasClass('show-all')){
            // 접기 상태
            $(e).text('[접기]');
            $(e).parents('.review-txt-area').addClass('show-all');
        }else{
            // 더보기 상태
            $(e).text('[더보기]');
            $(e).parents('.review-txt-area').removeClass('show-all');
        }
    }

    function SetSellerInfo() {
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/seller/seller_info.php",
            data: {AppID:"seller_info", Passwd:"seller_info_pwd", SellerID:$("#sellerId").val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    $("#seller_company_logo").attr("src", res.data[0].logo);
                                        
                    $("#seller_name").html(res.data[0].company_name);
                    $("#sName").val(res.data[0].company_name);
                    $("#seller_order_cnt").html(res.data[0].order_cnt + " 건");
                    $("#seller_intro").html(res.data[0].company_desc);
                    $("#seller_company_desc").html(res.data[0].company_desc);
                    $("#seller_last_login").html(res.data[0].last_login);
                    $("#seller_avg_time").html(res.data[0].avg_time);
                                        
                    var infoHtml = '<li>' +
                        '               <i class="ico-info1"></i>' +
                        '               <p>직원수 : '+res.data[0].C_EMP_CNT+'명</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info2"></i>' +
                        '               <p>설립일 : '+res.data[0].C_FOUND_DATE+'</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info3"></i>' +
                        '               <p>최근 매출액 : '+((eval(res.data[0].C_CUR_SALES)*1000)/100000000).toFixed(1)+' 억원</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info4"></i>' +
                        '               <p>위치 : '+res.data[0].C_address+'</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info5"></i>' +
                        '               <p>연락 가능 시간 : '+res.data[0].res_time+'</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info6"></i>' +
                        '               <p>평균 응답 시간 : '+res.data[0].avg_time+'</p>' +
                        '           </li>';

                    $(".info-list").html(infoHtml);
                } else {
                    console.log("판매자 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 정보 호출오류가 발생했습니다.");
                return;
            }
        });
    }
    function SetSellerServiceInfo() {
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/seller/seller_services.php",
            data: {AppID:"seller_service", Passwd:"seller_service_pwd", SellerID:$("#sellerId").val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    let htmlText = "";
                    let data = res.data;
                    for(let i = 0 ; i < data.length ; i ++){
                    	htmlText += "<div class=\"service-item\">"; //1001 선택 됐을때 select-item 추가
                        htmlText += "<a href=\"javascript:openPopup('"+data[i].logo+"','"+data[i].option_desc.split(';')[0]+"','"+comma(data[i].option_price.split(';')[0])+"','"+data[i].p_uid+"');\">";
                        htmlText += "<div class=\"service-thumb\"><img src='"+data[i].logo+"' alt=''></div>";
                        htmlText += "<div class=\"service-info\">";
                        htmlText += "<dl>";
                        htmlText += "<dt>"+data[i].option_desc.split(';')[0]+"</dt>";
                        htmlText += "<dd>"+comma(data[i].option_price.split(';')[0])+" 원</dd>";
                        htmlText += "</dl>";
                        htmlText += "</div>";
                        htmlText += "</a>";
                        htmlText += "</div>";

                        if(data[i].p_uid == $("#pUid").val()) {
                            seller_serviceData = data[i];
                        }

                    }
                    $("#service-list").html(htmlText);

                } else {
                	console.log("판매자 서비스가 없습니다.");                    
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 서비스정보 호출오류가 발생했습니다.");
                return;
            }
        });
    }
    
    // 서비스문의 팝업보이기
    function openPopup(logo, desc, price, pUid){

    	let htmlText = "";
        htmlText += "<div class=\"service-thumb\">"; 
        htmlText += "	<img id='popLogo' src='"+logo+"' alt=\"\">";
        htmlText += "</div>";
        htmlText += "<div class=\"service-info\">";
        htmlText += "	<dl>";
        htmlText += "		<dt id='popDesc'>"+desc.split(';')[0]+"</dt>";
        htmlText += "		<dd id='popPrice'>"+comma(price.split(';')[0])+" 원</dd>";
        htmlText += "	</dl>";
        htmlText += "</div>";

        $("#qPuid").val(pUid);
        $("#service-item-pop").html(htmlText);

		$('#servicePopup').addClass('show');
    }

    // 서비스문의 팝업닫기
    function closePopup(){

		$('#servicePopup').removeClass('show');
    }
    
    function SetPortfolios() {
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/seller/seller_portfolios.php",
            data: {AppID:"seller_portfolio", Passwd:"seller_portfolio_pwd", SellerID:$("#sellerId").val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    let htmlText = "";
                    let data = res.data;
                    for(let i = 0 ; i < data.length ; i ++){
                        htmlText += "<li><button type=\"button\" class=\"btn-pf\" onclick=\"popShow('pf-pop');\"><img src=\""+data[i].pp_logo+"\" alt=\"\"></button></li>";
                    }
                    $(".portfolio-list").html(htmlText);
                } else {                    
                    console.log("판매자 포트폴리오가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("포트폴리오 호출오류가 발생했습니다.");
                return;
            }
        });
    }
    function SetReviews() {
    	//console.log($("#sellerId").val());
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/seller/seller_reviews.php",
            data: {AppID:"seller_review", Passwd:"seller_review_pwd", SellerID:$("#sellerId").val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    let htmlText = "";
                    let data = res.data;
                    var review_avg = 0;
                    for(let i = 0 ; i < data.length ; i ++){
                        htmlText += "<div class=\"review-item\">";
                        htmlText += "<p class=\"review-id\">"+data[i].rv_name+"</p>";
                        htmlText += "<div class=\"review-info\">";
                        htmlText += "<div class=\"rating-star\">";
                        for(let  j = 0 ; j < 5; j ++ ){
                            htmlText += "<span class=\"ico-star "+ (j < data[i].rv_point ? "star-on" : "star-off") + "\"></span>";
                        }
                        htmlText += "</div>";
                        htmlText += "<div class=\"review-date\">"+data[i].rv_time+"</div>";
                        htmlText += "</div>";
                        htmlText += "<div class=\"review-txt-area\">";
                        htmlText += "<div class=\"review-txt\">"+data[i].rv_content+"</div>";
                        htmlText += "<button type=\"button\" class=\"btn-review-open\" onclick=\"onReviewItem(this)\">[더보기]</button>";
                        htmlText += "</div>";
                        htmlText += "</div>";
                        
                        review_avg = eval(review_avg) + eval(data[i].rv_point);
                    }
                    $(".review-list").html(htmlText);
                    
                    var review_score = eval(review_avg) / eval(data.length);
                    
                    //리뷰 점수
                    $(".total_score").text( review_score.toFixed(1) );
                    
                    var total_rating = "";
                    
                    if( Math.ceil(review_score) > 0 ){
                    	//$(".total_rating").remove();
                        for( let k = 0 ;  k < Math.ceil(review_score); k++ ){
                        	total_rating = total_rating+"<span class='ico-star star-on'></span>";	
                        }
                        
                        for( let l = 0 ;  l < 5-Math.ceil(review_score); l++ ){
                        	total_rating = total_rating+"<span class='ico-star star-off'></span>";	
                        }
                        
                    }
                    
                    //레이팅 초기화
                    $(".total_rating").empty();
                    $(".total_rating").append(total_rating);
                    
                    //리뷰 전체건수
                    $(".total_reviews").text( data.length );
                                        
                } else {                    
                    console.log("판매자 리뷰가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 리뷰 호출오류가 발생했습니다.");
                return;
            }
        });
    }
    
  	//콤마찍기
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }
    
    $(document).ready(function() {
    	
    	SetSellerInfo();
        SetSellerServiceInfo();
        SetPortfolios();
        SetReviews();
    	
    });

</script>
