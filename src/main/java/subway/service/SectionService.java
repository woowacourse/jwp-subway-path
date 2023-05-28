package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.Line;
import subway.service.dto.response.SectionInLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void updateLine(
            final LineEntity lineEntity,
            final Line line
    ) {
        sectionDao.deleteAll(lineEntity.getId());

        sectionDao.batchSave(
                line.getSections()
                    .stream()
                    .map(it -> new SectionEntity(
                            it.getStations().getCurrent().getName(),
                            it.getStations().getNext().getName(),
                            it.getStations().getDistance(),
                            lineEntity.getId())
                    )
                    .collect(Collectors.toList())
        );
    }

    public void deleteAll(final Long lineId) {
        sectionDao.deleteAll(lineId);
    }

    public void registerSection(
            final String currentStationName,
            final String nextStationName,
            final int distance,
            final Long lineId
    ) {

        final SectionEntity sectionEntity = new SectionEntity(
                currentStationName,
                nextStationName,
                distance,
                lineId
        );

        sectionDao.batchSave(List.of(sectionEntity));
    }

    public List<SectionInLineResponse> mapToSectionInLineResponseFrom(final Line line) {
        return line.getSections()
                   .stream()
                   .map(it -> new SectionInLineResponse(
                           it.getStations().getCurrent().getName(),
                           it.getStations().getNext().getName(),
                           it.getStations().getDistance()))
                   .collect(Collectors.toList());
    }
}
