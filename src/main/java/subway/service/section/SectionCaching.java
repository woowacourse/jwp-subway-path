package subway.service.section;

import org.springframework.stereotype.Component;
import subway.service.line.domain.Line;
import subway.service.section.domain.Sections;
import subway.service.section.repository.SectionRepository;

import java.util.List;
import java.util.Map;

@Component
public class SectionCaching {

    private final SectionRepository sectionRepository;
    private Map<Line, Sections> cachedSections;

    public SectionCaching(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Sections getSections(Line line) {
        if (cachedSections == null) {
            System.out.println(this + ": no caching");
            cachedSections = sectionRepository.findAllSectionsPerLine();
        }
        return cachedSections.getOrDefault(line, new Sections(List.of()));
    }

    public void clearSectionsCache() {
        System.out.println(this + " delete");
        cachedSections = null;
    }
}
