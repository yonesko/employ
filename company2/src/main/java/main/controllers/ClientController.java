package main.controllers;

import main.dao.CustomerDAO;
import main.model.domain.Customer;
import main.model.messages.Extra;
import main.model.messages.Request;
import main.model.messages.Response;
import main.model.messages.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;


@RestController
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private CustomerDAO customerDAO;

    @RequestMapping(path = "/customer", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public Response handleCustomerRequest(@RequestBody Request request) {
        log.info(request.toString());
        Response response;
        try {
            switch (request.getType()) {
                case CREATE_AGT:
                    response = register(request);
                    break;
                case GET_BALANCE:
                    response = getBalance(request);
                    break;
                default:
                    response = new Response(ResultCode.INTERNAL_ERR);
                    break;
            }
        } catch (Exception e) {
            log.error("handleCustomerRequest: ", e);
            response = new Response(ResultCode.INTERNAL_ERR);
        }
        log.info(response.toString());
        return response;
    }

    private Response getBalance(Request request) {
        Customer existing = customerDAO.getCustomer(request.getValue(Extra.Attr.LOGIN));
        if (existing != null) {
            if (!existing.getPassword().equals(request.getValue(Extra.Attr.PASS))) {
                return new Response(ResultCode.INCORRECT_PASSWORD);
            }
            return new Response(ResultCode.OK, new Extra(existing.getBalance().toPlainString(), Extra.Attr.BALANCE));
        }
        return new Response(ResultCode.NO_SUCH_USER);
    }

    private Response register(Request request) {
        if (customerDAO.getCustomer(request.getValue(Extra.Attr.LOGIN)) != null) {
            return new Response(ResultCode.USER_ALREADY_EXISTS);
        }

        try {
            customerDAO.insertCustomer(extractCustomer(request));
        } catch (DuplicateKeyException e) {
            return new Response(ResultCode.USER_ALREADY_EXISTS);
        }

        return new Response(ResultCode.OK);
    }

    private Customer extractCustomer(Request request) {
        return new Customer(
                request.getValue(Extra.Attr.LOGIN),
                request.getValue(Extra.Attr.PASS),
                Timestamp.from(Instant.now()),
                BigDecimal.ZERO);
    }

}

