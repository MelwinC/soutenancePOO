package fr.cda.ihm;

/** Interface d'utilisation d'un formulaire.<br>
    L'applicatif passer en paramètre du formulaire doit 
    implémenter cette interface. */
public interface FormulaireInt
{
    /** Cette méthode est appelée lors de l'utilsation d'un bouton.<br>
        L'appel à form.getValeurChamp permet de récupérer les valeurs des champs.<br>
        L'appel à form.setValeurChamp permet de changer les valeurs des champs.<br>
        @param form Le formulaire dans lequel se trouve le bouton
        @param nom Le nom du bouton qui a été utilisé.
     */
    public void     submit(Formulaire form,String nom);
}
