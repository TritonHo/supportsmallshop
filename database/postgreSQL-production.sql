/*
	script to clean the database:

	drop table shop;
	drop SEQUENCE shop_id_seq;
	drop  TYPE shop_type;
*/
CREATE TYPE shop_type AS ENUM ('食肆', '零售（食物）','零售（其他）', '服務');

CREATE SEQUENCE shop_id_seq START WITH 1;

create table shop
(
	id integer DEFAULT nextval('shop_id_seq'),
	name character varying(50) not null,
	short_description character varying(1000) not null,
	full_description character varying(1000) not null,
	open_hours character varying(1000) not null,
	search_tags character varying(100) not null,
	
	district integer null, /*hardcoded, 1 = HK Island, 2 = Kowloon, 3 = New Territories */
	address character varying(200) not null,
	phone character varying(20),

	/* remarks: the geohash use trunc method, not nearest method */

	/* takes latitude and longitude * 1000 values (round down), it would represent an area of ~ 111m * 111m(width)  */
	latitude1000 integer not null,
	longitude1000 integer not null,

	/* exact location */
	latitude1000000 integer not null, /* the value of latitude * 1000000, the Accuracy is ~0.1m */
	longitude1000000 integer not null, /* the value of longitude * 1000000, the Accuracy is ~0.1m */

	remarks character varying(200) not null default '',
	photo_url character varying(7) not null default '',

	start_time timestamp not null default current_timestamp,
	end_time timestamp null,

	shop_type shop_type not null,

    last_update_time timestamp not null default current_timestamp,
	last_update_by character varying(100) not null default '',

	CONSTRAINT "shop_pk" PRIMARY KEY (id)
);

CREATE INDEX shop_index1 ON shop (latitude1000, longitude1000);

