package hu.tamas.splendex.util;

public class SystemKeys {

    public static class Spring {
        public static final String DEFAULT_CHARSET = "UTF-8";
        public static final String PRODUCES_JSON_UTF8 = "application/json; charset=UTF-8";
        public static final String MYME_TYPE_PDF = "application/pdf";
    }

    public static class BankTransactionTypes {
        public static final Long DEPOSIT = 1L;
        public static final Long WITHDRAWAL = 2L;
        public static final Long TRANSFER = 3L;
    }

    public static class ResponseTexts {
        public static final String ERROR_COMPILING_RESPONSE = "Error compiling response!";
        public static final String SUCCESS_DELETE = "Delete successful!";

        public static final String SUCCESS_DEPOSIT = "Deposit successful!";
        public static final String FAILED_DEPOSIT = "Deposit failed!";

        public static final String SUCCESS_WITHDRAWAL = "Withdrawal successful!";
        public static final String FAILED_WITHDRAWAL = "Withdrawal failed!";

        public static final String SUCCESS_TRANSFER = "Transfer successful!";
        public static final String FAILED_TRANSFER = "Transfer failed!";
    }

    public static class ExceptionTexts {
        public static final String TARGET_PERSON_NOT_FOUND = "Target person not found!";
        public static final String PERSON_NOT_FOUND = "Person not found!";
        public static final String TARGET_PERSON_DONT_HAVE_INVOICE = "Target person don't have invoice!";
        public static final String YOU_DONT_HAVE_INVOICE = "You don't have invoice!";
        public static final String YOU_DONT_HAVE_ENOUGH_MONEY_TO_TRANSFER = "You do not have enough money to make the transfer!";
        public static final String IT_CANNOT_BE_THE_SAME_SENDER_AND_TARGET = "It cannot be the same sender and target!";
        public static final String INVOICE_ALREADY_EXISTS = "An invoice already exists with this invoice number!";
        public static final String CUSTOMER_ALREADY_HAS_INVOICE = "Customer already has an invoice!";
    }

    public static class Exports {
        public static class MyBankTransactions {
            public static class Headers {
                public static final String SENDER = "Sender";
                public static final String SENDER_INVOICE = "Sender invoice";
                public static final String TARGET = "Target";
                public static final String TARGET_INVOICE = "Target invoice";
                public static final String TYPE = "Type";
                public static final String TRANSACTION_DATE = "Transaction date";
                public static final String AMOUNT = "Amount";
                public static final String OLD_INVOICE_TOTAL = "Old invoice total";
                public static final String NEW_INVOICE_TOTAL = "New invoice total";
            }
        }
    }

}
