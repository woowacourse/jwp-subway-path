package subway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import subway.event.RouteUpdateEvent;
import subway.service.SubwayMapService;

@SpringBootApplication
public class SubwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubwayApplication.class, args);
    }

    @Bean
    public CommandLineRunner initEvent(final SubwayMapService subwayMapService) {
        return args -> {
            subwayMapService.updateRoute(new RouteUpdateEvent());
        };
    }
}
