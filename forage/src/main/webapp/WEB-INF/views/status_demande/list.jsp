<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Statuts de Demandes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; margin: 0; padding: 20px; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        h1 { color: #333; }
        .btn { display: inline-block; padding: 10px 15px; color: white; background-color: #007bff; text-decoration: none; border-radius: 5px; }
        .btn:hover { background-color: #0056b3; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; color: #333; }
        .nav-links { list-style: none; padding: 0; margin: 0 0 20px 0; display: flex; gap: 15px; }
        .nav-links li a { text-decoration: none; color: #007bff; font-weight: bold; }
        .nav-links li a.active { color: #333; border-bottom: 2px solid #007bff; }
    </style>
</head>
<body>
    <div class="container">
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/demandes">Demandes</a></li>
            <li><a href="${pageContext.request.contextPath}/devis">Devis</a></li>
            <li><a href="${pageContext.request.contextPath}/status-demandes" class="active">Statuts Demandes</a></li>
        </ul>

        <h1>Historique des Statuts de Demandes</h1>
        <a href="${pageContext.request.contextPath}/status-demandes/create" class="btn">Nouveau Statut Demande</a>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Demande (Ref)</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Observation</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="sd" items="${statusDemandes}">
                    <tr>
                        <td>${sd.id}</td>
                        <td>${sd.demande.reference}</td>
                        <td><fmt:formatDate value="${sd.dateStatus}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>${sd.status.libele}</td>
                        <td>${sd.observation}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/status-demandes/edit/${sd.id}" class="btn">Modifier</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
