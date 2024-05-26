package com.easyJava.controller;

import com.easyJava.entity.query.ProductInfoQuery;
import com.easyJava.entity.vo.ResponseVO;
import com.easyJava.entity.po.ProductInfo;
import com.easyJava.services.ProductInfoService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description: 商品信息 Controller
 * @author: 王绍泽
 * @date: 2024/05/21
 */
@RestController
@RequestMapping("/productInfoService")
public class ProductInfoController extends ABaseController {
	@Resource
	private ProductInfoService productInfoService;

	/**
	 * 加载数据
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ProductInfoQuery query) {
		return getSuccessResponseVO(this.productInfoService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ProductInfo bean) {
		this.productInfoService.add(bean);
		return getSuccessResponseVO(null);	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(List<ProductInfo> listBean) {
		this.productInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(List<ProductInfo> listBean) {
		this.productInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Id查询
	 */
	@RequestMapping("/getById")
	public ResponseVO<ProductInfo> getById(Integer id) {
		return getSuccessResponseVO(this.productInfoService.getById(id));
	}

	/**
	 * 根据 Id更新
	 */
	@RequestMapping("/updateById")
	public ResponseVO updateById(ProductInfo bean,Integer id) {
		this.productInfoService.updateById(bean, id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Id删除
	 */
	@RequestMapping("/deleteById")
	public ResponseVO deleteById(Integer id) {
		this.productInfoService.deleteById(id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Code查询
	 */
	@RequestMapping("/getByCode")
	public ResponseVO<ProductInfo> getByCode(String code) {
		return getSuccessResponseVO(this.productInfoService.getByCode(code));
	}

	/**
	 * 根据 Code更新
	 */
	@RequestMapping("/updateByCode")
	public ResponseVO updateByCode(ProductInfo bean,String code) {
		this.productInfoService.updateByCode(bean, code);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Code删除
	 */
	@RequestMapping("/deleteByCode")
	public ResponseVO deleteByCode(String code) {
		this.productInfoService.deleteByCode(code);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 SkuTypeAndColorType查询
	 */
	@RequestMapping("/getBySkuTypeAndColorType")
	public ResponseVO<ProductInfo> getBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return getSuccessResponseVO(this.productInfoService.getBySkuTypeAndColorType(skuType, colorType));
	}

	/**
	 * 根据 SkuTypeAndColorType更新
	 */
	@RequestMapping("/updateBySkuTypeAndColorType")
	public ResponseVO updateBySkuTypeAndColorType(ProductInfo bean,Integer skuType, Integer colorType) {
		this.productInfoService.updateBySkuTypeAndColorType(bean, skuType, colorType);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 SkuTypeAndColorType删除
	 */
	@RequestMapping("/deleteBySkuTypeAndColorType")
	public ResponseVO deleteBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		this.productInfoService.deleteBySkuTypeAndColorType(skuType, colorType);
		return getSuccessResponseVO(null);
	}
}

