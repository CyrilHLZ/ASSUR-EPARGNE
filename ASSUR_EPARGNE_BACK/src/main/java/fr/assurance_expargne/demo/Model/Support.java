package fr.assurance_expargne.demo.Model;

/**
 * Le support d'épargne sur lequel sont versés les bonus et les versements libres.
 * Chaque support a son taux de prélèvements sociaux sur les intérêts (taux 2026).
 */
public enum Support {

    // Disponible à tout moment ; exclue de la hausse de CSG de 2026
    ASSURANCE_VIE(0.172),

    // Bloqué jusqu'à la retraite (sauf cas légaux) ; touché par la hausse de CSG de 2026
    PER(0.186);

    private final double tauxPrelevementsSociaux;

    Support(double tauxPrelevementsSociaux) {
        this.tauxPrelevementsSociaux = tauxPrelevementsSociaux;
    }

    public double tauxPrelevementsSociaux() {
        return tauxPrelevementsSociaux;
    }
}
