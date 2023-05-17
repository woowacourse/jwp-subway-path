package subway.dao;

import subway.entity.LineEntity;

import java.util.List;

public interface LineDao {

    LineEntity insert(LineEntity line);

    List<LineEntity> findAll();

    LineEntity findById(Long id);
}
