package tokens;

/**
 * Clase para agrupar informacion sobre un token encontrados
 * @author juan
 *
 */
public class TokenFound {

	private String token;// token encontrado
	private int startIndex;// indice donde se encontro el token
			
	/**
	 * Constructor
	 * @param token token encontrado, no debe ser null
	 * @param startIndex indice donde se encontro el token
	 */
	public TokenFound(String token, int startIndex) {
		setToken(token);
		setStartIndex(startIndex);
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		assert token != null;
		this.token = token;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(int startIndex) {
		if(startIndex < 0)
			throw new IllegalArgumentException("El indice no puede ser negativo");
		this.startIndex = startIndex;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + startIndex;
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
		TokenFound other = (TokenFound) obj;
		if (startIndex != other.startIndex)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return token + ", " + startIndex;
	}	
	
}
