package hr.tvz.pios.mail;

import hr.tvz.pios.config.PiosProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  PiosProperties piosProperties;
  @Autowired
  JavaMailSender mailSender;


  private void posaljiMail(String toAddress) {
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
      messageHelper.setFrom(piosProperties.getMail().getFrom());
      messageHelper.setTo(toAddress);
      messageHelper.setSubject("Temp");
      messageHelper.setText("generirani thymeleaf", true);
    };
    try {
      mailSender.send(messagePreparator);
    } catch (MailException ex) {
      LOGGER.error("Greška kod slanja maila.", ex);
    }
  }
}
