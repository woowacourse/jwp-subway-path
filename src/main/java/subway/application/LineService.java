package subway.application;

import static subway.application.mapper.LineMapper.createLine;
import static subway.application.mapper.LineMapper.createLineResponse;
import static subway.application.mapper.SectionMapper.createSubwayLine;
import static subway.application.mapper.SectionMapper.getSectionsByLineId;
import static subway.application.mapper.StationMapper.createStation;
import static subway.application.mapper.StationMapper.getStationIdByName;
import static subway.exception.ErrorCode.LINE_NAME_DUPLICATED;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.application.dto.SectionRequest;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.line.LineWithSectionRes;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.section.SectionRepository;
import subway.domain.section.SubwayLine;
import subway.domain.section.dto.SectionSaveReq;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.exception.BadRequestException;

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
        final Line requestLine = createLine(request);
        return lineRepository.insert(requestLine);
    }

    @Transactional
    public void update(final Long id, final LineRequest request) {
        validateDuplicatedName(request);
        final Line requestLine = createLine(request);
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
        findLine.updateSubwayLine(createSubwayLine(lineWithSections));

        final SubwayLine subwayLine = findLine.subwayLine();
        final Section requestedSection = createSection(request);
        subwayLine.validateSections(requestedSection);

        if (subwayLine.isNewSection(requestedSection)) {
            saveSectionFromReq(request);
            return;
        }
        updateExistedSourceSection(request, subwayLine, requestedSection, lineWithSections);
        updateExistedTargetSection(request, subwayLine, requestedSection, lineWithSections);
    }

    @Transactional
    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        final Line findLine = lineRepository.findById(lineId);
        final List<LineWithSectionRes> lineWithSections = lineRepository.findWithSectionsByLineId(lineId);
        findLine.updateSubwayLine(createSubwayLine(lineWithSections));

        final SubwayLine subwayLine = findLine.subwayLine();
        final Station station = createStation(stationId, lineWithSections);
        sectionRepository.deleteByLineIdAndStationId(lineId, stationId);

        subwayLine.combineSection(station).ifPresent((newSection) -> {
            final Long sourceStationId = getStationIdByName(newSection.source().name(), lineWithSections);
            final Long targetStationId = getStationIdByName(newSection.target().name(), lineWithSections);
            saveSectionOf(lineId, sourceStationId, targetStationId, newSection.distance());
        });
    }

    public LineResponse getByLineId(final Long lineId) {
        final Line findLine = lineRepository.findById(lineId);
        final List<LineWithSectionRes> lineWithSections = lineRepository.findWithSectionsByLineId(lineId);
        findLine.updateSubwayLine(createSubwayLine(lineWithSections));
        return createLineResponse(lineWithSections, findLine);
    }

    public List<LineResponse> getAllLines() {
        final List<LineWithSectionRes> allWithSections = lineRepository.findAllWithSections();
        final Map<Long, List<LineWithSectionRes>> sectionsByLineId = getSectionsByLineId(allWithSections);
        return sectionsByLineId.values().stream().map(sectionRes -> {
            final Line line = createLine(sectionRes);
            return createLineResponse(sectionRes, line);
        }).collect(Collectors.toList());
    }

    private void validateDuplicatedName(final LineRequest request) {
        if (lineRepository.existByName(request.getName())) {
            throw new BadRequestException(LINE_NAME_DUPLICATED);
        }
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

    private void updateExistedSourceSection(final SectionRequest sectionRequest, final SubwayLine subwayLine,
                                            final Section section, final List<LineWithSectionRes> lineWithSections) {
        subwayLine.getExistsSectionOfSource(section).ifPresent(oldSection -> {
            sectionRepository.deleteOldSection(sectionRequest.getLineId(), sectionRequest.getSourceStationId());
            saveSectionFromReq(sectionRequest);
            final SectionDistance newDistance = oldSection.distance().subtract(section.distance());
            final Long oldTargetStationId = getStationIdByName(oldSection.target().name(), lineWithSections);
            saveSectionOf(sectionRequest.getLineId(), sectionRequest.getTargetStationId(),
                oldTargetStationId, newDistance);
        });
    }

    private void updateExistedTargetSection(final SectionRequest sectionRequest, final SubwayLine subwayLine,
                                            final Section section, final List<LineWithSectionRes> sectionStations) {
        subwayLine.getExistsSectionOfTarget(section).ifPresent(oldSection -> {
            sectionRepository.deleteOldSection(sectionRequest.getLineId(), sectionRequest.getSourceStationId());
            saveSectionFromReq(sectionRequest);
            final Long oldSourceStationId = getStationIdByName(oldSection.source().name(), sectionStations);
            final SectionDistance newDistance = oldSection.distance().subtract(section.distance());
            saveSectionOf(sectionRequest.getLineId(), oldSourceStationId,
                sectionRequest.getSourceStationId(), newDistance);
        });
    }

    private void saveSectionOf(final Long lineId, final Long sourceStationId,
                               final Long targetStationId, final SectionDistance distance) {
        final SectionSaveReq sectionSaveReq = new SectionSaveReq(lineId, sourceStationId, targetStationId,
            distance.distance());
        sectionRepository.insert(sectionSaveReq);
    }
}
