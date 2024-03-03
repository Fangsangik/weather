package zerobase.weather.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.demo.Memo;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMemoRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

//    public JdbcTemplate(DataSource dataSource){
//        jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    public Memo save(Memo memo) {
        String sql = "insert into memo values(?, ?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }


    //jdbc를 통해서 data를 가져오면 resultSet이라는 형식의 함수를 가져오게 된다.
    //{id = 1, text = 'this is memo'}
    private RowMapper<Memo> memoRowMapper() {
        return ((rs, rowNum) -> new Memo(
                rs.getLong("id"),
                rs.getString("text")
        ));
    }

    public List<Memo> findAll() {
        String sql = "select * from memo";
        return jdbcTemplate.query(sql, memoRowMapper());
    }

    public Optional<Memo> findById(Long id) {
        String sql = "select * from memo where id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }
}
