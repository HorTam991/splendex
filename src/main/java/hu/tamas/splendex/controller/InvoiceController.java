package hu.tamas.splendex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.tamas.splendex.model.Invoice;
import hu.tamas.splendex.service.InvoiceService;
import hu.tamas.splendex.util.SystemKeys;
import hu.tamas.splendex.util.exception.AlreadyExistsInvoiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final ObjectMapper jsonMapper;

    @RequestMapping(method = RequestMethod.POST, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> insert(@RequestBody Invoice data) {
        try {
            this.invoiceService.save(data);
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("insert - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsInvoiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> update(@RequestBody Invoice data) {
        try {
            this.invoiceService.update(data);
            return new ResponseEntity<>(this.jsonMapper.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("update - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> delete(@RequestParam Long id) {
        this.invoiceService.deleteById(id);
        return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_DELETE, HttpStatus.OK);
    }

}
