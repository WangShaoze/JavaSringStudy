package com.easyJava.services;

import com.easyJava.entity.po.ProductInfo;
import com.easyJava.entity.query.ProductInfoQuery;
import com.easyJava.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description: 商品信息 业务接口
 * @author: 王绍泽
 * @date: 2024/05/21
 */
public interface ProductInfoService {


	/**
	 * 根据条件查询列表
	 */
	List<ProductInfo> findListByParam(ProductInfoQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ProductInfoQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<ProductInfo> findListByPage(ProductInfoQuery query);


	/**
	 * 新增
	 */
	Integer add(ProductInfo bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<ProductInfo> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ProductInfo> listBean);


	/**
	 * 根据 Id查询
	 */
	ProductInfo getById(Integer id);


	/**
	 * 根据 Id更新
	 */
	Integer updateById(ProductInfo bean, Integer id);


	/**
	 * 根据 Id删除
	 */
	Integer deleteById(Integer id);


	/**
	 * 根据 Code查询
	 */
	ProductInfo getByCode(String code);


	/**
	 * 根据 Code更新
	 */
	Integer updateByCode(ProductInfo bean, String code);


	/**
	 * 根据 Code删除
	 */
	Integer deleteByCode(String code);


	/**
	 * 根据 SkuTypeAndColorType查询
	 */
	ProductInfo getBySkuTypeAndColorType(Integer skuType, Integer colorType);


	/**
	 * 根据 SkuTypeAndColorType更新
	 */
	Integer updateBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType);


	/**
	 * 根据 SkuTypeAndColorType删除
	 */
	Integer deleteBySkuTypeAndColorType(Integer skuType, Integer colorType);

}

