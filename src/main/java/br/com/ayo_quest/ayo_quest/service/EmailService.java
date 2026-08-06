package br.com.ayo_quest.ayo_quest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    public void enviarConvite(
            String destinatario,
            String nomeProfessor,
            String nomeTurma,
            String linkConvite) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(destinatario);

            helper.setSubject("Você foi convidado para uma turma no AyoQuest 🚀");

            String html = montarTemplate(
                    nomeProfessor,
                    nomeTurma,
                    linkConvite
            );

            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar convite por e-mail.", e);
        }

    }

    private String montarTemplate(
            String professor,
            String turma,
            String link) {

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
            </head>

            <body style="margin:0;padding:0;background:#0f1238;font-family:Arial,sans-serif;">

                <table width="100%%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="center" style="padding:40px;">

                            <table width="600" style="background:#171c54;border-radius:20px;padding:40px;">

                                <tr>
                                    <td align="center">

                                        <img
                                          src="https://ayoquest.com/assets/logo-ayo.png"/>

                                        <h1 style="color:white;">
                                            Você foi convidado!
                                        </h1>

                                        <p style="color:#d9e0ff;font-size:16px;">

                                            O professor
                                            <strong>%s</strong>

                                            convidou você para participar da turma

                                            <strong>%s</strong>.

                                        </p>

                                        <br>

                                        <a
                                            href="%s"
                                            style="
                                                background:#337ab7;
                                                color:white;
                                                padding:16px 32px;
                                                border-radius:12px;
                                                text-decoration:none;
                                                font-weight:bold;
                                            ">

                                            Aceitar convite

                                        </a>

                                        <br><br>

                                        <p style="color:#9fb4ff;font-size:13px;">
                                            Este convite expira em 7 dias.
                                        </p>

                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>
                </table>

            </body>
            </html>
            """.formatted(
                professor,
                turma,
                link
        );

    }


    public void enviarConfirmacaoEmail(
            String email,
            String nome,
            String token
    ) throws MessagingException, UnsupportedEncodingException {

        String link =
                "http://localhost:4200/confirmar?token="
                        + token;


        try {

            MimeMessage mensagem =
                    mailSender.createMimeMessage();


            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mensagem,
                            true,
                            "UTF-8"
                    );


            helper.setFrom(
                    from,
                    "AYO QUEST"
            );


            helper.setTo(email);


            helper.setSubject(
                    "Confirme seu cadastro no AyoQuest 🚀"
            );


            String html = """
        <!DOCTYPE html>
        <html lang="pt-BR">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Confirme seu cadastro</title>
        </head>


        <body style="
            margin:0;
            padding:0;
            background-color:#0f1238;
            font-family:Arial, Helvetica, sans-serif;
            color:#ffffff;
        ">


        <table role="presentation"
               width="100%%"
               cellspacing="0"
               cellpadding="0"
               style="
                    background:#0f1238;
               ">


            <tr>

            <td align="center"
                style="
                    padding:40px 20px;
                ">


                <table width="600"
                       cellspacing="0"
                       cellpadding="0"
                       style="
                            max-width:600px;
                            background:#171c54;
                            border-radius:20px;
                            padding:40px;
                       ">


                    <tr>

                    <td align="center">


                        <img
                        src="https://hpvplvslpbncrpklzqdh.supabase.co/storage/v1/object/public/images-server/logo-ayo.png"
                        alt="AyoQuest"
                        style="
                            max-width:180px;
                            margin-bottom:30px;
                        "
                        >



                        <p style="
                            color:#9fb4ff;
                            font-size:14px;
                            letter-spacing:1px;
                            text-transform:uppercase;
                        ">
                            Bem-vindo à jornada
                        </p>



                        <h1 style="
                            color:white;
                            font-size:30px;
                        ">
                            Olá {{NOME}} 👋
                        </h1>



                        <p style="
                            color:#d9e0ff;
                            font-size:16px;
                            line-height:1.7;
                        ">
                            Seus primeiros passos na
                            <strong>AyoQuest</strong>
                            já começaram.
                        </p>



                        <p style="
                            color:#d9e0ff;
                            font-size:16px;
                            line-height:1.7;
                        ">
                            Clique no botão abaixo para confirmar
                            seu e-mail e liberar seu acesso.
                        </p>



                        <table cellspacing="0"
                               cellpadding="0"
                               align="center">


                            <tr>

                            <td style="
                                background:#337ab7;
                                border-radius:12px;
                            ">


                                <a href="{{LINK}}"
                                   style="
                                    display:inline-block;
                                    padding:16px 32px;
                                    color:white;
                                    font-weight:bold;
                                    text-decoration:none;
                                    border-radius:12px;
                                   ">

                                    Confirmar meu e-mail

                                </a>


                            </td>

                            </tr>


                        </table>




                        <p style="
                            margin-top:30px;
                            color:#aeb8e8;
                            font-size:13px;
                            line-height:1.6;
                        ">

                            Se você não criou esta conta,
                            pode ignorar este e-mail.

                        </p>


                    </td>

                    </tr>


                </table>



                <p style="
                    color:#8f98c9;
                    font-size:12px;
                    margin-top:24px;
                ">
                    © 2026 AyoQuest. Todos os direitos reservados.
                </p>



            </td>

            </tr>


        </table>


        </body>

        </html>
        """;


            html = html
                    .replace("{{NOME}}", nome)
                    .replace("{{LINK}}", link);



            helper.setText(
                    html,
                    true
            );


            mailSender.send(mensagem);



        } catch (MessagingException | UnsupportedEncodingException e) {

            throw new RuntimeException(
                    "Erro ao enviar email de confirmação",
                    e
            );

        }

    }

    private String carregarTemplate() throws Exception {


        ClassPathResource resource =
                new ClassPathResource(
                        "templates/email-confirmacao.html"
                );


        return new String(
                resource.getInputStream().readAllBytes()
        );

    }


}