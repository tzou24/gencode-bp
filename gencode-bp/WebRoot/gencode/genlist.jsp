<%@ page language="java" pageEncoding="utf-8" session="false"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<div id="ajax-modal" class="modal  container fade modal-scroll" tabindex="-1">
</div>

<table class="table table-striped table-hover" id="gencodelist">
	<thead>
		<tr>
			<th class="table-checkbox"><input type="checkbox"
				id="group-checkable" data-set="#gencodelist .checkboxes" /></th>

			<th>序号</th>
			<th>代码模块</th>
			<th><pg:message code="gencode.author" /></th>
			<th class="hidden-xs"><pg:message code="gencode.company" /></th>
			<th class="hidden-xs"><pg:message code="gencode.dbname" /></th>
			<th class="hidden-xs"><pg:message code="gencode.tablename" /></th>
			<th class="hidden-xs"><pg:message code="gencode.createtime" /></th>
			<th class="hidden-xs"><pg:message code="gencode.updatetime" /></th>
			<th>操作</th>


		</tr>
	</thead>
	<tbody>
		<pg:list requestKey="gencodes">

			<tr>
				<td><input name="cked" type="checkbox" class="checkboxes"
					value="<pg:cell colName="id" />" /> <input id="id" type="hidden"
					name="id" value="<pg:cell colName="id" />" /></td>
				<td><pg:rowid increament="1" offset="false" /></td>
				<td><pg:cell colName="moudleName" /> <pg:cell
						colName="moudleCNName" /></td>
				<td><pg:cell colName="author" /></td>
				<td><pg:cell colName="company" /></td>

				<td><pg:cell colName="dbname" /></td>
				<td><pg:cell colName="tablename" /></td>
				<td><pg:cell colName="createtime"
						dateformat="yyyy-MM-dd HH:mm:ss" /></td>
				<td><pg:cell colName="updatetime"
						dateformat="yyyy-MM-dd HH:mm:ss"/></td>
				<td><a href="javascript:void(0)"
					onclick="TableAdvanced.regencode('<pg:cell colName="id" />','tablereconfig.page',event)"
					class="btn default btn-xs purple"> <i class="fa fa-edit"></i>
						编辑
				</a> <pg:true colName="fileexist">
						<a href="javascript:void(0)"
							onclick="TableAdvanced.readme('<pg:cell colName="id" />',event)"
							class="btn default btn-xs purple"  data-toggle="modal"> <i class="fa fa-edit"></i>查看部署说明
						</a>

						<a target="_blank"
							href="downcode.page?genid=<pg:cell colName="id" />"
							class="btn default btn-xs purple"> <i class="fa fa-edit"></i>下载
						</a>
						<a target="viewsource"
							href="viewCode.page?genid=<pg:cell colName="id" />"
							class="btn default btn-xs purple"> <i class="fa fa-edit"></i>查看源码
						</a>
					</pg:true> 
					<a href="javascript:void(0)"  data-singleton="true" data-toggle="confirmation" data-original-title="确定要删除吗 ?"  data-placement="top" 
					data-btn-ok-label="确定" data-btn-ok-icon="icon-like" data-btn-ok-class="btn-xs btn-success" data-btn-cancel-label="取消" data-btn-cancel-icon="icon-close" data-btn-cancel-class="btn-xs btn-danger"
					  dataid="<pg:cell colName="id" />" action="delete"
					class="btn btn-xs btn-danger" > <i class="fa fa-trash-o"></i>
						删除
				</a></td>

			</tr>
		</pg:list>

	</tbody>
</table>

<script>jQuery(document).ready(function() {
							 TableAdvanced.initgencodelist();
							 UIExtendedModals.init();
							 UIConfirmations.init(); 
							});