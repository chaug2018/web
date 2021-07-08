package com.yzj.ebs.service.utils;

/**
 * 创建于:2020-05-29<br>
 * 版权所有(C) 2020 深圳市银之杰科技股份有限公司<br>
 * 金额工具类
 *
 * @author dupeng
 * @version 1.0
 */
public class Credit {
    /**
     * 金额转换
     * 
     * @param focus
     *            true为获得焦点 false为失去焦点
     * @param credit
     *            返回金额但如果发生错误时会返回原输入的金额不做任修改
     * @return 经过处理的金额映射，可用的键有且仅有两个：OLD和NEW <br>
     *         OLD保存录入的值，NEW保存经过修正的值<br>
     *         当focus参数为true时,会去掉小数点和逗号<br>
     *         例如:1000.00转换为100000<br>
     *         当focus参数为false时,会在没有小数点和逗号的数字上加上小数点<br>
     *         例如:100000转换为1000.00,1转换为0.01<br>
     * 
     */
    public static String creditFormat(boolean focus, String credit) {

        String returnCredit = "";
        // 判断输入的是否为空
        if (null == credit || "".equals(credit)) {
            return "0";
        }
        // 去除千分位和小数点
        if (focus == true) {
            returnCredit = credit.replace(".", "");
            returnCredit = String.valueOf(Long.valueOf(returnCredit));
            // 将金额加上千分位和小数点
        } else {
            int creditLength = 0;
            String tmpCredit = "";
            // 取得金额总长度
            creditLength = credit.length();
            // 取得小数点在第几位
            int decimal = credit.lastIndexOf(".");
            if (decimal == -1) {
                // 判断总长度等于1的时候
                if (creditLength == 1) {
                    // 转换成.0+?
                    returnCredit = ".0" + credit;
                    tmpCredit = "0";
                } else if (creditLength == 2) {
                    // 转换成.+??
                    returnCredit = "." + credit;
                    tmpCredit = "0";
                } else {
                    // 取得两位为角和分
                    returnCredit = "." + credit.substring(creditLength - 2);
                    tmpCredit = credit.substring(0, creditLength - 2);
                }
                returnCredit = tmpCredit + returnCredit;
            } else {
                // 取得小数点后的位数
                returnCredit = credit.substring(decimal);
                // 判断小数点后的长度
                if (credit.substring(0, decimal).lastIndexOf(".") != -1) {
                    return credit;
                } else if (returnCredit.length() > 3) {
                    return credit;
                } else {
                    returnCredit = returnCredit.substring(1);
                    // 格式化小数点后的字符
                    for (int i = returnCredit.length(); i < 2; i++) {
                        returnCredit = returnCredit + "0";
                    }
                    returnCredit = "." + returnCredit;
                    // 得取小数点前的所有字符
                    // 如果小数点前没有数字,则自动加0;
                    if (decimal == 0) {
                        tmpCredit = "0";
                    } else {
                        tmpCredit = credit.substring(0, decimal);
                    }
                    returnCredit = tmpCredit + returnCredit;
                }
            }
        }
        return returnCredit;
    }

    public static void main(String[] args) {
        System.out.println(creditFormat(true, "323233239.87"));
    }
}
