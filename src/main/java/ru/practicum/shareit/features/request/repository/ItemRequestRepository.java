package ru.practicum.shareit.features.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByCreator(User creator);

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.creator.id <> ?1 ORDER BY ir.created DESC")
    List<ItemRequest> findAllItemRequestExceptCreator(Long userid, Pageable pageable);

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.id = ?1")
    ItemRequest findItemRequestById(Long requestId);
}
