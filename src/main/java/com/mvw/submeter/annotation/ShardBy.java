package com.mvw.submeter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记那个按照那个key散列
 * 
 * 分片key配置:
 * 参数按照mybatis的标准取: 
 * 		$1表示第一个(注意，下标是从1开始的奥)   
 * 		$name表示按照名称 
 *  	$num/name.attribute表示取属性
 * 	#场景分析
 *  1.单个常量如何获取       $1
 * 	2.单个pojo对象如何获取     $1.attribute
 * 	3.多个参数(常量，对象)如何获取 $n.attribute
 * 	4.符合kay(即多层次散列)  $m.attribute_$n.attribute【暂不考虑】
 * 	5.属性是否得支持多级别   $n.attribute.attribute【暂不考虑】
 * 
 * #取第一个参数值
 * xxx.
 * xxx
 * 空
 * 
 * #xxx.id取参数(xxx是序号或名称)的属性ID
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface ShardBy {
	String value();
}
