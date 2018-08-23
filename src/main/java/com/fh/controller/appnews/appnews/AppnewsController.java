package com.fh.controller.appnews.appnews;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fh.entity.system.Dictionaries;
import com.fh.entity.system.Type;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.util.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.appnews.appnews.AppnewsManager;

/** 
 * 说明：app_news
 * 创建人：FH Q313596790
 * 创建时间：2018-04-15
 */
@Controller
@RequestMapping(value="/appnews")
public class AppnewsController extends BaseController {
	
	String menuUrl = "appnews/list.do"; //菜单地址(权限用)
	@Resource(name="appnewsService")
	private AppnewsManager appnewsService;
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(
			HttpServletRequest request,

			@RequestParam(value="tp2",required=false) MultipartFile file2,

			@RequestParam(value="tpz2",required=false) String tpz2,
			@RequestParam(value="TITLE",required=false) String TITLE,
			@RequestParam(value="SUB_TITLE",required=false) String SUB_TITLE,
			@RequestParam(value="CONTENT",required=false) String CONTENT,
//			@RequestParam(value="THUMBNAIL",required=false) String THUMBNAIL,
			@RequestParam(value="AUTHOR",required=false) String AUTHOR,
			@RequestParam(value="SOURCE",required=false) String SOURCE,
			@RequestParam(value="TYPE",required=false) String TYPE,
			@RequestParam(value="STATUS",required=false) String STATUS,
			@RequestParam(value="IS_CAROU",required = false) String IS_CAROU,
			@RequestParam(value="PUB_TIME",required=false) String PUB_TIME

	) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Project");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		String  ffile = DateUtil.getDays(), fileName = "";
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/uploads"+"/";


		//String path = request.getContextPath();
		if(null == tpz2){tpz2 = "";}
		if (null != file2 && !file2.isEmpty()) {
			String filePath = PathUtil.getClasspath1() + Const.FILEPATHFILE + ffile;	//图片
			fileName = FileUpload.fileUp(file2, filePath, this.get32UUID());			//执行上传
			pd.put("THUMBNAIL", basePath+Const.FILEPATHFILE+ffile + "/" + fileName);									//路径
			//pd.put("NAME", fileName);
		}else{
			pd.put("THUMBNAIL", tpz2);
		}

		pd.put("TITLE", TITLE);
		pd.put("SUB_TITLE", SUB_TITLE);
		pd.put("CONTENT", CONTENT);
		pd.put("AUTHOR", AUTHOR);
		pd.put("SOURCE", SOURCE);
		pd.put("TYPE", TYPE);
		pd.put("STATUS", STATUS);
		pd.put("PUB_TIME", PUB_TIME);
		pd.put("IS_CAROU",IS_CAROU);

		pd.put("PROJECT_ID", this.get32UUID());	//主键
		appnewsService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}

	@RequestMapping(value="/deltp2")
	public void deltp2(PrintWriter out) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		String PATH = pd.getString("THUMBNAIL");
		if(Tools.notEmpty(pd.getString("THUMBNAIL").trim())){//图片路径
			DelAllFile.delFolder(PathUtil.getClasspath()+ Const.FILEPATHIMG + pd.getString("THUMBNAIL")); 	//删除图片
		}
		if(PATH != null){
			appnewsService.delTp2(pd);																//删除数据库中图片数据
		}
		out.write("success");
		out.close();
	}
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Appnews");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		appnewsService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(
		HttpServletRequest request,

		@RequestParam(value="tp2",required=false) MultipartFile file2,

		@RequestParam(value="tpz2",required=false) String tpz2,
		@RequestParam(value="ID",required=false) String ID,
		@RequestParam(value="TITLE",required=false) String TITLE,
		@RequestParam(value="SUB_TITLE",required=false) String SUB_TITLE,
		@RequestParam(value="CONTENT",required=false) String CONTENT,
