package subway.domain;

public class Line {

    private Long id;
    private String name;
    private Section starter;

    public Line(final String name, final Section starter) {
        validateNotNull(starter);
        this.name = name;
        this.starter = starter;
    }

    private void validateNotNull(final Section section) {
        if (section == null) {
            throw new IllegalArgumentException("섹션이 없다면 Line을 생성할 수 없습니다.");
        }
    }

    public void add(Section newSection) {
        final Section head = starter.find(newSection);

        if (head == null) {
            if (starter.isLinked(newSection)) {
                newSection.updateNextSection(starter);
                starter = newSection;
                return;
            }
            throw new IllegalArgumentException("해당 섹션은 현재 Line에 추가할 수 없습니다.");
        }

        starter.addNext(newSection);
    }

    public void delete(final Station deletedStation) {
        final Section head = starter.find(deletedStation);

        if (head == null) {
            if (starter.isSameCurrentWith(deletedStation)) {
                exchangeStarter();
                return;
            }
            throw new IllegalArgumentException("삭제할 역이 없습니다.");
        }

        starter.delete(deletedStation);
    }

    private void exchangeStarter() {
        starter.disconnectNextSection();
        starter = starter.getTo();
    }

    public Section getStarter() {
        return starter;
    }

    public String getName() {
        return name;
    }
}
