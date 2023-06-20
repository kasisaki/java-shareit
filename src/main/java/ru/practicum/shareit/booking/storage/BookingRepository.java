package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utils.BookingStatus;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByRequestorId(Long requestorId);

    List<Booking> findAllByRequestorIdAndStartBeforeAndEndAfterAndStatusIs(
            Long requestorId, LocalDateTime start, LocalDateTime end, BookingStatus status);

    List<Booking> findAllByRequestorIdAndStartAfterAndEndAfterAndStatusIs(
            Long requestorId, LocalDateTime start, LocalDateTime end, BookingStatus status);

    List<Booking> findAllByRequestorIdAndStartBeforeAndEndBeforeAndStatusIs(
            Long requestorId, LocalDateTime start, LocalDateTime end, BookingStatus status);

    List<Booking> findAllByRequestorIdAndStatusIs(Long requestorId, BookingStatus status);

    @Transactional
    List<Booking> findAllByItemOwnerIdAndStatusIs(Long itemOwnerId, BookingStatus statusStatus);

}
