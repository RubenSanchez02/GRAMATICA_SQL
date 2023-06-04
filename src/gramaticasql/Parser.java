/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramaticasql;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Sanch
 */

public class Parser {

    private final List<Token> tokens;

    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "");
    private final Token select = new Token(TipoToken.SELECT, "select");
    private final Token from = new Token(TipoToken.FROM, "from");
    private final Token distinct = new Token(TipoToken.DISTINCT, "distinct");
    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token asterisco = new Token(TipoToken.ASTERISCO, "*");
    private final Token finCadena = new Token(TipoToken.EOF, "");
    private final Token epsilon = new Token(TipoToken.EPSILON, " ");
    
    private int i = 0;

    private Token preanalisis;
    private final Map<String, Map<Token, List<?>>> parsingTable;

    {
        parsingTable = new HashMap<>();

        parsingTable.put("Q", new HashMap<>());
        parsingTable.get("Q").put(select, new ArrayList<>(Arrays.asList(select, "D", from, "T")));

        parsingTable.put("D", new HashMap<>());
        parsingTable.get("D").put(distinct, new ArrayList<>(Arrays.asList(distinct, "P")));
        parsingTable.get("D").put(asterisco, new ArrayList<>(Collections.singletonList("P")));
        parsingTable.get("D").put(identificador, new ArrayList<>(Collections.singletonList("P")));

        parsingTable.put("P", new HashMap<>());
        parsingTable.get("P").put(asterisco, new ArrayList<>(Collections.singletonList(asterisco)));
        parsingTable.get("P").put(identificador, new ArrayList<>(Collections.singletonList("A")));

        parsingTable.put("A", new HashMap<>());
        parsingTable.get("A").put(identificador, new ArrayList<>(Arrays.asList("A2", "A1")));

       
    }

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
            i = 0;
            preanalisis = tokens.get(i);
           Stack<Object> symbolStack = new Stack<>();
            symbolStack.push(finCadena);
            symbolStack.push("Q");

            Object topValueStack = symbolStack.peek();
                    while (!symbolStack.isEmpty()) {
            if (preanalisis.equals(topValueStack)) {
                symbolStack.pop();
                if (++i >= tokens.size()) break;
                preanalisis = tokens.get(i);
            } else if (topValueStack instanceof Token) {
                error( "Consulta no válida");
                return;
            } else if (getProductions(topValueStack, preanalisis) == null || getProductions(topValueStack, preanalisis).isEmpty()) {
                error( "Consulta no válida");
                return;
            }

            topValueStack = symbolStack.peek();
        }
        System.out.println("Consulta válida");
    }

    private java.util.List<?> getProductions(Object topValueStack, Token lookahead) {
        return parsingTable.get(topValueStack).entrySet().stream()
            .filter(entry -> entry.getKey().equals(lookahead))
            .map(java.util.Map.Entry::getValue)
            .findFirst()
            .orElse(null);
    }

    void error( String message) {
    System.err.println("Error at position " + ": " + message);
    }
}