<?xml version="1.0" encoding="UTF-8"?>
<sqls>

<sql id="source_table">
CREATE TABLE Orders (
    order_number BIGINT,
    price        DECIMAL(32,2),
    order_time   TIMESTAMP(3)
) WITH (
  'connector' = 'datagen'
)
</sql>

<sql id="sink_table">
	  CREATE TABLE print_sink (
    order_number BIGINT,
     max_price  DECIMAL(32,2)
    ) WITH (
    'connector' = 'print'
    )
</sql>


<sql id="agg_view">
create view agg_view as
select order_number,max(price) from Orders group by order_number
</sql>

<sql id="insert_table">
insert into print_sink
select * from agg_view
</sql>

</sqls>