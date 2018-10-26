package com.wisestar.elastic.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wisestar.elastic.entity.ContactRecord;
import com.wisestar.elastic.service.ContactRecordService;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
* @Class 	MaterialQuotedServiceImpl.java
* @Author 	作者姓名:宋天成
* @Version	1.0
* @Copyright Copyright by 宋天成
* @Direction 类说明	实际操作
*/
@Service
public class ContactRecordServiceImpl implements ContactRecordService {
	
	@Autowired
	private TransportClient client;

	/**
	 * 查询返回String
	 * @param indexname 主索引
	 * @param type 主索引的type
	 * @param id
     * @return
     */
	public String getIndexTypeById( String indexname ,String type ,String id){
		GetResponse response = client.prepareGet( indexname, type , id ).get();
		return response.getSourceAsString();
	}


	/**
	 * 复合查询
	 * @param indexname 主索引
	 * @param type 主索引的type
	 * @param contactRecord 信息
     * @return
     */
	public ResponseEntity getIndexTypeByInfo(String indexname , String type , ContactRecord contactRecord){
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		//查询客户姓名
		if(contactRecord.getCustomerName().isEmpty()){
			boolQuery.must(QueryBuilders.matchQuery("customerName",contactRecord.getCustomerName()));
		}
		//查询沟通状态
		if(contactRecord.getGotState().isEmpty()){
			boolQuery.must(QueryBuilders.matchQuery("gotState",contactRecord.getGotState()));
		}
		//分段查询
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("");
		//开始时间不为空
		if(null != contactRecord.getYuTimeStart()){
			rangeQuery.from(contactRecord.getYuTimeStart());
		}
		//结束时间不为空
		if(null != contactRecord.getYuTimeEnd()){
			rangeQuery.to(contactRecord.getYuTimeEnd());
		}

		boolQuery.filter(rangeQuery);

		SearchRequestBuilder builder = client.prepareSearch(indexname)
				.setTypes(type)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuery)
				.setFrom(0)
				.setSize(10);
		SearchResponse response = builder.get();
		List<Map<String,Object>> result = new ArrayList<>();

		for (SearchHit hit:response.getHits()) {
			result.add(hit.getSource());
		}
		return new ResponseEntity(result, HttpStatus.OK);
	}

	/***
	 * 保存文档
	 */
	public String saveIndex( String indexname ,String type , String json ) {
		try {
			IndexResponse response = client.prepareIndex(indexname , type ).setSource(json).get();
			//创建成功 反会的状态码是201
			if(response.status().getStatus()==201){
				return response.getId();
			}
		} catch (Exception e) {
			System.out.println( "不符合索引的数据存储，数据源为：" + json );
		}
		return null ;
	}

	/****
	 * 查询返回Map
	 */
	public Map<String,Object> getInfoByIndex( String indexname ,String type , String id ) {
		GetResponse response = client.prepareGet( indexname , type , id ).get();
		return response.getSource();
	}
	
	/***
	 * 删除文档
	 */
	public ResponseEntity deleteIndexById( String indexname ,String type , String id ) {
		DeleteResponse response = client.prepareDelete(  indexname , type , id ).get();
		if(response.status().getStatus()==200){
			return new ResponseEntity("删除成功！",HttpStatus.OK);
		}
		return new ResponseEntity("删除失败！",HttpStatus.NOT_FOUND);
	}
	
	/***
	 * 跟新文档
	 */
	public boolean UpdateIndexById( String indexname ,String type , String id , String doc ) throws Exception{
		UpdateRequest updateRequest = new UpdateRequest(indexname, type, id).doc(doc);
		UpdateResponse response = client.update(updateRequest).get();
		if (response.status().getStatus() == 200) {
			return true;
		}
		return false;
	}
	
}
