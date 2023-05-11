package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.features.item.model.ItemDto;
import ru.practicum.shareit.features.request.ItemRequestController;
import ru.practicum.shareit.features.request.model.ItemRequestDto;
import ru.practicum.shareit.features.request.model.ItemRequestWithItems;
import ru.practicum.shareit.features.request.service.ItemRequestServiceDao;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestServiceDao itemRequestServiceDao;

    @Autowired
    private MockMvc mvc;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("Поиск дрели")
            .created(LocalDateTime.of(2023, 4, 30, 14, 22, 22))
            .build();

    private final ItemRequestWithItems itemRequestWithItems = ItemRequestWithItems.builder()
            .id(2L)
            .description("Поиск машины")
            .created(LocalDateTime.of(2023, 4, 10, 14, 15, 22))
            .items(new ArrayList<>(List.of(new ItemDto(), new ItemDto())))
            .build();

    @Test
    void postItemTest() throws Exception {
        when(itemRequestServiceDao.postItemRequest(any(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())));
    }

    @Test
    void getItemRequestWithItemsByCreatorTest() throws Exception {
        when(itemRequestServiceDao.getItemRequestWithItemsByCreator(any()))
                .thenReturn(new ArrayList<>(List.of(itemRequestWithItems)));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestWithItems.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestWithItems.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestWithItems.getCreated().toString())))
                .andExpect(jsonPath("$[0].items.length()", is(2)));
    }

    @Test
    void getItemRequestDtoByUserIdTest() throws Exception {
        when(itemRequestServiceDao.getItemRequestWithItemsUserId(any(), any(), any()))
                .thenReturn(new ArrayList<>(List.of(itemRequestWithItems)));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestWithItems.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestWithItems.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestWithItems.getCreated().toString())))
                .andExpect(jsonPath("$[0].items.length()", is(2)));
    }

    @Test
    void getItemRequestDtoTest() throws Exception {
        when(itemRequestServiceDao.getItemRequestWithItemsById(any(), any()))
                .thenReturn(itemRequestWithItems);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestWithItems.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestWithItems.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestWithItems.getCreated().toString())))
                .andExpect(jsonPath("$.items.length()", is(2)));
    }
}
