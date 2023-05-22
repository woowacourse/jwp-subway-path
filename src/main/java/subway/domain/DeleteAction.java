package subway.domain;

@FunctionalInterface
public interface DeleteAction {

    void execute(final Sections sections, Station targetStation);
}
