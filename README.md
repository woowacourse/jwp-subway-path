# jwp-subway-path

## API 기능 요구사항 구현하기

1. 노선에 역 등록 API 신규 구현
    1. 노선 신규 등록
       :ux를 고려했을 때, 신규 노선을 등록할 때, 초기 역을 함께 설정하는 것이 적합하다고 판단함
         ```
         POST /lines
         {
             "name": "2호선"
             "color": "Green"
         }
         ```

    2. 기존 노선에 역 등록
       ```
         POST /lines/{lineId}/stations
         {
             "upStation": "낙성대id"
             "downStation": "사당id"
             "distance": 1
         }
         ```
        - 빈 노선인 경우,두 역을 동시에 등록해야 한다
        - 빈노선이 아닌 경우, 한 역은 해당 노선에 존재하는 역이어야 하고, 다른 역은 노선에 등록되지 않은 역이어야 한다 
        - 거리 정보는 양의 정수로 제한한다

2. 노선에 역 제거 API 신규 구현
    1. 노선 삭제
       :ux를 고려했을 때, 신규 노선을 등록할 때, 초기 역을 함께 설정하는 것이 적합하다고 판단함
         ```
         DELETE /lines/{lineId}
         ```

    2. 노선에 역 삭제
       ```
         DELETE /lines/{lineId}/stations/{stationId}
       ```
       
3. 노선 조회 API 수정
    1. 노선 조회
        ```
          GET /lines/{lineId}
        ```
    
    2. 노선 목록 조회
        ```
          GET /lines
        ```

4. 경로 조회 API
   1. 출발역과 도착역 사이의 경로 조회
      - 출발역과 도착역 사이의 최단 경로를 조회 (환승도 고려)
      - 해당 경로를 통한 이동에 대한 요금도 함께 응답
      ```
          GET /path
      ```


## 도메인 설계하기
### Line
- 필드
  - Long id
  - String name => LineName name (-선으로 끝나야함) endwith
  - String color => Color color
  - Sections
- 기능
  - 역 목록 조회하기

### Sections
- 필드
  - List<Section>
- 기능
  - 역 상행-하행 순으로 정렬하기
  - 역 목록 조회하기

### Section
- 필드
  - Station upStation
  - Station downStation
  - Integer distance => Distance distance

### Station
- 필드
  - Long id
  - String name => StaionName name (-역으로 끝나야함)  endwith
