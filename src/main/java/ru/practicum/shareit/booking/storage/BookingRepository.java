package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByRequestorIdOrderByStartDesc(Long requestorId);

    List<Booking> findAllByRequestorIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long requestorId, LocalDateTime startLimit, LocalDateTime endLimit);

    List<Booking> findAllByRequestorIdAndStartAfterOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime);

    List<Booking> findAllByRequestorIdAndEndBeforeOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime);

    List<Booking> findAllByRequestorIdAndStatusIsOrderByStartDesc(Long requestorId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long itemOwnerId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long requestorId, LocalDateTime start, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
            Long requestorId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStatusIsOrderByStartDesc(Long requestorId, BookingStatus status);

    List<Booking> findAllByItemIdAndRequestorIdAndStartIsBefore(Long itemId, Long requestorId, LocalDateTime dateTime);

    boolean existsByIdAndRequestorId(Long bookingId, Long requesterId);

    boolean existsByIdAndItemOwnerId(Long bookingId, Long ownerId);

    Booking findFirstByStatusAndItemIdAndStartIsAfterOrderByStartAsc(BookingStatus status,
                                                                     Long itemId, LocalDateTime dateTime);

    Booking findFirstByStatusAndItemIdAndStartIsBeforeOrderByStartDesc(BookingStatus status,
                                                                       Long itemId, LocalDateTime dateTime);
}