//			@RequestParam(value="THUMBNAIL",required=false) String THUMBNAIL,
		@RequestParam(value="AUTHOR",required=false) String AUTHOR,
		@RequestParam(value="SOURCE",required=false) String SOURCE,
		@RequestParam(value="TYPE",required=false) String TYPE,
		@RequestParam(value="STATUS",required=false) String STATUS,
		@RequestParam(value="IS_CAROU",required=false) String IS_CAROU,
		@RequestParam(value="PUB_TIME",required=false) String PUB_TIME

	) throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"新增Project");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();

			String  ffile = DateUtil.getDays(), fileName = "";
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ File.separator+"MVNFHM"+File.separator;


			//String path = request.getContextPath();
			if(null == tpz2){tpz2 = "";}
			if (null != file2 && !file2.isEmpty()) {
				String filePath = PathUtil.getClasspath1() + Const.FILEPATHFILE + ffile;	//图片
				fileName = FileUpload.fileUp(file2, filePath, this.get32UUID());			//执行上传
				pd.put("THUMBNAIL", basePath+Const.FILEPATHFILE+ffile + File.separator + fileName);									//路径
				//pd.put("NAME", fileName);
			}else{
				pd.put("THUMBNAIL", tpz2);
			}

			pd.put("TITLE", TITLE);
			pd.put("SUB_TITLE", SUB_TITLE);
			pd.put("CONTENT", CONTENT);
			pd.put("AUTHOR", AUTHOR);
			pd.put("SOURCE", SOURCE);
			pd.put("TYPE", TYPE);
			pd.put("STATUS", STATUS);
			pd.put("IS_CAROU",IS_CAROU);
			pd.put("PUB_TIME", PUB_TIME);
		pd.put("ID", ID);


		appnewsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Appnews");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<Dictionaries>	order_statuss = dictionariesService.listSubDictByParentId("xj"); //订单状态

		List<Type>	news_type = dictionariesService.getbyNewsType(); //订单状态
		mv.addObject("news_type", news_type);
		List<Dictionaries>	yOn = dictionariesService.listSubDictByParentId("yOn"); //订单状态
		mv.addObject("yOn", yOn);
		mv.addObject("order_statuss", order_statuss);
		List<PageData>	varList = appnewsService.list(page);	//列出Appnews列表
		mv.setViewName("appnews/appnews/appnews_list");
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
		mv.setViewName("appnews/appnews/appnews_edit");
		List<Dictionaries>	order_statuss = dictionariesService.listSubDictByParentId("xj"); //订单状态
		List<Dictionaries>	yOn = dictionariesService.listSubDictByParentId("yOn"); //订单状态
		mv.addObject("yOn", yOn);
		List<Type>	news_type = dictionariesService.getbyNewsType(); //订单状态
		mv.addObject("news_type", news_type);
		mv.addObject("order_statuss", order_statuss);
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
		pd = appnewsService.findById(pd);	//根据ID读取
		mv.setViewName("appnews/appnews/appnews_edit");
		List<Dictionaries>	order_statuss = dictionariesService.listSubDictByParentId("xj"); //订单状态
		List<Dictionaries>	yOn = dictionariesService.listSubDictByParentId("yOn"); //订单状态
		mv.addObject("yOn", yOn);
		List<Type>	news_type = dictionariesService.getbyNewsType(); //订单状态
		mv.addObject("news_type", news_type);
		mv.addObject("order_statuss", order_statuss);
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Appnews");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			appnewsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Appnews到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("新闻id");	//1
		titles.add("标题");	//2
		titles.add("副标题");	//3
		titles.add("内容");	//4
		titles.add("缩略图");	//5
		titles.add("作者");	//6
		titles.add("来源");	//7
		titles.add("新闻类别");	//8
		titles.add("状态(0下架1上架2删除)");	//9
		titles.add("发布日期");	//10
		titles.add("创建时间");	//11
		titles.add("修改时间");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = appnewsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).getString("TITLE"));	    //2
			vpd.put("var3", varOList.get(i).getString("SUB_TITLE"));	    //3
			vpd.put("var4", varOList.get(i).getString("CONTENT"));	    //4
			vpd.put("var5", varOList.get(i).getString("THUMBNAIL"));	    //5
			vpd.put("var6", varOList.get(i).getString("AUTHOR"));	    //6
			vpd.put("var7", varOList.get(i).getString("SOURCE"));	    //7
			vpd.put("var8", varOList.get(i).get("TYPE").toString());	//8
			vpd.put("var9", varOList.get(i).get("STATUS").toString());	//9
			vpd.put("var10", varOList.get(i).getString("PUB_TIME"));	    //10
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
