package com.wisestar.elastic.controller;

import com.wisestar.elastic.entity.ContactRecord;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisestar.elastic.conf.ElasticSearchProperties;
import com.wisestar.elastic.service.ContactRecordService;

/**
* @Class 	MaterialController.java
* @Author 	作者姓名:宋天成
* @Version	1.0
* @Copyright Copyright by 宋天成
* @Direction 类说明 联系记录的es搜索
*/
@RestController
public class ContactRecordController {

	@Autowired
	private ElasticSearchProperties conf ;				// 配置文件，引用配置参数使用
	
	@Autowired
    private ContactRecordService contactRecordService;	// 调用我们的接口与elasticsearch通讯

	 /**
	  *	添加
	  * @author tiancheng
	  * @date 2018/10/25 14:17
	  * @param
	  * @return
	  */
    @PostMapping("save")
	@ResponseBody
    public ResponseEntity save(ContactRecord contactRecord){
    	try {
			ObjectMapper mapper =new ObjectMapper();
			String json  =mapper.writeValueAsString( contactRecord );
			String result = contactRecordService.saveIndex(conf.getEsMainIndex(), conf.getEsMainType(), json);
			if( result == null ) {
				System.out.println( "存储失败 ......" );
			}
			return new ResponseEntity(result, HttpStatus.OK);
		} catch (JsonProcessingException e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
    }

	 /**
	  * 删除
	  * @author tiancheng  
	  * @date 2018/10/25 16:04
	  * @param
	  * @return   
	  */  
    @GetMapping("delete")
    public ResponseEntity delete(String id){
		return contactRecordService.deleteIndexById( conf.getEsMainIndex(), conf.getEsMainType(), id);
    }

    /**
     * 修改
     * @author tiancheng  
     * @date 2018/10/25 16:04
     * @param
     * @return   
     */  
    @GetMapping("update")
    public ResponseEntity update(ContactRecord contactRecord){
    	try {
			ObjectMapper mapper =new ObjectMapper();
			String json = mapper.writeValueAsString( contactRecord );
			contactRecordService.UpdateIndexById( conf.getEsMainIndex(), conf.getEsMainType(), contactRecord.getId() , json);
			return new ResponseEntity(HttpStatus.OK);
		} catch ( Exception e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
    }

    /**
     * 查询
     * @author tiancheng  
     * @date 2018/10/25 16:04
     * @param
     * @return   
     */  
    @GetMapping("getOne")
    public ResponseEntity getOne(String id){
		String json = contactRecordService.getIndexTypeById(conf.getEsMainIndex(), conf.getEsMainType(), id);
		if(null == json){
			return new ResponseEntity("信息未找到！",HttpStatus.OK) ;
		}
    	return new ResponseEntity(json,HttpStatus.OK) ;
    }


	 /**
	  * 复合查询
	  * @author tiancheng  
	  * @date 2018/10/25 16:25
	  * @param
	  * @return   
	  */  
	@PostMapping("query")
    public ResponseEntity query(ContactRecord contactRecord){
		return contactRecordService.getIndexTypeByInfo(conf.getEsMainIndex(), conf.getEsMainType(), contactRecord);
	}
	

}
