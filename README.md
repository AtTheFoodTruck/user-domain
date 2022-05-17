<h1>
■ FoodTruckAroundMe - 푸드트럭 통합 정보 제공 및 주문 서비스
</h1>

## 📋 설계서

### 사용자 시나리오

<table>
    <tr>
        <td align="center">
            <img width="1549" alt="EventStorming" src="https://user-images.githubusercontent.com/44490394/168741007-c8dfa213-3268-4ac3-b4b5-87e76df16ea3.png" width="500" height="500">
        </td>
    </tr>
</table>


### 이벤트 스토밍 (Event Storming)

<table>
    <tr>
        <td align="center">
            <img width="2395" alt="사용자시나리오_사용자중심" src="https://user-images.githubusercontent.com/44490394/168741464-e547e53d-1c10-4391-ad44-61b6a3962870.png" width="500" height="500">
        </td>
        <td align="center">
            <img width="1815" alt="사용자시나리오_점주중심" src="https://user-images.githubusercontent.com/44490394/168741637-bd2b11e3-bb2e-4abb-80d9-ca8f61af6245.png" width="500" height="500">
        </td>
    </tr>
</table>

### DB 설계

#### 물리 모델 (Physical model)

<table>
    <tr>
        <td align="center">
            <img width="1289" alt="Entity설계" src="https://user-images.githubusercontent.com/44490394/168744671-78cbd70c-5d0f-438d-8887-6352dd335612.png" width="500" height="500">
        </td>
    </tr>
</table>

### API 설계 ✏️

## 아키텍처
| 기술스택 | 개발환경 |
| --- | --- |
| Spring Boot | - Spring Framwork 2.6.3 </br> - Java 11 </br> - Gradle </br> - Spring Security |
| Spring Cloud | - Eureka </br> - Gateway </br> - OpenFeign </br> - ConfigServer(추가예정)|
| Authenticate | - JWT (Json Web Token) |
| ORM | - JPA </br> - QueryDsl |
| Message Queue | - Kafka (추가예정)|
| Database | - MYSQL </br> - Redis |
| 모니터링(추가예정) | - Zipkin </br> - Spring Cloud Sleuth </br> - Prometheus </br> - Grafana|

### 시스템 아키텍처

<table>
    <tr>
        <td align="center">
            <img width="2395" alt="CI:CD 파이프라인" src="https://user-images.githubusercontent.com/44490394/168752428-fbaab2e3-fd3e-4aa4-a0ae-b4b0ba967feb.png" width="500" height="500">
        </td>
        <td align="center">
            <img width="1815" alt="MicroService" src="https://user-images.githubusercontent.com/44490394/168751288-1953e812-95bf-4258-8970-abee24b3e2c2.png" width="500" height="500">
        </td>
    </tr>
</table>

### 향후 계획
- ConfigServer, 모니터링 연동 작업 추가 작업 예정
- kafka Message Queue를 적용예정 - 주문 접수에 적용 예정
