package fr.cda.projet;

import fr.cda.ihm.*;
import java.util.ArrayList;

// Classe de definition de l'IHM principale du compte
public class GUIModifierCommande implements FormulaireInt {
    private Site site;  // Le site
    private Commande commandeCourante;

    // Constructeur
    public GUIModifierCommande(Site site, Commande commande) {
        this.site = site;
        this.commandeCourante = commande;

        // Creation du formulaire
        Formulaire form = new Formulaire("Site de vente", this, 300, 300);

        //  Creation des elements de l'IHM
        int nbReferences = commandeCourante.getReferences().size();
        if (!commande.getReferences().get(0).isEmpty()) {
            for (int i = 0; i < nbReferences; i++) {
                String referenceCourante = commandeCourante.getReferences().get(i);
                String[] tabReference = referenceCourante.split("=", 2);
                String nomReference = tabReference[0];
                String quantite = tabReference[1];
                form.addText(nomReference, nomReference, true, quantite);
            }
            form.addButton("VALIDER", "Valider");
        } else {
            form.addLabel("La commande choisit ne possede pas de produits.");
        }

        // Affichage du formulaire
        form.afficher();
    }

    // Methode appellee quand on clique dans un bouton
    public void submit(Formulaire form, String nomSubmit) {
        // modifie la reference de l'objet commande selectionne par son numero
        if (nomSubmit.equals("VALIDER")) {
            ArrayList<String> listeReferences = new ArrayList<>();
            int nbRefenrences = this.commandeCourante.getReferences().size();
            for (int i = 0; i < nbRefenrences; i++) {
                String referenceCourante = this.commandeCourante.getReferences().get(i);
                String[] tabReference = referenceCourante.split("=", 2);
                String nomReference = tabReference[0];
                String quantite = form.getValeurChamp(nomReference);
                String reference = nomReference + "=" + quantite;
                listeReferences.add(reference);
            }
            commandeCourante.setReferences(listeReferences);
            site.initialiserRaisonsCommandes();
            form.fermer();
        }

    }

}