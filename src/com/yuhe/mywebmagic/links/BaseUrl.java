package com.yuhe.mywebmagic.links;

import java.util.HashSet;
import java.util.Set;

import com.yuhe.mywebmagic.util.StringUtil;

public class BaseUrl {
	private String url;
	
	public BaseUrl() {
		
	}
	
	public BaseUrl(String url) {
		if(StringUtil.isEmptyString(url)) {
			this.url = "";
		}else {
			url = url.trim();
			if(url.endsWith("/")) {
				this.url = url.substring(0, url.length()-1);
			}else {
				this.url = url;
			}
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
    @Override
    public int hashCode() {
    	return 0;
    }
    
	@Override
	public boolean equals(Object obj) {
        if (obj == null) {  
            return false;  
        }  
  
        if (this == obj) {  
            return true;  
        }  
  
        if (!(obj instanceof BaseUrl)) {  
            return false;  
        }  
  
        BaseUrl BaseUrl = (BaseUrl) obj;
        if(this.getUrl().equals(BaseUrl.getUrl())) {
        	return true;
        }

		return false;
	}
	
	public static void main(String[] args) {
		BaseUrl BaseUrl_1 = new BaseUrl("http://songshuhui.net/archives/89827");
		BaseUrl BaseUrl_2 = new BaseUrl("http://songshuhui.net/archives/90101");
		BaseUrl BaseUrl_3 = new BaseUrl("http://songshuhui.net/archives/date/2012/08");
		Set<BaseUrl> allLinksSet = new HashSet<BaseUrl>();
		allLinksSet.add(BaseUrl_1);
		allLinksSet.add(BaseUrl_2);
		allLinksSet.add(BaseUrl_3);
		//
		System.out.println(allLinksSet.size());
		for(BaseUrl tempUrl : allLinksSet) {
			System.out.println(tempUrl.getUrl());
		}
	}
}