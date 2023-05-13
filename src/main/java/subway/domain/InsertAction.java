package subway.domain;

@FunctionalInterface
public interface InsertAction {

    void execute(final Sections sections, Section targetSection);
}
