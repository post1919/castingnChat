function onClearSearch(){
    $('#search_text').val('');
    document.getElementById("btn_search").style.display = "flex";
    document.getElementById("btn_close").style.display = "none";
    $(".chat-list").html('');
}
function onSearch(){
    if($('#search_text').val() == ''){
        onClearSearch();
        return ;
    }
    document.getElementById("btn_search").style.display = "none";
    document.getElementById("btn_close").style.display = "flex";

    $.ajax({
        type: 'POST',
        url: "/search",
        data:{query: $("#search_text").val(), uPk: $("#uPk").val()},
        dataType: "json",
        success: function (res) {
        	
            convertChatListHtml(res.data);
        }
    });
}
function convertChatListHtml(data){
		
    let htmlText = "";
    if( data.length > 0 ){
    	for(let item of data){ 
            htmlText += '<div class="chat-item" onClick="setChangeChatRoom(this, \''+item.CM_PK+'\');">'; // 1001 disabled / active-item
            htmlText += '<div class="chat-thumb"><img src="'+stringNvltoStr(item.YOUR_PICTURE, "")+'" alt=""></div>';
            htmlText += '<div class="chat-info-area">';
            htmlText += '<dl>';
            htmlText += '<dt>';
            //htmlText += '<span class="id-txt" >판매처명 : '+item.CM_PK+'</span>';
            
            if(item.U_TYPE == "1")
            	htmlText += '<span class="id-txt" >'+stringNvltoStr(item.YOUR_NAME, "")+'</span>'; // 1001 판매처명
            else if(item.U_TYPE == "4")
            	htmlText += '<span class="id-txt" >'+stringNvltoStr(item.YOUR_NAME, "")+'</span>'; // 1001 고객명
                    
            //htmlText += '<span class="id-txt" onclick="location.href=\''+location.pathname+'?uPk='+item.CM_PU_PK+'&stndCd='+item.R_STND_CD+'&cmChatType='+item.CM_CHAT_TYPE+'&svcCd='+item.CU_SVC_CD+'&pUpk='+item.CM_P_PK+'&P_uid='+item.R_STND_CD+'&cmPk='+item.CM_PK+'\'">아이디</span>';        
            htmlText += '<span class="count">99</span>';
            htmlText += '<span class="date">'+item.CD_INDATE+'</span>';
            htmlText += '</dt>';
            let content = "";
            if(item.CD_CHAT_STAT == "CHAT")
                htmlText += '<dd>'+item.CD_CONTENT+'</dd>';
            else if(item.CD_CHAT_STAT == "FILE")
                htmlText += '<dd>'+item.CD_ORG_FILE_NM+'</dd>';
            htmlText += '</dl>';
            htmlText += '</div>';
            htmlText += '<button type="button" class="btn-pick ' + (item.CD_LIKE_CNT > 0 ? 'active-pick' : '') + '" onclick="onLikeChat(this, '+item.CD_PK+')">찜하기</button>';            
            htmlText += '<button type="button" class="btn-del">닫기</button>';            
            htmlText += '</div><!-- //chat-item -->';

            
            
        }
    } else {
    	 
    }
     
    $(".chat-list").html(htmlText);
}
//즐겨찾기 표시
function onLikeChat(e, cPk){
    $(e).toggleClass('active-pick');

    $.ajax({
        type: 'POST',
        url: "/likeChat",
        data:{cPk: cPk, uPk: $("#uPk").val(), isLike: $(e)[0].classList.contains("active-pick")},
        dataType: "json",
        success: function (res) {
        }
    });
}

function getChatListByMessageType(type){
	
    $.ajax({
        type: 'POST',
        url: "/getChatListByMessageType",
        data:{
            userType:$('#userType').val(),
            uPk:$('#uPk').val(),
            svcCd:$('#svcCd').val(),
            stndCd:$('#stndCd').val(),
            cmChatType:$('#cmChatType').val(),
            uPkArr:$('#uPkArr').val(),
            pUid:$('#pUid').val(),
            uName:$('#uName').val(),
            type: type
        },
        dataType: "json",
        success: function (res) {
            if(res.data != null) {            	
                convertChatListHtml(res.data);
            }
        }
    });
}

function setChangeChatRoom(self, cmPk){
	
	if(stringNvl(cmPk)) return;
	$('.chat-item').removeClass('active-item');
	$(self).parents('.chat-item').addClass('active-item');
	var userType = $('#userType').val(); 
	$.ajax({
        type: 'POST',
        url: "/setChangeChatRoom",
        data:{
            userType:userType,            
            cmPk:cmPk,
        },
        dataType: "json",
        success: function (res) {
            if(res != null) {
            	
            	console.log(res);
            	
            	$("#sellerUPk").val(res.sellerUPk);
            	$("#svcCd").val(res.svcCd);
            	$("#stndCd").val(res.stndCd);
            	$("#cmChatType").val(res.cmChatType);
            	$("#customerId").val(res.customerId);
            	$("#sellerId").val(res.sellerId);
            	$("#pUid").val(res.pUid);
            	$("#uPkArr").val(res.uPkArr);
            	$("#uName").val(res.uName);
            	$("#cmPk").val(res.cmPk);
            	$("#isRoomCreate").val("0");            	
            
            	onClose();
            	disableAllButtons(false);
            	$('.screen-list').empty();
            	if(userType == '1'){            
            		SetTopServiceInfo();
                	SetSellerInfo();
                    SetSellerServiceInfo();
                    SetPortfolios();
                    SetReviews();
            		
                	setChatHistList();  
                	fnConnectChat(); // 소켓연결
            	}else if(userType == '4'){
            		
            		SetUserInfo();
            		
            		fnConnectChat(); // 소켓연결
            		onRequestEstimate();
            	}
            	
            	$('#container').addClass('chat-list-hide');
            }
        }
    });
}

