# Whataplabs Task - ORDER
주문 API를 제공하는 애플리케이션입니다. ```PORT: 9089```

## 사용 기술
- Java 17
- SpringBoot 2.7.18
- Maven

## 프로젝트 구조

## 도메인 정의
### 주문 (Order)
- 주문은 ```주문 아이디(orderId)```, ```주문 상태(status)```, ```총 가격(totalPrice)```, ```등록 일시(createdAt)```, ```수정 일시(lastModifiedAt)```를 가집니다.
- 주문 아이디는 자동 증가하는 임의의 숫자이며 상품을 식별하는 키입니다.
- 주문 상태는 ```주문 요청(ORDER_REQUEST)```, ```결제 요청(PAY_REQUEST)```, ```주문 취소 요청(ORDER_CANCEL_REQUEST)```, ```주문 취소(ORDER_CANCELED)``` 로 구분합니다.
  + ```주문 요청(ORDER_REQUEST)```: 최초 주문 요청 상태 (재고 확인 x)
  + ```결제 요청(PAY_REQUEST)```: 재고 확인이 완료되어 실제 주문이 처리될 수 있는 상태 (결제를 요청하는 상태)
  + ```주문 취소 요청(ORDER_CANCEL_REQUEST)```: 요청 또는 결제 요청 상태에서 주문 취소를 요청했을 때 상태 (재고 원복 전)
  + ```주문 취소(ORDER_CANCELED)```: 재고 원복이 완료된 후 주문 취소 완료 상태
  + 위 4가지 상태 이후 로직은 구현에서 제외하였습니다.
- 제외된 내용
  + 결제 요청 이후 로직은 제외하였습니다.
  + 취소 시 취소 사유 등에 대한 내용은 제외하였습니다.
### 주문 상품 (Order Item)
- 주문 상품은 ```주문 상품 아이디(orderProductId)```, ```주문 아이디(orderId)```, ```상품 아이디(productId)```, ```수량(quantity)```, ```단일 가격(unitPrice)```, ```등록 일시(createdAt)```, ```수정 일시(lastModifiedAt)```를 가집니다.
- 주문 하나당 여러 개의 주문 상품이 포함될 수 있습니다.

## 주요 API
- 주문 단건 조회
- 주문 목록 조회
- 주문 생성
- 주문 변경
- 주문 취소