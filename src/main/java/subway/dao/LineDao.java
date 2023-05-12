package subway.dao;

import subway.entity.LineEntity;

import java.util.List;

public interface LineDao {

    LineEntity save(LineEntity line);

    List<LineEntity> findAll();

    LineEntity findById(Long id);

    void update(LineEntity newLine);

    void deleteById(Long id);
}
