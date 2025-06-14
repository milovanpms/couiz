package fr.couiz.question;

import java.util.List;
import fr.couiz.question.PointsCategorieManager;
import fr.couiz.processus.Processus;

public class QuestionQCM extends Question {
    private List<String> choix;
    private int bonneReponse;  // index 1-based de la bonne réponse

    public QuestionQCM(String enonce, CategorieQuestion categorie, List<String> choix, int bonneReponse) {
        super(enonce, Type.QMC, categorie);
        this.choix = choix;
        this.bonneReponse = bonneReponse;
    }

    @Override
    public void afficher() {
        System.out.println("QCM - " + enonce + " [" + getPoints() + " pts]");
        for (int i = 0; i < choix.size(); i++) {
            System.out.println((i + 1) + ". " + choix.get(i));
        }
    }

    @Override
    public boolean estBonneReponse(String reponseUtilisateur) {
        Processus processus = new Processus();

        if (reponseUtilisateur == null || reponseUtilisateur.trim().isEmpty()) return false;

        String saisie = reponseUtilisateur.trim().toLowerCase();
        String bonneReponseTexte = choix.get(bonneReponse - 1).toLowerCase().trim();

        // Si l'utilisateur entre un chiffre
        try {
            int reponseNum = Integer.parseInt(saisie);
            return reponseNum == bonneReponse;
        } catch (NumberFormatException e) {
            // Si le texte est trop court et pas égal à la bonne réponse
            if (saisie.length() <= 3 && !saisie.equals(bonneReponseTexte)) return false;

            // Si la longueur est très différente, c'est sûrement faux
            if (Math.abs(saisie.length() - bonneReponseTexte.length()) > 3) return false;

            // Vérification avec Levenshtein
            return processus.levenshtein(saisie, bonneReponseTexte);
        }
    }

    // Nouvelle méthode pour récupérer la bonne réponse en texte
    @Override
    public String getReponseCorrecte() {
        return choix.get(bonneReponse - 1);
    }

    @Override
    public String[] getPropositions() {
        return choix.toArray(new String[0]);
    }

    @Override
    public int getPoints() {
        return PointsCategorieManager.getPoints(categorie);
    }
}