package com.dm.teamquery.config;

import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.model.Challenge;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Configuration
@Service
public class DataLoader {

    final private ChallengeService challengeService;
    final private DateTimeFormatter df =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Inject
    public DataLoader(ChallengeService challengeService) throws IOException {
        this.challengeService = challengeService;
        List<String> lines = Files.readAllLines(Paths.get("./update.dat"));
        String[] headers = lines.remove(0).split(":=:");
        lines.forEach((l) -> createChallenge(headers, l));
    }

    private void createChallenge(String [] headers, String line) {
        try {
            List<String> tokens = new LinkedList<>(Arrays.asList(line.split(":=:")));
            Challenge c = new Challenge();
            for (String s : headers) {
                String v = tokens.remove(0);
                switch (s) {
                    case "challengeId": c.setChallengeId(UUID.fromString(v)); break;
                    case "question": c.setQuestion(v); break;
                    case "answer": c.setAnswer(v); break;
                    case "author": c.setAuthor(v); break;
                    case "lastAuthor": c.setLastAuthor(v); break;
                    case "dateCreated": c.setDateCreated(LocalDateTime.parse(v, df));break;
                    case "dateLastModified": c.setDateLastModified(LocalDateTime.parse(v, df));break;
                }
            }
            challengeService.updateChallenge(c);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
