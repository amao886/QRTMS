package com.ycg.ksh.common.util;

import com.ycg.ksh.common.exception.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;

public class ExcelUtil {
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";  
    public static final String OFFICE_EXCEL_2007_POSTFIX = "xlsx";  
    public static final String EMPTY = "";  
    public static final String POINT = ".";
    protected final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    
    public ExcelUtil() {
    	super();
	}
    
    public static ExcelUtil createExcelUtil(){
    	return new ExcelUtil();
    }
    
    /**
     * 
     * TODO 获得path的后缀名 
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-14 13:52:16
     * @param path
     * @return
     */
    public String getPostfix(String path){  
        if(path==null || EMPTY.equals(path.trim())){  
            return EMPTY;  
        }  
        if(path.contains(POINT)){  
            return path.substring(path.lastIndexOf(POINT)+1,path.length());  
        }  
        return EMPTY;  
    }  
    
    /**
     * 
     * TODO 对表格中数值进行格式化 
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-14 13:51:08
     * @param xssfCell
     * @return
     */ 
    public Object getCellValue(Cell cell){  
        Object value = null;  
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符  
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字  
        switch (cell.getCellTypeEnum()) {  
        case STRING:  
            value = cell.getRichStringCellValue().getString();  
            break;  
        case NUMERIC:  
            if("General".equals(cell.getCellStyle().getDataFormatString())){  
                value = df.format(cell.getNumericCellValue());  
            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){  
                value = DateUtils.date2Str(cell.getDateCellValue());  
            }else{  
                value = df2.format(cell.getNumericCellValue());  
            }
            break;  
        case BOOLEAN:  
            value = cell.getBooleanCellValue();  
            break;  
        case BLANK:  
            value = "";  
            break;  
        default:  
            break;  
        }
        return value;  
    }
    /**
     * 
     * TODO 根据不同Excel版本创建不同的Workbook
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-14 14:29:49
     * @param file
     * @return
     * @throws IOException
     */
    protected Workbook creteWorkBook(MultipartFile file){
    	Workbook wb = null;
    	try {
    		String postfix = getPostfix(file.getOriginalFilename().trim());
    		if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
    			wb = new HSSFWorkbook(file.getInputStream());
    		} else if (ExcelUtil.OFFICE_EXCEL_2007_POSTFIX.equals(postfix)) {
    			wb = new XSSFWorkbook(file.getInputStream());
    		} else {
    			throw new BusinessException("解析的文件格式有误！");
    		}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("创建Excel工作表异常！");
		}
    	return wb;  
    }
    
	public Collection<Collection<Object>> readExcel(MultipartFile file, int startRow, String[] columnKey) {
		Collection<Collection<Object>> list = Collections.emptyList();
		InputStream input = null;
		Workbook workbook = null;
		Collection<Object> rowList = null;
		Collection<String> error = Collections.emptyList();
		try {
			// 创建Excel工作薄
			workbook = creteWorkBook(file);
			// IO流读取文件
			input = file.getInputStream();
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				int totalRows = sheet.getLastRowNum();
				if(totalRows >= 200){
					throw new BusinessException("导入文件行数不能超过200行");
				}
				// 读取Row,从第二行开始
				for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row != null) {
						rowList = Collections.emptyList();
						int totalCells = row.getLastCellNum();
						// 读取列，从第一列开始
						for (int c = 0; c <= totalCells + 1; c++) {
							Cell cell = row.getCell(c);
							if (cell == null || EMPTY.equals(getCellValue(cell))) {
								rowList.add(ExcelUtil.EMPTY);
								error.add("第"+rowNum+"行，第"+ c +"列为空");
								logger.info("第"+rowNum+"行，第"+ c +"列为空");
								continue;
							}
							rowList.add(getCellValue(cell).toString());
						}
						list.add(rowList);
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
