delete from orders;

alter table orders
    drop column shop_item_id,
    drop column quantity,
    drop column total_price;

create table order_items (
    id uuid primary key,
    order_id uuid not null,
    shop_item_id uuid not null,
    quantity integer not null,
    unit_price bigint not null
);
