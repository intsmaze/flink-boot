package com.intsmaze.flink.validate.bean;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class FlowValidateData {

    private String uuid;
    @Size(min = 7, max = 20, message = "长度必须在{min}-{max}之间")
    private String subTestItem;
    @NotBlank(message = "billNumber不能为空")
    private String billNumber;

    @NotBlank(message = "barcode不能为空")
    private String barcode;

    private String flowName;

    private String flowStatus;

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSubTestItem() {
        return subTestItem;
    }

    public void setSubTestItem(String subTestItem) {
        this.subTestItem = subTestItem;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}
