package com.zaranik.coursework.checkerservice.repositories.extractors;

import com.zaranik.coursework.checkerservice.entities.Solution;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class SolutionRowMapper implements RowMapper<Solution> {

  @Override
  public Solution mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Solution.builder()
      .id(rs.getLong("id"))
      .compilationStatus(rs.getString("compilation_status"))
      .testingStatus(rs.getString("testing_status"))
      .build();
  }
}
