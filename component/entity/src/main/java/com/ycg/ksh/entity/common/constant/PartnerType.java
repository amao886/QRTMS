package com.ycg.ksh.entity.common.constant;

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CoreConstants;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/24
 */
public enum PartnerType {
        //1:发货方,2:收货方,3:承运方
        SHIPPER(1, "发货方") {
                @Override
                public Collection<Integer> abnormals() {
                        return Stream.of(4, 5, 6, 7).collect(Collectors.toList());
                }
        }, RECEIVE(2, "收货方") {
                @Override
                public Collection<Integer> abnormals() {
                        return Stream.of(2, 3, 6, 7).collect(Collectors.toList());
                }
        }, CONVEY(3, "承运方") {
                @Override
                public Collection<Integer> abnormals() {
                        return Stream.of(1, 3, 5, 7).collect(Collectors.toList());
                }
        };

        private int code;
        private String desc;

        private PartnerType(int code, String desc) {
                this.code = code;
                this.desc = desc;
        }

        public final static PartnerType convert(Integer code) {
                if (code != null) {
                        for (PartnerType fettle : values()) {
                                if (fettle.code - code == 0) {
                                        return fettle;
                                }
                        }
                }
                return PartnerType.SHIPPER;
        }

        //用户身份标识(1:司机,2:发货发,3:承运方,4:收货方)
        public static PartnerType identityKey(Integer code) {
                if (code - CoreConstants.USER_CATEGORY_SHIPPER == 0) {
                        return SHIPPER;
                }
                if (code - CoreConstants.USER_CATEGORY_RECEIVE == 0) {
                        return RECEIVE;
                }
                if (code - CoreConstants.USER_CATEGORY_CONVEY == 0) {
                        return CONVEY;
                }
                return PartnerType.SHIPPER;
        }

        public abstract Collection<Integer> abnormals();

        public boolean registered(int type) {
                return arrays(type)[code - 1] == 0;
        }

        /*
        Y:注册企业   N:客户关联
        发货方[Y] 收货方[Y] 承运方[Y] -------------> 0
        发货方[N] 收货方[N] 承运方[N] -------------> 7
        发货方[Y] 收货方[Y] 承运方[N] -------------> 1
        发货方[Y] 收货方[N] 承运方[Y] -------------> 2
        发货方[N] 收货方[Y] 承运方[Y] -------------> 4
        发货方[N] 收货方[N] 承运方[Y] -------------> 6
        发货方[N] 收货方[Y] 承运方[N] -------------> 5
        发货方[Y] 收货方[N] 承运方[N] -------------> 3
        */
        public int clientType(int type, boolean reg) {
                Integer[] binarys = arrays(type);
                binarys[code - 1] = reg ? 0 : 1;
                String svalue = Arrays.stream(binarys).map(i -> i.toString()).reduce("", String::concat);
                return Integer.parseInt(svalue, 2);
        }

        private Integer[] arrays(int type) {
                Integer[] binarys = new Integer[]{0, 0, 0};
                String str = Integer.toBinaryString(type);
                if (StringUtils.isNotBlank(str)) {
                        Integer[] chars = Stream.of(str.split("")).map(c -> Integer.parseInt(c)).toArray(Integer[]::new);
                        System.arraycopy(chars, 0, binarys, binarys.length - chars.length, chars.length);
                }
                return binarys;
        }

        public int getCode() {
                return code;
        }

        public String getDesc() {
                return desc;
        }

        public boolean shipper() {
                return SHIPPER == this;
        }

        public boolean receive() {
                return RECEIVE == this;
        }

        public boolean convey() {
                return CONVEY == this;
        }

        public static void main(String[] args) {
                //System.out.println(SHIPPER.clientType(0, true));
                //System.out.println(SHIPPER.clientType(0, false));

                //System.out.println(Integer.toBinaryString(2));


                System.out.println("Y:注册企业   N:客户关联");

                System.out.println("发货方[Y] 收货方[Y] 承运方[Y] -------------> " + Integer.parseInt("000", 2));
                System.out.println("发货方[N] 收货方[N] 承运方[N] -------------> " + Integer.parseInt("111", 2));

                System.out.println("发货方[Y] 收货方[Y] 承运方[N] -------------> " + Integer.parseInt("001", 2));
                System.out.println("发货方[Y] 收货方[N] 承运方[Y] -------------> " + Integer.parseInt("010", 2));
                System.out.println("发货方[N] 收货方[Y] 承运方[Y] -------------> " + Integer.parseInt("100", 2));

                System.out.println("发货方[N] 收货方[N] 承运方[Y] -------------> " + Integer.parseInt("110", 2));
                System.out.println("发货方[N] 收货方[Y] 承运方[N] -------------> " + Integer.parseInt("101", 2));
                System.out.println("发货方[Y] 收货方[N] 承运方[N] -------------> " + Integer.parseInt("011", 2));
        }
}
