
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../../common/common_header.jsp" %>
<div id="header">
    <div class="flex-wrap">
        <div class="flex-left">
            <h1 class="logo"><a href="javascript:void(0);"><img src="" id="logo" name="admin_logo" alt=""></a></h1>
            <!-- <a href="https://mall.castingn.com/casting/Buyr/" class="btn-quote"><img src="../images/quote-txt.png" alt="맞춤견적"></a> -->
        </div>
        <div class="flex-right">
            <a href="#" class="btn-top"><i class="ico-my"></i><span>마이페이지</span></a>
            <div class="login-info-pop">
                <div class="user-name"><span><strong id="admin_name" name="admin_name">관리자님</strong> 안녕하세요!</span><i class="ico-arrow"></i></div>
                <div class="user-box">
                    <div class="user-id">
                        <p class="id-txt">ID : ${adminId}</p>
                        <button type="button" class="btn-logout" onclick="location.href='/casting/Mypage/logout.php' ">로그아웃</button>
                    </div>
                    <dl class="user-log">
                        <dt>마지막 로그인</dt>
                        <dd id="last_login">2021.07.23 15:03</dd>
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
</div>

<script type="text/javascript">

    function SetAdminInfo() {
        $.ajax({
            type: 'POST',
            url: serverUrl+"/casting/rest/chat/admin/admin_info.php",
            data: {AppID:'admin_info', Passwd:'admin_info_pwd', LoginID:$('#adminId').val()},
            dataType: "json",
            success: function (res) {
                if (res.code == "200") {
                    let adminLogos = document.getElementsByName("admin_logo");
                    for(let i = 0 ; i < adminLogos.length ; i ++){
                        adminLogos[i].src = res.data[0].logo;
                        $("#logo, .chat-icon").attr("src", res.data[0].logo);
                    }

                    $("#logo").attr("src", res.data[0].logo);
                    $("#admin_name, .chat-name").html(res.data[0].name + "님" );
                    $('#uName').val(res.data[0].name);
                    $("#last_login").html(res.data[0].last_login);

                    let adminNames = document.getElementsByName("admin_name");
                    for(let i = 0 ; i < adminNames.length ; i ++){
                        adminNames[i].innerHTML = res.data[0].name + "님";
                    }

                    //$("#admin_avg_time").html(res.data[0].avg_time);
                } else {
                    console.log("관리자 정보가 없습니다.");
                    return;
                }
            },
            error: function (data, status, e) {
                alert("관리자정보 호출 오류가 발생했습니다.");
                return;
            }
        });
    }

    SetAdminInfo();
</script>
