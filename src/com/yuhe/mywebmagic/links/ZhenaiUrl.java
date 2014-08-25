package com.yuhe.mywebmagic.links;

import java.util.HashSet;
import java.util.Set;

public class ZhenaiUrl extends BaseUrl {
	
	public ZhenaiUrl() {
		super();
	}
	
	public ZhenaiUrl(String url) {
		super(url);
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj == null) {  
            return false;  
        }  
  
        if (this == obj) {  
            return true;  
        }  
  
        if (!(obj instanceof ZhenaiUrl)) {  
            return false;  
        }  
  
        ZhenaiUrl myUrl = (ZhenaiUrl) obj;
        String currentStart = this.getUrl().indexOf("?")!=-1 ? this.getUrl().substring(0, this.getUrl().indexOf("?")) : this.getUrl();
        String myUrlStart = myUrl.getUrl().indexOf("?")!=-1 ? myUrl.getUrl().substring(0, myUrl.getUrl().indexOf("?")) : myUrl.getUrl();
        if(currentStart.equals(myUrlStart)) {
        	return true;
        }

		return false;
	}
	
	public static void main(String[] args) {
		ZhenaiUrl myUrl_1 = new ZhenaiUrl("http://songshuhui.net/archives/89827");
		ZhenaiUrl myUrl_2 = new ZhenaiUrl("http://songshuhui.net/archives/90101");
		ZhenaiUrl myUrl_3 = new ZhenaiUrl("http://songshuhui.net/archives/date/2012/08");
		Set<ZhenaiUrl> allLinksSet = new HashSet<ZhenaiUrl>();
		allLinksSet.add(myUrl_1);
		allLinksSet.add(myUrl_2);
		allLinksSet.add(myUrl_3);
		//
		System.out.println(allLinksSet.size());
		for(ZhenaiUrl tempUrl : allLinksSet) {
			System.out.println(tempUrl.getUrl());
		}
	}
}
