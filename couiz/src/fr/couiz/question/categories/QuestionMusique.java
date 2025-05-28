package fr.couiz.question.categories;

import fr.couiz.question.Question;

public class QuestionMusique extends Question {
    private String genre;

    public QuestionMusique(String question, String reponseCorrecte, int basePoints, String genre, String[] reponsesAlternatives) {
        super(question, reponseCorrecte, basePoints, "Musique", reponsesAlternatives);
        this.genre = genre;
    }
}
