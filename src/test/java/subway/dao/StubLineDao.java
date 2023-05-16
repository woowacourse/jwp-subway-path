package subway.dao;

import java.util.List;

import subway.domain.Line;

public class StubLineDao implements LineDao {

    @Override
    public Line insert(final Line line) {
        return null;
    }

    @Override
    public List<Line> findAll() {
        return List.of(
                new Line(1L, "1호선", "파란색"),
                new Line(2L, "2호선", "초록색")
        );
    }

    @Override
    public Line findById(final Long id) {
        return null;
    }

    @Override
    public void update(final Line newLine) {
    }

    @Override
    public void deleteById(final Long id) {
    }
}
