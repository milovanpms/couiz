package fr.couiz.processus;

public class Processus {

    private int seuilLevenshtein = 2;

    public Processus() {}

    public int getSeuilLevenshtein() {
        return seuilLevenshtein;
    }

    public void setSeuilLevenshtein(int seuilLevenshtein) {
        this.seuilLevenshtein = seuilLevenshtein;
    }

    /**
     * Vérifie si la distance de Levenshtein entre deux chaînes est inférieure ou égale au seuil.
     */
    public boolean levenshtein(String x, String y) {
        return calculateLevenshtein(x, y) <= seuilLevenshtein;
    }

    /**
     * Calcule la distance de Levenshtein entre deux chaînes.
     */
    private int calculateLevenshtein(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                            dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                    );
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
}