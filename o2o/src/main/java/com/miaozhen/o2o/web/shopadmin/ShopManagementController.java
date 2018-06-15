package com.miaozhen.o2o.web.shopadmin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.miaozhen.o2o.util.CodeUtil;
import com.miaozhen.o2o.util.ImageUtil;
import com.miaozhen.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaozhen.o2o.dto.ShopExecution;
import com.miaozhen.o2o.entity.Area;
import com.miaozhen.o2o.entity.PersonInfo;
import com.miaozhen.o2o.entity.Shop;
import com.miaozhen.o2o.entity.ShopCategory;
import com.miaozhen.o2o.enums.ShopStateEnum;
import com.miaozhen.o2o.exceptions.ShopOperationException;
import com.miaozhen.o2o.service.AreaService;
import com.miaozhen.o2o.service.ShopCategoryService;
import com.miaozhen.o2o.service.ShopService;
import com.miaozhen.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<>();
		long shopId = HttpServletRequestUtil.getLong(request,"shopId");
		if(shopId<=0){
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj == null){
				modelMap.put("url","/shop/shopList");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				currentShop.setShopId(shopId);
				request.getSession().setAttribute("redirect",false);
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop",currentShop);
			modelMap.put("redirect",false);
		}
		return modelMap;
	}
	@RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopList(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<>();
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		user.setUserId(1L);
		user.setName("test");
		long employeeId = user.getUserId();
		List<Shop> shopList = new ArrayList<>();
		try{
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition,0,100);
			modelMap.put("shopList",se.getShopList());
			modelMap.put("user",user);
			modelMap.put("success",true);
		}catch (Exception e){
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
		if(shopId>-1){
			try{
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop",shop);
				modelMap.put("areaList",areaList);
				modelMap.put("success",true);
			}catch (Exception e){
				modelMap.put("success",false);
				modelMap.put("errMsg",e.toString());
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","empty shopId");
		}
		return modelMap;
	}
	@RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<>();
		List<ShopCategory> shopCategoryList ;
		List<Area> areaList ;
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList",shopCategoryList);
			modelMap.put("areaList",areaList);
			modelMap.put("success",true);
		}catch (Exception e){
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
	}
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success",false);
			modelMap.put("errMsg","输入的验证码有误！");
			return modelMap;
		}
		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		// 2.注册店铺
		if (shop != null && shopImg != null) {
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se = null;
			try {
				se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
				if(se.getState()==ShopStateEnum.CHECK.getState()){
					modelMap.put("success",true);
					//该用户可以操作的店铺列表
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList==null||shopList.size()==0){
						shopList = new ArrayList<>();
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList",shopList);
					}else {
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList",shopList);
					}
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}

			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success",false);
			modelMap.put("errMsg","输入的验证码有误！");
			return modelMap;
		}
		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// 2.修改店铺信息
		if (shop != null && shop.getShopId()!= null) {
			PersonInfo owner = new PersonInfo();
			//Session todo
			owner.setUserId(1L);
			shop.setOwner(owner);
			ShopExecution se = null;
			try {
				if(shopImg==null){
					se = shopService.modifyShop(shop,null,null);
				}else {
					se = shopService.modifyShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
				}
				if(se.getState()==ShopStateEnum.SUCCESS.getState()){
					modelMap.put("success",true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}

			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺Id");
			return modelMap;
		}
	}

	/*private static void inputStreamToFile(InputStream ins,File file){
		OutputStream os = null;
		try{
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead=ins.read(buffer))!=-1){
				os.write(buffer,0,bytesRead);
			}
		}catch (Exception e){
			throw new RuntimeException("调用inputStreamToFile产生异常"+e.getMessage());
		}finally {
			try{
				if(os!=null){
					os.close();
				}
				if(ins!=null){
					ins.close();
				}
			}catch (Exception e){
				throw new RuntimeException("inputStreamToFile关闭io产生异常"+e.getMessage());
			}
		}
	}*/
}
