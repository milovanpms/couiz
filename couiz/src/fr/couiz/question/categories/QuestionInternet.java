package fr.couiz.question.categories;

import fr.couiz.question.Question;

public class QuestionInternet extends Question {
    private String x;

    public QuestionInternet(String question, String reponseCorrecte, int basePoints, String x, String[] reponsesAlternatives) {
        super(question, reponseCorrecte, basePoints, "Internet", reponsesAlternatives);
        this.x = x;
    }
}