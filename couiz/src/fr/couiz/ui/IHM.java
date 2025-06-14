package fr.couiz.ui;

import fr.couiz.question.Question;
import fr.couiz.utilitaire.Classement;
import fr.couiz.utilitaire.Enregistrement;

import java.util.Scanner;
import java.util.concurrent.*;

public class IHM {
    private final Scanner scanner = new Scanner(System.in);
    private boolean reponse = false;

    // Executor pour gérer la saisie en thread séparé
    private final ExecutorService saisieExecutor = Executors.newSingleThreadExecutor();
    private Future<String> saisieFuture = null;

    // Affiche l'écran d'accueil et demande start ou quitter
    public void afficherHome() {
        System.out.println("=== Bienvenue dans Couiz ===");
        System.out.println("Tapez 'start' pour commencer ou 'quit' pour quitter.");
    }

    // Demande de début, gère la confirmation quit O/N
    public boolean demanderDebut() {
        System.out.print("Votre choix : ");
        String saisie = scanner.nextLine().trim().toLowerCase();

        if ("start".equals(saisie)) {
            reponse = true;
            return true;
        } else if ("quit".equals(saisie)) {
            System.out.print("Voulez-vous vraiment quitter ? (O/N) : ");
            String conf = scanner.nextLine().trim().toLowerCase();
            if (conf.equals("o") || conf.equals("oui")) {
                reponse = false;
                return false;
            } else {
                System.out.println("Reprise du jeu.");
                return demanderDebut();
            }
        } else {
            System.out.println("Commande non reconnue. Veuillez taper 'start' ou 'quit'.");
            return demanderDebut();
        }
    }

    public void afficherPseudo() {
        System.out.print("Veuillez saisir votre pseudo : ");
    }

    public String demanderPseudo() {
        String pseudo = scanner.nextLine().trim();
        if (pseudo.isEmpty()) {
            System.out.print("Pseudo invalide, veuillez ressaisir : ");
            return demanderPseudo();
        }
        reponse = true;
        return pseudo;
    }

    public boolean getReponse() {
        return reponse;
    }

    public void setReponse(boolean b) {
        reponse = b;
    }

    public void afficherQuestion(Question question) {
        System.out.println("\nQuestion : " + question.getIntitule());
        String[] propositions = question.getPropositions();
        if (propositions != null && propositions.length > 0) {
            for (int i = 0; i < propositions.length; i++) {
                System.out.println((i + 1) + ". " + propositions[i]);
            }
            System.out.print("Votre réponse (numéro ou texte) : ");
        } else {
            System.out.print("Votre réponse : ");
        }
    }

    /**
     * Méthode récupérant la réponse avec un temps limite.
     * Si le temps est dépassé, retourne null.
     */
    public String recupererReponseAvecTimeout(int secondes) {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<String> future = ex.submit(() -> scanner.nextLine().trim());

        try {
            String reponseUtilisateur = future.get(secondes, TimeUnit.SECONDS);
            if (reponseUtilisateur.isEmpty()) {
                return null;
            }
            return reponseUtilisateur;
        } catch (TimeoutException e) {
            System.out.println("\nTemps écoulé !");
            future.cancel(true);
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            ex.shutdownNow();
        }
    }

    /**
     * Méthode récupérant la réponse sans limite de temps,
     * mais la saisie se fait dans un thread séparé
     * pour pouvoir être interrompue en appelant arreterSaisie().
     */
    public String recupererReponse() {
        saisieFuture = saisieExecutor.submit(() -> {
            try {
                return scanner.nextLine().trim();
            } catch (Exception e) {
                return null;
            }
        });

        while (true) {
            try {
                return saisieFuture.get(200, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                // Vérifie si la minuterie globale est écoulée pendant l'attente
                if (Thread.currentThread().isInterrupted()) {
                    saisieFuture.cancel(true);
                    return null;
                }
            } catch (InterruptedException | ExecutionException | CancellationException e) {
                return null;
            }
        }
    }

    /**
     * Méthode à appeler pour interrompre la saisie bloquante
     * (quand la minuterie se termine)
     */
    public void arreterSaisie() {
        if (saisieFuture != null && !saisieFuture.isDone()) {
            saisieFuture.cancel(true); // demande interruption
        }
        // Ne pas fermer le Executor ici car on peut vouloir rejouer
        // (sinon prévoir de le recréer)
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }

    public void afficherScore(int score) {
        System.out.println("\n=== Fin de la partie ===");
        System.out.println("Votre score est : " + score);
    }

    public void afficherClassement(Classement classement) {
        System.out.println("=== Classement ===");
        if (classement.getListe().isEmpty()) {
            System.out.println("Aucun score enregistré.");
            return;
        }

        for (Enregistrement e : classement.getListe()) {
            System.out.println(e.getPseudo() + " : " + e.getScore());
        }
    }

    public boolean demanderReprise() {
        System.out.print("Voulez-vous reprendre la partie ? (O/N) : ");
        String rep = scanner.nextLine().trim().toLowerCase();
        return rep.equals("o") || rep.equals("oui");
    }
}