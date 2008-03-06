Create a derby database using ij.

- start ij, load driver, create databse, run script
driver 'org.apache.derby.jdbc.EmbeddedDriver';
connect 'jdbc:derby:tempDatabase;create=true';
run 'ecsiteCreate.sql';