package org.m4m.resource.controller;

import org.m4m.controller.HateoasStringController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HateoasStringController.class)
public class HateoasStringControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "somebody", authorities = {"CAN_STRING_TO_UPPER"})
    public void toUpper1() throws Exception {
        final MvcResult mvcResult = mvc.perform(get("http://localhost:8090/toUpperCase?string=TesT")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "somebody_else", authorities = {"CAN_STRING_TO_UPPER"})
    public void toUpper2() throws Exception {
        final MvcResult mvcResult = mvc.perform(get("http://localhost:8090/string/TesT/toUpperCase")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "yet_another_user", authorities = {"CAN_STRING_TO_LOWER"})
    public void toLower() throws Exception {
        final MvcResult mvcResult = mvc.perform(get("http://localhost:8090/string/TesT/toLowerCase")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test(expected=org.springframework.web.util.NestedServletException.class)
    @WithMockUser(username = "malicious_user", authorities = {"CAN_STRING_TO_UPPER"})
    public void toLower_with_security_error() throws Exception {
        final MvcResult mvcResult = mvc.perform(get("http://localhost:8090/string/TesT/toLowerCase")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
