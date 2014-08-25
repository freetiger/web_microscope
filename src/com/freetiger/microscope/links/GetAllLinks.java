package com.freetiger.microscope.links;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;

import com.freetiger.microscope.util.HttpsUtil;
import com.freetiger.microscope.util.StringUtil;

public class GetAllLinks {
	private HttpClient client = HttpsUtil.createHttpClient();
	/**多次使用的话不需要重新编译正则表达式了，对于频繁调用能提高效率*/  
	private Pattern urlPattern = Pattern.compile("<a.*?href=\"([^\"]+)\"[^>]*>(.*?)</a>");	//匹配url的规则
	private Class<? extends BaseUrl> clazzUrl = BaseUrl.class;	//url类，equals类重写，设置url相等的逻辑
	private Long maxUrlNum = 1000L;	//设置查找url的上限，不精确
	private Set<String> urlIncludes = new HashSet<String>();	//设置url爬取范围：url必须包含指定字符串 （且）
	private Set<String> urlExcludes = new HashSet<String>();	//设置url爬取范围：url不能包含指定字符串 （且）
	private String enterUrl;	//入口地址
	//
	private Set<BaseUrl> allLinksSet = new HashSet<BaseUrl>();
	private Queue<BaseUrl> pendingLinksQueue = new LinkedList<BaseUrl>();
	
	public GetAllLinks() {
	}
	
	public GetAllLinks(String enterUrl) {
		this.enterUrl = enterUrl;
	}
	
	public GetAllLinks(String enterUrl, HttpClient client) {
		this.enterUrl = enterUrl;
		this.client = client;
	}
	
	public GetAllLinks(String enterUrl, String urlPattern) {
		this.enterUrl = enterUrl;
		this.urlPattern = Pattern.compile(urlPattern);
	}
	
	public GetAllLinks(String enterUrl, String urlPattern, HttpClient client) {
		this.enterUrl = enterUrl;
		this.urlPattern = Pattern.compile(urlPattern);
		this.client = client;
	}

	/**
	 * 
	 * 访问url，并将返回的页面中的所有url加入遍历队列
	 *
	 * @autor: heyuxing  2014-8-25 下午3:28:59
	 * @param url    
	 * @return void
	 */
	public void handleLink(String url) {
		try{
			Set<BaseUrl> matchLinksSet = new HashSet<BaseUrl>();
			String page = HttpsUtil.requestGet(url, client);
			if(!StringUtil.isEmptyString(page)) {
				//匹配url页面的所有url
				Matcher matcher = this.urlPattern.matcher(page);
				String matchUrl = null;
				while(matcher.find()) {
					matchUrl = matcher.group(1);
					if(isValidUrl(matchUrl)) {
						matchLinksSet.add(this.newUrl(matchUrl));	
					}
				}	
				//将匹配的url加入处理队列
				matchLinksSet.removeAll(allLinksSet);
				allLinksSet.addAll(matchLinksSet);
				pendingLinksQueue.addAll(matchLinksSet);
				pendingLinksQueue.poll();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isValidUrl(String url) {
		boolean isValid = true;
		//
		if(StringUtil.isEmptyString(url)) {
			isValid = false;
		}
		//包含
		if(isValid) {
			for(String urlInclude : urlIncludes) {
				if(url.indexOf(urlInclude)==-1) {
					isValid = false;
					break;
				}
			}	
		}
		//不包含
		if(isValid) {
			for(String urlExclude : urlExcludes) {
				if(url.indexOf(urlExclude)!=-1) {
					isValid = false;
					break;
				}
			}	
		}
		
		return isValid;
	}
	
	/**
	 * 
	 * 创建url对象
	 *
	 * @autor: heyuxing  2014-8-25 下午3:30:14
	 * @param url
	 * @return    
	 * @return BaseUrl
	 */
	public BaseUrl newUrl(String url){
		BaseUrl instance = null;
		try {
			Constructor<? extends BaseUrl> constructor	= clazzUrl.getConstructor(String.class);
			instance = constructor.newInstance(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return instance;
	}
	
	/**
	 * 
	 * 爬取所有链接
	 *
	 * @autor: heyuxing  2014-8-25 下午3:31:01    
	 * @return void
	 */
	public void run() {
		BaseUrl baseUrl = this.newUrl(this.enterUrl);
		pendingLinksQueue.add(baseUrl);
		allLinksSet.add(baseUrl);
		BaseUrl url = null;
		do{
			url = pendingLinksQueue.poll();
			if(url==null || allLinksSet.size()>maxUrlNum) {
				break;
			}
			handleLink(url.getUrl());
		}while(true);
		
		System.out.println(allLinksSet.size());
		for(BaseUrl tempUrl : allLinksSet) {
			System.out.println(tempUrl.getUrl());
		}
	}
	
	public Pattern getUrlPattern() {
		return urlPattern;
	}

	public GetAllLinks setUrlPattern(Pattern urlPattern) {
		this.urlPattern = urlPattern;
		return this;
	}
	
	public String getEnterUrl() {
		return enterUrl;
	}

	public GetAllLinks setEnterUrl(String enterUrl) {
		this.enterUrl = enterUrl;
		return this;
	}

	public HttpClient getClient() {
		return client;
	}

	public GetAllLinks setClient(HttpClient client) {
		this.client = client;
		return this;
	}

	public Class<? extends BaseUrl> getClazzUrl() {
		return clazzUrl;
	}

	public GetAllLinks setClazzUrl(Class<? extends BaseUrl> clazzUrl) {
		this.clazzUrl = clazzUrl;
		return this;
	}

	public Long getMaxUrlNum() {
		return maxUrlNum;
	}

	public GetAllLinks setMaxUrlNum(Long maxUrlNum) {
		this.maxUrlNum = maxUrlNum;
		return this;
	}

	public Set<String> getUrlIncludes() {
		return urlIncludes;
	}

	public GetAllLinks setUrlIncludes(Set<String> urlIncludes) {
		this.urlIncludes = urlIncludes;
		return this;
	}
	
	public GetAllLinks setUrlIncludes(String urlIncludes) {
		this.urlIncludes.add(urlIncludes);
		return this;
	}

	public Set<String> getUrlExcludes() {
		return urlExcludes;
	}

	public GetAllLinks setUrlExcludes(String urlExcludes) {
		this.urlExcludes.add(urlExcludes);
		return this;
	}

	/** 
	 * 描述该方法的功能及算法流程
	 *
	 * @autor: heyuxing  2014-8-22 下午3:15:37
	 * @param args    
	 * @return void 
	 */
	public static void main(String[] args) {
//		new GetAllLinks("songshuhui.net", "http://songshuhui.net/").run();
		
//		new GetAllLinks("http://www.vaikan.com/").setUrlIncludes("vaikan.com").setClazzUrl(VaikanUrl.class).run();
//		String enterUrl = "http://profile.zhenai.com/login/loginactionindex.jsps?fid=&fromurl=&loginInfo=15821389004&loginmode=2&mid=&password=1161hyx&redirectUrl=&whereLogin=index&whichTV=";
//		new GetAllLinks(enterUrl).setClazzUrl(ZhenaiUrl.class).setUrlIncludes("zhenai.com").run();
		
		new GetAllLinks("http://www.damai.cn/").setUrlIncludes("damai.cn").setClazzUrl(DamaiUrl.class).run();
	}

}
