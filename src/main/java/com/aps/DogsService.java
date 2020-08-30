package com.aps;

import com.aps.domain.Dogs;
import com.github.javafaker.Dog;
import com.github.javafaker.Faker;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class DogsService {

  private static final int QUANTIDADEREPETICOES = 20000;

  public List<Dogs> generateDogsList() {
    Dog fakeDog = new Faker(new Locale("pt-BR")).dog();

    List<Dogs> dogs = new ArrayList<>();

    for (int i = 0; i < QUANTIDADEREPETICOES; i++) {
      Dogs dog = new Dogs(fakeDog.age(), fakeDog.breed(), fakeDog.coatLength(), fakeDog.gender(),
          fakeDog.memePhrase(), fakeDog.name(), fakeDog.size(), fakeDog.sound());
      dogs.add(dog);
    }
    return dogs;
  }

  public List<Dogs> generateDogsList2() {
    Dog fakeDog = new Faker(new Locale("pt-BR")).dog();

    List<Dogs> dogs = new ArrayList<>();

    for (int i = 0; i < QUANTIDADEREPETICOES; i++) {
      Dogs dog = new Dogs(fakeDog.age(), fakeDog.breed(), fakeDog.coatLength(), fakeDog.gender(),
          fakeDog.memePhrase(), fakeDog.name(), fakeDog.size(), fakeDog.sound());
      dogs.add(dog);
    }
    return dogs;
  }

  public File criarBancoDadosJDBC() {
    Connection connection = null;
    final String DATABASE_NAME = "banco.db";
    try {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
      connection.setAutoCommit(false);

      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      statement.executeUpdate("drop table if exists dogs");
      statement
          .executeUpdate("CREATE TABLE dogs (id integer PRIMARY KEY AUTOINCREMENT, name string)");

      List<Dogs> dogsList = generateDogsList();

      PreparedStatement pstmt = connection.prepareStatement("insert into dogs (name) values (?)");

      for (Dogs dogs : dogsList) {
        pstmt.setString(1, dogs.getName());
        pstmt.addBatch();
      }

      pstmt.executeBatch();
      connection.commit();

    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
      }
    }

    return new File(DATABASE_NAME);
  }

}
