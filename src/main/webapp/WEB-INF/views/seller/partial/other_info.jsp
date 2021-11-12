
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
    <div class="company-info-top2">
        <div class="company-logo"><img src="../images/company-logo.png" id="user_company_logo" alt=""></div>
        <div class="compnay-info-area">
            <div class="company-name"><i class="ico-grade"></i><span id="user_info_name">rem789</span></div>
            <ul class="info-detail">
                <li><span class="label" style="color: black; font-size: 12px; font-weight: inherit; display: flex; width: 100px;">기업명</span><span class="info-cont" id="user_company_name">캐스팅엔</span></li>
                <li><span class="label" style="color: black; font-size: 12px; font-weight: inherit; display: flex; width: 100px;">평균응답시간</span><span class="info-cont" id="avg_time">15분</span></li>
            </ul>
        </div>
    </div><!-- //company-info-top2 -->

    <div class="company-detail-cont2">
        <p class="company-detail-tt">고객 정보</p>
        <div class="detail-cont-area">
            <!-- 판매처 정보 -->
            <div class="detail-cont">
                <dl class="detail-box">
                    <dt id="user_company_desc" class="detail-tt">기업소개</dt>
                    <dd class="detail-in-box">
                        <p class="in-txt">기업소개</p>
                    </dd>
                </dl>
                <dl class="detail-box">
                    <dt class="detail-tt">기본정보</dt>
                    <dd class="detail-in-box">
                        <ul class="info-list">
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
                            </li>
                            <li>
                                <i class="ico-info5"></i>
                                <p>연락 가능 시간 : 12:00~ 19:00</p>
                            </li>
                            <li>
                                <i class="ico-info6"></i>
                                <p>평균 응답 시간 : 30분</p>
                            </li> -->
                        </ul>
                    </dd>
                </dl>
            </div>
            <!-- //판매처 정보 -->
        </div>
    </div>
</div>
<!-- //컴퍼니 정보 영역 -->
<script type="text/javascript">
    function SetUserInfo() {
        $.ajax({
            type: 'POST',
            url: serverUrl + "/casting/rest/chat/user/user_info.php",
            data: {AppID:"user_info", Passwd:"user_info_pwd", LoginID:$("#customerId").val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    $("#user_company_logo").attr("src", res.data[0].logo);
                    $("#user_info_name, #uName").html(res.data[0].name);
                    $("#user_company_name").html(res.data[0].company_name);
                    $("#avg_time").html(res.data[0].avg_time);
                    $(".info-desc").html(res.data[0].company_desc);
                    $(".info-last-login").html(res.data[0].last_login);
                    $(".info-avg-time").html(res.data[0].avg_time);
                    $("#user_company_desc").html(res.data[0].company_desc);                    
                    
                    $("#user_mobile").val(res.data[0].phone);
                    
                    //고객 사업자 정보 확인후 다시 업데이트
                    var infoHtml = "";

                   /*  var infoHtml = '<li>' +
                        '               <i class="ico-info1"></i>' +
                        '               <p>직원수 : 32명</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info2"></i>' +
                        '               <p>설립일 : 2018년 12월 30일</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info3"></i>' +
                        '               <p>최근 매출액 : 8423,245,115원</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info4"></i>' +
                        '               <p>위치 : 서울시 강남구 도곡동</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info5"></i>' +
                        '               <p>연락 가능 시간 : '+res.data[0].res_time+'</p>' +
                        '           </li>' +
                        '           <li>' +
                        '               <i class="ico-info6"></i>' +
                        '               <p>평균 응답 시간 : '+res.data[0].avg_time+'</p>' +
                        '           </li>';
 */
                    $(".info-list").html(infoHtml);
                } else {
                    console.log("회원 정보 호출에 실패하였습니다.");
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

</script>
