package fr.couiz.utilitaire;

public class Enregistrement {
    private String pseudo;
    private int score;

    public Enregistrement(String pseudo, int score) {
        this.pseudo = pseudo;
        this.score = score;
    }

    public Enregistrement() {}

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
