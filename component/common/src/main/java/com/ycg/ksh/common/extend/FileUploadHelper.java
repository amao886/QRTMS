package com.ycg.ksh.common.extend;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/18
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件上传
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/18
 */
public class FileUploadHelper {

    interface ValidateSuffix{
        String validate(String suffix) throws BusinessException;
    }

    public static Collection<String> saveImage(HttpServletRequest request, String baseDic, String subDic) throws IOException {
        return save(request, baseDic, subDic, new ValidateSuffix(){
            @Override
            public String validate(String suffix) throws BusinessException {
                String _suffix = suffix;
                if (StringUtils.isBlank(_suffix)) {
                    _suffix = "jpeg";
                }
                if (!FileUtils.isImage(_suffix)) {
                    throw new BusinessException("必须是图片文件,请重新选择文件!!!");
                }
                return _suffix;
            }
        });
    }

    public static File saveExcel(HttpServletRequest request, String baseDic) throws IOException {
        return saveSingle(request, baseDic, null, new ValidateSuffix(){
            @Override
            public String validate(String suffix) throws BusinessException {
                String _suffix = suffix;
                if (!FileUtils.isExcel(_suffix)) {
                    throw new BusinessException("文件类型有误,请重新选择文件!!!");
                }
                return _suffix;
            }
        });
    }

    public static Collection<String> save(HttpServletRequest request, String baseDic, String subDic, ValidateSuffix validate) throws IOException {
        Collection<String> collection = new ArrayList<String>();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    String suffix = FileUtils.suffix(file.getOriginalFilename());
                    if(validate != null){
                        suffix = validate.validate(suffix);
                    }
                    String filePath = FileUtils.appendSuffix(StringUtils.UUID(), suffix);
                    if(StringUtils.isNotBlank(subDic)){
                        filePath = FileUtils.path(subDic, filePath);
                    }
                    file.transferTo(FileUtils.file(FileUtils.path(baseDic, filePath), true));
                    collection.add("/" + filePath);
                }
            }
        }
        return collection;
    }

    public static File saveSingle(HttpServletRequest request, String baseDic, String subDic, ValidateSuffix validate) throws IOException {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            if (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    String suffix = FileUtils.suffix(file.getOriginalFilename());
                    if(validate != null){
                        suffix = validate.validate(suffix);
                    }
                    String filePath = FileUtils.appendSuffix(StringUtils.UUID(), suffix);
                    if(StringUtils.isNotBlank(subDic)){
                        filePath = FileUtils.path(subDic, filePath);
                    }
                    File saveFile = FileUtils.file(FileUtils.path(baseDic, filePath), true);
                    file.transferTo(saveFile);
                    return saveFile;
                }
            }
        }
        return null;
    }

    public static File saveSingle(HttpServletRequest request, String path) throws IOException {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            if (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    String suffix = FileUtils.suffix(file.getOriginalFilename());
                    String filePath = FileUtils.appendSuffix(StringUtils.UUID(), suffix);
                    File saveFile = FileUtils.file(FileUtils.path(path, filePath), true);
                    file.transferTo(saveFile);
                    return saveFile;
                }
            }
        }
        return null;
    }

    public static File save(HttpServletRequest request, String fileName, String path) throws IOException {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile(fileName);
            if(file != null){
                String suffix = FileUtils.suffix(file.getOriginalFilename());
                String filePath = FileUtils.appendSuffix(StringUtils.UUID(), suffix);
                File saveFile = FileUtils.file(FileUtils.path(path, filePath), true);
                file.transferTo(saveFile);
                return saveFile;
            }
        }
        return null;
    }

    public static Map<String, File> save(HttpServletRequest request, String path) throws IOException {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            Map<String, File> files = new HashMap<String, File>();
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            if (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    String suffix = FileUtils.suffix(file.getOriginalFilename());
                    String filePath = FileUtils.appendSuffix(StringUtils.UUID(), suffix);
                    File saveFile = FileUtils.file(FileUtils.path(path, filePath), true);
                    file.transferTo(saveFile);

                    files.put(file.getName(), saveFile);
                }
            }
            return files;
        }
        return null;
    }

}
