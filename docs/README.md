# 1단계 리팩토링 목록

## 리뷰 내용
- [ ] 기존에 Line 객체가 가지고 있던 color 필드는 없어져도 되는가? 
- [ ] `Line.addSection()` 메서드 분리
- [ ] `LineRequest`의 불필요한 기본 생성자 제거
- [ ] Response 객체에서의 `to`, `from` 사용 기준이 있는가?
- [ ] 일관성 있게 커스텀 예외 사용하도록 수정
- [ ] ControllerAdvice의 크기가 너무 커지지 않도록 부모 예외 설정
- [ ] LineController의 API URL 설계 시, PathVariable 변수명을 Id -> lineId로 수정
- [ ] station, section 테이블 참조 관계에 대한 제약 추가
- [ ] 서비스에서는 도메인만 다룰 수 있도록 수정?
- [ ] 실패하는 테스트 복구
- [ ] LineService 생성자 public?? 
- [ ] `created_at`, `updated_at`, `audit` 데이터 생성

## 미완성 기능 구현
- [ ] 조회 기능: API 레벨부터 천천히 살펴보기
- [ ] 노선에서의 역 제거 API 구현
- [ ] 테스트 작성
