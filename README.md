# jwp-subway-path

## 기능목록

- [x] 노선에 역 등록 API 신규 구현
    - [x] (POST /section) 구역에 새로운 역 추가하는 기능
        - [x] 검증
            - [x] 라인테이블에 라인이 존재하는지 확인 -> 없으면 예외
            - [x] 역 테이블에 두 역이 있는지 확인
        - [x] 구역테이블에 라인이 존재하는 경우
            - [x] 구역 테이블에 새로운 역이 존재 안해야한다 -> 이미 존재 예외
            - [x] 구역 테이블에 해당 라인의 기준역이 존재해야한다. -> 기준역 없음 예외
            - [x] 기준역이 종점인지 확인(역이 속한 구역이 1개면 기준역이 종점)
                - [x] 새로운 역이 종점이 되는 방향인지 확인?
                    - [x] 새로운 역이 종점일 경우
                        - [x] 새로운 구역 추가
                    - [x] 새로운 역이 종점이 아닐 경우
                        - [x] 거리 정보 관리
                            - [x] 해당 방향에 존재하는 구역 거리가 입력 거리보다 큰지 확인
                        - [x] 갈래길 방지
                            - [x] 기존 구역 삭제
                            - [x] 기준역과 새로운역으로 구역 추가
                            - [x] 기준역과 연결되어 있었던 역과 새로운 역으로 구역 추가
        - [x] 구역테이블에 라인이 존재하지 않는 경우 (신규 둥록)
            - [x] 구역 등록한다.

- [x] 노선에 역 제거 API 신규 구현
    - [x] 해당 라인에 삭제할 역이 존재하지 않으면 예외
    - [x] 해당 라인에 삭제할 역을 포함한 구역이 한개인 경우
        - [x] 종점 제거
    - [x] 해당 라인에 삭제할 역을 포함한 구역이 두개인 경우
        - [x] 재배치
- [x] 노선 조회 API 수정
    - [x] (GET /lines/{id}/stations)
        - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 반환한다.
            - [x] 상행 종점과 하행 종점을 찾는다.
            - [x] 노선에 포함된 역들을 그래프에 추가
            - [x] 그래프에 상행 종점과 하행 종점의 경로를 반환하도록 한다.
            - [x] 경로를 역들의 목록으로 변환한다.
- [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.
    - [x] (GET /lines/stations)
        - [x] 모든 노선을 조회한다.
        - [x] 조회한 노선의 역들을 순서대로 정렬한다.
        - [x] 역들의 목록으로 반환한다.

## LineSections 일급컬렉션

- [x] 한 노선의 구역들을 담당한다.
    - [x] 해당 라인의 구역이 비어있는지 확인한다.
    - [x] 해당 라인의 역이 존재하는지 확인한다.
    - [x] 해당 라인의 기준역이 종점인지 확인한다.
    - [x] 해당 라인의 종점들을 반환한다.

# Data.sql

insert into STATION (name) values ('왕십리역'); 1
insert into STATION (name) values ('한양대역'); 2
insert into STATION (name) values ('뚝섬역'); 3
insert into STATION (name) values ('성수역'); 4
insert into STATION (name) values ('건대입구역'); 5
insert into STATION (name) values ('구의역'); 6
insert into STATION (name) values ('강변역'); 7
insert into STATION (name) values ('잠실나루역'); 8
insert into STATION (name) values ('잠실역'); 9
insert into STATION (name) values ('잠실새내역'); 10
insert into STATION (name) values ('종합운동장역'); 11
insert into STATION (name) values ('삼성역'); 12
insert into STATION (name) values ('선릉역'); 13
insert into STATION (name) values ('역삼역'); 14
insert into STATION (name) values ('강남역'); 15

insert into STATION (name) values ('서울숲'); 16
insert into STATION (name) values ('압구정로데오'); 17
insert into STATION (name) values ('강남구청'); 18
insert into STATION (name) values ('선정릉'); 19

insert into STATION (name) values ('마장'); 20
insert into STATION (name) values ('답십리'); 21
insert into STATION (name) values ('장한평'); 22
insert into STATION (name) values ('군자'); 23
insert into STATION (name) values ('아차산'); 24
insert into STATION (name) values ('광나루'); 25
insert into STATION (name) values ('천호'); 26

insert into STATION (name) values ('청담'); 27
insert into STATION (name) values ('뚝섬유원지'); 28
insert into STATION (name) values ('어린이대공원'); 29

insert into STATION (name) values ('몽촌토성'); 30
insert into STATION (name) values ('강동구청'); 31