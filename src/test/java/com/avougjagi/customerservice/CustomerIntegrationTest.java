package com.avougjagi.customerservice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional //Transactional för att ''rollback'' på allt i den riktiga db
class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    //POST anrop som returnerar 201 om kund blir skapad
    @Test
    void return201WhenCreatingCustomer() throws Exception {
        String validCustomerJson = """
                {
                  "firstName": "Boris",
                  "lastName": "Ivis",
                  "email": "boris@hotmail.com",
                  "phone": "07011223344"
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCustomerJson))
                .andExpect(status().isCreated());
    }

    //Returnerar 400 om namn är tomt
    @Test
    void return400WhenCreatingInvalidCustomer() throws Exception {
        String invalidCustomerJson = """
                {
                  "firstName": "",
                  "lastName": "Ivis",
                  "email": "test@email.com",
                  "phone": "07011223344"
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCustomerJson))
                .andExpect(status().isBadRequest());
    }

    //GET anrop som returnerar 404 om kunden inte existerar
    @Test
    void return404WhenGettingNonExistingCustomer() throws Exception {
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound());
    }
}
