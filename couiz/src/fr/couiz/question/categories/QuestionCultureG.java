package fr.couiz.question.categories;

import fr.couiz.question.Question;

public class QuestionCultureG extends Question {
    private String x;

    public QuestionCultureG(String question, String reponseCorrecte, int basePoints, String x, String[] reponsesAlternatives) {
        super(question, reponseCorrecte, basePoints, "Culture G", reponsesAlternatives);
        this.x = x;
    }
}