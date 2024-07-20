# Whataplabs Task - ORDER
주문 API를 제공하는 애플리케이션입니다. ```PORT: 9089```
- [API 문서](http://localhost:9089/swagger-ui/index.html)

## 사용 기술
- Java 17
- SpringBoot 2.7.18
- Maven

## 프로젝트 구조
```
.
├── application
│   ├── event                              # event 방식을 처리하는 영역
│   │   ├── common
│   │   │   └── DomainEvent.java
│   │   └── listener
│   │       └── OrderEventListener.java
│   └── service                            # 비즈니스 로직을 처리하는 서비스
│       └── OrderService.java
├── domain                                 # 주문에 대한 도메인 영역
│   ├── Order.java
│   ├── OrderCancelRequested.java
│   ├── OrderChangeRequested.java
│   ├── OrderProduct.java
│   ├── OrderRepository.java
│   ├── OrderRequested.java
│   ├── OrderStatus.java
│   └── exception                          # 도메인 관련 예외 정의
│       ├── ErrorType.java
│       ├── OrderBusinessException.java
│       ├── OrderChangeNotAvailableException.java
│       ├── OrderFailException.java
│       └── OrderNotFoundException.java
├── infrastructure
│   ├── config                             # 설정 파일
│   └── repository                         # repository 및 entity
│       ├── OrderEntity.java
│       ├── OrderJpaRepository.java
│       ├── OrderProductEntity.java
│       └── OrderRepositoryImpl.java
└── interfaces
    ├── client                             # 외부 API 호출을 위한 Client 영역
    │   ├── ProductClient.java
    │   ├── request
    │   │   ├── OrderedProductRequest.java
    │   │   └── ProductOrderRequest.java
    │   └── response
    │       └── ProductOrderResponse.java
    └── web                                # controller 및 요청, 응답 객체
        ├── OrderRestController.java
        ├── exception
        │   ├── ErrorResponse.java
        │   └── GlobalExceptionHandler.java
        ├── request
        └── response
```
## 이벤트 방식으로 처리되는 주문 생성, 변경, 취소
주문 생성, 변경, 취소의 경우 상품의 재고 API를 호출해야 하기 때문에 외부 통신으로인해 응답 지연이 발생할 수 있습니다.     
이를 해결하기 위해 요청 시 주문 상태를 변경하고 이벤트를 발행한 뒤 바로 응답을 하는 방식으로 구현하였고
발행된 이벤트는 비동기 방식으로 상품의 재고 API로 요청을 보내고 처리 결과에 따라 주문 상태를 추후 업데이트 하도록 개발하였습니다.
- 주문 생성 시
  - 주문 상태는 ```ORDER_REQUEST```로 저장하고 ```OrderRequested``` 이벤트를 발행
  - 재고 확인 후 재고 차감에 성공한 경우 주문 상태를 ```ORDER_COMPLETED``` 로 변경
  - 재고 확인 후 재고 차감에 실패한 경우 주문 상태를 ```ORDER_FAILED``` 로 변경
- 주문 변경 시
  - 주문의 상태가 ```ORDER_COMPLETED``` 또는 ```ORDER_FAILED``` 일 때 변경이 가능하며
  - 주문 상태를 ```ORDER_REQUEST```로 저장하고 ```OrderChangeRequested``` 이벤트를 발행
  - 재고 확인 후 재고 차감에 성공한 경우 주문 상태를 ```ORDER_COMPLETED``` 로 변경하고 실제 주문 상품 데이터도 변경
  - 재고 확인 후 재고 차감에 실패한 경우 주문 상태를 변경 요청 전 상태로 변경
- 주문 취소 시
  - 주문 상태가 ```ORDER_REQUEST``` 또는 ```ORDER_COMPLETED``` 일 때 변경이 가능하며
  - 주문 상태를 ```ORDER_CANCEL_REQUEST```로 저장하고 ```OrderCancelRequested``` 이벤트를 발행
  - 재고 추가에 성공한 경우 주문 상태를 ```ORDER_CANCELED``` 로 변경
  - 재고 추가에 실패한 경우 주문 상태를 취소 요청 상태 그대로 유지

## 도메인 정의
### 주문 (Order)
- 주문은 ```주문 아이디(orderId)```, ```주문 상태(status)```, ```총 가격(totalPrice)```, ```등록 일시(createdAt)```, ```수정 일시(lastModifiedAt)```를 가집니다.
- 주문 아이디는 자동 증가하는 임의의 숫자이며 상품을 식별하는 키입니다.
- 주문 상태는 아래와 같이 5가지 상태로 구분합니다.
  + ```주문 요청(ORDER_REQUEST)```: 최초 주문 요청 상태 (재고 확인 x)
  + ```주문 완료(ORDER_COMPLETED)```: 재고 확인이 완료되어 실제 주문이 완료되었다고 보는 상태
  + ```주문 실패(ORDER_FAILED)```: 재고가 부족하거나 상품이 없는 등의 이유로 주문이 실패한 상태
  + ```주문 취소 요청(ORDER_CANCEL_REQUEST)```: 요청 또는 결제 요청 상태에서 주문 취소를 요청했을 때 상태 (재고 원복 전)
  + ```주문 취소(ORDER_CANCELED)```: 재고 원복이 완료된 후 주문 취소 완료 상태
- 제외된 내용
  + 취소 시 취소 사유 등에 대한 내용은 제외 하였습니다.
### 주문 상품 (Order Product)
- 주문 상품은 ```주문 상품 아이디(orderProductId)```, ```주문 아이디(orderId)```, ```상품 아이디(productId)```, ```수량(quantity)```, ```단일 가격(unitPrice)```, ```등록 일시(createdAt)```, ```수정 일시(lastModifiedAt)```를 가집니다.
- 주문 하나당 여러 개의 주문 상품이 포함될 수 있습니다.
- 주문이 생성될 때 주문 상품도 같이 생성이 됩니다.
- 주문 변경 시 변경된 주문에 포함되지 않은 주문 상품은 삭제됩니다.
- 주문 취소 시 주문 상품 데이터는 유지합니다.

## 주요 API
- 주문 단건 조회
  - ```GET /orders/{id}```: id를 이용하여 주문의 id, status, totalPrice, 주문 상품 목록을 조회합니다.
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "orderId": 101,
        "status": "ORDER_COMPLETED",
        "statusDesc": "주문 완료",
        "totalPrice": 10000.00,
        "orderProducts": [
          {
            "orderProductId": 101,
            "orderId": 101,
            "productId": 101,
            "quantity": 1,
            "unitPrice": 5000.00
          },
          {
            "orderProductId": 102,
            "orderId": 101,
            "productId": 102,
            "quantity": 2,
            "unitPrice": 2500.00
          }
        ]
      }
    }
    ```
- 주문 목록 조회
  - ```GET /orders```: 전체 주문의 id, status, totalPrice, 주문 상품 목록을 조회합니다.
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "orders": [
          {
            "orderId": 101,
            "status": "ORDER_COMPLETED",
            "statusDesc": "주문 완료",
            "totalPrice": 10000.00,
            "orderProducts": [
              {
                "orderProductId": 101,
                "orderId": 101,
                "productId": 101,
                "quantity": 1,
                "unitPrice": 5000.00
              },
              {
                "orderProductId": 102,
                "orderId": 101,
                "productId": 102,
                "quantity": 2,
                "unitPrice": 2500.00
              }
            ]
          },
          {
            "orderId": 102,
            "status": "ORDER_COMPLETED",
            "statusDesc": "주문 완료",
            "totalPrice": 20000.00,
            "orderProducts": [
              {
                "orderProductId": 103,
                "orderId": 102,
                "productId": 101,
                "quantity": 2,
                "unitPrice": 5000.00
              },
              {
                "orderProductId": 104,
                "orderId": 102,
                "productId": 102,
                "quantity": 1,
                "unitPrice": 1000.00
              },
              {
                "orderProductId": 105,
                "orderId": 102,
                "productId": 103,
                "quantity": 3,
                "unitPrice": 3000.00
              }
            ]
          },
          {
            "orderId": 103,
            "status": "ORDER_FAILED",
            "statusDesc": "주문 실패",
            "totalPrice": 15000.00,
            "orderProducts": [
              {
                "orderProductId": 106,
                "orderId": 103,
                "productId": 103,
                "quantity": 100,
                "unitPrice": 3000.00
              }
            ]
          }
        ]
      }
    }
    ```
