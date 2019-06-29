package com.advanceSearch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.advanceSearch.entity.KeyWord;
import com.advanceSearch.entity.SearchItem;
import org.springframework.stereotype.Repository;

/**
 * @Description:  dao操作类
 *
 *
 *
 */
@Repository
@Mapper
public interface AdvanceSearchDao {
	@Insert("insert into searchitem(title,content,url,searchKey) values(#{title},#{content},#{url},#{searchKey})")
	int addSearchItem(SearchItem searchItem);

	//每次准备搜索爬取时先进行关键字判断，如果已存在该关键字，则可以直接在本地查询；若需要重新查询，则更新数据库中已有的数据
	//关键字模糊匹配 内容表  并进行统计  CONCAT('%',',${cityId},','%' )
	@Select("select count(*) from searchitem where title like CONCAT('%',#{key},'%' )")
	List<SearchItem> matchByKey(@Param("key") String key);


	@Select("select * from searchitem where searchKey = #{key}")
	List<SearchItem> findByKey(@Param("key") String key);

	@Select("select * from searchitem where searchKey = #{k}")
	SearchItem findSearchKey(@Param("k") String key);

	//查询所有搜索到的数据
	@Select("select * from searchitem")
	List<SearchItem> findAllSearch();

	//插入关键字
	@Insert("insert into keyword(keyWord) values(#{key})")
	int addKeyWord(KeyWord key);

	//查询所有关键字
	@Select("select * from keyword")
	List<KeyWord> findAllKey();

	//每次准备搜索爬取时先进行关键字判断，如果已存在该关键字，则可以直接在本地查询；若需要重新查询，则更新数据库中已有的数据
	@Select("select * from keyword where keyWord = #{k}")
	KeyWord findKey(@Param("k") String key);

	@Update("update searchitem set title=#{Item.title},content=#{Item.content},url=#{Item.url} where title like CONCAT('%',',#{key},','%' )")
	int updateSearchItem(@Param("Item") SearchItem searchItem,@Param("key") String key);

	//根据关键字删除表中数据
	@Delete("delete from searchitem where searchKey = #{key}")
	int deleteSearchItem(String key);

}
