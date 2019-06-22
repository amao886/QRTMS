package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */

import com.ycg.ksh.common.util.NumberToCNUtils;
import com.ycg.ksh.constant.OrderRoleType;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.enterprise.AbstractOrder;
import com.ycg.ksh.entity.persistent.enterprise.Complaint;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * 订单联盟
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */
public class OrderAlliance extends Order {

    private CustomerConcise shipper;//发货方
    private CustomerConcise receive;//收货方
    private CustomerConcise convey;//承运方

    private Collection<OrderCommodity> commodities;//货物信息
    private Collection<ImageStorage> imageStorages; //回单图片
    private Collection<LocationTrack> locations; //位置信息
    private Collection<CustomData> customDatas;//自定义数据
    private OperateNote operateNote;//日志信息
    private OrderException exception;//异常
    private OrderExtra extra;//订单额外信息
    private Complaint complaint;//投诉信息

    /**
     * 电子回单状态
     **/
    private Integer receiptFettle = 0;

    private Integer count;//货物数
    private Integer quantity;//数量(件)
    private Integer boxCount;//箱数(箱)
    private Double volume;//体积(立方米)
    private Double weight;//重量(千克)
    private String realNumber;//实收件数 大写
    /**
     * 分享信息
     */
    private String shareName;//分享信息

    /**
     * 签署信息
     **/
    private Integer isUserType;//判断是收货方还是承运方
    private boolean isSign; //是否签署


    /**
     * 权限
     **/
    private boolean allowReceive;//是否有确认收货的权限

    /**
     * 作废操作人编号
     */
    private Integer invalidUserId;
    /**
     * 作废时间
     */
    private Date invalidTime;

    public OrderAlliance() {
        count = quantity = boxCount = 0;
        volume = weight = 0D;
    }

    public static OrderAlliance buildAlliance(AbstractOrder object) throws Exception {
        if (object instanceof OrderAlliance) {
            return (OrderAlliance) object;
        }
        if (object instanceof ObsoleteOrder) {
            return new OrderAlliance((ObsoleteOrder) object);
        }
        if(object instanceof Order){
            return new OrderAlliance((Order) object);
        }
        return new OrderAlliance(object);
    }
    public OrderAlliance(AbstractOrder order) throws Exception {
        this();
        BeanUtils.copyProperties(this, order);
    }

    public OrderAlliance(Order order) throws Exception {
        this();
        BeanUtils.copyProperties(this, order);
    }

    public OrderAlliance(ObsoleteOrder order) throws Exception {
        this();
        BeanUtils.copyProperties(this, order);
    }


    public boolean isCreateYourself(){
        if(isUserType != null && isUserType > 0){
            OrderRoleType roleType = OrderRoleType.convert(isUserType);
            PartnerType partnerType = PartnerType.convert(insertType);
            if(roleType.isShipper() && partnerType.shipper()){
                return true;
            }
            if(roleType.isReceive() && partnerType.receive()){
                return true;
            }
            if(roleType.isConvey() && partnerType.convey()){
                return true;
            }
        }
        return false;
    }


    public OrderRoleType signRoleType(Long companyKey) {
        if (companyKey - shipperId == 0) {
            return OrderRoleType.SHIPPER;
        }
        if (companyKey - receiveId == 0) {
            return OrderRoleType.RECEIVE;
        }
        if (companyKey - conveyId == 0) {
            return OrderRoleType.CONVEY;
        }
        return null;
    }

    /*public Order getOrder(){
        Order order = new Order();
        try{
            BeanUtils.copyProperties(order, this);
        }catch (Exception e){}
        return order;
    }*/

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public OperateNote getOperateNote() {
        return operateNote;
    }

    public void setOperateNote(OperateNote operateNote) {
        this.operateNote = operateNote;
    }

    public Integer getIsUserType() {
        return isUserType;
    }

    public void setIsUserType(Integer isUserType) {
        this.isUserType = isUserType;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public CustomerConcise getByRole(OrderRoleType roleType) {
        return roleType.isConvey() ? getConvey() : roleType.isReceive() ? getReceive() : getShipper();
    }

    public String getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(String realNumber) {
        this.realNumber = realNumber;
    }

    public Collection<ImageStorage> getImageStorages() {
        return imageStorages;
    }

    public void setImageStorages(Collection<ImageStorage> imageStorages) {
        this.imageStorages = imageStorages;
    }

    public CustomerConcise getShipper() {
        return shipper;
    }

    public void setShipper(CustomerConcise shipper) {
        this.shipper = shipper;
    }

    public CustomerConcise getReceive() {
        return receive;
    }

    public void setReceive(CustomerConcise receive) {
        this.receive = receive;
    }

    public CustomerConcise getConvey() {
        return convey;
    }

    public void setConvey(CustomerConcise convey) {
        this.convey = convey;
    }

    public Collection<OrderCommodity> getCommodities() {
        return commodities;
    }

    public void addCommodity(OrderCommodity commodity) {
        Collection<OrderCommodity> collection = Optional.ofNullable(commodities).orElseGet(() -> {
            commodities = new ArrayList<OrderCommodity>();
            return commodities;
        });
        if (commodity.getBoxCount() != null) {
            setBoxCount(boxCount + commodity.getBoxCount());
        }
        if (commodity.getQuantity() != null) {
            setQuantity(quantity + commodity.getQuantity());
        }
        if (commodity.getVolume() != null) {
            setVolume(volume + commodity.getVolume());
        }
        if (commodity.getWeight() != null) {
            setWeight(weight + commodity.getWeight());
        }
        if (getQuantity() != null) {
            setRealNumber(NumberToCNUtils.CNValueOf(getQuantity().toString()));
        }
        collection.add(commodity);
        setCount(collection.size());
    }

    public void setCommodities(Collection<OrderCommodity> commodities) {
        if (CollectionUtils.isNotEmpty(commodities)) {
            for (OrderCommodity commodity : commodities) {
                addCommodity(commodity);
            }
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(Integer boxCount) {
        this.boxCount = boxCount;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if (null != weight && weight > 0) {
            this.weight = NumberToCNUtils.formatDecimal(weight);
        } else {
            this.weight = weight;
        }
    }

    public OrderException getException() {
        return exception;
    }

    public void setException(OrderException exception) {
        this.exception = exception;
    }

    public Integer getInvalidUserId() {
        return invalidUserId;
    }

    public void setInvalidUserId(Integer invalidUserId) {
        this.invalidUserId = invalidUserId;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    public boolean isAllowReceive() {
        return allowReceive;
    }

    public void setAllowReceive(boolean allowReceive) {
        this.allowReceive = allowReceive;
    }

    public Integer getReceiptFettle() {
        return receiptFettle;
    }

    public void setReceiptFettle(Integer receiptFettle) {
        this.receiptFettle = Optional.ofNullable(receiptFettle).orElse(0);
    }

    public Collection<LocationTrack> getLocations() {
        return locations;
    }

    public void setLocations(Collection<LocationTrack> locations) {
        this.locations = locations;
    }


    public Collection<CustomData> getCustomDatas() {
        return customDatas;
    }

    public void setCustomDatas(Collection<CustomData> customDatas) {
        this.customDatas = customDatas;
    }

    public OrderExtra getExtra() {
        return extra;
    }

    public void setExtra(OrderExtra extra) {
        this.extra = extra;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }
}
