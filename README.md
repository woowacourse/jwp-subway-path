# jwp-subway-path

# 페어 프로그래밍 룰

## 스위치 기준

- [x] 스위치 시간은 20분으로 한다.
- [x] 내비게이터는 전자기기에 손을 대지 않는다.

## 깃 컨벤션

- [x] 기능 목록에 있는 기능 단위로 커밋한다.
- [x] 작동할 수 있는 기능 단위로 커밋한다.
- [x] 커밋 메세지는 아래 키워드를 사용해 기능 목록 그대로 작성한다.
    - feat: 기능 구현을 완료했을 때
    - refactor: 기능의 변화 없이 코드를 변경했을 때
    - test: 테스트 코드만 작성했을 때
    - chore: 패키지 변경 등 사소한 수정사항이 생겼을 때
    - fix: 프로그램의 결함을 수정할 때
    - docs: 문서를 수정할 때

## 구현 계획

- [x] 구현은 다음과 같은 순서로 진행된다.
    1. API 설계 시 사용되는 서비스에 대한 기능 파악
    2. 컨트롤러 기능 구현 
    3. 서비스 기능을 담기 위한 도메인 구현
    4. 영속성에 대한 인터페이스 구현(DIP)
    5. 서비스 기능 구현
    6. 영속성 구현체 생성 테이블 정의

## 기타 룰

- [x] 페어 프로그래밍 방식에 대해 논의하고 싶다면 언제든 이야기한다.
- [x] 집중이 안된다면 페어에게 솔직하게 이야기한다.
- [x] 최소한 2시간에 한 번은 쉬어야 한다.
- [x] 커피챗을 최소 1회 진행한다.


---

# 기능 목록

- [ ] 노선에 대한 역 등록 API 구현
  - [ ] 역의 위치는 자유롭게 지정할 수 있어야 한다.
  - [ ] 노선에 역이 등록될 때 거리 정보도 함께 포함되어야 한다.
    - [x] 거리 정보는 양의 정수로 제한한다.
  - [ ] 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야 한다.
  - [ ] 하나의 역은 여러 노선에 등록이 될 수 있다.
  - [ ] 노선은 갈래길을 가질 수 없다.
  - [ ] 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야 한다.
    - [ ] 신규로 등록된 역이 기존 노선의 거리 범위를 벗어날 수 없다.
    - [ ] A-B-C 노선에서 B 다음에 D 역을 등록하려고 한다면, B-C역의 거리가 3km인 경우 B-D 거리는 3km 보다 적어야 한다.

- [ ] 노선에 역 제거 API 구현
  - [ ] 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 한다.
  - [ ] 노선에서 역이 제거될 경우 역과 역 사이의 거리가 재배정된다.
  - [ ] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 한다.

- [ ] 단일 노선 조회 API 구현
  - [ ] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선한다.

- [ ] 노선 목록 조회 API 구현
  - [ ] 각 노선에 포함된 역을 순서대로 보여주도록 응답을 개선한다.
