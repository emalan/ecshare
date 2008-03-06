DROP TABLE entry;
DROP TABLE category;
DROP TABLE site;

CREATE TABLE site
(
    site_id INTEGER NOT NULL,
    name VARCHAR(128) NOT NULL,
    description VARCHAR(128) NOT NULL,
    PRIMARY KEY(site_id));

CREATE TABLE category
(
    category_id INTEGER NOT NULL,
    name VARCHAR(128) NOT NULL,
    PRIMARY KEY(category_id));

CREATE TABLE entry
(
    entry_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    entry_date DATE NOT NULL,
    entry_text VARCHAR(255) NOT NULL,
    category_id INTEGER NOT NULL,
    site_id INTEGER NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (category_id),
    FOREIGN KEY (site_id) REFERENCES site (site_id),
    PRIMARY KEY(entry_id));

insert into site
   values (1, 'ecsite', 'Demo site');
insert into category 
   values (1, 'personal');
insert into entry (entry_date, entry_text, category_id, site_id)
   values ('2008-02-28','First entry', 1, 1);

