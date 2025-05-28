package fr.couiz.question.categories;

import fr.couiz.question.Question;

public class QuestionTV extends Question {
    private String x;

    public QuestionTV(String question, String reponseCorrecte, int basePoints, String x, String[] reponsesAlternatives) {
        super(question, reponseCorrecte, basePoints, "TV", reponsesAlternatives);
        this.x = x;
    }
}