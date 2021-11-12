<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!--- responsive model -->

<!-- <div class="extended_modals">
	<div class="modal fade in" id="request-modal" tabindex="-1" role="dialog" aria-hidden="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h4 class="modal-title">요구사항</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-2">
							<img src="/images/message-thumb.png" class="request-partner-img">
						</div>
						<div class="col-md-10">
							<div class="row">
								<h5 class="request-parter-ID">ID 전체 노출</h5>
							</div>
							<div class="row">
								<h4>안녕하세요. 마케팅을 확실하게 하는 기업. (주) 그릿입니다. 원하시는 서비스 내용 있으시면, 빠르게 회신드리겠습니다.</h4>
							</div>
						</div>
					</div>
					<div class="row">
						<label style="color: red">*</label><label class="request-parter-ID">요구사항을 가능한 상세하게 알려주시면 더 정확한 서비스를 제공받으실 수 있습니다. </label>
					</div>
					<div class="row">
						<label class="normal-text">예 ) 기한, 예산, 준비상황, 원하는 업무스타일, 미팅횟수 등</label>
					</div>
					<div class="row">
						<textarea id="request_contents" rows="5" placeholder="- 문서,이미지, 기획서, 등 참고자료 및 기 진행된 자료 있다면 첨부해주세요. 최대(100MB, 1개 첨부 가능) "></textarea>
					</div>
					<div class="row">
						<button>파일 첨부하기</button><label class="normal-text" style="margin-left: 20px;">최대 100mb, 1개 문서 파일 첨부 가능</label>
					</div>
					<div class="row" style="margin-top: 20px;">
						<label class="request-parter-ID">잠깐만요!!</label>
					</div>
					<div class="row">
						<label class="normal-text">- 캐스팅엔에서 결제외에 개인적, 외부적으로 결제하신 건에대해서 캐스팅엔이 책임지지 않습니다.   </label>
					</div>
					<div class="row">
						<label class="normal-text">- 개인 계좌입금 및 타 사이트로의 결제요청에 대한 피해는 해당 판매처에서 문의하시기 바랍니다.</label>
					</div>
					<div class="row">
						<label class="normal-text">- 판매자와 연결이 어려울경우 캐스팅엔 채팅을 이용바랍니다. </label>
					</div>
					<div class="row">
						<label class="request-parter-IDt" style="color: #2b70ca">[고객섬김센터 : 1644-2653]</label>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-default" onclick="sendRequstToPartner();">제출하기</button>
					<button type="button" class="btn btn-primary">Save changes</button>
				</div>
			</div>
		</div>
	</div>
