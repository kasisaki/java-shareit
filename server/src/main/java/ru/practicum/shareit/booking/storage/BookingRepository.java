package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByRequestorIdOrderByStartDesc(Long requestorId, Pageable pageable);

    Page<Booking> findAllByRequestorIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long requestorId, LocalDateTime startLimit, LocalDateTime endLimit, Pageable pageable);

    Page<Booking> findAllByRequestorIdAndStartAfterOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime, Pageable pageable);

    Page<Booking> findAllByRequestorIdAndEndBeforeOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime, Pageable pageable);

    Page<Booking> findAllByRequestorIdAndStatusIsOrderByStartDesc(Long requestorId,
                                                                  BookingStatus status,
                                                                  Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(Long itemOwnerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long requestorId, LocalDateTime start, LocalDateTime dateTime, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusIsOrderByStartDesc(Long requestorId,
                                                                  BookingStatus status, Pageable pageable);

    Boolean existsByItemIdAndRequestorIdAndStartIsBefore(Long itemId,
                                                         Long requestorId,
                                                         LocalDateTime dateTime);

    boolean existsByIdAndRequestorId(Long bookingId, Long requestorId);

    boolean existsByIdAndItemOwnerId(Long bookingId, Long ownerId);

    Booking findFirstByStatusAndItemIdAndStartIsAfterOrderByStartAsc(BookingStatus status,
                                                                     Long itemId, LocalDateTime dateTime);

    Booking findFirstByStatusAndItemIdAndStartIsBeforeOrderByStartDesc(BookingStatus status,
                                                                       Long itemId, LocalDateTime dateTime);
}
