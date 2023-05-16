package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;
import subway.dto.StationResponse;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(final LineRequest lineRequest) {
        if (isExisted(lineRequest)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
        return lineDao.save(new LineEntity(lineRequest.getName()));
    }

    @Transactional(readOnly = true)
    public boolean isExisted(final LineRequest lineRequest) {
        try {
            lineDao.findByName(lineRequest.getName());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public LineResponse findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        Line line = Line.of(lineEntity.getName(), makeSections(id));
        List<StationResponse> stationsResponse = line.getStations().stream()
                .map(station -> new StationResponse(station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(lineEntity.getId(), lineEntity.getName(), stationsResponse);
    }

    private List<Section> makeSections(final Long id) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        return sectionEntities.stream()
                .map(entity -> new Section(
                        new Station(stationDao.findById(entity.getUpStationId()).getName()),
                        new Station(stationDao.findById(entity.getDownStationId()).getName()),
                        entity.getDistance()
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                .map(lineEntity -> findById(lineEntity.getId()))
                .collect(Collectors.toList());
    }

    public Long createSectionInLine(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        SectionEntity sectionEntity = new SectionEntity(
                sectionCreateRequest.getUpStationId(),
                sectionCreateRequest.getDownStationId(),
                lineId,
                sectionCreateRequest.getDistance()
        );

        StationEntity upStationEntity = stationDao.findById(sectionCreateRequest.getUpStationId());
        StationEntity downStationEntity = stationDao.findById(sectionCreateRequest.getDownStationId());

        Line line = makeLine(lineId);
        List<Section> originalSections = line.getSectionsByList();

        line.addSection(
                new Station(upStationEntity.getName()),
                new Station(downStationEntity.getName()),
                sectionEntity.getDistance()
        );

        Long newSectionId = saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);

        return newSectionId;
    }

    private Line makeLine(final long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<Section> sections = makeSections(lineId);

        return Line.of(lineEntity.getName(), sections);
    }

    private Long saveNewSections(final long lineId, final List<Section> originalSections, final Line line) {
        List<Section> newSections = line.getSectionsByList();
        newSections.removeAll(originalSections);

        Section newSection = newSections.get(0);
        StationEntity newUpStation = stationDao.findByName(newSection.getUpStation().getName());
        StationEntity newDownStation = stationDao.findByName(newSection.getDownStation().getName());

        return sectionDao.save(new SectionEntity(
                newUpStation.getId(),
                newDownStation.getId(),
                lineId,
                newSection.getDistance())
        );
    }

    private void deleteOriginalSection(final long lineId, final List<Section> originalSections, final Line line) {
        List<Section> newSections = line.getSectionsByList();
        originalSections.removeAll(newSections);

        if (originalSections.isEmpty()) {
            return;
        }

        Section deletedSection = originalSections.get(0);
        StationEntity deletedUpStation = stationDao.findByName(deletedSection.getUpStation().getName());
        StationEntity deletedDownStation = stationDao.findByName(deletedSection.getDownStation().getName());

        sectionDao.delete(new SectionEntity(
                deletedUpStation.getId(),
                deletedDownStation.getId(),
                lineId,
                deletedSection.getDistance())
        );
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId);
        Line line = makeLine(lineId);
        List<Section> originalSections = line.getSectionsByList();

        line.deleteStation(new Station(stationEntity.getName()));

        if (line.isEmpty()) {
            sectionDao.deleteByLineId(lineId);
            lineDao.deleteById(lineId);
            return;
        }

        saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);
    }
}
