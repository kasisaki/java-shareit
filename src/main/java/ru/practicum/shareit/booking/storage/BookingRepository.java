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

    List<Booking> findAllByRequestorIdAndStartBeforeAndEndAfterAndStatusIsOrderByStartDesc(
            Long requestorId, LocalDateTime start, LocalDateTime end, BookingStatus status);

    List<Booking> findAllByRequestorIdAndStartAfterAndStatusIsOrderByStartDesc(
            Long requestorId, LocalDateTime start, BookingStatus status);

    List<Booking> findAllByRequestorIdAndEndBeforeAndStatusIsOrderByStartDesc(
            Long requestorId, LocalDateTime start, BookingStatus status);

    List<Booking> findAllByRequestorIdAndStatusIsOrderByStartDesc(Long requestorId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long itemOwnerId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterAndStatusIsOrderByStartDesc(
            Long requestorId, LocalDateTime start, LocalDateTime end, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStartAfterAndStatusIsOrderByStartDesc(
            Long requestorId, LocalDateTime start, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndEndBeforeAndStatusIsOrderByStartDesc(
            Long requestorId, LocalDateTime start, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStatusIsOrderByStartDesc(Long requestorId, BookingStatus status);

}
