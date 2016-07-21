<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<!-- 
	描述:广告访问记录管理列表界面。
	作者:zouyaobin
	版权:福建长潮科技
	版本:v1.0 
	日期:2016-03-30 15:30:03
-->

<div id="customContent">
<pg:empty actual="${adViewRecodes}" evalbody="true">
    <pg:yes>
	<div class="nodata">
	没有数据
	</div>
	</pg:yes>
	<pg:no>
		
		<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
		<div id="changeColor" >
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
	       <tr>
	            <th align=center><input id="CKA" name="CKA" type="checkbox" 
								onClick="checkAll('CKA','CK')">
																</th>
				<th>序号</th>							
	       		<th><pg:message code="cckjcg.viewrecode.id"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.adContentId"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.adsenseId"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.areaCity"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.areaProvince"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.browser"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.cookie"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.deliveryId"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.device"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.keyWord"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.markAddress"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.netService"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.optionSys"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.phoneNum"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.screenRes"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.viewEDate"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.viewIp"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.viewResouce"/></th>       		
	       		<th><pg:message code="cckjcg.viewrecode.viewSDate"/></th>       		
				<th>操作</th>
	       	</tr>	
	      <pg:list requestKey="adViewRecodes">
	
	   		<tr onDblClick="viewAdViewRecode('<pg:cell colName="id"   />')">
	   		        <td class="td_center">
	                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="id"   />"/>
	                    <input id="id" type="hidden" name="id" value="<pg:cell colName="id"   />"/></td>
	                <td><pg:rowid increament="1" offset="false"/></td>    
	                <td><pg:cell colName="id"  /></td>
	                <td><pg:cell colName="adContentId"  /></td>
	                <td><pg:cell colName="adsenseId"  /></td>
	                <td><pg:cell colName="areaCity"  /></td>
	                <td><pg:cell colName="areaProvince"  /></td>
	                <td><pg:cell colName="browser"  /></td>
	                <td><pg:cell colName="cookie"  /></td>
	                <td><pg:cell colName="deliveryId"  /></td>
	                <td><pg:cell colName="device"  /></td>
	                <td><pg:cell colName="keyWord"  /></td>
	                <td><pg:cell colName="markAddress"  /></td>
	                <td><pg:cell colName="netService"  /></td>
	                <td><pg:cell colName="optionSys"  /></td>
	                <td><pg:cell colName="phoneNum"  /></td>
	                <td><pg:cell colName="screenRes"  /></td>
	                <td><pg:cell colName="viewEDate"  dateformat="yyyy-MM-dd HH:mm:ss"  /></td>
	                <td><pg:cell colName="viewIp"  /></td>
	                <td><pg:cell colName="viewResouce"  /></td>
	                <td><pg:cell colName="viewSDate"  dateformat="yyyy-MM-dd HH:mm:ss"  /></td>
	        		
	                <td class="td_center"><a href="javascript:void(0)" id="readAdViewRecode" onclick="viewAdViewRecode('<pg:cell colName="id"  />')">查看</a><a href="javascript:void(0)" id="editAdViewRecode" onclick="editAdViewRecode('<pg:cell colName="id"  />')">编辑</a><a href="javascript:void(0)" id="delAdViewRecode" onclick="delAdViewRecode('<pg:cell colName="id"  />')">删除</a></td>    
	                 
	        </tr>
		 </pg:list>
	    </table>
	    </div>
		
	</pg:no>
</pg:empty> 
</div>		