</div> -->
 
 
<!-- 요구사항 작성 팝업 2 -->
<div class="pop" id="requirement-pop2">
	<div class="pop-flex-wrap">
		<div class="pop-wrap">
			<form class="dev_request_form" id="reqeust_form">
				<button type="button" class="btn-pop-close" data-dismiss="modal" aria-hidden="true">닫기</button>
				<div class="pop-container requirement-container">
					<div class="pop-title"><strong>요구사항</strong></div>
					<div class="pop-content requirement-content">
						<div class="requirement-company">
							<div class="company-logo" ><img src="../images/company-logo.png" alt="" id="seller_company_logo"></div>
							<div class="company-info">
								<dl>
									<dt>${sellerId}</dt>
									<dd id="seller_company_desc">안녕하세요. 마케팅을 확실하게 하는 기업. (주) 그릿입니다. 원하시는 서비스 내용 있으시면, <br/>빠르게 회신드리겠습니다.</dd>
								</dl>
								<div class="info-other">
									<p class="info-item"><span class="label">최근 접속 일자</span><span class="result" id="seller_last_login">21.06.24</span></p>
									<p class="info-item"><span class="label">평균 응답 시간</span><span class="result" id="seller_avg_time">30분</span></p>
								</div>
							</div>
						</div>
						<div class="requirement-scroll-area">
							<!-- 판매정보 입력 -->							
							<div id="rfpBody"></div>
							<div id="rfpFooter">
								<div class="requirement-sec">
								 	<div class="sec-tt">
								 		<i class="ico-sec"></i>
								 		<strong>요구사항을 가능한 상세하게 알려주시면 더 정확한 서비스를 제공받으실 수 있습니다.</strong>
								 	</div>
								 	<p class="sec-disc">예 ) 기한, 예산, 준비상황, 원하는 업무스타일, 미팅횟수 등</p>
								 	<div class="sec-cont">
								 		<div class="form-text">
								 			<textarea name="pr_description" id="pr_description" placeholder="-  문서,이미지, 기획서, 등 참고자료 및 기 진행된 자료 있다면 첨부해주세요. 최대(100MB, 1개 첨부 가능)&#13;&#10;-  첨부문서는 내규에 의거하여 비밀유지협약이 자동적용됩니다. "></textarea>
								 		</div>
								 		<div class="form-file-area">
								 			<div class="file-area"><input type="file" id="file01" name="pr_file"><label for="file01"><i class="ico-file"></i><span>파일첨부파일</span></label></div>
								 			<div class="file-disc">최대 100mb, 1개 문서 파일 첨부 가능</div>
								 		</div>
								 	</div>
								 </div>
								 <div class="requirement-info">
								 	<dl>
								 		<dt><i class="ico-info-red"></i><strong>잠깐만요!</strong></dt>
								 		<dd>
								 			<ul>
								 				<li>캐스팅엔에서 결제외에 개인적, 외부적으로 결제하신 건에대해서 캐스팅엔이 책임지지 않습니다.</li>
								 				<li>개인 계좌입금 및 타 사이트로의 결제요청에 대한 피해는 해당 판매처에서 문의하시기 바랍니다.</li>
								 				<li>판매자와 연결이 어려울경우 캐스팅엔 채팅을 이용바랍니다.</li>
								 			</ul>
								 		</dd>
								 	</dl>
								 </div>
								 <div class="requirement-btm">
								 	<p class="btm-cs">고객섬김센터  1644-2653</p>
								 	<button type="button" class="btn-submit" id="requestBtn" ><span>제출하기</span></button>
								 </div>
							</div>
						</div>
					</div>
					<input type="hidden" id="request_u_pk" name="u_pk" value="${userId}" />
	                <input type="hidden" id="request_items" name="request_items" />	                
	                <input type="hidden" id="request_category" name="pr_category" value="${pUid}" />
	                
	                
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
					<input type="hidden" name="cmPk" id="cmPk1" value="${cmPk}">
	                
				</div>
			</form>
		</div>
	</div>
</div>

<!-- END modal-->
<script type="text/javascript">

