package subway.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T insert(T t);
    void update(T t);
    void deleteById(Long id);
    Optional<T> findById(Long id);
    List<T> findAll();
}
