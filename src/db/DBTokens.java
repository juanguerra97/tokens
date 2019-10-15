package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase con metodos para manipular la base de datos de los tokens
 * @author juan
 *
 */
public class DBTokens {
	
	public static final String SELECT_ALL = "SELECT * FROM tokens ORDER BY token";
	public static final String INSERT = "INSERT INTO tokens(token) VALUES(?)";
	public static final String DELETE = "DELETE FROM tokens WHERE token = ?";
	
	private Connection conexion;
	
	public DBTokens(Connection conexion) {
		setConexion(conexion);
	}
	
	public List<String> selectAll(){
		List<String> tokens = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL);
			while(rs.next())
				tokens.add(rs.getString("token"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tokens;
	}
	
	public boolean insert(String newToken) {
		assert newToken != null;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			st.setString(1, newToken);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete(String token) {
		assert token != null;
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setString(1, token);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void setConexion(Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}

}