- 주문 생성
  - ```POST /orders```: 상품 id, 주문 수량, 주문 상품 금액을 가진 상품 목록을 받아 주문을 요청합니다.
  - request
    ```json
    {
      "orderProduct": [
        {
          "productId": 101,
          "quantity": 3,
          "unitPrice": 1000
        },
        {
          "productId": 102,
          "quantity": 2,
          "unitPrice": 1500
        },
        {
          "productId": 103,
          "quantity": 1,
          "unitPrice": 2000
        }
      ]
    }
    ```
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "id": 1,
        "status": "ORDER_REQUEST",
        "totalPrice": 8000,
        "orderProducts": [
          {
            "orderProductId": 1,
            "orderId": 1,
            "productId": 101,
            "quantity": 3,
            "unitPrice": 1000
          },
          {
            "orderProductId": 2,
            "orderId": 1,
            "productId": 102,
            "quantity": 2,
            "unitPrice": 1500
          },
          {
            "orderProductId": 3,
            "orderId": 1,
            "productId": 103,
            "quantity": 1,
            "unitPrice": 2000
          }
        ]
      }
    }
    ```
- 주문 변경
  - ```PUT /orders/{id}```: 상품 id, 주문 수량, 주문 상품 금액을 가진 상품 목록을 받아 id에 해당하는 주문을 변경 요청합니다.
  - request
    ```json
    {
      "orderProduct": [
        {
          "productId": 103,
          "quantity": 7,
          "unitPrice": 1500
        },
        {
          "productId": 105,
          "quantity": 6,
          "unitPrice": 2000
        }
      ]
    }
    ```
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "id": 102,
        "status": "ORDER_REQUEST",
        "totalPrice": 22500,
        "orderProducts": [
          {
            "productId": 103,
            "quantity": 7,
            "unitPrice": 1500
          },
          {
            "productId": 105,
            "quantity": 6,
            "unitPrice": 2000
          }
        ]
      }
    }
    ```
- 주문 취소
  - ```DELETE /orders/{id}```: id에 해당하는 주문을 취소 요청합니다.
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "id": 101,
        "status": "ORDER_CANCEL_REQUEST",
        "totalPrice": 10000.00,
        "orderProducts": [
          {
            "orderProductId": 101,
            "orderId": 101,
            "productId": 101,
            "quantity": 1,
            "unitPrice": 5000.00
          },
          {
            "orderProductId": 102,
            "orderId": 101,
            "productId": 102,
            "quantity": 2,
            "unitPrice": 2500.00
          }
        ]
      }
    }
    ```