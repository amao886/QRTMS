/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:45:04
 */
package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.ImageStorage;
import com.ycg.ksh.service.persistence.ImageStorageMapper;
import com.ycg.ksh.service.api.ImageStorageService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 图片存储服务
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:45:04
 */
@Service("ksh.core.service.imageStorageService")
public class ImageStorageServiceImpl implements ImageStorageService {

    @Resource
    private ImageStorageMapper imageStorageMapper;


    @Override
    public Integer count(Integer imageType, Serializable associateKey) throws ParameterException, BusinessException {
        Assert.notBlank(imageType, "图片类型不能为空");
        Assert.notNull(associateKey, "关联信息不能为空");
        try {
            return imageStorageMapper.selectCount(new ImageStorage(imageType, associateKey));
        } catch (Exception e) {
            logger.error("save -> imageType:{} associateKey:{}", imageType, associateKey, e);
            throw BusinessException.dbException("查询图片存储数量异常");
        }
    }

    @Override
    public Collection<ImageStorage> list(Integer imageType, Serializable associateKey) throws ParameterException, BusinessException {
        Example example = new Example(ImageStorage.class);
        example.createCriteria().andEqualTo("imageType", imageType).andEqualTo("associateKey", associateKey);
        example.orderBy("storageTime").desc();
        return imageStorageMapper.selectByExample(example);
    }

    @Override
    public void save(Integer imageType, Serializable associateKey, String path) throws ParameterException, BusinessException {
        Assert.notBlank(imageType, "图片类型不能为空");
        Assert.notNull(associateKey, "关联信息不能为空");
        Assert.notBlank(path, "图片存储路径不能为空");
        try {
            ImageStorage storage = new ImageStorage(imageType, associateKey);
            storage.setId(Globallys.nextKey());
            storage.setStoragePath(path);
            storage.setStorageTime(new Date());
            imageStorageMapper.insertSelective(storage);
        } catch (Exception e) {
            logger.error("save -> imageType:{} associateKey:{} path:{}", imageType, associateKey, path, e);
            throw BusinessException.dbException("图片存储异常");
        }
    }

    @Override
    public void save(Integer imageType, Serializable associateKey, Collection<String> paths) throws ParameterException, BusinessException {
        Assert.notBlank(imageType, "图片类型不能为空");
        Assert.notNull(associateKey, "关联信息不能为空");
        Assert.notEmpty(paths, "图片存储路径不能为空");
        try {
            Date cdate = new Date();
            List<ImageStorage> collection = new ArrayList<ImageStorage>(paths.size());
            for (String storagePath : paths) {
                if (StringUtils.isBlank(storagePath)) {
                    continue;
                }
                ImageStorage storage = new ImageStorage(imageType, associateKey);
                storage.setId(Globallys.nextKey());
                storage.setStoragePath(storagePath);
                storage.setStorageTime(cdate);
                collection.add(storage);
            }
            //批量插入
            imageStorageMapper.inserts(collection);
        } catch (Exception e) {
            logger.error("save -> imageType:{} associateKey:{} path:{}", imageType, associateKey, paths, e);
            throw BusinessException.dbException("图片批量存储异常");
        }
    }

    @Override
    public void save(List<ImageStorage> collection) throws ParameterException, BusinessException {
        Assert.notEmpty(collection, "图片存储信息不能为空");
        try {
            Date cdate = new Date();
            for (Iterator<ImageStorage> iterator = collection.iterator(); iterator.hasNext(); ) {
                ImageStorage ms = iterator.next();
                if (ms.getAssociateKey() == null) {
                    iterator.remove();
                } else if (ms.getImageType() == null || ms.getImageType() <= 0) {
                    iterator.remove();
                } else if (com.ycg.ksh.common.util.StringUtils.isBlank(ms.getStoragePath())) {
                    iterator.remove();
                } else {
                    ms.setId(Globallys.nextKey());
                    ms.setStorageTime(cdate);
                }
            }
            //批量插入
            imageStorageMapper.inserts(collection);
        } catch (Exception e) {
            logger.error("save -> {}", collection, e);
            throw BusinessException.dbException("图片批量存储异常");
        }
    }

    @Override
    public void delete(Integer imageType, Serializable associateKey) throws ParameterException, BusinessException {
        Assert.notBlank(imageType, "图片类型不能为空");
        Assert.notNull(associateKey, "关联信息不能为空");
        try {
            imageStorageMapper.delete(new ImageStorage(imageType, associateKey));
        } catch (Exception e) {
            logger.error("delete -> imageType:{} associateKey:{}", imageType, associateKey, e);
            throw BusinessException.dbException("图片存储删除异常");
        }
    }
} 
