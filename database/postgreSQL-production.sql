/*
	script to clean the database:

	drop table blacklisted;
	drop table helper;
	drop table response_type;
	drop table shop;
	drop table submission;
	drop table submission_response;

	drop TYPE device_type;
	drop TYPE shop_type;
*/
CREATE TYPE device_type AS ENUM ('ios', 'google-android');
CREATE TYPE shop_type AS ENUM ('食肆', '零售（食物）','零售（其他）', '服務');


create table blacklisted
(
	device_type device_type not null,
	reg_id text not null,
    blacklist_time timestamp not null default current_timestamp,
	CONSTRAINT "blacklisted_pk" primary key(reg_id, device_type)
);

create table helper
(
    id UUID,
	device_type device_type not null,
	reg_id text not null,
	secret text not null,
	CONSTRAINT "user_pk" PRIMARY KEY (id),
	UNIQUE(reg_id, device_type)
);

create table response_type
(
	id integer,
	message character varying(100) not null,
	is_reject boolean not null,
	is_serious_reject boolean not null,
	is_accept boolean not null,
	CONSTRAINT "response_type_pk" PRIMARY KEY (id),
	CHECK ( (case when is_reject then 1 else 0 end) + (case when is_serious_reject then 1 else 0 end) + (case when is_accept then 1 else 0 end) = 1)
);


create table shop
(
	id UUID,
	name character varying(50) not null,
	short_description character varying(1000) not null,
	full_description character varying(1000) not null,
	open_hours character varying(1000) not null,
	search_tags character varying(100) not null,
	
	district integer not null, /*hardcoded, 0 = unspecified, 1 = HK Island, 2 = Kowloon, 3 = New Territories */
	address character varying(200) not null,
	phone character varying(20) not null default '',

	/* remarks: the geohash use trunc method, not nearest method */
	/* takes latitude and longitude * 1000 values (round down), it would represent an area of ~ 111m * 111m(width)  */
	latitude1000 integer not null default 0,
	longitude1000 integer not null default 0,

	/* exact location */
	latitude1000000 integer not null default 0, /* the value of latitude * 1000000, the Accuracy is ~0.1m */
	longitude1000000 integer not null default 0, /* the value of longitude * 1000000, the Accuracy is ~0.1m */

	photo_url character varying(7) not null default '',

	shop_type shop_type not null,
	CONSTRAINT "shop_pk" PRIMARY KEY (id)
);
CREATE INDEX shop_index1 ON shop (latitude1000, longitude1000);


create table submission
(
	id UUID,
	helper_id UUID,
	shop_id UUID, /* if shop_id = null, it is a new record */

	name character varying(50),
	short_description character varying(1000),
	full_description character varying(1000),
	open_hours character varying(1000),
	search_tags character varying(100),
	
	district integer, /*hardcoded, 1 = HK Island, 2 = Kowloon, 3 = New Territories */
	address character varying(200),
	phone character varying(20),

	/* exact location */
	latitude1000000 integer, /* the value of latitude * 1000000, the Accuracy is ~0.1m */
	longitude1000000 integer, /* the value of longitude * 1000000, the Accuracy is ~0.1m */

	photo_url character varying(7), 

	secret text, /* if the secret is null, it means the user have replied the secret */
	
    last_update_time timestamp not null default current_timestamp,

	CONSTRAINT "submission_pk" PRIMARY KEY (id)
);

create table submission_response
(
	submission_id UUID,
	helper_id UUID,
	secret text, /* if the secret is null, it means the user have replied the secret */
	response integer not null,
	response_time timestamp not null default current_timestamp,
	CONSTRAINT "submission_response_pk" PRIMARY KEY (submission_id, helper_id)
);


--foreign keys
ALTER TABLE submission ADD CONSTRAINT submission_fk1 FOREIGN KEY (helper_id) REFERENCES helper(id) ON DELETE CASCADE;
ALTER TABLE submission ADD CONSTRAINT submission_fk2 FOREIGN KEY (shop_id) REFERENCES shop(id) ON DELETE CASCADE;
ALTER TABLE submission_response ADD CONSTRAINT submission_response_fk1 FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE;
ALTER TABLE submission_response ADD CONSTRAINT submission_response_fk2 FOREIGN KEY (helper_id) REFERENCES helper(id) ON DELETE CASCADE;
