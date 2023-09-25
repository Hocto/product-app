package com.example.productapp.domain.controller;

import com.example.productapp.domain.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.FileCopyUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.example.productapp.domain.util.ItemTestUtil.getItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RestItemIntegrationTest {

    private static final String ITEM_URL = "/api/v1/item";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Value("classpath:request/domain/item/retrieve_request_body.json")
    private Resource retrieveRequestBody;

    @Value("classpath:request/domain/item/add_request_body.json")
    private Resource addRequestBody;

    @Value("classpath:request/domain/item/add_request_exceptional_body.json")
    private Resource addRequestExceptionalBody;

    @Value("classpath:request/domain/item/add_request_invalid_price_body.json")
    private Resource addRequestInvalidPriceBody;

    @Value("classpath:request/domain/item/update_request_body.json")
    private Resource updateRequestBody;

    @Value("classpath:request/domain/item/delete_request_body.json")
    private Resource deleteRequestBody;

    @BeforeEach
    @Transactional
    void beforeEach() {
        itemRepository.saveAll(getItems());
    }

    @AfterEach
    @Transactional
    void afterEach() {
        itemRepository.deleteAll();
    }

    @Test
    void onValidRequestBodyWithRetrieveMethodReturnsItem() throws Exception {

        MockHttpServletRequestBuilder builder = get(ITEM_URL)
                .content(resourceToString(retrieveRequestBody))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is("Macbook Pro")))
                .andExpect(jsonPath("$.item.price", is(799.99)))
                .andExpect(jsonPath("$.item.quantity", is(3)))
                .andExpect(jsonPath("$.item.sold", is(false)));
    }

    @Test
    void onValidRequestBodyWithAddMethodReturnsItem() throws Exception {

        MockHttpServletRequestBuilder builder = post(ITEM_URL)
                .content(resourceToString(addRequestBody))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is("Iphone 15 Pro Max")))
                .andExpect(jsonPath("$.item.price", is(999.99)))
                .andExpect(jsonPath("$.item.quantity", is(5)))
                .andExpect(jsonPath("$.item.sold", is(false)));
    }

    @Test
    void onInvalidValidRequestBodyWithAddMethodReturnBadRequest() throws Exception {

        MockHttpServletRequestBuilder builder = post(ITEM_URL)
                .content(resourceToString(addRequestExceptionalBody))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void onInvalidPriceWithAddMethodReturns5XXException() throws Exception {

        MockHttpServletRequestBuilder builder = post(ITEM_URL)
                .content(resourceToString(addRequestInvalidPriceBody))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("ERROR: numeric field overflow\n  Detail: A field with precision 5, scale 2 must round to an absolute value less than 10^3.")))
                .andExpect(jsonPath("$.type", is("Exception")));
    }

    @Test
    void onValidRequestBodyWithModifyMethodReturnsItem() throws Exception {

        MockHttpServletRequestBuilder builder = put(ITEM_URL)
                .content(resourceToString(updateRequestBody))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is("Macbook Pro")))
                .andExpect(jsonPath("$.item.price", is(799.99)))
                .andExpect(jsonPath("$.item.quantity", is(5)))
                .andExpect(jsonPath("$.item.sold", is(false)));
    }

    @Test
    void onValidRequestBodyWithDeleteMethodReturnsValidMessage() throws Exception {

        MockHttpServletRequestBuilder builder = delete(ITEM_URL)
                .content(resourceToString(deleteRequestBody))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Item was deleted successfully.")));

        assertThat(itemRepository.findAll()).hasSize(2);
    }

    @Test
    void onValidRequestParamWithSellItemMethodReturnsValidMessage() throws Exception {

        MockHttpServletRequestBuilder builder = post(ITEM_URL + "/sell")
                .queryParam("name", "Macbook Pro")
                .queryParam("quantity", "2")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("2 piece/pieces of item were sold successfully.")));
    }

    private String resourceToString(Resource resource) throws IOException {

        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}
