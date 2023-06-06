package subway.line.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.line.domain.interstation.InterStation;
import subway.line.domain.interstation.InterStations;
import subway.line.domain.line.Line;
import subway.line.domain.line.LineRepository;

public class FakeLineRepository implements LineRepository {

    private final Map<Long, Line> lines = new HashMap<>();
    private long id = 0L;
    private long interStationId = 0L;

    @Override
    public Line save(Line line) {
        List<InterStation> interStations = line.getInterStations()
                .getInterStations()
                .stream()
                .map(this::createInterStation)
                .collect(Collectors.toList());

        Line savedLine = new Line(++id, line.getColor(), line.getName(), new InterStations(interStations));
        lines.put(id, savedLine);
        return savedLine;
    }

    private InterStation createInterStation(InterStation interStation) {
        return new InterStation(++interStationId, interStation);
    }

    @Override
    public List<Line> findAll() {
        return new ArrayList<>(lines.values());
    }

    @Override
    public Line update(Line line) {
        Long lineId = line.getId();
        lines.put(lineId, new Line(lineId, line));
        return lines.get(lineId);
    }

    @Override
    public Optional<Line> findById(long id) {
        return Optional.ofNullable(lines.get(id));
    }

    @Override
    public void deleteById(long id) {
        lines.remove(id);
    }
}
