package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.application.domain.LineProperty;
import subway.application.repository.LinePropertyRepository;
import subway.persistence.dao.LinePropertyDao;
import subway.persistence.row.LinePropertyRow;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class H2LinePropertyRepository implements LinePropertyRepository {

   private final LinePropertyDao linePropertyDao;

    public H2LinePropertyRepository(LinePropertyDao linePropertyDao) {
        this.linePropertyDao = linePropertyDao;
    }

    public LineProperty insert(LineProperty lineProperty) {
        LinePropertyRow row = linePropertyDao.insert(
                new LinePropertyRow(lineProperty.getId(), lineProperty.getName(), lineProperty.getColor()));

        return new LineProperty(row.getId(), row.getName(), row.getColor());
    }

    public List<LineProperty> findAll() {
        List<LinePropertyRow> rows = linePropertyDao.selectAll();

        return rows.stream()
                .map(row -> new LineProperty(row.getId(), row.getName(), row.getColor()))
                .collect(Collectors.toList());
    }

    public LineProperty findById(Long id) {
        LinePropertyRow row = linePropertyDao.findById(id);

        return new LineProperty(row.getId(), row.getName(), row.getColor());
    }

    public void update(LineProperty lineProperty) {
        linePropertyDao.update(
                new LinePropertyRow(lineProperty.getId(), lineProperty.getName(), lineProperty.getColor()));
    }

    public void deleteById(Long id) {
        linePropertyDao.deleteById(id);
    }
}
