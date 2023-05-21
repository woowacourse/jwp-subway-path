package subway.infrastructure.repository;

import org.springframework.stereotype.Repository;
import subway.application.core.domain.LineProperty;
import subway.application.port.LinePropertyRepository;
import subway.infrastructure.dao.LineDao;
import subway.infrastructure.entity.LineRow;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class H2LinePropertyRepository implements LinePropertyRepository {

   private final LineDao lineDao;

    public H2LinePropertyRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineProperty insert(LineProperty lineProperty) {
        LineRow row = lineDao.insert(
                new LineRow(lineProperty.getId(), lineProperty.getName(), lineProperty.getColor()));
        return new LineProperty(row.getId(), row.getName(), row.getColor());
    }

    public List<LineProperty> findAll() {
        List<LineRow> rows = lineDao.selectAll();

        return rows.stream()
                .map(row -> new LineProperty(row.getId(), row.getName(), row.getColor()))
                .collect(Collectors.toList());
    }

    public LineProperty findById(Long id) {
        LineRow row = lineDao.findById(id);
        return new LineProperty(row.getId(), row.getName(), row.getColor());
    }

    public void update(LineProperty lineProperty) {
        lineDao.update(new LineRow(lineProperty.getId(), lineProperty.getName(), lineProperty.getColor()));
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
