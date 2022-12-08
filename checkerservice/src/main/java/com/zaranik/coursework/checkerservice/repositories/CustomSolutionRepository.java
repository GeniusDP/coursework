package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.repositories.extractors.SolutionRowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CustomSolutionRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public Solution save(Solution solution) {
    SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
      .withTableName("code_sources")
      .usingGeneratedKeyColumns("id");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("source_in_zip", solution.getSourceInZip());
    parameters.put("compilation_status", solution.getCompilationStatus());
    parameters.put("tests_number", solution.getTestsNumber());
    parameters.put("tests_passed", solution.getTestsPassed());
    parameters.put("testing_status", solution.getTestingStatus());

    Long id = (Long)insert.executeAndReturnKey(parameters);
    solution.setId(id);
    return solution;
  }

  public Solution findById(Long id) {
    String sql = "select * from code_sources where id = ?";
    List<Solution> solutionList = jdbcTemplate.query(sql, new SolutionRowMapper(), id);
    if(solutionList.isEmpty()){
      return null;
    }
    return solutionList.get(0);
  }

}
