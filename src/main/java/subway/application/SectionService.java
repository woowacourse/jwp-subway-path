package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public SectionService(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public Long insertSection(SectionRequest request) {
        if (request.getUpStationId() == request.getDownStationId()) {
            throw new IllegalArgumentException("같은 역을 구간으로 등록할 수 없습니다.");
        }

        // 기존에 section이 등록되어있는지 확인한다.
        if (sectionDao.exists(request.getDownStationId(), request.getUpStationId())
                || sectionDao.exists(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("동일한 구간을 추가할 수 없습니다.");
        }

        // 역이 있는지 여부를 확인
        final List<Section> sections = sectionDao.findAllByLineId(request.getLineId());

        final List<Section> sortedSections = sortSections(sections);

        // 둘 중의 하나만 있어야 한다.
        final Long upStationId = request.getUpStationId();
        final Long downStationId = request.getDownStationId();

        final Set<Long> stationIds = sortedSections.stream()
                .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                .collect(Collectors.toSet());

        // TODO 리펙토링
        int count = 0;
        for (Long stationId : stationIds) {
            if (stationId == upStationId || stationId == downStationId) {
                count++;
            }
        }
        if (count != 1) {
            throw new RuntimeException();
        }

        // 양 끝인 경우 확인
        // TODO 빌더 패턴으로 변경해보기
        if (isUpStationPoint(sortedSections, upStationId) || isDownStationPoint(sortedSections, downStationId)) {
            final Section section = new Section(request.getDistance(), request.getUpStationId(), request.getDownStationId(), request.getLineId());
            return sectionDao.insert(section);
        }

        // 중간인 경우
        // 상행역 기준
        if (isUpStationPoint(sortedSections, upStationId)) {
            final Section targetSection = sortedSections.stream()
                    .filter(section -> section.getUpStationId() == upStationId)
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));

            final int targetDistance = targetSection.getDistance();
            final Integer distance = request.getDistance();

            if (targetDistance <= distance) {
                throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
            }

            final Section updateSection = new Section(distance, targetSection.getUpStationId(), downStationId, targetSection.getLineId());
            sectionDao.update(updateSection);

            final Section newSection = new Section(targetDistance - distance, downStationId, targetSection.getDownStationId(), targetSection.getLineId());
            sectionDao.insert(newSection);
        }

        // 하행역 기준
        if (isDownStationPoint(sortedSections, downStationId)) {
            final Section targetSection = sortedSections.stream()
                    .filter(section -> section.getDownStationId() == downStationId)
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));

            final int targetDistance = targetSection.getDistance();
            final Integer distance = request.getDistance();

            if (targetDistance <= distance) {
                throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
            }

            final Section updateSection = new Section(distance, upStationId, downStationId, targetSection.getLineId());
            sectionDao.update(updateSection);

            final Section newSection = new Section(targetDistance - distance, targetSection.getUpStationId(), upStationId, targetSection.getLineId());
            sectionDao.insert(newSection);
        }
        return null;
    }

    private static boolean isDownStationPoint(List<Section> sortedSections, Long downStationId) {
        return downStationId == sortedSections.get(0).getUpStationId();
    }

    private static boolean isUpStationPoint(List<Section> sortedSections, Long upStationId) {
        return upStationId == sortedSections.get(sortedSections.size() - 1).getDownStationId();
    }

    public void deleteStation(SectionDeleteRequest request) {
        final List<Section> sections = sectionDao.findAllByLineId(request.getLineId());
        final List<Section> sortedSections = sortSections(sections);
        // 구간이 1개이면
        if (sortedSections.size() == 1) {
            sectionDao.delete(sortedSections.get(0).getId());
            lineDao.deleteById(request.getLineId());
            return;
        }

        if (isUpStationPoint(sortedSections, request.getStationId())) {
            sectionDao.delete(sortedSections.get(0).getId());
            return;
        }

        if (isDownStationPoint(sortedSections, request.getStationId())) {
            sectionDao.delete(sortedSections.get(sortedSections.size() - 1).getId());
            return;
        }

        final List<Section> includeSections = sortedSections.stream()
                .filter(section -> section.getUpStationId() == request.getStationId() || section.getDownStationId() == request.getStationId())
                .collect(Collectors.toList());
        final int newDistance = includeSections.stream()
                .mapToInt(section -> section.getDistance())
                .sum();
        sectionDao.insert(new Section(newDistance, includeSections.get(0).getUpStationId(), includeSections.get(1).getDownStationId(), request.getLineId()));

        for (Section section : includeSections) {
            sectionDao.delete(section.getId());
        }
    }

    private List<Section> sortSections(List<Section> sections) {
        final Long firstStationId = getFirstStations(sections);
        List<Section> result = new ArrayList<>();
        Long next = firstStationId;
        for (int i = 0; i < sections.size(); i++) {
            next = addNextSection(sections, result, next);
        }
        return result;
    }

    private Long getFirstStations(List<Section> sections) {
        final Set<Long> allStationIds = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                .collect(Collectors.toSet());

        final Set<Long> downStationIds = sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toSet());

        final List<Long> stationIds = new ArrayList<>(allStationIds);
        stationIds.removeAll(downStationIds);

        return stationIds.get(0);
    }

    private Long addNextSection(List<Section> sections, List<Section> result, Long nextStationId) {
        for (Section section : sections) {
            if (section.getUpStationId().equals(nextStationId)) {
                nextStationId = section.getDownStationId();
                result.add(section);
                break;
            }
        }
        return nextStationId;
    }
}
