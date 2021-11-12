
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../../common/common_header.jsp" %>
<div id="header" class="m-hide">
    <div class="flex-wrap">
        <div class="flex-left">
            <h1 class="logo"><a href="javascript:void(0);"><img src="/images/logo.svg" alt=""><span>서비스상품 상담 채팅</span></a></h1>            
        </div>
        <div class="flex-right">
            <a href="https://mall.castingn.com/casting/Buyr/Interaction/?menu=inquery" class="btn-top" target="_blank"><i class="ico-inq"></i><span>1:1문의</span></a>
            <a href="https://mall.castingn.com/casting/Mypage//?mode=purchase1" class="btn-top" target="_blank"><i class="ico-my"></i><span>마이페이지</span></a>
            <div class="login-info-pop">
                <div class="user-name"><span><strong id="user_name">담당자님</strong> 안녕하세요!</span><i class="ico-arrow"></i></div>
                <div class="user-box">
                    <div class="user-id">
                        <p class="id-txt">ID : ${customerId}</p>
                        <button type="button" class="btn-logout" onclick="location.href='/casting/Mypage/logout.php' ">로그아웃</button>
                    </div>
                    <dl class="user-log">
                        <dt>마지막 로그인</dt>
                        <dd id="last_login">로그인 시간</dd>
                    </dl>
                </div>
                <div class="user-point">
                    <dl class="type1">
                        <dt>보유쿠폰</dt>
                        <dd><strong>0</strong>장</dd>
                    </dl>
                    <dl class="type2">
                        <dt>사용가능 POINT</dt>
                        <dd><strong>0</strong>P</dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
</div><!-- //header -->

<script type="text/javascript">

    function SetUserInfo() {
        $.ajax({
            type: 'POST',
            url: serverUrl+"/casting/rest/chat/user/user_info.php",
            data: {AppID:'user_info', Passwd:'user_info_pwd', LoginID:$('#customerId').val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    $("#user_name, .chat-name").html(res.data[0].name + "님" );
                    $("#last_login").html(res.data[0].last_login);
                   // $("#logo, .chat-icon").attr("src", res.data[0].logo);
                    $("#uName").val(res.data[0].name);
                } else {
                    console.log("회원 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("회원 정보 호출 오류가 발생했습니다.");
                return;
            }
        });
    }

    SetUserInfo();
</script>
