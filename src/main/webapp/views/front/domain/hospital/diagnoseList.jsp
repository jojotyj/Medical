<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/views/common/meta_info.htm"%>
<%@ include file="/views/common/common_css.htm"%>
<link rel="stylesheet" type="text/css" href="/css/front/style.css"
	title="v" />
<link rel="stylesheet" type="text/css"
	href="/third-party/pagination/paging.css" />
</head>
<body>
	<%@ include file="/views/front/common/hospital/top.htm"%>
	<div class="pz_main">
		<%@ include file="/views/front/common/hospital/hospitalTop.htm"%>
		<!-- 顶部菜单栏 -->
		<div class="w_1000">
			<%@ include file="/views/front/common/hospital/hospitalLeft.htm"%><!-- 左边菜单栏 -->
			<div class="w_851">
				<div class="t_851_2">病历夹</div>
				<div class="c_851">
					<div class="t_851_3">
						<table cellpadding="0" cellspacing="0" border="0">
							<tr height="44" valign="middle">
								<td width="100">医生姓名：</td>
								<td width="180"><input type="text" class="text_input2"
									style="width: 160px;" id="doctorName" /></td>
								<td width="75">排序：</td>
								<td width="199"><select id="searchType" class="text_input2">
										<option value="">请选择</option>
										<option value="1">就诊时间</option>
										<option value="4">医生姓名</option>
								</select></td>
								<td width="199"><select id="ascOrdesc" class="text_input2">
										<option value="">请选择</option>
										<option value="ASC">升序</option>
										<option value="DESC">降序</option>
								</select></td>
								<td width="64"><input type="submit" class="sub_1"
									value="搜索" onclick="loadGridData()" /></td>
								<td width="64"><input type="submit" class="sub_1"
									value="重置" onclick="resetQuery()" style="margin-left: 10px;" /></td>
							</tr>
						</table>
					</div>
					<div class="d_851_1">
						<table cellpadding="0" cellspacing="0" border="0" width="849">
							<tbody id="dataListContainerList">
							</tbody>
						</table>
						<div class="fy_new">
							<div id="page" class="inline-center"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="pz_down">
		<%@ include file="/views/front/common/bottom.htm"%><!-- 底部栏 -->
	</div>
	<%@ include file="/views/common/common_js.htm"%>
	<%@ include file="/views/common/common_front_js.htm"%>
	<script type="text/javascript"
		src="/js/front/domain/hospital/hospitalCommon.js" title="v"></script>
	<script type="text/javascript"
		src="/js/front/domain/hospital/diagnoseList.js" title="v"></script>

	<script id="template" type="x-tmpl-mustache"><!-- -->
		<tr height="53" align="center" style="font-size:14px; color:#63a13f">
        	<td width="95">所属医生</td>
        	<td width="95">姓名</td>
            <td width="70">性别</td>
            <td width="155">联系电话</td>
            <td width="155">就诊时间</td>
			<td width="155">药品总价</td>
            <td width="15">&nbsp;</td>
            <td width="140">操作</td>
        </tr>
		{{#rows}}
		<tr height="58" align="center" style="color:#666666">
	    	<td width="95">{{doctorName}}</td>
	    	<td width="95">{{patientName}}</td>
	        <td width="70">{{patientSexName}}</td>
	        <td width="155">{{patientPhone}}</td>
	        <td width="155">{{createDate}}</td>
			<td width="155">{{medicalTotalPrice}}</td>
	        <td width="15">&nbsp;</td>
	        <td width="140">
				<button type="button" onclick="window.open('/hospital/diagnosePrint/{{id}}');"" class="btn btn-info">打印</button>
				{{#isOwner}}
				<button type="button" onclick="lh.confirm({content: '是否确定删除该病历？', clickYes:deleteDiagnose, clickYesParam:{{id}}});" class="btn btn-danger">删除</button>
				<button type="button" onclick="readDiagnose({{id}});" class="btn btn-success">查看</button>
				{{/isOwner}}
				{{^isOwner}}
				<button type="button" onclick="readDiagnose({{id}});" class="btn btn-primary">查看</button>
				{{/isOwner}}


	        </td>
	    </tr>
		{{/rows}}		 		 
	</script>
</body>
</html>

