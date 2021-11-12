
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- 파트너 정보 영역 -->
<div class="cont-sec partner-area">
    <div class="partner-info-top">
        <div class="company-logo"><img src="../images/company-logo.png" id="user_logo" alt=""></div>
        <div class="compnay-info-area">
            <div class="company-name"><i class="ico-grade"></i><span id="user_id">고객</span></div>
            <ul class="info-detail">
                <li><span class="label">담당자</span><span class="info-cont" id="user_name">담당자</span></li>
                <li><span class="label">평균응답시간</span><span class="info-cont" id="user_avg_time"> 15분</span></li>
                <li><span class="label">연락처</span><span class="info-cont" id="user_phone">010 1234 5678</span></li>
            </ul>
        </div>
    </div><!-- //partner-info-top -->
    <div class="partner-info-top">
        <div class="company-logo"><img src="../images/company-logo.png" id="seller_logo" alt=""></div>
        <div class="compnay-info-area">
            <div class="company-name"><i class="ico-grade"></i><span id="seller_id">rem789</span></div>
            <ul class="info-detail">
                <li><span class="label">담당자</span><span class="info-cont" id="seller_name">담당자</span></li>
                <li><span class="label">평균응답시간</span><span class="info-cont" id="seller_avg_time"> 15분</span></li>
                <li><span class="label">연락처</span><span class="info-cont" id="seller_phone">010 1234 5678</span></li>
            </ul>
        </div>
    </div><!-- //partner-info-top -->
    <div class="partner-detail-cont">
        <div class="detail-tab-area">
            <button type="button" class="btn-tab active-tab" data-tab="partner-cont1">파트너 정보</button>
            <button type="button" class="btn-tab" data-tab="partner-cont2">고객정보</button>
        </div>
        <div class="detail-cont-area">
            <!-- 파트너 정보 -->
            <div class="detail-cont active-cont" id="partner-cont1">
                <dl class="detail-box">
                    <dt class="detail-tt">기업소개</dt>
                    <dd class="detail-in-box">
                        <p class="in-txt" id="seller_intro">기업소개</p>
                    </dd>
                </dl>
                <dl class="detail-box">
                    <dt class="detail-tt">기본정보</dt>
                    <dd class="detail-in-box">
                        <ul id="seller_info_list" class="info-list">
                            <li>
                                <i class="ico-info1"></i>
                                <p>직원수 : 0명</p>
                            </li>
                            <li>
                                <i class="ico-info2"></i>
                                <p>설립일 : 2018년 12월 30일</p>
                            </li>
                            <li>
                                <i class="ico-info3"></i>
                                <p>최근 매출액 : 0원</p>
                            </li>
                            <li>
                                <i class="ico-info4"></i>
                                <p>위치 : 서울시 강남구 도곡동</p>
                            </li>
                            <li>
                                <i class="ico-info5"></i>
                                <p>연락 가능 시간 : 09:00~ 18:00</p>
                            </li>
                            <li>
                                <i class="ico-info6"></i>
                                <p>평균 응답 시간 : 30분</p>
                            </li>
                        </ul>
                    </dd>
                </dl>
                <dl class="detail-box">
                    <dt class="detail-tt">제공서비스 <!-- <a href="#" class="btn-more">전체 서비스<i class="ico-more"></i></a> --></dt>
                    <dd class="detail-in-box">
                        <div class="service-list">
                        </div>
                    </dd>
                </dl>
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
                            <strong class="rating-num total_score">5.8</strong>
                            <div class="rating-star total_rating">
                                <span class="ico-star star-on"></span>
                                <span class="ico-star star-on"></span>
                                <span class="ico-star star-on"></span>
                                <span class="ico-star star-off"></span>
                                <span class="ico-star star-off"></span>
                            </div>
                            <div class="total-count">총 리뷰<span class="t-blue total_reviews">3건</span></div>
                        </div><!-- //review-total -->
                        <div class="review-list">
                        </div><!-- //review-list -->
                    </dd>
                </dl>
            </div>
            <!-- //파트너 정보 -->
            <!-- 고객정보 -->
            <div class="detail-cont" id="partner-cont2">
                <dl class="detail-box">
                    <dt class="detail-tt">기업소개</dt>
                    <dd class="detail-in-box">
                        <p class="in-txt" id="user_intro">기업소개</p>
                    </dd>
                </dl>
                <dl class="detail-box">
                    <dt class="detail-tt">기본정보</dt>
                    <dd class="detail-in-box">
                        <ul id="user_info_list" class="info-list">
                            <!-- <li>
                                <i class="ico-info1"></i>
                                <p>직원수 : 32명</p>
                            </li>
                            <li>
                                <i class="ico-info2"></i>
                                <p>설립일 : 2018년 12월 30일</p>
                            </li>
                            <li>
                                <i class="ico-info3"></i>
                                <p>최근 매출액 : 8423,245,115원</p>
                            </li>
                            <li>
                                <i class="ico-info4"></i>
                                <p>위치 : 서울시 강남구 도곡동</p>
                            </li> -->
                            <li>
                                <i class="ico-info5"></i>
                                <p>연락 가능 시간 : 09:00~ 18:00</p>
                            </li>
                            <li>
                                <i class="ico-info6"></i>
                                <p>평균 응답 시간 : 30분</p>
                            </li>
                        </ul>
                    </dd>
                </dl>
            </div>
            <!-- //고객정보 -->
        </div>
    </div>

