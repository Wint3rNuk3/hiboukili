/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.beans.ConnexionBean;
import model.beans.EditionBean;
import model.classes.Edition;

/**
 *
 * @author cdi306
 */
@WebServlet(name = "ReapprovisionnementController", urlPatterns = {"/reapprovisionnement"})
public class ReapprovisionnementController extends HttpServlet {

    private final String INDEX_ROUTE = "/WEB-INF/jsp/reapprovisionnement.jsp";

    private final String PAGE_PARAMETER = "page";
    private final String PER_PAGE_PARAMETER = "perPage";

    private final String PAGE_ATTRIBUTE = "page";
    private final String NB_PAGE_ATTRIBUTE = "nbPage";
    private final String PER_PAGE_ATTRIBUTE = "perPage";
    private final String EDITIONS_ATTRIBUTE = "editions";

    private final int DEFAULT_PAGE = 1;
    private final int DEFAULT_PERPAGE = 6;

    private int perPage;
    private int page;

    @Override
    public void init() throws ServletException {
        super.init();
        this.page = DEFAULT_PAGE;
        this.perPage = DEFAULT_PERPAGE;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // c'est du copié/collé, car on refuse de reutiliser les DAO,
        // et que je n'ai pas le droit de coder.

        // début copié/collé
        // vérifié si un beanConnexion est enregistre ds la session; si non, le cree 
        HttpSession session = request.getSession();
        ConnexionBean bc = (ConnexionBean) session.getAttribute("sessionConnexion");
        if (bc == null) {
            bc = new ConnexionBean();
            session.setAttribute("sessionConnexion", bc);
        }
        // fin copié/collé

        // on demande la liste complète des editions.
        List<Edition> editions = new EditionBean().findAll(bc);

        // traitement du parametre indiquant le nombre d'edition par page.
        if (request.getParameter(PER_PAGE_PARAMETER) != null) {
            perPage = Integer.parseInt(request.getParameter(PER_PAGE_PARAMETER));
        }

        // on transmet ne nombre d'element par page en attribut, 
        // pour l'utiliser dans la jsp.
        request.setAttribute(PER_PAGE_ATTRIBUTE, perPage);

        // on calcule le nombre de page.
        int nbPage = (int) Math.ceil((double) editions.size() / perPage);
        request.setAttribute(NB_PAGE_ATTRIBUTE, nbPage);

        // on reagrde la page demandée.
        if (request.getParameter(PAGE_PARAMETER) != null) {
            page = Integer.parseInt(request.getParameter(PAGE_PARAMETER));

            if (page > nbPage) {
                // Si la page demandée excede le nombre de page,
                // allons a la derniere page.
                page = nbPage;
            }
        }
        EditionBean eb = new EditionBean();
        if (request.getParameter("iwantmoaarr") != null) {
            if (request.getParameter("qty") != null) {
                if (Integer.parseInt(request.getParameter("qty")) <= 0) {
                } else {
                    eb.setStockInDB(bc, request.getParameter("iwantmoaarr"), Integer.parseInt(request.getParameter("qty")));
                }
            }
            else
                System.out.println("oops pas de quantitié renseignée !");
        }

        // on transmet la valeur en attribute pour l'utiliser dans la jsp.
        request.setAttribute(PAGE_ATTRIBUTE, page);

        // on forme une nouvelle liste a partir de l'ensemble.
        List displayed = editions.subList(
                Math.max(page * perPage - perPage, 0), // avoid <0
                Math.min(page * perPage, editions.size())); // avoid >index

        // on transmet la liste des editions a afficher.
        request.setAttribute(EDITIONS_ATTRIBUTE, displayed);

        // la liste des rubriques est chargée depuis le MenuFilter.
//        List<Rubrique> rubriques = new RubriqueBean().findAll(bc);
//        request.setAttribute("rubriques", rubriques);
        // charger la liste des themes ?
        // on fait suivre à la jsp de l'index.
        getServletContext()
                .getRequestDispatcher(INDEX_ROUTE)
                .forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
