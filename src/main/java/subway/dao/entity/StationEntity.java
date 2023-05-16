package subway.dao.entity;

public class StationEntity {
    private Long id;
    private String name;

    private StationEntity() {
    }

    /**
     * insert를 할 떄
     * TODO 삭제하기 -> insert문 두 개로
     */
    public StationEntity(Long id) {
        this(id, null);
    }

    public StationEntity(String name) {
        this(null, name);
    }

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
