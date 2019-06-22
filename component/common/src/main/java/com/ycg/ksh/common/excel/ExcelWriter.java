/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 12:46:11
 */
package com.ycg.ksh.common.excel;

import com.ycg.ksh.common.util.DateFormatUtils;
import com.ycg.ksh.common.util.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;

import java.io.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

/**
 * excel文件写入操作工具，基于poi之上
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 12:46:11
 */
public class ExcelWriter implements java.lang.AutoCloseable {

    private static String DF_FORMAT= "yyyy-MM-dd HH:mm:ss";
    private static DateTimeFormatter DF_YMDHMS= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter DF_YMD= DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter DF_HMS= DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String EMPTY_VALUE = "";

    private SXSSFWorkbook workbook;

    private SXSSFSheet sheet;
    private OutputStream outputStream;

    private int rowIndex;
    private int columnIndex;

    private CellStyle headerStyle;         // 文件头样式
    private CellStyle titleStyle;
    private CellStyle commonStyle;         // 数据列表样式
    private CellStyle emphasizeStyle;      // 强调数据列表样式

    public ExcelWriter(File file) throws Exception {
       this();
        outputStream = new FileOutputStream(FileUtils.file(file));
    }

    public ExcelWriter() throws Exception {
        workbook = new SXSSFWorkbook(100);
        rowIndex = columnIndex = 0;
        init();
    }


    private void init() {
        // 设置样式
        headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColorPredefined.SKY_BLUE.getIndex());// 设置背景色
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        headerStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        headerStyle.setBorderTop(BorderStyle.THIN);// 上边框
        headerStyle.setBorderRight(BorderStyle.THIN);// 右边框

        Font headerFont = workbook.createFont();
        headerFont.setFontName("黑体");
        headerFont.setFontHeightInPoints((short) 14);// 设置字体大小
        headerStyle.setFont(headerFont);


        // 设置样式
        titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(HSSFColorPredefined.SKY_BLUE.getIndex());// 设置背景色
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        titleStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        titleStyle.setBorderTop(BorderStyle.THIN);// 上边框
        titleStyle.setBorderRight(BorderStyle.THIN);// 右边框
        titleStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        Font titleFont = workbook.createFont();
        titleFont.setFontName("黑体");
        titleFont.setFontHeightInPoints((short) 25);// 设置字体大小
        titleStyle.setFont(titleFont);


        Font commonFont = workbook.createFont();
        commonFont.setFontName("仿宋_GB2312");
        commonFont.setFontHeightInPoints((short) 12);


        // 设置样式
        commonStyle = workbook.createCellStyle();
        commonStyle.setFillPattern(FillPatternType.NO_FILL);
        commonStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        commonStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        commonStyle.setBorderTop(BorderStyle.THIN);// 上边框
        commonStyle.setBorderRight(BorderStyle.THIN);// 右边框
        commonStyle.setFont(commonFont);// 选择需要用到的字体格式


        // 设置样式
        emphasizeStyle = workbook.createCellStyle();
        //emphasizeStyle.setFillForegroundColor(HSSFColorPredefined.RED.getIndex());
        //emphasizeStyle.setFillBackgroundColor(HSSFColorPredefined.RED.getIndex());
        //emphasizeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        emphasizeStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        emphasizeStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        emphasizeStyle.setBorderTop(BorderStyle.THIN);// 上边框
        emphasizeStyle.setBorderRight(BorderStyle.THIN);// 右边框

