package main.controllers;

import main.Main;
import main.dao.CustomerDAO;
import main.model.messages.Extra;
import main.model.messages.Request;
import main.model.messages.Response;
import main.model.messages.ResultCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class ClientControllerTest {
    private static final String CUSTOMER_ENDPOINT = "http://localhost:8080/customer";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private CustomerDAO customerDAO;

    private Request badReqTypeReq = new Request(null);

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

    private Response respOK = new Response(ResultCode.OK);
    private Response respUserAlreadyExists = new Response(ResultCode.USER_ALREADY_EXISTS);
    private Response respNoSuchUser = new Response(ResultCode.NO_SUCH_USER);
    private Response respInternalError = new Response(ResultCode.INTERNAL_ERR);
    private Response respIncorrectPassword = new Response(ResultCode.INCORRECT_PASSWORD);
    private Response respOKZeroBalance = new Response(ResultCode.OK, new Extra(BigDecimal.ZERO.toPlainString(), Extra.Attr.BALANCE));


    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mappingJackson2HttpMessageConverter = new MappingJackson2XmlHttpMessageConverter();
    }

    public void clean() {
        customerDAO.truncateTable();
    }

    @Test
    public void handleCustomerReg() throws Exception {
        clean();

        requestAndExpect(olegRegReq, respOK);

        requestAndExpect(olegRegReq, respUserAlreadyExists);
    }

    @Test
    public void handleCustomerBalance() throws Exception {
        clean();
        requestAndExpect(olegRegReq, respOK);

        requestAndExpect(olegPass1BalanceReq, respOKZeroBalance);

        requestAndExpect(olegPass2BalanceReq, respIncorrectPassword);

        requestAndExpect(karlBalanceReq, respNoSuchUser);
    }

    @Test
    public void handleCustomerError() throws Exception {
        customerDAO.dropTale();

        requestAndExpect(olegRegReq, respInternalError);

        customerDAO.reCreateTable();

        requestAndExpect(badReqTypeReq, respInternalError);
    }

    private String toXml(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    private void requestAndExpect(Request request, Response response) throws Exception {
        mockMvc.perform(post(CUSTOMER_ENDPOINT)
                .contentType(MediaType.APPLICATION_XML)
                .content(toXml(request)))
                .andExpect(status().isOk())
                .andExpect(content().xml(toXml(response)));
    }

}