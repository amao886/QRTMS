/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-18 14:26:18
 */
package com.ycg.ksh.common.excel;

import com.ycg.ksh.common.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * 操作excel
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-18 14:26:18
 */
public class EasyExcelBuilder {
    
    public static ExcelWriter createWriteExcel(File file) throws Exception {
        return new ExcelWriter(file);
    }

    public static ExcelReader createReadExcel(String filePath) throws Exception{
        return createReadExcel(new File(filePath));
        
    }

    public static ExcelReader createReadExcel(File file) throws Exception {
        return new ExcelReader(new FileInputStream(file));
    }
    public static Collection<Object[]> readExcel(File file, int startIndex, int maxCellIndex) throws Exception {
        try (ExcelReader excelReader = EasyExcelBuilder.createReadExcel(file)) {
            excelReader.setIndex(startIndex, maxCellIndex);
            return excelReader.readExcel();
        }
    }


    
    public static void main(String[] args) throws Exception {
        /*
        ExcelReader reader = EasyExcelBuilder.createReadExcel("C:\\Users\\baymax\\Documents\\Tencent Files\\122543281\\FileRecv\\任务单模板.xlsx");
        reader.setIndex(4, 12);
        Collection<Object[]> collection = reader.readExcel();
        for (Object[] objects : collection) {
            System.out.println(Arrays.toString(objects));
        }
        reader.close();
        */
        File file = FileUtils.file("E:\\cache\\111.xls");
        ExcelWriter writer = EasyExcelBuilder.createWriteExcel(file);
        writer.createSheet("测试");
        for (int i = 0; i < 100; i++) {
            if(i == 0){
                writer.header("表头1","表头1表头1表头1表头1","表头1","表头1","表头1","表头1","表头1","表头1表头1表头1表头1表头1表头1表头1表头1表头1","表头1","表头1","表头1","表头1","表头1","表头1表头1表头1表头1表头1表头1表头1表头1表头1表头1表头1","表头1","表头1","表头1","表头1","表头1");
            }else{
                for (int j = 0; j < 20; j++) {
                    if(j < 19) {
                        writer.column(i * j +"sssss");
                    }else {
                        writer.column(new Date());
                    }
                }
            }
            writer.newRow();
        }
        writer.write();
        writer.close();
    }
    
}
