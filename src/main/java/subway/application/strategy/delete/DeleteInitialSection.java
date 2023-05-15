package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.dao.LineDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

@Component
public class DeleteInitialSection implements DeleteStationStrategy {

    private final SectionRepository sectionRepository;
    private final LineDao lineDao;

    public DeleteInitialSection(SectionRepository sectionRepository, LineDao lineDao) {
        this.sectionRepository = sectionRepository;
        this.lineDao = lineDao;
    }

    @Override

    public boolean support(Sections sections, Station targetStation) {
        return sections.isInitialState();
    }

    @Override
    public void delete(Sections sections, Station targetStation) {
        sectionRepository.delete(sections.findFirstSectionId());

        final Section firstSection = sections.getSections().get(0);
        lineDao.deleteById(firstSection.getLineId());
    }
}