function GetRfpInfo() {
    $.ajax({
        type: 'POST',
        url: serverUrl+"/casting/rest/chat/rfp/rfp_info.php",
        data: {AppID:'rfp_info', Passwd:'rfp_info_pwd', P_uid:'${pUid}'},
        dataType: "json",
        success: function (res) {
            if (res.code == "200") {   
            	
            	//console.log(res.data.length)
            	
                if(res.data.length){
                	
                	//$('#requirement-pop2 .requirement-scroll-area').empty();
                	$('#rfpBody').empty();                	
                	var mainHtml = '';
                	res.data.forEach(function(item, index){
                		
                		var q_ri_pk = item.RI_PK;
            			var q_ri_type = item.RI_TYPE;
            			var q_ri_necessity = item.RI_NECESSITY;
            			var title_nec_class = "";                			
            							
            			if(q_ri_necessity == 1) {
                            title_nec_class = "imp-type";
                        }
            			
                      	mainHtml = '<div class="requirement-sec">';
                      	mainHtml += '	<div class="sec-tt dev_rfp_titles '+title_nec_class+'">'
									+ '		<i class="ico-sec "></i>';												
						mainHtml += '		<strong>'+item.RI_Q_DETAIL+'</strong>';								
						
						if(q_ri_type == "CHECKBOX") {
							mainHtml += " <span class=\"disc\">(복수 선택 가능)</span>";
                        }	
						mainHtml += '</div>';
                        	
						var area_class = "check-wrap";
                        if(q_ri_type == "RADIO") {
                            area_class = "radio-wrap";
                        }
                        
                        if(q_ri_type == "TEXT" && item.RI_CALENDAR == 1) {
                        	area_class = "form-text";
                        }
                        
                        mainHtml += '<div class="sec-cont dev_rfp_items" data-type="'+q_ri_type+'" data-rtpk="'+q_ri_pk+'" data-necessity="'+q_ri_necessity+'">';
            			
            			
				 		var rfp_a_idx = 0;
                        var check_idx = 0;
                        var radio_idx = 0;
                        var text_idx = 0;
                        var textarea_idx = 0;
                        var listHtml = '';
                        var direct_item_id = '';
                		item.list2.forEach(function(item2, index2){
                			
							if (q_ri_type == "CHECKBOX") {
                				
                                if( check_idx == 0 ){
                                	listHtml += "<div class='check-wrap'>";
                                }

                                var a_ri_direct = item2.RI_NECESSITY;
                                listHtml += "<div class='chk-item'>";
                                listHtml += "<input type='checkbox' class='dev_rfp_checkbox' value='" + item2.RI_PK + "' id='checkbox_" + item.RI_PK + "_" + item2.RI_PK + "' name='checkbox_" + item.RI_PK + "' data-direct='" + a_ri_direct + "' data-answer='" + item2.RI_NAME + "'>";
                                listHtml += "<label for='checkbox_" + item.RI_PK + "_" + item2.RI_PK + "'>" + item2.RI_NAME + "</label>";
                                listHtml += "</div>";
                               
                                if (a_ri_direct == 1) {
                                    direct_item_id = "checkbox_" + item.RI_PK + "_" + item2.RI_PK + "_text";
                                }

                                check_idx++;
                                if( check_idx == index2 ){
                                	listHtml += "</div>";
                                }								
                            }else if (q_ri_type == "RADIO") {
                            	
                            	var a_ri_direct = item2.RI_NECESSITY;
                                var checked = "";
                                if (rfp_a_idx == 0) {
                                    checked = ' checked="checked" ';
                                }

                                if( radio_idx == 0 ){
                                	listHtml += "<div class='radio-wrap'>";
                                }

                                listHtml += "<div class='radio-item'>";
                                listHtml += "<input type='radio' class='dev_rfp_radio' value='" + item2.RI_PK + "' id='radio_" + item.RI_PK + "_" + item2.RI_PK + "' name='radio_" + item.RI_PK + "' data-direct='" + a_ri_direct + "' data-answer='" + item2.RI_NAME + "' " + checked + ">";
                                listHtml += "<label for='radio_" + item.RI_PK + "_" + item2.RI_PK + "'>" + item2.RI_NAME + "</label>";
                                listHtml += "</div>";
                                
                                if (a_ri_direct == 1) {
                                    direct_item_id = "radio_" + item.RI_PK + "_" + item2.RI_PK + "_text";
                                }

                                radio_idx++;
                                if( radio_idx == index2 ){
                                	listHtml += "</div>";
                                }

                            }else if (q_ri_type == "TEXT") {
                                var a_ri_calendar = item.RI_CALENDAR;
                                var a_ri_placeholder = item.RI_PLACEHOLDER;
                                var calendar = "";
                                var readonly = "";
                                if (a_ri_calendar == 1) {
                                    calendar = " datepicker";
                                    readonly = ' readonly="readonly" ';
                                }

                                listHtml += "<div class='form-text2'>";
                                listHtml += "    <input type='text' id='text_" + item.RI_PK + "_" + item2.RI_PK + "' name='text_" + item.RI_PK + "' placeholder='" + a_ri_placeholder + "' class='dev_test_class dev_rfp_text " + calendar + "' data-ripk='" + item2.RI_PK + "' " + readonly + " />";
                                listHtml += "</div>";
                                
                            }else if(q_ri_type == "TEXTAREA") {
                                var a_ri_placeholder = item.RI_PLACEHOLDER;
                                listHtml += "<div class='form-text'>";
                                listHtml += "    <textarea id='textarea_"+item.RI_PK+"_"+item2.RI_PK+"' name='textarea_"+item.RI_PK+"' placeholder='"+a_ri_placeholder+"' class='dev_rfp_textarea' data-ripk='"+item2.RI_PK+"'></textarea>";
                                listHtml += "</div>";
                                
                            }
                		});
                		
                		if(direct_item_id) {
                			listHtml += "<div class='form-ip-area'>";
                			listHtml += "<input type=\"text\" id=\"" + direct_item_id + "\" class=\"form-ip dev_etc_text\" placeholder=\"기타 항목 아이템을 작성해주세요.\">";
                			listHtml += "</div>";
                        }
                		
                		mainHtml += listHtml;
                		mainHtml += '</div></div></div>';
                		//$('#requirement-pop2 .requirement-scroll-area').append(mainHtml);	
                		$('#rfpBody').append(mainHtml);	
                	});                	
                	
            		//$('#requirement-pop2 .requirement-scroll-area').append(GetRfpFooter());
                	
                	
                }
            } else {
                console.log("요구사항 정보가 없습니다.");
                return;
            }
        },
        error: function (data, status, e) {
            alert("요구사항 정보 호출 오류가 발생했습니다.");
            return;
        }
    });
}

