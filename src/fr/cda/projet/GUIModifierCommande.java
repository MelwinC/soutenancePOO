package fr.cda.projet;

import fr.cda.ihm.*;
import java.util.ArrayList;

/**
 * Classe de definition de l'IHM secondaire de modification d'une commande
 */
public class GUIModifierCommande implements FormulaireInt {
    private final Site site;
    private final Commande commandeCourante;

    /**
     * Constructeur de l'ihm de modification de commande
     * @param site site courant envoye en parametre
     * @param commande commande a modifier envoye en parametre
     */
    public GUIModifierCommande(Site site, Commande commande) {
        this.site = site;
        this.commandeCourante = commande;

        // Creation du formulaire
        Formulaire form = new Formulaire("Modification de commande" + commande.getNumero(), this, 200, 150);

        // Creation des elements de l'IHM

        // check si la commande possede bien au moins une reference
        if (!commande.getReferences().get(0).isEmpty()) {
            for (String ref : commande.getReferences()) {
                String[] tabReference = ref.split("=", 2);
                String nomReference = tabReference[0];
                String quantite = tabReference[1];
                // creation d'un champ de texte par reference
                form.addText(nomReference, nomReference, true, quantite);
            }
            form.addButton("VALIDER", "Valider");
        } else { // la reference en index 0 n'a rien retourne
            form.addLabel("La commande choisit ne possede pas de produits.");
        }

        // Affichage du formulaire
        form.afficher();
    }

    /**
     * Methode appellee quand on clique dans un bouton
     * @param form Le formulaire dans lequel se trouve le bouton
     * @param nomSubmit Le nom du bouton qui a ete utilise.
     */
    public void submit(Formulaire form, String nomSubmit) {

        // modifie la reference de l'objet commande selectionne par son numero
        if (nomSubmit.equals("VALIDER")) {
            ArrayList<String> listeReferences = new ArrayList<>();
            for (String ref : commandeCourante.getReferences()) {
                String[] tabReference = ref.split("=", 2);
                String nomReference = tabReference[0];

                // on recupere la valeur modifiee par l'utilisateur
                String quantite = form.getValeurChamp(nomReference);

                // on reconstruit la reference avec la nouvelle quantite
                String reference = nomReference + "=" + quantite;

                // on ajoute cette reference dans le nouvel arraylist de references
                listeReferences.add(reference);
            }
            // on assigne les references modifiees a la commande selectionnee
            commandeCourante.setReferences(listeReferences);

            // on refresh les raisons en fonction des modifications apportees
            site.initialiserRaisonsCommandes();
            form.fermer();
        }

    }

}