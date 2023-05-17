package subway.domain;

public interface SectionRepository {
    void saveAll(Long lineId, Sections sections);

    Sections findAllByLineId(Long lineId);

    void deleteAllByLineId(Long lineId);

    boolean isExistSectionUsingStation(Long stationId);
}