function GetRfpFooter(){
	var foo = '<div class="requirement-sec">'
		+ '	<div class="sec-tt">'
		+ '		<i class="ico-sec"></i>'
		+ '		<strong>요구사항을 가능한 상세하게 알려주시면 더 정확한 서비스를 제공받으실 수 있습니다.</strong>'
		+ '	</div>'
		+ '	<p class="sec-disc">예 ) 기한, 예산, 준비상황, 원하는 업무스타일, 미팅횟수 등</p>'
		+ '	<div class="sec-cont">'
		+ '		<div class="form-text">'
		+ '			<textarea name="pr_description" id="pr_description" placeholder="-  문서,이미지, 기획서, 등 참고자료 및 기 진행된 자료 있다면 첨부해주세요. 최대(100MB, 1개 첨부 가능)&#13;&#10;-  첨부문서는 내규에 의거하여 비밀유지협약이 자동적용됩니다. "></textarea>'
		+ '		</div>'
		+ '		<div class="form-file-area">'
		+ '			<div class="file-area"><input type="file" id="pr_file" name="pr_file" onchange="onSelectRequestFile(this);"><label for="pr_file"><i class="ico-file"></i><span>파일첨부파일</span></label></div>'
		+ '			<div class="file-disc">최대 100mb, 1개 문서 파일 첨부 가능</div>'
		+ '		</div>'
		+ '	</div>'
		+ '</div>'
		+ '<div class="requirement-info">'
		+ '	<dl>'
		+ '		<dt><i class="ico-info-red"></i><strong>잠깐만요!</strong></dt>'
		+ '		<dd>'
		+ '			<ul>'
		+ '				<li>캐스팅엔에서 결제외에 개인적, 외부적으로 결제하신 건에대해서 캐스팅엔이 책임지지 않습니다.</li>'
		+ '				<li>개인 계좌입금 및 타 사이트로의 결제요청에 대한 피해는 해당 판매처에서 문의하시기 바랍니다.</li>'
		+ '				<li>판매자와 연결이 어려울경우 캐스팅엔 채팅을 이용바랍니다.</li>'
		+ '			</ul>'
		+ '		</dd>'
		+ '	</dl>'
		+ '</div>'
		+ '<div class="requirement-btm">'
		+ '	<p class="btm-cs">고객섬김센터  1644-2653</p>'
		+ '	<button type="submit" class="btn-submit" ><span>제출하기</span></button>'
		+ '</div>';
		
	return foo;
}

