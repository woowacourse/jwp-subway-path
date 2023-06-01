package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class SectionService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public SectionService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public Long insertSection(SectionRequest request) {
        final List<Section> sections = getSections(request.getLineId());
        final Sections sortedSections = Sections.from(sections);
        validateInput(sortedSections, request);

        checkCanInsert(request, sortedSections);

        if (sortedSections.isUpEndPoint(request.getDownStationId()) || sortedSections.isDownEndPoint(request.getUpStationId())) {
            return insertToEndPoint(request);
        }

        return insertToMiddleSection(request, sortedSections);
    }

    private List<Section> getSections(Long lineId) {
        return sectionDao.findAllByLineId(lineId);
    }

    private void validateInput(Sections sections, SectionRequest request) {
        if (lineDao.findById(request.getLineId()).isEmpty()) {
            throw new IllegalArgumentException("존재 하지 않는 노선에는 구간을 추가 할 수 없습니다.");
        }
        sections.isExistSection(request);
    }

    private static void checkCanInsert(SectionRequest request, Sections sortedSections) {
        final Set<Long> allStationIds = sortedSections.getSections().stream()
                .flatMap(section -> Stream.of(
                        section.getUpStationId(), section.getDownStationId()))
                .collect(Collectors.toSet());

        final boolean haveUpStation = allStationIds.contains(request.getUpStationId());
        final boolean haveDownStation = allStationIds.contains(request.getDownStationId());

        if (haveUpStation == haveDownStation) {
            throw new IllegalArgumentException("역이 존재하지 않으면 추가할 수 없습니다.");
        }
    }

    private Long insertToEndPoint(SectionRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        final Section section = new Section(new Distance(request.getDistance()), upStation, downStation, request.getLineId());
        return sectionDao.insert(section);
    }

    private Station findStationById(Long id) {
        return new Station(id, stationDao.findById(id).getName());
    }

    private Long insertToMiddleSection(SectionRequest request, Sections sortedSections) {
        if (sortedSections.isUpStationPoint(request.getUpStationId())) {
            final Section targetSection = sortedSections.getTargetUpStationSection(request.getUpStationId());
            validateDistance(targetSection.getDistance(), request.getDistance());

            final Section updateSection = new Section(new Distance(request.getDistance()), findStationById(targetSection.getUpStationId()), findStationById(request.getDownStationId()), targetSection.getLineId());
            final Section newSection = new Section(new Distance(targetSection.getDistance() - request.getDistance()), findStationById(request.getDownStationId()), findStationById(targetSection.getDownStationId()), targetSection.getLineId());
            return updateAndInsertSection(updateSection, newSection);
        }
        final Section targetSection = sortedSections.getTargetDownStationSection(request.getDownStationId());
        validateDistance(targetSection.getDistance(), request.getDistance());

        final Section updateSection = new Section(new Distance(request.getDistance()), findStationById(request.getUpStationId()), findStationById(request.getDownStationId()), targetSection.getLineId());
        final Section newSection = new Section(new Distance(targetSection.getDistance() - request.getDistance()), findStationById(targetSection.getUpStationId()), findStationById(request.getUpStationId()), targetSection.getLineId());
        return updateAndInsertSection(updateSection, newSection);
    }

    private static void validateDistance(int targetDistance, int requestDistance) {
        if (targetDistance <= requestDistance) {
            throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
        }
    }

    private Long updateAndInsertSection(Section updateSection, Section insertSection) {
        sectionDao.update(updateSection);
        return sectionDao.insert(insertSection);
    }

    public void deleteStation(SectionDeleteRequest request) {
        final List<Section> sections = getSections(request.getLineId());
        final Sections sortedSections = Sections.from(sections);

        sortedSections.deleteSection(request);
        sectionDao.deleteByLineId(request.getLineId());
        sectionDao.saveAll(sortedSections.getSections());
    }
}
