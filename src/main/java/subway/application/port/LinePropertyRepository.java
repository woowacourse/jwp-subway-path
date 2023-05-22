package subway.application.port;

import subway.application.core.domain.LineProperty;

import java.util.List;

public interface LinePropertyRepository {

    LineProperty insert(LineProperty lineProperty);

    LineProperty findById(Long id);

    List<LineProperty> findAll();

    void update(LineProperty lineProperty);

    void deleteById(Long id);
}
