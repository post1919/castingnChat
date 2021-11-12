<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!-- 간편견적서 : 0810 -->
<div class="pop" id="estimate-pop">
	<div class="pop-flex-wrap">
		<div class="pop-wrap">
			<button type="button" class="btn-pop-close">닫기</button>
			<div class="pop-container estimate-container">
				<form id="reqeust_form">
					<div class="pop-title"><strong>간편견적서</strong></div>
					<div class="pop-content estimate-content">
						<div class="estimate-scroll-area">
							<!-- S : 반복 -->
							<div class="estimate-set-box">
								<div class="estimate-sec">
									<div class="sec-tt">
										<i class="ico-sec"></i>
										<strong>서비스 선택 </strong>
									</div>
									<div class="sec-cont">
										<div class="service-slide service-slide-wrap1">
											<div class="service-slide-container1 swiper-container">
												<div class="swiper-wrapper" id="serviceItemList">
													<!-- 서비스 목록 -->
												</div>
												<input type="hidden" id="estimate_contents">
											</div>
											<div class="swiper-pagination"></div>
											<div class="swiper-button-next"></div>
											<div class="swiper-button-prev"></div>
										</div>
									</div>
								</div>
								<div class="estimate-sec">
									<div class="sec-tt">
										<i class="ico-sec"></i>
										<strong>제공 서비스 내용</strong>
									</div>
									<div class="sec-cont">
										<div class="form-text">
											<textarea name="pa_content" id="pa_content" placeholder="-  문서,이미지, 기획서, 등 참고자료 및 기 진행된 자료 있다면 첨부해주세요. 최대(100MB, 1개 첨부 가능)&#13;&#10;-  첨부문서는 내규에 의거하여 비밀유지협약이 자동적용됩니다. "></textarea>
										</div>
										<div class="form-file-area">
											<div class="file-area"><input type="file" id="addEstimateFile" name="addEstimateFile" onchange="onSelectEstimateFile(this);"><label for="addEstimateFile"><i class="ico-file"></i><span>파일첨부파일</span></label></div>
											<div class="file-disc">최대 100mb, 1개 문서 파일 첨부 가능 <strong class="txt-count">0/1000자</strong></div>
										</div>
										<div class="estimate-flex">
											<div class="estimate-box">
												<p class="box-tt">금액</p>
												<div class="form-ip in-text-type"><input type="text" id="pa_price1" name="pa_price1"><span class="in-text">원</span></div>
											</div>
											<div class="estimate-box">
												<p class="box-tt">소비자가</p>
												<div class="form-flex">
													<div class="form-ip in-text-type"><input type="text" id="pa_price2" name="pa_price2"><span class="in-text">원</span></div>
													<div class="form-ip in-text-type"><input type="text" id="pa_discount" name="pa_discount"><span class="in-text">할인</span></div>
												</div>
											</div>
											<div class="estimate-box">
												<p class="box-tt">판매가</p>
												<div class="form-flex">
													<div class="form-ip in-text-type"><input type="text" id="pa_price" name="pa_price"><span class="in-text">원</span></div>
													<div class="form-ip in-text-type"><input type="text" id="pa_margin" name="pa_margin"><span class="in-text">마진</span></div>
												</div>
											</div>
											<div class="estimate-box">
												<p class="box-tt">작업일</p>
												<div class="form-ip in-text-type"><input type="text" id="pa_period" name="pa_period"><span class="in-text">일</span></div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="estimate-btm">
								<button type="button" class="btn-submit dev_send_request"><span>제출하기</span></button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="estimate_cdpk" id="estimate_cdpk" value="">


<script type="text/javascript">

	function GetSellerServices() {
		$.ajax({
			type: 'POST',
			url: serverUrl+"/casting/rest/chat/seller/seller_services.php",
			data: {AppID:'seller_service', Passwd:'seller_service_pwd', SellerID:'${sellerId}'},
			dataType: "json",
			success: function (res) {
				if (res.code == "200") {
					if(res.data.length){
						//console.log(res.data);
						$('#serviceItemList').empty();
						res.data.forEach(function(item, index){

							var html = '';
							var desc = (item.option_desc).split(";");
							var name = (item.option_name).split(";");
							var price = (item.option_price).split(";");

							var checked = "";
							if("${pUid}" == item.p_uid || index == 0) checked = "checked";

							html += '<div class="swiper-slide">';
							html += '	<input type="radio" id="' + item.p_uid + '" name="p_uid" value="' + item.p_uid + '"  ' + checked + '>';
							html += '	<label for="' + item.p_uid + '">';
							html += '		<p class="service-chk-thumb"><img src="' + item.logo + '" alt=""></p>';
							html += '		<p class="service-chk-info">' + item.p_name + ' </p>';
							html += '	</label>';
							html += '</div>';

							$('#serviceItemList').append(html);
						});

						serviceChkSwiper_wrap();
					}
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



	function checkBasicInfo() {

		var pa_uid = $("input:radio[name=p_uid]:checked").val();
		var pa_content = $("textarea[name=pa_content]");
		var pa_price1 = $("input[name=pa_price1]");
		var pa_price2 = $("input[name=pa_price2]");
		var pa_price = $("input[name=pa_price]");
		var pa_period = $("input[name=pa_period]");

		if(pa_uid === ""){
			alert("제공 서비스를 선택해 주세요.");
			pa_uid.focus();
			return false;
		}

		if(pa_content.val().trim() === "") {
			alert("제공 서비스 내용을 입력해 주세요.");
			pa_content.focus();
			return false;
		}

		if(pa_price1.val().trim() === "") {
			alert("공급가를 입력해 주세요.");
			pa_price1.focus();
			return false;
		}

		if(pa_price2.val().trim() === "") {
			alert("소비자가를 입력해 주세요.");
			pa_price2.focus();
			return false;
		}

		if(pa_price.val().trim() === "") {
			alert("판매가를 입력해 주세요.");
			pa_price.focus();
			return false;
		}

		if(pa_period.val().trim() === "") {
			alert("작업예상일을 입력해 주세요.");
			pa_period.focus();
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


	$(function() {
		$(document).on("click", ".dev_send_request", function(e) {
			e.preventDefault();

			if(!checkBasicInfo()) {
				return;
			}

			var form = document.getElementById('reqeust_form');
			var sendData = new FormData(form);

			$.ajax({
				url: serverUrl + "/casting/Buyr/rest/submit_quote.php",
				data: sendData,
				dataType: 'JSON',
				type: 'POST',
				enctype: "multipart/form-data",
				processData: false,
				contentType: false,
				success: function(result) {
					var status = result.status;
					var jandi = result.jandi_map;
					if(status == "success") {
						// 의뢰 등록 성공
						// 로컬 스토리지에 저장된 값을 지워준다.
						//removeAllSaved();
						/* if(jandi) {
                            var jandiParams = {
                                url: jandi.url,
                                data: {
                                    body: "[업무마켓] 공급사 견적서 제출 완료",
                                    connectColor: "#FAC11B",
                                    connectInfo: [{
                                        title: jandi.title,
                                        description: jandi.description
                                    }]
                                }
                            };

                            // 실제에서는 잔디 발송을 해야 하니까 주석을 풀도록 합니다.
                            jandi_ajax(jandiParams, function jandiResult(data, status, xhr) {});
                        } */

						sendEstimateToCustomer();
						alert("등록하신 견적이 제출 되었습니다.");
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
					console.log(xhr);
					alert("의뢰 등록이 실패했습니다.");
				}
			});
		});
	});



</script>
<!-- END modal-->
