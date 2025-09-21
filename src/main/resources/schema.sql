-- Users
create table if not exists users (
  id bigserial primary key,
  email varchar(255) unique,
  phone varchar(64),
  name varchar(255),
  password_hash varchar(255),
  created_at timestamp
);

-- Profiles
create table if not exists profiles (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  bio text,
  age int,
  city varchar(128)
);

-- Matches
create table if not exists matches (
  id bigserial primary key,
  user_id1 bigint not null references users(id) on delete cascade,
  user_id2 bigint not null references users(id) on delete cascade,
  status varchar(32),
  created_at timestamp,
  last_message_at timestamp
);

-- Messages
create table if not exists messages (
  id bigserial primary key,
  match_id bigint not null references matches(id) on delete cascade,
  sender_id bigint not null references users(id) on delete cascade,
  content text,
  created_at timestamp
);

