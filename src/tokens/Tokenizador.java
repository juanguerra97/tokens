package tokens;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class Tokenizador {
	
	/**
	 * Encuentra todos los tokens en un texto
	 * @param text texto donde se buscaran tokens, no debe ser null
	 * @param tokens tokens a buscar en el texto, no debe ser null
	 * @return lista de objetos TokenFound que contiene el token encontrado
	 * y su posicion en el texto
	 */
	public static List<TokenFound> findTokens(String text,List<String> tokens){
		assert text != null;
		assert tokens != null;
		
		List<TokenFound> tokensFound = new LinkedList<>();// lista con los tokens encontrados
		
		// si el texto esta vacio o no hay tokens que encontrar se devuelve una lista vacia
		if(text.isEmpty() || tokens.isEmpty())
			return tokensFound;
		
		final int len = text.length();// tamaño del texto
		
		// se recorren todos los caracteres del texto
		for(int textIndex = 0; textIndex < len; ++textIndex ) {
			
			final char currChar = text.charAt(textIndex);// caracter actual del texto siendo analizado
			
			// si ningun token inicia con el caracter currChar si pasa a la siguiente iteracion
			if(tokens.stream().filter(
					token -> token.charAt(0) == currChar).count() == 0)
				continue;
			
			// se obtiene los tokens que inician con el caracter currChar
			List<String> possibleMatches = tokens.stream()
				.filter( token -> token.charAt(0) == currChar)
					.collect(Collectors.toList());
			
			int endTextIndex = textIndex + 1;// indice donde termina una palabra
			int wordSize = 1;// tamaño
			final int ti = textIndex;
			
			while(endTextIndex <= len) {
				
				final int ws = wordSize;
				final int eti = endTextIndex;
				String word = text.substring(textIndex, endTextIndex);
				
				possibleMatches.stream().filter(token -> token.length() == ws)
					.filter(token -> token.equals(word))
						.forEach(token -> tokensFound.add(new TokenFound(token,ti)));
				
				if( eti >= len || possibleMatches.stream()
					.filter(token -> token.length() > ws)
						.filter(token -> token.charAt(ws) == text.charAt(eti))
							.count() == 0)
					break;
				
				possibleMatches = possibleMatches.stream()
					.filter(token -> token.length() > ws)
						.filter(token -> token.charAt(ws) == text.charAt(eti))
							.collect(Collectors.toList());
				
				++endTextIndex;
				++wordSize;
	
			}
			
		}
		return tokensFound;
	}
	
	/**
	 * Metodo para obtener una lista de ResultadoToken a partir  de una lista de TokenFound
	 * @param tokensFound tokens encontrados en un texto
	 * @return lista de objetos ResultadoToken de los tokens encontrados
	 */
	public static List<ResultadoToken> resumirTokens(List<TokenFound> tokensFound){
		assert tokensFound != null;
		return tokensFound.stream().collect(
				Collectors.groupingBy(TokenFound::getToken,Collectors.counting()))
				.entrySet().stream()
				.map(es -> new ResultadoToken(es.getKey(),es.getValue()))
				.collect(Collectors.toList());
	}

}
