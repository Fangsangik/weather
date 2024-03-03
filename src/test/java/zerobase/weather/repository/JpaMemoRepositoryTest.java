package zerobase.weather.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.demo.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional //다 롤백 처리를 해준다.
@SpringBootTest
public class JpaMemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @BeforeEach
    void beforeEach() {
        System.out.println("clear");
    }

    @Test
    void save() {
        //given
        Memo memo = new Memo(1L, "Hello");
        //when
        memoRepository.save(memo);
        //then
        List<Memo> memoAll = memoRepository.findAll();
        assertTrue(memoAll.size() > 0);
    }

    @Test
    @Transactional
    void findById() {
        //given
        Memo memo = new Memo(2L, "Hello");
        //when
        Memo save = memoRepository.save(memo);
        System.out.println("save.getId() = " + save.getId());
        Optional<Memo> id = memoRepository.findById(save.getId());
        //then
        assertEquals(id.get().getId(), 2L);
    }

}
