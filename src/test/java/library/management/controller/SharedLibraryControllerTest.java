package library.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import library.management.dto.MemberDto;
import library.management.service.SharedLibraryService;

@WebMvcTest(SharedLibraryController.class)
public class SharedLibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SharedLibraryService sharedLibraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateMember() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setName("John Doe");
        memberDto.setEmail("john@example.com");

        when(sharedLibraryService.createMember(any(MemberDto.class))).thenReturn(memberDto);

        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testListMembers() throws Exception {
        MemberDto m1 = new MemberDto();
        m1.setName("Alice");
        MemberDto m2 = new MemberDto();
        m2.setName("Bob");

        when(sharedLibraryService.listMembers()).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }
}
