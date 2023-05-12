package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionRepository sectionRepository;

    public SectionService(LineDao lineDao, StationDao stationDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionRepository = sectionRepository;
    }

    public Long insertSection(SectionRequest request) {
        final Station upStation = stationDao.findById(request.getUpStationId()).orElseThrow(NoSuchElementException::new);
        final Station downStation = stationDao.findById(request.getDownStationId()).orElseThrow(NoSuchElementException::new);

        validateInput(request);

        final Sections sections = sectionRepository.findAllByLineId(request.getLineId());

        checkCanInsert(upStation, downStation, sections);

        if (sections.isUpEndPoint(downStation) || sections.isDownEndPoint(upStation)) {
            return insertToEndPoint(request);
        }

        return insertToMiddleSection(upStation, downStation, request, sections);
    }

    private void validateInput(SectionRequest request) {
        if (lineDao.findById(request.getLineId()).isEmpty()) {
            throw new IllegalArgumentException("존재 하지 않는 노선에는 구간을 추가 할 수 없습니다.");
        }

        if (Objects.equals(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("같은 역을 구간으로 등록할 수 없습니다.");
        }

        if (sectionRepository.exists(request.getDownStationId(), request.getUpStationId())
                || sectionRepository.exists(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("동일한 구간을 추가할 수 없습니다.");
        }
    }

    private static void checkCanInsert(Station upStation, Station downStation, Sections sortedSections) {
        if (sortedSections.canInsert(upStation, downStation)) {
            throw new IllegalArgumentException("역이 존재하지 않으면 추가할 수 없습니다.");
        }
    }

    private Long insertToEndPoint(SectionRequest request) {
        final Section section = new Section(
                request.getDistance(),
                new Station(request.getUpStationId()),
                new Station(request.getDownStationId()),
                request.getLineId()
        );
        return sectionRepository.insert(section);
    }

    private Long insertToMiddleSection(Station upStation, Station downStation, SectionRequest request, Sections sortedSections) {

        final Distance distance = new Distance(request.getDistance());

        if (sortedSections.isUpStationPoint(upStation)) {
            final Section targetSection = sortedSections.getTargtUpStationSection(upStation);

            validateDistance(targetSection.getDistance(), distance);

            final Section updateSection = new Section(distance, targetSection.getUpStation().getId(), request.getDownStationId(), targetSection.getLineId());
            final Section newSection = new Section(targetSection.getDistance().minus(distance), request.getDownStationId(), targetSection.getDownStation().getId(), targetSection.getLineId());
            return insert(updateSection, newSection);
        }

        final Section targetSection = sortedSections.getTargtDownStationSection(downStation);

        validateDistance(targetSection.getDistance(), distance);

        final Section updateSection = new Section(distance, request.getUpStationId(), request.getDownStationId(), targetSection.getLineId());
        final Section newSection = new Section(targetSection.getDistance().minus(distance), targetSection.getUpStation().getId(), request.getUpStationId(), targetSection.getLineId());
        return insert(updateSection, newSection);
    }

    private static void validateDistance(Distance targetDistance, Distance distance) {
        if (targetDistance.isShorterThan(distance)) {
            throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
        }
    }

    private Long insert(Section updateSection, Section insertSection) {
        sectionRepository.update(updateSection);
        return sectionRepository.insert(insertSection);
    }

    public void deleteStation(Long targetId, SectionDeleteRequest request) {
        final Station targetStation = stationDao.findById(targetId).orElseThrow(NoSuchElementException::new);

        final Sections sections = sectionRepository.findAllByLineId(request.getLineId());

        if (sections.isInitialState()) {
            sectionRepository.delete(sections.findFirstSectionId());
            lineDao.deleteById(request.getLineId());
            return;
        }

        if (sections.isDownEndPoint(targetStation)) {
            sectionRepository.delete(sections.findFirstSectionId());
            return;
        }

        if (sections.isUpEndPoint(targetStation)) {
            sectionRepository.delete(sections.findLastSectionId());
            return;
        }

        final Sections includeTargetSection = sections.findIncludeTargetSection(targetStation);
        final Distance newDistance = includeTargetSection.calculateTotalDistance();

        final Section forwardSection = includeTargetSection.getSections().get(0);
        final Section backwardSection = includeTargetSection.getSections().get(1);

        final Section newSection = new Section(newDistance, forwardSection.getUpStation(), backwardSection.getDownStation(), request.getLineId());
        sectionRepository.insert(newSection);
        for (Section section : includeTargetSection.getSections()) {
            sectionRepository.delete(section.getId());
        }
    }
}
