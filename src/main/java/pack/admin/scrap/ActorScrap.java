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
public class ActorScrap {
	
	public ArrayList<ScrapDto> scrapActors(String keyword){
    	ArrayList<ScrapDto> actorDatalist = new ArrayList<ScrapDto>();
        try {
        	String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_sly.hst&fbm=0&acr=1&ie=utf8&query=";
            Document doc = Jsoup.connect(url + URLEncoder.encode(keyword + "등장인물", "UTF-8"))
            		.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
            		.get();

			Elements actors = doc.select("div > ul > li");
			for (Element actor : actors) {
				Element name = actor.selectFirst(".title_box>span>a");
				Element character = actor.selectFirst(".title_box>strong>a");
				Element pic = actor.selectFirst(".thumb>img");
				if (name != null) {
					ScrapDto dto = new ScrapDto();
					dto.setActor(name.text());
					dto.setCharacter(character.text());
					dto.setPic(pic.attr("src")); // attr(): key를 통해 해당 value 반환
					actorDatalist.add(dto);
				}
			}

		} catch (Exception e) {
			System.out.println("err: " + e);
		}
		return actorDatalist;
    }

}
