package subway.adapter.line.out;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.application.line.port.in.LineNotFoundException;
import subway.application.line.port.out.LineRepository;
import subway.domain.interstation.InterStation;
import subway.domain.line.Line;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;

    public LineRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Override
    public Line save(final Line line) {
        try {
            final LineEntity lineEntity = lineDao.insert(LineEntity.from(line));
            return toLineProxy(lineEntity);
        } catch (final DuplicateKeyException e) {
            throw new LineAlreadyRegisteredException();
        }
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(this::toLineProxy)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Line> findById(final long id) {
        return lineDao.findById(id)
                .map(this::toLineProxy);
    }

    @Override
    public Line update(final Line line) {
        if (!(line instanceof LineProxy)) {
            throw new LineProxyNotInitializedException();
        }
        final LineProxy lineProxy = (LineProxy) line;
        updateInformation(line, lineProxy);
        addInterStations(lineProxy);
        removeInterStations(lineProxy);
        return findById(line.getId())
                .orElseThrow(LineNotFoundException::new);
    }


    private void addInterStations(final LineProxy lineProxy) {
        if (!lineProxy.getAddedInterStations().isEmpty()) {
            lineDao.insertInterStations(toInterStationEntities(lineProxy.getAddedInterStations(), lineProxy.getId()));
        }
    }

    private void updateInformation(final Line line, final LineProxy lineProxy) {
        if (lineProxy.isInfoNeedToUpdated()) {
            lineDao.updateInformation(LineEntity.from(line));
        }
    }

    private void removeInterStations(final LineProxy lineProxy) {
        if (!lineProxy.getRemovedInterStations().isEmpty()) {
            lineDao.deleteInterStations(lineProxy.getId(),
                    toInterStationEntities(lineProxy.getRemovedInterStations(), lineProxy.getId()));
        }
    }

    private List<InterStationEntity> toInterStationEntities(final List<InterStation> interStations,
            final long id) {
        return interStations.stream()
                .map(interStation -> InterStationEntity.of(interStation, id))
                .collect(Collectors.toList());
    }

    private LineProxy toLineProxy(final LineEntity lineEntity) {
        return new LineProxy(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                toInterStations(lineEntity.getInterStationEntities()));
    }

    private List<InterStation> toInterStations(final List<InterStationEntity> interStationEntities) {
        return interStationEntities.stream()
                .map(InterStationEntity::toInterStation)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(final long id) {
        lineDao.deleteById(id);
    }
}
