package br.edu.ifsp.encryption.demo.controller;

import br.edu.ifsp.encryption.demo.domain.Mensagem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@RequestMapping("/")
@Controller
@Slf4j
public class CryptoController {

    private static String secretKey = "AESEncriptionTest";
    private static String salt = "AESEncription";

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

        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            String encriptada = Base64.getEncoder().encodeToString(cipher.doFinal(mensagem.getMensagem().getBytes("UTF-8")));

            log.info("Mensagem depois de ser encriptada: {}", encriptada);
            model.addAttribute("encriptar", true);
            model.addAttribute("mensagem", encriptada);
            return "sucesso";
        }
        catch (Exception e)
        {
            log.error("Erro na encriptação");
        }

        return "home";
    }


    @PostMapping("/desencriptar")
    public String desencriptar(Mensagem mensagem, Model model){
        log.info("===============================================================");
        log.info("Controller desencriptar");
        log.info("Mensagem antes de ser desencriptada: {}", mensagem.getMensagem());

        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            String desencriptada = new String(cipher.doFinal(Base64.getDecoder().decode(mensagem.getMensagem())));

            log.info("Mensagem depois de ser desencriptada: {}", desencriptada);
            model.addAttribute("encriptar", false);
            model.addAttribute("mensagem", desencriptada);
            return "sucesso";
        }
        catch (Exception e)
        {
            log.error("Erro na desencriptação");
        }

        return "home";
    }
}
