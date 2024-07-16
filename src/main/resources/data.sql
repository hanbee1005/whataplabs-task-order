-- init order
INSERT INTO ORDERS(order_id, status, total_price) VALUES (101, 'ORDER_REQUEST', 10000);
INSERT INTO ORDERS(order_id, status, total_price) VALUES (102, 'ORDER_REQUEST', 20000);
INSERT INTO ORDERS(order_id, status, total_price) VALUES (103, 'PAY_REQUEST', 15000);

-- init order_product
INSERT INTO ORDER_PRODUCT(order_product_id, order_id, product_id, quantity, unit_price) VALUES (101, 101, 101, 1, 5000);
INSERT INTO ORDER_PRODUCT(order_product_id, order_id, product_id, quantity, unit_price) VALUES (102, 101, 102, 2, 2500);
INSERT INTO ORDER_PRODUCT(order_product_id, order_id, product_id, quantity, unit_price) VALUES (103, 102, 101, 2, 5000);
INSERT INTO ORDER_PRODUCT(order_product_id, order_id, product_id, quantity, unit_price) VALUES (104, 102, 102, 1, 1000);
INSERT INTO ORDER_PRODUCT(order_product_id, order_id, product_id, quantity, unit_price) VALUES (105, 102, 103, 3, 3000);
INSERT INTO ORDER_PRODUCT(order_product_id, order_id, product_id, quantity, unit_price) VALUES (106, 103, 103, 5, 3000);

