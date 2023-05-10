package subway.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.StationEdge;

@Repository
public class SimpleLineRepository implements LineRepository {

    private Long idIndex;

    private final List<Line> lines;

    public SimpleLineRepository() {
        this.idIndex = 1L;
        this.lines = new ArrayList<>();
    }

    @Override
    public Optional<Line> findById(Long id) {
        return lines.stream().filter(line -> line.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Line> findByName(String name) {
        return lines.stream().filter(line -> line.getName().equals(name)).findFirst();
    }

    @Override
    public Long create(Line line) {
        lines.add(Line.of(idIndex, line.getName(), line.getColor(), line.getStationEdges()));
        return idIndex++;
    }

    @Override
    public void updateWithSavedEdge(Line line, StationEdge stationEdge) {
        Line originalLine = lines.stream().filter(l -> l.getId().equals(line.getId()))
                .findFirst()
                .get();

        lines.remove(originalLine);
        lines.add(line);
    }

    @Override
    public void deleteStation(Line line, Long stationId) {
        Line originalLine = lines.stream().filter(l -> l.getId().equals(line.getId()))
                .findFirst()
                .get();

        lines.remove(originalLine);
        lines.add(line);
    }

    @Override
    public void deleteById(Long id) {
        Line originalLine = lines.stream().filter(l -> l.getId().equals(id))
                .findFirst()
                .get();


        lines.remove(originalLine);
    }
}
