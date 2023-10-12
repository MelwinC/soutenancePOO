package fr.cda.projet;

import fr.cda.ihm.*;
import java.util.ArrayList;

/**
 * Classe de definition de l'IHM secondaire de modification des quantites du stock
 */
public class GUIModifierStock implements FormulaireInt {
    private final Site site;
    private final ArrayList<Produit> stock;

    /**
     * Constructeur de l'IHM de modification du stock
     * @param site courant envoye en parametre
     */
    public GUIModifierStock(Site site) {
        this.site = site;
        // on recupere le stock actuel
        stock = site.getStock();

        // Creation du formulaire
        Formulaire form = new Formulaire("Modification du stock", this, 600, 400);

        for (Produit p : stock) {
            // on liste tout le stock
            form.addText(p.getReference(), p.getReference() + " " + p.getNom() + " " + p.getPrix(),true,String.valueOf(p.getQuantite()));
        }
        form.addButton("VALIDER", "Valider");

        // Affichage du formulaire
        form.afficher();
    }

    public void submit(Formulaire form, String nomSubmit) {
        if (nomSubmit.equals("VALIDER")) {
            for (Produit p : stock) {
                // on reassigne la valeur quantite du produit avec la valeur saisie par l'utilisateur
                p.setQuantite(Integer.parseInt(form.getValeurChamp(p.getReference())));
            }
            // on reattribue ces nouvelles valeurs a notre vrai stock
            site.setStock(stock);
            // on refresh les raisons des commanndes afin qu'on puisse voir les modifications apportees
            site.initialiserRaisonsCommandes();
            form.fermer();
        }

    }

}