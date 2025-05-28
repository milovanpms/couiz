package fr.couiz.question.categories;

import fr.couiz.question.Question;

public class QuestionCinema extends Question {
    private String x;

    public QuestionCinema(String question, String reponseCorrecte, int basePoints, String x, String[] reponsesAlternatives) {
        super(question, reponseCorrecte, basePoints, "Cinéma", reponsesAlternatives);
        this.x = x;
    }
}