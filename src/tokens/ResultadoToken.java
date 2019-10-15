package tokens;

/**
 * Clase para almacenar la cantidad de tokens encontrados
 * @author juan
 *
 */
public class ResultadoToken {

	private String token;
	private long cantidad;
	
	public ResultadoToken(String token, long cantidad) {
		setToken(token);
		setCantidad(cantidad);
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		assert token != null;
		this.token = token;
	}
	
	public long getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(long cantidad) {
		if(cantidad < 0)
			throw new IllegalArgumentException("La cantidad no puede ser negativa");
		this.cantidad = cantidad;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultadoToken other = (ResultadoToken) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return token + ", " + cantidad;
	}
	
}