//$('#reqeust_form').submit(function(e){
$(document).on("click","#requestBtn",function(e){		
	
	e.preventDefault();
    if(!makeRfpItems()) {
        return;
    }
    
    if(!checkBasicInfo()) {
        return;
    }
    
    var form = document.getElementById('reqeust_form');
    var sendData = new FormData(form);
    if($("#request_items").val() !== '') {
        sendData.append('request_items', $("#request_items").val());
    }
    
    $.ajax({
        url: serverUrl + "/casting/Buyr/rest/request_rfi.php",
        data: sendData,
        dataType: 'JSON',
        type: 'POST',
        enctype: "multipart/form-data",
        processData: false,
        contentType: false,
        success: function(result) {
            /* console.log("result:"+result);
            console.log("result_str:"+result.rfp_str);
            console.log("result_items:"+result.rfp_items);
            console.log("step1:"+result.step1);
            console.log("step2:"+result.step2);
            console.log("step3:"+result.step3);
            console.log("step4:"+result.step4);
            console.log("step11:"+result.step11);
            console.log("step20:"+result.step20);
 */
            var status = result.status;
            var jandi = result.jandi_map;
            var prPk =  result.prPk;
            if(status == "success") {
                // 의뢰 등록 성공
                // 로컬 스토리지에 저장된 값을 지워준다.
                //removeAllSaved();
                if(jandi) {
                    var jandiParams = {
                        url: jandi.url,
                        data: {
                            body: "[업무마켓] 고객 요구사항이 작성완료",
                            connectColor: "#FAC11B",
                            connectInfo: [{
                                title: jandi.title,
                                description: jandi.description
                            }]
                        }
                    };

                    // 실제에서는 잔디 발송을 해야 하니까 주석을 풀도록 합니다.
                    //jandi_ajax(jandiParams, function jandiResult(data, status, xhr) {});                    
                    
                }
                $("#prPk").val(prPk);//prPK 세팅
                
                sendRequstToPartner();      
                
                alert("요구사항 작성이 완료되었습니다.");
                // TODO 여기
                //cardChange(3);
            } else {
                // 의뢰 등록 실패
                var msg = result.msg;
                if(msg) {
                    if(msg === "invalid file type") {
                        var validType = result.valid_types;
                        alert("첨부할 수 없는 파일 형식입니다.\n아래 형식의 파일 중에서 첨부해주세요.\n\n" + validType);
                    } else if(msg === "too large file") {
                        var maxSize = result.max_file_size;
                        alert("파일 용량이 너무 큽니다.\n첨부 가능한 최대 파일 용량은 " + maxSize + "메가바이트입니다.");
                    } else {
                        alert("의뢰 등록이 실패했습니다.");
                    }
                }
            }
        },
        error: function(xhr) {            
            alert("의뢰 등록이 실패했습니다.");
        }
    });
});

var necVoidList = [];
var checkedDirectVoidList = [];
function makeRfpItems() {
    $("#request_items").val('');
    var qnaList = [];
    necVoidList = [];
    checkedDirectVoidList = [];

    var $itemContainers = $(".dev_rfp_items");

    $('.dev_rfp_titles').removeClass('dev_alert');
    if($itemContainers.length < 1) {
        return true;
    }

    for(var i = 0; i < $itemContainers.length; i++) {

        var $item = $itemContainers.eq(i);
        var type = $item.data('type');
        var nec = $item.data('necessity');
        var rtPk = String($item.data('rtpk'));

        var answerList = [];
        if(type === 'CHECKBOX') {
            answerList = findRfpCheckbox($item);
        } else if(type === 'RADIO') {
            answerList = findRfpRadio($item);
        } else if(type === 'TEXT') {
            answerList = findRfpText($item);
        } else if(type === 'TEXTAREA') {
            answerList = findRfpTextarea($item);
        }

        if(nec === 1 && answerList.length < 1) {
            necVoidList.push(rtPk);
        }

        if(answerList && answerList.length > 0) {
            var qnaObj = {};
            qnaObj['rtPk'] = rtPk;
            qnaObj['answers'] = answerList;
            qnaList.push(qnaObj);
        }
    }

    //console.log("necVoidList.length.length:"+necVoidList.length)
    if(necVoidList.length > 0) {
        alert('필수 답변 항목이 비었습니다.');
        for(var i = 0; i < necVoidList.length; i++) {
            var rtPk = necVoidList[i];
            // $("#rfp_item_" + rtPk).addClass('dev_alert');
        }
        return false;
    }

    //console.log("checkedDirectVoidList.length.length:"+checkedDirectVoidList.length)
    if(checkedDirectVoidList.length > 0) {
        alert('직접 입력 답변 항목이 비었습니다.');
        for(var i = 0; i < checkedDirectVoidList.length; i++) {
            var rtPk = checkedDirectVoidList[i];
            // $("#rfp_item_" + rtPk).addClass('dev_alert');
        }
        return false;
    }

    $("#request_items").val(JSON.stringify(qnaList));

    /*var answerList1 = $("#request_items").val();
    for (var key in answerList1) {
        // Javascript의 for-in문을 사용해 key를 뽑아낼 수 있다.
        // key 변수에는 obj가 가진 key가 하나씩 들어온다.

        // 가져온 key를 이용해 value를 추출하자.
        // 두 가지 형식이 존재한다.
        //     1. obj.a
        //     2. obj[a]
        console.log(answerList1[key]);
    }
*/
    return true;
}


