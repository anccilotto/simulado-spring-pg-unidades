package br.com.fiap.unidades.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Personalizando erros de validação
 */
@Data
public class ValidationErrorResponse {

    private int status;
    private String message;
    private String endpoint;
    private List<ValidationError> errors = new ArrayList<>();

    public void addValidationError(String field, String message) {
        ValidationError error = new ValidationError( field, message );
        errors.add( error );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    // classe interna para representar um erro de validação
    public static class ValidationError {
        private String field;
        private String message;
    }
}