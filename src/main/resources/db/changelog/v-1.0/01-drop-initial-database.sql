alter table if exists user_project
    drop constraint if exists FKocfkr6u2yh3w1qmybs8vxuv1c;

alter table if exists user_project
    drop constraint if exists FK5n9393t40e47m9srydudsq9ew;

alter table if exists usr
    drop constraint if exists FKpxhk0d0wmwt6budg7q4qsic5n;

drop table if exists department cascade;

drop table if exists project cascade;

drop table if exists user_project cascade;

drop table if exists usr cascade;