package main.controllers;

import main.dao.CustomerDAO;
import main.model.messages.Extra;
import main.model.messages.Request;
import main.model.messages.Response;
import main.model.messages.ResultCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context.xml"})
@WebAppConfiguration
public class ClientControllerTest {
    private static final String CUSTOMER_REST_URL = "http://localhost:8080/customer";

    @Autowired
    private CustomerDAO customerDAO;

    private RestTemplate restTemplate = new RestTemplate();

    private Request badReqTypeReq = new Request(null,
            new Extra("Oleg", Extra.Attr.LOGIN),
            new Extra("P2$$W0гD", Extra.Attr.PASS));

    private Request olegRegReq = new Request(Request.Types.CREATE_AGT,
            new Extra("Oleg", Extra.Attr.LOGIN),
            new Extra("P2$$W0гD", Extra.Attr.PASS));

    private Request olegPass1BalanceReq = new Request(Request.Types.GET_BALANCE,
            new Extra("Oleg", Extra.Attr.LOGIN),
            new Extra("P2$$W0гD", Extra.Attr.PASS));

    private Request olegPass2BalanceReq = new Request(Request.Types.GET_BALANCE,
            new Extra("Oleg", Extra.Attr.LOGIN),
            new Extra("olololol", Extra.Attr.PASS));

    private Request karlBalanceReq = new Request(Request.Types.GET_BALANCE,
            new Extra("KARL", Extra.Attr.LOGIN),
            new Extra("P2$$W0гD", Extra.Attr.PASS));


    @Before
    public void clean() {
        customerDAO.truncateTable();
    }

    @Test
    public void handleCustomerReg() throws Exception {
        Response response;

        response = makeRequest(olegRegReq);
        Assert.assertEquals(ResultCode.OK, response.getResultCode());

        response = makeRequest(olegRegReq);
        Assert.assertEquals(ResultCode.USER_ALREADY_EXISTS, response.getResultCode());
    }

    @Test
    public void handleCustomerBalance() throws Exception {
        Response response;

        response = makeRequest(olegRegReq);
        Assert.assertEquals(ResultCode.OK, response.getResultCode());

        response = makeRequest(olegPass1BalanceReq);
        Assert.assertEquals(ResultCode.OK, response.getResultCode());
        Assert.assertEquals(BigDecimal.ZERO.toPlainString(), response.getByAttr(Extra.Attr.BALANCE).getValue());

        response = makeRequest(olegPass2BalanceReq);
        Assert.assertEquals(ResultCode.INCORRECT_PASSWORD, response.getResultCode());

        Assert.assertEquals(ResultCode.OK, response.getResultCode());
        Assert.assertEquals(BigDecimal.ZERO.toPlainString(), response.getByAttr(Extra.Attr.BALANCE).getValue());

        response = makeRequest(karlBalanceReq);
        Assert.assertEquals(ResultCode.NO_SUCH_USER, response.getResultCode());

    }

    @Test
    public void handleCustomerError() {
        Response response;

        customerDAO.dropTale();

        response = makeRequest(olegRegReq);
        Assert.assertEquals(ResultCode.INTERNAL_ERR, response.getResultCode());

        response = makeRequest(badReqTypeReq);
        Assert.assertEquals(ResultCode.INTERNAL_ERR, response.getResultCode());

        customerDAO.reCreateTable();
    }

    private Response makeRequest(Request request) {
        return restTemplate.postForObject(
                CUSTOMER_REST_URL,
                request,
                Response.class);
    }


}