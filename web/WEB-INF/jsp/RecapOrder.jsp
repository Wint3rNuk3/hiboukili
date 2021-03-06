<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.classes.Edition" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template title="Récapitulatif">

    <jsp:attribute name="styles">
       
    </jsp:attribute>

    <jsp:body> 
        
                <jsp:useBean class="model.beans.ShoppingCartBean" id="cart" scope="session" />
                <form action="order" method="Post">
                    
                    <div class="container">
                        <div class="row">
                            <div class="well col-xs-10 col-sm-10 col-md-6 col-xs-offset-1 col-sm-offset-1 col-md-offset-3">
                                <div class="row">
                                    <div class="col-xs-6 col-sm-6 col-md-6">
                                        <address>
                                            <h1>
                                                <strong>Votre Panier</strong>
                                            </h1>
                                            <br> 
                                        </address>
                                    </div>
                                    <div class="col-xs-6 col-sm-6 col-md-6 text-right">
                                        

                                    </div>
                                </div>

                                </span>
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Produit</th>

                                            <th class="text-center">PrixHt</th>
                                            <th class="text-center">Quantité</th>
                                        </tr>
                                    </thead>

                                    <c:choose> 
                                        <c:when test="${cart.list().size() == 0}}">
                                            <div class="row">
                                                <div class="col-xs-2">
                                                </div>
                                                <div class="col-xs-4">
                                                </div>
                                                <div class="col-xs-6">
                                                    <div class="col-xs-4">
                                                    </div>
                                                    <div class="col-xs-4">
                                                        <span>Panier vide !</span>
                                                    </div>
                                                    <div class="col-xs-2">          
                                                    </div>
                                                    <div class="col-xs-2">
                                                    </div>
                                                </div>
                                            </div>
                                            <hr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${cart.list()}" var="e">
                                                <tr>
                                                    <td class="col-md-9"><em>${e.isbn}</em></h4></td>
                                                    <td class="col-md-1 text-center">${e.prixHt}</td>
                                                    <td class="col-md-1 text-center">${e.cartQty}</td>
                                                    <td>   </td>
                                                </tr>

                                                <c:choose>
                                                    <c:when test="${ e.getTVA() == null }">

                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td>   </td>
                                                            <td>   </td>
                                                            <td class="text-left">

                                                                <p>
                                                                    <strong>${ e.getTVA().getLibelle() }: </strong>
                                                                </p></td>
                                                            <td class="text-center">
                                                                <p>
                                                                    <br>
                                                                    <strong>${ e.getTVA().getValeur() } </strong>
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                                <tr>
                                                    <td>   </td>
                                                    <td>   </td>
                                                    <td class="text-right"><h4><strong>Total: </strong></h4></td>
                                                    <td class="text-center text-danger"><h4><strong>${ e.getPrix() * e.getCartQty() }</strong></h4></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>

                                </table>


                                <p align="center">
                                   
                                    <input type='submit' value='Modifier' name='modif'/>
                                    
                                    
                                    
                                    <input type='submit' name='valid' value='Valider'>
                                    <input type="hidden" name="section" value="finalOrder" />
                                    

                                   
                                    
                            </div>
                        </div>
                    </div>
                </p>
            </form>
        
    </html>
</jsp:body>
</t:template>
