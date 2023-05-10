package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.service.dto.LineRegisterRequest;

import java.util.Optional;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionService sectionService;

    public LineService(final LineDao lineDao, final SectionService sectionService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    public void registerLine(final LineRegisterRequest lineRegisterRequest) {

        final Optional<LineEntity> savedLine =
                lineDao.findLineByName(lineRegisterRequest.getLineName());

        if (savedLine.isPresent()) {
            throw new IllegalArgumentException("이미 해당 라인을 가진 이름은 존재합니다.");
        }

        final Long savedLineId = lineDao.save(new LineEntity(lineRegisterRequest.getLineName()));

        sectionService.registerFirstSection(lineRegisterRequest, savedLineId);
    }
}
