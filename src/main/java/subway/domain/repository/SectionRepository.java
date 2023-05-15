package subway.domain.repository;

import java.util.List;

import subway.domain.Section;

public interface SectionRepository {

    List<Section> findAll();

    void createSection(String lineName, List<Section> sections);

    List<Section> findAllByLineName(String lineName);

    void deleteBySection(Long lineId, Section section);

    Section findIdByUpDown(String upStation, String downStation);
}
