insert into person(first_name, last_name, date_of_birth, place_of_birth, mother_name) values
('Elek', 'Mekk', '1989-05-01', 'Nagykanizsa', 'Kovács Annamária'),
('Géza', 'Nándor', '1999-12-01', 'Kaposvár', 'Boros Izabella'),
('Valéria', 'Szvath', '1978-01-12', 'Budapest', 'Pécsi Boróka'),
('Ferenc', 'Meleg', '1999-05-21', 'Kaposvár', 'Boros Izabella'),
('Gábor', 'Kovács', '1999-05-04', 'Kaposvár', 'Boros Annamária');

insert into invoice(person_id, actual_invoice_total, invoice_number) values
(1, 10000, '12345-67890'),
(2, 20000, '54321-98760'),
(3, 30000, '10101-22222'),
(4, 40000, '65435-78976'),
(5, 50000, '43454-64544');

insert into bank_transaction_type(type) values
('Deposit'),
('Withdrawal'),
('Transfer');
