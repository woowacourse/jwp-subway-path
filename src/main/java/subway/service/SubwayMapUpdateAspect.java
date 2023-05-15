package subway.service;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SubwayMapUpdateAspect {

    private final SubwayMapService subwayMapService;

    public SubwayMapUpdateAspect(final SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }

    @AfterReturning(pointcut = "execution(* subway.service.SectionCreateService.createSection(..)) || execution(* subway.service.SectionDeleteService.deleteSection(..))")
    public void updateSubwayMap() {
        subwayMapService.update();
    }
}
