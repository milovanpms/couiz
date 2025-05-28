package fr.couiz.question.categories;

import fr.couiz.question.Question;

public class QuestionHistoire extends Question {
    private String x;

    public QuestionHistoire(String question, String reponseCorrecte, int basePoints, String x, String[] reponsesAlternatives) {
        super(question, reponseCorrecte, basePoints, "Histoire", reponsesAlternatives);
        this.x = x;
    }
}