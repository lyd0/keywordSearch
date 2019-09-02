package com.advanceSearch.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.advanceSearch.utils.MyThreadPool;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.advanceSearch.entity.KeyWord;
import com.advanceSearch.entity.SearchItem;
import com.advanceSearch.utils.Config;
import com.advanceSearch.utils.HttpUtils;
import com.advanceSearch.utils.Rule;

import org.jsoup.Jsoup;

/**
 *
 */
@Service
public class SpiderSearch {
	@Bean
	public MyThreadPool getMyThreadPool() {
		return new MyThreadPool(40);
	}
	@Autowired
	private MyThreadPool myThreadPool;
	@Autowired
	private AdvanceSearchService advanceSearchService;


	/**
	 * 仅根据关键字进行爬取并直接返回相关数据，然后将数据存入数据，以便后续查询
	 * @param keyWord 关键字
	 * @return
	 */
	public List<SearchItem> searchByKey(String keyWord) throws UnsupportedEncodingException{
		String[] enables=Config.defaultRule.enables.toArray(new String[Config.defaultRule.enables.size()]);
//    	String[] enables = {"bing"};
		int pageTotalNum =1;
		return searchAndStore(keyWord, pageTotalNum, enables);
	}

	/**
	 *
	 * @param keyword 关键字
	 * @param pageTotalNum 搜索引擎返回的页码数
	 * @param engineNames 搜索引擎，，目前有bing, baidu
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<SearchItem> searchAndStore(String keyword,int pageTotalNum,String... engineNames) throws UnsupportedEncodingException {
		List<SearchItem> searchItems = new ArrayList<SearchItem>();
		SearchItem searchItem = new SearchItem();



//		searchItems.addAll(doSearchAndStore(keyword, pageTotalNum, "bing"));
//		for(String engineName:engineNames){
		//单个 不启用这句
//   			searchItem = doSearchAndStore(keyword, pageTotalNum, engineName)


			//多个 只用这句
//			searchItems.addAll(doSearchAndStore(keyword, pageTotalNum, "baidu"));
//		}

		doSearchAndStore(keyword, pageTotalNum, "bing");

		return searchItems;
	}

	/**
	 * 获取并解析网页内容,以及添加到数据库
	 * @param keyword
	 * @param pageCount
	 * @param engineName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<SearchItem> doSearchAndStore(String keyword,int pageCount,String engineName) throws UnsupportedEncodingException{
		SearchItem searchItem = new SearchItem();
		List<SearchItem> searchItems = new ArrayList<SearchItem>();

		Rule.ExtractRule eRule1=Config.defaultRule.getExtractRule("baidu");
		Rule.ExtractRule eRule2=Config.defaultRule.getExtractRule("bing");
		Rule.ExtractRule eRule3=Config.defaultRule.getExtractRule("sogou");

		parseDoc(keyword,1,"baidu",eRule1,searchItem,searchItems);

		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					parseDoc(keyword,1,"bing",eRule2,searchItem,searchItems);
				}catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			}
		};
		myThreadPool.execute(task);
		Runnable task2 = new Runnable() {
			@Override
			public void run() {
				try {
					parseDoc(keyword,1,"sogou",eRule3,searchItem,searchItems);
				}catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			}
		};
		myThreadPool.execute(task2);

		//关键字只存储一次
		if(advanceSearchService.findKey(keyword)!=null){
			return searchItems;
		}

		KeyWord keyWord = new KeyWord(keyword);
		advanceSearchService.addKeyWord(keyWord);

		return searchItems;
	}

	public void parseDoc(String keyword,int pageNum,String engineName,Rule.ExtractRule eRule,SearchItem searchItem,List<SearchItem> searchItems) throws UnsupportedEncodingException{
		StringBuilder sb=new StringBuilder();
		int pageCode=pageNum*eRule.pageMulti+eRule.pageOffset;
		try {
			sb.append(eRule.baseURL)
					.append(eRule.queryParam)
					.append("=")
					.append(URLEncoder.encode(keyword,"utf-8"))
					.append("&")
					.append(eRule.pageParam)
					.append("=")
					.append(pageCode);
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String url=sb.toString();
		try {
			Document doc= HttpUtils.fetchDoc(url);
			Elements itemEls=doc.select(eRule.itemCSS);
			int resultNum = 0;

			for(Element element: itemEls) {
				long t = System.currentTimeMillis();
				Element element_a = element.select(eRule.titleCSS).first();
				if(element_a == null) {
					continue;
				}
				String title = element_a.text().trim();
				String link = element_a.attr("href");
				//处理url
				if(link.startsWith("/")) {
					link = "http://www.sogou.com" + link;
				}
				String content = null;
				Element element_p = element.select(eRule.contentCSS).first();
				if(element_p == null) {
					continue;
				}
				content = element_p.text().trim();

//	                    SearchItem searchItem = new SearchItem(title, link, content);
				searchItem.setTitle(title);
				searchItem.setContent(content);
				searchItem.setUrl(link);
				searchItem.setSearchKey(keyword);

				//此处添加数据库相关操作
				advanceSearchService.addSearchItem(searchItem);
				searchItems.add(searchItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
