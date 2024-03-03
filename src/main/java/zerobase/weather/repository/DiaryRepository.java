package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.demo.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByDate(LocalDate date);
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    Diary getFirstByDate(LocalDate date);
    //findAll -> find *
    //getFirst -> find * 이기는 한데, 마지막에 limit 1 -> 한줄만 나온다.

    @Transactional
    //원래대로 복구 & data 주고 받거나 그럴 때 예외가 발생 할 수 있다.
    void deleteAllByDate(LocalDate date);
}
