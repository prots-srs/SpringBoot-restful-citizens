# Java practical RESTful service as HATEOAS

Citizens and their family relations:
-marriages
-parenthood
-sibling
Operations:
-read by
--citizen (identification: firstName, familyName, birthDate, gender, citizenship)
--relatives nuclear family citizen:
---adult (upper 18 years)
---child
--relatives extended family citizen

-create by
--citizen by birth
--marriage by event
--parenthood by birth of child or addopting

-update by
--citizen by death, change family name, change citizenship
--dissolution marriage
--clear parenthood
