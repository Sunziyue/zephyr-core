package xyz.szy.zephyr.core.api.utils.sale;


import xyz.szy.common.utils.Arguments;

import java.math.BigDecimal;

/**
 * @author Sunziyue
 * @date 2020-09-17 14:23
 * @apiNote HDT 工具类
 */
public class SaleUtils {

    public static final Integer ZERO = 0;

    public static final Integer ONE = 1;

    public static final Integer MINUS_ONE = -1;

    public static final Short SHORT_ZERO = 0;

    public static final String ZERO_STR = "0";

    public static final String ONE_STR = "1";

    public static final String EMPTY_STR = "";

    public static final String NULL = null;

    public static final BigDecimal ZERO_DECIMAL = BigDecimal.ZERO;

    public static final BigDecimal ONE_DECIMAL = BigDecimal.ONE;

    /**
     * 生成验证码
     *
     * @param code
     * @return
     */
    public static String createEinvoiceCode(String code) throws Exception {
        String result = null;
        if (Arguments.notEmpty(code) && code.length() > 6) {
            String lastEightStr = code.substring(code.length() - 8);
            String excludeLastTwoDigitsStr = lastEightStr.substring(0, 6);
            if (Arguments.isNumberic(excludeLastTwoDigitsStr)) {
                int num = Integer.parseInt(excludeLastTwoDigitsStr);
                String salt = "00" + ((num) + 1974);
                result = salt.substring(salt.length() - 6);
            } else {
                throw new Exception("订单号: [ " + code + " ] 生成验证码失败, 不是数字");
            }
        } else {
            throw new Exception("订单号: [ " + code + " ] 生成验证码失败, 订单号为空或位数不够");
        }
        return result;
    }
}
