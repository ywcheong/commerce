create table shop_items (
    id uuid primary key,
    seller_id uuid not null,
    name varchar(100) not null,
    price bigint not null,
    stock integer not null
);
