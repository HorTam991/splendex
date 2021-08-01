create table person (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name varchar(50),
    last_name varchar(50),
    date_of_birth date,
    place_of_birth varchar(50),
    mother_name varchar(100)
);

create table invoice(
    id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT references person(id),
    invoice_number varchar(50),
    actual_invoice_total BIGINT
);

create table bank_transaction_type(
    id INT AUTO_INCREMENT PRIMARY KEY,
    type varchar(30)
);

create table bank_transaction(
    id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT references person(id),
    from_person_id INT references person(id),
    from_invoice_number varchar(50),
    to_person_id INT references person(id),
    to_invoice_number varchar(50),
    transaction_type_id int references bank_transaction_type(id),
    transaction_date date,
    transaction_amount bigint,
    old_invoice_total bigint,
    new_invoice_total bigint
);
