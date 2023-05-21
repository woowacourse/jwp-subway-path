package subway.cache;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import subway.event.RouteUpdateEvent;
import subway.service.SubwayMapService;

@Component
public class CacheInitializer implements SmartLifecycle {

    private final SubwayMapService subwayMapService;

    private boolean running;

    public CacheInitializer(final SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }

    @Override
    public void start() {
        subwayMapService.updateRoute(new RouteUpdateEvent());
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
