set referential_integrity false;
truncate table station restart identity;
truncate table line restart identity;
truncate table sections restart identity;
set referential_integrity true;
