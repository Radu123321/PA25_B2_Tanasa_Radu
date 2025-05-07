// Comandă pentru generarea unui raport HTML folosind FreeMarker
import freemarker.template.*;
import java.io.*;
import java.util.*;

public class ReportCommand implements Command {
    @Override
    public void execute(String[] args, ImageRepository repository) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31); // Configurare FreeMarker
        cfg.setDirectoryForTemplateLoading(new File("templates")); // Directorul unde e .ftl-ul
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("report.ftl"); // Încarcă șablonul

        Map<String, Object> data = new HashMap<>();
        data.put("images", repository.getAllImages()); // Adaugă lista imaginilor pentru template

        try (Writer writer = new FileWriter("report.html")) {
            template.process(data, writer); // Generează HTML-ul
        }

        DesktopHelper.openImage("report.html"); // Deschide raportul în browser
        System.out.println("Report generated and opened.");
    }
}
