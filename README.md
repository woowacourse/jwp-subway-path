## 1. api 재설계

### 현재 : station하나를 다른 노선에 공통으로 사용하지 않음
line 컨트롤러
- post : /lines
- get : /lines/{lineId}
- get : /lines
- update : /lines/{lineId}
- delete : /lines/{lineId}

lineStation컨트롤러
- post : lines/{lineId}/stations/init (초기 구간 등록)
- post : lines/{lineId}/stations")


### 수정 : station하나를 다른 노선에 공통으로 사용함
이유: 경로 조회시, 
line 컨트롤러
- post : /lines
- get : /lines/{lineId}
- get : /lines
- update : /lines/{lineId}
- delete : /lines/{lineId}

station 컨트롤러
- post : /stations
- get : /stations
- get : /stations/{stationId} (역 정보 조회)
- update : /stations/{stationId}
- delete : /stations/{stationId}
    - 특정 노선 삭제시, 모든 노선의 해당 station 삭제 및 이어주기

lineStation컨트롤러
- post : lines/{lineId}/stations/init
    - (빈 노선 등록)
    - 이유: 빈 노선에 등록할 때는, 기준역과 새로운 역의 구분이 있기 떄문에, 요청 페이지가 달라야 한다고 생각한다
- post : lines/{lineId}/stations
- delete : lines/{lineId}/stations/{stationId}
    - 특정 노선의 역 삭제

## 2. 데이터 베이스 재 설계
line
- id : pk
- name : unique
- color : unique

station
- id : pk
- name : unique

section
- id
- lineId
- stationId
- distance
- unique : lineId, stationId


---
2단계 시작

## 3. 데이터 베이스 설정 및 영속성 레이어 인터페이스로 만들기
- 프로덕션의 데이터베이스는 로컬에 저장될 수 있도록 설정
- 테스트용 데이터베이스는 인메모리로 동작할 수 있도록 설정


## 4. 기능 구현 목록

### 경로 조회 API 구현
:출발역과 도착역 사이의 최단 거리 경로를 구하는 API를 구현

1. 비즈니스 로직
- subway
    - [x] List<Line>
    - [x] 새로운 노선 추가할 때, 이름 중복/색깔 중복 없는지 확인 기능 추가하기
    - [x] 노선 추가 기능
    - [x] 노선 삭제 기능
    - [x] 노선 수정 기능
    - [x] 노선 조회 기능
      - [x] id로 노선 조회 기능 (* 고민해보기)
      - [x] 이름으로 노선 조회 기능

    - [x] 출발역과 도착역 사이의 최단 경로 구하기 기능 (인터페이스 사용하기)
    - [x] 최단 거리 경로 조회하기 List<Station>
    - [x] 최단 총 거리 길이 조회하기

- 최단 경로 구하기 기능 구체화
    - 입력 : 출발역, 도착역
    - 출발역의 line 가져오기
    - 도착역의 line 가져오기
    - 예를 들어,
        - 2호선 : 서울대 - 낙성대 - 사당 - 교대 - 방배 - 서초 - 강남
        - 3호선 : 압구정 - 신사 - 잠원 - 고속터미널 - 교대 - 남부터미널
        - 4호선 : 사당 - 이수 - 동작
        - 7호선 : 이수 - 내방 - 고속터미널 - 반포
        - 출발역 : 낙성대 / 도착역 : 고속터미널

        - 가능한 경로
            - 낙성 - 사당 - 교대 - 고속터미널 (2->3호선) 1번 환승
            - 낙성 - 사당(2,4) - 이수(4,7) - 고속터미널 (2->4->7호선) 2번 환승

2. api 명세
- RouteController
- get : /route
    - 경로
    - 총 거리

3. api 구조
- RouteController
- RouteService
    - [x] 경로 조회 전략 패턴 의존성 주입받아서 저장하기
    - [] 질문 : RouteService의 내부 변수로 **루트 계산 인터페이스**를 저장하는 것이 맞는지?
- LineRepository
    - [x] 모든 line목록 조회해서, subway 만들기


### 요금 조회 기능 추가
: 경로 조회 시 요금 정보를 포함하여 응답한다
요금 계산 방법
기본운임(10㎞ 이내): 기본운임 1,250원
이용 거리 초과 시 추가운임 부과
10km~50km: 5km 까지 마다 100원 추가
50km 초과: 8km 까지 마다 100원 추가

1. 비즈니스 로직
- [x] 고민점1 : 요금 조회 기능을 subway가 가지고 있는 것이 맞나? NO!!!!
    - 만약 갖는다고 하자, 최단 경로도 구하고 + 주어진 경로의 요금도 조회할 수 있다
    - 안 가지면 누가 경로 조회 기능을 가지고 요금 조회 기능을 가질 것인가?
      - 다른 객체가 가진다.

- subway
    - [x] 출발역과 도착역 사이의 최단 경로 구하기 기능 (인터페이스 => 다익스트라)
        - [X] 최단 거리 경로 조회하기 List<Station>
        - [X] 총 거리 길이 조회하기

- subwayFare
    - [ ] 요금 조회 기능 (인터페이스 => 기본 요금 정책, 추가운임 정책)
        - [ ] 입력값 : 총 distance
        - [ ] distance가
            - 기본운임(10㎞ 이내): 기본운임 1,250원
            - 이용 거리 초과 시 추가운임 부과
                - distance-10km(기본 운임거리)가
                    - 10km~50km 인 경우: 5km 까지 마다 100원 추가
                        - 예) 28km이면, 18km는 추가 운임 부과
                        - 나머지는 버린다
                    - 
                    - 50km 초과: 8km 까지 마다 100원 추가
                        - 예) 60km 인 경우

2. api 명세
- RouteController
- get : /route
    - 경로
    - 총 거리
    - 총 금액

3. api 구조
- RouteController
- RouteService
    - [x] 경로 조회하기
        - [x] 최단 경로 조회하기
        - [x] 최단 경로의 거리 조회하기
- FareService
    - [] 거리에 따른 요금 조회하기
