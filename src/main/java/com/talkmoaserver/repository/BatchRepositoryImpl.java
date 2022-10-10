package com.talkmoaserver.repository;

import com.talkmoaserver.entity.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BatchRepositoryImpl implements BatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<Word> words) {
        String sql = "INSERT INTO word (`WORD`) VALUES (?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, words.get(i).getWord());
            }
            @Override
            public int getBatchSize() {
                return words.size();
            }
        });
    }

}
