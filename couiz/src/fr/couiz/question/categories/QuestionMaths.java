package fr.couiz.question.categories;

import fr.couiz.question.Question;

public class QuestionMaths extends Question {
    private String x;

    public QuestionMaths(String question, String reponseCorrecte, int basePoints, String x, String[] reponsesAlternatives) {
        super(question, reponseCorrecte, basePoints, "Maths", reponsesAlternatives);
        this.x = x;
    }
}