package org.example.project;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class ProjectDao {

  private String url;

  @SneakyThrows
  public InputStream getZipStream(Long solutionId) {
    Connection connection = DriverManager.getConnection(url);

    String sql = "select source_in_zip from code_sources where id = ?;";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setLong(1, solutionId);

    ResultSet resultSet = statement.executeQuery();
    resultSet.next();

    InputStream binaryStream = resultSet.getBinaryStream("source_in_zip");

    resultSet.close();
    connection.close();

    return binaryStream;
  }

  @SneakyThrows
  public void writeCompilationStatus(Long id, String value) {
    Connection connection = DriverManager.getConnection(url);

    String sql = "update code_sources set compilation_status = ? where id = ?;";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, value);
    statement.setLong(2, id);

    statement.executeUpdate();

    statement.close();
    connection.close();
  }

  @SneakyThrows
  public void writeTestingStatistics(Long id, String value) {
    Connection connection = DriverManager.getConnection(url);

    String sql = "update code_sources set testing_status = ? where id = ?;";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, value);
    statement.setLong(2, id);

    statement.executeUpdate();

    statement.close();
    connection.close();
  }

}
