package br.edu.ifsp.encryption.demo.controller;

import br.edu.ifsp.encryption.demo.domain.Mensagem;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
@Slf4j
public class CryptoController {

    private static String secretKey = "AESEncriptionTest";

    @GetMapping
    public String mostrarPagina(){
        return "home";
    }

    @ModelAttribute(name = "mensagem")
    public Mensagem mensagem(){
        return new Mensagem();
    }

    @PostMapping("/encriptar")
    public String encriptar(Mensagem mensagem, Model model){
        log.info("===============================================================");
        log.info("Controller encriptar");
        log.info("Mensagem antes de ser encriptada: {}", mensagem.getMensagem());

        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKey);
        String encriptada = textEncryptor.encrypt(mensagem.getMensagem());

        log.info("Mensagem depois de ser encriptada: {}", encriptada);
        model.addAttribute("encriptar", true);
        model.addAttribute("mensagem", encriptada);
        return "sucesso";

    }


    @PostMapping("/desencriptar")
    public String desencriptar(Mensagem mensagem, Model model){
        log.info("===============================================================");
        log.info("Controller desencriptar");
        log.info("Mensagem antes de ser desencriptada: {}", mensagem.getMensagem());

        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKey);
        String desencriptada = textEncryptor.decrypt(mensagem.getMensagem());

        log.info("Mensagem depois de ser desencriptada: {}", desencriptada);
        model.addAttribute("encriptar", false);
        model.addAttribute("mensagem", desencriptada);
        return "sucesso";
    }
}
