package shindo.Java.jdbc;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class CreateCardNumber {

    // 西洋街卡片编码前缀
    private static final String[] XYJ_PREFIX_LIST = new String[] { "6" };

    // 制卡批次号
    private static final String[] BATCH_PREFIX_LIST = new String[] { "001" };

    /**
     * 序列倒置
     * @param str
     * @return
     */
    private static String strrev(String str) {
        if (str == null) {
            return "";
        }
        String revstr = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            revstr += str.charAt(i);
        }
        return revstr;
    }

    /**
     * 生产卡号
     * @param prefix
     * @param length
     * @return
     */
    private static String completed_number(String prefix, int length) {
        String ccnumber = prefix;
        // 产生卡序列
        while (ccnumber.length() < (length - 1)) {
            ccnumber += new Double(Math.floor(Math.random() * 10)).intValue();
        }
        // 数据转换
        String reversedCCnumberString = strrev(ccnumber);

        List<Integer> reversedCCnumberList = new Vector<Integer>();
        for (int i = 0; i < reversedCCnumberString.length(); i++) {
            reversedCCnumberList.add(new Integer(String.valueOf(reversedCCnumberString.charAt(i))));
        }
        // 计算总合数
        int sum = 0;
        int pos = 0;
        Integer[] reversedCCnumber = reversedCCnumberList.toArray(new Integer[reversedCCnumberList.size()]);
        while (pos < length - 1) {
            int odd = reversedCCnumber[pos] * 2;
            if (odd > 9) {
                odd -= 9;
            }
            sum += odd;
            if (pos != (length - 2)) {
                sum += reversedCCnumber[pos + 1];
            }
            pos += 2;
        }
        // 添加数字校验位
        int checkdigit = new Double(((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
        ccnumber += checkdigit;
        return ccnumber;
    }

    /**
     * 随机获取标示位
     * @param xyjPrefixList
     * @param batchPrefixList
     * @param length
     * @param howMany
     * @return
     */
    private static String[] credit_card_number(String[] xyjPrefixList, String[] batchPrefixList, int length, int howMany) {
        Stack<String> result = new Stack<String>();
        for (int i = 0; i < howMany; i++) {
            // 随机取西洋街卡片编码前缀
            int xyjRandomArrayIndex = (int) Math.floor(Math.random() * xyjPrefixList.length);
            int batchRandomArrayIndex = (int) Math.floor(Math.random() * batchPrefixList.length);
            String ccnumber = xyjPrefixList[xyjRandomArrayIndex] + batchPrefixList[batchRandomArrayIndex];
            result.push(completed_number(ccnumber, length));
        }

        return result.toArray(new String[result.size()]);
    }

    public static String[] generateXyjCardNumbers(int howMany) {
        return credit_card_number(XYJ_PREFIX_LIST, BATCH_PREFIX_LIST, 16, howMany);
    }

    /**
     * 校验卡位
     * @param creditCardNumber
     * @return
     */
    private static boolean isValidCreditCardNumber(String creditCardNumber) {
        boolean isValid = false;
        try {
            String reversedNumber = new StringBuffer(creditCardNumber).reverse().toString();
            int mod10Count = 0;
            for (int i = 0; i < reversedNumber.length(); i++) {
                int augend = Integer.parseInt(String.valueOf(reversedNumber.charAt(i)));
                if (((i + 1) % 2) == 0) {
                    String productString = String.valueOf(augend * 2);
                    augend = 0;
                    for (int j = 0; j < productString.length(); j++) {
                        augend += Integer.parseInt(String.valueOf(productString.charAt(j)));
                    }
                }
                mod10Count += augend;
            }
            if ((mod10Count % 10) == 0) {
                isValid = true;
            }
        } catch (NumberFormatException e) {
            System.err.println("Data verification error.");
        }

        return isValid;
    }

    public static void main(String[] args) {
        int howMany = 0;
        try {
            howMany = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Usage error. You need to supply a numeric argument");
        }
        String[] creditcardnumbers = generateXyjCardNumbers(howMany);
        for (int i = 0; i < creditcardnumbers.length; i++) {
            System.out
                    .println(creditcardnumbers[i] + "," + (isValidCreditCardNumber(creditcardnumbers[i]) ? "valid" : "invalid") + "," + "2016/12/31");

        }
    }
}
