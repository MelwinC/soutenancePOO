package fr.cda.projet;

/**
 * Classe de definition d'un produit du stock
 */
public class Produit {

    private String reference;
    private String nom;
    private double prix;
    private int quantite;

    /**
     * @param reference reference du produit
     * @param nom nom du produit
     * @param prix prix du produit
     * @param quantite quantite du produit
     */
    public Produit(String reference, String nom, double prix, int quantite) {
        this.reference = reference;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * override de la methode toString de Object, affiche en String le produit
     */
    @Override
    public String toString() {
        return String.format("%-15s %-55s %3.2f %3d", reference, nom, prix, quantite);
    }

}