        Font emphasizeFont = workbook.createFont();
        emphasizeFont.setFontName("仿宋_GB2312");
        emphasizeFont.setFontHeightInPoints((short) 12);
        emphasizeFont.setColor(HSSFColorPredefined.RED.getIndex());//字体颜色
        emphasizeStyle.setFont(emphasizeFont);// 选择需要用到的字体格式

    }

    /**
     * 创建一个工作页
     * <p>
     *
     * @param name
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 15:05:54
     */
    public void createSheet(String name) {
        sheet = workbook.createSheet(name);
        rowIndex = columnIndex = 0;
    }

    public void clean(Integer startRow) {
        if (sheet != null) {
            clean(startRow, sheet.getLastRowNum());
        }
    }

    public void clean(Integer startRow, Integer endRow) {
        if (sheet != null) {
            for (int i = startRow; i <= endRow; i++) {
                Row row = sheet.getRow(i);
                if(row != null){
                    sheet.removeRow(row);
                }
            }
            rowIndex = startRow;
            columnIndex = 0;
        }
    }

    /**
     * 启一新行
     * <p>
     *
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 15:06:10
     */
    public void newRow() {
        rowIndex = rowIndex + 1;
        columnIndex = 0;
    }

    /**
     * 设置列宽
     * <p>
     *
     * @param widths
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-19 12:14:52
     */
    public void columnWidth(int... widths) {
        for (int i = 0; i < widths.length; i++) {
            System.out.println(widths[i]);
            columnWidth(i, widths[i]);
        }
    }

    public void columnWidth(int columnIndex, int width) {
        sheet.setColumnWidth(columnIndex, width * 256);
    }

    /**
     * 设置表头
     * <p>
     *
     * @param objects
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 15:06:28
     */
    public void header(Object... objects) {
        if (null != objects && objects.length > 0) {
            Row row = sheet.createRow(rowIndex++);
            columnIndex = 0;
            row.setHeight((short) (30 * 15));

            sheet.trackAllColumnsForAutoSizing();

            for (int i = 0; i < objects.length ; i++) {
                setCellValue(row.createCell(columnIndex++), objects[i], headerStyle);
                sheet.autoSizeColumn(i);
                //sheet.setColumnWidth(i, sheet.getColumnWidth(i)*17/10);
            }
        }
    }

    /**
     * 设置标题
     * <p>
     *
     * @param object
     * @param columnNum
     */
    public void title(Object object, int columnNum) {
        if (null != object) {
            Row row = sheet.createRow(rowIndex);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnNum));// 合并单元格
            row.setHeight((short) (40 * 15));
            setCellValue(row.createCell(0), object, titleStyle);
            rowIndex++;
        }
    }

    /**
     * 插入一行
     * <p>
     *
     * @param objects
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 15:06:42
     */
    public void row(Object... objects) {
        if (null != objects && objects.length > 0) {
            Row row = sheet.createRow(rowIndex++);
            columnIndex = 0;
            row.setHeight((short) (30 * 15));
            for (Object object : objects) {
                setCellValue(row.createCell(columnIndex++), object, commonStyle);
            }
        }
    }
    public void row(Collection<Object> objects) {
        if (null != objects && objects.size() > 0) {
            Row row = sheet.createRow(rowIndex++);
            columnIndex = 0;
            row.setHeight((short) (30 * 15));
            for (Object object : objects) {
                setCellValue(row.createCell(columnIndex++), object, commonStyle);
            }
        }
    }
    /**
     * 单独插入一列
     * <p>
     *
     * @param object
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 15:06:50
     */
    public void column(Object object) {
        if (null == object) {
            object = EMPTY_VALUE;
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        setCellValue(row.createCell(columnIndex++), object, commonStyle);
    }

    /**
     * 单独插入一列
     * <p>
     *
     * @param object
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 15:06:50
     */
    public void emphasizeColumn(Object object) {
        if (null == object) {
            object = EMPTY_VALUE;
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        setCellValue(row.createCell(columnIndex++), object, emphasizeStyle);
    }


    /**
     * 单独插入一列
     * <p>
     *
     * @param object
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 15:06:50
     */
    public void column(int columnIndex, Object object) {
        if (null == object) {
            object = EMPTY_VALUE;
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        setCellValue(row.createCell(columnIndex), object, commonStyle);
        this.columnIndex = columnIndex;
    }

    /**
     * 写入文件
     * <p>
     *
     * @throws IOException
     */
    public void write() throws IOException {
        workbook.write(outputStream);
    }

    /**
     * 写入文件
     * <p>
     *
     * @throws IOException
     */
    public void write(File file) throws IOException {
        outputStream = new FileOutputStream(FileUtils.file(file));
        workbook.write(outputStream);
    }

    public void close() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
    }

    private void setCellValue(Cell cell, Object object, CellStyle cellStyle) {
        //sheet.autoSizeColumn(1, true);
        cell.setCellStyle(cellStyle);
        if (null == object) {
            cell.setCellType(CellType.BLANK);
            cell.setCellValue(EMPTY_VALUE);
        } else {
            Class<?> clazz = object.getClass();
            if ((Long.class == clazz) || (long.class == clazz)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue((Long) object);
            } else if ((Integer.class == clazz) || (int.class == clazz)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue((Integer) object);
            } else if ((Float.class == clazz) || (float.class == clazz)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue((Float) object);
            } else if ((Double.class == clazz) || (double.class == clazz)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue((Double) object);
            } else if ((Character.class == clazz) || (char.class == clazz)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue((Character) object);
            } else if ((Boolean.class == clazz) || (boolean.class == clazz)) {
                cell.setCellType(CellType.BOOLEAN);
                cell.setCellValue((Boolean) object);
            } else if (Date.class == clazz) {
                cell.setCellValue(DateFormatUtils.format((Date) object,  DF_FORMAT));
            } else if (LocalDateTime.class == clazz) {
                cell.setCellValue(((LocalDateTime) object).format(DF_YMDHMS));
            } else if (LocalDate.class == clazz) {
                cell.setCellValue(((LocalDate) object).format(DF_YMD));
            } else if (LocalTime.class == clazz) {
                cell.setCellValue(((LocalTime) object).format(DF_HMS));
            } else {
                cell.setCellValue(object.toString());
            }
        }
    }
}
