package pack.admin.scrap;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import pack.dto.ActorScrapDto;
import pack.dto.ShowDto;

@Service
public class Scrap {
	
	String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_sly.hst&fbm=0&acr=1&ie=utf8&query=";
	
	// 정확한 작품명과 대표 사진
	public ShowDto scrapShow(String keyword){
		ShowDto dto = new ShowDto();
        try {
            Document doc = Jsoup.connect(url + URLEncoder.encode(keyword, "UTF-8"))
            		.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
            		.get();
            Elements elements = doc.select("#main_pack > div");
            for (Element ele : elements) {
                Element title = ele.selectFirst("div > div > h2 > span > strong");
                if (title != null) {
                	Element showpic = ele.selectFirst("div > div > div > div > a > img");
                	dto.setTitle(title.text());
                	dto.setPic(showpic.attr("src"));
                }
            }
		} catch (Exception e) {
			System.out.println("err: " + e);
		}
		return dto;
    }
	
	// 등장인물 목록
	public ArrayList<ActorScrapDto> scrapActors(String keyword){
    	ArrayList<ActorScrapDto> actorDatalist = new ArrayList<ActorScrapDto>();
        try {
            Document doc = Jsoup.connect(url + URLEncoder.encode(keyword + "등장인물", "UTF-8"))
            		.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
            		.get();

			Elements actors = doc.select("div > ul > li");
			for (Element actor : actors) {
				Element actorname = actor.selectFirst(".title_box>span>a");
				Element character = actor.selectFirst(".title_box>strong>a");
				Element actorpic = actor.selectFirst(".thumb>img");
				if (actorname != null) {
					ActorScrapDto dto = new ActorScrapDto();
					dto.setActor(actorname.text());
					dto.setCharacter(character.text());
					dto.setPic(actorpic.attr("src"));
					actorDatalist.add(dto);
				}
			}

		} catch (Exception e) {
			System.out.println("err: " + e);
		}
		return actorDatalist;
    }

}
