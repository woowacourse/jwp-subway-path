# 1단계 리팩토링 목록

## 리뷰 내용
- [ ] 기존에 Line 객체가 가지고 있던 color 필드는 없어져도 되는가? 
- [x] `Line.addSection()` 메서드 분리
- [x] `LineRequest`의 불필요한 기본 생성자 제거
- [x] Response 객체에서의 `to`, `from` 사용 기준이 있는가?
- [x] 일관성 있게 커스텀 예외 사용하도록 수정
- [x] ControllerAdvice의 크기가 너무 커지지 않도록 부모 예외 설정
- [x] LineController의 API URL 설계 시, PathVariable 변수명을 Id -> lineId로 수정
- [x] station, section 테이블 참조 관계에 대한 제약 추가
- [x] 서비스에서는 도메인만 다룰 수 있도록 수정
- [x] 실패하는 테스트 복구
- [x] LineService 생성자 public?? 
- [x] `created_at`, `updated_at` 데이터 생성

## 미완성 기능 구현
- [x] 조회 기능: API 레벨부터 천천히 살펴보기
  - [x] 조회 응답 Dto 다시 만들기
    - String lineName
    - List<StationResponse> stations
- [x] 노선에서의 역 제거 API 구현
- [x] 테스트 작성
- [x] 도메인 객체에 id 추가 -> equals,hashcode 다시 정의하기
  - [x] Line
  - [x] Station

## 궁금한 내용
- 프론트에서는 조회/삽입/수정/삭제 등의 요청을 날릴 때 서버가 넘겨준 정보(id)를 기반으로 요청할 것이다. 그렇다면, 조회 요청에 대한 응답으로는 항상 pk를 넘겨줘야 되는 것일까?
- 도메인 객체가 id를 갖도록 했다. 그럴지 않으면 조회 작업이 너무 까다로워진다는 것을 확인함. 

## 리팩토링
- [x] 조건에 따라 조회하는 DAO 메서드는 Optional을 반환하도록 수정
- [x] 테스트 Fixture 생성
