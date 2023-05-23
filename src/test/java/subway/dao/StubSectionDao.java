package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import subway.domain.section.Section;
import subway.domain.station.Station;

public class StubSectionDao implements SectionDao {

    private final Map<Long, Section> sectionMap = new HashMap<>();
    private final AtomicLong maxId = new AtomicLong();

    @Override
    public Section insert(final Section section) {
        final long currentId = maxId.incrementAndGet();
        final Section saved = new Section(currentId, section);
        sectionMap.put(currentId, saved);
        return saved;
    }

    @Override
    public Optional<Section> findById(final Long id) {
        final Section section = sectionMap.get(id);
        if (section == null) {
            return Optional.empty();
        }
        return Optional.of(section);
    }

    @Override
    public void deleteById(final Long id) {
        sectionMap.remove(id);
    }

    @Override
    public List<Section> findAll() {
        return List.of(
                Section.builder()
                        .id(1L)
                        .lineId(1L)
                        .upStation(new Station(1L, "잠실역"))
                        .downStation(new Station(2L, "선릉역"))
                        .distance(3)
                        .build(),
                Section.builder()
                        .id(2L)
                        .lineId(1L)
                        .upStation(new Station(2L, "선릉역"))
                        .downStation(new Station(3L, "강남역"))
                        .distance(2)
                        .build(),
                Section.builder()
                        .id(3L)
                        .lineId(1L)
                        .upStation(new Station(3L, "강남역"))
                        .downStation(new Station(4L, "사당역"))
                        .distance(4)
                        .build(),
                Section.builder()
                        .id(4L)
                        .lineId(2L)
                        .upStation(new Station(3L, "강남역"))
                        .downStation(new Station(5L, "낙성대역"))
                        .distance(2)
                        .build(),
                Section.builder()
                        .id(5L)
                        .lineId(2L)
                        .upStation(new Station(5L, "낙성대역"))
                        .downStation(new Station(6L, "신림역"))
                        .distance(1)
                        .build()
        );
    }

    @Override
    public List<Section> findByLineId(final Long lineId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Section> findUpSection(final Long lineId, final Long stationId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId) && it.getDownStation().getId().equals(stationId))
                .findFirst();
    }

    @Override
    public Optional<Section> findDownSection(final Long lineId, final Long stationId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId) && it.getUpStation().getId().equals(stationId))
                .findFirst();
    }
}
