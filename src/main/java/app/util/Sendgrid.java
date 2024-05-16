package app.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;

public class Sendgrid {

        public boolean sendMail(String to, String name, String email, String zip) throws IOException {
            // Erstat xyx@gmail.com med din egen email, som er afsender
            Email from = new Email("otto.toubro@gmail.com");
            from.setName("Johannes Fog Byggemarked");

            Mail mail = new Mail();
            mail.setFrom(from);

            String API_KEY = System.getenv("SENDGRID_API_KEY");

            Personalization personalization = new Personalization();

            /* Erstat kunde@gmail.com, name, email og zip med egne værdier ****/
            /* I test-fasen - brug din egen email, så du kan modtage beskeden */
            personalization.addTo(new Email("kunde@gmail.com"));
            personalization.addDynamicTemplateData("name", "Anders And");
            personalization.addDynamicTemplateData("email", "anders@and.dk");
            personalization.addDynamicTemplateData("zip", "2100");
            mail.addPersonalization(personalization);

            mail.addCategory("carportapp");

            SendGrid sg = new SendGrid(API_KEY);
            Request request = new Request();
            try
            {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");

                // indsæt dit skabelonid herunder
                mail.templateId = "d-02687f175d1e4a5fb64da1bd3e97ea26";
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            }
            catch (IOException ex)
            {
                System.out.println("Error sending mail" + ex.getMessage());
                throw ex;
            }
            return true; // VED IK OM DET ER RIGTIGT
        }
}
