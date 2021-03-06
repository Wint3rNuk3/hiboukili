package model.beans;

import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import javax.sql.DataSource;
import model.classes.Commande;
import model.classes.Edition;
import model.classes.Promotion;
import model.classes.Taxe;

public class OrderBean implements Serializable {

    
    HashMap<Long, Commande>commandes;

    public OrderBean() {

        this.commandes = new HashMap();

    }

   

    public void save(ConnexionBean bc, Long idAdresseFacturation, Long idAdresseLivraison, Long idUtilisateur) {
        DataSource ds = bc.MaConnexion();

        try (Connection c = ds.getConnection();) {

            String query = "DECLARE @guid varchar(50);"
                    + " SET @guid=NEWID();"
                    + " INSERT INTO Commande(idAdresseFacturation, idAdresseLivraison, idUtilisateur, numero, dateCommande)"
                    + " VALUES (?, ?, ?, @guid, cast(convert(char(8), GETDATE(), 112) as int));";

            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setLong(1, idAdresseFacturation);
            stmt.setLong(2, idAdresseLivraison);
            stmt.setLong(3, idUtilisateur);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException ex) {
            

            System.err.println("Erreur dans Commande" + ex.getMessage());
        }

        ds = bc.MaConnexion();

    }

//    public void save(ConnexionBean bc, Long idAdresseFacturation, Long idAdresseLivraison, Long idUtilisateur) {
//        int i = 0;
//
//        DataSource ds = bc.MaConnexion();
//
//        try (Connection c = ds.getConnection();) {
//
//            //requete SQL pour sauvegarder la commande dans la base de donnée.
//            String query = "DECLARE @guid varchar(50);"
//                    + " SET @guid= NEWID();"
//                    + " INSERT INTO Commande(idAdresseFacturation, idAdresseLivraison, idUtilisateur, numero, dateCommande)"
//                    + " SELECT ?,?,?,@guid, cast(convert(char(8), GETDATE(), 112) as int)"
//                    + " FROM Utilisateur AS a"
//                    + " INNER JOIN DernieresFacturations AS b"
    //insertion en 4 fois . inner join = sorte de multiplicateur. donc envoie selon le nombre de derniere facturation
//                    + " ON a.IdUtilisateur=b.IdUtilisateur"
//                    + " INNER JOIN DernieresLivraisons AS c"
//                    + " ON a.IdUtilisateur=c.idUtilisateur";
//            
//            PreparedStatement stmt = c.prepareStatement(query);
//
//            stmt.setLong(1, idAdresseFacturation);
//            stmt.setLong(2, idAdresseLivraison);
//            stmt.setLong(3, idUtilisateur);
//
//            int result = stmt.executeUpdate();
//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ result+"/"+idAdresseFacturation
//+"/"+idAdresseLivraison
//+"/"+idUtilisateur);
//
//            stmt.close();
//
//        } catch (SQLException ex) {
//            System.err.println("Erreur dans Commande" + ex.getMessage());
//        }
//
//        ds = bc.MaConnexion();
//
//    }
    public void modifier() {
        // modifier et supprimer : 
        // retour au panier
    }

    public String recupererStatutCommande(ConnexionBean bc, Long idUtilisateur) {

        String statCommande = null;

        DataSource ds = bc.MaConnexion();

        try (Connection c = ds.getConnection();) {

            String query = "SELECT libelle FROM StatutCommande WHERE code = 'cp1'";
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                commandes.put(idUtilisateur, new Commande(rs.getString("libelle")));
            }
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            System.err.println("Erreur dans Statut commande" + ex.getMessage());
        }

        ds = bc.MaConnexion();

        return statCommande;

    }

    public void recupererNumerosCommande(ConnexionBean bc, Long idUtilisateur) {

        DataSource ds = bc.MaConnexion();

        try (Connection c = ds.getConnection();) {

            String query = "SELECT numero FROM Commande WHERE idUtilisateur = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setLong(1, idUtilisateur);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
               //Commande com = new Commande(res.getString("numero"));
                commandes.put(idUtilisateur, new Commande(res.getString("numero")));
            }

            res.close();
            stmt.close();

        } catch (SQLException ex) {
            System.err.println("Erreur dans Statut commande" + ex.getMessage());
        }

        ds = bc.MaConnexion();

    }

    public Collection<Commande> list() {
        return commandes.values();
    }

    public int size() {
        return commandes.size();
    }

    public boolean isEmpty() {
        return commandes.isEmpty();
    }

    public void clean() {
        commandes.clear();
        //apres la validation final : clean map
    }
    
    
   
    
    // calcul total du prix du panier 
    public String calculTotal(Collection<Edition> list){
        Float prixTotal = 0F;
        
        if (!(list().isEmpty())) {
            for (Edition e : list) {
                if (e.getPrixHt() != null) {
                    
                    prixTotal += (Float.parseFloat(e.getPrix())) * (e.getCartQty());
                }
            }
        }

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.FRENCH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", otherSymbols);
        df.setRoundingMode(RoundingMode.HALF_UP);

        //System.out.println(df.format(prixTotal));
        return df.format(prixTotal);
    }
    
    
    // calcul de la quantite total. 
    public String calculQty(Collection<Edition> list){
        int qtyTotal = 0;
        if(!(list().isEmpty())){
            for(Edition e : list){
                if(e.getCartQty() != 0){
                    
                    qtyTotal += (e.getCartQty());
                    
                }
            }
        }
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.FRENCH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("0", otherSymbols);
        df.setRoundingMode(RoundingMode.HALF_UP);

        //System.out.println(df.format(prixTotal));
        return df.format(qtyTotal);
    }

}
