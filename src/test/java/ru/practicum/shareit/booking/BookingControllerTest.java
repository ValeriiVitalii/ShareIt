package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.features.booking.BookingController;
import ru.practicum.shareit.features.booking.BookingStatus;
import ru.practicum.shareit.features.booking.model.BookingDto;
import ru.practicum.shareit.features.booking.model.BookingDtoShort;
import ru.practicum.shareit.features.booking.service.BookingServiceDao;
import ru.practicum.shareit.features.item.model.ItemShortIdWithName;
import ru.practicum.shareit.features.user.model.User;

import static org.mockito.ArgumentMatchers.any;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {


    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingServiceDao bookingServiceDao;

    @Autowired
    private MockMvc mvc;

    private final ItemShortIdWithName itemShortIdWithName = ItemShortIdWithName.builder()
            .id(1L)
            .name("валерий")
            .build();

    private final User booker = new User(1L, "аава@mail.ru", "name");

    private final BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
            .start(LocalDateTime.of(2023, 7, 30, 14, 22, 22))
            .end(LocalDateTime.of(2023, 7, 30, 22, 22, 22))
            .itemId(itemShortIdWithName.getId())
            .booker(booker)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2023, 7, 30, 14, 22, 22))
            .end(LocalDateTime.of(2023, 7, 30, 22, 22, 22))
            .item(itemShortIdWithName)
            .booker(booker)
            .status(BookingStatus.WAITING)
            .build();

    private final BookingDto bookingDto2 = BookingDto.builder()
            .id(2L)
            .start(LocalDateTime.of(2023, 7, 30, 14, 22, 22))
            .end(LocalDateTime.of(2023, 7, 30, 22, 22, 22))
            .item(itemShortIdWithName)
            .booker(booker)
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void postBookingTest() throws Exception {
        when(bookingServiceDao.postBooking(any(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoShort))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void patchBookingTest() throws Exception {
        when(bookingServiceDao.patchBooking(any(), any(), any()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingServiceDao.getBookingById(any(), any()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getAllBookingByUserTest() throws Exception {
        when(bookingServiceDao.getAllBookingByBooker(any(), any(), any(), any()))
                .thenReturn(new ArrayList<>(List.of(bookingDto, bookingDto2)));

        mvc.perform(get("/bookings", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))

                .andExpect(jsonPath("$[1].id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].start", is(bookingDto2.getStart().toString())))
                .andExpect(jsonPath("$[1].end", is(bookingDto2.getEnd().toString())))
                .andExpect(jsonPath("$[1].item.id", is(bookingDto2.getItem().getId().intValue())))
                .andExpect(jsonPath("$[1].booker.id", is(bookingDto2.getBooker().getId().intValue())))
                .andExpect(jsonPath("$[1].status", is(bookingDto2.getStatus().toString())));
    }

    @Test
    void getAllBookingByOwnerTest() throws Exception {
        when(bookingServiceDao.getAllBookingByOwner(any(), any(), any(), any()))
                .thenReturn(new ArrayList<>(List.of(bookingDto, bookingDto2)));

        mvc.perform(get("/bookings/owner", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))

                .andExpect(jsonPath("$[1].id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].start", is(bookingDto2.getStart().toString())))
                .andExpect(jsonPath("$[1].end", is(bookingDto2.getEnd().toString())))
                .andExpect(jsonPath("$[1].item.id", is(bookingDto2.getItem().getId().intValue())))
                .andExpect(jsonPath("$[1].booker.id", is(bookingDto2.getBooker().getId().intValue())))
                .andExpect(jsonPath("$[1].status", is(bookingDto2.getStatus().toString())));


    }
}

