/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramaticasql;

/**
 *
 * @author Sanch
 */

public enum TipoToken {
    IDENTIFICADOR,

    // Palabras reservadas
    SELECT, FROM, DISTINCT,

    // Caracteres
    COMA, PUNTO, ASTERISCO,
    
    EPSILON,

    // Final de cadena
    EOF
}
