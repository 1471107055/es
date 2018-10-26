package com.wisestar.elastic.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* @Class 	CompanyQuotedPrice.java
* @Author 	作者姓名:宋天成
* @Version	1.0
* @Copyright Copyright by 宋天成
* @Direction 类说明	联系记录数据承载对象
*/
@Data
public class ContactRecord  implements Serializable {

    private String id;

    private Date yuTime;  //预约时间

    private String customerId;  //客户id

    private String customerName;  //客户姓名

    private String content; //联系内容

    private String no; // 手机号

    private String gotState; // 联系状态

    private Date yuTimeStart;  //预约开始时间

    private Date yuTimeEnd;  //预约结束时间

}
