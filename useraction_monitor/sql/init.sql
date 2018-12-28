CREATE DATABASE useraction_stat CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

create table pv_stat (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `product_id` varchar(128) NOT NULL COMMENT '产品ID',
    `count_value` bigint(20) NOT NULL COMMENT '统计数值',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PV统计表';

create table uv_stat (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `product_id` varchar(128) NOT NULL COMMENT '产品ID',
    `count_value` bigint(20) NOT NULL COMMENT '统计数值',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='UV统计表';

insert pv_stat (product_id, count_value)
values
('1', 100),
('2', 300),
('3', 500);

insert uv_stat (product_id, count_value)
values
('1', 15),
('2', 30),
('3', 12);