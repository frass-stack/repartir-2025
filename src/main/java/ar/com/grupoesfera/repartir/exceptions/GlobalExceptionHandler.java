package ar.com.grupoesfera.repartir.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GrupoInvalidoException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleGrupoInvalidoException(GrupoInvalidoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "El nombre del grupo debe tener al menos 2 caracteres.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
} 