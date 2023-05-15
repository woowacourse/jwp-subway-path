package subway.domain.repository;

import subway.domain.Section;

import java.util.List;

public interface SectionRepository {

    List<Section> findAll();

    void createSection(String lineName, List<Section> sections);

    List<Section> findAllByLineId(Long lineId);

    void deleteBySection(Long lineId, Section section);
}
