-- Seed users
insert into users (id, email, phone, name, password_hash, created_at) values
  (1,'user@example.com','+7 (999) 123-45-67','Тимур', md5('password123'), now()),
  (2,'anna@example.com',null,'Анна', md5('pass'), now()),
  (3,'sofia@example.com',null,'София', md5('pass'), now()),
  (4,'valeria@example.com',null,'Валерия', md5('pass'), now()),
  (5,'alexandra@example.com',null,'Александра', md5('pass'), now())
on conflict do nothing;

-- Profile
insert into profiles (user_id, bio, age, city) values
  (1,'Привет! Я программист, люблю путешествия и активный отдых.', 23, 'Москва'),
  (2,'Люблю путешествия и фото',25,'Москва'),
  (3,'Дизайнер и йога',28,'Москва'),
  (4,'Студентка медуниверситета, люблю спорт',23,'Москва'),
  (5,'Маркетолог, обожаю активный отдых',27,'Москва')
on conflict do nothing;

-- Another users for demo
-- уже включены выше

-- Matches
insert into matches (id, user_id1, user_id2, status, created_at, last_message_at) values
  (1,1,2,'PENDING', now(), now()),
  (2,1,3,'PENDING', now(), now())
on conflict do nothing;

-- Messages
insert into messages (match_id, sender_id, content, created_at) values
  (1,1,'Привет! Как дела?', now()),
  (1,2,'Привет! Все отлично, а у тебя?', now());

