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
		<div class="w_1000">
			<%@ include file="/views/front/common/hospital/hospitalLeft.htm"%><!--左边导航 -->
			<div class="w_851">
				<div class="t_851_2">患者库</div>
				<div class="t_851_3">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr height="44" valign="middle">
							<td width="75">姓名：</td>
							<td width="199"><input type="text" class="text_input2"
								id="name" /></td>
							<td width="60">排序：</td>
							<td width="199"><select id="searchType" class="text_input1">
									<option value="">请选择</option>
									<option value="1">地区</option>
									<option value="2">注册时间</option>
									<option value="3">患者姓名</option>
							</select></td>
							<td width="199"><select id="ascOrdesc" class="text_input1">
									<option value="">请选择</option>
									<option value="ASC">升序</option>
									<option value="DESC">降序</option>
							</select></td>
							<td width="64"><input type="submit" class="sub_1" value="搜索"
								onclick="loadGridData()" /></td>
							<td width="64"><input type="submit" class="sub_1" value="重置"
								onclick="resetQuery()" style="margin-left: 10px;" /></td>
						</tr>
					</table>
				</div>
				<div class="d_851_1">
					<table cellpadding="0" cellspacing="0" border="0" width="849">
						<tbody id="dataListContainer">
						</tbody>
						<tr height="53" align="center"
							style="color: #666666; display: none" id="noData">
							<td>暂无数据</td>
						</tr>
					</table>
				</div>
				<div class="fy_new">
					<div id="page" class="inline-center"></div>
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
		src="/js/front/domain/hospital/patient.js" title="v"></script>
	<script id="template" type="x-tmpl-mustache">
		<tr height="53" align="center" style="font-size:14px; color:#63a13f">
        	<td width="95">用户昵称</td>
			<td width="95">真实姓名</td>
            <td width="70">性别</td>
            <td width="130">地区</td>
            <td width="155">联系电话</td>
            <td width="115">注册时间</td>
            <td width="15">&nbsp;</td>
        </tr>
		{{#rows}}
			<tr height="53" align="center" style="font-size:14px; color:#63a13f">
        	<td width="95">{{username}}</td>
			<td width="95">{{realName}}</td>
            <td width="70">{{sexName}}</td>
            <td width="130">{{provinceName}}&nbsp;{{cityName}}</td>
            <td width="155">{{phone}}</td>
            <td width="115">{{&createDate}}</td>
        </tr>
		{{/rows}}
	</script>
</body>
</html>
