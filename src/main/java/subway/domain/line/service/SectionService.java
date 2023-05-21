package subway.domain.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.dao.SectionDao;
import subway.domain.line.domain.SectionSelector;
import subway.domain.line.dto.StationRegisterRequest;
import subway.domain.line.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<SectionEntity> findByLineId(Long lindId) {
        return sectionDao.findByLineId(lindId);
    }

    public List<SectionEntity> findAll() {
        return sectionDao.findAll();
    }

    @Transactional
    public List<SectionEntity> createSection(Long lineId, StationRegisterRequest request) {
        SectionEntity section = new SectionEntity(
                lineId,
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );

        if (sectionDao.isEmpty(lineId)) {
            SectionEntity insert = sectionDao.insert(section);
            return List.of(insert);
        }

        Optional<SectionEntity> upSectionOfUpStation = sectionDao.findByUpStationId(lineId, request.getUpStationId());
        Optional<SectionEntity> downSectionOfUpStation = sectionDao.findByDownStationId(lineId, request.getUpStationId());
        Optional<SectionEntity> upSectionOfDownStation = sectionDao.findByUpStationId(lineId, request.getDownStationId());
        Optional<SectionEntity> downSectionOfDownStation = sectionDao.findByDownStationId(lineId, request.getDownStationId());

        SectionSelector sectionSelector = new SectionSelector(upSectionOfUpStation, downSectionOfUpStation, upSectionOfDownStation, downSectionOfDownStation);

        if (sectionSelector.isNotFoundSection()) {
            throw new IllegalArgumentException("해당 구간이 존재하지 않습니다.");
        }

        if (sectionSelector.isEndSection()) {
            SectionEntity insert = sectionDao.insert(section);
            return List.of(insert);
        }

        if (sectionSelector.isUpSection()) {
            SectionEntity insert = sectionDao.insert(section);
            SectionEntity section1 = downSectionOfDownStation.get().setDownStationId(request.getUpStationId())
                    .setDistance(downSectionOfDownStation.get().getDistance() - request.getDistance());
            sectionDao.update(section1);
            return List.of(insert, section1);
        }

        if (sectionSelector.isDownSection()) {
            SectionEntity insert = sectionDao.insert(section);
            SectionEntity section1 = upSectionOfUpStation.get().setUpStationId(request.getDownStationId())
                    .setDistance(upSectionOfUpStation.get().getDistance() - request.getDistance());
            sectionDao.update(section1);
            return List.of(insert, section1);
        }

        throw new IllegalArgumentException("해당 구간이 존재하지 않습니다.");
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Optional<SectionEntity> up = sectionDao.findByUpStationId(lineId, stationId);
        Optional<SectionEntity> down = sectionDao.findByDownStationId(lineId, stationId);

        if (up.isEmpty() && down.isEmpty()) {
            throw new IllegalArgumentException("등록되어있지 않은 역은 지울 수 없습니다.");
        }

        if (up.isPresent() && down.isEmpty()) {
            sectionDao.deleteById(up.get().getId());
            return;
        }

        if (up.isEmpty() && down.isPresent()) {
            sectionDao.deleteById(down.get().getId());
            return;
        }

        SectionEntity updateSection = down.get().setUpStationId(up.get().getUpStationId())
                .setDistance(down.get().getDistance() + up.get().getDistance());

        sectionDao.update(updateSection);
        sectionDao.deleteById(up.get().getId());
    }
}
