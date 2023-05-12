package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final SectionMapper sectionMapper;

    public SectionService(SectionDao sectionDao, LineDao lineDao, SectionMapper sectionMapper) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.sectionMapper = sectionMapper;
    }

    public Long insertSection(SectionRequest request) {
        validateInput(request);

        final List<Section> sections = sectionMapper.mapFrom(sectionDao.findAllByLineId(request.getLineId()));
        final Sections sortedSections = Sections.from(sections);

        checkCanInsert(request, sortedSections);

        if (sortedSections.isUpEndPoint(request.getDownStationId()) || sortedSections.isDownEndPoint(request.getUpStationId())) {
            return insertToEndPoint(request);
        }

        return insertToMiddleSection(request, sortedSections);
    }

    private void validateInput(SectionRequest request) {
        if (lineDao.findById(request.getLineId()).isEmpty()) {
            throw new IllegalArgumentException("존재 하지 않는 노선에는 구간을 추가 할 수 없습니다.");
        }

        if (Objects.equals(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("같은 역을 구간으로 등록할 수 없습니다.");
        }

        if (sectionDao.exists(request.getDownStationId(), request.getUpStationId())
                || sectionDao.exists(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("동일한 구간을 추가할 수 없습니다.");
        }
    }

    private static void checkCanInsert(SectionRequest request, Sections sortedSections) {
        final Set<Long> allStationIds = sortedSections.getSections().stream()
                .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                .collect(Collectors.toSet());

        final boolean haveUpStation = allStationIds.contains(request.getUpStationId());
        final boolean haveDownStation = allStationIds.contains(request.getDownStationId());

        if (haveUpStation == haveDownStation) {
            throw new IllegalArgumentException("역이 존재하지 않으면 추가할 수 없습니다.");
        }
    }

    private Long insertToEndPoint(SectionRequest request) {
        final Section section = new Section(request.getDistance(), request.getUpStationId(), request.getDownStationId(), request.getLineId());
        return sectionDao.insert(sectionMapper.mapToEntity(section));
    }

    private Long insertToMiddleSection(SectionRequest request, Sections sortedSections) {

        final Distance distance = new Distance(request.getDistance());

        if (sortedSections.isUpStationPoint(request.getUpStationId())) {
            final Section targetSection = sortedSections.getTargtUpStationSection(request.getUpStationId());

            validateDistance(targetSection.getDistance(), distance);

            final Section updateSection = new Section(distance, targetSection.getUpStationId(), request.getDownStationId(), targetSection.getLineId());
            final Section newSection = new Section(targetSection.getDistance().minus(distance), request.getDownStationId(), targetSection.getDownStationId(), targetSection.getLineId());
            return insert(updateSection, newSection);
        }

        final Section targetSection = sortedSections.getTargtDownStationSection(request.getDownStationId());

        validateDistance(targetSection.getDistance(), distance);

        final Section updateSection = new Section(distance, request.getUpStationId(), request.getDownStationId(), targetSection.getLineId());
        final Section newSection = new Section(targetSection.getDistance().minus(distance), targetSection.getUpStationId(), request.getUpStationId(), targetSection.getLineId());
        return insert(updateSection, newSection);
    }

    private static void validateDistance(Distance targetDistance, Distance distance) {
        if (targetDistance.isShorterThan(distance)) {
            throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
        }
    }

    private Long insert(Section updateSection, Section insertSection) {
        sectionDao.update(sectionMapper.mapToEntity(updateSection));
        return sectionDao.insert(sectionMapper.mapToEntity(insertSection));
    }

    public void deleteStation(Long stationId, SectionDeleteRequest request) {
        final List<Section> sections = sectionMapper.mapFrom(sectionDao.findAllByLineId(request.getLineId()));
        final Sections sortedSections = Sections.from(sections);

        if (sortedSections.isInitialState()) {
            sectionDao.delete(sortedSections.findFirstSectionId());
            lineDao.deleteById(request.getLineId());
            return;
        }

        if (sortedSections.isDownEndPoint(stationId)) {
            sectionDao.delete(sortedSections.findFirstSectionId());
            return;
        }

        if (sortedSections.isUpEndPoint(stationId)) {
            sectionDao.delete(sortedSections.findLastSectionId());
            return;
        }

        final Sections includeTargetSection = sortedSections.findIncludeTargetSection(stationId);
        final Distance newDistance = includeTargetSection.calculateTotalDistance();

        final Section forwardSection = includeTargetSection.getSections().get(0);
        final Section backwardSection = includeTargetSection.getSections().get(1);

        final Section newSection = new Section(newDistance, forwardSection.getUpStationId(), backwardSection.getDownStationId(), request.getLineId());
        sectionDao.insert(sectionMapper.mapToEntity(newSection));
        for (Section section : includeTargetSection.getSections()) {
            sectionDao.delete(section.getId());
        }
    }
}
