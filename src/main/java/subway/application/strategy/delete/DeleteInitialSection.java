package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.dao.LineDao;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
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

    public boolean support(SingleLineSections sections, Station targetStation) {
        return sections.isInitialState();
    }

    @Override
    public void delete(SingleLineSections sections, Station targetStation) {
        sectionRepository.delete(sections.findFirstSectionId());

        final Section firstSection = sections.getSections().get(0);
        lineDao.deleteById(firstSection.getLineId());
    }
}
