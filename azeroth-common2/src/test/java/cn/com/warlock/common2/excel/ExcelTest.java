package cn.com.warlock.common2.excel;

import java.util.List;

import cn.com.warlock.common2.excel.convert.ExcelConvertCSVReader;

public class ExcelTest {

    /**
     * @param args
     */
    public static void main(String[] args) {

        List<PersonSalaryInfo> list = ExcelConvertCSVReader.read("/Users/warlock/Desktop/test.xls",
            PersonSalaryInfo.class);

        System.out.println(list);
    }

}
