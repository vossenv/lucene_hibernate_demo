package com.dm.teamquery.config;

import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.model.Challenge;
import net.andreinc.mockneat.abstraction.MockUnitString;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static net.andreinc.mockneat.types.enums.StringFormatType.CAPITALIZED;
import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.objects.Probabilities.probabilities;
import static net.andreinc.mockneat.unit.text.Formatter.fmt;
import static net.andreinc.mockneat.unit.text.NaughtyStrings.naughtyStrings;
import static net.andreinc.mockneat.unit.text.Words.words;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.user.Names.names;

@Service
public class DataGenerator {

    final private ChallengeService challengeService;

    @Inject
    public DataGenerator(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    public void generateChallenges() {

        MockUnitString addressGenerator =
                fmt("#{adj}#{noun} #{suffix} #{nr}")
                        .param("adj", probabilities(String.class)
                                .add(0.25, words()
                                        .adjectives()
                                        .format(CAPITALIZED)
                                        .append(" ")
                                )
                                .add(0.75, "")
                                .mapToString()
                        )
                        .param("noun", words().nouns().format(CAPITALIZED))
                        .param("suffix", probabilities(String.class)
                                .add(0.25, "Lane")
                                .add(0.25, "Blvd.")
                                .add(0.50, "Street")
                                .mapToString())
                        .param("nr", ints().range(1, 600));


        filler(Challenge::new)
                .setter(Challenge::setDateLastModified, localDates().thisYear())
                .setter(Challenge::setDateCreated, localDates().thisYear())
                .setter(Challenge::setAuthor, names().full())
                .setter(Challenge::setQuestion, addressGenerator)
                .setter(Challenge::setAnswer, naughtyStrings().inconousStrings())
                .list(50).get().forEach(challengeService::updateChallenge);
    }





    @PostConstruct
    void insertData(){
        generateChallenges();
    }



}


