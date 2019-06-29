package com.advanceSearch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advanceSearch.dao.AdvanceSearchDao;
import com.advanceSearch.entity.KeyWord;
import com.advanceSearch.entity.SearchItem;

/**
 * @Description: 高级搜索Service类
 * @author hjd
 * @date 2017年1月19日 下午1:39:37
 *
 */
@Service
public class AdvanceSearchService {

	@Autowired
	private AdvanceSearchDao advanceSearchDao;

	//存储搜索内容实体
	public int addSearchItem(SearchItem searchItem){
		int addcount = advanceSearchDao.addSearchItem(searchItem);
		return addcount;
	}

	public List<SearchItem> findAllSearch(){
		List<SearchItem> list = new ArrayList<SearchItem>();
		list = advanceSearchDao.findAllSearch();
		return list;
	}

	//存储关键字
	public int addKeyWord(KeyWord key){
		int addcount = advanceSearchDao.addKeyWord(key);
		return addcount;
	}

	//查询所有关键字
	public List<KeyWord> findAllKey(){
		List<KeyWord> keys = new ArrayList<KeyWord>();
		keys = advanceSearchDao.findAllKey();
		return keys;
	}

	/**
	 * 关键字查询
	 * @param key
	 * @return
	 */
	public List<SearchItem> query(String key){
		List<SearchItem> list = new ArrayList<SearchItem>();
//		int flag =1;
//		if(flag != judgeKey(key)){
//			return list;
//		}
		list = advanceSearchDao.findByKey(key);
		return list;
	}

	//判断关键字是否已存在
	public int judgeKey(String key){
//		List<KeyWord> list = new ArrayList<KeyWord>();
		KeyWord keyword = new KeyWord();
		int flag = 1;
		keyword = advanceSearchDao.findKey(key);
		if(keyword!=null){
			flag=0;
			return flag; //flag为0，说明该关键字已存在
		}
		return flag;
	}

	//从keyword表中查找关键字
	public KeyWord findKey(String key){
		KeyWord keyword = new KeyWord();
		keyword = advanceSearchDao.findKey(key);
		return keyword;
	}

	//删除数据
	public int deleteSearchItem(String key){
		int deletecount = advanceSearchDao.deleteSearchItem(key);
		return deletecount;

	}

}
