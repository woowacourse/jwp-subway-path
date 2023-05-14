package subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.line.dto.LineWithSectionRes;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.section.dto.SectionSaveReq;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.StationResponse;
import subway.exception.ErrorCode;
import subway.exception.GlobalException;
import subway.exception.NotFoundException;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository,
                       final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long save(final LineRequest request) {
        validateDuplicatedName(request);
        final Line requestLine = new Line(request.getName(), request.getColor());
        return lineRepository.insert(requestLine);
    }

    @Transactional
    public void update(final Long id, final LineRequest request) {
        validateDuplicatedName(request);
        final Line requestLine = new Line(request.getName(), request.getColor());
        lineRepository.updateById(id, requestLine);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void saveSection(final SectionRequest request) {
        final Line findLine = lineRepository.findById(request.getLineId());
        final List<LineWithSectionRes> lineWithSections = lineRepository.findWithSectionsByLineId(request.getLineId());
        findLine.updateSections(createSections(lineWithSections));

        final Sections sections = findLine.getSections();
        final Section requestedSection = createSection(request);
        sections.validateSections(requestedSection);

        if (sections.isNewSection(requestedSection)) {
            saveSectionFromReq(request);
            return;
        }
        updateExistedSourceSection(request, sections, requestedSection, lineWithSections);
        updateExistedTargetSection(request, sections, requestedSection, lineWithSections);
    }

    @Transactional
    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        final Line findLine = lineRepository.findById(lineId);
        final List<LineWithSectionRes> lineWithSections = lineRepository.findWithSectionsByLineId(lineId);
        findLine.updateSections(createSections(lineWithSections));

        final Sections sections = findLine.getSections();
        final Station station = createStation(stationId, lineWithSections);
        sectionRepository.deleteByLineIdAndStationId(lineId, stationId);

        sections.combineSection(station).ifPresent((newSection) -> {
            final Long sourceStationId = getStationIdByName(newSection.getSource().getName(), lineWithSections);
            final Long targetStationId = getStationIdByName(newSection.getTarget().getName(), lineWithSections);
            saveSectionOf(lineId, sourceStationId, targetStationId, newSection.getDistance());
        });
    }

    public LineResponse getByLineId(final Long lineId) {
        final Line findLine = lineRepository.findById(lineId);
        final List<LineWithSectionRes> lineWithSections = lineRepository.findWithSectionsByLineId(lineId);
        findLine.updateSections(createSections(lineWithSections));
        return createLineResponse(lineWithSections, findLine);
    }

    public List<LineResponse> getAllLines() {
        final List<LineWithSectionRes> allWithSections = lineRepository.findAllWithSections();
        final Map<Long, List<LineWithSectionRes>> sectionsByLineId = allWithSections.stream()
            .collect(Collectors.groupingBy(LineWithSectionRes::getLineId));

        return sectionsByLineId.values().stream().map(sectionRes -> {
            final Line line = createLine(sectionRes);
            return createLineResponse(sectionRes, line);
        }).collect(Collectors.toList());
    }

    private void validateDuplicatedName(final LineRequest request) {
        if (lineRepository.existByName(request.getName())) {
            throw new GlobalException(ErrorCode.LINE_NAME_DUPLICATED);
        }
    }

    private Sections createSections(final List<LineWithSectionRes> lineWithSections) {
        final List<Section> sections = lineWithSections.stream().map(res -> {
            final Station sourceStation = new Station(res.getSourceStationName());
            final Station targetStation = new Station(res.getTargetStationName());
            return new Section(sourceStation, targetStation, res.getDistance());
        }).collect(Collectors.toList());
        return new Sections(sections);
    }

    private Line createLine(final List<LineWithSectionRes> lineWithSections) {
        final Sections sections = createSections(lineWithSections);
        return new Line(lineWithSections.get(0).getLineName(),
            lineWithSections.get(0).getLineColor(), sections);
    }

    private Section createSection(final SectionRequest sectionRequest) {
        final Station sourceStation = stationRepository.findById(sectionRequest.getSourceStationId());
        final Station targetStation = stationRepository.findById(sectionRequest.getTargetStationId());
        return new Section(sourceStation, targetStation, sectionRequest.getDistance());
    }

    private void saveSectionFromReq(final SectionRequest sectionRequest) {
        final SectionSaveReq sectionSaveReq = new SectionSaveReq(sectionRequest.getLineId(),
            sectionRequest.getSourceStationId(), sectionRequest.getTargetStationId(), sectionRequest.getDistance());
        sectionRepository.insert(sectionSaveReq);
    }

    private void updateExistedSourceSection(final SectionRequest sectionRequest, final Sections sections,
                                            final Section section, final List<LineWithSectionRes> lineWithSections) {
        sections.getExistsSectionOfSource(section).ifPresent(oldSection -> {
            sectionRepository.deleteOldSection(sectionRequest.getLineId(), sectionRequest.getSourceStationId());
            saveSectionFromReq(sectionRequest);
            final SectionDistance newDistance = oldSection.getDistance().subtract(section.getDistance());
            final Long oldTargetStationId = getStationIdByName(oldSection.getTarget().getName(), lineWithSections);
            saveSectionOf(sectionRequest.getLineId(), sectionRequest.getTargetStationId(),
                oldTargetStationId, newDistance);
        });
    }

    private void updateExistedTargetSection(final SectionRequest sectionRequest, final Sections sections,
                                            final Section section, final List<LineWithSectionRes> sectionStations) {
        sections.getExistsSectionOfTarget(section).ifPresent(oldSection -> {
            sectionRepository.deleteOldSection(sectionRequest.getLineId(), sectionRequest.getSourceStationId());
            saveSectionFromReq(sectionRequest);
            final Long oldSourceStationId = getStationIdByName(oldSection.getSource().getName(), sectionStations);
            final SectionDistance newDistance = oldSection.getDistance().subtract(section.getDistance());
            saveSectionOf(sectionRequest.getLineId(), oldSourceStationId,
                sectionRequest.getSourceStationId(), newDistance);
        });
    }

    private Long getStationIdByName(final String stationName, final List<LineWithSectionRes> lineWithSections) {
        return lineWithSections.stream()
            .filter(res -> res.isSourceOrTargetStation(stationName))
            .findFirst()
            .map(res -> res.getStationIdByStationName(stationName))
            .orElseThrow(() -> new NotFoundException("역 정보가 존재하지 않습니다."));
    }

    private void saveSectionOf(final Long lineId, final Long sourceStationId,
                               final Long targetStationId, final SectionDistance distance) {
        final SectionSaveReq sectionSaveReq = new SectionSaveReq(lineId, sourceStationId, targetStationId,
            distance.getDistance());
        sectionRepository.insert(sectionSaveReq);
    }

    private LineResponse createLineResponse(final List<LineWithSectionRes> lineWithSections, final Line line) {
        final Sections sections = line.getSections();
        final List<Station> sortedStations = sections.getSortedStations();
        final List<StationResponse> stationResponses = sortedStations.stream().map(station -> {
            final Long stationId = getStationIdByName(station.getName(), lineWithSections);
            return new StationResponse(stationId, station.getName());
        }).collect(Collectors.toUnmodifiableList());
        return new LineResponse(lineWithSections.get(0).getLineId(), line.getName(), line.getColor(), stationResponses);
    }

    private Station createStation(final Long stationId, final List<LineWithSectionRes> lineWithSections) {
        return lineWithSections.stream()
            .filter(res -> res.isSourceOrTargetStation(stationId))
            .findFirst()
            .map(res -> new Station(res.getStationNameByStationId(stationId)))
            .orElseThrow(() -> new NotFoundException("역 정보가 존재하지 않습니다."));
    }
}
