<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<!-- 
	描述:广告访问记录管理主界面。
	作者:zouyaobin
	版权:福建长潮科技
	版本:v1.0 
	日期:2016-03-30 15:30:03
-->
<head>
<title>广告访问记录管理</title>
<%@ include file="/include/css.jsp"%>

</head>
	<body>
		<div class="mcontent">
			
						<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				
				<div class="search_box">
					<form id="queryForm" name="queryForm">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
								
										
																																												<tr>
																							<th><pg:message code="cckjcg.viewrecode.keyWord"/>：</th>
												<td>
																									<input id="keyWord" name="keyWord" type="text"
															value="" class="w120"/>
																								</td>
												
												
																																														<th><pg:message code="cckjcg.viewrecode.markAddress"/>：</th>
												<td>
																									<input id="markAddress" name="markAddress" type="text"
															value="" class="w120"/>
																								</td>
												
												
																																														<th><pg:message code="cckjcg.viewrecode.optionSys"/>：</th>
												<td>
																									<input id="optionSys" name="optionSys" type="text"
															value="" class="w120"/>
																								</td>
												
												
																									<th>
													&nbsp;
												</th>
												<td>
													<a href="javascript:void(0)" class="bt_1" id="queryButton"><span>查询</span>
													</a>
													<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span>
													</a>
													<input type="reset" id="reset" style="display:none"/>
												</td>	
													
											</tr>
																																
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
						</table>
					</form>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
						<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void" class="bt_small" id="addButton"><span>新增</span></a>
				<a href="javascript:void" class="bt_small" id="delBatchButton"><span>批量删除</span></a>
				<a href="javascript:void" class="bt_small" id="exportButton"><span>导出</span></a>
				<input id="excelType" type="radio" name="excelType"   checked  >2003</input>
				<input  type="radio" name="excelType"  >2007</input>
				</div>
				
				<strong>广告访问记录 列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/include/images/wait.gif" />				
			</div>
			<div id="custombackContainer" style="overflow:auto">
			
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">

<!--

$(document).ready(function() {
	bboss.pager.pagerevent = {   
			                            beforeload:function(opt){   
			                                //alert("beforeload containerid:"+opt.containerid);   
			                            },   
			                          afterload:function(opt){   
			                              //alert("afterload containerid:"+opt.containerid);   
	}};   

	 $("#wait").hide();
	 queryList();

    $('#queryButton').click(function() {
    	queryData();
    });
    $('#delBatchButton').click(function() {
    	delBatch();
    });
    $('#exportButton').click(function() {
    	exportExcel();
    });           
    
    $('#addButton').click(function() {  
  	 var url="<%=request.getContextPath()%>/adsense/viewrecode/toAddAdViewRecode.page";
	 	 $.dialog({ id:'iframeNewId', title:'新增&nbsp;广告访问记录',width:740,height:560, content:'url:'+url});   			 		  
    });            
});

function queryList(){	
	$("#custombackContainer").load("queryListInfoAdViewRecodes.page #customContent",function(){})
}

function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
   return ( containSpecial.test(s) );      
}

//查找
function queryData()
{	
	var keyWord=$("#keyWord").val();
	var markAddress=$("#markAddress").val();
	var optionSys=$("#optionSys").val();
		if( containSpecial(keyWord)  || containSpecial(markAddress)  || containSpecial(optionSys)){
		$.dialog.alert('查询字符串含有非法字符集,请检查输入条件！');
		return;
	}
		$("#custombackContainer").load("queryListInfoAdViewRecodes.page #customContent",
	{ keyWord:keyWord ,markAddress:markAddress ,optionSys:optionSys},function(){});


}

function modifyQueryData()
{	
	if($("#querystring").val() != '')
		$("#custombackContainer").load("queryListInfoAdViewRecodes.page?"+$("#querystring").val()+" #customContent");
	else
		$("#custombackContainer").load("queryListInfoAdViewRecodes.page #customContent");
	
}
function editAdViewRecode(id){
	var url="<%=request.getContextPath()%>/adsense/viewrecode/toUpdateAdViewRecode.page?id="+id;
	$.dialog({ title:'修改广告访问记录信息',width:740,height:560, content:'url:'+url,lock: true}); 		
}
function viewAdViewRecode(id){
	var url="<%=request.getContextPath()%>/adsense/viewrecode/getAdViewRecode.page?id="+id;		
	$.dialog({ title:'查看广告访问记录信息',width:740,height:560, content:'url:'+url,lock: true}); 	
}
function doreset(){
	$("#reset").click();
}
function delAdViewRecode(id){		
	$.dialog.confirm('确定要删除选中的广告访问记录记录吗?', function(){ 
		 $.ajax({
	 	 type: "POST",
		url : "<%=request.getContextPath()%>/adsense/viewrecode/deleteAdViewRecode.page",
		data :{"id":id},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
	 		modifyQueryData();
	 		//alert("成功删除记录");
		}	
	  });
	}, function(){ 
		 
	});
		
	  
}
function delBatch(){
	var ids=[];
	var i = 0;
	 $("#tb tr:gt(0)").each(function() {	 
            if ($(this).find("#CK").get(0).checked == true) {
                 ids.push($(this).find("#id").val());                
                i++;
            }
              	 
       });
       if(i==0){
        $.dialog.alert('请选择需要删除的记录！');
        return false;
       }
     $.dialog.confirm('确定要删除选中的'+i+'条广告访问记录记录吗?', function(){
     	$.ajax({
		 	 	type: "POST",
				url : "<%=request.getContextPath()%>/adsense/viewrecode/deleteBatchAdViewRecode.page",
				data :{"ids":ids},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
			 		modifyQueryData();
			 		//alert("成功删除记录");
				}	
			 });
      },function(){
      		
      });         
}

//-->
</script>
