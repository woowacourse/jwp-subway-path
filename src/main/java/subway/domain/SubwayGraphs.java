package subway.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubwayGraphs {
    private final List<SubwayGraph> subwayGraphs;

    public SubwayGraphs() {
        this.subwayGraphs = new ArrayList<>();
    }
}
