package com.example.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Actor;

public class ActorRepository implements Repository<Actor> {

    private final String URL = "jdbc:mysql://localhost:3306/sakila";
    private final String USER = "root";
    private final String PASSWORD = "zero100"; 

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    @Override
    public List<Actor> findAll() {
        List<Actor> list = new ArrayList<>();
        String sql = "SELECT actor_id, first_name, last_name FROM actor";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Actor actor = new Actor();
                actor.setId(rs.getInt("actor_id"));
                actor.setFirstName(rs.getString("first_name"));
                actor.setLastName(rs.getString("last_name"));
                list.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Actor getByID(Integer id) {
        String sql = "SELECT actor_id, first_name, last_name FROM actor WHERE actor_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Actor actor = new Actor();
                actor.setId(rs.getInt("actor_id"));
                actor.setFirstName(rs.getString("first_name"));
                actor.setLastName(rs.getString("last_name"));
                return actor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void save(Actor actor) {
        String sql = "INSERT INTO actor (first_name, last_name, last_update) VALUES (?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, actor.getFirstName());
            pstmt.setString(2, actor.getLastName());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        actor.setId(generatedId); 
                        System.out.println("Actor insertado con ID: " + generatedId);
                    }
                }
            } else {
                System.out.println("No se insertó ningún actor.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM actor WHERE actor_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Actor eliminado correctamente.");
            } else {
                System.out.println("No se encontró actor con ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
