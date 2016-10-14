package main.controllers;

import main.dao.CustomerDAO;
import main.model.domain.Customer;
import main.model.messages.*;
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
                    response = register(extractCustomer(request));
                    break;
                case GET_BALANCE:
                    response = getBalance(extractCustomer(request));
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

    private Customer extractCustomer(Request request) {//TODO is needed ?
        return new Customer(
                request.getByAttr(Extra.Attr.LOGIN).getValue(),
                request.getByAttr(Extra.Attr.PASS).getValue(),
                Timestamp.from(Instant.now()),
                BigDecimal.ZERO);
    }


    private Response getBalance(Customer customer) {
        Customer existing = customerDAO.getCustomer(customer.getLogin());
        if (existing != null) {
            if (!existing.getPassword().equals(customer.getPassword())) {
                return new Response(ResultCode.INCORRECT_PASSWORD);
            }
            return new Response(ResultCode.OK, new Extra(existing.getBalance().toPlainString(), Extra.Attr.BALANCE));
        }
        return new Response(ResultCode.NO_SUCH_USER);
    }

    private Response register(Customer customer) {
        if (customerDAO.getCustomer(customer.getLogin()) != null) {
            return new Response(ResultCode.USER_ALREADY_EXISTS);
        }

        try {
            customerDAO.insertCustomer(customer);
        } catch (DuplicateKeyException e) {
            return new Response(ResultCode.USER_ALREADY_EXISTS);
        }

        return new Response(ResultCode.OK);
    }

}

