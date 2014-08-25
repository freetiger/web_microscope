package com.freetiger.microscope.links;

import java.util.HashSet;
import java.util.Set;

public class DamaiUrl extends BaseUrl {
	
	public DamaiUrl() {
		super();
	}
	
	public DamaiUrl(String url) {
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
  
        if (!(obj instanceof DamaiUrl)) {  
            return false;  
        }  
  
        DamaiUrl myUrl = (DamaiUrl) obj;
        String currentStart = null;
        String myUrlStart = null;
        if(this.getUrl().indexOf("?")!=-1) {
        	currentStart = this.getUrl().substring(0, this.getUrl().indexOf("?"));
        }else if(this.getUrl().indexOf(".htm")!=-1) {
        	currentStart = this.getUrl().substring(0, this.getUrl().lastIndexOf("/"));
        }else if(this.getUrl().indexOf(".shtm")!=-1) {
        	currentStart = this.getUrl().substring(0, this.getUrl().lastIndexOf("/"));
        }else {
        	currentStart = this.getUrl();
        }
        //
        if(myUrl.getUrl().indexOf("?")!=-1) {
        	myUrlStart = myUrl.getUrl().substring(0, myUrl.getUrl().indexOf("?"));
        }else if(myUrl.getUrl().indexOf(".htm")!=-1) {
        	myUrlStart = myUrl.getUrl().substring(0, myUrl.getUrl().lastIndexOf("/"));
        }else if(myUrl.getUrl().indexOf(".shtm")!=-1) {
        	myUrlStart = myUrl.getUrl().substring(0, myUrl.getUrl().lastIndexOf("/"));
        }else {
        	myUrlStart = myUrl.getUrl();
        }
        //
        if(currentStart.equals(myUrlStart)) {
        	return true;
        }

		return false;
	}
	
	public static void main(String[] args) {
		DamaiUrl myUrl_1 = new DamaiUrl("http://songshuhui.net/archives/89827");
		DamaiUrl myUrl_2 = new DamaiUrl("http://songshuhui.net/archives/90101");
		DamaiUrl myUrl_3 = new DamaiUrl("http://songshuhui.net/archives/date/2012/08");
		Set<DamaiUrl> allLinksSet = new HashSet<DamaiUrl>();
		allLinksSet.add(myUrl_1);
		allLinksSet.add(myUrl_2);
		allLinksSet.add(myUrl_3);
		//
		System.out.println(allLinksSet.size());
		for(DamaiUrl tempUrl : allLinksSet) {
			System.out.println(tempUrl.getUrl());
		}
	}
}
