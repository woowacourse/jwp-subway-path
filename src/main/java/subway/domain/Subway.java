package subway.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class Subway {
    private final List<Sections> sections;

    public Subway() {
        this.sections = new ArrayList<>();
    }

    public void createSectionsOf(final Line line) {
        final Sections newSections = new Sections(line);
        sections.add(newSections);
    }

    public void createNewSection(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Sections newSections = findSectionsOf(line);
        sections.add(newSections);
        newSections.createInitialSection(upStation, downStation, distance);
    }

    public Station addStation(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Sections sections = findSectionsOf(line);
        return sections.addStation(upStation, downStation, distance);
    }

    public Sections findSectionsOf(final Line line) {
        return sections.stream().filter(element -> element.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 노선이 존재하지 않습니다."));
    }

    public List<Station> findStationsInOrderOf(final Line line) {
        final Sections sections = findSectionsOf(line);
        return sections.findAllStationsInOrder();
    }

    public Map<List<Station>, Integer> findAllSectionsInOrder(final Line line) {
        final Sections sections = findSectionsOf(line);
        return sections.findAllSectionsInOrder();
    }

    public int findDistanceBetween(final Line line, final Station upStation, final Station downStation) {
        final Sections sections = findSectionsOf(line);
        System.out.println("sections = " + sections);

        if (!sections.containsStation(upStation) || !sections.containsStation(downStation)) {
            System.out.println(sections.containsStation(upStation));
            System.out.println(sections.containsStation(downStation));
            throw new IllegalArgumentException("존재하지 않는 역입니다!");
        }
        return sections.findDistanceBetween(upStation, downStation);
    }

    public Station findStationBefore(final Line line, final Station station) {
        final Sections sections = findSectionsOf(line);
        return sections.findStationBefore(station);
    }

    public Station findStationAfter(final Line line, final Station station) {
        final Sections sections = findSectionsOf(line);
        return sections.findStationAfter(station);
    }
}
