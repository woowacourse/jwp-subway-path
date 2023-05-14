package subway.adapter.line.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.application.line.port.out.LineRepository;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.InterStations;
import subway.domain.line.Line;

public class FakeLineRepository implements LineRepository {

    private final Map<Long, Line> lines = new HashMap<>();
    private long id = 0L;
    private long interStationId = 0L;

    @Override
    public Line save(final Line line) {
        final List<InterStation> interStations = line.getInterStations()
            .getInterStations()
            .stream()
            .map(this::createInterStation)
            .collect(Collectors.toList());

        final Line savedLine = new Line(++id, line.getColor(), line.getName(), new InterStations(interStations));
        lines.put(id, savedLine);
        return savedLine;
    }

    private InterStation createInterStation(final InterStation interStation) {
        return new InterStation(++interStationId, interStation);
    }

    @Override
    public List<Line> findAll() {
        return new ArrayList<>(lines.values());
    }

    @Override
    public void update(final Line line) {
        final Long lineId = line.getId();
        lines.put(lineId, new Line(lineId, line));
    }

    @Override
    public Optional<Line> findById(final long id) {
        return Optional.ofNullable(lines.get(id));
    }

    @Override
    public void deleteById(final long id) {
        lines.remove(id);
    }
}
