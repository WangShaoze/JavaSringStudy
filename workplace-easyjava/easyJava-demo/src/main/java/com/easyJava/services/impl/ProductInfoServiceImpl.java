package com.easyJava.services.impl;

import com.easyJava.entity.po.ProductInfo;
import com.easyJava.entity.query.ProductInfoQuery;
import com.easyJava.services.ProductInfoService;
import com.easyJava.mappers.ProductInfoMapper;
import com.easyJava.entity.query.SimplePage;
import com.easyJava.entity.vo.PaginationResultVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description: 商品信息 业务接口实现
 * @author: 王绍泽
 * @date: 2024/05/21
 */
@Service("productInfoService")
public class ProductInfoServiceImpl implements ProductInfoService{
	@Resource
	private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;


	/**
	 * 根据条件查询列表
	 */
	public List<ProductInfo> findListByParam(ProductInfoQuery query) {

		return this.productInfoMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(ProductInfoQuery query) {

		return this.productInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<ProductInfo> findListByPage(ProductInfoQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<ProductInfo> list = this.findListByParam(query);
		PaginationResultVO<ProductInfo> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(ProductInfo bean) {
		return this.productInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<ProductInfo> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.productInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<ProductInfo> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.productInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 Id查询
	 */
	public ProductInfo getById(Integer id) {
		return this.productInfoMapper.selectById(id);
	}

	/**
	 * 根据 Id更新
	 */
	public Integer updateById(ProductInfo bean,Integer id) {

		return this.productInfoMapper.updateById(bean, id);
	}

	/**
	 * 根据 Id删除
	 */
	public Integer deleteById(Integer id) {

		return this.productInfoMapper.deleteById(id);
	}

	/**
	 * 根据 Code查询
	 */
	public ProductInfo getByCode(String code) {
		return this.productInfoMapper.selectByCode(code);
	}

	/**
	 * 根据 Code更新
	 */
	public Integer updateByCode(ProductInfo bean,String code) {

		return this.productInfoMapper.updateByCode(bean, code);
	}

	/**
	 * 根据 Code删除
	 */
	public Integer deleteByCode(String code) {

		return this.productInfoMapper.deleteByCode(code);
	}

	/**
	 * 根据 SkuTypeAndColorType查询
	 */
	public ProductInfo getBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return this.productInfoMapper.selectBySkuTypeAndColorType(skuType, colorType);
	}

	/**
	 * 根据 SkuTypeAndColorType更新
	 */
	public Integer updateBySkuTypeAndColorType(ProductInfo bean,Integer skuType, Integer colorType) {

		return this.productInfoMapper.updateBySkuTypeAndColorType(bean, skuType, colorType);
	}

	/**
	 * 根据 SkuTypeAndColorType删除
	 */
	public Integer deleteBySkuTypeAndColorType(Integer skuType, Integer colorType) {

		return this.productInfoMapper.deleteBySkuTypeAndColorType(skuType, colorType);
	}
}

