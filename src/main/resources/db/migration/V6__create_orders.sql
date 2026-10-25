create table orders (
    id uuid primary key,
    buyer_id uuid not null,
    shop_item_id uuid not null,
    quantity integer not null,
    total_price bigint not null
);
