package uz.app.finalproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Barcha endpointlar uchun CORS ni sozlash
        registry.addMapping("/**")  // Barcha yo'llar uchun
                .allowedOrigins("/**")  // Frontend manzilingiz
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Ruxsat etilgan metodlar
                .allowedHeaders("*");  // Ruxsat etilgan header'lar
                //.allowCredentials(true);  // Cookies va autentifikatsiyani ruxsat berish
    }
}
