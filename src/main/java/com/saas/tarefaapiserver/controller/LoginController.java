package com.saas.tarefaapiserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * GET /login → renderiza o template templates/login.html
     *
     * Nós NÃO precisamos escrever a lógica de validar usuário/senha.
     * O Spring Security intercepta o POST /login automaticamente e cuida disso.
     * Este controller só serve para mostrar a tela.
     */
    @GetMapping("/login")
    public String login() {
        return "login";   // nome do arquivo em templates/, sem a extensão
    }
}