package subway.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Graph {
    void set(List<Station> stations, List<Section> sections);

    List<String> findPath(String start, String end);

    double findPathDistance(String start, String end);
}
