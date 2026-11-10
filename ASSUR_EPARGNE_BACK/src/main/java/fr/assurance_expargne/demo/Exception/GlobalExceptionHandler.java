package fr.assurance_expargne.demo.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // JSON illisible : champ obligatoire absent, mauvais type, mode inconnu…
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> requeteIllisible(HttpMessageNotReadableException ex) {
        return Map.of("requete",
                "Requête incomplète ou mal formée : un champ obligatoire manque ou n'a pas le bon format. "
                + "Si vous voyez ce message sur le site, rechargez la page (Ctrl + F5).");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) 
    @ResponseStatus(HttpStatus.BAD_REQUEST)       
    public Map<String, String> erreursDeValidation(MethodArgumentNotValidException ex) {

        Map<String, String> erreurs = new HashMap<>();

        for (FieldError erreur : ex.getBindingResult().getFieldErrors()) {
            erreurs.put(erreur.getField(), erreur.getDefaultMessage());
        }

        return erreurs;
    }
}
