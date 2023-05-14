package subway2.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import subway2.dao.LineDao;
import subway2.dao.SectionDao;
import subway2.dao.SectionEntity;
import subway2.dao.StationDao;
import subway2.dto.SectionRequest;
import subway2.exception.InvalidSectionException;

@Service
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        checkIfExistLine(lineId);
        checkIfExistStation(sectionRequest.getUpStationId());
        checkIfExistStation(sectionRequest.getDownStationId());
        SectionEntity sectionEntity = new SectionEntity(lineId, sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(), sectionRequest.getDistance());

        if (isEmptyLine(lineId)) {
            sectionDao.insert(sectionEntity);
            return;
        }
        saveSectionWhenLineIsNotEmpty(sectionEntity);
    }

    private boolean isEmptyLine(Long lineId) {
        Optional<List<SectionEntity>> byLineId = sectionDao.findByLineId(lineId);
        return byLineId.get().isEmpty();
    }

    private void saveSectionWhenLineIsNotEmpty(SectionEntity sectionEntity) {
        checkIfImpossibleSection(sectionEntity);
        addStationBetweenStations(sectionEntity);
    }

    private void checkIfImpossibleSection(SectionEntity sectionEntity) {
        Long lineId = sectionEntity.getLineId();
        Long upStationId = sectionEntity.getUpStationId();
        Long downStationId = sectionEntity.getDownStationId();
        boolean hasUpStationInLine = hasStationInLine(upStationId, lineId);
        boolean hasDownStationInLine = hasStationInLine(downStationId, lineId);

        if (hasUpStationInLine && hasDownStationInLine) {
            throw new InvalidSectionException("이미 존재하는 구간입니다.");
        }
    }

    private boolean hasStationInLine(Long stationId, Long lineId) {
        return sectionDao.findByUpStationId(stationId, lineId).isPresent()
                || sectionDao.findByDownStationId(stationId, lineId).isPresent();
    }

    private void addStationBetweenStations(SectionEntity sectionEntity) {
        if (hasStationInLine(sectionEntity.getUpStationId(), sectionEntity.getLineId())) {
            addSectionBasedOnUpStation(sectionEntity.getUpStationId(), sectionEntity);
            return;
        }
        if (hasStationInLine(sectionEntity.getDownStationId(), sectionEntity.getLineId())) {
            addSectionBasedOnDownStation(sectionEntity.getDownStationId(), sectionEntity);
            return;
        }
        throw new InvalidSectionException("한 역은 기존의 노선에 존재해야 합니다.");
    }

    public void addSectionBasedOnUpStation(Long upStationId, SectionEntity sectionToAdd) {
        Long lineId = sectionToAdd.getLineId();
        Optional<SectionEntity> originalSectionEntity = sectionDao.findByUpStationId(upStationId, lineId);
        if (originalSectionEntity.isPresent()) {
            SectionEntity originalSection = originalSectionEntity.get();
            Long downStationIdOfOrigin = originalSection.getDownStationId();
            Long downStationIdOfToAdd = sectionToAdd.getDownStationId();
            int revisedDistance = findRevisedDistance(sectionToAdd, originalSection);
            SectionEntity revisedSection = new SectionEntity(lineId, downStationIdOfToAdd, downStationIdOfOrigin,
                    revisedDistance);
            sectionDao.updateByDownStationId(revisedSection);
            sectionDao.insert(sectionToAdd);
            return;
        }

        if (sectionDao.findByDownStationId(upStationId, lineId).isPresent()) {
            sectionDao.insert(sectionToAdd);
        }
    }

    public void addSectionBasedOnDownStation(Long downStationId, SectionEntity sectionToAdd) {
        Long lineId = sectionToAdd.getLineId();
        if (sectionDao.findByUpStationId(downStationId, lineId).isPresent()) {
            sectionDao.insert(sectionToAdd);
            return;
        }
        Optional<SectionEntity> originalSectionEntity = sectionDao.findByDownStationId(downStationId, lineId);
        if (originalSectionEntity.isPresent()) {
            SectionEntity originalSection = originalSectionEntity.get();
            Long upStationIdOfOrigin = originalSection.getUpStationId();
            Long upStationIdOfToAdd = sectionToAdd.getUpStationId();
            int revisedDistance = findRevisedDistance(sectionToAdd, originalSection);

            SectionEntity revisedSection = new SectionEntity(lineId, upStationIdOfOrigin, upStationIdOfToAdd,
                    revisedDistance);
            sectionDao.updateByUpStationId(revisedSection);
            sectionDao.insert(sectionToAdd);
        }
    }

    private static int findRevisedDistance(SectionEntity sectionToAdd, SectionEntity originalSection) {
        int revisedDistance = originalSection.getDistance() - sectionToAdd.getDistance();
        if (revisedDistance <= 0) {
            throw new InvalidSectionException("현재 구간보다 큰 구간은 입력할 수 없습니다.");
        }
        return revisedDistance;
    }

    public void removeStationFromLine(Long lineId, Long stationIdToRemove) {
        checkIfExistLine(lineId);
        checkIfExistStation(stationIdToRemove);
        if (hasStationInLine(stationIdToRemove, lineId)) {
            deleteStationBetweenStations(lineId, stationIdToRemove);
            return;
        }
        Optional<SectionEntity> upStationEntity = sectionDao.findByUpStationId(stationIdToRemove, lineId);
        if (upStationEntity.isPresent()) {
            SectionEntity upSectionOfOrigin = upStationEntity.get();
            sectionDao.delete(upSectionOfOrigin);
            return;
        }
        Optional<SectionEntity> downStationEntity = sectionDao.findByDownStationId(stationIdToRemove, lineId);
        if (downStationEntity.isPresent()) {
            SectionEntity downSectionOfOrigin = downStationEntity.get();
            sectionDao.delete(downSectionOfOrigin);
            return;
        }
        throw new NotFoundException("노선에 역이 존재하지 않습니다.");
    }

    private void deleteStationBetweenStations(Long lineId, Long stationIdToRemove) {
        SectionEntity upSectionOfOrigin = sectionDao.findByUpStationId(stationIdToRemove, lineId).get();
        SectionEntity downSectionOfOrigin = sectionDao.findByDownStationId(stationIdToRemove, lineId).get();
        int revisedDistance = upSectionOfOrigin.getDistance() + downSectionOfOrigin.getDistance();

        SectionEntity revisedSection = new SectionEntity(lineId, upSectionOfOrigin.getUpStationId(),
                downSectionOfOrigin.getDownStationId(), revisedDistance);

        sectionDao.updateByUpStationId(revisedSection);
        sectionDao.delete(downSectionOfOrigin);
    }


    private void checkIfExistLine(Long lineId) {
        lineDao.findById(lineId).orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다."));
    }

    private void checkIfExistStation(Long stationId) {
        stationDao.findById(stationId).orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
    }

}
