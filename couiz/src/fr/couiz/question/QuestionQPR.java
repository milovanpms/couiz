package fr.couiz.question;

import fr.couiz.question.PointsCategorieManager;
import fr.couiz.processus.Processus;

public class QuestionQPR extends Question {
    private String reponse;

    public QuestionQPR(String enonce, CategorieQuestion categorie, String reponse) {
        super(enonce, Type.QPR, categorie);
        this.reponse = reponse;
    }

    @Override
    public void afficher() {
        System.out.println("Question ouverte - " + enonce + " [" + getPoints() + " pts]");
    }

    @Override
    public boolean estBonneReponse(String reponseUtilisateur) {
        if (reponseUtilisateur == null) return false;

        String reponseAttendue = reponse.toLowerCase().trim();
        String saisie = reponseUtilisateur.toLowerCase().trim();

        if (saisie.length() <= 3 && !reponseAttendue.equals(saisie)) {
            return false; // trop court et pas exactement égal
        }

        Processus processus = new Processus();
        return processus.levenshtein(saisie, reponseAttendue);
    }

    @Override
    public String[] getPropositions() {
        return new String[0]; // Pas de choix proposés pour QPR
    }

    @Override
    public int getPoints() {
        return PointsCategorieManager.getPoints(categorie);
    }

    // Méthode pour récupérer la bonne réponse en texte
    @Override
    public String getReponseCorrecte() {
        return reponse;
    }
}