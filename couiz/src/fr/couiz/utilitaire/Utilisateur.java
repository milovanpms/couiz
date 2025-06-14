package fr.couiz.utilitaire;

public class Utilisateur {
    private String pseudo;
    private String reponseCourante; // ajoutée pour stocker la réponse donnée par l'utilisateur

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getReponseCourante() {
        return reponseCourante;
    }

    public void setReponseCourante(String reponseCourante) {
        this.reponseCourante = reponseCourante;
    }

    public Utilisateur(String pseudo) {
        this.pseudo = pseudo;
    }
}