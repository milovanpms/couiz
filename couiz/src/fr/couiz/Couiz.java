package fr.couiz;

import fr.couiz.question.*;
import fr.couiz.utilitaire.*;
import fr.couiz.ui.*;
import java.util.*;
import fr.couiz.processus.Processus;

public class Couiz {
    private int timer = 30;
    private boolean autoriserQMC = true;
    private boolean autoriserQPR = true;
    private boolean autoriserIMAGE = false;
    private final GestionAleatoire aleatoire = new GestionAleatoire();
    private Etat etat = Etat.INIT;
    private int score = 0;

    private final IHM ihm = new IHM();
    private final Utilisateur utilisateur = new Utilisateur("toto");
    private final Minuterie minuterie = new Minuterie(); // Thread-based maintenant
    private final Classement classement = new Classement();

    private List<Question> questions;
    private Question questionCourante;
    private boolean minuterieLancee = false;
    private boolean finPartieTraitee = false;

    public void lancer() {
        while (etat != Etat.FIN) {
            if (minuterieLancee && minuterie.estTerminee() && !finPartieTraitee) {
                etat = Etat.FIN_PARTIE;
                finPartieTraitee = true;
                minuterie.arreter();
            }

            switch (etat) {
                case INIT -> initialiser();
                case ATTENDRE_START -> attendreDebut();
                case DEMANDE_PSEUDO -> demanderPseudo();
                case POSER_QUESTION -> poserQuestion();
                case ATTENDRE_REPONSE -> attendreReponse();
                case VERIFIER_REPONSE -> verifierReponse();
                case RETOUR_QUESTION -> retourQuestion();
                case PAUSE_MENU -> menuPause();
                case FIN_PARTIE -> finPartie();
                case MENU_CLASSEMENT -> afficherClassement();
            }
        }
    }

    private void initialiser() {
        try {
            PointsCategorieManager.chargerDepuisCSV("categorie_points.csv");
            questions = CsvLecteur.lireQuestions("questions.csv");

            // Utilise le classement.csv local ou fallback sur default.csv (dans ressources)
            classement.initialiser("classement.csv", "default.csv");

            etat = Etat.ATTENDRE_START;
        } catch (Exception e) {
            ihm.afficherMessage("Erreur lors de l'initialisation.");
            etat = Etat.FIN;
        }
    }

    private void attendreDebut() {
        ihm.afficherHome();
        if (ihm.demanderDebut()) {
            etat = Etat.DEMANDE_PSEUDO;
        }
    }

    private void demanderPseudo() {
        ihm.afficherPseudo();
        if (ihm.getReponse()) {
            String pseudo = ihm.demanderPseudo();
            utilisateur.setPseudo(pseudo);
            ihm.setReponse(false);
            etat = Etat.POSER_QUESTION;
        }
    }

    private void poserQuestion() {
        if (!minuterieLancee) {
            minuterie.debut(timer);
            minuterieLancee = true;
        }

        if (minuterie.estTerminee() || finPartieTraitee || questions.isEmpty()) {
            ihm.arreterSaisie();
            etat = Etat.FIN_PARTIE;
            return;
        }

        if (questionCourante == null) {
            // Filtrage des types autorisés
            List<Question> questionsFiltrees = new ArrayList<>();
            for (Question q : questions) {
                Type t = q.getType();
                if ((t == Type.QMC && autoriserQMC) ||
                        (t == Type.QPR && autoriserQPR) ||
                        (t == Type.IMAGE && autoriserIMAGE)) {
                    questionsFiltrees.add(q);
                }
            }

            if (questionsFiltrees.isEmpty()) {
                ihm.afficherMessage("Plus de questions du type autorisé.");
                etat = Etat.FIN_PARTIE;
                return;
            }

            // Tirage aléatoire dans les types autorisés
            aleatoire.setGrandeBorne(questionsFiltrees.size() - 1);
            int index = aleatoire.nombreAleatoire();
            questionCourante = questionsFiltrees.get(index);
        }

        ihm.afficherQuestion(questionCourante);
        ihm.afficherMessage(" Temps restant : " + minuterie.getTempsActuel() + " secondes");
        etat = Etat.ATTENDRE_REPONSE;
    }

    private void attendreReponse() {
        if (finPartieTraitee || minuterie.estTerminee()) {
            ihm.arreterSaisie();
            etat = Etat.FIN_PARTIE;
            return;
        }

        String reponse = ihm.recupererReponse();

        if (minuterie.estTerminee() || finPartieTraitee) {
            ihm.arreterSaisie();
            etat = Etat.FIN_PARTIE;
        } else if (reponse == null || reponse.isBlank()) {
            etat = Etat.RETOUR_QUESTION;
        } else {
            utilisateur.setReponseCourante(reponse);
            etat = Etat.VERIFIER_REPONSE;
        }
    }

    private void verifierReponse() {
        String reponseUtilisateur = utilisateur.getReponseCourante();
        boolean estCorrect = questionCourante.estBonneReponse(reponseUtilisateur);

        switch (questionCourante.getType()) {
            case QMC -> {
                if (estCorrect) {
                    score += questionCourante.getPoints();
                    ihm.afficherMessage("Bonne réponse !");
                } else {
                    ihm.afficherMessage("Mauvaise réponse !");
                }
                questions.remove(questionCourante); // retirée qu'importe la réponse
                questionCourante = null;
                etat = questions.isEmpty() ? Etat.FIN_PARTIE : Etat.POSER_QUESTION;
            }

            case QPR, IMAGE -> {
                if (estCorrect) {
                    score += questionCourante.getPoints();
                    ihm.afficherMessage("Bonne réponse !");
                    questions.remove(questionCourante); // retirée seulement si bonne réponse
                    questionCourante = null;
                    etat = questions.isEmpty() ? Etat.FIN_PARTIE : Etat.POSER_QUESTION;
                } else {
                    ihm.afficherMessage("Faux, réessaie !");
                    // On garde la même question
                    etat = Etat.POSER_QUESTION;
                }
            }

            default -> {
                ihm.afficherMessage("Type de question non géré.");
                etat = Etat.FIN_PARTIE;
            }
        }
    }

    private void retourQuestion() {
        etat = questions.isEmpty() ? Etat.FIN_PARTIE : Etat.POSER_QUESTION;
    }

    private void menuPause() {
        if (ihm.demanderReprise()) {
            etat = Etat.POSER_QUESTION;
        } else {
            etat = Etat.FIN_PARTIE;
        }
    }

    private void finPartie() {
        minuterie.arreter();
        ihm.afficherScore(score);
        classement.ajouter(new Enregistrement(utilisateur.getPseudo(), score));
        classement.trierParScoreDecroissant();
        classement.sauvegarder("classement.csv");
        etat = Etat.MENU_CLASSEMENT;
    }

    private void afficherClassement() {
        ihm.afficherClassement(classement);
        etat = Etat.INIT;
    }
}
