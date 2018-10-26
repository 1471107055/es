package com.wisestar.elastic.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
* @Class 	ElasticSearchProperties.java
* @Version	1.0
* @Copyright Copyright by 宋天成
* @Direction 类说明	多个索引建立等信息到时候全部读取配置文件
*/
@Component
@Configuration
@Data
@PropertySource("classpath:elasticsearch.properties")
public class ElasticSearchProperties {

	
	@Value("${es.main.index}")
	private String esMainIndex;			// 主索引【可能我们会建立多张索引库】
	
	@Value("${es.main.type}")
	private String esMainType;			// 主索引的type

}
