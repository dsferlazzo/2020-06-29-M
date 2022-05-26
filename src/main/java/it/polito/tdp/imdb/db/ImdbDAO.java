package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Corrispondenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * dato un anno, ritorna tutti i directors che hanno fatto almeno un film in quell'anno
	 * @param anno
	 * @return
	 */
	public List<Director> getDirectorsByYear(int anno){
		String sql = "SELECT DISTINCT d.* "
				+ "FROM movies m, movies_directors md, directors d "
				+ "WHERE m.year=? AND m.id=md.movie_id AND md.director_id=d.id "
				+ "ORDER BY d.id asc";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * dato un anno, ritorna la lista di tutte le coppie di director con stesso actor, ed il loro peso
	 * @param anno
	 * @return
	 */
	public List<Corrispondenza> getCorrispondenzeByYear(int anno){
		String sql = "WITH actors_directors "
				+ "AS "
				+ "	( "
				+ "	SELECT DISTINCT a.id AS actor_id, md.director_id "
				+ "	FROM movies_directors md, roles r, actors a, movies m "
				+ "	WHERE a.id=r.actor_id AND md.movie_id=r.movie_id AND m.id=md.movie_id AND m.year=? "
				+ "	) "
				+ "SELECT ad1.director_id AS did1, ad2.director_id AS did2, COUNT(*) AS peso "
				+ "FROM actors_directors ad1, actors_directors ad2 "
				+ "WHERE ad1.actor_id=ad2.actor_id AND ad1.director_id>ad2.director_id "
				+ "GROUP BY ad1.director_id, ad2.director_id";
		List<Corrispondenza> result = new ArrayList<Corrispondenza>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				Corrispondenza c = new Corrispondenza(res.getInt("did1"),
						res.getInt("did2"), res.getInt("peso"));
				result.add(c);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
}
