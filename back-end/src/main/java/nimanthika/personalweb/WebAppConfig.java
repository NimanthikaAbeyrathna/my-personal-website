package nimanthika.personalweb;

import nimanthika.personalweb.api.MessageController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class WebAppConfig {
    @Bean
    public MessageController messageController(){
        return  new MessageController();
    }
}
