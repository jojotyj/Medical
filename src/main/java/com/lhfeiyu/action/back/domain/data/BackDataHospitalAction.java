package com.lhfeiyu.action.back.domain.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.lhfeiyu.config.AssetsPath;
import com.lhfeiyu.config.PagePath;
import com.lhfeiyu.config.Table;
import com.lhfeiyu.po.Admin;
import com.lhfeiyu.po.DataHospital;
import com.lhfeiyu.service.AA_UtilService;
import com.lhfeiyu.service.DataHospitalService;
import com.lhfeiyu.service.DictService;
import com.lhfeiyu.service.PictureService;
import com.lhfeiyu.tools.ActionUtil;
import com.lhfeiyu.tools.Check;
import com.lhfeiyu.tools.Pagination;
import com.lhfeiyu.tools.Result;
import com.lhfeiyu.util.RequestUtil;

@Controller
@RequestMapping(value="/back")
public class BackDataHospitalAction {
	
	@Autowired
	private DataHospitalService  dataHospitalService;
	@Autowired
	private AA_UtilService  utilService;
	@Autowired
	private PictureService  pictureService;
	@Autowired
	private DictService  dictService;
	
	private static Logger logger = Logger.getLogger("R");
	
	@RequestMapping(value="/dataHospital")
	public ModelAndView  dataHospital(HttpServletRequest request, ModelMap modelMap){
		String path = PagePath.backDataHospital;
		try{
			Admin admin = ActionUtil.checkSession4Admin(request.getSession());//验证session中的user，存在即返回
			if(admin.getRoleId() == 3){
				modelMap.put("notAdmin", 1);
			}
			Result.success(modelMap, "诊所页面加载成功", null);
		}catch(Exception e){
			path = PagePath.error;
			Result.catchError(e, logger, "LH_ERROR-Hospital-PAGE-/back/dataHospital-加载诊所出现异常", modelMap);
		}
		return new ModelAndView(path,modelMap);
	}
	
