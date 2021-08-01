package hu.tamas.splendex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.tamas.splendex.model.Invoice;
import hu.tamas.splendex.model.Person;
import hu.tamas.splendex.model.dto.PersonInvoiceView;
import hu.tamas.splendex.service.BankTransactionService;
import hu.tamas.splendex.service.InvoiceService;
import hu.tamas.splendex.service.PersonService;
import hu.tamas.splendex.util.SystemKeys;
import hu.tamas.splendex.util.exception.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;

    private final InvoiceService invoiceService;

    private final BankTransactionService bankTransactionService;

    private final ObjectMapper jsonMapper;

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> list(@RequestParam(value = "filter", required = false, defaultValue = "{}") final String filter,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") final int pageIndex,
                                       @RequestParam(value = "pageSize", defaultValue = "-1") final int pageSize) {
        try {
            Map<String, String> filterParams = this.jsonMapper.readValue(filter, new TypeReference<Map<String, String>>() {});
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(this.personService.findAll(filterParams, pageIndex, pageSize)), HttpStatus.OK);
        } catch (IOException e) {
            log.error("list - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        try {
            Person data = this.personService.findById(id);
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("findById - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> insert(@RequestBody Person data) {
        try {
            this.personService.save(data);
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("insert - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> update(@RequestBody Person data) {
        try {
            this.personService.update(data);
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("update - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> delete(@RequestParam Long id) {
        this.personService.deleteById(id);
        return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_DELETE, HttpStatus.OK);
    }

    @RequestMapping(value = "/getMyInvoiceData/{personId}", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> getMyInvoiceData(@PathVariable Long personId) {
        try {
            Invoice invoice = this.invoiceService.findByPersonId(personId);
            PersonInvoiceView result = new PersonInvoiceView(invoice);
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(result), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("findById - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/getMyBankTransactionLog", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> getMyBankTransactionLog(@RequestParam(value = "personId") final Long personId) {
        try {
            log.info("Start getMyBankTransactionLog");
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(this.bankTransactionService.getMyBankTransactionLog(personId)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("getMyBankTransactionLog - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PersonNotFoundException e) {
            log.error("getMyBankTransactionLog - PersonNotFoundException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getMyBankTransactions", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> getMyBankTransactions(@RequestParam(value = "personId") final Long personId,
                                                        @RequestParam(value = "filter", required = false, defaultValue = "{}") final String filter,
                                                        @RequestParam(value = "pageIndex", required = false, defaultValue = "-1") final int pageIndex,
                                                        @RequestParam(value = "pageSize", defaultValue = "-1") final int pageSize) {
        try {
            log.info("Start getMyBankTransactions");
            Map<String, String> filterParams = this.jsonMapper.readValue(filter, new TypeReference<Map<String, String>>() {});
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(this.bankTransactionService.getMyBankTransactions(personId, filterParams, pageIndex, pageSize)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("getMyBankTransactions - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PersonNotFoundException e) {
            log.error("getMyBankTransactions - PersonNotFoundException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/exportMyBankTransactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> exportMyBankTransactions(@RequestParam(value = "personId") final Long personId,
                                                           @RequestParam(value = "filter", required = false, defaultValue = "{}") final String filter) {
        try {
            log.info("Start exportMyBankTransactions");
            Map<String, String> filterParams = this.jsonMapper.readValue(filter, new TypeReference<Map<String, String>>() {});

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/vnd.ms-excel"));
            headers.setContentDispositionFormData("inline", "My bank transactions export - " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()) + ".xls");

            return new ResponseEntity<>(this.bankTransactionService.exportMyBankTransactions(personId, filterParams), headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("exportMyBankTransactions - ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
