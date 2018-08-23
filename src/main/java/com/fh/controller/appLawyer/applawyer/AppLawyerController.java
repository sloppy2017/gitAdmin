package com.fh.controller.appLawyer.applawyer;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.service.appLawyer.applawyer.AppLawyerManager;
import com.fh.entity.system.Dictionaries;
import com.fh.service.system.dictionaries.impl.DictionariesService;
/** 
 * 说明：app_lawyer
 * 创建人：FH Q313596790
 * 创建时间：2018-04-26
 */
@Controller
@RequestMapping(value="/applawyer")
public class AppLawyerController extends BaseController {
	
	String menuUrl = "applawyer/list.do"; //菜单地址(权限用)
	@Resource(name="applawyerService")
	private AppLawyerManager applawyerService;
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;



	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增AppLawyer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("APPLAWYER_ID", this.get32UUID());	//主键
		applawyerService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除AppLawyer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		applawyerService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改AppLawyer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		applawyerService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表AppLawyer");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<Dictionaries>  order_statuss = dictionariesService.listSubDictByParentId("yesOrNo"); //订单状态
		mv.addObject("order_statuss", order_statuss);
		List<Dictionaries>  sexs = dictionariesService.listSubDictByParentId("sex"); //订单状态
		mv.addObject("sexs", sexs);

		List<PageData>	varList = applawyerService.list(page);	//列出AppLawyer列表
		mv.setViewName("appLawyer/applawyer/applawyer_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<Dictionaries>  order_statuss = dictionariesService.listSubDictByParentId("yesOrNo"); //订单状态
		mv.addObject("order_statuss", order_statuss);
		List<Dictionaries>  sexs = dictionariesService.listSubDictByParentId("sex"); //订单状态
		mv.addObject("sexs", sexs);
		mv.setViewName("appLawyer/applawyer/applawyer_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<Dictionaries>  order_statuss = dictionariesService.listSubDictByParentId("yesOrNo"); //订单状态
		mv.addObject("order_statuss", order_statuss);
		List<Dictionaries>  sexs = dictionariesService.listSubDictByParentId("sex"); //订单状态
		mv.addObject("sexs", sexs);
		pd = applawyerService.findById(pd);	//根据ID读取
		mv.setViewName("appLawyer/applawyer/applawyer_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除AppLawyer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			applawyerService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出AppLawyer到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("");	//1
		titles.add("律师姓名");	//2
		titles.add("性别(0未知1男2女)");	//3
		titles.add("手机号");	//4
		titles.add("从业年限");	//5
		titles.add("所属律所");	//6
		titles.add("专业简介");	//7
		titles.add("详细介绍");	//8
		titles.add("合作案例");	//9
		titles.add("状态(0删除1正常)");	//10
		titles.add("创建时间");	//11
		titles.add("修改时间");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = applawyerService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).getString("USER_NAME"));	    //2
			vpd.put("var3", varOList.get(i).get("SEX").toString());	//3
			vpd.put("var4", varOList.get(i).getString("MOBILE"));	    //4
			vpd.put("var5", varOList.get(i).get("WORK_YEAR").toString());	//5
			vpd.put("var6", varOList.get(i).getString("COUNSEL"));	    //6
			vpd.put("var7", varOList.get(i).getString("SYNOPSIS"));	    //7
			vpd.put("var8", varOList.get(i).getString("DETAIL"));	    //8
			vpd.put("var9", varOList.get(i).getString("COOPERATION"));	    //9
			vpd.put("var10", varOList.get(i).get("STATUS").toString());	//10
			vpd.put("var11", varOList.get(i).getString("CREATE_TIME"));	    //11
			vpd.put("var12", varOList.get(i).getString("UPDATE_TIME"));	    //12
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
