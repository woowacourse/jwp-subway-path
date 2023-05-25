package subway.line.dao;

public class LineEntity {

  private Long id;
  private final String name;

  public LineEntity(final Long id, final String name) {
    this.id = id;
    this.name = name;
  }

  public LineEntity(final String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
