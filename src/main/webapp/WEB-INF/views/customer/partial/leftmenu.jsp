
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="left-wrap">
	<!-- 0813 추가 : Mobile 헤더 -->
	<div class="flex-wrap p-hide">
		<button type="button" class="btn-back top-left">뒤로</button>
		<div class="head-tt">메시지</div>
		<button type="button" class="btn-setting top-right">설정</button>
	</div>
	<!-- //0813 추가 : Mobile 헤더 -->
    <button type="button" class="btn-left-toggle m-hide"><span>닫기</span></button>
    <div class="left-title m-hide"><i class="ico-message"></i><span>Message</span></div>
    <div class="left-content">
        <dl class="total-area">
            <dt >전체 </dt>
            <dd><span id="cnt_total">0</span></dd>
        </dl>
        <ul class="message-list">
            <!-- 
            <li>
                <dl>
                    <dt>안읽은 메시지</dt>
                    <dd><a id="cnt_unread" class="chat-link" onclick="getChatListByMessageType('unread')">0</a></dd>
                </dl>
            </li>
             -->
            <li>
                <dl class="no-read">
                    <dt>거래중 메시지</dt>
                    <dd><a id="cnt_transaction" class="chat-link" onclick="getChatListByMessageType('transaction')">0</a></dd>
                </dl>
            </li>
            <li>
                <dl>
                    <dt>완료 메시지</dt>
                    <dd><a id="cnt_exit" class="chat-link" onclick="getChatListByMessageType('exit')">0</a></dd>
                </dl>
            </li>
            <li>
                <dl>
                    <dt><i class="ico-imp"></i>중요 메시지</dt>
                    <dd><a id="cnt_important" class="chat-link" onclick="getChatListByMessageType('important')">0</a></dd>
                </dl>
            </li>
        </ul>
    </div>
    <!-- <button type="button" class="chat-setting btn-setting m-hide">설정</button> --><!-- 0813 : 추가 되었습니다 -->
</div><!-- //left-wrap -->