package microservice.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
//        return new RestTemplate();
//          cấu hình timeout
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // Timeout kết nối: 3 giây
        factory.setReadTimeout(3000);    // Timeout đọc dữ liệu: 3 giây
        return new RestTemplate(factory);
    }
}
