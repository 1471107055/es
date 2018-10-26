package com.wisestar.elastic.service;

import com.wisestar.elastic.entity.ContactRecord;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/***
 * 
 * @Class 	ContactRecordService.java
 * @Author 	作者姓名:宋天成
 * @Version	1.0
 * @Copyright Copyright by 宋天成
 * @Direction 类说明	 提供调用elasticsearch的接口服务类
 */
public interface ContactRecordService {
	
	String getIndexTypeById(String indexname ,String type , String id);

	String saveIndex(String indexname ,String type , String json ) ;

	Map<String,Object> getInfoByIndex(String indexname ,String type , String id ) ;

	ResponseEntity deleteIndexById(String indexname ,String type , String id ) ;
	
	boolean UpdateIndexById(String indexname ,String type , String id , String doc ) throws Exception ;

	ResponseEntity getIndexTypeByInfo(String indexname , String type ,ContactRecord contactRecord);
}
