package subway.application;

import subway.domain.LineProperty;

import java.util.List;

public interface LinePropertyRepository {

    LineProperty insert(LineProperty lineProperty);

    List<LineProperty> findAll();

    LineProperty findById(Long id);

    void update(LineProperty lineProperty);

    void deleteById(Long id);
}
