# 고민 사항들

### checkExistByXX 에 대한 고민

- LineRepository에서 findById할 때, Section 및 Station 정보들까지 join 해서 가져오는 구조이다.
    - Section정보들까지는 필요 없고, id로 해당 라인이 존재하는지 아닌지만 판단하고 싶은 경우 findById 메서드를 사용하면 성능적으로 낭비이다.
    - join하지 않고, LineInfo만 담긴 Line 도메인을 가져오자니 불완전한 도메인 객체가 생기는 것 같다.
    - Dao에서는 `Optional<LineEntity>`를 반환하되, Repository에서는 도메인을 반환하는 대신 유무만 반환하는것은 어떨까?
- 하지만 Station같은 경우엔 다르다..
    - Station에서는 checkExistByXX를 사용하지 않고, findById / findByName 메서드를 활용하면 일관성이 깨지는걸까?
- 결론
    - checkExistById라는 메서드를 만들어, 유무만 확인한다.
    - checkExistByName도 마찬가지!

### 노선 이름 중복 방지 구현을 Lines라는 일급 컬렉션을 만들어서 할 것인가, 서비스에서 할 것인가?

- 일급 컬렉션에서 할 경우, "노선간 이름이 중복될 수 없다."라는 도메인 규칙 검증을 도메인에서 수행 책임을 가진다.
- 하지만, 모든 노선 정보를 가져와야한다는 성능상 단점이 있다. (노선 정보가 많을수록 더더욱..)
- 서비스에서 수행할 경우, checkExistByName 함수를 사용하면 성능상으로 우수하다.
- 결론
    - 성능을 택하기로 결정. (서비스에서 수행)

### DB에서 unique한 칼럼을 기준으로 SELECT 해 올 때, 쿼리에 LIMIT 1을 붙여주는것과 붙이지 않는 것에 성능 차이가 있을까?

- unique하지 않을 경우, LIMIT 1을 붙여주면 성능상 우수하다고 한다.
    - 해당 조건의 데이터를 찾으면 더 이상의 탐색을 진행하지 않고 멈추기 때문에.
- unique한 column을 기준으로 SELECT하는 경우에도 그럴까?
    - 챗지피티 왈..
        - `LIMIT 1을 사용하면 데이터베이스는 일치하는 첫 번째 행을 찾은 후 검색 작업을 중지할 수 있습니다. 따라서 데이터베이스 시스템은 전체 결과 집합을 처리하는 대신 첫 번째 결과만 반환하므로 성능이 향상될 수 있습니다. 특히 결과 집합이 큰 경우에는 성능 개선이 더욱 두드러질 수 있습니다.`
        - `결과 집합이 작거나 UNIQUE한 칼럼을 기준으로 조회하는 경우, 성능 차이가 크게 나타나지 않을 수 있습니다. UNIQUE한 칼럼을 기준으로 조회할 때는 해당 칼럼에 대한 인덱스가 이미 생성되어 있으므로 LIMIT 1을 사용하든 사용하지 않든 성능 차이가 크지 않을 것입니다.`

- 결론
    - 보통의 경우 LIMIT 1을 붙여 성능상 이점을 가져가자.
    - unique한 칼럼인 경우 LIMIT 1을 붙이지 않아도 성능상으로 크게 차이나지 않는다.

### Station 삭제시, 해당 역이 존재하는 구간을 전부 지워줄 때 도메인과 엔티티의 괴리감

- 현재 도메인 구조상 Section은 Line을 모른다. (Line이 Sections를 가짐.)
- 하지만, RemoveStationFromLineUseCased를 사용하기 위해서는 lineId와 stationId를 알아야한다.
- SectionDao 에서 findByStationId로 `List<Section>`을 가져올 수 있지만 여기는 Line 정보를 담고 있지 않다.
- 이 정보를 어떻게 가져와야할까..? Line 도메인으로 가져오기엔, 다른 Section 정보는 담고있지 않기 때문에 불완전한 도메인이 된다.
- `Map<Line, Section> lineToSection`이나, `Map<Long, Section> lineIdToSection`으로 가져올 수 있을 것 같다.
    - 전자의 경우, 또 Sections 정보가 불완전하기 때문에좋은 구조가 아닌 것 같다.
    - 후자의 경우, lineId를 포장해주지 않았기 때문에 타입이 Long이다. -> LineId 라고 포장했으면 도메인 정보임이 나타났을텐데, 원시값이다 보니 도메인 정보라는게 드러나지 않는다.
        - 하지만, `lineIdToSection`이라는 변수명으로 나타낼 수 있지 않을까? (값객체 포장을 해줬으면 좋겠지만, 이는 복잡도가 증가하기 때문에 트레이드-오프라 생각한다.)
- stationId로 `Map<Long, Section> lineIdToSection`를 반환하는 것은 lineRepository의 책임일까, sectionRepository의 책임일까?
    - Line이 Section을 가지는 개념이기 때문에, 더 큰 개념인 LineRepository에서 수행하도록 하자.
- 이렇게 하니, 또 완벽한 Section을 알아내기 위해 join문이 필요해졌다. (Station 정보)
    - 우리가 필요한 건 LineId와 StationId 뿐
    - `Map<Long, Long> lineIdToStationId`는 어떨까?
        - 이게 도메인 설계적으로 좋지 않은 것은 분명하다.. 상위 계층에서 어떻게 사용될지를 고려한 메서드.
- 생각해보니, 우리는 Station을 포함하는 Line의 정보만 알면 된다.
    - LineRepository에서 해당 역을 포함하는 line 리스트만 뽑아오는 것이 간단하다.
    - 이 때, 또 Line 전부를 가져올것이냐 Id들만 가져올것이냐에 대한 고민이 있지만.. Id 리스트만 가져오도록 하자. (join 연산에 대한 비용이 크다.)
- 결론
    - lineRepository에서 해당 Station을 포함하는 `List<Long> lineIds`를 반환하도록 하자.

### 도메인 Route에 대한 고민들

- JGrpahT 라이브러리를 통해서 손쉽게 `List<Station>` 과 총 거리를 구할 수 있다.
- 하지만 확장성을 고려했을 때, 우리가 Route 도메인을 통해 얻고싶은 정보는 단순 역과 거리 정보가 아닐 수 있다.
- 어떤 역에서 어떤 역으로 몇호선을 타고 가는지, 그리고 그 거리는 얼마인지 등을 알고싶을 것이다.
- RouteSection이라는 도메인을 도출해내고, 이의 일급 컬렉션을 Route라 정의했다.
- 하지만 이렇게 하면, 총 거리를 구하는 일과 거처간 역을 구하는 과정을 다시한 번 수동으로 진행해야한다.
- 큰 비용이 들지는 않을 것이라 생각한다..
- 한 번 더 귀찮은 작업이 있지만, 이게 도메인적으로 옳은 구조인 것 같다.
