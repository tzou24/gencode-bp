<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- 
	描述:查看广告访问记录界面。
	作者:zouyaobin
	版权:福建长潮科技
	版本:v1.0 
	日期:2016-03-30 15:30:03
-->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看<pg:message code="cckjcg.viewrecode.viewrecode"/></title>
<%@ include file="/include/css.jsp"%>
</head>
<body>
		<div class="mcontent">
			<div id="searchblock">
				<pg:beaninfo requestKey="adViewRecode">
				<form id="editForm" name="editForm">
				  						<table border="0" cellpadding="0" cellspacing="0"
						class="table4">
																								<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.id"/>：
						</th>
						<td width=140px>
							<input id="id" name="id" type="text" value="<pg:cell colName="id"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.adContentId"/>：
						</th>
						<td width=140px>
							<input id="adContentId" name="adContentId" type="text" value="<pg:cell colName="adContentId"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.adsenseId"/>：
						</th>
						<td width=140px>
							<input id="adsenseId" name="adsenseId" type="text" value="<pg:cell colName="adsenseId"    />" readonly/>
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.areaCity"/>：
						</th>
						<td width=140px>
							<input id="areaCity" name="areaCity" type="text" value="<pg:cell colName="areaCity"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.areaProvince"/>：
						</th>
						<td width=140px>
							<input id="areaProvince" name="areaProvince" type="text" value="<pg:cell colName="areaProvince"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.browser"/>：
						</th>
						<td width=140px>
							<input id="browser" name="browser" type="text" value="<pg:cell colName="browser"    />" readonly/>
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.cookie"/>：
						</th>
						<td width=140px>
							<input id="cookie" name="cookie" type="text" value="<pg:cell colName="cookie"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.deliveryId"/>：
						</th>
						<td width=140px>
							<input id="deliveryId" name="deliveryId" type="text" value="<pg:cell colName="deliveryId"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.device"/>：
						</th>
						<td width=140px>
							<input id="device" name="device" type="text" value="<pg:cell colName="device"    />" readonly/>
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.keyWord"/>：
						</th>
						<td width=140px>
							<input id="keyWord" name="keyWord" type="text" value="<pg:cell colName="keyWord"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.markAddress"/>：
						</th>
						<td width=140px>
							<input id="markAddress" name="markAddress" type="text" value="<pg:cell colName="markAddress"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.netService"/>：
						</th>
						<td width=140px>
							<input id="netService" name="netService" type="text" value="<pg:cell colName="netService"    />" readonly/>
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.optionSys"/>：
						</th>
						<td width=140px>
							<input id="optionSys" name="optionSys" type="text" value="<pg:cell colName="optionSys"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.phoneNum"/>：
						</th>
						<td width=140px>
							<input id="phoneNum" name="phoneNum" type="text" value="<pg:cell colName="phoneNum"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.screenRes"/>：
						</th>
						<td width=140px>
							<input id="screenRes" name="screenRes" type="text" value="<pg:cell colName="screenRes"    />" readonly/>
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewEDate"/>：
						</th>
						<td width=140px>
							<input id="viewEDate" name="viewEDate" type="text" value="<pg:cell colName="viewEDate"    dateformat="yyyy-MM-dd HH:mm:ss"  />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewIp"/>：
						</th>
						<td width=140px>
							<input id="viewIp" name="viewIp" type="text" value="<pg:cell colName="viewIp"    />" readonly/>
						</td>
																							<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewResouce"/>：
						</th>
						<td width=140px>
							<input id="viewResouce" name="viewResouce" type="text" value="<pg:cell colName="viewResouce"    />" readonly/>
						</td>
																								</tr>						<tr>
												<th width=85px >
							<pg:message code="cckjcg.viewrecode.viewSDate"/>：
						</th>
						<td width=140px>
							<input id="viewSDate" name="viewSDate" type="text" value="<pg:cell colName="viewSDate"    dateformat="yyyy-MM-dd HH:mm:ss"  />" readonly/>
						</td>
											</tr>	
																	
					</table>
				
				
				<div class="btnarea" >
					<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span>退出</span></a>
				</div>	
				</form>
				</pg:beaninfo>
			</div>
			
  
  	</div>					
</body>