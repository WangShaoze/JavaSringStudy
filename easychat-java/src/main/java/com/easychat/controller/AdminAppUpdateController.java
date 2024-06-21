package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.query.AppUpdateQuery;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.po.AppUpdate;
import com.easychat.exception.BusinessException;
import com.easychat.services.AppUpdateService;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description: app发布 Controller
 * @author: 王绍泽
 * @date: 2024/06/21
 */
@RestController("adminAppUpdateController")
@RequestMapping("/admin")
public class AdminAppUpdateController extends ABaseController {
	@Resource
	private AppUpdateService appUpdateService;

	/**
	 * 获取更新列表
	 * */
	@RequestMapping("/load_update_list")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO loadUpdateList(AppUpdateQuery query){
		query.setOrderBy("id desc");
		return getSuccessResponseVO(appUpdateService.findListByPage(query));
	}

	/**
	 * 创建或更新 app更新列表
	 * @param id  app更新表id  [ 有则是更新，无则是新建 ]
	 * @param version app版本  [必填]
	 * @param updateDesc 该版本的描述  [必填]
	 * @param fileType 文件类型（本地文件 or 外链） [必填]
	 * @param outerLink 外链
	 * @param file app更新包
	 * */
	@RequestMapping("/save_update")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO saveUpdate(Integer id,
								 @NotEmpty String version,
								 @NotEmpty String updateDesc,
								 @NotNull Integer fileType,
								 String outerLink,
								 MultipartFile file
								 ) throws BusinessException, IOException {

		AppUpdate appUpdate = new AppUpdate();
		appUpdate.setId(id);
		appUpdate.setVersion(version);
		appUpdate.setUpdateDesc(updateDesc);
		appUpdate.setFileType(fileType);
		appUpdate.setOuterLink(outerLink);
		appUpdateService.saveUpdate(appUpdate, file);
		return getSuccessResponseVO(null);
	}


	/**
	 * 删除更新
	 * @param id  app更新表中的id
	 * */
	@RequestMapping("/del_update")
	@GlobalInterceptor(checkAdmin = true)
	@Transactional(rollbackFor = Exception.class)
	public ResponseVO delUpdate(@NotNull Integer id) throws BusinessException {
		appUpdateService.deleteById(id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 发布更新
	 * @param id 需要发布版本的id
	 * @param status 当前的状态
	 * @param greyscaleUid  需要灰度发布时，必须提供灰度发布的人员UID
	 * */
	@RequestMapping("/post_update")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO postUpdate(@NotNull Integer id, @NotNull Integer status, String greyscaleUid) throws BusinessException {
		appUpdateService.postUpdate(id, status, greyscaleUid);
		return getSuccessResponseVO(null);
	}


	/**
	 * 加载数据
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(AppUpdateQuery query) {
		return getSuccessResponseVO(this.appUpdateService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(AppUpdate bean) {
		this.appUpdateService.add(bean);
		return getSuccessResponseVO(null);	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(List<AppUpdate> listBean) {
		this.appUpdateService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(List<AppUpdate> listBean) {
		this.appUpdateService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Id查询
	 */
	@RequestMapping("/getById")
	public ResponseVO<AppUpdate> getById(Integer id) {
		return getSuccessResponseVO(this.appUpdateService.getById(id));
	}

	/**
	 * 根据 Id更新
	 */
	@RequestMapping("/updateById")
	public ResponseVO updateById(AppUpdate bean,Integer id) {
		this.appUpdateService.updateById(bean, id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Id删除
	 */
	@RequestMapping("/deleteById")
	public ResponseVO deleteById(Integer id) throws BusinessException {
		this.appUpdateService.deleteById(id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Version查询
	 */
	@RequestMapping("/getByVersion")
	public ResponseVO<AppUpdate> getByVersion(String version) {
		return getSuccessResponseVO(this.appUpdateService.getByVersion(version));
	}

	/**
	 * 根据 Version更新
	 */
	@RequestMapping("/updateByVersion")
	public ResponseVO updateByVersion(AppUpdate bean,String version) {
		this.appUpdateService.updateByVersion(bean, version);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Version删除
	 */
	@RequestMapping("/deleteByVersion")
	public ResponseVO deleteByVersion(String version) {
		this.appUpdateService.deleteByVersion(version);
		return getSuccessResponseVO(null);
	}
}