function findRfpCheckbox($item) {
    var answerList = [];
    var rtPk = String($item.data('rtpk'));
    var $checkboxes = $item.find('input[name=checkbox_' + rtPk + ']:checked');
    for(var i = 0; i < $checkboxes.length; i++) {
        var $checkbox = $checkboxes.eq(i);
        var riPk = $checkbox.val().trim();
        var answer = $checkbox.data('answer');
        if($checkbox.data('direct') === 1) {
            var $direct = $("#checkbox_" + rtPk + '_' + riPk + '_text');
            var direct = $direct.val().trim();
            answer = 'DIRECT_INSERT;;' + direct;
            if(direct) {
            } else {
                checkedDirectVoidList.push(rtPk);
            }
        }
        var answerObj = {};
        answerObj['riPk'] = String(riPk);
        answerObj['answer'] = answer;
        answerList.push(answerObj);
    }
    return answerList;
}

function findRfpRadio($item) {
    var answerList = [];
    var rtPk = String($item.data('rtpk'));
    var $radio = $item.find('input[name=radio_' + rtPk + ']:checked').eq(0);
    var riPk = $radio.val().trim();
    var answer = $radio.data('answer');
    if($radio.data('direct') === 1) {
        var $direct = $("#radio_" + rtPk + '_' + riPk + '_text');
        var direct = $direct.val().trim();
        answer = 'DIRECT_INSERT;;' + direct;
        if(direct) {
        } else {
            checkedDirectVoidList.push(rtPk);
        }
    }
    var answerObj = {};
    answerObj['riPk'] = String(riPk);
    answerObj['answer'] = answer;
    answerList.push(answerObj);
    return answerList;
}

function findRfpText($item) {
    var answerList = [];
    var rtPk = String($item.data('rtpk'));

    var $text = $item.find('input[name=text_' + rtPk + ']').eq(0);
    var riPk = $text.data('ripk');
    var answer = $text.val().trim();

    //if(answer) {
        var answerObj = {};
        answerObj['riPk'] = String(riPk);
        answerObj['answer'] = answer;
        answerList.push(answerObj);
    //}
    return answerList;
}

function findRfpTextarea($item) {
    var answerList = [];
    var rtPk = String($item.data('rtpk'));
    var $text = $item.find('textarea[name=textarea_' + rtPk + ']').eq(0);
    var riPk = $text.data('ripk');
    //var answer = $text.val().trim();
    var answer = stringNvltoStr($text.val(), "");
    //if(answer) {
        var answerObj = {};
        answerObj['riPk'] = String(riPk);
        answerObj['answer'] = answer;
        answerList.push(answerObj);
    //}
    return answerList;
}

function checkBasicInfo() {

    var content = $("textarea[name=pr_description]");

    if(content.val().trim() === "") {
        alert("상세 요청 내용을 입력해 주세요.");
        // $("#section_basic").addClass("item-imp");
        content.focus();
        return false;
    }

    return true;
}

//잔디처리 공통
function jandi_ajax(param, fn_result){
    $.ajax({
        url         : param.url,
        data        : JSON.stringify(param.data),
        async       : typeof param.async === 'undefined' ? true : param.async,
        type        : "POST",
        dataType    : typeof param.dataType === 'undefined' ? "JSON" : param.dataType,
        contentType : "application/json",
        headers: {
            "Accept"       : "application/vnd.tosslab.jandi-v2+json",
            "Content-Type" : "application/json"
        },
        success: function(data, status, xhr) {
            fn_result(data, status, xhr);
        },
        error: function(xhr, msg) {
            //console.log(xhr);
            console.log(msg);
            // alert("Error - " + msg);
        }
    });
}





</script>

