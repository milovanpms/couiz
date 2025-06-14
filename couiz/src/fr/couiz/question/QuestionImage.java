package fr.couiz.question;

import fr.couiz.question.PointsCategorieManager;
import fr.couiz.processus.Processus;

public class QuestionImage extends Question {
    private String imagePath;
    private String reponse;

    public QuestionImage(String enonce, CategorieQuestion categorie, String imagePath, String reponse) {
        super(enonce, Type.IMAGE, categorie);
        this.imagePath = imagePath;
        this.reponse = reponse;
    }

    @Override
    public void afficher() {
        System.out.println("Question avec image - " + enonce + " [" + getPoints() + " pts]");
        System.out.println("Image à afficher : " + imagePath); // Remplacer par affichage image dans l'IHM graphique
    }

    @Override
    public boolean estBonneReponse(String reponseUtilisateur) {
        if (reponseUtilisateur == null) return false;
        Processus processus = new Processus();
        return processus.levenshtein(reponseUtilisateur.toLowerCase().trim(), reponse.toLowerCase().trim());
    }

    @Override
    public String[] getPropositions() {
        return new String[0]; // Pas de propositions pour QuestionImage
    }

    @Override
    public int getPoints() {
        return PointsCategorieManager.getPoints(categorie);
    }

    @Override
    public String getReponseCorrecte() {
        return reponse;
    }
}
