package com.eb.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * @ClassName CommonUtil
 * @Description TODO
 * @Author
 * @Date 2020/5/3 18:04
 * @Version
 **/
public class CommonUtil {

    /**
     * @return
     * @throws
     * @Title: getCurrentSystemTime
     * @Description: 获取当前指定系统时间, 默认格式为"yyyy-MM-dd HH:mm:ss"(适配mysql)
     */
    public static String getCurrentSystemDate() {
        // java.utils.Date mysql无限制适用,oracle之所以日期格式错误(与插入格式限定有关)
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(currentTime);
    }

    /**
     * @return
     * @throws
     * @Title: getCurrentSystemTime
     * @Description: 获取当前指定系统时间, 默认格式为"yyyy-MM-dd HH:mm:ss"(适配mysql)
     */
    public static String getCurrentSystemTime() {
        // java.utils.Date mysql无限制适用,oracle之所以日期格式错误(与插入格式限定有关)
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(currentTime);
    }

    /**
     * @return java.sql.Timestamp
     * @MethodName getCurrentTimestamp
     * @Description 获当前时间(适配oracle 、 mysql)
     * @Param []
     **/
    public static Timestamp getCurrentTimestamp() {
        Date currentTime = new Date();
        // return new java.sql.Date(currentTime.getTime());// 统计到年月日
        // 统计到时分秒数据
        return new Timestamp(currentTime.getTime());
    }

    /**
     * @return
     * @throws
     * @Title: getCurrentMilliSecondTime
     * @Description: 获取当前系统毫秒时间, 用于性能测试
     */
    public static String getCurrentMilliSecondTime() {
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStr = sdf.format(currentTime);
        long millionSeconds = currentTime.getTime();//毫秒
        return currentTimeStr + "-" + millionSeconds;
    }


