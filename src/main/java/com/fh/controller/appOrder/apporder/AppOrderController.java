package com.fh.controller.appOrder.apporder;

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
import com.fh.service.appOrder.apporder.AppOrderManager;
import com.fh.entity.system.Dictionaries;
import com.fh.service.system.dictionaries.impl.DictionariesService;

/** 
 * 说明：app_order
 * 创建人：FH Q313596790
 * 创建时间：2018-04-26
 */
@Controller
@RequestMapping(value="/apporder")
public class AppOrderController extends BaseController {
	
	String menuUrl = "apporder/list.do"; //菜单地址(权限用)
	@Resource(name="apporderService")
	private AppOrderManager apporderService;
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增AppOrder");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("APPORDER_ID", this.get32UUID());	//主键
		apporderService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除AppOrder");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		apporderService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改AppOrder");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		apporderService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表AppOrder");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		List<Dictionaries>  order_statuss = dictionariesService.listSubDictByParentId("dd"); //订单状态
		mv.addObject("order_statuss", order_statuss);
		page.setPd(pd);
		List<PageData>	varList = apporderService.list(page);	//列出AppOrder列表
		mv.setViewName("appOrder/apporder/apporder_list");
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
		List<Dictionaries>  order_statuss = dictionariesService.listSubDictByParentId("dd"); //订单状态
		mv.addObject("order_statuss", order_statuss);
		mv.setViewName("appOrder/apporder/apporder_edit");
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
		List<Dictionaries>  order_statuss = dictionariesService.listSubDictByParentId("dd"); //订单状态
		mv.addObject("order_statuss", order_statuss);
		pd = apporderService.findById(pd);	//根据ID读取
		mv.setViewName("appOrder/apporder/apporder_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除AppOrder");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			apporderService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出AppOrder到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("订单id");	//1
		titles.add("用户ID");	//2
		titles.add("商品id");	//3
		titles.add("订单号");	//4
		titles.add("外部订单号");	//5
		titles.add("价格");	//6
		titles.add("运费");	//7
		titles.add("总价");	//8
		titles.add("购买数量");	//9
		titles.add("收货人");	//10
		titles.add("收货人手机号");	//11
		titles.add("收货地址");	//12
		titles.add("订单状态(0待支付10支付成功11支付失败20服务中30已完成)");	//13
		titles.add("创建时间");	//14
		titles.add("修改时间");	//15
		dataMap.put("titles", titles);
		List<PageData> varOList = apporderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).get("USER_ID").toString());	//2
			vpd.put("var3", varOList.get(i).get("GOODS_ID").toString());	//3
			vpd.put("var4", varOList.get(i).getString("ORDER_NUM"));	    //4
			vpd.put("var5", varOList.get(i).getString("OUT_ORDER_NUM"));	    //5
			vpd.put("var6", varOList.get(i).getString("PRICE"));	    //6
			vpd.put("var7", varOList.get(i).getString("FREIGHT"));	    //7
			vpd.put("var8", varOList.get(i).getString("TOTAL_PRICE"));	    //8
			vpd.put("var9", varOList.get(i).get("BUY_NUMS").toString());	//9
			vpd.put("var10", varOList.get(i).getString("CONSIGNEE"));	    //10
			vpd.put("var11", varOList.get(i).getString("MOBILE"));	    //11
			vpd.put("var12", varOList.get(i).getString("REC_ADDRESS"));	    //12
			vpd.put("var13", varOList.get(i).get("STATUS").toString());	//13
			vpd.put("var14", varOList.get(i).getString("CREATE_TIME"));	    //14
			vpd.put("var15", varOList.get(i).getString("UPDATE_TIME"));	    //15
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
