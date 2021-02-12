
-- Database initialization backup. Move it to src/main/resources and Spring Boot will execute it.
-- Don't forget to place 'spring.datasource.initialization-mode=always' in application.properties

INSERT INTO subject(type, name_en, name_ua)
VALUES('EXAM', 'Mathematics', 'Математика'), 
('EXAM', 'Ukrainian language', 'Українська мова'), 
('EXAM', 'History of Ukraine', 'Історія України'), 
('EXAM', 'Geography', 'Географія'), 
('EXAM', 'English', 'Англійська мова'), 
('EXAM', 'World History', 'Всесвітня історія'), 
('SCHOOL', 'Mathematics', 'Математика'),
('SCHOOL', 'Ukrainian language', 'Українська мова'), 
('SCHOOL', 'History of Ukraine', 'Історія України'), 
('SCHOOL', 'Geography', 'Географія'), 
('SCHOOL', 'English', 'Англійська мова'),
('SCHOOL', 'World History', 'Всесвітня історія');

INSERT INTO faculty(name_en, name_ua, description_en, description_ua, state_funded_places, contract_places) 
VALUES('Faculty of Computer Sciences', 'Факультет інформатики', 'It is one of the youngest faculties of our university, but for the third year in a row it ranks first in the world rankings in terms of IT education. The faculty produces experienced and qualified specialists.', 'Є одним з наймолодших факультетів нашого університету, проте вже третій рік поспіль посідає перші місця у світових рейтингах за рівнем ІТ-освіти. Факультет випускає досвідчених і кваліфікованих спеціалістів.', 40, 50),
('Faculty of Law', 'Факультет правничих наук', 'The Faculty of Law of our university is an elite law school that creates, preserves and disseminates knowledge in the field of law.', 'Факультет правничих наук нашого університету є елітною юридичною школою, що створює, зберігає та поширює знання у галузі права.', 50, 40),
('Faculty of Economics', 'Факультет економічних наук', 'Graduates of this faculty have a high professional reputation in domestic and foreign labor markets and hold high-paying positions in prestigious foreign and domestic firms, audit and consulting companies, leading banks, audit and advertising agencies, leading economic journals, research institutes.', 'Випускники цього факультету мають високу професійну репутацію на вітчизняних і закордонних ринках праці та займають високооплачувані посади в престижних іноземних та вітчизняних фірмах, аудиторських і консалтингових компаніях, провідних банках, аудиторських та рекламних агентствах, провідних економічних журналах, науково-дослідних інституціях тощо.', 55, 35),
('Faculty of Humanities', 'Факультет гуманітарних наук', 'For many years, graduates of this faculty are in demand in the labor market, because, according to the ratings of employers, this faculty is a recognized leader among higher education institutions in Ukraine in the field of humanities education.', 'Впродовж багатьох років випускники цього факультету користуються попитом на ринку праці, адже, за рейтингами працедавців, цей факультет є визнаним лідером серед вищих навчальних закладів України в царині гуманітарної освіти.', 45, 50);

INSERT INTO faculty_subjects(faculty_id, subjects_id) 
VALUES((select id from faculty where name_ua = 'Факультет інформатики'), (select id from subject where name_ua = 'Математика' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет інформатики'), (select id from subject where name_ua = 'Математика' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет інформатики'), (select id from subject where name_ua = 'Англійська мова' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет інформатики'), (select id from subject where name_ua = 'Англійська мова' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет інформатики'), (select id from subject where name_ua = 'Українська мова' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет інформатики'), (select id from subject where name_ua = 'Українська мова' and type = 'SCHOOL')),

((select id from faculty where name_ua = 'Факультет правничих наук'), (select id from subject where name_ua = 'Історія України' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет правничих наук'), (select id from subject where name_ua = 'Історія України' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет правничих наук'), (select id from subject where name_ua = 'Українська мова' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет правничих наук'), (select id from subject where name_ua = 'Українська мова' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет правничих наук'), (select id from subject where name_ua = 'Англійська мова' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет правничих наук'), (select id from subject where name_ua = 'Англійська мова' and type = 'SCHOOL')),

((select id from faculty where name_ua = 'Факультет економічних наук'), (select id from subject where name_ua = 'Українська мова' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет економічних наук'), (select id from subject where name_ua = 'Українська мова' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет економічних наук'), (select id from subject where name_ua = 'Географія' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет економічних наук'), (select id from subject where name_ua = 'Географія' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет економічних наук'), (select id from subject where name_ua = 'Математика' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет економічних наук'), (select id from subject where name_ua = 'Математика' and type = 'SCHOOL')),

((select id from faculty where name_ua = 'Факультет гуманітарних наук'), (select id from subject where name_ua = 'Всесвітня історія' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет гуманітарних наук'), (select id from subject where name_ua = 'Всесвітня історія' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет гуманітарних наук'), (select id from subject where name_ua = 'Українська мова' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет гуманітарних наук'), (select id from subject where name_ua = 'Українська мова' and type = 'SCHOOL')),
((select id from faculty where name_ua = 'Факультет гуманітарних наук'), (select id from subject where name_ua = 'Англійська мова' and type = 'EXAM')),
((select id from faculty where name_ua = 'Факультет гуманітарних наук'), (select id from subject where name_ua = 'Англійська мова' and type = 'SCHOOL'));

INSERT INTO user(full_name, email, password, city, region, education)
VALUES('Admin', 'admin@a', '$2a$10$NX.Vcv7DWw7LRNA6Qr40c.9kk3.aNtUFijWrxF3upduG9M01K5JpW', 'Kyiv', 'Kyiv', 'stub'),
      ('User', 'user@u', '$2a$10$NX.Vcv7DWw7LRNA6Qr40c.9kk3.aNtUFijWrxF3upduG9M01K5JpW', 'Kyiv', 'Kyiv region', 'Some school'),
      ('Admin Servlet', 'a@a', '1c35a06d1808c8a50aff5e2d6adc1beafcb708ffae002feb578550a4ffe38afc', 'Kyiv', 'Kyiv', 'stub'),
      ('User Servlet', 'u@u', '1c35a06d1808c8a50aff5e2d6adc1beafcb708ffae002feb578550a4ffe38afc', 'Kyiv', 'Kyiv region', 'Some school');

INSERT INTO user_role(user_id, roles)
VALUES((select id from user where email = 'admin@a'), 'ADMIN'),
      ((select id from user where email = 'user@u'), 'ENTRANT'),
      ((select id from user where email = 'a@a'), 'ADMIN'),
      ((select id from user where email = 'u@u'), 'ENTRANT');