    /**
     * @return java.lang.String
     * @MethodName getRandomId
     * @Description 自定义生成id(随机字符串 + 时间戳创建组合Id)
     * @Param []
     **/
    public static String getRandomId() {
        // 获得系统时间时间戳(long装载的时间戳十进制最大有19位)
        long currentMill = System.currentTimeMillis();
        StringBuilder randomId = new StringBuilder(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));
        randomId.append(currentMill);
        return randomId.toString();
    }

    /**
     * 正向解析枚举类型(将标识转化为对应的value)
     **/
    public static String convertByExp(String propertyValue, String converterExp) {
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (itemArray[0].equals(propertyValue)) {
                return itemArray[1];
            }
        }
        return propertyValue;
    }

    /**
     * 反向解析枚举类型(将value转化为对应的标识)
     **/
    public static String reverseByExp(String propertyValue, String converterExp) {
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (itemArray[1].equals(propertyValue)) {
                return itemArray[0];
            }
        }
        return propertyValue;
    }

    /**
     * 处理JAVA程序在不同操作系统下编码格式不同导致的乱码问题(获取系统编码)
     */
    public static String getSystemFileCharset() {
        Properties pro = System.getProperties();
        return pro.getProperty("file.encoding");
    }

    /**
     * 根据当前系统编码格式创建指定参数值
     */
    public static String getStrByCharset(String sourceStr, String targetCharset) {
        String result = null;
        try {
            result = new String(sourceStr.getBytes(getSystemFileCharset()), targetCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据当前系统编码格式创建指定参数值(默认编码格式ISO-8859-1)
     */
    public static String getStrByDefaultCharset(String sourceStr) {
        return getStrByCharset(sourceStr, "ISO-8859-1");
    }


    /**
     * 字符串数据转化:指定源数字字符串,指定整除位数(万、亿),指定保留位数格式
     **/
    public static String transferNum(Object num, String divDigit, String decimalPattern) {
        if (num == null) {
            return "空";
        }
        // 将String类型数据作大数处理
        BigDecimal sourceNum = new BigDecimal(String.valueOf(num));
        // 作亿元单位转化,保留2位小数
        // 转换为万元（除以10000）
        BigDecimal decimal = sourceNum.divide(new BigDecimal(divDigit));
        // 保留两位小数
        DecimalFormat formater = new DecimalFormat(decimalPattern);
        // 四舍五入,保留指定位数小数
        formater.setRoundingMode(RoundingMode.HALF_UP);
//        formater.setRoundingMode(RoundingMode.HALF_DOWN);
//        formater.setRoundingMode(RoundingMode.HALF_EVEN);
        // 格式化完成之后得出结果
        return formater.format(decimal);
    }

    /**
     * 字符串数据转化:如果字符串数据为空输出为空
     **/
    public static String transferStr(String str) {
        return StringUtils.isEmpty(str) ? "/" : str;
    }

    /**
     * Double数据转化:如果字符串数据为空输出为空
     **/
    public static Double transferDouble(Object amount) {
        if (amount == null) {
            return 0.00;
        }
        String amountStr = String.valueOf(amount);
        // Double.parseDouble(historyTrade.getBuyFee().toString())
        return StringUtils.isEmpty(amountStr) ? 0.00 : Double.parseDouble(amountStr);
    }

    /**
     * 日期类数据转化:如果字符串数据为空输出为空,可指定日期格式
     **/
    public static String transferDate(Date date, SimpleDateFormat sdf) {
        return date == null ? "/" : sdf.format(date);
    }

    /**
     * Object对象转化为BigDecimal数据,处理空指针异常
     **/
    public static BigDecimal transferObjToBigDecimal(Object amount) {
        if(amount==null){
            return new BigDecimal("0.00");
        }
        if(StringUtils.isEmpty(String.valueOf(amount))){
            // 获取的数据为nul或空字符串,则返回0
            return new BigDecimal("0.00");
        }
        // 正常返回转化后的BigDecimal数据
        return new BigDecimal(String.valueOf(amount));
    }

    /**
     * 根据指定规则格式化金额数据::指定源数字字符串,指定整除位数(万、亿),指定保留位数格式
     **/
    public static String formatAmtByPattern(Object num, String divDigit, String decimalPattern) {
        if (num == null) {
            return "/";
        }
        // 将String类型数据作大数处理
        BigDecimal sourceNum = CommonUtil.transferObjToBigDecimal(num);
        // 作亿元单位转化,保留2位小数
        // 转换为万元（除以10000）
        BigDecimal decimal = sourceNum.divide(new BigDecimal(divDigit));
        // 保留两位小数
        DecimalFormat formater = new DecimalFormat(decimalPattern);
        // 四舍五入,保留指定位数小数
        formater.setRoundingMode(RoundingMode.HALF_UP);
//        formater.setRoundingMode(RoundingMode.HALF_DOWN);
//        formater.setRoundingMode(RoundingMode.HALF_EVEN);
        // 格式化完成之后得出结果
        return formatAmt(formater.format(decimal));
    }

    /**
     * 金额或数字数据转化:如果字符串数据为空输出为空,默认金额格式
     * 此处只对整数部分做千分位转化处理,不作精度处理
     **/
    public static String formatAmt(Object amount) {
        if (amount == null) {
            return "0.00";
        }
//        NumberFormat nf = new DecimalFormat("###,###");
//        String[] subStr = String.valueOf(amount).split(".");
//        String integerPart = subStr[0];
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append(nf.format(integerPart)).append(".").append(subStr[1]);
//        return stringBuffer.toString();
//        String pattern = "(^[1-9](\d+)?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)";
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(str);
//        System.out.println(m.matches());

        // 将整数部分和小数部分拆开处理
        String amountStr = String.valueOf(amount);
        // 通过小数点进行拆分(小数点特殊符号需要进行转义)
        String[] amountArr = amountStr.split("\\.");

        if (amountArr.length == 1) {
            // 如果只有整数部分直接返回数据
            DecimalFormat df = new DecimalFormat("###,###");
            // 处理空字符串BigDecimal转化异常
            if(StringUtils.isEmpty(amountArr[0])){
                return "0.00";
            }else{
                return df.format(new BigDecimal(amountArr[0]))+".00";
            }
        } else if (amountArr.length == 2) {
            DecimalFormat df1 = new DecimalFormat("###,###");
            BigDecimal integerPart = new BigDecimal(amountArr[0]);
            DecimalFormat df2 = new DecimalFormat("0.00");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(df1.format(integerPart)).append(".");
            stringBuffer.append(amountArr[1]);
            return stringBuffer.toString();
        }
        // return amount==null?"":String.valueOf(amount);
        return "0.00";
    }


    public static void main(String[] args) throws Exception {
        // NumberFormat nf = new DecimalFormat("#,###.####");
        // NumberFormat nf = new DecimalFormat("$,###.##");
        // 如果位数过多会四舍五入导致精度丢失(将整数部分和小数部分拆开处理)
//        NumberFormat nf = new DecimalFormat("###,###.00");
        NumberFormat nf = new DecimalFormat("###,###.##");

        // 将数值转化为BigDecimal进行处理
//        BigDecimal num = new BigDecimal(219438434344478.21);
//        num.setScale(0,BigDecimal.ROUND_CEILING);
//        System.out.println(nf.format(num,BigDecimal.ROUND_HALF_UP));


        // 设定处理
        nf.setRoundingMode(RoundingMode.FLOOR);
        System.out.println(nf.format(219438434344478.21));
        System.out.println(nf.format(8473712237035.01));
        System.out.println(nf.format(549598237534378.61));
        System.out.println(nf.format(043943904342384.31));

        // transferAmt直接输入数值会传入转化为2.xxxxxxEEA这一类的数据导致转化出错
        System.out.println(formatAmt("219438434344478.21"));
        System.out.println(formatAmt("8473712237035.01"));
        System.out.println(formatAmt("549598237534378.61"));
        System.out.println(formatAmt("043943904342384.31"));



//        System.out.println(transferAmt(219438434344478.21));
//        System.out.println(transferAmt(8473712237035.01));
//        System.out.println(transferAmt(549598237534378.61));
//        System.out.println(transferAmt(043943904342384.31));


//        for(int i=0;i<10;i++){
//            System.out.println(getProcInstName("ces","def"));
//        }
    }


}
