package fr.cda.projet;

import fr.cda.ihm.*;

// Classe de definition de l'IHM principale du compte
public class GUISite implements FormulaireInt {
    private Site site;  // Le site

    // Constructeur
    public GUISite(Site site) {
        this.site = site;

        // Creation du formulaire
        Formulaire form = new Formulaire("Site de vente", this, 1100, 730);

        //  Creation des elements de l'IHM
        form.addLabel("Afficher tous les produits du stock");
        form.addButton("AFF_STOCK", "Tous le stock");
        form.addLabel("");
        form.addLabel("Afficher tous les bons de commande");
        form.addButton("AFF_COMMANDES", "Toutes les commandes");
        form.addLabel("");
        form.addButton("NON_LIVREES", "Commandes non livrees");
        form.addLabel("");
        form.addText("NUM_COMMANDE", "Numero de commande", true, "1");
        form.addButton("AFF_COMMANDE", "Afficher");
        form.addLabel("");
        form.addButton("MODIFIER", "Modifier");
        form.addLabel("");
        form.addButton("LIVRER", "Livrer");
        form.addLabel("");
        form.addButton("CALCUL_VENTES", "Calculer ventes");

        form.setPosition(400, 0);
        form.addZoneText("RESULTATS", "Resultats",
                true,
                "",
                600, 700);

        // Affichage du formulaire
        form.afficher();
    }

    // Methode appellee quand on clique dans un bouton
    public void submit(Formulaire form, String nomSubmit) {

        // Affichage de tous les produits du stock
        if (nomSubmit.equals("AFF_STOCK")) {
            String res = site.listerTousProduits();
            form.setValeurChamp("RESULTATS", res);
        }

        // Affichage de toutes les commandes
        if (nomSubmit.equals("AFF_COMMANDES")) {
            form.setValeurChamp("RESULTATS", site.listerToutesCommandes());
        }

        // Affichage d'une commande par son numero
        if (nomSubmit.equals("AFF_COMMANDE")) {
            String numStr = form.getValeurChamp("NUM_COMMANDE");
            try {
                int num = Integer.parseInt(numStr);
                String res = site.listerCommande(num);
                form.setValeurChamp("RESULTATS", res);
            } catch (NumberFormatException e) {
                String res = "Veuillez entrer un numéro de commande.";
                form.setValeurChamp("RESULTATS", res);
            }
        }

        // appel de l'IHM de modification de commande
        if (nomSubmit.equals("MODIFIER")) {
            String numStr = form.getValeurChamp("NUM_COMMANDE");
            try {
                int num = Integer.parseInt(numStr);
                Commande commande = site.trouverCommande(num);
                if (!commande.isEtatLivraison()) {
                    GUIModifierCommande gui = new GUIModifierCommande(site, commande);
                } else {
                    form.setValeurChamp("RESULTATS", "Cette commande a deja ete livree, impossible de l'a modifier.");
                }
            } catch (NullPointerException | NumberFormatException e) {
                form.setValeurChamp("RESULTATS", "Aucune commande n'a ete trouvee avec ce numero.");
                e.printStackTrace();
            }
        }

        // Affichage de toutes les commandes non livrees
        if (nomSubmit.equals("NON_LIVREES")) {
            form.setValeurChamp("RESULTATS", site.commandesNonLivrees());
        }

        // Livraison de la commande
        if (nomSubmit.equals("LIVRER")) {
//            Commande c = site.trouverCommande(Integer.parseInt(form.getValeurChamp("NUM_COMMANDE")));
            try {
                String res = site.livrerToutesCommandes();
                form.setValeurChamp("RESULTATS", res);
            } catch (NullPointerException | NumberFormatException e) {
                form.setValeurChamp("RESULTATS", "Aucune commande n'a ete trouvee avec ce numero.");
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e2) {
                form.setValeurChamp("RESULTATS", "La commande ne possede pas de references.");
            }
        }

        if (nomSubmit.equals("CALCUL_VENTES")) {
            int numCommande = Integer.parseInt(form.getValeurChamp("NUM_COMMANDE"));
            Commande c = site.trouverCommande(numCommande);
            String res = site.calculVente(c);
            form.setValeurChamp("RESULTATS", res);
        }

    }

}