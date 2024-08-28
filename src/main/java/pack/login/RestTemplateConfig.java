package pack.login;
import java.util.Collections;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
public class RestTemplateConfig {
	public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        return restTemplate;
    }
}
