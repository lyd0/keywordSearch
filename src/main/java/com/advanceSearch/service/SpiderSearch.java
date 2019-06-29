package com.advanceSearch.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advanceSearch.entity.KeyWord;
import com.advanceSearch.entity.SearchItem;
import com.advanceSearch.utils.Config;
import com.advanceSearch.utils.HttpUtils;
import com.advanceSearch.utils.Rule;

/**
 *
 */
@Service
public class SpiderSearch {
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
	 * @param engineNames 搜索引擎，，目前有bing，sougou
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<SearchItem> searchAndStore(String keyword,int pageTotalNum,String... engineNames) throws UnsupportedEncodingException {
		List<SearchItem> searchItems = new ArrayList<SearchItem>();
		SearchItem searchItem = new SearchItem();
		for(String engineName:engineNames){
//   			searchItem = doSearchAndStore(keyword, pageTotalNum, engineName)
			searchItems.addAll(doSearchAndStore(keyword, pageTotalNum, engineName));
		}

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
		Rule.ExtractRule eRule=Config.defaultRule.getExtractRule(engineName);
		System.out.println(eRule);
		long t = System.currentTimeMillis();


		for(int pageNum=1;pageNum<=pageCount;pageNum++){
			StringBuilder sb=new StringBuilder();
			int pageCode=pageNum*eRule.pageMulti+eRule.pageOffset;
			System.out.println("pageCode : " + pageCode);
			sb.append(eRule.baseURL)
					.append(eRule.queryParam)
					.append("=")
					.append(URLEncoder.encode(keyword,"utf-8"))
					.append("&")
					.append(eRule.pageParam)
					.append("=")
					.append(pageCode);

			String url=sb.toString();
			System.out.println("sb: " + url);
			try {
				Document doc= HttpUtils.fetchDoc(url);
				Elements itemEls=doc.select(eRule.itemCSS);
				int resultNum = 0;

				for(Element element: itemEls) {
					resultNum++;

					Element element_a = element.select(eRule.titleCSS).first();
					System.out.println(element_a);
					if(element_a == null) {
						continue;
					}

					String title = element_a.text().trim();

					String link = element_a.attr("href");

					String content = null;
					Element element_p = element.select(eRule.contentCSS).first();
					if(element_p == null) {
						continue;
					}

					content = element_p.text().trim();

					System.out.println("resultNum: " + resultNum);
//	                    SearchItem searchItem = new SearchItem(title, link, content);
					searchItem.setTitle(title);
					searchItem.setContent(content);
					searchItem.setUrl(link);
					searchItem.setSearchKey(keyword);

					//此处添加数据库相关操作
					advanceSearchService.addSearchItem(searchItem);

					System.out.println(searchItem);
					System.out.println(engineName + "\n");
					searchItems.add(searchItem);

				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("--t2"+(System.currentTimeMillis()-t));
		}

		//关键字只存储一次
		if(advanceSearchService.findKey(keyword)!=null){
			return searchItems;
		}

		KeyWord keyWord = new KeyWord(keyword);
		advanceSearchService.addKeyWord(keyWord);

		return searchItems;
	}
}
