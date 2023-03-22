package hr.tvz.pios.mail;

import hr.tvz.pios.config.PiosProperties;
import hr.tvz.pios.modul.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Servis za slanje mailova.
 */
@Service
public class EmailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  PiosProperties piosProperties;
  @Autowired
  JavaMailSender mailSender;
  @Autowired
  SpringTemplateEngine templateEngine;



  public void generirajActivationEmail(User newUser, String activationToken) {
    String subject = "TVZ PcPartPicker - Registracija";
    String activationURL = piosProperties.frontendUrl() + "/" + activationToken;

    Context context = new Context();
    context.setVariable("user", newUser);
    context.setVariable("activationURL", activationURL);
    String html = templateEngine.process("mail/registracija", context);

    //TODO napraviti email template kak spada
    posaljiMail(newUser.getEmail(), subject, html);
  }

  private void posaljiMail(String toAddress, String subject, String html) {
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
      messageHelper.setFrom(piosProperties.mail().from());
      messageHelper.setTo(toAddress);
      messageHelper.setSubject(subject);
      messageHelper.setText(html, true);
    };
    try {
      mailSender.send(messagePreparator);
    } catch (MailException ex) {
      LOGGER.error("Greška kod slanja maila.", ex);
    }
  }
}
