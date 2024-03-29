package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.demo.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
}
