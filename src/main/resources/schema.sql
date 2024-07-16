DROP TABLE IF EXISTS ORDER_PRODUCT;
DROP TABLE IF EXISTS ORDERS;

-- CREATE ORDERS TABLE
CREATE TABLE ORDERS (
    order_id bigint AUTO_INCREMENT NOT NULL,
    status varchar(255) NOT NULL,
    total_price numeric(19, 2) NOT NULL,
    created_at timestamp default now(),
    last_modified_at timestamp,
    primary key (order_id)
);

-- CREATE ORDERS TABLE
CREATE TABLE ORDER_PRODUCT (
    order_product_id bigint AUTO_INCREMENT NOT NULL,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer NOT NULL,
    unit_price numeric(19, 2) NOT NULL,
    created_at timestamp default now(),
    last_modified_at timestamp,
    primary key (order_product_id)
);