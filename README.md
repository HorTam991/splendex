# Feladatleírás
Deposit and Withdrawal:
The user has to be able to deposit and withdraw a certain amount of money from his/her bank account.

Transfer:
The user has to be able to transfer money from his bank account to some other user’s bank account

Transaction history (date, amount, balance):
The user has to be able to get his transaction history with the info about his recent transactions with some attributes such as date, amount and current balance.

Transaction history printing:
The user has to be able to print his transaction history (to the Console).

Transaction history filters (just deposits, withdrawal, date):
The user has to be able to filter his transactions by the direction of the transaction (withdraw or deposit) and by the date of the transaction.

# Indítás
Egyszerű spring boot start. Az alkalmazás H2 inMemory adatbázist használ.

# Funkciók
## Person
A /person endpointon keresztül hívható
1. Ügyfelek listázása (/list)
2. Ügyfelek létrehozása, módosítása, törlése
3. Ügyfél számlaadatinak lekérdezése ID alapján (/getMyInvoiceData/{personId})
4. Ügyfél tranzakcióinak listázása (/getMyBankTransactions). 
    Táblázathoz készült, szerveri oldali lapozással, szűrési lehetőségekkel
        Tranzakció típusa (transactionTypeId)
        Tranzakció dátuma (transactionDateFrom, transactionDateTo). Paraméterezéstől függően (>=, =<, between)
5. Ügyfél tranzakcióinak exportálása .xls fájlba, szűrési lehetőségekkel (/exportMyBankTransactions)
    Tranzakció típusa (transactionTypeId)
    Tranzakció dátuma (transactionDateFrom, transactionDateTo). Paraméterezéstől függően (>=, =<, between)
## Transactions
A /transaction endpointon keresztül hívható
1. Befizetés (/deposit)
2. Pénz felvétel (/withdraw)
3. Utalás (/transfer")


# Tesztelés
Tesztelni a mellékelt (docs mappa) postman hívásokkal lehet.