	@RequestMapping(value="/dataHospitalDetail/{dataHospitalId}")
	public ModelAndView  dataHospitalDetail(HttpServletRequest request, ModelMap modelMap, @PathVariable Integer dataHospitalId){
		String path = PagePath.dataHospitalDetail;
		try{
			Admin admin = ActionUtil.checkSession4Admin(request.getSession());//验证session中的user，存在即返回
			if(admin.getRoleId() == 3){
				modelMap.put("notAdmin", 1);
			}
			DataHospital dataHospital = dataHospitalService.selectByPrimaryKey(dataHospitalId);
			JSONObject json = new JSONObject();
			json.put("dataHospital", dataHospital);
			modelMap.put("paramJson", json);
			modelMap.put("dataHospital", dataHospital);
			Result.success(modelMap, "诊所页面加载成功", null);
		}catch(Exception e){
			path = PagePath.error;
			Result.catchError(e, logger, "LH_ERROR-Hospital-PAGE-/back/dataHospitalDetail-加载诊所出现异常", modelMap);
		}
		return new ModelAndView(path,modelMap);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getDataHospitalList", method=RequestMethod.POST)
	public JSONObject getDataHospitalList(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {//自动获取所有参数（查询条件）
			Admin admin = ActionUtil.checkSession4Admin(request.getSession());//验证session中的user，存在即返回
			if(null == admin)return Result.adminSessionInvalid(json);
			HashMap<String, Object> map = Pagination.getOrderByAndPage(RequestUtil.getRequestParam(request), request);
			String ascOrdesc = request.getParameter("ascOrdesc");
			if(null != ascOrdesc){
				if(ascOrdesc.equals("1")){
					map.put("orderBy", "whole_name");
					map.put("ascOrdesc", "ASC");
				}else if(ascOrdesc.equals("2")){
					map.put("orderBy", "whole_name");
					map.put("ascOrdesc", "DESC");
				}else if(ascOrdesc.equals("3")){
					map.put("orderBy", "province");
					map.put("ascOrdesc", "ASC");
				}else if(ascOrdesc.equals("4")){
					map.put("orderBy", "province");
					map.put("ascOrdesc", "DESC");
				}else if(ascOrdesc.equals("5")){
					map.put("orderBy", "created_at");
					map.put("ascOrdesc", "ASC");
				}else if(ascOrdesc.equals("6")){
					map.put("orderBy", "created_at");
					map.put("ascOrdesc", "DESC");
				}
			}
			
			if(admin.getRoleId()  == 3){//数据录入员 - 只查看自己的数据
				map.put("adminId", admin.getId());
			}
			
			List<DataHospital> hospitalList = dataHospitalService.selectListByCondition(map);
			Integer total = dataHospitalService.selectCountByCondition(map);
			Result.gridData(hospitalList, total, json);
			Result.success(json, "数据加载成功", null);
		} catch (Exception e) {
			Result.catchError(e, logger, "LH_ERROR-Hospital-AJAX-/back/getHospitalList-加载诊所列表出现异常", json);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/addOrUpdateDataHospital", method = RequestMethod.POST)
	public JSONObject addOrUpdateDataHospital(@ModelAttribute DataHospital hospital,HttpServletRequest request){
		JSONObject json = new JSONObject();
		try {
			Admin admin = ActionUtil.checkSession4Admin(request.getSession());//验证session中的user，存在即返回
			if(null == admin)return Result.adminSessionInvalid(json);
			Date date = new Date();
			String username = admin.getUsername();
			if(Check.isNull(hospital.getLogo())){
				String dictValue = dictService.getDictValueByCode(AssetsPath.code_defaultHospitalLogo);
				hospital.setLogo(dictValue);
			}
			if(null == hospital.getId()){//添加
				hospital.setAdminId(admin.getId());//关联录入员ID，录入员只能查看自己录入的数据
				hospital.setCreatedAt(date);
				hospital.setCreatedBy(username);
				dataHospitalService.insert(hospital);
			}else{//修改
				DataHospital dbHospital = dataHospitalService.selectByPrimaryKey(hospital.getId());
				String basePath = request.getSession().getServletContext().getRealPath("/");
				String newAvatar = hospital.getLogo();
				String oldAvatar = dbHospital.getLogo();
				if(Check.isNotNull(newAvatar) && Check.isNotNull(oldAvatar) && !newAvatar.equals(oldAvatar)){//路径不相等，删除之前的头像
					Integer picId = pictureService.insertPicAndTransfer(newAvatar, basePath, AssetsPath.foler_dataHospital);//现在是上传照片时不插入数据库，具体业务功能时插入数据库
					String savePath = basePath + AssetsPath.foler_dataHospital;
					String savePic = savePath + newAvatar.replace( "/"+ AssetsPath.defaultFileFolder, "");
					hospital.setLogo(savePic);
					hospital.setLogoPicId(picId);
					Integer avatarPicId = dbHospital.getLogoPicId();
					if(Check.isNotNull(avatarPicId)){
						pictureService.deleteByPrimaryKey(avatarPicId);
					}
				}
				hospital.setUpdatedAt(date);
				hospital.setUpdatedBy(username);
				dataHospitalService.updateByPrimaryKeySelective(hospital);
			}
			json.put("id", hospital.getId());
			Result.success(json, "添加或修改诊所成功", null);
		}catch (Exception e) {
			Result.catchError(e, logger, "LH_ERROR-Hospital-AJAX-/back/addOrUpdateDataHospital-新增或修改诊所出现异常", json);
		}
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/updateDataHospitalDelete",method=RequestMethod.POST)
	public JSONObject updateDataHospitalDelete(HttpServletRequest request, @RequestParam(value="ids") String ids) {
		JSONObject json = new JSONObject();
		try {
			if(Check.isNull(ids))return Result.failure(json, "请先选择数据", "ids_null");
			Admin admin = ActionUtil.checkSession4Admin(request.getSession());//验证session中的user，存在即返回
			if(null == admin)return Result.adminSessionInvalid(json);
			String username = admin.getUsername();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids", ids);
			map.put("table", Table.data_hospital);
			map.put("username", username);
			utilService.updateDeletedNowByIds(map);
			Result.success(json, "数据删除成功", null);
		} catch (Exception e) {
			Result.catchError(e, logger, "LH_ERROR-Hospital-AJAX-/back/updateDataHospitalDelete-删除诊所出现异常", json);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateDataHospitalRecover",method=RequestMethod.POST)
	public JSONObject updateDataHospitalRecover(HttpServletRequest request, @RequestParam(value="ids") String ids) {
		JSONObject json = new JSONObject();
		try {
			if(Check.isNull(ids))return Result.failure(json, "请先选择数据", "ids_null");
			Admin admin = ActionUtil.checkSession4Admin(request.getSession());//验证session中的user，存在即返回
			if(null == admin)return Result.adminSessionInvalid(json);
			String username = admin.getUsername();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids", ids);
			map.put("table", Table.data_hospital);
			map.put("username", username);
			utilService.updateDeletedNullByIds(map);
			Result.success(json, "数据恢复成功", null);
		} catch (Exception e) {
			Result.catchError(e, logger, "LH_ERROR-Hospital-AJAX-/back/updateDataHospitalRecover-恢复诊所出现异常", json);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDataHospitalThorough",method=RequestMethod.POST)
	public JSONObject deleteDataHospitalThorough(HttpServletRequest request, @RequestParam(value="ids") String ids) {
		JSONObject json = new JSONObject();
		try {
			if(Check.isNull(ids))return Result.failure(json, "请先选择数据", "ids_null");
			Admin admin = ActionUtil.checkSession4Admin(request.getSession());//验证session中的user，存在即返回
			if(null == admin)return Result.adminSessionInvalid(json);
			String username = admin.getUsername();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids", ids);
			map.put("table", Table.data_hospital);
			map.put("username",username);
			utilService.deleteByIds(map);
			Result.success(json, "数据彻底删除成功", null);
		} catch (Exception e) {
			Result.catchError(e, logger, "LH_ERROR-Hospital-AJAX-/back/deleteDataHospitalThorough-彻底删除诊所出现异常", json);
		}
		return json;
	}
	
}
