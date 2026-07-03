package br.com.ayo_quest.ayo_quest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
                                            src="https://hpvplvslpbncrpklzqdh.supabase.co/storage/v1/object/public/images-server/logo-ayo.png"
                                            width="180">

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

}