</div>
<!-- //파트너 정보 영역 -->

<script>

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

    function SetUserInfo() {
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/user/user_info.php",
            data: {AppID:"user_info", Passwd:"user_info_pwd", LoginID:$("#customerId").val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    $("#user_logo").attr("src", res.data[0].logo);
                    $("#user_name").html(res.data[0].name);
                    $("#user_avg_time").html(res.data[0].avg_time);
                    $("#user_phone").html(res.data[0].phone);
                    $("#user_name").html(res.data[0].name);
                    $("#user_id").html(res.data[0].company_name);
                   
                    
                    /* var infoHtml = '<li>' +
                        '               <i className="ico-info1"></i>' +
                        '               <p>직원수 : '+res.data[0].C_EMP_CNT+'명</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i className="ico-info2"></i>' +
                        '               <p>설립일 : '+res.data[0].C_FOUND_DATE+'</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i className="ico-info3"></i>' +
                        '               <p>최근 매출액 : '+comma(res.data[0].C_CUR_SALES)+'원</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i className="ico-info4"></i>' +
                        '               <p>위치 : '+res.data[0].C_address+'</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i className="ico-info5"></i>' +
                        '               <p>연락 가능 시간 : '+res.data[0].res_time+'</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i className="ico-info6"></i>' +
                        '               <p>평균 응답 시간 : '+res.data[0].avg_time+'</p>' +
                        '           </li>';
                    $("#user_info_list").html(infoHtml); */
                } else {
                    console.log("회원 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("회원정보 호출 오류가 발생했습니다.");
                return;
            }
        });
    }

    SetUserInfo();

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
                    $("#seller_logo").attr("src", res.data[0].logo);
                    $("#seller_id").html(res.data[0].company_name);
                    $("#seller_name").html(res.data[0].name);
                    $("#seller_avg_time").html(res.data[0].avg_time);

                    $("#seller_intro").html(res.data[0].company_desc);
                    $("#seller_phone").html(res.data[0].mobile);

                    var infoHtml = '<li>' +
                    '               <i className="ico-info1"></i>' +
                    '               <p>직원수 : '+res.data[0].C_EMP_CNT+'명</p>' +
                    '           </li>' +
                    '           <li>' +
                    '               <i className="ico-info2"></i>' +
                    '               <p>설립일 : '+res.data[0].C_FOUND_DATE+'</p>' +
                    '           </li>' +
                    '           <li>' +
                    '               <i className="ico-info3"></i>' +
                    '               <p>최근 매출액 : '+comma(res.data[0].C_CUR_SALES)+'천원</p>' +
                    '           </li>' +
                    '           <li>' +
                    '               <i className="ico-info4"></i>' +
                    '               <p>위치 : '+res.data[0].C_address+'</p>' +
                    '           </li>' +
                    '           <li>' +
                    '               <i className="ico-info5"></i>' +
                    '               <p>연락 가능 시간 : '+res.data[0].res_time+'</p>' +
                    '           </li>' +
                    '           <li>' +
                    '               <i className="ico-info6"></i>' +
                    '               <p>평균 응답 시간 : '+res.data[0].avg_time+'</p>' +
                    '           </li>';
                    $("#seller_info_list").html(infoHtml);

                    
                } else {
                    console.log("판매자 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 장보 호출 오류가 발생했습니다.");
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
                        htmlText += "<div class=\"service-item\">";
                        htmlText += "<a href=\"#\">";
                        htmlText += "<div class=\"service-thumb\"><img src='"+data[i].logo+"' alt=''></div>";
                        htmlText += "<div class=\"service-info\">";
                        htmlText += "<dl>";
                        htmlText += "<dt>"+data[i].option_desc.split(';')[0]+"</dt>";
                        htmlText += "<dd>"+data[i].option_price.split(';')[0]+" 원</dd>";
                        htmlText += "</dl>";
                        htmlText += "</div>";
                        htmlText += "</a>";
                        htmlText += "</div>";
                    }
                    $(".service-list").html(htmlText);
                } else {
                    console.log("판매자 서비스 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 서비스 호출 오류가 발생했습니다.");
                return;
            }
        });
    }
    function SetPortfolios() {
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/seller/seller_portfolios.php",
            data: {AppID:"seller_portfolio", Passwd:"seller_portfolio_pwd", SellerID:$("#sellerId").val()},
            dataType: "json",
            success: function (res) {
                //console.log(res);
                if (res.code == "200") {
                    let htmlText = "";
                    let data = res.data;
                    for(let i = 0 ; i < data.length ; i ++){
                        //htmlText += "<li><button type=\"button\" class=\"btn-pf\" onclick=\"popShow('pf-pop');\"><img src=\""+data[i].pp_logo+"\"></button></li>";
                    }
                    $(".portfolio-list").html(htmlText);
                } else {
                    console.log("판매자 포트폴리오 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 포트폴리오 호출 오류가 발생했습니다.");
                return;
            }
        });
    }
    function SetReviews() {
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
                    console.log("판매자 리뷰 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 리뷰 호출 오류가 발생했습니다.");
                return;
            }
        });
    }
    
    function SetChatInfo() {
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
                    console.log("판매자 리뷰 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("판매자 리뷰 호출 오류가 발생했습니다.");
                return;
            }
        });
    }
    
  	//콤마찍기
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    SetSellerInfo();
    SetSellerServiceInfo();
    SetPortfolios();
    SetReviews();
</script>