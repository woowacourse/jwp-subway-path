package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class SectionService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository, SectionRepository sectionRepository,
                          StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public Long save(SectionCreateRequest sectionCreateRequest) {
        Line line = lineRepository.findById(sectionCreateRequest.getLineId());
        Station upStation = stationRepository.findByName(sectionCreateRequest.getUpStation());
        Station downStation = stationRepository.findByName(sectionCreateRequest.getDownStation());

        line.addSection(upStation, downStation, sectionCreateRequest.getDistance());

        sectionRepository.deleteByLineId(line.getId());
        sectionRepository.batchSave(line);

        return line.getId();
    }

    public void delete(SectionDeleteRequest sectionDeleteRequest) {
        Line line = lineRepository.findById(sectionDeleteRequest.getLineId());
        Station station = stationRepository.findByName(sectionDeleteRequest.getName());

        line.deleteSection(station);

        sectionRepository.deleteByLineId(line.getId());
        sectionRepository.batchSave(line);
    }
}
