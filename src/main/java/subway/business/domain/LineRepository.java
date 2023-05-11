package subway.business.domain;

public interface LineRepository {

    long save(Line line);

    Line findById(Long id);
}
