package com.yuhe.mywebmagic.links;

import java.util.HashSet;
import java.util.Set;

public class VaikanUrl extends BaseUrl {
	
	public VaikanUrl() {
		super();
	}
	
	public VaikanUrl(String url) {
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
  
        if (!(obj instanceof VaikanUrl)) {  
            return false;  
        }  
  
        VaikanUrl myUrl = (VaikanUrl) obj;
        String currentStart = this.getUrl().substring(0, this.getUrl().lastIndexOf("/"));
        String myUrlStart = myUrl.getUrl().substring(0, myUrl.getUrl().lastIndexOf("/"));
        if(currentStart.equals(myUrlStart)) {
        	return true;
        }

		return false;
	}
	
	public static void main(String[] args) {
		VaikanUrl myUrl_1 = new VaikanUrl("http://songshuhui.net/archives/89827");
		VaikanUrl myUrl_2 = new VaikanUrl("http://songshuhui.net/archives/90101");
		VaikanUrl myUrl_3 = new VaikanUrl("http://songshuhui.net/archives/date/2012/08");
		Set<VaikanUrl> allLinksSet = new HashSet<VaikanUrl>();
		allLinksSet.add(myUrl_1);
		allLinksSet.add(myUrl_2);
		allLinksSet.add(myUrl_3);
		//
		System.out.println(allLinksSet.size());
		for(VaikanUrl tempUrl : allLinksSet) {
			System.out.println(tempUrl.getUrl());
		}
	}
}
