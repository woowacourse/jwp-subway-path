# jwp-subway-path

## 요구사항

### API 요구사항

- [ ] 노선에 역 등록 API 신규 구현
- [ ] 노선에 역 제거 API 신규 구현
- [ ] 노선 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선
- [ ] 노선 목록 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선

### 도매인 요구사항

#### 노선 역 등록

##### 노선 등록 정보

- [ ] 등록되는 역
- [ ] 역의 위치
- [ ] 거리

##### 노선 등록 조건

- [ ] 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야한다.
- [ ] 하나의 역은 여러 노선에 등록이 될 수 있다.
- [ ] 노선은 갈래길을 가질 수 없다.
- [ ] 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야한다.
- [ ] 노선 가운데 역이 등록 될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야한다.

#### 노선 역 제거

- [ ] 노선에서 역을 제거할 경우 정상 동작을 위해 역을 재배치 되어야한다.
- [ ] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야한다.
- [ ] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야한다.
