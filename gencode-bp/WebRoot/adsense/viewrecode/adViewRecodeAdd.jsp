<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<!-- 
	描述:添加广告访问记录界面。
	作者:zouyaobin
	版权:福建长潮科技
	版本:v1.0 
	日期:2016-03-30 15:30:03
-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>新增广告访问记录</title>
		<%@ include file="/include/css.jsp"%>
	</head>
	<body>
		<div class="form_box">
			<form id="addForm" name="addForm" method="post">
			
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
																								<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.id"/>：
						</th>
						<td width=140px>
															<input id="id" name="id" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.adContentId"/>：
						</th>
						<td width=140px>
															<input id="adContentId" name="adContentId" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.adsenseId"/>：
						</th>
						<td width=140px>
															<input id="adsenseId" name="adsenseId" type="text" 								/>														
							
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.areaCity"/>：
						</th>
						<td width=140px>
															<input id="areaCity" name="areaCity" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.areaProvince"/>：
						</th>
						<td width=140px>
															<input id="areaProvince" name="areaProvince" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.browser"/>：
						</th>
						<td width=140px>
															<input id="browser" name="browser" type="text" 								/>														
							
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.cookie"/>：
						</th>
						<td width=140px>
															<input id="cookie" name="cookie" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.deliveryId"/>：
						</th>
						<td width=140px>
															<input id="deliveryId" name="deliveryId" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.device"/>：
						</th>
						<td width=140px>
															<input id="device" name="device" type="text" 								/>														
							
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.keyWord"/>：
						</th>
						<td width=140px>
															<input id="keyWord" name="keyWord" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.markAddress"/>：
						</th>
						<td width=140px>
															<input id="markAddress" name="markAddress" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.netService"/>：
						</th>
						<td width=140px>
															<input id="netService" name="netService" type="text" 								/>														
							
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.optionSys"/>：
						</th>
						<td width=140px>
															<input id="optionSys" name="optionSys" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.phoneNum"/>：
						</th>
						<td width=140px>
															<input id="phoneNum" name="phoneNum" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.screenRes"/>：
						</th>
						<td width=140px>
															<input id="screenRes" name="screenRes" type="text" 								/>														
							
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewEDate"/>：
						</th>
						<td width=140px>
															<input type="text"  id="viewEDate" name="viewEDate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								 class="Wdate"/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewIp"/>：
						</th>
						<td width=140px>
															<input id="viewIp" name="viewIp" type="text" 								/>														
							
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewResouce"/>：
						</th>
						<td width=140px>
															<input id="viewResouce" name="viewResouce" type="text" 								/>														
							
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewSDate"/>：
						</th>
						<td width=140px>
															<input type="text"  id="viewSDate" name="viewSDate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								 class="Wdate"/>														
							
						</td>
											</tr>	
															</table>			
			
			<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.operation.add"/></span></a>
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
				<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
	</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;


   function dosubmit()
   {
		
		$.ajax({
		   type: "POST",
			url : "addAdViewRecode.page",
			data :formToJson("#addForm"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					var validated = $("#addForm").form('validate');
			      	if (validated){
			      		blockUI();	
			      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			      	}
			      	else
			      	{			      		
			      		return false;
			      	}				 	
				},
			success : function(responseText){
				//去掉遮罩
				unblockUI();
				if(responseText=="success"){
					W.$.dialog.alert("新增记录成功",function(){	
							W.modifyQueryData();
							api.close();
					},api);													
				}else{
					W.$.dialog.alert("新增出错",function(){},api);
				}
			}
		  });
   	 }
function doreset(){
	$("#reset").click();
}
</script>