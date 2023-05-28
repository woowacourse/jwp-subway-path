package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private String name;
    private Section starter;

    public Line(final String name) {
        this.name = name;
    }

    public void add(Section newSection) {
        if (starter == null) {
            starter = newSection;
            return;
        }

        final Section targetSection = starter.findPreSectionOnAdd(newSection);

        if (canExchangeStarter(targetSection)) {
            if (starter.isLinked(newSection)) {
                exchangeStarterOnAdd(newSection);
                return;
            }
            throw new IllegalArgumentException("해당 섹션은 현재 Line에 추가할 수 없습니다.");
        }

        starter.addNext(newSection);
    }

    private boolean canExchangeStarter(final Section targetSection) {
        return targetSection == null;
    }

    private void exchangeStarterOnAdd(final Section newSection) {
        newSection.updateNextSection(starter);
        starter = newSection;
    }

    public void deleteTargetStation(final Station deletedStation) {
        final Section target = starter.findPreSectionOnDelete(deletedStation);

        if (canDeleteLine(target)) {
            starter = null;
            return;
        }

        if (canExchangeStarter(target)) {
            if (starter.isSameCurrentWith(deletedStation)) {
                exchangeStarterOnDelete();
                return;
            }
            throw new IllegalArgumentException("삭제할 역이 없습니다.");
        }

        if (canExchangeTail(target)) {
            Section newLastSection = findPreSection(target);
            newLastSection.disconnectNextSection();
            return;
        }

        starter.delete(deletedStation);
    }

    private boolean canDeleteLine(final Section target) {
        return target == starter && target.getTo() == null;
    }

    private void exchangeStarterOnDelete() {
        final Section newStarter = starter.getTo();
        starter.disconnectNextSection();
        starter = newStarter;
    }

    private boolean canExchangeTail(final Section target) {
        return target.getTo() == null;
    }

    private Section findPreSection(Section targetSection) {
        Section current = starter;

        while (current.getTo() != targetSection) {
            current = current.getTo();
        }
        return current;
    }

    public boolean isDeleted() {
        return starter == null;
    }

    public List<Section> getSections() {
        Section current = starter;

        List<Section> sections = new ArrayList<>();

        while (current != null) {
            sections.add(current);

            current = current.getTo();
        }

        return sections;
    }

    public Section getStarter() {
        return starter;
    }

    public String getName() {
        return name;
    }
}
