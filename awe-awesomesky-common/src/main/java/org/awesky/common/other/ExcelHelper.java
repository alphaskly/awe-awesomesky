package org.awesky.common.other;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author ybb
 */
public class ExcelHelper {
    
    private static ExcelHelper instance = new  ExcelHelper();
    
    public static ExcelHelper getInstance() {
        if(instance == null)
            instance = new ExcelHelper();
        return instance;
    }
    
    //创建excel
    public boolean createExcel() {
        FileOutputStream output = null;
        try {
            //创建HSSFWorkbook对象
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建HSSFSheet对象
            HSSFSheet sheet = wb.createSheet("Test");
            //创建HSSFRow对象
            HSSFRow row = sheet.createRow(0);
            //创建HSSFCell对象
            HSSFCell cell=row.createCell(0);
            //设置单元格的值
            cell.setCellValue("TEST");
            //输出Excel文件
            output = new FileOutputStream("d:\\workbook.xls");
            wb.write(output);
            output.flush();
        } catch (FileNotFoundException ex) { } catch (IOException ex) { } finally {
            try {output.close();  } catch (IOException ex) { }
        }
        return true;
    }

    /*
    //导出excel
    public boolean exportExcel(List<Customer> users, String path) {
        OutputStream output = null;
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("日志");
            //在sheet里创建行（0～65535）
            HSSFRow row1 = sheet.createRow(0);
            //创建单元格（0～255）
            HSSFCell cell = row1.createCell(0);
            //设置单元格内容
            cell.setCellValue("2017-04-21操作记录");
            //合并单元格 参数：（起始行，截至行，起始列， 截至列）
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
            HSSFRow row2 = sheet.createRow(1);
            //创建单元格并设置单元格内容
            row2.createCell(0).setCellValue("编号");
            row2.createCell(1).setCellValue("账号");
            row2.createCell(2).setCellValue("名字");
            row2.createCell(3).setCellValue("性别");
            row2.createCell(4).setCellValue("年龄");
            row2.createCell(5).setCellValue("生日");
            row2.createCell(6).setCellValue("账号密码");
            row2.createCell(7).setCellValue("联系方式");
            row2.createCell(8).setCellValue("邮箱");
            row2.createCell(9).setCellValue("加入时间");
            row2.createCell(10).setCellValue("签名");
            HSSFCellStyle cellStyle = wb.createCellStyle();
         //   cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));  //yyyy-MM-dd
            for(int i=0;i<users.size();i++) {
                Customer user = users.get(i);
                HSSFRow row = sheet.createRow(2+i);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getAccount());
                row.createCell(2).setCellValue(user.getName());
                row.createCell(3).setCellValue(user.getSex());
                row.createCell(4).setCellValue(user.getAge());
                row.createCell(5).setCellValue(user.getBirth());
                row.createCell(6).setCellValue(user.getPassword());
       //         row.getCell(3).setCellStyle(cellStyle);
                row.createCell(7).setCellValue(user.getPhone());
                row.createCell(8).setCellValue(user.getEmail());
                row.createCell(9).setCellValue(user.getJoinTime());
                row.createCell(10).setCellValue(user.getSignature());
            }
            //输出Excel文件
            output = new FileOutputStream(path+".xls");
            wb.write(output);
            output.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false; 
        } finally {
            try { output.close(); } catch (IOException ex) {  }
        }
        
    }
    
     //导入
    public static List<Customer> enterExcel(String xlsPath) {
        FileInputStream fileIn = null;
        List<Customer> temp = null;
        try {
            temp = new ArrayList<Customer>();
            fileIn = new FileInputStream(xlsPath);
            //根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb = new HSSFWorkbook(fileIn);
            //获取Excel文档中的第一个表单
            Sheet sht = wb.getSheetAt(0);
            //对Sheet中的每一行进行迭代
            for (Row r : sht) {
                if(r.getRowNum()<2)continue;
                Customer user = new Customer();
                user.setId((int) r.getCell(0).getNumericCellValue());
                user.setAccount(r.getCell(1).getStringCellValue());
                user.setName(r.getCell(2).getStringCellValue());
                temp.add(user);
            }   
        } catch (FileNotFoundException ex) {  } catch (IOException ex) {  } finally {
            try {fileIn.close(); } catch (IOException ex) { }
        }
        return temp;
    }
    */
    
}
