package pack.admin.scrap;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import pack.dto.ScrapDto;

@Service
public class Scrap {
	
	public ArrayList<ScrapDto> scrapActors(String keyword){
    	ArrayList<ScrapDto> actorDatalist = new ArrayList<ScrapDto>();
        try {
        	String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_sly.hst&fbm=0&acr=1&ie=utf8&query=";
            Document doc = Jsoup.connect(url + URLEncoder.encode(keyword + "등장인물", "UTF-8"))
            		.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
            		.get();

            Elements elements;
            Elements actors;

            int i =  0;
            boolean success = false;
            while(!success){
            	i ++;
            	elements = doc.select("div:nth-of-type(" + i + ") > ul > li");
            	for(Element element: elements){
            		boolean hasStrong = element.select("div > div > strong").size() > 0;
            		boolean hasSpan = element.select("div > div > span").size() > 0;
            		if(hasStrong && hasSpan){
            			success = true;
            			actors = elements;
            			for (Element actor : actors) {
                       	 	Element name = actor.selectFirst(".title_box>span>a");
                            Element character = actor.selectFirst(".title_box>strong>a");
                            if(name != null) {
                            	ScrapDto dto = new ScrapDto(name.text(), character.text());
                               	actorDatalist.add(dto);
                            }
                        }
            			break;
            		}
            	}
            }
        } catch (Exception e){
            System.out.println("err: " + e);
        }
        return actorDatalist;
    }

}
