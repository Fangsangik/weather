package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.demo.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class MemoRepositoryTest {
    @Autowired
    JdbcMemoRepository memoRepository;

    @Test
    void save() {
        //given
        Memo memo = new Memo(1L, "this is new Memo");
        //when
        memoRepository.save(memo);
        //then
    }

    @Test
        void findById(){
        //given
        Memo memo = new Memo(2L, "Hello");
        //when
        memoRepository.save(memo);
        Optional<Memo> memoId = memoRepository.findById(2L);
        //then
        assertEquals(memoId.get().getText(), "Hello");
        assertEquals(memoId.get().getId(), 2);
       }

       @Test
           void findAll(){
           //given
           List<Memo> findAllMember = memoRepository.findAll();
           //when
           System.out.println("findAllMember = " + findAllMember);
           //findAllMember = [zerobase.weather.demo.Memo@5f0d8937]

           //then
           assertNotNull(findAllMember);
          }
}